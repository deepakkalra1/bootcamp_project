package com.tothenew.bootcamp.repositories;

import com.tothenew.bootcamp.entity.ProductContent.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Integer> {
}
