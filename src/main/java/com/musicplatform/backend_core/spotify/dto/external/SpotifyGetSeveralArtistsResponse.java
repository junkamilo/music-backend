package com.musicplatform.backend_core.spotify.dto.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpotifyGetSeveralArtistsResponse {

    private List<SpotifyFullArtistObject> artists;
}
