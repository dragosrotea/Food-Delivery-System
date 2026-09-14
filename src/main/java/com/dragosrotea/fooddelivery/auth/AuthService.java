package com.dragosrotea.fooddelivery.auth;

import com.dragosrotea.fooddelivery.user.UserAccount;
import com.dragosrotea.fooddelivery.user.UserRepository;
import com.dragosrotea.fooddelivery.user.UserRole;
import com.dragosrotea.fooddelivery.user.exception.EmailAlreadyRegisteredException;
import com.dragosrotea.fooddelivery.user.exception.InvalidCredentialsException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final AccessTokenService accessTokenService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            AccessTokenService accessTokenService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.accessTokenService = accessTokenService;
    }

    public UserAccount register(RegisterRequest request) {
        String email = normalizeEmail(request.email());

        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyRegisteredException(email);
        }

        String passwordHash = passwordEncoder.encode(request.password());
        UserAccount account = new UserAccount(email, passwordHash, UserRole.CUSTOMER);
        return userRepository.save(account);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        String email = normalizeEmail(request.email());

        try {
            authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken.unauthenticated(
                            email,
                            request.password()
                    )
            );
        } catch (AuthenticationException exception) {
            throw new InvalidCredentialsException();
        }

        UserAccount account = userRepository.findByEmail(email)
                .orElseThrow(InvalidCredentialsException::new);
        String token = accessTokenService.createToken(account);

        return new AuthResponse(
                token,
                "Bearer",
                accessTokenService.getExpirationSeconds()
        );
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
