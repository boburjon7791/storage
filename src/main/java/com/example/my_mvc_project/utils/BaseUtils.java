package com.example.my_mvc_project.utils;

import org.springframework.stereotype.Component;

@Component
public class BaseUtils {
    public String setPointToNumber(String number){
        StringBuilder sb = new StringBuilder(number);
        for (int i = sb.length() - 3; i > 0; i -= 3) {
            sb.insert(i, '.');
        }
        return sb.toString();
    }
}
