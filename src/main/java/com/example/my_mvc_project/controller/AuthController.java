package com.example.my_mvc_project.controller;

import com.example.my_mvc_project.dtos.employee.EmployeeCreateDto;
import com.example.my_mvc_project.dtos.employee.EmployeeGetDto;
import com.example.my_mvc_project.dtos.employee.EmployeeUpdateDto;
import com.example.my_mvc_project.services.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final EmployeeService employeeService;
    @GetMapping("/save")
    public String save(){
        return "auth/register";
    }
    @PreAuthorize("hasAnyRole('MANAGER','SUPER_MANAGER')")
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
    @GetMapping("/change-password")
    public String changePassword(Model model){
        EmployeeGetDto getDto=employeeService.get();
        model.addAttribute("id",getDto.getId());
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
}
