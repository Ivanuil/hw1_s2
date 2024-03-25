package edu.example.hw1_s2.mapper;

import edu.example.hw1_s2.dto.ImageDto;
import edu.example.hw1_s2.entity.ImageEntity;
import edu.example.hw1_s2.service.MinioFileStorageService.FileSaveResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ImageMapper {

    ImageDto toImageDto(ImageEntity image);

    @Mapping(source = "savedFilename", target = "savedByName")
    @Mapping(source = "originalName", target = "originName")
    ImageEntity toImageEntity(FileSaveResult fsr);

}
