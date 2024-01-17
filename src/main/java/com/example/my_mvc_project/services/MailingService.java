package com.example.my_mvc_project.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MailingService {
    @Async
    public void sendMessage(String message){
        log.info("message sent");
        System.out.println(message);
    }
}
