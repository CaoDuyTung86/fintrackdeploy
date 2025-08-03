package com.example.be_fintrack.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PurchaseDTO {
    private String productName;
    private double price;
    private String store;
    private String note;
    private String productLink;
    private String purchaseDate;
    private Long userId;
    // getter + setter
}

