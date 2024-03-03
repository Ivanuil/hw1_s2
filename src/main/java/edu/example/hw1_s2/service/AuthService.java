package edu.example.hw1_s2.service;


import edu.example.hw1_s2.dto.AuthUserDto;
import edu.example.hw1_s2.dto.LoginRequestDto;
import edu.example.hw1_s2.dto.RegisterRequestDto;
import edu.example.hw1_s2.entity.UserEntity;
import edu.example.hw1_s2.repository.UserRepository;
import edu.example.hw1_s2.repository.exception.UnprocessableEntityException;
import edu.example.hw1_s2.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final TokenService tokenService;

    /**
     * Creates user in the database with given username and password. It generates a token based on
     * username and saves that token in the database.
     * @param registerRequest object that contains username and password
     * @return user dto with roles and generated token
     * @throws UnprocessableEntityException if user with that username already exists
     */
    public AuthUserDto register(RegisterRequestDto registerRequest) throws UnprocessableEntityException {
        if (userRepository.existsById(registerRequest.getUsername())) {
            throw new UnprocessableEntityException("User with that username already exists");
        }
        UserEntity user = new UserEntity();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        userRepository.save(user);

        String generatedToken = jwtService.generateFromUser(user);
        tokenService.saveNewToken(generatedToken, user);
        return new AuthUserDto(generatedToken, user.getUsername());
    }

    /**
     * Authenticates the user with provided username and password. It creates new active token and associates it
     * with the user. All existing user token are deactivated.
     * @param loginRequest object that contains username and password
     * @return user dto with roles and generated token
     */
    @Transactional
    public AuthUserDto login(LoginRequestDto loginRequest) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                loginRequest.getPassword()));
        UserEntity user = userRepository.findById(loginRequest.getUsername())
                .orElseThrow(() -> new IllegalStateException("Authenticated user cannot be null"));

        tokenService.deactivateUserTokens(user.getUsername());
        String generatedToken = jwtService.generateFromUser(user);
        tokenService.saveNewToken(generatedToken, user);
        return new AuthUserDto(generatedToken, user.getUsername());
    }
}
