package com.tothenew.bootcamp.repositories;

import com.tothenew.bootcamp.entity.ProductContent.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CategoryRespository extends CrudRepository<Category,Integer> {

    Category findByName(String name);

    @Query(value = "select * from category where parent_id=:id",nativeQuery = true)
    Iterable<Category> findByParentId(int id);

    Iterable<Category> findAll(Pageable pageable);

    @Query(value = "select * from category order by :query :order",nativeQuery = true)
    Iterable<Category> findAllOrderBy(String query,String order);

    @Query(value = "select * from category where parent_id=:id",nativeQuery = true)
    List<Category> findByParentIdInList(int id);


    @Query(value = "select * from category where parent_id IS NULL",nativeQuery = true)
    List<Category> findParentCategories();
}
