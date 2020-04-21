package com.tothenew.bootcamp;

import com.tothenew.bootcamp.entity.ProductContent.*;
import com.tothenew.bootcamp.entity.User.Seller;
import com.tothenew.bootcamp.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

@SpringBootTest
public class CategoryTesting {
    @Autowired
    CategoryRespository categoryRespository;

    @Autowired
    SellerRepository sellerRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryMetadataRepository categoryMetadataRepository;

    @Autowired
    CategoryMetadataValueRepository categoryMetadataValueRepository;


@Test
    public void categoryPersistanceTest(){
    Category category = new Category();
    category.setName("upperWear");
    Optional<Category> category1 = categoryRespository.findById(8);
    category.setParentCategory(category1.get());
    categoryRespository.save(category);

}

@Test
    public void productAddition(){
    Product product= new Product();
    product.setBrand("J&J");
    product.setDescription("This is round neck t-shirt");
    product.setIs_cancellable(true);
    product.setIs_returnable(true);
    product.setName("cool tshirt");
    Category category = categoryRespository.findById(9).get();
    product.setCategory(category);
    Seller seller = sellerRepository.findById(54);
    product.setSeller_seller(seller);
    product.setIs_active(true);
    productRepository.save(product);
}





//not to store category this way
//@Test
//    public void categoryMetaData(){
//    CategoryMetadataField categoryMetadataField = new CategoryMetadataField();
//    //categoryMetadataField.setCategoryKey("asdfasf");
//
//    HashMap hashMap = new HashMap<String, String>();
//
//    hashMap.put("testkey1","testkeyvalue1");
//    categoryMetadataField.setCategoryMetadata(hashMap);
//    categoryMetadataRepository.save(categoryMetadataField);
//}

@Test
    public void product_Variation_meta(){
    Product product = new Product();
    product.setBrand("Zara");
    product.setDescription("This is V neck t-shirt");
    product.setIs_cancellable(true);
    product.setIs_returnable(true);
    product.setName("V tshirt black ");
    Category category = categoryRespository.findById(9).get();
    product.setCategory(category);
    Seller seller = sellerRepository.findById(54);
    product.setSeller_seller(seller);
    product.setIs_active(true);
    ProductVariation productVariation = new ProductVariation();
    HashMap metadata = new HashMap();
    metadata.put("material","poly");
    metadata.put("cut","apple");
    productVariation.setMetadataHashmap(metadata);
    product.setProductVariationlist(Arrays.asList(productVariation));
    productRepository.save(product);
}





@Test
    public void categoryMetadataField(){
    CategoryMetadataField categoryMetadataField = new CategoryMetadataField();
    categoryMetadataField.setCategoryKey("size");
    categoryMetadataRepository.save(categoryMetadataField);
}


    @Test
    public void categoryMetadataValues(){

        CategoryMetadataValue categoryMetadataValue = new CategoryMetadataValue();
        categoryMetadataValue.setCategoryMetadataField(categoryMetadataRepository.findById(5).get());
        categoryMetadataValue.setCategory(categoryRespository.findById(9).get());
        categoryMetadataValue.setCategoryValue("40,41,42,44,46");
        categoryMetadataValueRepository.save(categoryMetadataValue);
    }

}
