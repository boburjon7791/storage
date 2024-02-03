package com.example.my_mvc_project.repositories;

import com.example.my_mvc_project.entities.Selling;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface SellingRepository extends JpaRepository<Selling, UUID>, JpaSpecificationExecutor<Selling> {
    @Query(value = "from Selling s where extract(date from s.dateTime)=?1")
    Page<Selling> findAllByDate(Pageable pageable, LocalDate date);

    @Query(nativeQuery = true,value = "select * from selling s where extract(localtime from s.date_time) between ?1 and?2")
    Page<Selling> findAllByBetweenTime(LocalTime startTime, LocalTime endTime, Pageable pageable);

    @Query(value = "from Selling s where s.product.id=?1")
    Page<Selling> findAllByProduct(Long productId, Pageable pageable);

    @Query(value = "from Selling s where s.count between ?1 and ?2")
    Page<Selling> findAllByBetweenCount(Double countStart, Double countEnd, Pageable pageable);

    @Query(value = "from Selling s where s.soldPrice between ?1 and ?2")
    Page<Selling> findAllByBetweenSoldPrice(Double soldPriceStart, Double soldPriceEnd, Pageable pageable);
}