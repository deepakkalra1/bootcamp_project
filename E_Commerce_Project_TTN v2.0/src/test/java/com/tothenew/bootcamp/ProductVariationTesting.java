package com.tothenew.bootcamp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tothenew.bootcamp.entity.ProductContent.ProductVariation;
import com.tothenew.bootcamp.repositories.ProductVariationRepository;
import com.tothenew.bootcamp.configurations.email.EmailServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import io.jsonwebtoken.*;

@SpringBootTest
public class ProductVariationTesting {
    @Autowired
    ProductVariationRepository productVariationRepository;

    //--------------------------------------------------------------------------------------------------------------->

    /***
     *
     * @throws JsonProcessingException
     * @desciption
     *  before storing the object in database
     *  we have to serialize the hashmap to json string
     *  in order to store data in database
     *  using jsonMetadataStringSerialize
     *  */
    @Test
    public void metadataTesting() throws JsonProcessingException {
    ProductVariation productVariation = new ProductVariation();
    productVariation.setId(2);
    productVariation.setPrice(1000);
    productVariation.setQuantity_available(2);
    HashMap<String,String> hashMap = new HashMap<>();
    hashMap.put("color","blue");
    hashMap.put("brand","levis");

    productVariation.setMetadataHashmap(hashMap);
    productVariation.jsonMetadataStringSerialize();

    productVariationRepository.save(productVariation);


}





//--------------------------------------------------------------------------------------------------------------->
    /***
     * before using json string value.. as a json
     * we have to deserialize it using jsonMetadataStringDeserialize
     * which deserialize and stores the data in hashmap
     */
    @Test
    public  void fetchingProductVariation(){
    Optional<ProductVariation> productVariation= productVariationRepository.findById(1);
    productVariation.ifPresent(pro->{
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            pro.jsonMetadataStringDeserialize();
        } catch (JsonProcessingException e) {

        }
        System.out.println( pro.getMetadata() );
        System.out.println(pro.getMetadataHashmap().get("brand"));
    });
}




    //--------------------------------------------------------------------------------------------------------------->

    /***
     * generic method testing
     *
     */
    public <T> void genericTesting(T t){
        System.out.println(t);

    }

    @Test
    public void testingGenericMethod(){
        //genericTesting(10);
    }


    @Test
    public void email2(){
        EmailServiceImpl emailService = new EmailServiceImpl();
        emailService.sendSimpleMessage("deepak.kalra1@tothenew.com","test","testing bro");

    }

    public static String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);

        }
        return hexString.toString();
    }





    //-------------------------------------------------------------------------------------------------------->
    /*
    converting string to sha-256 hash code
     */
    @Test
    public void hashing() throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        String message="hello";

        byte[] hashed=
                md.digest(message.getBytes());
        System.out.println(bytesToHex(hashed));
    }

    /*
    working
    creation of jwt token
    and then fetching of json web signature which can contain any key value pair as a claim
    JWT is made of -> header, Payload, Signature
    payload and signature can be used interchangebely
     */
    @Test
    public void jwtToken() throws JsonProcessingException {

        String token=Jwts.builder()
                .setSubject("testing")

                .claim("name","deepak")
                .signWith(SignatureAlgorithm.HS256,"4789")
                .compact();

        System.out.println(token);
        //this will give me entire body of token containing name and subject
        //Object claims= Jwts.parser().setSigningKey("4789").parse(token).getBody();

        //jws is jason web signature
        Jws<Claims> jws= Jwts.parser().setSigningKey("4789")

                .parseClaimsJws(token);

       String name= (String) jws.getBody().get("name");
        System.out.println(name);




    }


    //wrong way... hashmap cant be stored as json in db
    @Test
    public void jsonInsertInVariation(){
        ProductVariation productVariation = new ProductVariation();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("key","value");
        productVariation.setPrice(10000);
        productVariation.setMetadataHashmap(hashMap);
        productVariationRepository.save(productVariation);
    }





}
