package com.tothenew.bootcamp.controller;

import com.tothenew.bootcamp.enums.StatusCode;
import com.tothenew.bootcamp.pojo.CommonResponseVO;
import com.tothenew.bootcamp.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.LinkedHashMap;

@RestController
public class Product {

    @Autowired
    ProductService productService;

    //--------------------------------------------------------------------------------------------------------->
    @PostMapping("/user/seller/product/add")
    public ResponseEntity addProduct(@RequestHeader(value = "Authorization")String tokenString,
                                     @RequestParam(name = "name")String productName,
                                     @RequestParam(name = "categoryId")int cayegoryId,
                                     @RequestParam(name = "brand")String brandName,
                                     @RequestParam(name = "description")String description,
                                     @RequestParam(name = "isCancelable",required = false) Boolean is_cancelable,
                                     @RequestParam(name = "isReturnable",required = false) Boolean is_returnable
                                     ){
        String token = tokenString.split(" ")[1];
        CommonResponseVO commonResponseVO=
                productService.addProductBySeller(token,productName,cayegoryId,brandName,description, is_cancelable,is_returnable);
        return new ResponseEntity(commonResponseVO,HttpStatus.OK);

    }





    //---------------------------------------------------------------------------------------------------------->
    @PostMapping("/user/seller/product-variation/add")
    public ResponseEntity addProductVariation(@RequestHeader(value = "Authorization") String tokenString,
                                              @RequestParam(value = "productId")int productId,
                                              @RequestParam(value = "primaryImage") String primaryImage,
                                              @RequestBody LinkedHashMap<String, String> metadata,
                                              @RequestParam(value = "quantity",required = false)Integer quantity,
                                              @RequestParam(value = "price",required = false)Integer price
                                              ){
        String token = tokenString.split(" ")[1];
        productService.addProductVariationForProduct(token,productId,primaryImage,metadata,quantity,price);
        CommonResponseVO commonResponseVO = new CommonResponseVO(Arrays.asList(StatusCode.SUCCESS.toString()));
        return new ResponseEntity(commonResponseVO,HttpStatus.OK);
    }
}
