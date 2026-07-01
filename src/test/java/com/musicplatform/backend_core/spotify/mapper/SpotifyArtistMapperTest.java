package com.musicplatform.backend_core.spotify.mapper;



import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertNull;



import com.musicplatform.backend_core.spotify.dto.external.SpotifyExternalUrls;

import com.musicplatform.backend_core.spotify.dto.external.SpotifyFollowers;

import com.musicplatform.backend_core.spotify.dto.external.SpotifyFullArtistObject;

import com.musicplatform.backend_core.spotify.dto.external.SpotifyImage;

import com.musicplatform.backend_core.spotify.dto.external.SpotifySimplifiedArtistObject;

import com.musicplatform.backend_core.spotify.dto.response.SpotifyArtistResponse;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;



class SpotifyArtistMapperTest {



    private SpotifyArtistMapper mapper;



    @BeforeEach

    void setUp() {

        mapper = new SpotifyArtistMapper();

    }



    @Test

    void toResponse_mapsStableFieldsFromFullArtist() {

        SpotifyFullArtistObject artist = new SpotifyFullArtistObject();

        artist.setId("artist-1");

        artist.setName("Shakira");

        artist.setHref("https://api.spotify.com/v1/artists/artist-1");

        artist.setType("artist");

        artist.setUri("spotify:artist:artist-1");

        artist.setGenres(List.of("latin pop", "colombian pop"));

        artist.setPopularity(89);



        SpotifyFollowers followers = new SpotifyFollowers();

        followers.setHref(null);

        followers.setTotal(78_000_000);

        artist.setFollowers(followers);



        SpotifyImage image = new SpotifyImage();

        image.setUrl("https://i.scdn.co/image/example");

        image.setHeight(300);

        image.setWidth(300);

        artist.setImages(List.of(image));



        SpotifyExternalUrls externalUrls = new SpotifyExternalUrls();

        externalUrls.setSpotify("https://open.spotify.com/artist/artist-1");

        artist.setExternalUrls(externalUrls);



        SpotifyArtistResponse response = mapper.toResponse(artist);



        assertEquals("https://open.spotify.com/artist/artist-1", response.externalUrls().getSpotify());

        assertEquals("https://api.spotify.com/v1/artists/artist-1", response.href());

        assertEquals("artist-1", response.id());

        assertEquals(1, response.images().size());

        assertEquals("https://i.scdn.co/image/example", response.images().get(0).getUrl());

        assertEquals(300, response.images().get(0).getHeight());

        assertEquals("Shakira", response.name());

        assertEquals("artist", response.type());

        assertEquals("spotify:artist:artist-1", response.uri());

    }



    @Test

    void toResponse_usesDefaultsWhenOptionalStableFieldsAreMissing() {

        SpotifyFullArtistObject artist = new SpotifyFullArtistObject();

        artist.setId("artist-2");

        artist.setName("Unknown Artist");



        SpotifyArtistResponse response = mapper.toResponse(artist);



        assertEquals("artist-2", response.id());

        assertEquals("Unknown Artist", response.name());

        assertEquals(List.of(), response.images());

        assertEquals("artist", response.type());

        assertNull(response.href());

        assertNull(response.uri());

        assertNull(response.externalUrls().getSpotify());

    }



    @Test

    void toResponseFromSimplified_mapsStableFieldsFromSearchResult() {

        SpotifySimplifiedArtistObject artist = new SpotifySimplifiedArtistObject();

        artist.setId("artist-4");

        artist.setName("Shakira");

        artist.setHref("https://api.spotify.com/v1/artists/artist-4");

        artist.setType("artist");

        artist.setUri("spotify:artist:artist-4");



        SpotifyImage image = new SpotifyImage();

        image.setUrl("https://i.scdn.co/image/example");

        artist.setImages(List.of(image));



        SpotifyExternalUrls externalUrls = new SpotifyExternalUrls();

        externalUrls.setSpotify("https://open.spotify.com/artist/artist-4");

        artist.setExternalUrls(externalUrls);



        SpotifyArtistResponse response = mapper.toResponseFromSimplified(artist);



        assertEquals("artist-4", response.id());

        assertEquals("Shakira", response.name());

        assertEquals("https://api.spotify.com/v1/artists/artist-4", response.href());

        assertEquals("artist", response.type());

        assertEquals("spotify:artist:artist-4", response.uri());

        assertEquals("https://open.spotify.com/artist/artist-4", response.externalUrls().getSpotify());

        assertEquals(1, response.images().size());

    }

}

