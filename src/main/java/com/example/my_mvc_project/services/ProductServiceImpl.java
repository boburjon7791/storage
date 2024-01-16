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
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final ImageService imageService;

    @Override
    public ProductGetDto save(ProductCreateDto dto) {
        if (productRepository.existsByName(dto.name())) {
            throw new BadParamException("Ushbu mahsulot allaqachon mavjud");
        }
        ProductGetDto getDto = productMapper.toDto(productRepository.save(productMapper.toEntity(dto)));
        ImageService.cachedImages.remove(getDto.getImage());
        System.out.println("After removing...");
        ImageService.cachedImages.forEach((s, time) -> System.out.println(s+" : "+time));
        return getDto;
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
        String sql= """
                    select image
                    from product
                    where id=:id
                    """;
        Map<String, Long> map = Map.of("id", productId);
        RowMapper<String> rowMapper=(rs, rowNum) -> rs.getString("image");
        String productImage = namedParameterJdbcTemplate.queryForObject(sql, map, rowMapper);
        int i = productRepository.updateImageById(saved, productId);
        if (i>0) {
            System.out.println(productImage+" is deleting");
            imageService.delete(productImage);
        }
        return i;
    }

    @Override
    public List<Product> getAllByIds(List<Long> ids) {
        if (ids.size()>50) {
            throw new BadParamException("Juda ko'p malumot so'rayapsan");
        }
        return productRepository.findAllById(ids);
    }
}
