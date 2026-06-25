package com.musicplatform.backend_core.spotify.dto.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpotifyArtistObject {

    private String id;
    private String name;
    private List<String> genres;
    private Integer popularity;
    private List<SpotifyImage> images;
    private SpotifyFollowers followers;

    @JsonProperty("external_urls")
    private SpotifyExternalUrls externalUrls;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SpotifyExternalUrls {
        private String spotify;
    }
}
