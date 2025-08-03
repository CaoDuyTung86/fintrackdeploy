package com.example.be_fintrack.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Investment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String assetType;     // cổ phiếu, vàng, crypto...
    private String assetName;     // BTC, vàng SJC, FPT...

    private double quantity;
    private double purchasePrice;
    private double currentPrice;


    private LocalDate purchaseDate;

    @ManyToOne
    private User user;

    public double getProfitOrLoss() {
        return (currentPrice - purchasePrice) * quantity;
    }
}
