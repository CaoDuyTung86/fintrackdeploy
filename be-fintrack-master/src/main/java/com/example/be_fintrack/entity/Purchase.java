package com.example.be_fintrack.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;
    private double price;
    private String store;           // Ví dụ: Shopee, Tiki, v.v.
    private String note;
    private String productLink;
    private LocalDate purchaseDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
