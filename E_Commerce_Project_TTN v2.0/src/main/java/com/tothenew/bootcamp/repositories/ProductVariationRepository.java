package com.tothenew.bootcamp.repositories;

import com.tothenew.bootcamp.entity.ProductContent.ProductVariation;
import org.springframework.data.repository.CrudRepository;

public interface ProductVariationRepository extends CrudRepository<ProductVariation,Integer> {

}
