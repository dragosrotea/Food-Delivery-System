package com.dragosrotea.fooddelivery.auth;

public record AuthResponse(
        String accessToken,
        String tokenType,
        long expiresIn
) {
}
