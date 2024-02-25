package edu.example.hw1_s2.service;

import edu.example.hw1_s2.entity.ImageEntity;
import edu.example.hw1_s2.repository.exception.FileReadException;
import edu.example.hw1_s2.repository.exception.FileWriteException;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

public interface ImageStorageService {

    String saveFile(MultipartFile file) throws FileWriteException;

    List<MinioFileStorageService.FileSaveResult> save(List<MultipartFile> files) throws FileWriteException;

    InputStream get(String filename) throws FileReadException;

    ImageEntity getModel(String filename);

    void delete(String filename) throws FileWriteException;
}
