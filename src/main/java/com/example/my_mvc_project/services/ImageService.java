package com.example.my_mvc_project.services;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    byte[] get(String name);
    boolean exist(String name);
    String save(MultipartFile file);
    void deleteUnusedImages();
    void delete(String img);
}
