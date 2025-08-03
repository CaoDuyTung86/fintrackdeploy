package com.example.be_fintrack.entity;

import jakarta.persistence.*;
        import lombok.*;
        import java.time.LocalDate;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type; // income / expense

    private String category;

    private Double amount;

    private String description;


    private LocalDate date;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}

