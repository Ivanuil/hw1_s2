package edu.example.hw1_s2.mapper;

import edu.example.hw1_s2.dto.MessageDto;
import edu.example.hw1_s2.entity.MessageEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ImageMapper.class})
public interface MessageMapper {

    @Mapping(source = "image", target = "image")
    MessageDto toMessageDto(MessageEntity message);

    List<MessageDto> toMessageDtoList(List<MessageEntity> messages);

}
