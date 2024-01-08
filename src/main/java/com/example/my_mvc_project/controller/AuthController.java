package com.example.my_mvc_project.controller;

import com.example.my_mvc_project.dtos.employee.EmployeeCreateDto;
import com.example.my_mvc_project.dtos.employee.EmployeeGetDto;
import com.example.my_mvc_project.dtos.employee.EmployeeUpdateDto;
import com.example.my_mvc_project.exceptions.UnauthorizedException;
import com.example.my_mvc_project.services.EmployeeService;
import io.jsonwebtoken.io.Decoders;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final EmployeeService employeeService;
    @Value(value = "${secure.name}")
    private String secureName;
    @GetMapping("/save")
    public String save(){
        return "auth/register";
    }
    @PreAuthorize("permitAll()")
    @PostMapping("/save")
    public void save(HttpServletResponse response,@Valid @ModelAttribute EmployeeCreateDto dto, Model model){
        System.out.println("dto.password() = " + dto.password());
        EmployeeGetDto saved = employeeService.save(dto);
        model.addAttribute("emp",saved);
        try {
            response.sendRedirect("/login");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @GetMapping("/profile")
    public String profile(HttpServletRequest request,Model model){
        EmployeeGetDto getDto = employeeService.get(request);
        model.addAttribute("emp",getDto);
        return "auth/profile";
    }
    @PostMapping("/update")
    public String update(@Valid @ModelAttribute EmployeeUpdateDto dto,Model model){
        EmployeeGetDto updated = employeeService.update(dto);
        model.addAttribute("emp",updated);
        return "auth/profile";
    }
    /*@GetMapping("/login")
    public String login(){
        return "auth/login";
    }
    @PostMapping("/login")
    public String login(
            @RequestParam(name = "uname") String username,
            @RequestParam(name = "pwd") String password,
            Model model,
            HttpServletResponse response
    ){
        System.out.println("password = " + password);
        EmployeeGetDto login = employeeService.login(username, password,response);
        model.addAttribute("emp",login);
        System.out.println("login = " + login);
        return "auth/profile";
    }
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        employeeService.logout(request,response);
        return "auth/login";
    }*/
    @GetMapping("/change-password")
    public String changePassword(@RequestParam long id,Model model){
        model.addAttribute("id",id);
        return "auth/change_password";
    }
    @PostMapping("/change-password")
    public String changePassword(
            @RequestParam(name = "u_id")long userId,
            @RequestParam(name = "n_p") String newPassword,
            @RequestParam(name = "o_p")String oldPassword,
            @RequestParam(name = "c_p")String confirmPassword,
            Model model
            ){
        EmployeeGetDto getDto = employeeService.changePassword(
                oldPassword,newPassword,confirmPassword,userId);
        model.addAttribute("emp",getDto);
        return "auth/profile";
    }
    public String decode(String data){
        byte[] decode = Decoders.BASE64.decode(data);
        return new String(decode);
    }
}
