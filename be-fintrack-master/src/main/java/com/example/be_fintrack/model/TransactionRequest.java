package com.example.be_fintrack.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TransactionRequest {
    private double amount;
    private String description;
    private LocalDate date;
    private Long categoryId;
    private String type;

}
