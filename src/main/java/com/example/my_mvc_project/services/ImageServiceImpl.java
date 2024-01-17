package com.example.my_mvc_project.services;

import com.example.my_mvc_project.exceptions.BadParamException;
import com.example.my_mvc_project.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

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

    @Override
    @SneakyThrows
    public byte[] get(String name) {
        Path targetFile = Path.of(path + "/" + name);
        if (!targetFile.toFile().exists()) {
            throw new NotFoundException("Rasm topilmadi");
        }
        return Files.readAllBytes(targetFile);
    }
    @Override
    public String save(MultipartFile file) {
        if(file==null || file.isEmpty()){
            return "none";
	    }
        String filename = file.getOriginalFilename();
        String extension = Objects.requireNonNullElseGet(FilenameUtils.getExtension(filename),() -> {throw new BadParamException("Noto'g'ri fayl");});
        if (!extension.equals("png") && !extension.equals("jpg")) {
            throw new BadParamException("Noto'g'ri fayl berdingiz");
        }
        try {
            Path path1 = Path.of(path + "/" + UUID.randomUUID() + "." + extension);
            Files.copy(file.getInputStream(),
                    path1,
                    StandardCopyOption.REPLACE_EXISTING);
            try {
                BufferedImage read = ImageIO.read(file.getInputStream());
                int type = read.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : read.getType();
                BufferedImage resized = resize(read, type);
                ImageIO.write(resized,extension,path1.toFile());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String name = FilenameUtils.getName(path1.toString());
            cachedImages.put(name, LocalDateTime.now().plusMinutes(5));
            return name;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static BufferedImage resize(BufferedImage originalImage, int type){
        double with=1000;
        double value = originalImage.getWidth() / with;
        double heightValue = originalImage.getHeight() / value;
        if (heightValue>2500) {
            throw new BadParamException("Noto'g'ri rasm berdingiz");
        }
        int height = (int)Math.round(heightValue);
        BufferedImage resizedImage = new BufferedImage(1000,height,type);
        Graphics graphics = resizedImage.getGraphics();
        graphics.drawImage(originalImage,0,0,1000,height,null);
        graphics.dispose();
        return resizedImage;
    }

    @Override
    @SneakyThrows
    @Async
    public void delete(String img) {
        Files.deleteIfExists(Path.of(path+"/"+img));
    }
}
