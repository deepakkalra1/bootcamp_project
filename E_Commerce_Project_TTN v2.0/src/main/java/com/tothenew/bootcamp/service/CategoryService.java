package com.tothenew.bootcamp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tothenew.bootcamp.entity.ProductContent.*;
import com.tothenew.bootcamp.enums.StatusCode;
import com.tothenew.bootcamp.exceptionHandling.GiveMessageException;
import com.tothenew.bootcamp.pojo.CategoryMetadataValuePojo;
import com.tothenew.bootcamp.pojo.CommonResponseVO;
import com.tothenew.bootcamp.repositories.CategoryMetadataFieldRepository;
import com.tothenew.bootcamp.repositories.CategoryMetadataValueRepository;
import com.tothenew.bootcamp.repositories.CategoryRespository;
import com.tothenew.bootcamp.repositories.ProductRepository;
import net.bytebuddy.dynamic.scaffold.MethodGraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Service
public class CategoryService {
    @Autowired
    CategoryMetadataFieldRepository categoryMetadataFieldRepository;

    @Autowired
    CategoryRespository categoryRespository;

    @Autowired
    ProductRepository productRepository;


    @Autowired
    CategoryMetadataValueRepository categoryMetadataValueRepository;



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
        int totalSize = categoryMetadataFieldRepository.findAll().size();
        int maxRecordsPerPage =totalSize;
        int pageOffset=0;
        Direction direction=Direction.ASC;
        String property="id";
            if (offset!=null) {
                pageOffset = offset.intValue();
            }
            if (max!=null) {
                maxRecordsPerPage = max.intValue();
            }

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
                    if (query.equals("category_key")|| query.equals("id") ){

                    }
                    else{
                        throw new GiveMessageException(Arrays.asList(StatusCode.INVALID.toString()),Arrays.asList("Property name provided was invalid. ONLY (category_key,id) is eligible"));
                    }

                    property=query;
                }
                Pageable pageable = PageRequest.of(pageOffset,maxRecordsPerPage,direction,property);
                categoryMetadataFields=categoryMetadataFieldRepository.findAll(pageable);
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
            checkCategoryNameExistUpInTree(parentId,newCategoryName);

            fetchedParentCategoryId=parentId.intValue();
                Category parentCategory = categoryRespository.findById(fetchedParentCategoryId).get();

                /*
                this method is to check whether the category name exist down in the tree
                 */
               checkCategoryNameExistDownInTree(parentCategory.getId(),newCategoryName);

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


    public void checkCategoryNameExistUpInTree(int parent_id,String newCategoryName){
        int fetchedParentCategoryId=parent_id;
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
    }



    public void checkCategoryNameExistDownInTree(int id,String newCategoryName){
        Category category = categoryRespository.findById(id).get();
        Iterable<Category> categories = categoryRespository.findByParentId(category.getId());
        if (categories==null){
            return;
        }
        categories.forEach(category1 -> {
            if (category1.getName().equals(newCategoryName)){
                throw new GiveMessageException(Arrays.asList(StatusCode.FAILED.toString()),
                        Arrays.asList("Category name already EXIST in the !^ Tree"));
            }
            checkCategoryNameExistDownInTree(category1.getId(),newCategoryName);
        });

    }






