package com.musicplatform.backend_core.spotify.service;

import com.musicplatform.backend_core.config.SpotifyProperties;
import com.musicplatform.backend_core.spotify.dto.external.SpotifyFullArtistObject;
import com.musicplatform.backend_core.spotify.dto.external.SpotifyGetSeveralArtistsResponse;
import com.musicplatform.backend_core.spotify.dto.external.SpotifySearchResponse;
import com.musicplatform.backend_core.spotify.dto.external.SpotifySimplifiedArtistObject;
import com.musicplatform.backend_core.spotify.dto.response.SpotifyArtistResponse;
import com.musicplatform.backend_core.spotify.exception.SpotifyException;
import com.musicplatform.backend_core.spotify.mapper.SpotifyArtistMapper;
import com.musicplatform.backend_core.spotify.util.SpotifyApiErrorParser;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Servicio para consumir la API pública de Spotify usando el token cacheado.
 */
@Service
@Slf4j
public class SpotifyService {

    private static final String SPOTIFY_SEARCH_URL = "https://api.spotify.com/v1/search";
    private static final String SPOTIFY_ARTISTS_URL = "https://api.spotify.com/v1/artists";
    private static final int MAX_ARTISTS_PER_BATCH = 50;

    private final SpotifyAuthService spotifyAuthService;
    private final SpotifyArtistMapper spotifyArtistMapper;
    private final SpotifyProperties spotifyProperties;
    private final RestTemplate restTemplate;

    public SpotifyService(
            SpotifyAuthService spotifyAuthService,
            SpotifyArtistMapper spotifyArtistMapper,
            SpotifyProperties spotifyProperties,
            RestTemplate restTemplate
    ) {
        this.spotifyAuthService = spotifyAuthService;
        this.spotifyArtistMapper = spotifyArtistMapper;
        this.spotifyProperties = spotifyProperties;
        this.restTemplate = restTemplate;
    }

    /**
     * Obtiene el Artist Object completo de Spotify (uso interno: sync, admin).
     */
    public SpotifyFullArtistObject getFullArtistById(String spotifyArtistId) {
        try {
            String token = spotifyAuthService.getAccessToken();
            HttpEntity<Void> request = authorizedRequest(token);

            URI uri = UriComponentsBuilder.fromUriString(SPOTIFY_ARTISTS_URL)
                    .pathSegment(spotifyArtistId)
                    .build()
                    .encode()
                    .toUri();

            log.debug("Spotify get full artist URI: {}", uri);

            ResponseEntity<SpotifyFullArtistObject> response = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    request,
                    SpotifyFullArtistObject.class
            );

            if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
                throw new SpotifyException(
                        "Spotify devolvió un estado inválido: " + response.getStatusCode());
            }

