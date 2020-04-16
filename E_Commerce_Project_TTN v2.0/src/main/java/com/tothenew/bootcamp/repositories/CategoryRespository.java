package com.tothenew.bootcamp.repositories;

import com.tothenew.bootcamp.entity.ProductContent.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRespository extends CrudRepository<Category,Integer> {

    Category findByName(String name);

    @Query(value = "select * from category where parent_id=:id",nativeQuery = true)
    Iterable<Category> findByParentId(int id);
}
