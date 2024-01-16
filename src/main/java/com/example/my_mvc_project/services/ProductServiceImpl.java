package com.example.my_mvc_project.services;

import com.example.my_mvc_project.dtos.product.ProductCreateDto;
import com.example.my_mvc_project.dtos.product.ProductGetDto;
import com.example.my_mvc_project.dtos.product.ProductUpdateDto;
import com.example.my_mvc_project.entities.Product;
import com.example.my_mvc_project.exceptions.BadParamException;
import com.example.my_mvc_project.exceptions.NotFoundException;
import com.example.my_mvc_project.mappers.ProductMapper;
import com.example.my_mvc_project.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    @Override
    public ProductGetDto save(ProductCreateDto dto) {
        if (productRepository.existsByName(dto.name())) {
            throw new BadParamException("Ushbu mahsulot allaqachon mavjud");
        }
        return productMapper.toDto(productRepository.save(productMapper.toEntity(dto)));
    }

    @Override
    public ProductGetDto update(ProductUpdateDto dto) {
        if (!productRepository.existsById(dto.id())) {
            throw new NotFoundException("Mahsulot mavjud emas");
        }
        if (productRepository.existsByNameAndIdNotMatches(dto.name(),dto.id())) {
            throw new BadParamException("Ushbu mahsulot allaqachon mavjud");
        }
        return productMapper.toDto(productRepository.save(productMapper.toEntity(dto)));
    }

    @Override
    public ProductGetDto get(Long id) {
        return productMapper.toDto(productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Mahsulot topilmadi")));
    }

    @Override
    public ProductGetDto updateCountById(Long count, Long id) {
        if (!productRepository.existsById(id)) {
            throw new NotFoundException("Mahsulot topilmadi");
        }
        productRepository.updateCountById(count,id);
        return productMapper.toDto(productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Mahsulot topilmadi")));
    }

    @Override
    public Page<ProductGetDto> products(Pageable pageable) {
        Page<ProductGetDto> products = productRepository.findAll(pageable)
                .map(productMapper::toDto);
        if (products.isEmpty()) {
            throw new NotFoundException("Mahsulotlar topilmadi");
        }
        return products;
    }


    @Override
    public Page<ProductGetDto> productsByName(Pageable pageable, String name) {
        Page<ProductGetDto> products = productRepository.findAllByNameContainingIgnoreCase(name, pageable)
                .map(productMapper::toDto);
        if (products.isEmpty()) {
            throw new NotFoundException("Mahsulotlar topilmadi");
        }
        return products;
    }

    @Override
    public Page<ProductGetDto> productsByAbout(Pageable pageable, String about) {
        Page<ProductGetDto> products = productRepository.findAllByAbout(about, pageable)
                .map(productMapper::toDto);
        if (products.isEmpty()) {
            throw new NotFoundException("Mahsulotlar topilmadi");
        }
        return products;
    }

    @Override
    public Page<ProductGetDto> productsByPrice(Pageable pageable, Double priceStart, Double priceEnd) {
        Page<ProductGetDto> products = productRepository.findAllByBetweenPrices(priceStart, priceEnd, pageable)
                .map(productMapper::toDto);
        if (products.isEmpty()) {
            throw new NotFoundException("Mahsulotlar topilmadi");
        }
        return products;
    }

    @Override
    public Page<ProductGetDto> productsByCount(Pageable pageable, Long countStart, Long countEnd) {
        Page<ProductGetDto> products = productRepository.findAllByBetweenCounts(countStart, countEnd, pageable)
                .map(productMapper::toDto);
        if (products.isEmpty()) {
            throw new NotFoundException("Mahsulotlar topilmadi");
        }
        return products;
    }

    @Override
    public void exists(long productId) {
        if (!productRepository.existsById(productId)) {
            throw new NotFoundException("Mahsulot topilmadi");
        }
    }

    @Override
    public int updateImage(String saved, long productId) {
        return productRepository.updateImageById(saved, productId);
    }

    @Override
    public List<Product> getAllByIds(List<Long> ids) {
        if (ids.size()>50) {
            throw new BadParamException("Juda ko'p malumot so'rayapsan");
        }
        return productRepository.findAllById(ids);
    }
}
