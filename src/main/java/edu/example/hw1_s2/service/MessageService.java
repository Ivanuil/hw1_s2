package edu.example.hw1_s2.service;

import edu.example.hw1_s2.config.AllowedImageExtension;
import edu.example.hw1_s2.dto.MessageDto;
import edu.example.hw1_s2.dto.OperationDto;
import edu.example.hw1_s2.entity.ImageEntity;
import edu.example.hw1_s2.entity.MessageEntity;
import edu.example.hw1_s2.entity.OperationEntity;
import edu.example.hw1_s2.mapper.MessageMapper;
import edu.example.hw1_s2.repository.ImageRepository;
import edu.example.hw1_s2.repository.MessageRepository;
import edu.example.hw1_s2.repository.exception.FileWriteException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final ImageRepository imageRepository;
    private final ImageStorageService imageStorageService;
    private final OperationService operationService;

    private final MessageMapper messageMapper;

    public List<MessageDto> getMessages() {
        var messages = messageMapper.toMessageDtoList(messageRepository.findAll());

        operationService.logOperation(new OperationDto(
                String.format("getMessages: %s", messages),
                LocalDateTime.now(),
                OperationEntity.OperationType.READ
        ));

        return messages;
    }

    @Cacheable(value = "MessageService::getMessage", key = "#id")
    public MessageDto getMessage(Integer id) {
        var message = messageMapper.toMessageDto(messageRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("No message with this id")));

        operationService.logOperation(new OperationDto(
                String.format("getMessage: %s", message),
                LocalDateTime.now(),
                OperationEntity.OperationType.READ
        ));

        return message;
    }

    public MessageEntity saveMessage(String author, String recipient, String content) {
        var message = new MessageEntity(
                0, author, recipient, content, null, null);

        operationService.logOperation(new OperationDto(
                String.format("getMessage: %s", message),
                LocalDateTime.now(),
                OperationEntity.OperationType.WRITE
        ));

        return messageRepository.save(message);
    }

    @Transactional
    @CacheEvict(value = "MessageService::getMessage", key = "#messageId")
    public MessageDto attachImage(Integer messageId, MultipartFile file) {
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
            imageEntity.setSize(file.getSize());

            imageEntity = imageRepository.save(imageEntity);
            message.setImage(imageEntity);
            messageRepository.save(message);
        } catch (FileWriteException e) {
            throw new RuntimeException(e);
        }

        operationService.logOperation(new OperationDto(
                String.format("attachImage: %s", message),
                LocalDateTime.now(),
                OperationEntity.OperationType.WRITE
        ));

        return messageMapper.toMessageDto(message);
    }

}
