package com.musicplatform.backend_core.artist;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.musicplatform.backend_core.artist.entity.ArtistProfile;
import com.musicplatform.backend_core.artist.entity.ArtistProfileFactory;
import com.musicplatform.backend_core.artist.entity.ArtistProfileSetters;
import com.musicplatform.backend_core.artist.entity.ArtistType;
import com.musicplatform.backend_core.artist.repository.ArtistProfileRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
class ArtistProfileIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ArtistProfileRepository artistProfileRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void register_claimsFeaturedProfileAndPromotesToCreator() throws Exception {
        String suffix = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        String stageName = "Bad Bunny " + suffix;
        String spotifyId = "spotify_" + suffix;

        seedFeaturedProfile(stageName, spotifyId);

        String username = "bunny_" + suffix;
        String email = username + "@example.com";
        String password = "Password1";

        MvcResult registerResult = mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "username", username,
                                "email", email,
                                "password", password,
                                "stageName", stageName,
                                "spotifyId", spotifyId
                        ))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.role").value("CREATOR"))
                .andExpect(jsonPath("$.artist.stageName").value(stageName))
                .andExpect(jsonPath("$.artist.artistType").value("INDEPENDENT"))
                .andExpect(jsonPath("$.artist.spotifyId").value(spotifyId))
                .andReturn();

        long profileId = objectMapper.readTree(registerResult.getResponse().getContentAsString())
                .get("artist")
                .get("id")
                .asLong();

        mockMvc.perform(get("/api/v1/artist-profiles/" + profileId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.artistType").value("INDEPENDENT"))
                .andExpect(jsonPath("$.spotifyId").value(spotifyId));
    }

    @Test
    void listFeaturedProfiles_returnsUnclaimedFeaturedArtists() throws Exception {
        String suffix = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        String stageName = "Featured Star " + suffix;
        seedFeaturedProfile(stageName, "spotify_feat_" + suffix);

        mockMvc.perform(get("/api/v1/artist-profiles/featured"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.stageName == '" + stageName + "')]").exists());
    }

    @Test
    void creator_canManageMilestonesAndTimeline_andPublicProfileIsEnriched() throws Exception {
        String accessToken = registerBecomeCreatorAndCreateProfile();
        long profileId = getMyProfileId(accessToken);

        MvcResult milestoneResult = mockMvc.perform(post("/api/v1/artist-profiles/me/milestones")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "title", "Billboard Music Award",
                                "milestoneType", "AWARD",
                                "dateAchieved", "2019-05-01"
                        ))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Billboard Music Award"))
                .andReturn();

        long milestoneId = objectMapper.readTree(milestoneResult.getResponse().getContentAsString())
                .get("id")
                .asLong();

        mockMvc.perform(post("/api/v1/artist-profiles/me/timeline-events")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "eventTitle", "Nace en San Juan",
                                "eventType", "BEGINNING",
                                "eventDate", "1994-03-10"
                        ))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.eventTitle").value("Nace en San Juan"));

        mockMvc.perform(get("/api/v1/artist-profiles/" + profileId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stageName").value("Test Artist"))
                .andExpect(jsonPath("$.milestones").isArray())
                .andExpect(jsonPath("$.milestones[0].title").value("Billboard Music Award"))
                .andExpect(jsonPath("$.timelineEvents").isArray())
                .andExpect(jsonPath("$.timelineEvents[0].eventTitle").value("Nace en San Juan"));

        mockMvc.perform(put("/api/v1/artist-profiles/me/milestones/" + milestoneId)
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "title", "Grammy Award"
                        ))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Grammy Award"));

        mockMvc.perform(delete("/api/v1/artist-profiles/me/milestones/" + milestoneId)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isNoContent());
    }

    @Test
    void otherUserCannotUpdateForeignMilestone() throws Exception {
        String ownerToken = registerBecomeCreatorAndCreateProfile();
        long ownerProfileId = getMyProfileId(ownerToken);

        MvcResult milestoneResult = mockMvc.perform(post("/api/v1/artist-profiles/me/milestones")
                        .header("Authorization", "Bearer " + ownerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "title", "Owner Milestone",
                                "milestoneType", "AWARD",
                                "dateAchieved", "2020-01-01"
                        ))))
                .andExpect(status().isCreated())
                .andReturn();

        long milestoneId = objectMapper.readTree(milestoneResult.getResponse().getContentAsString())
                .get("id")
                .asLong();

        String otherToken = registerBecomeCreatorAndCreateProfile();

        mockMvc.perform(put("/api/v1/artist-profiles/me/milestones/" + milestoneId)
                        .header("Authorization", "Bearer " + otherToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "title", "Hacked"
                        ))))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/api/v1/artist-profiles/" + ownerProfileId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.milestones[0].title").value("Owner Milestone"));
    }

    private String registerBecomeCreatorAndCreateProfile() throws Exception {
        String suffix = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        String username = "artist_" + suffix;
        String email = username + "@example.com";
        String password = "Password1";

        registerUser(username, email, password);
        String accessToken = login(username, password);

        mockMvc.perform(post("/api/v1/users/me/become-creator")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/artist-profiles")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "stageName", "Test Artist",
                                "birthCity", "San Juan",
                                "birthCountry", "Puerto Rico",
                                "birthDate", "1994-03-10",
                                "stageNameOrigin", "Porque era rapido como un conejo"
                        ))))
                .andExpect(status().isCreated());

        return accessToken;
    }

    private long getMyProfileId(String accessToken) throws Exception {
        MvcResult result = mockMvc.perform(get("/api/v1/artist-profiles/me")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andReturn();

        return objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asLong();
    }

    private void registerUser(String username, String email, String password) throws Exception {
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "username", username,
                                "email", email,
                                "password", password
                        ))))
                .andExpect(status().isCreated());
    }

    private void seedFeaturedProfile(String stageName, String spotifyId) {
        ArtistProfile profile = ArtistProfileFactory.createEmpty();
        ArtistProfileSetters.setStageName(profile, stageName);
        ArtistProfileSetters.setSpotifyId(profile, spotifyId);
        ArtistProfileSetters.setArtistType(profile, ArtistType.FEATURED);
        ArtistProfileSetters.setActive(profile, true);
        artistProfileRepository.save(profile);
    }

    private String login(String identifier, String password) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "identifier", identifier,
                                "password", password
                        ))))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode body = objectMapper.readTree(result.getResponse().getContentAsString());
        return body.get("accessToken").asText();
    }
}
