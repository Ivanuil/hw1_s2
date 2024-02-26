package edu.example.hw1_s2.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class MessageDto implements Serializable {

    private Integer id;

    private String author;

    private String recipient;

    private String content;

    private Timestamp time;

    private ImageDto image;

}
