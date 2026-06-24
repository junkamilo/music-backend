package com.musicplatform.backend_core.artist.controller;

import com.musicplatform.backend_core.artist.dto.request.CreateArtistProfileRequest;
import com.musicplatform.backend_core.artist.dto.request.UpdateArtistProfileRequest;
import com.musicplatform.backend_core.artist.dto.response.ArtistProfileResponse;
import com.musicplatform.backend_core.artist.dto.response.ArtistPublicProfileResponse;
import com.musicplatform.backend_core.artist.dto.response.FeaturedArtistSummary;
import com.musicplatform.backend_core.artist.milestone.dto.request.CreateArtistMilestoneRequest;
import com.musicplatform.backend_core.artist.milestone.dto.request.UpdateArtistMilestoneRequest;
import com.musicplatform.backend_core.artist.milestone.dto.response.ArtistMilestoneResponse;
import com.musicplatform.backend_core.artist.milestone.service.ArtistMilestoneService;
import com.musicplatform.backend_core.artist.service.ArtistProfileService;
import com.musicplatform.backend_core.artist.timeline.dto.request.CreateArtistTimelineEventRequest;
import com.musicplatform.backend_core.artist.timeline.dto.request.UpdateArtistTimelineEventRequest;
import com.musicplatform.backend_core.artist.timeline.dto.response.ArtistTimelineEventResponse;
import com.musicplatform.backend_core.artist.timeline.service.ArtistTimelineEventService;
import com.musicplatform.backend_core.shared.security.UserPrincipal;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/artist-profiles")
public class ArtistProfileController {

    private final ArtistProfileService artistProfileService;
    private final ArtistMilestoneService artistMilestoneService;
    private final ArtistTimelineEventService artistTimelineEventService;

    public ArtistProfileController(
            ArtistProfileService artistProfileService,
            ArtistMilestoneService artistMilestoneService,
            ArtistTimelineEventService artistTimelineEventService
    ) {
        this.artistProfileService = artistProfileService;
        this.artistMilestoneService = artistMilestoneService;
        this.artistTimelineEventService = artistTimelineEventService;
    }

    @PostMapping
    public ResponseEntity<ArtistProfileResponse> create(
            @Valid @RequestBody CreateArtistProfileRequest request,
            Authentication authentication
    ) {
        ArtistProfileResponse response = artistProfileService.create(
                resolveUserId(authentication),
                request
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<ArtistProfileResponse> getMyProfile(Authentication authentication) {
        ArtistProfileResponse response = artistProfileService.getMyProfile(resolveUserId(authentication));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/me")
    public ResponseEntity<ArtistProfileResponse> updateMyProfile(
            @Valid @RequestBody UpdateArtistProfileRequest request,
            Authentication authentication
    ) {
        ArtistProfileResponse response = artistProfileService.updateMyProfile(
                resolveUserId(authentication),
                request
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deactivateMyProfile(Authentication authentication) {
        artistProfileService.deactivateMyProfile(resolveUserId(authentication));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me/milestones")
    public ResponseEntity<List<ArtistMilestoneResponse>> listMyMilestones(Authentication authentication) {
        return ResponseEntity.ok(artistMilestoneService.listMyMilestones(resolveUserId(authentication)));
    }

    @PostMapping("/me/milestones")
    public ResponseEntity<ArtistMilestoneResponse> createMilestone(
            @Valid @RequestBody CreateArtistMilestoneRequest request,
            Authentication authentication
    ) {
        ArtistMilestoneResponse response = artistMilestoneService.create(resolveUserId(authentication), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/me/milestones/{milestoneId}")
    public ResponseEntity<ArtistMilestoneResponse> updateMilestone(
            @PathVariable Long milestoneId,
            @Valid @RequestBody UpdateArtistMilestoneRequest request,
            Authentication authentication
    ) {
        ArtistMilestoneResponse response = artistMilestoneService.update(
                resolveUserId(authentication),
                milestoneId,
                request
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/me/milestones/{milestoneId}")
    public ResponseEntity<Void> deleteMilestone(
            @PathVariable Long milestoneId,
            Authentication authentication
    ) {
        artistMilestoneService.delete(resolveUserId(authentication), milestoneId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me/timeline-events")
    public ResponseEntity<List<ArtistTimelineEventResponse>> listMyTimelineEvents(Authentication authentication) {
        return ResponseEntity.ok(artistTimelineEventService.listMyTimelineEvents(resolveUserId(authentication)));
    }

    @PostMapping("/me/timeline-events")
    public ResponseEntity<ArtistTimelineEventResponse> createTimelineEvent(
            @Valid @RequestBody CreateArtistTimelineEventRequest request,
            Authentication authentication
    ) {
        ArtistTimelineEventResponse response = artistTimelineEventService.create(
                resolveUserId(authentication),
                request
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/me/timeline-events/{eventId}")
    public ResponseEntity<ArtistTimelineEventResponse> updateTimelineEvent(
            @PathVariable Long eventId,
            @Valid @RequestBody UpdateArtistTimelineEventRequest request,
            Authentication authentication
    ) {
        ArtistTimelineEventResponse response = artistTimelineEventService.update(
                resolveUserId(authentication),
                eventId,
                request
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/me/timeline-events/{eventId}")
    public ResponseEntity<Void> deleteTimelineEvent(
            @PathVariable Long eventId,
            Authentication authentication
    ) {
        artistTimelineEventService.delete(resolveUserId(authentication), eventId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/featured")
    public ResponseEntity<List<FeaturedArtistSummary>> listFeaturedProfiles() {
        return ResponseEntity.ok(artistProfileService.listFeaturedProfiles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArtistPublicProfileResponse> getPublicProfile(@PathVariable Long id) {
        ArtistPublicProfileResponse response = artistProfileService.getPublicProfileEnriched(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/verify")
    public ResponseEntity<ArtistProfileResponse> verifyProfile(
            @PathVariable Long id,
            Authentication authentication
    ) {
        ArtistProfileResponse response = artistProfileService.verifyProfile(
                id,
                resolveUserId(authentication)
        );
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/revoke-verification")
    public ResponseEntity<ArtistProfileResponse> revokeVerification(
            @PathVariable Long id,
            Authentication authentication
    ) {
        ArtistProfileResponse response = artistProfileService.revokeVerification(
                id,
                resolveUserId(authentication)
        );
        return ResponseEntity.ok(response);
    }

    private Long resolveUserId(Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        return principal.userId();
    }
}
