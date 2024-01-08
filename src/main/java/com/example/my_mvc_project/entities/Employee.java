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
        @Index(name = "idx_employee_role", columnList = "role,username,account_locked")
}, uniqueConstraints = {
        @UniqueConstraint(name = "uc_user_username", columnNames = {"username"})
})
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "first_name",nullable = false)
    private String firstName;

    @NotBlank
    @Column(name = "last_name",nullable = false)
    private String lastName;

    @NotBlank
    @Column(name = "username",nullable = false)
    private String username;

    @NotBlank
    @Size(min = 8)
    @Column(name = "password",nullable = false)
    private String password;

    @Column(nullable = false)
    @Builder.Default
    @Enumerated(value = EnumType.STRING)
    private Role role=Role.EMPLOYEE;

    @Builder.Default
    @Column(name = "account_non_locked",nullable = false)
    private Boolean accountNonLocked=true;

    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL, mappedBy = "employee")
    private Set<Selling> sellings;
}
