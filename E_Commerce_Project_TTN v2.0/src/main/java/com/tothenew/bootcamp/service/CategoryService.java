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
import net.bytebuddy.dynamic.scaffold.MethodGraph;
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
                    if (query!="category_key" || query!="id"){
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
    public CommonResponseVO<HashMap<String, String>> addCategoryByAdmin(String newCategoryName,Integer parentId)
    {
        int categoryIdCreated=0;
            int fetchedParentCategoryId;
        CommonResponseVO<HashMap<String, String>> commonResponseVO = new CommonResponseVO();
        //this hasmap will hold the actual data like.. newcreated-id
        HashMap<String, String> hashMap = new HashMap<String, String>();

        //called all the categories which exist in db
        Iterable<Category> categories = categoryRespository.findAll();

        //if parentid is not given.. that means jst to create parent ccategory
        if (parentId==null){
            categories.forEach(category -> {
                if (category.getParentCategory()==null){

                    if (newCategoryName.equals(category.getName())){
                        throw new GiveMessageException(Arrays.asList(StatusCode.EXIST.toString()),
                                Arrays.asList("This name category Already Exist as Parent Category"));
                    }
                }
                else if (category.getParentCategory().getName().equals(newCategoryName)){
                    throw new GiveMessageException(Arrays.asList(StatusCode.EXIST.toString()),
                            Arrays.asList("This name category Already Exist as Child Category"));

                }
            });
            Category category = new Category();
            category.setName(newCategoryName);
            categoryRespository.save(category);
            categoryIdCreated= categoryRespository.findByName(newCategoryName).getId();
            hashMap.put("categoryNameCreated",newCategoryName);
            hashMap.put("createdAs",StatusCode.PARENT.toString());
        }
        else {
            Iterable<Product> products = productRepository.findAll();
            products.forEach(product -> {
                try {
                    int productParentId = product.getCategory().getId();
                    if (productParentId == parentId.intValue()) {
                        throw new GiveMessageException(Arrays.asList(StatusCode.FAILED.toString()),
                                Arrays.asList("This category with parent ID=" + parentId.intValue() + " can not be created since products are attached to this Parent Category already")
                        );
                    }
                }catch (NullPointerException e){
                }
            });

            fetchedParentCategoryId=parentId.intValue();
            while (true) {
                try {

                    Iterable<Category> parentCategories = categoryRespository.findByParentId(fetchedParentCategoryId);
                    parentCategories.forEach(parentCategory -> {

                        if (parentCategory.getName().equals(newCategoryName)) {
                            throw new GiveMessageException(Arrays.asList(StatusCode.FAILED.toString()),
                                    Arrays.asList("Category name already EXIST in the ^ Tree"));

                        }
                    });
                }catch (NullPointerException e){
                }
                try {
                    Category category = categoryRespository.findById(fetchedParentCategoryId).get();
                    fetchedParentCategoryId = category.getParentCategory().getId();

                }catch (NullPointerException ee){
                    Category ToppestCategory = categoryRespository.findById(fetchedParentCategoryId).get();

                    if (ToppestCategory.getName().equals(newCategoryName)){
                        throw new GiveMessageException(Arrays.asList( StatusCode.FAILED.toString()),Arrays.asList("Category name already EXIST in the ^ Tree"));
                    }
                    System.out.println("No more parent Exist futher"); StatusCode.FAILED.toString();
                    break;
                }

            }

            fetchedParentCategoryId=parentId.intValue();
                Category parentCategory = categoryRespository.findById(fetchedParentCategoryId).get();

                /*
                this method is to check whether the category name eist down in the tree
                 */
               checkCategoryNameExistDownInTree(parentCategory,newCategoryName);
                Category newChildCategory = new Category();
                newChildCategory.setName(newCategoryName);
                newChildCategory.setParentCategory(parentCategory);
                categoryRespository.save(newChildCategory);
               categoryIdCreated= categoryRespository.findByName(newCategoryName).getId();
            hashMap.put("categoryNameCreated",newCategoryName);
            hashMap.put("createdAs",StatusCode.CHILD.toString());
        }
        hashMap.put("newCategoryId", String.valueOf(categoryIdCreated));
        commonResponseVO.setData(hashMap);
    return commonResponseVO;
    }






    public void checkCategoryNameExistDownInTree(Category category,String newCategoryName){
        Iterable<Category> categories = categoryRespository.findByParentId(category.getId());
        if (categories==null){
            return;
        }
        categories.forEach(category1 -> {
            if (category1.getName().equals(newCategoryName)){
                throw new GiveMessageException(Arrays.asList(StatusCode.FAILED.toString()),
                        Arrays.asList("Category name already EXIST in the !^ Tree"));
            }
            checkCategoryNameExistDownInTree(category1,newCategoryName);
        });

    }


    public CommonResponseVO<LinkedHashMap> viewSingleCategoryByAdmin(int id){
        CommonResponseVO<LinkedHashMap> commonResponseVO = new CommonResponseVO<>();

        try {
            LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap>> hashMap = new LinkedHashMap<>();
        Category category = categoryRespository.findById(id).get();
        Iterable<Category> childCategories;
            LinkedHashMap<String, String> childCategoryDetailContainer = new LinkedHashMap<>();
            LinkedHashMap<String, LinkedHashMap> childContainer = new LinkedHashMap<>();
            int[] j=new int[]{0};
            try {
            childCategories = categoryRespository.findByParentId(id);
            childCategories.forEach(childCategory->{
                childCategoryDetailContainer.put("id", String.valueOf(childCategory.getId()));
                childCategoryDetailContainer.put("categoryName", childCategory.getName());
                childCategoryDetailContainer.put("parent_id", String.valueOf(childCategory.getParentCategory().getId()));
                childContainer.put(String.valueOf( j[0]++),childCategoryDetailContainer);

            });

            hashMap.put("childCategories",childContainer);
        }
        catch (NullPointerException e){
            hashMap.put("Child Categories",childContainer);
        }

        LinkedHashMap<String, LinkedHashMap> categoryTree = new LinkedHashMap<>();
        int[] count = new int[]{0};
        while (true) {
            LinkedHashMap<String, String> categoryDetailContainer = new LinkedHashMap<>();
            if (category.getParentCategory() != null) {
                categoryDetailContainer.put("id", String.valueOf(category.getId()));
                categoryDetailContainer.put("categoryName", category.getName());
                categoryDetailContainer.put("parent_id", String.valueOf(category.getParentCategory().getId()));

                categoryTree.put(String.valueOf(count[0]), categoryDetailContainer);
                count[0]++;
                category = category.getParentCategory();
            } else {
                categoryDetailContainer.put("id", String.valueOf(category.getId()));
                categoryDetailContainer.put("categoryName", category.getName());
                categoryDetailContainer.put("parent_id", StatusCode.NOT_AVAILABLE.toString());

                categoryTree.put(String.valueOf(count[0]), categoryDetailContainer);
                break;
            }
            hashMap.put("categoryTree",categoryTree);
            commonResponseVO.setData(hashMap);
        }

        }catch (NullPointerException e){
            throw new GiveMessageException(Arrays.asList(StatusCode.DOES_NOT_EXIST.toString()),
                    Arrays.asList("Provided Category ID does not Exist"));
        }
        catch (NoSuchElementException e){
            throw new GiveMessageException(Arrays.asList(StatusCode.DOES_NOT_EXIST.toString()),
                    Arrays.asList("Provided Category ID does not Exist"));
        }


        return commonResponseVO;
    }


    /***
     *
     * @param max
     * @param offset
     * @param order
     * @param sort
     * @param query
     * @return
     * 1->
     */
    public CommonResponseVO<LinkedHashMap> viewAllCategoriesByAdmin(Integer max,Integer offset,
                                                                    String order,String sort, String query)
    {
        int[] count = new int[]{0};
        LinkedHashMap<String, Category> categoriesHashmap = new LinkedHashMap<>();
        Iterable<Category> allCategories = categoryRespository.findAll();
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
                if (query!="id" ||query!="parent_id" || query!="name"){
                    throw new GiveMessageException(Arrays.asList(StatusCode.INVALID.toString()),Arrays.asList("Property name provided was invalid"));
                }
                property=query;
            }
            Pageable pageable = PageRequest.of(pageOffset,maxRecordsPerPage,direction,property);
            allCategories=categoryRespository.findAll(pageable);

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
                if (query!="id" ||query!="parent_id" || query!="name"){
                    throw new GiveMessageException(Arrays.asList(StatusCode.INVALID.toString()),Arrays.asList("Property name provided was invalid"));
                }
                property=query;
            }

            allCategories= categoryRespository.findAllOrderBy(property,direction.toString());

        }
        else if (query!=null){
            if (query!="id" ||query!="parent_id" || query!="name"){
                throw new GiveMessageException(Arrays.asList(StatusCode.INVALID.toString()),Arrays.asList("Property name provided was invalid"));
            }

            allCategories=categoryRespository.findAllOrderBy(direction.toString(),query);
        }
        else {
            allCategories=categoryRespository.findAll();
        }
        allCategories.forEach(category -> {
            category.setParentCategory(null);
            category.setProductSet(null);
//            List<Product> productList = category.getProductSet();
//            productList.forEach(product -> {
//                product.setProductVariationlist(null);
//                product.setSeller_seller(null);
//                product.setCategory(null);
//
//            });
//            category.setProductSet(productList);
            categoriesHashmap.put("category_"+count[0]++,category);
        });

        CommonResponseVO<LinkedHashMap> commonResponseVO = new CommonResponseVO<>();
        commonResponseVO.setData(categoriesHashmap);
        return commonResponseVO;
    }
}
