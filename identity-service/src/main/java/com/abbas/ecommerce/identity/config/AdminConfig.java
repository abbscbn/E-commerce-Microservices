package com.abbas.ecommerce.identity.config;

import com.abbas.ecommerce.identity.enumtype.Role;
import com.abbas.ecommerce.identity.model.User;
import com.abbas.ecommerce.identity.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class AdminConfig {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    public AdminConfig(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.password}")
    private String adminPassword;

    @Bean
    CommandLineRunner createAdmin() {
        return args -> {
            if (!userRepository.existsByUsername(adminUsername)) {
                User admin = new User();
                admin.setUsername(adminUsername);
                admin.setEmail("admin@mail.com");
                admin.setPassword(passwordEncoder.encode(adminPassword));
                admin.setRoles(Set.of(Role.ADMIN));
                userRepository.save(admin);
            }
        };
    }


}
