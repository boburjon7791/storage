/*package com.example.my_mvc_project.configurations;

import com.example.my_mvc_project.services.CustomUserDetails;
import com.example.my_mvc_project.utils.BaseUtils;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.springframework.security.core.context.SecurityContextHolder;

@WebListener
public class SessionListener implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        HttpSessionListener.super.sessionCreated(se);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        if (principal instanceof CustomUserDetails customUserDetails) {
//            BaseUtils.users.removeIf(aLong -> aLong.equals(customUserDetails.employee.getId()));
//        }
    }
}*/
