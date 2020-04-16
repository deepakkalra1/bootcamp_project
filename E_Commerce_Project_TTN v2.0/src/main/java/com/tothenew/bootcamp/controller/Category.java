package com.tothenew.bootcamp.controller;

import com.tothenew.bootcamp.pojo.CommonResponseVO;
import com.tothenew.bootcamp.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.xml.transform.Source;
import java.util.HashMap;

@RestController
public class Category {

    @Autowired
    CategoryService categoryService;

    // --------------------------------Admin Category Api's
    //--------------------------------------------------------------------------------------------------------->
    @PostMapping("/user/admin/add/field/{fieldValue}")
    public ResponseEntity addFieldByAdmin(@PathVariable(name = "fieldValue")String fieldValue
                                          )
    {
        CommonResponseVO commonResponseVO = categoryService.addFieldByAdmin(fieldValue);
        return new ResponseEntity(commonResponseVO, HttpStatus.OK);

    }




    //--------------------------------------------------------------------------------------------------------->
    @GetMapping("/user/admin/view/fields")
    public ResponseEntity viewFieldsByAdmin(
            @RequestParam(value = "max",required = false) Integer max,
            @RequestParam(value = "offset",required = false) Integer offset,
            @RequestParam(value = "order",required = false) String order,
            @RequestParam(value = "sort",required = false) String sort,
            @RequestParam(value = "query",required = false) String query
    )
    {
        CommonResponseVO commonResponseVO = categoryService.viewFieldsByAdmin( max,offset, order, sort, query);
        return new ResponseEntity(commonResponseVO, HttpStatus.OK);

    }




    //---------------------------------------------------------------------------------------------------------->
    @PostMapping("/user/admin/add/category")
    public ResponseEntity addCategoryByAdmin(@RequestParam(value = "categoryName")String newCategoryName,
                                             @RequestParam(value = "parentId",required = false)Integer parentId ){
        CommonResponseVO commonResponseVO = categoryService.addCategoryByAdmin(newCategoryName,parentId);
        return new ResponseEntity(commonResponseVO, HttpStatus.OK);
    }





    //------------------------------------------------------------------------------------------------------------>
    @GetMapping("/user/admin/view/category/{id}")
    public ResponseEntity viewSingleCategoryByAdmin(@PathVariable("id") int categoryId ){
        CommonResponseVO commonResponseVO = categoryService.viewSingleCategoryByAdmin(categoryId);
        return new ResponseEntity(commonResponseVO, HttpStatus.OK);
    }
//
//
//
//
//
//    //------------------------------------------------------------------------------------------------------------>
//    @GetMapping("/user/admin/view/categories")
//    public ResponseEntity viewAllCategoryByAdmin(@RequestHeader(value = "Authorization")String tokenString,
//                                                 @RequestParam(value = "max",required = false) int max,
//                                                 @RequestParam(value = "offset",required = false) int offset,
//                                                 @RequestParam(value = "order",required = false) String order,
//                                                 @RequestParam(value = "sort",required = false) String sort,
//                                                 @RequestParam(value = "string",required = false) String query){
//        String token = tokenString.split(" ")[1];
//        //CommonResponseVO commonResponseVO = categoryService.
//        return new ResponseEntity(commonResponseVO, HttpStatus.OK);
//    }
//
//
//
//
//
//    //------------------------------------------------------------------------------------------------------------>
//    @DeleteMapping("/user/admin/delete/category/{id}")
//    public ResponseEntity deleteSingleCategoryByAdmin(@RequestHeader(value = "Authorization")String tokenString,
//                                                      @PathVariable(name = "id")int categoryId ){
//        String token = tokenString.split(" ")[1];
//        //CommonResponseVO commonResponseVO = categoryService.
//        return new ResponseEntity(commonResponseVO, HttpStatus.OK);
//    }
//
//
//
//
//
//    //------------------------------------------------------------------------------------------------------------>
//    @PutMapping("/user/admin/update/category/{id}/{name}")
//    public ResponseEntity deleteSingleCategoryByAdmin(@RequestHeader(value = "Authorization")String tokenString,
//                                                      @PathVariable(name = "id")int categoryId ,
//                                                      @PathVariable(name = "name")String categoryNewName){
//        String token = tokenString.split(" ")[1];
//        //CommonResponseVO commonResponseVO = categoryService.
//        return new ResponseEntity(commonResponseVO, HttpStatus.OK);
//    }


}
