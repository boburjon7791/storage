package com.example.my_mvc_project.dtos.employee;

import com.example.my_mvc_project.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class EmployeeGetDto{
        private Long id;

        private String firstName;

        private String lastName;

        private String username;

        private Boolean active;

        @Builder.Default
        private Role role=Role.EMPLOYEE;
}
