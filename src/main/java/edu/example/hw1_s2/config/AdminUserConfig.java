package edu.example.hw1_s2.config;


import edu.example.hw1_s2.entity.Role;
import edu.example.hw1_s2.entity.UserEntity;
import edu.example.hw1_s2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import static java.util.Objects.isNull;

@Configuration
@RequiredArgsConstructor
public class AdminUserConfig {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${admin.username}")
    private String username;
    @Value("${admin.password}")
    private String password;

    @EventListener(ApplicationReadyEvent.class)
    public void processAdminUser() {
        if (isNull(username) || isNull(password)) {
            throw new RuntimeException("Admin user incorrect credentials (null)");
        }
        var adminUsers = userRepository.findByRole(Role.ADMIN);
        if (adminUsers.size() > 1) {
            throw new RuntimeException("Unexpected: there is more than one administrator");
        }
        UserEntity admin = adminUsers.isEmpty() ? new UserEntity() : adminUsers.get(0);
        admin.setUsername(username);
        admin.setPassword(passwordEncoder.encode(password));
        admin.setRole(Role.ADMIN);
        userRepository.save(admin);
    }
}