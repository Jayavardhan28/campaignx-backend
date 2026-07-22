package com.campaignx;

import com.campaignx.entity.Role;
import com.campaignx.entity.User;
import com.campaignx.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminUserInitializer {

    @Bean
    public CommandLineRunner initAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder,
            JdbcTemplate jdbcTemplate) {
        return args -> {
            try {
                jdbcTemplate.execute("ALTER TABLE generated_content DROP COLUMN created_at");
                System.out.println("====== Dropped legacy column 'created_at' from 'generated_content' ======");
            } catch (Exception e) {
                // Ignore if not present
            }

            String adminEmail = "admin@campaignx.com";
            if (!userRepository.existsByEmail(adminEmail)) {
                User admin = new User();
                admin.setName("Admin User");
                admin.setEmail(adminEmail);
                admin.setPassword(passwordEncoder.encode("AdminPassword123"));
                admin.setRole(Role.ADMIN);
                userRepository.save(admin);
                System.out.println(
                        "====== Default Admin User Initialized: admin@campaignx.com / AdminPassword123 ======");
            }
        };
    }
}
