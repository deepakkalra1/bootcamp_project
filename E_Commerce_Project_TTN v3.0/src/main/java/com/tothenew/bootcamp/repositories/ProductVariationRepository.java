package com.tothenew.bootcamp.repositories;

import com.tothenew.bootcamp.entity.ProductContent.ProductVariation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductVariationRepository extends CrudRepository<ProductVariation,Integer> {

    @Query(value = "select MIN(price) from product_variation",nativeQuery = true)
    int findMinPrice();

    @Query(value = "select MAX(price) from product_variation",nativeQuery = true)
    int findMaxPrice();

    @Query(value = "select * from product_variation where product_id=:id",nativeQuery = true)
    List<ProductVariation> findAllVariationByProductId(int id);

    @Query(value = "select * from product_variation where product_id=:id",nativeQuery = true)
    List<ProductVariation> findAllVariationByProductId(int id, Pageable pageable);
}
