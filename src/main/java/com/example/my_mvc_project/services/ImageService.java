package com.example.my_mvc_project.services;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface ImageService {
    Map<String, LocalDateTime> cachedImages =new ConcurrentHashMap<>();
    byte[] get(String name);
    String save(MultipartFile file,boolean save);
    void delete(String img);
}
