package com.musicplatform.backend_core.user;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getMe_returnsExtendedProfile() throws Exception {
        String accessToken = registerAndLogin();

        mockMvc.perform(get("/api/v1/users/me")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.username").isString())
                .andExpect(jsonPath("$.email").isString())
                .andExpect(jsonPath("$.role").value("LISTENER"))
                .andExpect(jsonPath("$.active").value(true))
                .andExpect(jsonPath("$.emailVerified").value(false));
    }

    @Test
    void updateMe_updatesUsernameAndEmail() throws Exception {
        String accessToken = registerAndLogin();
        String newUsername = "user_" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        String newEmail = newUsername + "@example.com";

        mockMvc.perform(put("/api/v1/users/me")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "username", newUsername,
                                "email", newEmail
                        ))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(newUsername))
                .andExpect(jsonPath("$.email").value(newEmail));
    }

    @Test
    void updateMe_withDuplicateUsername_returnsConflict() throws Exception {
        String suffix = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        String username = "dup_" + suffix;
        registerUser(username, username + "@first.com", "Password1");
        String accessToken = registerAndLogin();

        mockMvc.perform(put("/api/v1/users/me")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("username", username))))
                .andExpect(status().isConflict());
    }

    @Test
    void changePassword_withWrongCurrentPassword_returnsUnauthorized() throws Exception {
        String accessToken = registerAndLogin();

        mockMvc.perform(put("/api/v1/users/me/password")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "currentPassword", "WrongPass1",
                                "newPassword", "NewPassword1"
                        ))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void changePassword_success() throws Exception {
        String suffix = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        String username = "pwd_" + suffix;
        String email = username + "@example.com";
        String originalPassword = "Password1";
        String newPassword = "NewPassword2";

        registerUser(username, email, originalPassword);
        String accessToken = login(username, originalPassword);

        mockMvc.perform(put("/api/v1/users/me/password")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "currentPassword", originalPassword,
                                "newPassword", newPassword
                        ))))
                .andExpect(status().isNoContent());

        String newAccessToken = login(username, newPassword);
        assertFalse(newAccessToken.isBlank());
    }

    private String registerAndLogin() throws Exception {
        String suffix = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        String username = "test_" + suffix;
        String email = username + "@example.com";
        String password = "Password1";
        registerUser(username, email, password);
        return login(username, password);
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
