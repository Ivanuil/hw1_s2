package edu.example.hw1_s2.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "image")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ImageEntity {

    @Id
    @Column(name = "saved_by_name")
    private String savedByName;

    @Column(name = "origin_name")
    private String originName;

    private Long size;

}
