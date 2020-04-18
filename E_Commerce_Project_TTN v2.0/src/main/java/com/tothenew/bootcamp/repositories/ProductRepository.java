package com.tothenew.bootcamp.repositories;

import com.tothenew.bootcamp.entity.ProductContent.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Integer> {

    @Query(value = "select * from product where seller_user_id=:id AND name=:product_name AND category_id=:categoryId",nativeQuery = true)
    List<Product> findProductsWithSellerIdAndProductNameAndCategoryId(int id,String product_name,int categoryId);



}
