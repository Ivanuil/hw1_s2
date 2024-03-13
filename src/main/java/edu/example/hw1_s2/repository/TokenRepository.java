package edu.example.hw1_s2.repository;

import edu.example.hw1_s2.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<TokenEntity, Long> {

    Optional<TokenEntity> findByToken(String tokenValue);

    List<TokenEntity> findByUser_UsernameAndIsActive(String username, boolean isActive);

}