package com.tothenew.bootcamp.repositories;

import com.tothenew.bootcamp.entity.ProductContent.CategoryMetadataField;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CategoryMetadataFieldRepository extends CrudRepository<CategoryMetadataField,Integer> {

    Iterable<CategoryMetadataField> findAll(Pageable pageable);

    @Query(value = "select * from category_metadata_field order by :property :order",nativeQuery = true)
    Iterable<CategoryMetadataField> findAllOrderBy(String property,String order);

    CategoryMetadataField findByCategoryKey(String categoryKey);
}
