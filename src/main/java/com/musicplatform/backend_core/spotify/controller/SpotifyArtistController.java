package com.musicplatform.backend_core.spotify.controller;

import com.musicplatform.backend_core.spotify.dto.response.SpotifyArtistResponse;
import com.musicplatform.backend_core.spotify.service.SpotifyService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/spotify")
public class SpotifyArtistController {

    private final SpotifyService spotifyService;

    public SpotifyArtistController(SpotifyService spotifyService) {
        this.spotifyService = spotifyService;
    }

    @GetMapping("/artists/search")
    public ResponseEntity<List<SpotifyArtistResponse>> searchArtists(
            @RequestParam String query,
            @RequestParam(defaultValue = "10") int limit
    ) {
        return ResponseEntity.ok(spotifyService.searchArtists(query, limit));
    }

    @GetMapping("/artists/{spotifyArtistId}")
    public ResponseEntity<SpotifyArtistResponse> getArtist(
            @PathVariable String spotifyArtistId
    ) {
        return ResponseEntity.ok(spotifyService.getArtistById(spotifyArtistId));
    }
}
