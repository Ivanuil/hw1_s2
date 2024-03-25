package edu.example.hw1_s2.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document
@AllArgsConstructor
public class OperationEntity {

    @Id
    private String id;
    private String content;
    private LocalDateTime time;
    private OperationType type;

    public enum OperationType {
        WRITE, READ
    }

}
