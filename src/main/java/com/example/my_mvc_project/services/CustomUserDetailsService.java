package com.example.my_mvc_project.services;

import com.example.my_mvc_project.entities.Employee;
import com.example.my_mvc_project.exceptions.NotFoundException;
import com.example.my_mvc_project.exceptions.UnauthorizedException;
import com.example.my_mvc_project.repositories.EmployeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
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
        UserDetails userDetails = User.withUsername(username)
                .password(employee.getPassword())
                .passwordEncoder(passwordEncoder::encode)
                .roles(employee.getRole().name())
                .credentialsExpired(false)
                .accountExpired(false)
                .accountLocked(false)
                .disabled(false)
                .build();
        System.out.println("userDetails.getPassword() = " + userDetails.getPassword());
        System.out.println("userDetails = " + userDetails);
        return userDetails;
    }
    public UserDetails loadUserById(Long id) throws UsernameNotFoundException {
        Employee employee = employeeRepository.findByIdAndAccountNonLockedFalse(id)
                .orElseThrow(() -> new UnauthorizedException("Unauthorized"));
        return new CustomUserDetails(employee);
    }
}
