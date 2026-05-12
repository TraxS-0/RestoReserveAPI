package com.gastrotech.restoreserve.config;

import com.gastrotech.restoreserve.entity.Role;
import com.gastrotech.restoreserve.entity.User;
import com.gastrotech.restoreserve.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            if (!userRepository.existsByUsername("admin")) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setEmail("admin@gastrotech.com");
                admin.setRole(Role.ROLE_ADMIN);
                userRepository.save(admin);
                System.out.println(">>> Usuario ADMIN creado correctamente");
            }

            if (!userRepository.existsByUsername("user1")) {
                User user = new User();
                user.setUsername("user1");
                user.setPassword(passwordEncoder.encode("user123"));
                user.setEmail("user1@gastrotech.com");
                user.setRole(Role.ROLE_USER);
                userRepository.save(user);
                System.out.println(">>> Usuario USER creado correctamente");
            }
        };
    }
}