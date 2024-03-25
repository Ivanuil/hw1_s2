package edu.example.hw1_s2.repository;

import edu.example.hw1_s2.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<ImageEntity, Integer> {

    Optional<ImageEntity> findBySavedByName(String name);

}
