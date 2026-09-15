package com.dragosrotea.fooddelivery.driver;

import com.dragosrotea.fooddelivery.user.UserAccount;
import com.dragosrotea.fooddelivery.user.UserRepository;
import com.dragosrotea.fooddelivery.user.UserRole;
import com.dragosrotea.fooddelivery.user.exception.EmailAlreadyRegisteredException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DriverAccountServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void createsDriverWithNormalizedEmailAndHashedPassword() {
        DriverAccountService service =
                new DriverAccountService(userRepository, passwordEncoder);
        CreateDriverRequest request = new CreateDriverRequest(
                " Driver@Example.com ",
                "secure-password"
        );

        when(userRepository.existsByEmail("driver@example.com")).thenReturn(false);
        when(passwordEncoder.encode("secure-password")).thenReturn("password-hash");
        when(userRepository.save(any(UserAccount.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UserAccount result = service.createDriver(request);

        assertEquals("driver@example.com", result.getEmail());
        assertEquals("password-hash", result.getPasswordHash());
        assertEquals(UserRole.DRIVER, result.getRole());

        ArgumentCaptor<UserAccount> captor =
                ArgumentCaptor.forClass(UserAccount.class);
        verify(userRepository).save(captor.capture());
        assertEquals(UserRole.DRIVER, captor.getValue().getRole());
    }

    @Test
    void rejectsEmailAlreadyUsedByAnotherAccount() {
        DriverAccountService service =
                new DriverAccountService(userRepository, passwordEncoder);
        CreateDriverRequest request = new CreateDriverRequest(
                "driver@example.com",
                "secure-password"
        );
        when(userRepository.existsByEmail("driver@example.com")).thenReturn(true);

        assertThrows(
                EmailAlreadyRegisteredException.class,
                () -> service.createDriver(request)
        );

        verifyNoInteractions(passwordEncoder);
        verify(userRepository, never()).save(any(UserAccount.class));
    }
}
