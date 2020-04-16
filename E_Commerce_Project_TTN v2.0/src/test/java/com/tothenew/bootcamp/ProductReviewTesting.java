package com.tothenew.bootcamp;

import com.tothenew.bootcamp.entity.ProductContent.ProductReview;
import com.tothenew.bootcamp.repositories.CategoryRespository;
import com.tothenew.bootcamp.repositories.CustomerRepository;
import com.tothenew.bootcamp.repositories.ProductRepository;
import com.tothenew.bootcamp.repositories.ProductReviewRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProductReviewTesting {
@Autowired
    ProductReviewRepository productReviewRepository;
@Autowired
    CustomerRepository customerRepository;
@Autowired
    ProductRepository productRepository;
@Autowired
    CategoryRespository categoryRespository;

    @Test
    public void productReview(){
        ProductReview productReview = new ProductReview();
        productReview.setRating(5);
        productReview.setReview("this was fantastic");
        productReview.setCustomerWhoReviewIt(customerRepository.findById(42));
        productReview.setProductWhoseReviewItIs(productRepository.findById(11).get());
        productReviewRepository.save(productReview);
    }


    @Test
    public void abc(){
        categoryRespository.findByParentId(8).forEach(category->{
            System.out.println(category.getName());
        });
    }
}
