package edu.example.hw1_s2.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class ImageDto implements Serializable {

    private String savedByName;

    private String originName;

    private Long size;

}
