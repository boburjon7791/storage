package com.example.my_mvc_project.configurations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;

@EnableMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true
)
@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.headers(headers -> headers.xssProtection(xXssConfig ->
                        xXssConfig.headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK))
                        .contentSecurityPolicy(cps-> cps.policyDirectives("script-src 'self'"))
                )
                .authorizeHttpRequests(registry ->
                        registry.requestMatchers(
                                        "/auth/save",
                                        "/login",
                                        "/logout",
                                        "/css/**",
                                        "/js/**",
                                        "/error_pages/**",
                                        "/favicon.ico"
                                )
                        .permitAll()
                        .anyRequest().authenticated())
                .formLogin(configurer -> {
                    configurer.permitAll();
                    configurer.defaultSuccessUrl("/",true);
                })
                .logout(LogoutConfigurer::permitAll).build();
    }
}