            return response.getBody();
        } catch (HttpClientErrorException e) {
            String spotifyMessage = SpotifyApiErrorParser.extractMessage(e);
            log.error("Error HTTP de Spotify al obtener artista completo: {} — {}", e.getStatusCode(), spotifyMessage);
            throw buildSpotifyHttpException("obtener artista", e, spotifyMessage);
        } catch (SpotifyException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al obtener artista completo en Spotify", e);
            throw new SpotifyException("Error al obtener artista en Spotify: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene un artista por su Spotify ID ({@code GET /v1/artists/{id}}).
     */
    public SpotifyArtistResponse getArtistById(String spotifyArtistId) {
        return spotifyArtistMapper.toResponse(getFullArtistById(spotifyArtistId));
    }

    /**
     * Busca artistas en Spotify por nombre y enriquece con datos completos.
     */
    public List<SpotifyArtistResponse> searchArtists(String query, int limit) {
        try {
            List<SpotifySimplifiedArtistObject> searchResults = searchSimplifiedArtists(query, limit);
            if (searchResults.isEmpty()) {
                return List.of();
            }

            List<String> ids = searchResults.stream()
                    .map(SpotifySimplifiedArtistObject::getId)
                    .filter(Objects::nonNull)
                    .toList();

            try {
                return fetchArtistsByIds(ids).stream()
                        .map(spotifyArtistMapper::toResponse)
                        .toList();
            } catch (HttpClientErrorException enrichmentError) {
                if (enrichmentError.getStatusCode() == HttpStatus.FORBIDDEN) {
                    log.warn(
                            "Enriquecimiento de artistas bloqueado por Spotify (403). "
                                    + "Devolviendo resultados de búsqueda simplificados. Detalle: {}",
                            SpotifyApiErrorParser.extractMessage(enrichmentError)
                    );
                    return searchResults.stream()
                            .map(spotifyArtistMapper::toResponseFromSimplified)
                            .toList();
                }
                throw enrichmentError;
            }
        } catch (HttpClientErrorException e) {
            String spotifyMessage = SpotifyApiErrorParser.extractMessage(e);
            log.error("Error HTTP de Spotify al buscar artistas: {} — {}", e.getStatusCode(), spotifyMessage);
            throw buildSpotifyHttpException("buscar artistas", e, spotifyMessage);
        } catch (SpotifyException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al buscar artistas en Spotify", e);
            throw new SpotifyException("Error al buscar artistas en Spotify: " + e.getMessage(), e);
        }
    }

    private List<SpotifySimplifiedArtistObject> searchSimplifiedArtists(String query, int limit) {
        String token = spotifyAuthService.getAccessToken();
        HttpEntity<Void> request = authorizedRequest(token);

        URI uri = UriComponentsBuilder.fromUriString(SPOTIFY_SEARCH_URL)
                .queryParam("q", query)
                .queryParam("type", "artist")
                .queryParam("limit", limit)
                .queryParam("market", spotifyProperties.getMarket())
                .build()
                .encode()
                .toUri();

        log.debug("Spotify search URI: {}", uri);

        ResponseEntity<SpotifySearchResponse> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                request,
                SpotifySearchResponse.class
        );

        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            throw new SpotifyException(
                    "Spotify devolvió un estado inválido: " + response.getStatusCode());
        }

        SpotifySearchResponse body = response.getBody();
        if (body.getArtists() == null || body.getArtists().getItems() == null) {
            return List.of();
        }

        return body.getArtists().getItems();
    }

    private List<SpotifyFullArtistObject> fetchArtistsByIds(List<String> ids) {
        List<SpotifyFullArtistObject> artists = new ArrayList<>();

        for (int start = 0; start < ids.size(); start += MAX_ARTISTS_PER_BATCH) {
            int end = Math.min(start + MAX_ARTISTS_PER_BATCH, ids.size());
            List<String> batch = ids.subList(start, end);
            artists.addAll(fetchArtistBatch(batch));
        }

        return reorderByIds(ids, artists);
    }

    private List<SpotifyFullArtistObject> fetchArtistBatch(List<String> ids) {
        String token = spotifyAuthService.getAccessToken();
        HttpEntity<Void> request = authorizedRequest(token);

        URI uri = UriComponentsBuilder.fromUriString(SPOTIFY_ARTISTS_URL)
                .queryParam("ids", String.join(",", ids))
                .build()
                .encode()
                .toUri();

        log.debug("Spotify artists batch URI: {}", uri);

        ResponseEntity<SpotifyGetSeveralArtistsResponse> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                request,
                SpotifyGetSeveralArtistsResponse.class
        );

        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            throw new SpotifyException(
                    "Spotify devolvió un estado inválido: " + response.getStatusCode());
        }

        List<SpotifyFullArtistObject> batchArtists = response.getBody().getArtists();
        return batchArtists != null ? batchArtists : List.of();
    }

    private List<SpotifyFullArtistObject> reorderByIds(
            List<String> orderedIds,
            List<SpotifyFullArtistObject> artists
    ) {
        Map<String, SpotifyFullArtistObject> artistsById = new HashMap<>();
        for (SpotifyFullArtistObject artist : artists) {
            if (artist != null && artist.getId() != null) {
                artistsById.put(artist.getId(), artist);
            }
        }

        return orderedIds.stream()
                .map(artistsById::get)
                .filter(Objects::nonNull)
                .toList();
    }

    private HttpEntity<Void> authorizedRequest(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return new HttpEntity<>(headers);
    }

    private SpotifyException buildSpotifyHttpException(
            String operation,
            HttpClientErrorException exception,
            String spotifyMessage
    ) {
        String message = "Error al " + operation + " en Spotify: " + spotifyMessage;

        if (exception.getStatusCode() == HttpStatus.FORBIDDEN) {
            message += ". Verifica en developer.spotify.com/dashboard que la cuenta del desarrollador "
                    + "tenga Spotify Premium activo (requerido desde feb 2026 en Development Mode).";
        }

        return new SpotifyException(message, exception);
    }
}
