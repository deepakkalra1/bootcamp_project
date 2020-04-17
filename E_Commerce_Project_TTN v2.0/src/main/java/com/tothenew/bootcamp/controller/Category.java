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
    @PostMapping("/user/admin/add/field/{fieldName}")
    public ResponseEntity addFieldByAdmin(@PathVariable(name = "fieldName")String fieldName
                                          )
    {
        CommonResponseVO commonResponseVO = categoryService.addFieldByAdmin(fieldName);
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





    //------------------------------------------------------------------------------------------------------------>
    @GetMapping("/user/admin/view/categories")
    public ResponseEntity viewAllCategoryByAdmin(@RequestParam(value = "max",required = false) Integer max,
                                                 @RequestParam(value = "offset",required = false) Integer offset,
                                                 @RequestParam(value = "order",required = false) String order,
                                                 @RequestParam(value = "sort",required = false) String sort,
                                                 @RequestParam(value = "string",required = false) String query){
        CommonResponseVO commonResponseVO = categoryService.viewAllCategoriesByAdmin(max,offset,order,sort,query);
        return new ResponseEntity(commonResponseVO, HttpStatus.OK);
    }






    //------------------------------------------------------------------------------------------------------------>
    @PutMapping("/user/admin/update/category/{id}/{name}")
    public ResponseEntity updateSingleCategoryByAdmin(@PathVariable(name = "id")int categoryId ,
                                                      @PathVariable(name = "name")String categoryNewName)
    {
         CommonResponseVO commonResponseVO= categoryService.updateCategoryByAdmin(categoryId,categoryNewName);
        return new ResponseEntity(commonResponseVO, HttpStatus.OK);
    }





    //------------------------------------------------------------------------------------------------------------>
    /*
     {
     * 	"categoryId":"8",
     * 	"categoryMetadataFieldId":"4",
     * 	"value_0":"41",
     * 	"value_1":"42",
     * 	"value_2":"43"
     *
     * }
     */
    @PostMapping("/user/admin/add/category-metadata-value")
    public ResponseEntity addCategoryMetadataByAdmin( @RequestBody HashMap<String, String> metadata)
    {
        CommonResponseVO commonResponseVO= categoryService.addCategoryMetadataValueByAdmin(metadata);
        return new ResponseEntity(commonResponseVO, HttpStatus.OK);
    }





    //------------------------------------------------------------------------------------------------------------>
    /***
     *
     * @param metadata
     * @return
     * @desciption- api will update by adding new values and deleting old value and yet maintaining uniquness
     * in values of Category_metadata_values
     *
     * {
     * 	"categoryId":"8",
     * 	"categoryMetadataFieldId":"7",
     * 	"delete_value_0":"43",
     * 	"add_value_0":"44"
     *
     * }
     * */
    @PutMapping("/user/admin/update/category-metadata-value")
    public ResponseEntity updateCategoryMetadataByAdmin( @RequestBody HashMap<String, String> metadata)
    {
        CommonResponseVO commonResponseVO= categoryService.updateCategoryMetadataValueByAdmin(metadata);
        return new ResponseEntity(commonResponseVO, HttpStatus.OK);
    }





    //------------------------------->seller category API's
    //------------------------------------------------------------------------------------------------------------->
    @GetMapping("/user/seller/view/categories")
    public ResponseEntity viewCategoriesBySeller( )
    {
        CommonResponseVO commonResponseVO= categoryService.viewCategoriesBySeller();
        return new ResponseEntity(commonResponseVO, HttpStatus.OK);
    }





    //------------------------------->customer category API's
    //------------------------------------------------------------------------------------------------------------->
    @GetMapping("/user/customer/view/categories")
    public ResponseEntity viewCategoriesByCustomer(@RequestParam(name = "categoryId",required = false)Integer categoryId )
    {
        CommonResponseVO commonResponseVO= categoryService.viewCategoriesByCustomer(categoryId);
        return new ResponseEntity(commonResponseVO, HttpStatus.OK);
    }
}
