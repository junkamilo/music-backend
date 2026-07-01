package com.musicplatform.backend_core.spotify.dto.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpotifySimplifiedArtistObject {

    private String id;
    private String name;
    private String href;
    private String type;
    private String uri;
    private List<SpotifyImage> images;

    @JsonProperty("external_urls")
    private SpotifyExternalUrls externalUrls;
}
