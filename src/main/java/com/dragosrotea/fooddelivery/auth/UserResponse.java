package com.dragosrotea.fooddelivery.auth;

import com.dragosrotea.fooddelivery.user.UserAccount;

public record UserResponse(
        Long id,
        String email,
        String role
) {

    public static UserResponse from(UserAccount account) {
        return new UserResponse(
                account.getId(),
                account.getEmail(),
                account.getRole().name()
        );
    }
}
