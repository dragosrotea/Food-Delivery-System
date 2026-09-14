package com.dragosrotea.fooddelivery.security;

import com.dragosrotea.fooddelivery.user.UserAccount;
import com.dragosrotea.fooddelivery.user.UserRepository;
import com.dragosrotea.fooddelivery.user.UserRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Component
public class AdminAccountInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final String adminEmail;
    private final String adminPassword;

    public AdminAccountInitializer(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.admin.email:}") String adminEmail,
            @Value("${app.admin.password:}") String adminPassword
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminEmail = adminEmail;
        this.adminPassword = adminPassword;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments arguments) {
        boolean emailConfigured = !adminEmail.isBlank();
        boolean passwordConfigured = !adminPassword.isBlank();

        if (!emailConfigured && !passwordConfigured) {
            return;
        }

        if (!emailConfigured || !passwordConfigured) {
            throw new IllegalStateException(
                    "APP_ADMIN_EMAIL and APP_ADMIN_PASSWORD must be configured together"
            );
        }

        String normalizedEmail = adminEmail.trim().toLowerCase(Locale.ROOT);

        if (!userRepository.existsByEmail(normalizedEmail)) {
            userRepository.save(
                    new UserAccount(
                            normalizedEmail,
                            passwordEncoder.encode(adminPassword),
                            UserRole.ADMIN
                    )
            );
        }
    }
}
