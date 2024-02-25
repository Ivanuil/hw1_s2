package edu.example.hw1_s2.controller;


import edu.example.hw1_s2.entity.MessageEntity;
import edu.example.hw1_s2.repository.exception.FileReadException;
import edu.example.hw1_s2.service.MessageService;
import edu.example.hw1_s2.service.MinioFileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class ImageController {

    private final MinioFileStorageService minioFileStorageService;
    private final MessageService messageService;

    @GetMapping
    public ResponseEntity<Resource> getImageForMessage(@RequestParam Integer messageId) throws FileReadException {
        var message = messageService.getMessage(messageId);
        var image = minioFileStorageService.getModel(message.getImage().getSavedByName());

        InputStream fileInputStream = minioFileStorageService.get(image.getSavedByName());
        if (fileInputStream == null) {
            return ResponseEntity.notFound().build();
        }

        InputStreamResource resource = new InputStreamResource(fileInputStream);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +
                        URLEncoder.encode(image.getOriginName(), StandardCharsets.UTF_8) + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @PostMapping
    public MessageEntity attachImage(@RequestPart Integer messageId,
                                     @RequestBody MultipartFile file) {

        return messageService.attachImage(messageId, file);

    }

}