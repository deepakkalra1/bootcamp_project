package com.tothenew.bootcamp.repositories;

import com.tothenew.bootcamp.entity.ProductContent.CategoryMetadataField;
import org.springframework.data.repository.CrudRepository;

public interface CategoryMetadataRepository extends CrudRepository<CategoryMetadataField,Integer> {
}
