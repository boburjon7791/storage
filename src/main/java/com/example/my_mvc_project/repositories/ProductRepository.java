package com.example.my_mvc_project.repositories;

import com.example.my_mvc_project.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    boolean existsByName(String name);
    @Transactional
    @Modifying
    @Query("update Product p set p.count = ?1 where p.id = ?2")
    void updateCountById(Long count, Long id);

    @Query(value = "from Product p where p.name like concat('%',?1,'%')")
    Page<Product> findAllByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query(value = "from Product p where ?1 = p.about")
    Page<Product> findAllByAbout(String about,Pageable pageable);

    @Query(value = "from Product p where p.price between ?1 and ?2")
    Page<Product> findAllByBetweenPrices(Double priceStart, Double priceEnd, Pageable pageable);
    @Query(value = "from Product p where p.count between ?1 and ?2")
    Page<Product> findAllByBetweenCounts(Long countStart, Long countEnd, Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "update Product p set p.image=?1 where p.id=?2")
    int updateImageById(String saved, long productId);

    @Query(value = "select exists (from Product p where p.name=?1 and p.id<>?2)")
    boolean existsByNameAndIdNotMatches(String name, Long id);

    @Modifying
    @Query(value = "update Product p set p.count=p.count-?1 where p.id=?2")
    int subtractCountById(Long count, Long id);

}