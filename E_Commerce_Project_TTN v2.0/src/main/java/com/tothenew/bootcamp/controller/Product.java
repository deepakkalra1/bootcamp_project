package com.tothenew.bootcamp.controller;

import com.tothenew.bootcamp.pojo.CommonResponseVO;
import com.tothenew.bootcamp.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
