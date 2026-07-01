package com.musicplatform.backend_core.spotify.dto.external;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;



/**

 * Objeto followers del Artist Object de Spotify.

 *

 * <p>Campo obsoleto en la Web API de Spotify; se deserializa pero no se expone en la API pública.

 */

@Data

@JsonIgnoreProperties(ignoreUnknown = true)

public class SpotifyFollowers {



    /**

     * Siempre {@code null} por diseño de Spotify; el enlace a la lista de seguidores no está disponible.

     */

    private String href;



    /**

     * Número total de seguidores. Obsoleto según la documentación de Spotify Web API.

     */

    private Integer total;

}

