package com.example.my_mvc_project.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "selling")
public class Selling {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @NotNull
    @PositiveOrZero
    @Column(nullable = false)
    private Long count;

    @Builder.Default
    @Column(nullable = false,name = "date_time")
    private LocalDateTime dateTime= LocalDateTime.now();

    @Column(name = "sold_price",nullable = false)
    private Double soldPrice;
}
