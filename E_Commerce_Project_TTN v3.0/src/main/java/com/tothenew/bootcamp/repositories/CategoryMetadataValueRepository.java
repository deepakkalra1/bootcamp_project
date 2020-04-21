package com.tothenew.bootcamp.repositories;

import com.tothenew.bootcamp.entity.ProductContent.CategoryMetadataValue;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CategoryMetadataValueRepository extends CrudRepository<CategoryMetadataValue,Integer> {

    @Query(value = "select * from category_metadata_values where category_key_id=:kid AND category_id=:cid",nativeQuery = true)
    Iterable<CategoryMetadataValue> findValueCombinationOfFieldIdAndCategoryId(int kid,int cid);

    @Query(value = "select * from category_metadata_values where category_id=:id",nativeQuery = true)
    List<CategoryMetadataValue> findByCategoryId(int id);

}
