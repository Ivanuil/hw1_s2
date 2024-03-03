package edu.example.hw1_s2.security.method;

import edu.example.hw1_s2.entity.MessageEntity;
import edu.example.hw1_s2.repository.MessageRepository;
import edu.example.hw1_s2.security.UserDetailsImpl;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Objects;

@RequiredArgsConstructor
@Component("messageSecurity")
public class MessageSecurity {

    private final MessageRepository messageRepository;

    public boolean isAuthorisedUser(Authentication auth, String username) {
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        return userDetails.getUser().getUsername().equals(username);
    }

    public boolean isAuthorOrRecipient(Authentication auth, Integer messageId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        String username = userDetails.getUser().getUsername();
        MessageEntity message = messageRepository.findById(messageId).orElseThrow(() ->
                new EntityNotFoundException("No message with id: " + messageId));

        return message.getAuthor().equals(username)
                || message.getRecipient().equals(username);
    }

    public boolean isAuthor(Authentication auth, Integer messageId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        String username = userDetails.getUser().getUsername();
        MessageEntity message = messageRepository.findById(messageId).orElseThrow(() ->
                new EntityNotFoundException("No message with id: " + messageId));

        return message.getAuthor().equals(username);
    }

}
