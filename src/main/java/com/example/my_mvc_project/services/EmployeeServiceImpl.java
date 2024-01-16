package com.example.my_mvc_project.services;

import com.example.my_mvc_project.dtos.employee.EmployeeCreateDto;
import com.example.my_mvc_project.dtos.employee.EmployeeGetDto;
import com.example.my_mvc_project.dtos.employee.EmployeeUpdateDto;
import com.example.my_mvc_project.entities.Employee;
import com.example.my_mvc_project.entities.Role;
import com.example.my_mvc_project.exceptions.BadParamException;
import com.example.my_mvc_project.exceptions.NotFoundException;
import com.example.my_mvc_project.mappers.EmployeeMapper;
import com.example.my_mvc_project.repositories.EmployeeRepository;
import com.example.my_mvc_project.utils.JwtUtils;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    @Value(value = "${secure.name}")
    private String secureName;
    private final EmployeeRepository employeeRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final EmployeeMapper employeeMapper;

    @Override
    public EmployeeGetDto save(EmployeeCreateDto dto) {
        if (employeeRepository.existsByUsername(dto.username())) {
            throw new BadParamException("Ushbu login allaqachon mavjud");
        }
        System.out.println("dto.password() = " + dto.password());
        Employee employee = employeeMapper.toEntity(dto);
        System.out.println("employee.getPassword() = " + employee.getPassword());
        return employeeMapper.toDto
                (employeeRepository.save
                (employee));
    }
    public String decode(String data){
        byte[] decode = Decoders.BASE64.decode(data);
        return new String(decode);
    }
    @Override
    @Transactional
    public EmployeeGetDto update(EmployeeUpdateDto dto) {
        if (!employeeRepository.existsById(dto.id())) {
            throw new NotFoundException("Ushbu ishchi topilmadi");
        }
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (employeeRepository.existsByUsername(dto.username()) && !principal.getUsername().equals(dto.username())) {
            throw new BadParamException("Ushbu login allaqachon mavjud");
        }
        employeeRepository.updateEmployee(dto.username(), dto.firstName(), dto.lastName(), dto.id());
        return employeeMapper.toDto
                (employeeRepository.findById(dto.id())
                        .orElseThrow(()->new NotFoundException("Foydalanuvchi topilmadi")));
    }

    @Override
    public EmployeeGetDto get(HttpServletRequest request) {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        EmployeeGetDto getDto = employeeMapper.toDto(employeeRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new NotFoundException("Ishchi topilmadi")));
        if (!principal.getUsername().equals(getDto.getUsername())) {
            throw new AccessDeniedException("Siz begona odamning malumotlarini olishga urindingiz. Bu mumkin emas!");
        }
        return getDto;
    }

    @Override
    public Page<EmployeeGetDto> delete(Long id) {
        if (employeeRepository.updateAccountLockedFalseById(false, id)==0){
            throw new NotFoundException("Ishchi topilmadi");
        }
        Page<EmployeeGetDto> reports = employeeRepository.findAll(PageRequest.of(0, 10))
                .map(employeeMapper::toDto);
        if (reports.isEmpty()) {
            throw new NotFoundException("Hisobtlar topilmadi");
        }
        return reports;
    }

    @Override
    public Page<EmployeeGetDto> employees(Pageable pageable) {
        Page<EmployeeGetDto> reports = employeeRepository.findAll(pageable)
                .map(employeeMapper::toDto);
        if (reports.isEmpty()) {
            throw new NotFoundException("Hisobtlar topilmadi");
        }
        return reports;
    }

    @Override
    public Page<EmployeeGetDto> employeesByName(Pageable pageable,String name) {
        Page<EmployeeGetDto> reports = employeeRepository.findAllByName(name, pageable)
                .map(employeeMapper::toDto);
        if (reports.isEmpty()) {
            throw new NotFoundException("Hisobtlar topilmadi");
        }
        return reports;
    }

    @Override
    @Transactional
    public EmployeeGetDto changePassword(String oldPassword, String newPassword, String confirmPassword,long id) {
        if (!newPassword.equals(confirmPassword)) {
            throw new BadParamException("Parol tasdiqlanmagan");
        }
        EmployeeGetDto getDto = get();
        if (!getDto.getId().equals(id)) {
            throw new NotFoundException("Ishchi topilmadi");
        }
        CustomUserDetails userDetails=(CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        System.out.println("oldPassword = " + oldPassword);
        System.out.println("userDetails.getPassword() = " + userDetails.getEmployee().getPassword());
        System.out.println("passwordEncoder.matches(oldPassword,userDetails.getPassword()) = " + passwordEncoder.matches(oldPassword, userDetails.getPassword()));
        if (!passwordEncoder.matches(oldPassword,userDetails.getPassword())) {
            throw new BadParamException("parol noto'g'ri");
        }
        employeeRepository.updatePasswordByPasswordAndId(newPassword, oldPassword, id);
        return getDto;
    }

    @Override
    public void updateRole(long userId, String role) {
        try {
            Role targetRole = Role.valueOf(role);
            employeeRepository.updateRoleById(userId,targetRole);
        }catch (Exception e){
            e.printStackTrace();
            throw new BadParamException(e.getMessage());
        }
    }

    @Override
    public void block(long userId) {
        employeeRepository.updateAccountLockedFalseById(false,userId);
    }

    @Override
    public void active(long userId) {
        employeeRepository.updateAccountLockedFalseById(true,userId);
    }

    @Override
    public EmployeeGetDto get() {
        UserDetails userDetails=(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return employeeMapper.toDto(employeeRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new NotFoundException("Ishchi topilmadi")));
    }

    @Override
    public EmployeeGetDto get(long id) {
        return employeeMapper.toDto(employeeRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Ishchi topilmadi")));
    }

    /*@Override
    public EmployeeGetDto login(String username, String password,HttpServletResponse response) {
        Employee employee = employeeRepository.findByUsername(username)
                .orElseThrow(() -> new BadParamException("Login yoki parol noto'g'ri"));

        System.out.println("password = " + password);
        System.out.println("employee = " + employee);
        if (!passwordEncoder.matches(password,employee.getPassword())) {
            throw new BadParamException("Login yoki parol noto'g'ri");
        }
        System.out.println("employee.getId() = " + employee.getId());
        String userId = encode(employee.getId().toString());
        System.out.println("userId = " + userId);
        String encoded = jwtUtils.encode(userId);
        Cookie cookie = new Cookie(secureName,encoded);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(60*60);
        response.addCookie(cookie);
        return employeeMapper.toDto(employee);
    }*/

    private String encode(String string) {
        return Encoders.BASE64.encode(string.getBytes());
    }

    /*@Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            Arrays.stream(request.getCookies())
                    .collect(Collectors.toSet()).stream()
                    .peek(cookie -> System.out.println(cookie.getName()+": "+cookie.getValue()+" all"))
                    .filter(cookie -> cookie.getName().equals(secureName) || cookie.getName().equals("JSESSIONID"))
                    .peek(cookie -> System.out.println(cookie.getName()+": "+cookie.getValue()+" remove"))
                    .forEach(cookie -> {
                        cookie.setMaxAge(0);
                        cookie.setPath("/");
                        response.addCookie(cookie);
                    });
        }catch (Exception e){
            throw new UnauthorizedException(e.getMessage());
        }
    }*/
}
