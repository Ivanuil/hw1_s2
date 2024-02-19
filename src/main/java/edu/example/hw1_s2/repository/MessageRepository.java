package edu.example.hw1_s2.repository;

import edu.example.hw1_s2.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<MessageEntity, Integer> {

}
