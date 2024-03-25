package edu.example.hw1_s2.dto;

import edu.example.hw1_s2.entity.OperationEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class OperationDto {

    private String content;
    private LocalDateTime time;
    private OperationEntity.OperationType type;

}
