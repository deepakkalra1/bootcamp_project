package com.tothenew.bootcamp.service;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.sun.org.apache.xpath.internal.operations.Bool;
import com.tothenew.bootcamp.constants.JwtUtility;
import com.tothenew.bootcamp.entity.ProductContent.Category;
import com.tothenew.bootcamp.entity.ProductContent.CategoryMetadataValue;
import com.tothenew.bootcamp.entity.ProductContent.Product;
import com.tothenew.bootcamp.entity.ProductContent.ProductVariation;
import com.tothenew.bootcamp.entity.User.Seller;
import com.tothenew.bootcamp.enums.StatusCode;
import com.tothenew.bootcamp.exceptionHandling.GiveMessageException;
import com.tothenew.bootcamp.pojo.CommonResponseVO;
import com.tothenew.bootcamp.pojo.ProductPojo;
import com.tothenew.bootcamp.pojo.ProductVariationPojo;
import com.tothenew.bootcamp.repositories.*;
import javafx.scene.chart.CategoryAxis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class ProductService {

    @Autowired
    SellerRepository sellerRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRespository categoryRespository;

    @Autowired
    ProductVariationRepository productVariationRepository;

    @Autowired
    CategoryMetadataValueRepository categoryMetadataValueRepository;




    //----------------------------------------------------------------------------------------------------------->
    public CommonResponseVO addProductBySeller(String token,String productName, int cayegoryId, String brandName,String description,
                                               Boolean is_cancelable, Boolean is_returnable){

        String username = JwtUtility.findUsernameFromToken(token);
        Seller seller = sellerRepository.findByEmail(username);

       List<Product> anyExistingProductOfSellerWithSameNameInSameCategory= productRepository.findProductsWithSellerIdAndProductNameAndCategoryId(seller.getId(),productName,cayegoryId);

       if (anyExistingProductOfSellerWithSameNameInSameCategory.isEmpty()){

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





    //------------------------------------------------------------------------------------------------------->
    public void checkIfItIsLeafCategoryNode(int categoryId){

        Iterable<Category> categories= categoryRespository.findByParentId(categoryId);
        if (categories.iterator().hasNext()){
            throw new GiveMessageException(Arrays.asList(StatusCode.FAILED.toString())
                    ,Arrays.asList("Provided Category Id is not a leaf Category, You can not append product to this category, go for its child category"));

        }

    }





    //------------------------------------------------------------------------------------------------------->
    public void addProductVariationForProduct
    (String token, int productId, MultipartFile primaryImage, LinkedHashMap<String, String>metadata
            , Integer quantity, Integer price
    ) throws IOException {
        String username = JwtUtility.findUsernameFromToken(token);
        Seller seller = sellerRepository.findByEmail(username);
        ProductVariation productVariation = new ProductVariation();
        //since it returns optional.. so if not found.. will throw noSuchElementException
        Product product = productRepository.findById(productId).get();
        System.out.println("test");
        int quantityOfProductVariation=0;
        if (quantity!=null){
            quantityOfProductVariation=quantity.intValue();
        }
        int priceOfProductVariation=0;
        if (price!=null){
            priceOfProductVariation=price.intValue();
        }
        //checking if provided id' product belongs to current seller or not
        if (product.getSeller_seller().getSellerOrganizationDetails().getSeller().getId() != seller.getId() ){
            throw new GiveMessageException(Arrays.asList(StatusCode.FAILED.toString())
                    ,Arrays.asList("Provided Product Id does not belongs to seller = "+username));

        }
        //product should be active
        if (product.isIs_active()==false){
            throw new GiveMessageException(Arrays.asList(StatusCode.FAILED.toString())
                    ,Arrays.asList("Provided Product is not Active"));

        }
        if(primaryImage!=null){
            //a random no to be the name of image
            int randomImageNumber = (int) Math.random();
            byte[] primaryImageBytes = primaryImage.getBytes();
            String[] imageNameArr=primaryImage.getOriginalFilename().split(".");

            if (imageNameArr[imageNameArr.length-1]!="jpg"){
                throw new GiveMessageException(Arrays.asList(StatusCode.FAILED.toString()),
                        Arrays.asList("Jpg image can be uploaded only"));
            }
            Path path = Paths.get(System.getProperty("user.dir")+"/src/main/resources/images/productVariationImages/"+randomImageNumber+".jpg");
            Files.write(path,primaryImageBytes);
            productVariation.setPrimary_image(System.getProperty("user.dir")+"/src/main/resources/images/profileImages/"+randomImageNumber+".jpg");
        }


        List<CategoryMetadataValue> categoryMetadataValuesByCategoryId = categoryMetadataValueRepository.findByCategoryId(product.getCategory().getId());
        if (categoryMetadataValuesByCategoryId.isEmpty()){
            throw new GiveMessageException(Arrays.asList("No fields are present for this category"));
        }
        HashMap<String, String> finalMetadata = new HashMap<>();
            int sizeOfMDVfromCategory=categoryMetadataValuesByCategoryId.size();
            int[] checkIfFound= new int[]{-1,-1};

            //looping on sent metadata to be added to product variation
        metadata.forEach((k, v)->{
             checkIfFound[1]=0;

                    //looping through avaible metadataValues.. if provided one is available thr in that category
                    for (int i=0;i<sizeOfMDVfromCategory;i++){

                        if (k.equals(categoryMetadataValuesByCategoryId.get(i).getCategoryMetadataField().getCategoryKey())){
                            //setting to 0 that means .. key has matched..
                            checkIfFound[1]=-1;
                            checkIfFound[0]=0;
                            String[] valueArr= categoryMetadataValuesByCategoryId.get(i).getCategoryValue().split(",");
                           for (int u=0;u<valueArr.length;u++){
                               if (v.equals(valueArr[u])){
                                   finalMetadata.put(v,valueArr[u]);
                                   checkIfFound[0]=-1;
                                   break;
                               }
                           }
                            if (checkIfFound[0]==0){
                                throw new GiveMessageException(Arrays.asList("Provided Field Value Does not exist in defined value"));
                            }

                        }
                    }
            if (checkIfFound[1]==0){
                throw new GiveMessageException(Arrays.asList("Provided Field key Does not exist for this category"));
            }


        });

        //putting all values
        productVariation.setMetadataHashmap(finalMetadata);
        productVariation.setPrice(priceOfProductVariation);
        productVariation.setQuantity_available(quantityOfProductVariation);
        productVariation.setProduct(product);
        productVariationRepository.save(productVariation);
    }





    //------------------------------------------------------------------------------------------------------>
    public CommonResponseVO viewProductBySeller(String token,int productId){
        String username = JwtUtility.findUsernameFromToken(token);
        Seller seller = sellerRepository.findByEmail(username);
        //since it returns optional.. so if not found.. will throw noSuchElementException
        Product product = productRepository.findById(productId).get();


        //checking if provided id' product belongs to current seller or not
        if (product.getSeller_seller().getSellerOrganizationDetails().getSeller().getId() != seller.getId() ){
            throw new GiveMessageException(Arrays.asList(StatusCode.FAILED.toString())
                    ,Arrays.asList("Provided Product Id does not belongs to seller = "+username));

        }
        //product should be active
        if (product.isIs_active()==false){
            throw new GiveMessageException(Arrays.asList(StatusCode.FAILED.toString())
                    ,Arrays.asList("Provided Product is not Active"));

        }
        product.setSeller_seller(null);
        Category productCategory = product.getCategory();
        productCategory.setParentCategory(null);
        productCategory.setCategoryMetadataValues(null);
        productCategory.setProductSet(null);
//        productCategory.setLinkedCategoryValueHashMap(null);

        /*
        this piece of code can help u to have product variation list with product
         */
        product.setCategory(productCategory);
        List<ProductVariation>productVariations = product.getProductVariationlist();
        productVariations.forEach(variation->{
            variation.setProduct(null);
        });
        product.setProductVariationlist(productVariations);
        CommonResponseVO<Product> commonResponseVO = new CommonResponseVO(Arrays.asList(StatusCode.SUCCESS.toString()));
        commonResponseVO.setData(product);
        return commonResponseVO;

    }





    //------------------------------------------------------------------------------------------------------>
    public CommonResponseVO viewProductVariationBySeller(String token,int productVariationId){
        String username = JwtUtility.findUsernameFromToken(token);
        Seller seller = sellerRepository.findByEmail(username);
        ProductVariation productVariation = productVariationRepository.findById(productVariationId).get();
        if (productVariation.getProduct().getSeller_seller().getId()!=seller.getId()){
            throw new GiveMessageException(Arrays.asList(StatusCode.FAILED.toString())
                    ,Arrays.asList("You are not owner of provided product variation ID"));
        }
        productVariation.setProduct(null);
        ProductVariationPojo productVariationPojo = new ProductVariationPojo();
        productVariationPojo.setId(productVariation.getId());
        productVariationPojo.setProduct_id(productVariation.getProduct().getId());
        productVariationPojo.setQuantity_available(productVariation.getQuantity_available());
        productVariationPojo.setPrimary_image(productVariation.getPrimary_image());
        productVariationPojo.setIs_active(productVariation.isIs_active());
        productVariationPojo.setPrice(productVariation.getPrice());
        productVariationPojo.setMetadataHashmap(productVariation.getMetadataHashmap());

        CommonResponseVO<ProductVariationPojo> commonResponseVO = new CommonResponseVO<>(Arrays.asList(StatusCode.SUCCESS.toString()));
        commonResponseVO.setData(productVariationPojo);
        return commonResponseVO;

    }






    //------------------------------------------------------------------------------------------------------->
    public CommonResponseVO viewAllProductsOfSeller(String token,Integer max,Integer offset,String order
            ,String sort, String query
    )
    {
        String username = JwtUtility.findUsernameFromToken(token);
        Seller seller = sellerRepository.findByEmail(username);
        int maxNumberOfSellerProducts = productRepository.findProductsWithSellerId(seller.getId()).size();

        int maxRecordsPerPage=maxNumberOfSellerProducts;
        int pageOffset=0;
        Sort.Direction direction= Sort.Direction.ASC;
        String property="id";

        LinkedHashMap<String, ProductPojo> sellerProductsHashmap = new LinkedHashMap<>();
        List<Product> sellerProducts;
        int[] count = new int[]{0};

                if (offset!=null) {
                    pageOffset = offset.intValue();
                }
                if (max!=null) {
                    maxRecordsPerPage = max.intValue();
                    }

                if (sort!=null) {
                    if (sort.toLowerCase().equals("asc")) {
                        direction = Sort.Direction.ASC;
                    } else if (sort.toLowerCase().equals("desc")) {
                        direction = Sort.Direction.DESC;
                    } else {
                        throw new GiveMessageException(Arrays.asList(StatusCode.FAILED.toString()), Arrays.asList("sorting order can be either asc or desc only"));
                    }
                }

                if (query!=null){
                    if (query.equals("name") || query.equals("categoryId") || query.equals("brand") || query.equals("id")){
                    }else {
                        throw new GiveMessageException(Arrays.asList(StatusCode.INVALID.toString()),Arrays.asList("Property name provided was invalid. ONLY (id,brand,categoryId,name) is eligible"));
                    }
                    property=query;
                }

                Pageable pageable = PageRequest.of(pageOffset,maxRecordsPerPage,direction,property);
                sellerProducts = productRepository.findProductsWithSellerId(seller.getId(),pageable);
                if (sellerProducts.isEmpty()){
                    throw new GiveMessageException(Arrays.asList(StatusCode.NOT_AVAILABLE.toString())
                    ,Arrays.asList("No products are available by seller = "+username));
                }

            sellerProducts.forEach(product -> {
                ProductPojo productPojo = new ProductPojo();
                productPojo.setName(product.getName());
                productPojo.setId(product.getId());
                productPojo.setBrand(product.getBrand());
                productPojo.setCategory_id(product.getCategory().getId());
                productPojo.setDescription(product.getDescription());
                productPojo.setIs_active(product.isIs_active());
                productPojo.setIs_cancellable(product.isIs_cancellable());
                productPojo.setIs_returnable(product.isIs_returnable());
                productPojo.setSeller_id(product.getSeller_seller().getId());
                productPojo.setCategory_name(product.getCategory().getName());
/*
tried to set dynamic filtering on entity..
 */
//                SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.
//                        filterOutAllExcept("id","name","description","is_cancellable","is_returnable","is_active","brand");
//                FilterProvider filterProvider = new SimpleFilterProvider().addFilter("sendingAllSellerProducts", filter);
//                MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(product);
//                mappingJacksonValue.setFilters(filterProvider);
                sellerProductsHashmap.put("product_"+count[0]++,productPojo);
            });



        CommonResponseVO<LinkedHashMap> commonResponseVO = new CommonResponseVO<>(Arrays.asList(StatusCode.SUCCESS.toString()));
        commonResponseVO.setData(sellerProductsHashmap);
        return commonResponseVO;
    }






    //------------------------------------------------------------------------------------------------------>
    public CommonResponseVO viewAllProductVariationsBySeller(String token,Integer max,Integer offset,String order
            ,String sort, String query,int productId
    )
    {
        List<ProductVariation> productVariations;
        String username = JwtUtility.findUsernameFromToken(token);
        Seller seller = sellerRepository.findByEmail(username);
        int maxNumberOProductVariation = productVariationRepository.findAllVariationByProductId(productId).size();

        int maxRecordsPerPage=maxNumberOProductVariation;
        int pageOffset=0;
        Sort.Direction direction= Sort.Direction.ASC;
        String property="id";

        Product product = productRepository.findById(productId).get();
        if (product.getSeller_seller().getId()!=seller.getId()){
            throw new GiveMessageException(Arrays.asList(StatusCode.UNAUTHORIZED.toString())
                    ,Arrays.asList("Provided product id does not belongs to you ~"+username));
        }

        LinkedHashMap<String, ProductVariationPojo> productVariationPojoLinkedHashMap = new LinkedHashMap<>();

        if (offset!=null) {
            pageOffset = offset.intValue();
        }
        if (max!=null) {
            maxRecordsPerPage = max.intValue();
        }

        if (sort!=null) {
            if (sort.toLowerCase().equals("asc")) {
                direction = Sort.Direction.ASC;
            } else if (sort.toLowerCase().equals("desc")) {
                direction = Sort.Direction.DESC;
            } else {
                throw new GiveMessageException(Arrays.asList(StatusCode.FAILED.toString()), Arrays.asList("sorting order can be either asc or desc only"));
            }
        }

        if (query!=null){
            if (query.equals("name") || query.equals("categoryId") || query.equals("brand") || query.equals("id")){
            }else {
                throw new GiveMessageException(Arrays.asList(StatusCode.INVALID.toString()),Arrays.asList("Property name provided was invalid. ONLY (id,brand,categoryId,name) is eligible"));
            }
            property=query;
        }
        Pageable pageable = PageRequest.of(pageOffset,maxRecordsPerPage,direction,property);
        productVariations= productVariationRepository.findAllVariationByProductId(productId,pageable);
        int[] count = new int[]{0};

        productVariations.forEach(productVariation -> {
            ProductVariationPojo productVariationPojo = new ProductVariationPojo();
            productVariationPojo.setId(productVariation.getId());
            productVariationPojo.setIs_active(productVariation.isIs_active());
            productVariationPojo.setMetadataHashmap(productVariation.getMetadataHashmap());
            productVariationPojo.setPrice(productVariation.getPrice());
            productVariationPojo.setPrimary_image(productVariation.getPrimary_image());
            productVariationPojo.setProduct_id(productVariation.getProduct().getId());
            productVariationPojo.setQuantity_available(productVariation.getQuantity_available());

            productVariationPojoLinkedHashMap.put("productVariation_"+count[0]++,productVariationPojo);
        });

        CommonResponseVO<LinkedHashMap> commonResponseVO = new CommonResponseVO<>(Arrays.asList(StatusCode.SUCCESS.toString()));
        commonResponseVO.setData(productVariationPojoLinkedHashMap);
        return commonResponseVO;
    }






    //------------------------------------------------------------------------------------------------------>
   @Transactional
   @Modifying
    public CommonResponseVO deleteProductBySeller(String token,int productId){
        String username = JwtUtility.findUsernameFromToken(token);
        Seller seller = sellerRepository.findByEmail(username);

        Product product = productRepository.findById(productId).get();
        if (product.getSeller_seller().getId()!=seller.getId()){
            throw new GiveMessageException(Arrays.asList(StatusCode.UNAUTHORIZED.toString())
                    ,Arrays.asList("Provided product id does not belongs to you ~"+username));
        }

        productRepository.delete(product);

        CommonResponseVO<LinkedHashMap> commonResponseVO = new CommonResponseVO<>(Arrays.asList(StatusCode.SUCCESS.toString()));
        return commonResponseVO;
    }






    //------------------------------------------------------------------------------------------------------>
    public CommonResponseVO updateProductBySeller
    (String token,int productId,String name,String description,Boolean is_cancelable,Boolean is_returnable)
    {
        String username = JwtUtility.findUsernameFromToken(token);
        Seller seller = sellerRepository.findByEmail(username);

        Product product = productRepository.findById(productId).get();
        if (product.getSeller_seller().getId()!=seller.getId()){
            throw new GiveMessageException(Arrays.asList(StatusCode.UNAUTHORIZED.toString())
                    ,Arrays.asList("Provided product id does not belongs to you ~"+username));
        }
        if (description!=null){
            product.setDescription(description);
        }
        if (is_cancelable!=null){
            product.setIs_cancellable(is_cancelable.booleanValue());
        }
        if (is_returnable!=null){
            product.setIs_returnable(is_returnable.booleanValue());
        }
        if (name!=null){

            List<Product> anyExistingProductOfSellerWithSameNameInSameCategory=
                    productRepository.findProductsWithSellerIdAndProductNameAndCategoryId
                            (seller.getId(),product.getName(),product.getCategory().getId());
            if (anyExistingProductOfSellerWithSameNameInSameCategory.isEmpty()==false){
                throw new GiveMessageException(Arrays.asList(StatusCode.UNAUTHORIZED.toString())
                        ,Arrays.asList("This product name is already taken under this category by seller= "+username));
            }
            product.setName(name);
        }
        productRepository.save(product);
            CommonResponseVO<LinkedHashMap> commonResponseVO =
                    new CommonResponseVO<>(Arrays.asList(StatusCode.SUCCESS.toString()));
        return commonResponseVO;
    }





    //--------------------------------------------------------------------------------------------------->
    public CommonResponseVO updateProductVariationBySeller
    (String token,int productVariationId,Integer quantity,Integer price,Boolean is_active,String primaryImage,LinkedHashMap<String, HashMap<String, String>> metadata)
    {
        String username = JwtUtility.findUsernameFromToken(token);
        Seller seller = sellerRepository.findByEmail(username);

        //fetched asked productVariation....so that it can be altered and saved back
        ProductVariation productVariation=productVariationRepository.findById(productVariationId).get();

        //if seller is not the owner of this product variation
        if (seller.getId()!=productVariation.getProduct().getSeller_seller().getId()){
            throw new GiveMessageException(Arrays.asList(StatusCode.UNAUTHORIZED.toString())
                    ,Arrays.asList("Provided product variation id does not belongs to you ~"+username));
        }
        if (quantity!=null){
            productVariation.setQuantity_available(quantity.intValue());
        }
        if (price!=null){
            productVariation.setPrice(price.intValue());
        }
        if (is_active!=null){
            productVariation.setIs_active(is_active.booleanValue());
        }
        if (primaryImage!=null){
            productVariation.setPrimary_image(primaryImage);
        }


        if (metadata.isEmpty()==false){

            Product product = productVariation.getProduct();

            List<String> categoryMetadataValuesFromProduct=new ArrayList<>();
            int[] lengthOfCategoryMDVfromProduct ;

            List<CategoryMetadataValue> categoryMetadataValuesByCategoryId = categoryMetadataValueRepository.findByCategoryId(product.getCategory().getId());
            if (categoryMetadataValuesByCategoryId.isEmpty()){
                throw new GiveMessageException(Arrays.asList("No fields are present for this category"));
            }

            //persistent entity hashmap cant be directly get and stored in variable.. so looped to get values
            HashMap<String, String> finalMetadata = new HashMap<>();
            productVariation.getMetadataHashmap().forEach((k,v)->{
                finalMetadata.put(k,v);
                categoryMetadataValuesFromProduct.add(k);
            });
            lengthOfCategoryMDVfromProduct =new int[] {finalMetadata.size()};


            int sizeOfMDVfromCategory=categoryMetadataValuesByCategoryId.size();
            int[] checkIfFoundForRemoveFields= new int[]{-1};
            int[] checkIfFoundForAddFields= new int[]{-1};

            List<String> finalCategoryMetadataValuesFromProduct = categoryMetadataValuesFromProduct;
            int[] finalLengthOfCategoryMDVfromProduct = lengthOfCategoryMDVfromProduct;


            metadata.forEach((k, v)-> {
                        if (Pattern.matches("addFields", k)) {

                            v.forEach((key2,value2)->{


                                //to check if already thr in metadata hashmap of producct variation
                                for (int i = 0; i < finalLengthOfCategoryMDVfromProduct[0]; i++) {
                                    if (key2.equals(finalCategoryMetadataValuesFromProduct.get(i))) {
                                        throw new GiveMessageException(Arrays.asList("Provided field already exist in metadata"));
                                    }
                                }
                                checkIfFoundForAddFields[0] = 0;
                                for (int i = 0; i < sizeOfMDVfromCategory; i++) {


                                    if (key2.equals(categoryMetadataValuesByCategoryId.get(i).getCategoryMetadataField().getCategoryKey())) {
                                        String[] valuesArr =categoryMetadataValuesByCategoryId.get(i).getCategoryValue().split(",");

                                              for (String val:valuesArr){
                                                  if (value2.equals(val)){
                                                      finalMetadata.put(key2,value2);
                                                      checkIfFoundForAddFields[0]=-1;
                                                      break;
                                                  }

                                              }

                                        continue;
                                    }
                                }
                                if (checkIfFoundForAddFields[0] == 0) {
                                    throw new GiveMessageException(Arrays.asList("Provided Field Does not belong to this category product"));
                                }
                            });

                        }

                        else if (Pattern.matches("removeFields", k)) {

                            v.forEach((key2,value2)->{
                                    checkIfFoundForRemoveFields[0]=0;
                                for (int i = 0; i < finalLengthOfCategoryMDVfromProduct[0]; i++) {

                                    if (key2.equals(finalCategoryMetadataValuesFromProduct.get(i))) {
                                        finalMetadata.remove(key2);
                                        checkIfFoundForRemoveFields[1] = -1;
                                    }
                                }


                                if (checkIfFoundForRemoveFields[0] == 0) {
                                    throw new GiveMessageException(Arrays.asList("Provided field to be removed does not exist in product variation metadata"));
                                }


                            });



                        }



                        else {
                            throw new GiveMessageException(Arrays.asList("To add and remove a field to product variation metadata,JSON key name should begin with addFields or removeFields"));
                        }
                    });

            productVariation.setMetadataHashmap(finalMetadata);
        }
        productVariationRepository.save(productVariation);

        CommonResponseVO commonResponseVO =
                new CommonResponseVO(Arrays.asList(StatusCode.SUCCESS.toString()));
        return commonResponseVO;

    }







    //---------------------------------------------------------------------------------------------------->
    public CommonResponseVO getProductByCustomer(int productId){
        Product product = productRepository.findById(productId).get();
        LinkedHashMap<String,LinkedHashMap> productFinalContainer = new LinkedHashMap<>();
        LinkedHashMap<String,ProductVariationPojo> productVariationContainer = new LinkedHashMap<>();
        int[] count=new int[]{0};
        if (product.getProductVariationlist().isEmpty()){
        productFinalContainer.put("productVariation",productVariationContainer);

      }else {
            List<ProductVariation> productVariations = product.getProductVariationlist();
            productVariations.forEach(productVariation -> {

                ProductVariationPojo productVariationPojo = new ProductVariationPojo();
                productVariationPojo.setId(productVariation.getId());
                productVariationPojo.setIs_active(productVariation.isIs_active());
                productVariationPojo.setMetadataHashmap(productVariation.getMetadataHashmap());
                productVariationPojo.setPrice(productVariation.getPrice());
                productVariationPojo.setPrimary_image(productVariation.getPrimary_image());
                productVariationPojo.setProduct_id(productVariation.getProduct().getId());
                productVariationPojo.setQuantity_available(productVariation.getQuantity_available());

                productVariationContainer.put("productVariation_"+count[0]++,productVariationPojo);
            });
        }

        LinkedHashMap<String,ProductPojo> productContainer = new LinkedHashMap<>();

        ProductPojo productPojo = new ProductPojo();
        productPojo.setName(product.getName());
        productPojo.setId(product.getId());
        productPojo.setBrand(product.getBrand());
        productPojo.setCategory_id(product.getCategory().getId());
        productPojo.setDescription(product.getDescription());
        productPojo.setIs_active(product.isIs_active());
        productPojo.setIs_cancellable(product.isIs_cancellable());
        productPojo.setIs_returnable(product.isIs_returnable());
        productPojo.setSeller_id(product.getSeller_seller().getId());
        productPojo.setCategory_name(product.getCategory().getName());

        productContainer.put("product",productPojo);

        productFinalContainer.put("product",productContainer);
        productFinalContainer.put("productVariations",productVariationContainer);


        CommonResponseVO<LinkedHashMap> commonResponseVO =
                new CommonResponseVO<>(Arrays.asList(StatusCode.SUCCESS.toString()));
        commonResponseVO.setData(productFinalContainer);
        return commonResponseVO;
    }






    //------------------------------------------------------------------------------------------------------>
    public CommonResponseVO getProductsByCustomer
    (Integer max,Integer offset,String order,String sort,String query)
    {
        int maxRecordsPerPage= productRepository.findTotalNumberOfProduct().intValue();
        int pageOffset=0;
        Sort.Direction direction= Sort.Direction.ASC;
        String property="id";


        if (offset!=null) {
            pageOffset = offset.intValue();
        }
        if (max!=null) {
            maxRecordsPerPage = max.intValue();
        }

        if (sort!=null) {
            if (sort.toLowerCase().equals("asc")) {
                direction = Sort.Direction.ASC;
            } else if (sort.toLowerCase().equals("desc")) {
                direction = Sort.Direction.DESC;
            } else {
                throw new GiveMessageException(Arrays.asList(StatusCode.FAILED.toString()), Arrays.asList("sorting order can be either asc or desc only"));
            }
        }

        if (query!=null){
            if (query.equals("name") || query.equals("categoryId") || query.equals("brand") || query.equals("id")){
            }else {
                throw new GiveMessageException(Arrays.asList(StatusCode.INVALID.toString()),Arrays.asList("Property name provided was invalid. ONLY (id,brand,categoryId,name) is eligible"));
            }
            property=query;
        }
        Pageable pageable = PageRequest.of(pageOffset,maxRecordsPerPage,direction,property);
        List<Product> products = productRepository.findAll(pageable);
        LinkedHashMap<String, ProductPojo> productPojoLinkedHashMap = new LinkedHashMap<>();
        int[] count = new int[]{0};
        if (products.isEmpty()){
            throw new GiveMessageException(Arrays.asList(StatusCode.NOT_AVAILABLE.toString()),Arrays.asList("No Product available"));

        }else {
            products.forEach(product -> {
                try {


                    if (product.getProductVariationlist().isEmpty()) {
                        throw new GiveMessageException(Arrays.asList(StatusCode.UNAUTHORIZED.toString()));
                    }
                    ProductPojo productPojo = new ProductPojo();
                    productPojo.setName(product.getName());
                    productPojo.setId(product.getId());
                    productPojo.setBrand(product.getBrand());
                    productPojo.setCategory_id(product.getCategory().getId());
                    productPojo.setDescription(product.getDescription());
                    productPojo.setIs_active(product.isIs_active());
                    productPojo.setIs_cancellable(product.isIs_cancellable());
                    productPojo.setIs_returnable(product.isIs_returnable());
                    productPojo.setSeller_id(product.getSeller_seller().getId());
                    productPojo.setCategory_name(product.getCategory().getName());

                    productPojoLinkedHashMap.put("product_" + count[0]++, productPojo);
                }
                catch (GiveMessageException e){
                    //do nothing..
                    //this enables me to pass this loop bcoz there is no continue and break in forEach
                }
            });

        }
        CommonResponseVO<LinkedHashMap> commonResponseVO =
                new CommonResponseVO<>(Arrays.asList(StatusCode.SUCCESS.toString()));
        commonResponseVO.setData(productPojoLinkedHashMap);
        return commonResponseVO;
    }





    //------------------------------------------------------------------------------------------------------->
    public CommonResponseVO getSimilarProductsByCustomer
    (int productId,Integer max,Integer offset,String order,String sort,String query)
    {
        LinkedHashMap<String, ProductPojo> productPojoLinkedHashMap =  new LinkedHashMap<>();
        int categoryId = productRepository.findById(productId).get().getCategory().getId();
        int maxRecordsPerPage= productRepository.findTotalNumberOfProductUnderCategory
                (categoryId).intValue();
        int pageOffset=0;
        Sort.Direction direction= Sort.Direction.ASC;
        String property="id";



        if (offset!=null) {
            pageOffset = offset.intValue();
        }
        if (max!=null) {
            maxRecordsPerPage = max.intValue();
        }

        if (sort!=null) {
            if (sort.toLowerCase().equals("asc")) {
                direction = Sort.Direction.ASC;
            } else if (sort.toLowerCase().equals("desc")) {
                direction = Sort.Direction.DESC;
            } else {
                throw new GiveMessageException(Arrays.asList(StatusCode.FAILED.toString()), Arrays.asList("sorting order can be either asc or desc only"));
            }
        }

        if (query!=null){
            if (query.equals("name") || query.equals("categoryId") || query.equals("brand") || query.equals("id")){
            }else {
                throw new GiveMessageException(Arrays.asList(StatusCode.INVALID.toString()),Arrays.asList("Property name provided was invalid. ONLY (id,brand,categoryId,name) is eligible"));
            }
            property=query;
        }
        Pageable pageable = PageRequest.of(pageOffset,maxRecordsPerPage,direction,property);
        List<Product> products = productRepository.findByCategoryId(categoryId,pageable);
        int count[] = new int[]{0};
        products.forEach(product -> {
            try {
                if (product.getProductVariationlist().isEmpty()){
                    throw new GiveMessageException(Arrays.asList(StatusCode.UNAUTHORIZED.toString()));
                }else {
                    ProductPojo productPojo = new ProductPojo();
                    productPojo.setName(product.getName());
                    productPojo.setId(product.getId());
                    productPojo.setBrand(product.getBrand());
                    productPojo.setCategory_id(product.getCategory().getId());
                    productPojo.setDescription(product.getDescription());
                    productPojo.setIs_active(product.isIs_active());
                    productPojo.setIs_cancellable(product.isIs_cancellable());
                    productPojo.setIs_returnable(product.isIs_returnable());
                    productPojo.setSeller_id(product.getSeller_seller().getId());
                    productPojo.setCategory_name(product.getCategory().getName());

                    productPojoLinkedHashMap.put("product_" + count[0]++, productPojo);
                }
            }
            catch (GiveMessageException e){}

        });
        CommonResponseVO<LinkedHashMap> commonResponseVO =
                new CommonResponseVO<>(Arrays.asList(StatusCode.SUCCESS.toString()));
        commonResponseVO.setData(productPojoLinkedHashMap);
        return commonResponseVO;
    }






    //--------------------------------------------------------------------------------------------------->
    public CommonResponseVO activateProductByAdmin(int productId){
            Product product = productRepository.findById(productId).get();
            if (product.isIs_active()==false){
                product.setIs_active(true);
            }
            else {
                throw new GiveMessageException(Arrays.asList(StatusCode.FAILED.toString()),
                        Arrays.asList("Product Already Active"));
            }
        CommonResponseVO<LinkedHashMap> commonResponseVO =
                new CommonResponseVO<>(Arrays.asList(StatusCode.PRODUCT_ACTIVATED.toString()));
        return commonResponseVO;
    }





    //--------------------------------------------------------------------------------------------------->
    public CommonResponseVO deactivateProductByAdmin(int productId){
        Product product = productRepository.findById(productId).get();
        if (product.isIs_active()==true){
            product.setIs_active(false);
        }
        else {
            throw new GiveMessageException(Arrays.asList(StatusCode.FAILED.toString()),
                    Arrays.asList("Product Already DeActive"));
        }
        CommonResponseVO<LinkedHashMap> commonResponseVO =
                new CommonResponseVO<>(Arrays.asList(StatusCode.PRODUCT_DEACTIVATED.toString()));
        return commonResponseVO;
    }






}
