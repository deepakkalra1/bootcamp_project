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






    //---------------------------------------------------------------------------------------------------------->
    @GetMapping("/user/seller/product")
    public ResponseEntity viewProductBySeller(@RequestHeader(value = "Authorization") String tokenString,
                                              @RequestParam(value = "productId")int productId
    ){
        String token = tokenString.split(" ")[1];
        CommonResponseVO commonResponseVO = productService.viewProductBySeller(token,productId);
        return new ResponseEntity(commonResponseVO,HttpStatus.OK);
    }





    //---------------------------------------------------------------------------------------------------------->
    @GetMapping("/user/seller/product/variation")
    public ResponseEntity viewProductVariationBySeller(@RequestHeader(value = "Authorization") String tokenString,
                                              @RequestParam(value = "productVariationId")int productVariationId
    ){
        String token = tokenString.split(" ")[1];
        CommonResponseVO commonResponseVO = productService.viewProductVariationBySeller(token,productVariationId);
        return new ResponseEntity(commonResponseVO,HttpStatus.OK);
    }





    //---------------------------------------------------------------------------------------------------------->
    @GetMapping("/user/seller/products")
    public ResponseEntity viewAllProductBySeller(@RequestHeader(value = "Authorization") String tokenString,
                                                 @RequestParam(value = "max",required = false) Integer max,
                                                 @RequestParam(value = "offset",required = false) Integer offset,
                                                 @RequestParam(value = "order",required = false) String order,
                                                 @RequestParam(value = "sort",required = false) String sort,
                                                 @RequestParam(value = "query",required = false) String query)
    {
        String token = tokenString.split(" ")[1];
        CommonResponseVO commonResponseVO = productService.viewAllProductsOfSeller(token,max,offset, order, sort, query);
        return new ResponseEntity(commonResponseVO,HttpStatus.OK);
    }






    //---------------------------------------------------------------------------------------------------------->
    @GetMapping("/user/seller/product/variations")
    public ResponseEntity viewAllProductVariationsBySeller(@RequestHeader(value = "Authorization") String tokenString,
                                                 @RequestParam(value = "max",required = false) Integer max,
                                                 @RequestParam(value = "offset",required = false) Integer offset,
                                                 @RequestParam(value = "order",required = false) String order,
                                                 @RequestParam(value = "sort",required = false) String sort,
                                                 @RequestParam(value = "query",required = false) String query,
                                                           @RequestParam(value = "productId")int productId)
    {
        String token = tokenString.split(" ")[1];
        CommonResponseVO commonResponseVO = productService.viewAllProductVariationsBySeller(token,max,offset, order, sort, query,productId);
        return new ResponseEntity(commonResponseVO,HttpStatus.OK);
    }





    //---------------------------------------------------------------------------------------------------------->
    @DeleteMapping("/user/seller/product/delete")
    public ResponseEntity deleteProductBySeller(@RequestHeader(value = "Authorization") String tokenString,
                                                           @RequestParam(value = "productId")int productId)
    {
        String token = tokenString.split(" ")[1];
        CommonResponseVO commonResponseVO = productService.deleteProductBySeller(token,productId);
        return new ResponseEntity(commonResponseVO,HttpStatus.OK);
    }
}
