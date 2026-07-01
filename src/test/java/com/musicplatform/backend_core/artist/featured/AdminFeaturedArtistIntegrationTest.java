package com.musicplatform.backend_core.artist.featured;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musicplatform.backend_core.auth.entity.User;
import com.musicplatform.backend_core.auth.entity.UserFactory;
import com.musicplatform.backend_core.auth.entity.UserSetters;
import com.musicplatform.backend_core.auth.repository.UserRepository;
import com.musicplatform.backend_core.shared.enums.UserRole;
import com.musicplatform.backend_core.spotify.dto.external.SpotifyExternalUrls;
import com.musicplatform.backend_core.spotify.dto.external.SpotifyFullArtistObject;
import com.musicplatform.backend_core.spotify.dto.external.SpotifyImage;
import com.musicplatform.backend_core.spotify.service.SpotifyService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
class AdminFeaturedArtistIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private SpotifyService spotifyService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUpSpotifyMock() {
        SpotifyFullArtistObject artist = new SpotifyFullArtistObject();
        artist.setId("0EmeFodog0BfCgMzAIvKQp");
        artist.setName("Shakira");
        artist.setPopularity(94);
        artist.setGenres(List.of("reggaeton", "latin-pop"));

        SpotifyImage image = new SpotifyImage();
        image.setUrl("https://example.com/shakira.jpg");
        image.setHeight(640);
        artist.setImages(List.of(image));

        SpotifyExternalUrls externalUrls = new SpotifyExternalUrls();
        externalUrls.setSpotify("https://open.spotify.com/artist/0EmeFodog0BfCgMzAIvKQp");
        artist.setExternalUrls(externalUrls);

        when(spotifyService.getFullArtistById(anyString())).thenReturn(artist);
    }

    @Test
    void listenerCannotSyncAll() throws Exception {
        String suffix = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        String token = loginAs("listener_" + suffix, UserRole.LISTENER);

        mockMvc.perform(post("/api/v1/admin/artists/sync-all")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void adminCanTriggerSyncAll() throws Exception {
        String suffix = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        String token = loginAs("admin_" + suffix, UserRole.ADMIN);

        mockMvc.perform(post("/api/v1/admin/artists/sync-all")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    private String loginAs(String username, UserRole role) throws Exception {
        String email = username + "@example.com";
        String password = "Password1";

        User user = UserFactory.createEmpty();
        UserSetters.setUsername(user, username);
        UserSetters.setEmail(user, email);
        UserSetters.setPasswordHash(user, passwordEncoder.encode(password));
        UserSetters.setRole(user, role);
        UserSetters.setActive(user, true);
        UserSetters.setEmailVerifiedAt(user, LocalDateTime.now());
        userRepository.save(user);

        MvcResult result = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "identifier", username,
                                "password", password
                        ))))
                .andExpect(status().isOk())
                .andReturn();

        return objectMapper.readTree(result.getResponse().getContentAsString())
                .get("accessToken")
                .asText();
    }
}
