package com.example.be_fintrack.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class InvestmentDTO {
    private Long id;
    private String assetType;
    private String assetName;
    private double quantity;
    private double purchasePrice;
    private double currentPrice;
    private LocalDate purchaseDate;
    private double profitOrLoss;

    private Long userId;
    private String username;
}
