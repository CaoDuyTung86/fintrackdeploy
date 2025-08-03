package com.example.be_fintrack.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Reminder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private LocalDate remindDate;
    private boolean done;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
