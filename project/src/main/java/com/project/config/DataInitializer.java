package com.project.config;

import com.project.entity.User;
import com.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner createAdmin(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder, @Value("${app.admin.password:Admin@123}")String adminPassword) {

        return args -> {

            String adminEmail = "admin@smartcart.com";

            if (!userRepository.existsByEmail(adminEmail)) {

                User admin = new User();

                admin.setName("SmartCart Admin");
                admin.setEmail(adminEmail);

                admin.setPassword(
                        passwordEncoder.encode(adminPassword)
                );

                admin.setRole("ADMIN");

                userRepository.save(admin);

                System.out.println("=================================");
                System.out.println("SmartCart ADMIN created");
                System.out.println("Email: admin@smartcart.com");
                System.out.println("Role: ADMIN");
                System.out.println("=================================");
            }
        };
    }
}