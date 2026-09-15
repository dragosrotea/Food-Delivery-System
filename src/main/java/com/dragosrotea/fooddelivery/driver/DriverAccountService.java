package com.dragosrotea.fooddelivery.driver;

import com.dragosrotea.fooddelivery.user.UserAccount;
import com.dragosrotea.fooddelivery.user.UserRepository;
import com.dragosrotea.fooddelivery.user.UserRole;
import com.dragosrotea.fooddelivery.user.exception.EmailAlreadyRegisteredException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
@Transactional
public class DriverAccountService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DriverAccountService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserAccount createDriver(CreateDriverRequest request) {
        String email = request.email().trim().toLowerCase(Locale.ROOT);

        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyRegisteredException(email);
        }

        return userRepository.save(
                new UserAccount(
                        email,
                        passwordEncoder.encode(request.password()),
                        UserRole.DRIVER
                )
        );
    }
}
