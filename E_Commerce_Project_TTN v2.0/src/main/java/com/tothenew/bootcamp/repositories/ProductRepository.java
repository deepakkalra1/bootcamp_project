package com.tothenew.bootcamp.repositories;

import com.tothenew.bootcamp.entity.ProductContent.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.LinkedList;
import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Integer> {

    @Query(value = "select * from product where seller_user_id=:id AND name=:product_name AND category_id=:categoryId",nativeQuery = true)
    List<Product> findProductsWithSellerIdAndProductNameAndCategoryId(int id,String product_name,int categoryId);

    @Query(value = "select * from product where seller_user_id=:id",nativeQuery = true)
    List<Product> findProductsWithSellerId(int id);

    @Query(value = "select * from product where seller_user_id=:id",nativeQuery = true)
    List<Product> findProductsWithSellerId(int id, Pageable pageable);

    @Query(value = "select * from product where seller_user_id=:id order by :property :direction",nativeQuery = true)
    LinkedList<Product> findProductsWithSellerIdAndOrderByAndWithProperty(int id, String property, String direction);

  //  void deleteByProductId()
}
