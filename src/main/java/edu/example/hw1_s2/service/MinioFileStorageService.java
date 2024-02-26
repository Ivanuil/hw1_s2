package edu.example.hw1_s2.service;

import edu.example.hw1_s2.entity.ImageEntity;
import edu.example.hw1_s2.repository.ImageRepository;
import edu.example.hw1_s2.repository.MinioFileStorage;
import edu.example.hw1_s2.repository.exception.FileReadException;
import edu.example.hw1_s2.repository.exception.FileWriteException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MinioFileStorageService implements ImageStorageService {

    private static final int MAX_ATTEMPTS_TO_GEN_FILENAME = 66;

    private final MinioFileStorage imageStorage;
    private final ImageRepository imageRepository;

    /**
     * Save file in the Minio storage with file extension as prefix
     * @param file file to save
     * @return generated file name
     */
    public String saveFile(MultipartFile file) throws FileWriteException {

        String fileExt = FilenameUtils.getExtension(file.getOriginalFilename());
        String generatedFileName = String.format("%s/%s", fileExt, UUID.randomUUID());

        int tryCount = 0;
        while (imageStorage.isObjectExist(generatedFileName)) {
            if (tryCount++ > MAX_ATTEMPTS_TO_GEN_FILENAME) {
                throw new RuntimeException("Object with generated name already exists. This is an internal error");
            }
            generatedFileName = String.format("%s/%s", fileExt, UUID.randomUUID());
        }
        try {
            imageStorage.saveObject(generatedFileName, file.getSize(), file.getInputStream());
        } catch (FileWriteException | IOException e) {
            throw new FileWriteException(e);
        }

        return generatedFileName;
    }

    @Override
    public List<FileSaveResult> save(List<MultipartFile> files) throws FileWriteException {
        List<FileSaveResult> result = new ArrayList<>();
        for (var file : files) {
            try {
                result.add(new FileSaveResult(file.getOriginalFilename(), saveFile(file)));
            } catch (FileWriteException ex) {
                for (var saved : result) {
                    try {
                        imageStorage.deleteObject(saved.getSavedFilename());
                    } catch (FileWriteException ignored) {}
                }
                throw new FileWriteException(ex);
            }
        }
        return result;
    }

    @Override
    public InputStream get(String filename) throws FileReadException {
        return imageStorage.getObject(filename);
    }

    @Override
    public ImageEntity getModel(String filename) {
        return imageRepository.findBySavedByName(filename).orElseThrow(() -> new EntityNotFoundException("File not found"));
    }

    @Override
    public void delete(String filename) throws FileWriteException {
        imageStorage.deleteObject(filename);
    }

    @Data
    @AllArgsConstructor
    public static class FileSaveResult {
        private String originalName;
        private String savedFilename;
    }
}
