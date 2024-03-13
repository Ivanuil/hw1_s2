package edu.example.hw1_s2.security;

import edu.example.hw1_s2.entity.Role;
import edu.example.hw1_s2.entity.UserEntity;
import edu.example.hw1_s2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findById(username).orElseThrow(() ->
                new UsernameNotFoundException("Unable to find user with username: " + username));

        if (isNull(user.getRole())) {
            user.setRole(Role.USER);
        }

        Set<GrantedAuthority> roles = user.getRole().getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toSet());
        return new UserDetailsImpl(user, roles);
    }
}
