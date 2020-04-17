package com.tothenew.bootcamp.repositories;

import com.tothenew.bootcamp.entity.ProductContent.ProductVariation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ProductVariationRepository extends CrudRepository<ProductVariation,Integer> {

    @Query(value = "select MIN(price) from product_variation",nativeQuery = true)
    int findMinPrice();

    @Query(value = "select MAX(price) from product_variation",nativeQuery = true)
    int findMaxPrice();
}
