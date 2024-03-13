package edu.example.hw1_s2.security.provider;

import edu.example.hw1_s2.entity.TokenEntity;
import edu.example.hw1_s2.service.TokenService;
import edu.example.hw1_s2.security.exception.InvalidTokenException;
import edu.example.hw1_s2.security.jwt.JwtAuthentication;
import edu.example.hw1_s2.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import java.util.Optional;

@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenService tokenService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String tokenValue = (String) authentication.getCredentials();
        String subject;
        try {
            subject = jwtService.extractSubject(tokenValue);
        } catch (RuntimeException e) {
            throw new InvalidTokenException("Token is invalid", e);
        }
        Optional<TokenEntity> tokenOptional = tokenService.findByTokenValue(tokenValue);
        if (tokenOptional.isEmpty()) {
            return null;
        }
        TokenEntity token = tokenOptional.get();
        if (!token.isActive()) {
            throw new InvalidTokenException("Token is not active");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(subject);
        JwtAuthentication auth = new JwtAuthentication(tokenValue, subject, userDetails);
        auth.setAuthenticated(true);
        return auth;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthentication.class.isAssignableFrom(authentication);
    }
}
