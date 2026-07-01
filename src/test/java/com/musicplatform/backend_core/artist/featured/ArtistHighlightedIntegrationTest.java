package com.musicplatform.backend_core.artist.featured;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.musicplatform.backend_core.artist.entity.ArtistProfile;
import com.musicplatform.backend_core.artist.entity.ArtistProfileFactory;
import com.musicplatform.backend_core.artist.entity.ArtistProfileSetters;
import com.musicplatform.backend_core.artist.entity.ArtistType;
import com.musicplatform.backend_core.artist.featured.entity.ArtistInspiration;
import com.musicplatform.backend_core.artist.featured.entity.ArtistInspirationFactory;
import com.musicplatform.backend_core.artist.featured.entity.ArtistInspirationSetters;
import com.musicplatform.backend_core.artist.featured.repository.ArtistInspirationRepository;
import com.musicplatform.backend_core.artist.featured.repository.FeaturedArtistRepository;
import com.musicplatform.backend_core.artist.repository.ArtistProfileRepository;
import com.musicplatform.backend_core.auth.entity.User;
import com.musicplatform.backend_core.auth.entity.UserFactory;
import com.musicplatform.backend_core.auth.entity.UserSetters;
import com.musicplatform.backend_core.auth.repository.UserRepository;
import com.musicplatform.backend_core.artist.entity.ArtistProfileGetters;
import com.musicplatform.backend_core.artist.featured.entity.FeaturedArtistGetters;
import com.musicplatform.backend_core.auth.entity.UserGetters;
import com.musicplatform.backend_core.shared.enums.UserRole;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ArtistHighlightedIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FeaturedArtistRepository featuredArtistRepository;

    @Autowired
    private ArtistProfileRepository artistProfileRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArtistInspirationRepository artistInspirationRepository;

    @Test
    void getHighlighted_returnsOnlySyncedSpotifyArtists() throws Exception {
        mockMvc.perform(get("/api/v1/artists/highlighted").param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].type").value("FEATURED"))
                .andExpect(jsonPath("$[0].spotifyId").isNotEmpty())
                .andExpect(jsonPath("$[0].imageUrl").isNotEmpty())
                .andExpect(jsonPath("$[?(@.type == 'INDEPENDENT')]").doesNotExist());
    }

    @Test
    void getInspiredCreators_returnsLinkedIndependentArtists() throws Exception {
        String suffix = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        Long emergentProfileId = seedIndependentCreator("Inspired " + suffix);
        Long featuredArtistId = FeaturedArtistGetters.getId(
                featuredArtistRepository.findBySpotifyId("0EmeFodog0BfCgMzAIvKQp").orElseThrow()
        );

        ArtistInspiration inspiration = ArtistInspirationFactory.createEmpty();
        ArtistInspirationSetters.setEmergentArtistId(inspiration, emergentProfileId);
        ArtistInspirationSetters.setFeaturedArtistId(inspiration, featuredArtistId);
        ArtistInspirationSetters.setSimilarityReason(inspiration, "Reggaeton urbano");
        artistInspirationRepository.save(inspiration);

        mockMvc.perform(get("/api/v1/artists/" + featuredArtistId + "/inspired-creators")
                        .param("limit", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Inspired " + suffix))
                .andExpect(jsonPath("$[0].type").value("INDEPENDENT"));
    }

    private Long seedIndependentCreator(String stageName) {
        String suffix = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        User user = UserFactory.createEmpty();
        UserSetters.setUsername(user, "creator_" + suffix);
        UserSetters.setEmail(user, "creator_" + suffix + "@example.com");
        UserSetters.setPasswordHash(user, "hash");
        UserSetters.setRole(user, UserRole.CREATOR);
        UserSetters.setActive(user, true);
        UserSetters.setEmailVerifiedAt(user, LocalDateTime.now());
        User savedUser = userRepository.save(user);

        ArtistProfile profile = ArtistProfileFactory.createEmpty();
        ArtistProfileSetters.setUserId(profile, UserGetters.getId(savedUser));
        ArtistProfileSetters.setStageName(profile, stageName);
        ArtistProfileSetters.setArtistType(profile, ArtistType.INDEPENDENT);
        ArtistProfileSetters.setActive(profile, true);
        return ArtistProfileGetters.getId(artistProfileRepository.save(profile));
    }
}
