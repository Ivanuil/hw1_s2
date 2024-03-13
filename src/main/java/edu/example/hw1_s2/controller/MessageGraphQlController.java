package edu.example.hw1_s2.controller;

import edu.example.hw1_s2.dto.MessageDto;
import edu.example.hw1_s2.security.UserDetailsImpl;
import edu.example.hw1_s2.service.MessageService;
import edu.example.hw1_s2.entity.MessageEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MessageGraphQlController {

    private final MessageService messageService;

    @QueryMapping
    public List<MessageDto> getMessages() {
        return messageService.getMessages();
    }

    @QueryMapping
    public MessageDto getMessage(@Argument Integer id) {
        return messageService.getMessage(id);
    }

    @QueryMapping
    public List<MessageDto> getMessagesForUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return messageService.getMessagesForUser(userDetails.getUsername());
    }

    @MutationMapping
    public MessageEntity saveMessage(@Argument String author,
                                     @Argument String recipient,
                                     @Argument String content) {
        return messageService.saveMessage(author, recipient, content);
    }

}
