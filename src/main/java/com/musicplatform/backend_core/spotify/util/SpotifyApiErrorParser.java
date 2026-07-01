package com.musicplatform.backend_core.spotify.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.HttpClientErrorException;

public final class SpotifyApiErrorParser {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private SpotifyApiErrorParser() {
    }

    public static String extractMessage(HttpClientErrorException exception) {
        String body = exception.getResponseBodyAsString();
        if (body == null || body.isBlank()) {
            return exception.getStatusCode() + " " + exception.getStatusText();
        }

        try {
            JsonNode root = OBJECT_MAPPER.readTree(body);
            JsonNode errorNode = root.get("error");
            if (errorNode != null && errorNode.has("message")) {
                return errorNode.get("message").asText();
            }
        } catch (Exception ignored) {
            // Usar body crudo si no es JSON de Spotify
        }

        return body;
    }
}
