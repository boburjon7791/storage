package com.example.my_mvc_project.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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

    @NotNull(message = "Sotuv miqdori bo'sh bo'lmasligi kerak")
    @Positive(message = "Sotuv miqdori 0 katta bo'lishi kerak")
    @Column(nullable = false)
    private Long count;

    @Builder.Default
    @Column(nullable = false,name = "date_time")
    private LocalDateTime dateTime= LocalDateTime.now();

    @NotNull(message = "Sotuv narxi bo'sh bo'lmaslgi kerak")
    @PositiveOrZero(message = "Sotuv narxi 0 yoki undan katta bo'lishi kerak")
    @Column(name = "sold_price",nullable = false)
    private Double soldPrice;

    @ManyToOne(fetch = FetchType.LAZY, optional = false,cascade = CascadeType.ALL)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;
}
