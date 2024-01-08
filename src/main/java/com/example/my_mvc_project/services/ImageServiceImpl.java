package com.example.my_mvc_project.services;

import com.example.my_mvc_project.entities.Product;
import com.example.my_mvc_project.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@AllArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final Path path=Path.of("images");
    static File file=new File("images");
    static {
        if (!file.exists()) {
            System.out.println("images = " + file.mkdirs());
        }
    }
    private final ProductRepository productRepository;

    @Override
    @SneakyThrows
    public byte[] get(String name) {
        return Files.readAllBytes(Path.of(path+"/"+name));
    }

    @Override
    public boolean exist(String name) {
        return Files.exists(Path.of(path + "/" + name), LinkOption.NOFOLLOW_LINKS);
    }

    @Override
    public String save(MultipartFile file) {
        String filename = file.getOriginalFilename();
        String extension = FilenameUtils.getExtension(filename);
        try {
            Path path1 = Path.of(path + "/" + UUID.randomUUID() + "." + extension);
            Files.copy(file.getInputStream(),
                    path1,
                    StandardCopyOption.REPLACE_EXISTING);
            return FilenameUtils.getName(path1.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteUnusedImages() {
        Runnable runnable=() -> {
            Page<Product> globalProducts = productRepository.findAll(PageRequest.of(0, 10));
            Set<String> globalImages = globalProducts.stream()
                    .map(Product::getImage)
                    .collect(Collectors.toSet());
            try (Stream<Path> list = Files.list(path)) {
                List<String> fileNames = list.map(path1 -> path1.getFileName().toString())
                        .toList();
                fileNames.stream()
                        .filter(s -> !globalImages.contains(s))
                        .peek(System.out::println)
                        .forEach(this::delete);
                for (int i = 1; i <= globalProducts.getTotalPages(); i++) {
                    Set<String> images = productRepository.findAll(PageRequest.of(i, 10)).stream()
                            .filter(product -> product.getImage() != null && product.getImage().isBlank())
                            .map(Product::getImage)
                            .collect(Collectors.toSet());
                    fileNames.stream()
                            .filter(s -> !images.contains(s))
                            .peek(System.out::println)
                            .forEach(this::delete);
                }
            } catch (Exception ignored) {}
        };
        runnable.run();
    }

    @Override
    @SneakyThrows
    public void delete(String img) {
        Files.deleteIfExists(Path.of(path+"/"+img));
    }
}
