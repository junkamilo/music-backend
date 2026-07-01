package com.musicplatform.backend_core.spotify.dto.external;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import lombok.Data;



/**

 * Deserialización del Artist Object completo de Spotify ({@code GET /v1/artists/{id}}).

 *

 * @see <a href="https://developer.spotify.com/documentation/web-api/reference/get-an-artist">Get Artist</a>

 */

@Data

@JsonIgnoreProperties(ignoreUnknown = true)

public class SpotifyFullArtistObject {



    private String id;

    private String name;

    private String href;

    private String type;

    private String uri;

    private List<SpotifyImage> images;



    @JsonProperty("external_urls")

    private SpotifyExternalUrls externalUrls;



    /**

     * Obsoleto según la documentación de Spotify Web API. Se deserializa pero no se expone en la API pública.

     */

    private List<String> genres;



    /**

     * Obsoleto según la documentación de Spotify Web API. Se deserializa pero no se expone en la API pública.

     */

    private Integer popularity;



    /**

     * Obsoleto según la documentación de Spotify Web API. Se deserializa pero no se expone en la API pública.

     */

    private SpotifyFollowers followers;

}

