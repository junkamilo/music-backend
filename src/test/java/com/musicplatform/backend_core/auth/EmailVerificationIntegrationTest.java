package com.musicplatform.backend_core.auth;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.musicplatform.backend_core.auth.entity.EmailVerificationTokenGetters;
import com.musicplatform.backend_core.auth.entity.User;
import com.musicplatform.backend_core.auth.entity.UserGetters;
import com.musicplatform.backend_core.auth.repository.EmailVerificationTokenRepository;
import com.musicplatform.backend_core.auth.repository.UserRepository;
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
class EmailVerificationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailVerificationTokenRepository tokenRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void register_createsVerificationToken() throws Exception {
        String suffix = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        String username = "verify_" + suffix;
        String email = username + "@example.com";

        registerUser(username, email);

        User user = userRepository.findByEmail(email).orElseThrow();
        assertNull(UserGetters.getEmailVerifiedAt(user));
        assertFalse(tokenRepository.findActiveByUserId(UserGetters.getId(user)).isEmpty());
    }

    @Test
    void verifyEmail_withValidCode_marksUserVerified() throws Exception {
        String suffix = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        String username = "code_" + suffix;
        String email = username + "@example.com";

        registerUser(username, email);
        User user = userRepository.findByEmail(email).orElseThrow();
        String code = EmailVerificationTokenGetters.getVerificationCode(
                tokenRepository.findActiveByUserId(UserGetters.getId(user)).getFirst());

        mockMvc.perform(post("/api/v1/auth/verify-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("code", code))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Email verificado correctamente"));

        User updated = userRepository.findByEmail(email).orElseThrow();
        assertNotNull(UserGetters.getEmailVerifiedAt(updated));
    }

    @Test
    void verifyEmail_withInvalidCode_returnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/auth/verify-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("code", "ZZZZZZ"))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void verifyByLink_redirectsToFrontendSuccess() throws Exception {
        String suffix = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        String username = "link_" + suffix;
        String email = username + "@example.com";

        registerUser(username, email);
        User user = userRepository.findByEmail(email).orElseThrow();
        String token = EmailVerificationTokenGetters.getToken(
                tokenRepository.findActiveByUserId(UserGetters.getId(user)).getFirst());

        mockMvc.perform(get("/api/v1/auth/verify").param("token", token))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", org.hamcrest.Matchers.containsString("status=success")));
    }

    @Test
    void resendVerification_replacesActiveToken() throws Exception {
        String suffix = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        String username = "resend_" + suffix;
        String email = username + "@example.com";

        registerUser(username, email);
        User user = userRepository.findByEmail(email).orElseThrow();
        String firstCode = EmailVerificationTokenGetters.getVerificationCode(
                tokenRepository.findActiveByUserId(UserGetters.getId(user)).getFirst());

        Thread.sleep(1100);

        mockMvc.perform(post("/api/v1/auth/resend-verification")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("email", email))))
                .andExpect(status().isNoContent());

        String secondCode = EmailVerificationTokenGetters.getVerificationCode(
                tokenRepository.findActiveByUserId(UserGetters.getId(user)).getFirst());
        assertNotEquals(firstCode, secondCode);
    }

    private void registerUser(String username, String email) throws Exception {
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "username", username,
                                "email", email,
                                "password", "Password1"
                        ))))
                .andExpect(status().isCreated());
    }
}
