package edu.example.hw1_s2.repository;

import edu.example.hw1_s2.entity.Role;
import edu.example.hw1_s2.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, String> {

    List<UserEntity> findByRole(Role role);

}
