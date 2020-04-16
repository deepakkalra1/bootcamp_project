package com.tothenew.bootcamp.service;

import com.tothenew.bootcamp.entity.ProductContent.Category;
import com.tothenew.bootcamp.entity.ProductContent.CategoryMetadataField;
import com.tothenew.bootcamp.entity.ProductContent.Product;
import com.tothenew.bootcamp.enums.StatusCode;
import com.tothenew.bootcamp.exceptionHandling.GiveMessageException;
import com.tothenew.bootcamp.pojo.CommonResponseVO;
import com.tothenew.bootcamp.repositories.CategoryMetadataFieldRepository;
import com.tothenew.bootcamp.repositories.CategoryRespository;
import com.tothenew.bootcamp.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoryService {
    @Autowired
    CategoryMetadataFieldRepository categoryMetadataFieldRepository;

    @Autowired
    CategoryRespository categoryRespository;

    @Autowired
    ProductRepository productRepository;


    public CommonResponseVO addFieldByAdmin(String fieldName){


        Iterable<CategoryMetadataField> categoryMetadataFields= categoryMetadataFieldRepository.findAll();

        try {
            categoryMetadataFields.forEach(categoryMetadataField -> {
                if (categoryMetadataField.getCategoryKey().toLowerCase().equals(fieldName.toLowerCase())) {
                    throw new GiveMessageException(Arrays.asList(StatusCode.INVALID.toString()),
                            Arrays.asList("This Field or Key already EXIST")
                    );
                }
            });
        }
        catch (NullPointerException e){
            throw new GiveMessageException(Arrays.asList(StatusCode.FAILED.toString()),Arrays.asList("Null value found in category key"));
        }
            CategoryMetadataField categoryMetadataField = new CategoryMetadataField();
            categoryMetadataField.setCategoryKey(fieldName);
            categoryMetadataFieldRepository.save(categoryMetadataField);
            int id= categoryMetadataFieldRepository.findByCategoryKey(fieldName).getId();
            CommonResponseVO<Integer> commonResponseVO = new CommonResponseVO(Arrays.asList(StatusCode.SUCCESS.toString()));
            commonResponseVO.setData(id);
            return commonResponseVO;
    }





    //--------------------------------------------------------------------------------------------------------->
    public CommonResponseVO<LinkedHashMap<String, CategoryMetadataField>> viewFieldsByAdmin(
            Integer max,Integer offset,String order
            ,String sort, String query
    ){
        Iterable<CategoryMetadataField> categoryMetadataFields=new ArrayList<>();

        /*
        scenario 1->
        if page offset is provided... that means it must have maxNoRecords per page else exception
        does no matter if it have sort and query parameter

        scenario 2->
         sort order is provided or property(query) also

         scenerio->
         nothing is provided but only property then exception
         */
        int maxRecordsPerPage;
        int pageOffset=0;
        Direction direction=Direction.ASC;
        String property="id";

        if (max!=null){
            if (offset!=null) {
                pageOffset = offset.intValue();
            }
                maxRecordsPerPage=max.intValue();

                if (sort!=null) {
                    if (sort.toLowerCase().equals("asc")) {
                        direction = Direction.ASC;
                    } else if (sort.toLowerCase().equals("desc")) {
                        direction = Direction.DESC;
                    } else {
                        throw new GiveMessageException(Arrays.asList(StatusCode.FAILED.toString()), Arrays.asList("sorting order can be either asc or desc only"));
                    }
                }


                if (query!=null){
                    if (query!="category_key"){
                        throw new GiveMessageException(Arrays.asList(StatusCode.INVALID.toString()),Arrays.asList("Property name provided was invalid"));
                    }
                    property=query;
                }
                Pageable pageable = PageRequest.of(pageOffset,maxRecordsPerPage,direction,property);
                categoryMetadataFields=categoryMetadataFieldRepository.findAll(pageable);

        }
        else if (sort!=null){


                if (sort.toLowerCase().equals("asc")) {
                    direction = Direction.ASC;
                } else if (sort.toLowerCase().equals("desc")) {
                    direction = Direction.DESC;
                } else {
                    throw new GiveMessageException(Arrays.asList(StatusCode.FAILED.toString()), Arrays.asList("sorting order can be either asc or desc only"));
                }

            if (query!=null){
                if (query!="category_key"){
                    throw new GiveMessageException(Arrays.asList(StatusCode.INVALID.toString()),Arrays.asList("Property name provided was invalid"));
                }
                property=query;
            }

            categoryMetadataFields= categoryMetadataFieldRepository.findAllOrderBy(direction.toString(),property);

        }
        else if (query!=null){
           if (query!="category_key"){
               throw new GiveMessageException(Arrays.asList(StatusCode.INVALID.toString()),Arrays.asList("Property name provided was invalid"));
           }

            categoryMetadataFields=categoryMetadataFieldRepository.findAllOrderBy(direction.toString(),query);
        }
        else {
            categoryMetadataFields=categoryMetadataFieldRepository.findAll();
        }

        int[] count = new int[]{0};
        CommonResponseVO<LinkedHashMap<String, CategoryMetadataField>> commonResponseVO = new CommonResponseVO<LinkedHashMap<String, CategoryMetadataField>>();
        LinkedHashMap<String, CategoryMetadataField> hashMap = new LinkedHashMap<>();
        try {
            categoryMetadataFields.forEach(categoryMetadataField -> {
                System.out.println(categoryMetadataField);
                hashMap.put("field_"+count[0],categoryMetadataField);
                count[0]++;
            });
        }
        catch (NullPointerException e){
            throw new GiveMessageException(Arrays.asList(StatusCode.FAILED.toString()),Arrays.asList("Null value found in category key"));
        }
        commonResponseVO.setData(hashMap);
        return commonResponseVO;
    }


    /***
     * @param newCategoryName
     * @param parentId
     * @return
     * 1->fetch all the categories
     * 2->CASE 1= when parentId is not given.. to create only parent Category
     *      *-looping through all categories and holding only those whose parentCategory is null
     *      *- while looping through entire category list.. if found any category name same as new given EXCEPTION
     *      *- if no exception raise.. that means not already registered with same name
     *      *-reistered it then
     */
//    public CommonResponseVO<HashMap<String, String>> addCategoryByAdmin(String newCategoryName,Integer parentId)
//    {
//        int categoryIdCreated=0;
//            int fetchedParentCategoryId;
//        CommonResponseVO<HashMap<String, String>> commonResponseVO = new CommonResponseVO();
//        //this hasmap will hold the actual data like.. newcreated-id
//        HashMap<String, String> hashMap = new HashMap<String, String>();
//
//        //called all the categories which exist in db
//        Iterable<Category> categories = categoryRespository.findAll();
//
//        //if parentid is not given.. that means jst to create parent ccategory
//        if (parentId==null){
//            categories.forEach(category -> {
//                if (category.getParentCategory()==null){
//
//                    if (newCategoryName.equals(category.getName())){
//                        throw new GiveMessageException(Arrays.asList(StatusCode.EXIST.toString()),
//                                Arrays.asList("This name category Already Exist as Parent Category"));
//                    }
//                }
//                else if (category.getParentCategory().getName().equals(newCategoryName)){
//                    throw new GiveMessageException(Arrays.asList(StatusCode.EXIST.toString()),
//                            Arrays.asList("This name category Already Exist as Child Category"));
//
//                }
//            });
//            Category category = new Category();
//            category.setName(newCategoryName);
//            categoryRespository.save(category);
//            categoryIdCreated= categoryRespository.findByName(newCategoryName).getId();
//            hashMap.put("categoryNameCreated",newCategoryName);
//            hashMap.put("createdAs",StatusCode.PARENT.toString());
//
//        }
//        else {
//
//            Iterable<Product> products = productRepository.findAll();
//            products.forEach(product -> {
//                if (product.getCategory().getId()==parentId){
//                    throw new GiveMessageException(Arrays.asList(StatusCode.FAILED.toString()),
//                            Arrays.asList("This category with parent ID="+parentId+" can not be created since products are attached to this Parent Category already")
//                            );
//                }
//            });
//
//
//
//
//        }
//
//
//
//
//        hashMap.put("newCategoryId", String.valueOf(categoryIdCreated));
//        commonResponseVO.setData(hashMap);
//    return commonResponseVO;
//    }
}
