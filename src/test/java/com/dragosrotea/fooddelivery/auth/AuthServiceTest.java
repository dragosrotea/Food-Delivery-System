package com.dragosrotea.fooddelivery.auth;

import com.dragosrotea.fooddelivery.user.UserAccount;
import com.dragosrotea.fooddelivery.user.UserRepository;
import com.dragosrotea.fooddelivery.user.UserRole;
import com.dragosrotea.fooddelivery.user.exception.EmailAlreadyRegisteredException;
import com.dragosrotea.fooddelivery.user.exception.InvalidCredentialsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private AccessTokenService accessTokenService;

    @InjectMocks
    private AuthService authService;

    @Test
    void registersNormalizedCustomerWithHashedPassword() {
        when(userRepository.existsByEmail("dragos@example.com")).thenReturn(false);
        when(passwordEncoder.encode("secure-password")).thenReturn("password-hash");
        when(userRepository.save(any(UserAccount.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UserAccount result = authService.register(
                new RegisterRequest(" Dragos@Example.com ", "secure-password")
        );

        assertEquals("dragos@example.com", result.getEmail());
        assertEquals("password-hash", result.getPasswordHash());
        assertEquals(UserRole.CUSTOMER, result.getRole());

        ArgumentCaptor<UserAccount> accountCaptor =
                ArgumentCaptor.forClass(UserAccount.class);
        verify(userRepository).save(accountCaptor.capture());
        assertEquals("password-hash", accountCaptor.getValue().getPasswordHash());
    }

    @Test
    void rejectsAlreadyRegisteredEmail() {
        when(userRepository.existsByEmail("dragos@example.com")).thenReturn(true);

        assertThrows(
                EmailAlreadyRegisteredException.class,
                () -> authService.register(
                        new RegisterRequest("Dragos@Example.com", "secure-password")
                )
        );

        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any(UserAccount.class));
    }

    @Test
    void returnsTokenAfterSuccessfulLogin() {
        UserAccount account = new UserAccount(
                "dragos@example.com",
                "password-hash",
                UserRole.CUSTOMER
        );
        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(userRepository.findByEmail("dragos@example.com"))
                .thenReturn(Optional.of(account));
        when(accessTokenService.createToken(account)).thenReturn("signed-token");
        when(accessTokenService.getExpirationSeconds()).thenReturn(3600L);

        AuthResponse response = authService.login(
                new LoginRequest("Dragos@Example.com", "secure-password")
        );

        assertEquals("signed-token", response.accessToken());
        assertEquals("Bearer", response.tokenType());
        assertEquals(3600L, response.expiresIn());
    }

    @Test
    void hidesReasonForInvalidCredentials() {
        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenThrow(new BadCredentialsException("Password did not match"));

        InvalidCredentialsException exception = assertThrows(
                InvalidCredentialsException.class,
                () -> authService.login(
                        new LoginRequest("dragos@example.com", "wrong-password")
                )
        );

        assertEquals("Invalid email or password", exception.getMessage());
        verify(accessTokenService, never()).createToken(any(UserAccount.class));
    }
}
