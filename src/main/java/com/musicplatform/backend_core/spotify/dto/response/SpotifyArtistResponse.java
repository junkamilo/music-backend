package com.musicplatform.backend_core.spotify.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.musicplatform.backend_core.spotify.dto.external.SpotifyExternalUrls;
import com.musicplatform.backend_core.spotify.dto.external.SpotifyImage;
import java.util.List;

/**
 * Respuesta pública alineada con los campos estables del Artist Object de Spotify.
 *
 * @see <a href="https://developer.spotify.com/documentation/web-api/reference/get-an-artist">Get Artist</a>
 */
public record SpotifyArtistResponse(
        @JsonProperty("external_urls") SpotifyExternalUrls externalUrls,
        String href,
        String id,
        List<SpotifyImage> images,
        String name,
        String type,
        String uri
) {
}
