package edu.example.hw1_s2.service;

import edu.example.hw1_s2.AllowedImageExtension;
import edu.example.hw1_s2.entity.ImageEntity;
import edu.example.hw1_s2.entity.MessageEntity;
import edu.example.hw1_s2.repository.ImageRepository;
import edu.example.hw1_s2.repository.MessageRepository;
import edu.example.hw1_s2.repository.exception.FileWriteException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final ImageRepository imageRepository;
    private final ImageStorageService imageStorageService;

    public List<MessageEntity> getMessages() {
        return messageRepository.findAll();
    }

    public MessageEntity getMessage(Integer id) {
        return messageRepository.findById(id).orElse(null);
    }

    public MessageEntity saveMessage(String author, String recipient, String content) {
        return messageRepository.save(new MessageEntity(
                0, author, recipient, content, null, null
        ));
    }

    @Transactional
    public MessageEntity attachImage(Integer messageId, MultipartFile file) {
        var message = messageRepository.findById(messageId)
                .orElseThrow(() -> new EntityNotFoundException("No message with such id"));

        try {
            AllowedImageExtension.caseIgnoreValueOf(FilenameUtils.getExtension(file.getOriginalFilename()));
        } catch (Exception e) {
            throw new ConstraintViolationException(String.format("File extension %s not allowed",
                    FilenameUtils.getExtension(file.getOriginalFilename())), null);
        }
        try {
            var image = imageStorageService.save(List.of(file)).get(0);
            var imageEntity = new ImageEntity();
            imageEntity.setOriginName(image.getOriginalName());
            imageEntity.setSavedByName(image.getSavedFilename());
            message.setImage(imageEntity);

            imageRepository.save(imageEntity);
            messageRepository.save(message);
        } catch (FileWriteException e) {
            throw new RuntimeException(e);
        }

        return message;
    }

}
