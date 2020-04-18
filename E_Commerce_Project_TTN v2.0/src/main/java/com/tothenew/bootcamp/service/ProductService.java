package com.tothenew.bootcamp.service;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.tothenew.bootcamp.constants.JwtUtility;
import com.tothenew.bootcamp.entity.ProductContent.Category;
import com.tothenew.bootcamp.entity.ProductContent.Product;
import com.tothenew.bootcamp.entity.User.Seller;
import com.tothenew.bootcamp.enums.StatusCode;
import com.tothenew.bootcamp.exceptionHandling.GiveMessageException;
import com.tothenew.bootcamp.pojo.CommonResponseVO;
import com.tothenew.bootcamp.repositories.CategoryRespository;
import com.tothenew.bootcamp.repositories.ProductRepository;
import com.tothenew.bootcamp.repositories.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    SellerRepository sellerRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRespository categoryRespository;


    //----------------------------------------------------------------------------------------------------------->
    public CommonResponseVO addProductBySeller(String token,String productName, int cayegoryId, String brandName,String description,
                                               Boolean is_cancelable, Boolean is_returnable){
        System.out.println("0");
        String username = JwtUtility.findUsernameFromToken(token);
        Seller seller = sellerRepository.findByEmail(username);
        System.out.println("1");
       List<Product> anyExistingProductOfSellerWithSameNameInSameCategory= productRepository.findProductsWithSellerIdAndProductNameAndCategoryId(seller.getId(),productName,cayegoryId);
        System.out.println("2");
       if (anyExistingProductOfSellerWithSameNameInSameCategory.isEmpty()){
           System.out.println("3");
            Product product = new Product();
            product.setName(productName);

            //setting category
            checkIfItIsLeafCategoryNode(cayegoryId);
           System.out.println("4");
            Category category = categoryRespository.findById(cayegoryId).get();
            product.setCategory(category);
           System.out.println("5");

            product.setSeller_seller(seller);

            product.setDescription(description);

            product.setBrand(brandName);

            if (is_cancelable!=null){
                product.setIs_cancellable(is_cancelable.booleanValue());
            }

            if (is_returnable!=null){
                product.setIs_returnable(is_returnable.booleanValue());
            }

            productRepository.save(product);

        }
        else {
            throw new GiveMessageException(Arrays.asList(StatusCode.EXIST.toString())
            ,Arrays.asList("Similar product already exist by via seller= "+username+" with product name= "+productName));
        }
        CommonResponseVO commonResponseVO = new CommonResponseVO(Arrays.asList(StatusCode.SUCCESS.toString()));
        return commonResponseVO;

    }

    public void checkIfItIsLeafCategoryNode(int categoryId){

        Iterable<Category> categories= categoryRespository.findByParentId(categoryId);
        if (categories.iterator().hasNext()){
            throw new GiveMessageException(Arrays.asList(StatusCode.FAILED.toString())
                    ,Arrays.asList("Provided Category Id is not a leaf Category, You can not append product to this category, go for its child category"));

        }

    }
}
