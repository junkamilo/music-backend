package com.musicplatform.backend_core.artist.featured.controller;

import com.musicplatform.backend_core.artist.featured.dto.response.ArtistHomeResponse;
import com.musicplatform.backend_core.artist.featured.service.ArtistHomeService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/artists")
@Slf4j
public class ArtistController {

    private final ArtistHomeService artistHomeService;

    public ArtistController(ArtistHomeService artistHomeService) {
        this.artistHomeService = artistHomeService;
    }

    @GetMapping("/highlighted")
    public ResponseEntity<List<ArtistHomeResponse>> getHighlightedArtists(
            @RequestParam(defaultValue = "6") int limit
    ) {
        log.debug("GET /api/v1/artists/highlighted?limit={}", limit);
        return ResponseEntity.ok(artistHomeService.getHighlightedArtists(limit));
    }

    @GetMapping("/{featuredArtistId}/inspired-creators")
    public ResponseEntity<List<ArtistHomeResponse>> getInspiredCreators(
            @PathVariable Long featuredArtistId,
            @RequestParam(defaultValue = "5") int limit
    ) {
        log.debug("GET /api/v1/artists/{}/inspired-creators?limit={}", featuredArtistId, limit);
        return ResponseEntity.ok(artistHomeService.getInspiredCreators(featuredArtistId, limit));
    }
}
