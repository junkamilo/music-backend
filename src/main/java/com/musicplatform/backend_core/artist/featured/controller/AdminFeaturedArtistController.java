package com.musicplatform.backend_core.artist.featured.controller;

import com.musicplatform.backend_core.artist.featured.dto.request.AddFeaturedArtistFromSpotifyRequest;
import com.musicplatform.backend_core.artist.featured.dto.response.FeaturedArtistAdminResponse;
import com.musicplatform.backend_core.artist.featured.dto.response.FeaturedArtistSyncResult;
import com.musicplatform.backend_core.artist.featured.service.FeaturedArtistAdminService;
import com.musicplatform.backend_core.shared.security.UserPrincipal;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/artists")
public class AdminFeaturedArtistController {

    private final FeaturedArtistAdminService featuredArtistAdminService;

    public AdminFeaturedArtistController(FeaturedArtistAdminService featuredArtistAdminService) {
        this.featuredArtistAdminService = featuredArtistAdminService;
    }

    @PostMapping("/sync-now/{featuredArtistId}")
    public ResponseEntity<FeaturedArtistAdminResponse> syncArtistNow(
            @PathVariable Long featuredArtistId,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                featuredArtistAdminService.syncArtistNow(featuredArtistId, resolveUserId(authentication))
        );
    }

    @PostMapping("/add-from-spotify")
    public ResponseEntity<FeaturedArtistAdminResponse> addArtistFromSpotify(
            @Valid @RequestBody AddFeaturedArtistFromSpotifyRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                featuredArtistAdminService.addArtistFromSpotify(
                        request.spotifyArtistId(),
                        resolveUserId(authentication)
                )
        );
    }

    @PostMapping("/sync-all")
    public ResponseEntity<FeaturedArtistSyncResult> syncAll(
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                featuredArtistAdminService.syncAllNow(resolveUserId(authentication))
        );
    }

    private Long resolveUserId(Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        return principal.userId();
    }
}
