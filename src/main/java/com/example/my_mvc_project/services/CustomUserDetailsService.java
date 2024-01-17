package com.example.my_mvc_project.services;

import com.example.my_mvc_project.entities.Employee;
import com.example.my_mvc_project.exceptions.NotFoundException;
import com.example.my_mvc_project.repositories.EmployeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("username = " + username);
        Employee employee = employeeRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        UserDetails userDetails = new CustomUserDetails(employee);
        System.out.println("userDetails.getPassword() = " + userDetails.getPassword());
        System.out.println("userDetails = " + userDetails);
        System.out.println("employee = " + employee);
        return userDetails;
    }
}