//---------------------------------------------------------------------------------------------------------->
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





    //------------------------------------------------------------------------------------------------------->
    /***
     * @param max
     * @param offset
     * @param order
     * @param sort
     * @param query
     * @return
     */
    public CommonResponseVO<LinkedHashMap> viewAllCategoriesByAdmin(Integer max,Integer offset,
                                                                    String order,String sort, String query)
    {
        int[] count = new int[]{0};
        LinkedHashMap<String, Category> categoriesHashmap = new LinkedHashMap<>();
        Iterable<Category> allCategories;
        int maxRecordsPerPage =categoryRespository.findAll().size();
        int pageOffset=0;
        Direction direction=Direction.ASC;
        String property="id";

            if (offset!=null) {
                pageOffset = offset.intValue();
            }
            if (max!=null) {
                maxRecordsPerPage = max.intValue();
            }
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
                if (query.equals("id")||query.equals("parent_id") || query.equals("name")){
                }
                else{
                    throw new GiveMessageException(Arrays.asList(StatusCode.INVALID.toString()),Arrays.asList("Property name provided was invalid. ONLY (id,parent_id,name) is eligible"));
                }
                property=query;
            }
            Pageable pageable = PageRequest.of(pageOffset,maxRecordsPerPage,direction,property);
            allCategories=categoryRespository.findAll(pageable);


        allCategories.forEach(category -> {
            category.setParentCategory(null);
            category. setProductSet(null);
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





    //-------------------------------------------------------------------------------------------------------->
    public CommonResponseVO updateCategoryByAdmin(int categoryId,String newCategoryName){
        int fetchedParentCategoryId;
        fetchedParentCategoryId=categoryId;
        checkCategoryNameExistUpInTree(categoryId,newCategoryName);
        checkCategoryNameExistDownInTree(categoryId,newCategoryName);
        Category updatedCategory= categoryRespository.findById(categoryId).get();
        updatedCategory.setName(newCategoryName);
        categoryRespository.save(updatedCategory);
        return new CommonResponseVO(Arrays.asList(StatusCode.SUCCESS.toString()));
    }





    //--------------------------------------------------------------------------------------------------->
    /***
     *
     * @param metadata
     * @return
     *
     */
    public CommonResponseVO addCategoryMetadataValueByAdmin(HashMap<String, String> metadata){

        CategoryMetadataValue categoryMetadataValue = new CategoryMetadataValue();
        try {

           int categoryId = Integer.parseInt( metadata.get("categoryId"));
            Category category = categoryRespository.findById(categoryId).get();
            categoryMetadataValue.setCategory(category);


            int categoryMetadataFieldId = Integer.parseInt( metadata.get("categoryMetadataFieldId"));
            CategoryMetadataField categoryMetadataField =
                    categoryMetadataFieldRepository.findById(categoryMetadataFieldId).get();
            categoryMetadataValue.setCategoryMetadataField(categoryMetadataField);

            Iterator it = categoryMetadataValueRepository.findValueCombinationOfFieldIdAndCategoryId(categoryMetadataFieldId,categoryId).iterator();

            if (it.hasNext()==true){
                throw new GiveMessageException(Arrays.asList(StatusCode.EXIST.toString()),
                        Arrays.asList("Value for the combination of category id and field id already exist"));
            }

            int count[] = new int[]{0};
            String values="";
            Set<Entry<String, String>> entrySet= metadata.entrySet();
            boolean exist=false;
            for (Entry entry: entrySet){
                if (Pattern.matches("value_[0-9]*$",entry.getKey().toString() )){
                    if (values.equals("")){
                        values=entry.getValue().toString();
                    }
                    else {
                        String[] sepratedValues = values.split(",");
                        for (String val:sepratedValues){
                            if (entry.getValue().toString().equals(val)){
                                throw new GiveMessageException(Arrays.asList(StatusCode.NOT_VALID_FORMAT.toString()),
                                        Arrays.asList("Duplicate values are given, Values must be unique"));
                            }
                        }
                        values+=","+entry.getValue().toString();
                    }

                    exist=true;

                }
                else if(entry.getKey().toString().equals("categoryId") || entry.getKey().toString().equals("categoryMetadataFieldId")){
                    continue;
                }
                else {
                    throw new GiveMessageException(Arrays.asList(StatusCode.NOT_VALID_FORMAT.toString()),
                            Arrays.asList("Unknown keys are being passed"));
                }
            }
            if (exist==false){
                throw new GiveMessageException(Arrays.asList(StatusCode.NOT_VALID_FORMAT.toString()),
                        Arrays.asList("No values were provided"));
            }
            categoryMetadataValue.setCategoryValue(values);
            categoryMetadataValueRepository.save(categoryMetadataValue);
        }catch (NullPointerException e){
            throw new GiveMessageException(Arrays.asList(StatusCode.NOT_VALID_FORMAT.toString())
            ,Arrays.asList("Either Key is not proved Or Does not match the required key"));
        }
        catch (NumberFormatException number){
            throw new GiveMessageException(Arrays.asList(StatusCode.NOT_VALID_FORMAT.toString()),
                    Arrays.asList("Either Category Id or Field Id is not provided or are in wrong format"));
        }
        catch (NoSuchElementException ele){
            throw new GiveMessageException(Arrays.asList(StatusCode.DOES_NOT_EXIST.toString()),
                    Arrays.asList("Either Category Id or Field Id provided does not exist"));
        }
        CommonResponseVO commonResponseVO = new CommonResponseVO(Arrays.asList(StatusCode.SUCCESS.toString()));
        return commonResponseVO;
    }






    //----------------------------------------------------------------------------------------------------->
    public CommonResponseVO updateCategoryMetadataValueByAdmin(HashMap<String, String> metadata){

        CategoryMetadataValue categoryMetadataValue = new CategoryMetadataValue();
        try {
            int categoryId = Integer.parseInt( metadata.get("categoryId"));
            metadata.remove("categoryId");
            int categoryMetadataFieldId = Integer.parseInt( metadata.get("categoryMetadataFieldId"));
            metadata.remove("categoryMetadataFieldId");
            Iterator<CategoryMetadataValue> it = categoryMetadataValueRepository.findValueCombinationOfFieldIdAndCategoryId(categoryMetadataFieldId,categoryId).iterator();
            if (it.hasNext()==false){
                throw new GiveMessageException(Arrays.asList(StatusCode.EXIST.toString()),
                        Arrays.asList("Value for the combination of category id and field id does not exist"));
            }
            categoryMetadataValue= it.next();
            String values = categoryMetadataValue.getCategoryValue();
            String[] valArr = values.split(",");
            List<String> valuesList=new ArrayList<>();
            for (String val:valArr){
                valuesList.add(val);
            }
            int count[] = new int[]{0};

            Set<Entry<String, String>> entrySet= metadata.entrySet();
            for (Entry entry: entrySet) {

                int indexesOfValueListToBeDeleted=-1;
                boolean eligibleToBeDeleted=false;
                if (Pattern.matches("delete_value_[0-9]*$", entry.getKey().toString())) {
                    eligibleToBeDeleted=true;
                    for (String val : valuesList) {
                        if (val.equals(entry.getValue().toString())) {
                            indexesOfValueListToBeDeleted=valuesList.indexOf(val);

                        }
                    }

                }
                else if(Pattern.matches("add_value_[0-9]*$", entry.getKey().toString())){
                    continue;
                }
                else {
                    throw new GiveMessageException(Arrays.asList(StatusCode.NOT_VALID_FORMAT.toString()),
                            Arrays.asList("Unknown keys are being passed"));

                }
                if (indexesOfValueListToBeDeleted!=-1) {
                    valuesList.remove(indexesOfValueListToBeDeleted);
                    eligibleToBeDeleted=false;

                }
                if (eligibleToBeDeleted){
                    throw new GiveMessageException(Arrays.asList(StatusCode.DOES_NOT_EXIST.toString()),
                            Arrays.asList("Value to be deleted either does not exist or is in wrong format"));
                }
                metadata.remove(entry.getKey());
            }
            for (Entry entry: entrySet) {
                if (Pattern.matches("add_value_[0-9]*$", entry.getKey().toString())) {
                    for (String val : valuesList) {
                        if (val.equals(entry.getValue().toString())) {
                            throw new GiveMessageException(Arrays.asList(StatusCode.EXIST.toString()),
                                    Arrays.asList("New value given to be added is not unique"));
                        }

                    }
                    valuesList.add(entry.getValue().toString());

                }
                else {
                    throw new GiveMessageException(Arrays.asList(StatusCode.NOT_VALID_FORMAT.toString()),
                            Arrays.asList("Unknown keys are being passed"));

                }
            }
            String finalValue="";
            for (String val:valuesList){
                if (finalValue.equals("")){
                    finalValue=val;
                }
                else {
                    finalValue+=","+val;
                }
            }
            categoryMetadataValue.setCategoryValue(finalValue);
            categoryMetadataValueRepository.save(categoryMetadataValue);
        }
        catch (NullPointerException e){
            throw new GiveMessageException(Arrays.asList(StatusCode.NOT_VALID_FORMAT.toString())
                    ,Arrays.asList("Either Key is not proved Or Does not match the required key"));
        }
        catch (NumberFormatException number){
            throw new GiveMessageException(Arrays.asList(StatusCode.NOT_VALID_FORMAT.toString()),
                    Arrays.asList("Either Category Id or Field Id is not provided or are in wrong format"));
        }
        catch (NoSuchElementException ele){
            throw new GiveMessageException(Arrays.asList(StatusCode.DOES_NOT_EXIST.toString()),
                    Arrays.asList("Either Category Id or Field Id provided does not exist"));
        }
        CommonResponseVO commonResponseVO = new CommonResponseVO(Arrays.asList(StatusCode.SUCCESS.toString()));
        return commonResponseVO;
    }





    //--------------------------------------------------------------------------------------------------------->
    public CommonResponseVO<LinkedHashMap> viewCategoriesBySeller(){
        Iterable<Category> allCategories = categoryRespository.findAll();
        List<Integer> idOfAllCategories = new ArrayList<>();
        List<Category> leafNodeCategories = new ArrayList<>();
        allCategories.forEach(category -> {
            idOfAllCategories.add(category.getId());
        });

        idOfAllCategories.forEach(id->{
              List<Category> categoryIterable = categoryRespository.findByParentIdInList(id);

            System.out.println(categoryIterable.iterator().hasNext());
            if (categoryIterable.iterator().hasNext()==false){

                Category category =categoryRespository.findById(id).get();
//                ObjectMapper objectMapper = new ObjectMapper();
//                try {
//
//                    List<CategoryMetadataValue> listOfCategoryMetadata = category.getCategoryMetadataValues();
//
//                    System.out.println(listOfCategoryMetadata);
//                    int sizeOfmetadata = listOfCategoryMetadata.size();
//                    if (sizeOfmetadata>0){
//                        LinkedHashMap<String, LinkedHashMap> metadataValueHashmap = new LinkedHashMap<>();
//                    for (int i=0;i<sizeOfmetadata;i++){
//                        System.out.println(i);
//                        CategoryMetadataValue categoryMetadataValue = listOfCategoryMetadata.get(i);
//                        CategoryMetadataValuePojo categoryMetadataValuePojo = new CategoryMetadataValuePojo();
//
//                        categoryMetadataValuePojo.setCategoryId(categoryMetadataValue.getCategory().getId());
//                        categoryMetadataValuePojo.setCategoryMetadataFieldId(categoryMetadataValue.getCategoryMetadataField().getId());
//                        categoryMetadataValuePojo.setCategoryValue(categoryMetadataValue.getCategoryValue());
//
//                        String jsonString = objectMapper.writeValueAsString(categoryMetadataValuePojo);
//                        System.out.println(jsonString);
//                         LinkedHashMap pojoConvertedHashmap = objectMapper.readValue(jsonString,LinkedHashMap.class);
//                        metadataValueHashmap.put("metadataValue_"+i,pojoConvertedHashmap);
//                    }
//                    category.setLinkedCategoryValueHashMap(metadataValueHashmap);
//
//                    }
//                }
//                catch (NullPointerException e){}
//                catch (JsonProcessingException jsonEx){}

                leafNodeCategories.add(category);

            }
        });
        int[] count = new int[]{0};
        LinkedHashMap<String, Category> leafCategoryHashmap = new LinkedHashMap<>();
        leafNodeCategories.forEach(singleLeafCategoryNode->{

             singleLeafCategoryNode.setProductSet(null);
             try {
                 Category parentCat = singleLeafCategoryNode.getParentCategory();
                 parentCat.setProductSet(null);
                 parentCat.setParentCategory(null);
                 singleLeafCategoryNode.setParentCategory(parentCat);
             }
             catch (NullPointerException e){}



           leafCategoryHashmap.put("leafCategoryNode_"+count[0]++,singleLeafCategoryNode);
        });

        CommonResponseVO<LinkedHashMap> commonResponseVO = new CommonResponseVO<>();
        commonResponseVO.setData(leafCategoryHashmap);
        return commonResponseVO;

    }





    //------------------------------------------------------------------------------------------------------->
    public CommonResponseVO viewCategoriesByCustomer(Integer categoryId){
        LinkedHashMap<String, Category> allCategoriesMap = new LinkedHashMap<>();
        List<Category> categoryList;
        int[] count = new int[]{0};
        if (categoryId!=null){
            try {
                Category category = categoryRespository.findById(categoryId.intValue()).get();
            }
            catch (NoSuchElementException nosuch){
                throw new GiveMessageException(Arrays.asList(StatusCode.DOES_NOT_EXIST.toString()),
                        Arrays.asList("Category Id provided Does Not Exist"));

            }

            categoryList = categoryRespository.findByParentIdInList(categoryId.intValue());
            //if immediate children are present of provided category id
            if (categoryList.iterator().hasNext()){
                categoryList.forEach(childCategory->{

                    childCategory.setProductSet(null);
                    childCategory.setParentCategory(null);
                    childCategory.setCategoryMetadataValues(null);
                    childCategory.setParentCategory(null);
                    allCategoriesMap.put("childCategory_"+count[0]++,childCategory);
                });

            }
            //if no child of provided is present
            else {
                throw new GiveMessageException(Arrays.asList(StatusCode.NOT_AVAILABLE.toString())
                ,Arrays.asList("No immediate child Categories Present"));
            }

        }else {
            categoryList = categoryRespository.findParentCategories();

            categoryList.forEach(category -> {
                category.setProductSet(null);
                category.setParentCategory(null);
                category.setCategoryMetadataValues(null);
                category.setParentCategory(null);
                allCategoriesMap.put("parentCategory_"+count[0]++,category);
            });

        }

        CommonResponseVO<LinkedHashMap<String, Category>> commonResponseVO = new CommonResponseVO(Arrays.asList(StatusCode.SUCCESS.toString()));
        commonResponseVO.setData(allCategoriesMap);
        return commonResponseVO;
    }





    //--------------------------------------------------------------------------------------------------------->
    public CommonResponseVO getFiltersOfCategoryByCustomer(int categoryId){
        LinkedHashMap<String, LinkedHashMap> finalFilters = new LinkedHashMap<>();
            List<CategoryMetadataValue> filters = new ArrayList<>();
            LinkedHashMap<String, CategoryMetadataValue> filterHashmap = new LinkedHashMap<>();
            LinkedHashMap<String, String> minAndMaxPriceHashmap = new LinkedHashMap<>();

            /*
            if category id is not present
             */
            Category category = categoryRespository.findById(categoryId).get();


        /*
        fetcching categoryMetadataFilter for provided id
         */
            int[] count = new int[]{0};
            filters = categoryMetadataValueRepository.findByCategoryId(categoryId);
            filters.forEach(filter->{
                filter.setCategory(null);
                filterHashmap.put("filter_"+count[0]++,filter);
            });
            count[0]=0;



        /*
        fetching brands,min and max price for provided category id
         */
        Set<String> brandNames = new HashSet<>();
        Set<Integer> productPrices = new HashSet<>();
            LinkedHashMap<String, String> brandsHashmap = new LinkedHashMap<>();
        findBrandNameDownInTree(categoryId,brandNames,productPrices);
            brandNames.forEach(brandName->{
                brandsHashmap.put("brandName_"+count[0]++,brandName);
                System.out.println(brandName);
            });
            int min = Collections.min(productPrices);
            int max = Collections.max(productPrices);
            minAndMaxPriceHashmap.put("minPrice",String.valueOf(min));
            minAndMaxPriceHashmap.put("maxPrice",String.valueOf(max));


        /*
        adding all seprate  filters like brands,metadataValues etc
         */
        finalFilters.put("metdataFilters",filterHashmap);
        finalFilters.put("brands",brandsHashmap);
        finalFilters.put("minAndMaxPrice",minAndMaxPriceHashmap);
        CommonResponseVO<LinkedHashMap> commonResponseVO =
                new CommonResponseVO<>(Arrays.asList(StatusCode.SUCCESS.toString()));
        commonResponseVO.setData(finalFilters);
        return commonResponseVO;
    }






    //--------------------------------------------------------------------------------------------------------->
    /**
     * @param id
     * @return
     * 1->adding products brands of provided category id..(if it is last)
     * 2-> calling itself
     * so that if any child category of provided catId is there.. is can add product of them..if it does not
     * have any product assigned..it will repeat itself till.. it reach to case.. when no category is further
     */
    public void findBrandNameDownInTree(int id,Set<String> brandNames,Set<Integer> productPrices){
        Category category = categoryRespository.findById(id).get();

        //fetching no of product realated to fetchhed category ..
        int size = category.getProductSet().size();

        //this loop will iterate through all the products and add its brand name
        for (int i=0;i<size;i++){
            //adding brand name of looped product
            brandNames.add(category.getProductSet().get(i).getBrand());
           List<ProductVariation> relatedPoductVariations= category.getProductSet().get(i).getProductVariationlist();

           //this  loop will find product variation prices and add it into SET(productPrices)
           for (int j=0;j<relatedPoductVariations.size();j++){
               productPrices.add(relatedPoductVariations.get(j).getPrice());
           }
            System.out.println(relatedPoductVariations.size());
        }

        Iterable<Category> categories = categoryRespository.findByParentId(id);
        if (categories.iterator().hasNext()==false){
            return ;
        }
        else {
            categories.forEach(category1 -> {
                findBrandNameDownInTree(category1.getId(),brandNames,productPrices);
            });
        }

        return ;
    }
}
