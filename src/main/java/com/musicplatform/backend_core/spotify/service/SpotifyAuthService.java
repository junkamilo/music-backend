package com.musicplatform.backend_core.spotify.service;

import com.musicplatform.backend_core.config.SpotifyProperties;
import com.musicplatform.backend_core.spotify.dto.token.SpotifyTokenResponse;
import com.musicplatform.backend_core.spotify.exception.SpotifyException;
import java.util.Base64;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * Servicio para obtener y cachear tokens de Spotify (Client Credentials Flow).
 *
 * <p>Flujo:
 * <ol>
 *   <li>Envía Client ID + Secret a Spotify (autenticación).</li>
 *   <li>Spotify devuelve un Access Token válido por ~1 hora.</li>
 *   <li>Se cachea el token para evitar llamadas innecesarias.</li>
 *   <li>Cuando expira (con margen de seguridad), pide uno nuevo.</li>
 * </ol>
 */
@Service
@Slf4j
public class SpotifyAuthService {

    private static final String SPOTIFY_TOKEN_URL = "https://accounts.spotify.com/api/token";
    private static final long EXPIRY_SAFETY_MARGIN_SECONDS = 300L;

    private final SpotifyProperties spotifyProperties;
    private final RestTemplate restTemplate;

    private String cachedToken;
    private long tokenExpiryTime;

    public SpotifyAuthService(SpotifyProperties spotifyProperties, RestTemplate restTemplate) {
        this.spotifyProperties = spotifyProperties;
        this.restTemplate = restTemplate;
        this.tokenExpiryTime = 0;
    }

    /**
     * Devuelve un Access Token válido, reutilizando el cache si no ha expirado.
     */
    public synchronized String getAccessToken() {
        if (isCachedTokenValid()) {
            log.debug("Token en cache, reutilizando (vence en {} ms)",
                    tokenExpiryTime - System.currentTimeMillis());
            return cachedToken;
        }

        log.info("Token expirado o inexistente, solicitando uno nuevo a Spotify...");
        return fetchNewToken();
    }

    private boolean isCachedTokenValid() {
        return cachedToken != null && System.currentTimeMillis() < tokenExpiryTime;
    }

    private String fetchNewToken() {
        try {
            String credentials = spotifyProperties.getId() + ":" + spotifyProperties.getSecret();
            String encodedCredentials = Base64.getEncoder()
                    .encodeToString(credentials.getBytes());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.set("Authorization", "Basic " + encodedCredentials);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("grant_type", "client_credentials");

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

            ResponseEntity<SpotifyTokenResponse> response = restTemplate.postForEntity(
                    SPOTIFY_TOKEN_URL,
                    request,
                    SpotifyTokenResponse.class
            );

            if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
                throw new SpotifyException(
                        "Spotify devolvió un estado inválido: " + response.getStatusCode());
            }

            SpotifyTokenResponse tokenResponse = response.getBody();

            this.cachedToken = tokenResponse.getAccessToken();
            this.tokenExpiryTime = System.currentTimeMillis()
                    + ((tokenResponse.getExpiresIn() - EXPIRY_SAFETY_MARGIN_SECONDS) * 1000L);

            log.info("Token de Spotify obtenido. Expira en {} segundos", tokenResponse.getExpiresIn());

            return cachedToken;

        } catch (HttpClientErrorException.Unauthorized e) {
            log.error("Error de autenticación con Spotify: Client ID o Secret inválidos");
            throw new SpotifyException(
                    "Credenciales de Spotify inválidas. Verifica tu Client ID y Secret", e);
        } catch (HttpClientErrorException e) {
            log.error("Error HTTP de Spotify: {}", e.getStatusCode());
            throw new SpotifyException("Error en la API de Spotify: " + e.getStatusCode(), e);
        } catch (SpotifyException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al conectar con Spotify", e);
            throw new SpotifyException("Error al obtener token de Spotify: " + e.getMessage(), e);
        }
    }
}
