package com.tothenew.bootcamp.repositories;

import com.tothenew.bootcamp.entity.ProductContent.ProductReview;
import org.springframework.data.repository.CrudRepository;

public interface ProductReviewRepository extends CrudRepository<ProductReview,Integer> {
}
