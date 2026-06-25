package com.musicplatform.backend_core.spotify.service;

import com.musicplatform.backend_core.spotify.dto.external.SpotifySearchResponse;
import com.musicplatform.backend_core.spotify.dto.response.SpotifyArtistResponse;
import com.musicplatform.backend_core.spotify.exception.SpotifyException;
import com.musicplatform.backend_core.spotify.mapper.SpotifyArtistMapper;
import java.net.URI;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
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

    private final SpotifyAuthService spotifyAuthService;
    private final SpotifyArtistMapper spotifyArtistMapper;
    private final RestTemplate restTemplate;

    public SpotifyService(
            SpotifyAuthService spotifyAuthService,
            SpotifyArtistMapper spotifyArtistMapper,
            RestTemplate restTemplate
    ) {
        this.spotifyAuthService = spotifyAuthService;
        this.spotifyArtistMapper = spotifyArtistMapper;
        this.restTemplate = restTemplate;
    }

    /**
     * Busca artistas en Spotify por nombre.
     *
     * @param query texto de búsqueda
     * @param limit número máximo de resultados (1-50)
     * @return lista de artistas mapeados
     */
    public List<SpotifyArtistResponse> searchArtists(String query, int limit) {
        try {
            String token = spotifyAuthService.getAccessToken();

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<Void> request = new HttpEntity<>(headers);

            URI uri = UriComponentsBuilder.fromUriString(SPOTIFY_SEARCH_URL)
                    .queryParam("q", query)
                    .queryParam("type", "artist")
                    .queryParam("limit", limit)
                    .build()
                    .encode()
                    .toUri();

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

            return body.getArtists().getItems().stream()
                    .map(spotifyArtistMapper::toResponse)
                    .toList();

        } catch (HttpClientErrorException e) {
            log.error("Error HTTP al buscar artistas en Spotify: {}", e.getStatusCode());
            throw new SpotifyException("Error al buscar artistas en Spotify: " + e.getStatusCode(), e);
        } catch (SpotifyException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al buscar artistas en Spotify", e);
            throw new SpotifyException("Error al buscar artistas en Spotify: " + e.getMessage(), e);
        }
    }
}
