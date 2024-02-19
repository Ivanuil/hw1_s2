package edu.example.hw1_s2.service;

import edu.example.hw1_s2.entity.MessageEntity;
import edu.example.hw1_s2.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    public List<MessageEntity> getMessages() {
        return messageRepository.findAll();
    }

    public MessageEntity getMessage(Integer id) {
        return messageRepository.findById(id).orElse(null);
    }

    public MessageEntity saveMessage(String author, String recipient, String content) {
        return messageRepository.save(new MessageEntity(
                0, author, recipient, content, null
        ));
    }

}
