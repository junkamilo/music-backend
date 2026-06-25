package com.musicplatform.backend_core.spotify.dto.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpotifyImage {

    private String url;
    private Integer height;
    private Integer width;
}
