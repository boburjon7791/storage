package com.example.my_mvc_project.configurations;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@EnableMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true
)
@Slf4j
@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfiguration {
    public static Map<String,Integer> ipAddresses=new ConcurrentHashMap<>();
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.headers(headers -> headers.xssProtection(xXssConfig ->
                        xXssConfig.headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK))
                        .contentSecurityPolicy(cps-> cps.policyDirectives("script-src 'self'"))
                )
                .authorizeHttpRequests(registry ->
                        registry.requestMatchers(
                           "/auth/save",
                           "/auth/login",
                           "/login",
                           "/logout",
                           "/css/**",
                           "/js/**",
                           "/error_pages/**",
                           "/favicon.ico",
                           "/source/**"
                         )
                        .permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(configurer ->
                        configurer.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .formLogin(configurer1 -> {
                            configurer1.permitAll();
                            configurer1.usernameParameter("uname");
                            configurer1.passwordParameter("pswd");
                            configurer1.loginPage("/auth/login");
                            configurer1.successForwardUrl("/");
                            configurer1.successHandler(authenticationSuccessHandler());
//                            configurer1.failureHandler(authenticationFailureHandler());
                        }
                ).exceptionHandling(configurer -> configurer.authenticationEntryPoint(
                        (request, response, authException) -> {
                            addIpAddressToList(request.getRemoteAddr());
                            response.sendRedirect("auth/login");
                        }))
                .logout(configurer2 -> {
                    configurer2.permitAll();
                    configurer2.logoutUrl("/logout");
                    configurer2.logoutSuccessHandler((request, response, authentication) ->
                            response.sendRedirect("/auth/login"));
                    configurer2.logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
                })
                .addFilterBefore(oncePerRequestFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }
    @Bean
    public OncePerRequestFilter oncePerRequestFilter(){
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(@NonNull HttpServletRequest request,
                                            @NonNull HttpServletResponse response,
                                            @NonNull FilterChain filterChain) throws ServletException, IOException {
                if (ipAddresses.containsKey(request.getRemoteAddr()) && ipAddresses.get(request.getRemoteAddr())>=3) {
                    System.out.println("request.getRemoteAddr() = " + request.getRemoteAddr());
                    response.getWriter().println("Xatolik bo'ldi. Keyinroq urinib ko'ring");
                    return;
                }
                filterChain.doFilter(request,response);
            }
        };
    }
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler(){
        return (request, response, authentication) ->{
            removeIpAddressFromList(request.getRemoteAddr());
            response.sendRedirect("/");
        };
    }
    public void removeIpAddressFromList(String ipAddress){
        System.out.println("request.getRemoteAddr() = " + ipAddress);
        ipAddresses.remove(ipAddress);
    }
    public void addIpAddressToList(String ipAddress){
        System.out.println("request.getRemoteAddr() = " + ipAddress);
        if (ipAddresses.containsKey(ipAddress)) {
            synchronized (new Object()){
                ipAddresses.put(ipAddress,ipAddresses.get(ipAddress)+1);
            }
            return;
        }
        ipAddresses.put(ipAddress,1);
    }
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler(){
        return (request, response, exception) ->
                addIpAddressToList(request.getRemoteAddr());
    }
}
