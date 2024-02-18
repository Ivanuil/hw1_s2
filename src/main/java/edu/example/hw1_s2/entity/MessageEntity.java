package edu.example.hw1_s2.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "message")
@AllArgsConstructor
@NoArgsConstructor
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "message_seq")
    @SequenceGenerator(name = "message_seq", sequenceName = "message_seq", allocationSize = 1)
    private Integer id;

    @Column
    private String author;

    @Column
    private String recipient;

    @Column
    private String content;

    @Column
    @CreationTimestamp
    private Timestamp time;

}
