package com.musicplatform.backend_core.spotify.mapper;

import com.musicplatform.backend_core.spotify.dto.external.SpotifyExternalUrls;
import com.musicplatform.backend_core.spotify.dto.external.SpotifyFullArtistObject;
import com.musicplatform.backend_core.spotify.dto.external.SpotifyImage;
import com.musicplatform.backend_core.spotify.dto.external.SpotifySimplifiedArtistObject;
import com.musicplatform.backend_core.spotify.dto.response.SpotifyArtistResponse;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class SpotifyArtistMapper {

    private static final String ARTIST_TYPE = "artist";

    public SpotifyArtistResponse toResponse(SpotifyFullArtistObject artist) {
        return new SpotifyArtistResponse(
                resolveExternalUrls(artist.getExternalUrls()),
                artist.getHref(),
                artist.getId(),
                resolveImages(artist.getImages()),
                artist.getName(),
                resolveType(artist.getType()),
                artist.getUri()
        );
    }

    /**
     * Mapeo de respaldo cuando el endpoint de artistas completos no está disponible (p. ej. Dev Mode).
     */
    public SpotifyArtistResponse toResponseFromSimplified(SpotifySimplifiedArtistObject artist) {
        return new SpotifyArtistResponse(
                resolveExternalUrls(artist.getExternalUrls()),
                artist.getHref(),
                artist.getId(),
                resolveImages(artist.getImages()),
                artist.getName(),
                resolveType(artist.getType()),
                artist.getUri()
        );
    }

    private SpotifyExternalUrls resolveExternalUrls(SpotifyExternalUrls externalUrls) {
        return externalUrls != null ? externalUrls : new SpotifyExternalUrls();
    }

    private List<SpotifyImage> resolveImages(List<SpotifyImage> images) {
        return images != null ? images : List.of();
    }

    private String resolveType(String type) {
        return type != null ? type : ARTIST_TYPE;
    }
}
