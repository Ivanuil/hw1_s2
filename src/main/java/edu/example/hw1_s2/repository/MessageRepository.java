package edu.example.hw1_s2.repository;

import edu.example.hw1_s2.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<MessageEntity, Integer> {

    List<MessageEntity> findAllByAuthorOrRecipient(String author, String recipient);

}
