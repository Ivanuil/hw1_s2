package edu.example.hw1_s2.mapper;

import edu.example.hw1_s2.dto.ImageDto;
import edu.example.hw1_s2.entity.ImageEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ImageMapper {

    ImageDto toImageDto(ImageEntity image);

}
