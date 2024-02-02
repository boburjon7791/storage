package com.example.my_mvc_project.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Table(name = "users", indexes = {
        @Index(name = "idx_employee_role", columnList = "role,username,account_non_locked")
}, uniqueConstraints = {
        @UniqueConstraint(name = "uc_user_username", columnNames = {"username"})
})
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Ism bo'sh bo'lishi mumkin emas")
    @Column(name = "first_name",nullable = false)
    private String firstName;

    @NotBlank(message = "Familya bo'sh bo'lishi mumkin emas")
    @Column(name = "last_name",nullable = false)
    private String lastName;

    @NotBlank(message = "Login bo'sh bo'lishi mumkin emas")
    @Column(name = "username",nullable = false)
    private String username;

    @NotBlank(message = "Parol bo'sh bo'lishi mumkin emas")
    @Size(min = 8,message = "Parol 8 ta belgidan kam bo'lishi mumkin emas")
    @Column(name = "password",nullable = false)
    private String password;

    @Column(nullable = false)
    @Builder.Default
    @Enumerated(value = EnumType.STRING)
    private Role role=Role.EMPLOYEE;

    @Builder.Default
    @Column(name = "account_non_locked",nullable = false)
    private Boolean accountNonLocked=false;

    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL, mappedBy = "employee")
    private Set<Selling> sellings;
}
