/*
package com.example.my_mvc_project.configurations;

import com.example.my_mvc_project.exceptions.UnauthorizedException;
import com.example.my_mvc_project.services.CustomUserDetailsService;
import com.example.my_mvc_project.utils.JwtUtils;
import io.jsonwebtoken.io.Decoders;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.UnavailableException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    @Value(value = "${secure.name}")
    private String secureName;
    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService customUserDetailsService;
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        log.info(request.getHeader("User-Agent"));
        if(request.getCookies()==null){
            filterChain.doFilter(request,response);
            return;
        }
        Optional<Cookie> first = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(secureName)
                        & cookie.getValue() != null && !cookie.getValue().isBlank())
                .findFirst();
        if (first.isPresent()) {
            try {
                Cookie cookie = first.get();
                String userId = jwtUtils.decode(cookie.getValue());
                System.out.println("userId = " + userId);
                UserDetails userDetails = customUserDetailsService.loadUserById(decode(userId));
                Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails,null,authorities);
                WebAuthenticationDetails details = new WebAuthenticationDetails(request);
                log.info(details.toString());
                log.info(userDetails.toString());
                authenticationToken.setDetails(details);
                SecurityContext context = SecurityContextHolder.getContext();
                context.setAuthentication(authenticationToken);
            }catch (Exception e){
                e.printStackTrace();
            }
            finally {
                filterChain.doFilter(request,response);
            }
        }
    }
    private Long decode(String date){
        byte[] decode = Decoders.BASE64.decode(date);
        return Long.parseLong(new String(decode));
    }
}
*/
