package com.dragosrotea.fooddelivery.auth;

import com.dragosrotea.fooddelivery.user.UserAccount;

public interface AccessTokenService {

    String createToken(UserAccount account);

    long getExpirationSeconds();
}
