package com.tothenew.bootcamp.service;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.tothenew.bootcamp.constants.JwtUtility;
import com.tothenew.bootcamp.entity.ProductContent.Category;
import com.tothenew.bootcamp.entity.ProductContent.Product;
import com.tothenew.bootcamp.entity.ProductContent.ProductVariation;
import com.tothenew.bootcamp.entity.User.Seller;
import com.tothenew.bootcamp.enums.StatusCode;
import com.tothenew.bootcamp.exceptionHandling.GiveMessageException;
import com.tothenew.bootcamp.pojo.CommonResponseVO;
import com.tothenew.bootcamp.repositories.CategoryRespository;
import com.tothenew.bootcamp.repositories.ProductRepository;
import com.tothenew.bootcamp.repositories.ProductVariationRepository;
import com.tothenew.bootcamp.repositories.SellerRepository;
import javafx.scene.chart.CategoryAxis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

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



    //----------------------------------------------------------------------------------------------------------->
    public CommonResponseVO addProductBySeller(String token,String productName, int cayegoryId, String brandName,String description,
                                               Boolean is_cancelable, Boolean is_returnable){
        System.out.println("0");
        String username = JwtUtility.findUsernameFromToken(token);
        Seller seller = sellerRepository.findByEmail(username);
        System.out.println("1");
       List<Product> anyExistingProductOfSellerWithSameNameInSameCategory= productRepository.findProductsWithSellerIdAndProductNameAndCategoryId(seller.getId(),productName,cayegoryId);
        System.out.println("2");
       if (anyExistingProductOfSellerWithSameNameInSameCategory.isEmpty()){
           System.out.println("3");
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
    public void addProductVariationForProduct(String token,int productId,String primaryImage, LinkedHashMap<String, String>metadata
                                                                            ,Integer quantity,Integer price
    ){
        String username = JwtUtility.findUsernameFromToken(token);
        Seller seller = sellerRepository.findByEmail(username);
        ProductVariation productVariation = new ProductVariation();
        //since it returns optional.. so if not found.. will throw noSuchElementException
        Product product = productRepository.findById(productId).get();

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
        //putting all values
        productVariation.setMetadataHashmap(metadata);
        productVariation.setPrice(priceOfProductVariation);
        productVariation.setQuantity_available(quantityOfProductVariation);
        productVariation.setPrimary_image(primaryImage);
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
        CommonResponseVO<ProductVariation> commonResponseVO = new CommonResponseVO<>(Arrays.asList(StatusCode.SUCCESS.toString()));
        commonResponseVO.setData(productVariation);
        return commonResponseVO;
    }
}
