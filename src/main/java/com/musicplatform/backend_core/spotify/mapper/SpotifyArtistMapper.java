package com.musicplatform.backend_core.spotify.mapper;

import com.musicplatform.backend_core.spotify.dto.external.SpotifyArtistObject;
import com.musicplatform.backend_core.spotify.dto.response.SpotifyArtistResponse;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class SpotifyArtistMapper {

    public SpotifyArtistResponse toResponse(SpotifyArtistObject artist) {
        return new SpotifyArtistResponse(
                artist.getId(),
                artist.getName(),
                resolveImageUrl(artist),
                artist.getGenres() != null ? artist.getGenres() : List.of(),
                resolveFollowers(artist),
                artist.getPopularity(),
                resolveSpotifyUrl(artist)
        );
    }

    private String resolveImageUrl(SpotifyArtistObject artist) {
        if (artist.getImages() == null || artist.getImages().isEmpty()) {
            return null;
        }
        return artist.getImages().get(0).getUrl();
    }

    private Integer resolveFollowers(SpotifyArtistObject artist) {
        return artist.getFollowers() != null ? artist.getFollowers().getTotal() : null;
    }

    private String resolveSpotifyUrl(SpotifyArtistObject artist) {
        return artist.getExternalUrls() != null ? artist.getExternalUrls().getSpotify() : null;
    }
}
