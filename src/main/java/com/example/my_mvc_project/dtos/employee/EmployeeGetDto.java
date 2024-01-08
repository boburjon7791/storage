package com.example.my_mvc_project.dtos.employee;

import com.example.my_mvc_project.entities.Role;
import lombok.Data;

@Data
public class EmployeeGetDto{
        private Long id;

        private String firstName;

        private String lastName;

        private String username;

        private Boolean active;

        private Role role=Role.EMPLOYEE;
}
