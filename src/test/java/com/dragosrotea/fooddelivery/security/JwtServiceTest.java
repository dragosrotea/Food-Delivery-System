package com.dragosrotea.fooddelivery.security;

import com.dragosrotea.fooddelivery.user.UserAccount;
import com.dragosrotea.fooddelivery.user.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;

import javax.crypto.SecretKey;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtServiceTest {

    @Test
    void createsSignedTokenWithIdentityRoleAndExpiration() {
        JwtProperties properties = new JwtProperties(
                "test-secret-key-that-is-at-least-32-bytes-long",
                3600L,
                "https://food-delivery-api.local"
        );
        JwtConfig config = new JwtConfig();
        SecretKey key = config.jwtSecretKey(properties);
        JwtEncoder encoder = config.jwtEncoder(key);
        JwtDecoder decoder = config.jwtDecoder(key, properties);
        JwtService jwtService = new JwtService(encoder, properties);
        UserAccount account = new UserAccount(
                "dragos@example.com",
                "password-hash",
                UserRole.CUSTOMER
        );

        String token = jwtService.createToken(account);
        Jwt decodedToken = decoder.decode(token);

        assertEquals("dragos@example.com", decodedToken.getSubject());
        assertEquals("CUSTOMER", decodedToken.getClaimAsString("role"));
        assertEquals(
                "https://food-delivery-api.local",
                decodedToken.getIssuer().toString()
        );
        assertNotNull(decodedToken.getIssuedAt());
        assertNotNull(decodedToken.getExpiresAt());
        assertTrue(decodedToken.getExpiresAt().isAfter(decodedToken.getIssuedAt()));
    }
}
