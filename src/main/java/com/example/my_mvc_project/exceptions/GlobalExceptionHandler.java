package com.example.my_mvc_project.exceptions;

import com.example.my_mvc_project.services.MailingService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MailingService mailingService;

    @ExceptionHandler({ForbiddenException.class,AccessDeniedException.class})
    public ModelAndView handler(RuntimeException e){
        ModelAndView modelAndView = new ModelAndView("error_pages/forbidden");
        modelAndView.addObject("value",e.getMessage());
        return modelAndView;
    }
    @ExceptionHandler(BadParamException.class)
    public ModelAndView handler(BadParamException e){
        log.warn(e.getMessage());
        ModelAndView modelAndView = new ModelAndView("error_pages/bad_request");
        modelAndView.addObject("value",e.getMessage());
        return modelAndView;
    }
    @ExceptionHandler(ConstraintViolationException.class)
    public ModelAndView handlerValidationExceptions(ConstraintViolationException e){
        log.warn(e.getMessage());
        StringBuilder sb=new StringBuilder();
        List<ConstraintViolation<?>> list = e.getConstraintViolations().stream().toList();
        for (int i = 1; i <= list.size(); i++) {
            sb.append(i+1).append(". ").append(list.get(i).getMessage()).append("\n");
        }
        ModelAndView modelAndView = new ModelAndView("error_pages/bad_request");
        modelAndView.addObject("value",sb.toString());
        return modelAndView;
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ModelAndView handlerValidationExceptions(MethodArgumentNotValidException e){
        log.warn(e.getMessage());
        e.printStackTrace();
        StringBuilder sb=new StringBuilder();
        assert e.getDetailMessageArguments()!=null;
        List<Object> list = Arrays.stream((e.getDetailMessageArguments())).toList();
        for (Object o : list) {
            sb.append(o).append("\n");
        }
        ModelAndView modelAndView = new ModelAndView("error_pages/bad_request");
        modelAndView.addObject("value",sb.toString());
        return modelAndView;
    }
    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ModelAndView handler(InternalAuthenticationServiceException e){
        log.warn(e.getMessage());
        return new ModelAndView("auth/login");
    }

    @ExceptionHandler(DailyReportNotFoundException.class)
    public ModelAndView handler(DailyReportNotFoundException e){
        ModelAndView modelAndView = new ModelAndView("error_pages/not_found_daily");
        modelAndView.addObject("value",e.getMessage());
        modelAndView.addObject("months", Month.values());
        List<LocalDate> localDates=new LinkedList<>();
        LocalDate start = LocalDate.of(Year.now().minusYears(10).getValue(), 1, 1);
        while (!start.isAfter(LocalDate.now())) {
            localDates.add(start);
            start = start.plusDays(1);
        }
        Set<Integer> years = localDates.stream()
                .map(LocalDate::getYear)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toCollection(LinkedHashSet::new));
        log.warn(e.getMessage());
        modelAndView.addObject("years",years);
        return modelAndView;
    }


    @ExceptionHandler(MonthlyReportNotFoundException.class)
    public ModelAndView handler(MonthlyReportNotFoundException e){
        log.warn(e.getMessage());
        ModelAndView modelAndView = new ModelAndView("error_pages/not_found_monthly");
        modelAndView.addObject("value",e.getMessage());
        return modelAndView;
    }

    @ExceptionHandler({AuthenticationException.class, UnauthorizedException.class})
    public ModelAndView handlerException(RuntimeException e){
        return new ModelAndView("auth/login");
    }

    @ExceptionHandler({FileNotFoundException.class, NotFoundException.class, NoResourceFoundException.class})
    public ModelAndView handlerNotFount(Exception e){
        log.warn(e.getMessage());
        ModelAndView modelAndView = new ModelAndView("error_pages/not_found");
        modelAndView.addObject("value",e.getMessage());
        return modelAndView;
    }


    @ExceptionHandler(Exception.class)
    public ModelAndView handler(Exception e){
        // TODO: 30/12/2023 send a message
        StringBuilder sb = new StringBuilder();
        sb.append("\n")
                .append("----------")
                .append(e.getClass().getName())
                .append("\n")
                .append(e.getMessage())
                .append("\n")
                .append(e.getLocalizedMessage())
                .append("\n");
        for (StackTraceElement element : e.getStackTrace()) {
            sb.append(element).append("\n");
        }
        sb.append("----------")
                .append("\n");
        mailingService.sendMessage(sb.toString());
        log.error("");
        log.error(e.getClass().getName());
        log.error(e.getMessage());
        Arrays.stream(e.getStackTrace())
                .forEach(element->log.error(element.toString()));
        log.error("");
        ModelAndView modelAndView = new ModelAndView("error_pages/server_error");
        modelAndView.addObject("value",e.getMessage());
        return modelAndView;
    }
}
