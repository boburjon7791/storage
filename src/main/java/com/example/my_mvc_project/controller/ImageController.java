package com.example.my_mvc_project.controller;

import com.example.my_mvc_project.dtos.product.ProductGetDto;
import com.example.my_mvc_project.exceptions.BadParamException;
import com.example.my_mvc_project.services.ImageService;
import com.example.my_mvc_project.services.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@AllArgsConstructor
@RequestMapping("/image")
@PreAuthorize("permitAll()")
public class ImageController {
    private final ImageService imageService;
    private final ProductService productService;

    @GetMapping(value = "/get/{imageName}",produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public @ResponseBody byte[] image(@PathVariable(required = false) String imageName){
        return imageService.get(imageName);
    }
    @PreAuthorize("hasAnyRole('SUPER_MANAGER','MANAGER')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String save(@RequestParam(required = false) MultipartFile file, Model model){
        String image;
        if (file==null) {
            image="none";
        }else{
            image = imageService.save(file,true);
        }
        model.addAttribute("img", image);
        return "product/save2";
    }
    @PreAuthorize("hasAnyRole('SUPER_MANAGER','MANAGER')")
    @GetMapping("/update/{pId}")
    public String updateImage(@PathVariable long pId, Model model){
        model.addAttribute("p_id",pId);
        return "product/update";
    }
    @PreAuthorize("hasAnyRole('SUPER_MANAGER','MANAGER')")
    @PostMapping("/update2")
    public String updateCommit(
            @RequestParam(required=false) MultipartFile file,
            @RequestParam long p_id,
            Model model
            ){
        String filename;
        if (file==null || file.isEmpty()) {
            filename="none";
        }else {
            filename = imageService.save(file,false);
        }
        if (productService.updateImage(filename,p_id)>0) {
            ProductGetDto dto = productService.get(p_id);
            model.addAttribute("prod",dto);
            return "product/about";
        }
        throw new BadParamException("Rasm yangilanmadi");
    }
}
