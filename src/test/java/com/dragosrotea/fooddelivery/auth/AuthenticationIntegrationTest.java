package com.dragosrotea.fooddelivery.auth;

import com.dragosrotea.fooddelivery.user.UserAccount;
import com.dragosrotea.fooddelivery.user.UserRepository;
import com.dragosrotea.fooddelivery.user.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void cleanUsers() {
        userRepository.deleteAll();
    }

    @Test
    void registersCustomerAndHashesPassword() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": " Dragos@Example.com ",
                                  "password": "secure-password"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("dragos@example.com"))
                .andExpect(jsonPath("$.role").value("CUSTOMER"));

        UserAccount savedAccount = userRepository
                .findByEmail("dragos@example.com")
                .orElseThrow();

        assertNotEquals("secure-password", savedAccount.getPasswordHash());
        assertTrue(
                passwordEncoder.matches(
                        "secure-password",
                        savedAccount.getPasswordHash()
                )
        );
        assertTrue(savedAccount.getRole() == UserRole.CUSTOMER);
    }

    @Test
    void rejectsDuplicateEmailIgnoringCase() throws Exception {
        registerUser();

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "DRAGOS@EXAMPLE.COM",
                                  "password": "another-password"
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message")
                        .value("An account with email 'dragos@example.com' already exists"));
    }

    @Test
    void logsInWithCorrectCredentialsAndReturnsBearerToken() throws Exception {
        registerUser();

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "dragos@example.com",
                                  "password": "secure-password"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.expiresIn").value(3600));
    }

    @Test
    void returnsUnauthorizedForIncorrectPassword() throws Exception {
        registerUser();

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "dragos@example.com",
                                  "password": "incorrect-password"
                                }
                                """))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid email or password"));
    }

    private void registerUser() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "dragos@example.com",
                                  "password": "secure-password"
                                }
                                """))
                .andExpect(status().isCreated());
    }
}
