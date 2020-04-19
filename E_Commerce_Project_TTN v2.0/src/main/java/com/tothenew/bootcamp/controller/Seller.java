package com.tothenew.bootcamp.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.tothenew.bootcamp.constants.JwtUtility;
import com.tothenew.bootcamp.enums.StatusCode;
import com.tothenew.bootcamp.pojo.CommonResponseVO;
import com.tothenew.bootcamp.pojo.user.UserSellerPojo;
import com.tothenew.bootcamp.service.userService.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;

@RestController
public class Seller {

    @Autowired
    SellerService sellerService;

//------------------------------------------------------------------------------------------------------------->
    /***
     *
     * @param user
     * @return Response Entity
     * @description=
     * Registration of a seller along with his address,sellerBlock and user_role
     *     user<- user_role , list< address >,sellerBlock
     *     ->value returned by registered seller will tell whether it has been registered or not
     *
     *         This is representation how data is supposed to be send to this api
     *         {
     * 	"first_name":"deepak",
     * 	"last_name":"kalra",
     * 	"addressList":[{"city":"rohini","state":"delhi"},{"city":"vikaspuri","state":"delhi"}],
     * 	"sellerBlock_pojo":{
     * 	    "gst":"12345",
     * 	    "company_name":"ttn",
     * 	    "company_contact":"9818124789",
     * 	}
     * 	"user_role":{
     * 		"role": {
     * 			"authority":"user"
     *                }}}
     */
    @PostMapping("/register/seller")
    public ResponseEntity SellerRegistration(@RequestBody UserSellerPojo user) throws IOException {

        boolean registered=false;
        registered= sellerService.registerSeller(user);

        if(registered ){
            CommonResponseVO commonResponseVO = new CommonResponseVO(Arrays.asList(StatusCode.SUCCESS.toString()));

            return new ResponseEntity(commonResponseVO,HttpStatus.CREATED);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);

    }





    //------------------------------------------------------------------------------------------------------------->
    @GetMapping("/user/seller/profile")
    public ResponseEntity viewCustomerProfile(@RequestHeader(value = "Authorization")String tokenString )
            throws JsonProcessingException, IllegalAccessException, InstantiationException, InvocationTargetException
    {
        String token = tokenString.split(" ")[1];
        CommonResponseVO commonResponseVO= sellerService.getSellerProfile(token);
        return new ResponseEntity(commonResponseVO,HttpStatus.OK);
    }




    @PutMapping("/user/seller/profile/update")
    public ResponseEntity customerProfileUpdation(@RequestBody HashMap hashMap,
                                                  @RequestHeader(value = "Authorization")String tokenString){
        String token = tokenString.split(" ")[1];
        sellerService.updateSellerProfile(token,hashMap);
        CommonResponseVO commonResponseVO = new CommonResponseVO(Arrays.asList(StatusCode.SUCCESS.toString()));
        return new ResponseEntity(commonResponseVO,HttpStatus.OK);

    }






    //---------------------------------------------------------------------------------------------------------->
    @PutMapping("/user/seller/password/update/{oldPassword}/{newPassword}/{confirmPassword}")
    public ResponseEntity sellerPasswordUpdation(@PathVariable(value = "oldPassword")String oldPassword,
                                                  @PathVariable(value = "newPassword")String newPassword,
                                                  @PathVariable(value = "confirmPassword")String confirmPassword,
                                                  @RequestHeader(value = "Authorization")String tokenString)
    {

        String token = tokenString.split(" ")[1];
        System.out.println(token);
        sellerService.updateSellerPasswordOnLogedIn(token,oldPassword,newPassword,confirmPassword);
        CommonResponseVO commonResponseVO = new CommonResponseVO(Arrays.asList(StatusCode.SUCCESS.toString()));
        return new ResponseEntity(commonResponseVO,HttpStatus.OK);
    }







    //----------------------------------------------------------------------------------------------------------->
    @PutMapping("/user/seller/update-address")
    public ResponseEntity customerUpdateAddress(@RequestBody HashMap<String, String> addressHashmap,
                                                @RequestHeader(value = "Authorization")String tokenString)
    {

        String token = tokenString.split(" ")[1];
        sellerService.updateSellerAddress(token,addressHashmap);
        CommonResponseVO commonResponseVO = new CommonResponseVO(Arrays.asList(StatusCode.SUCCESS.toString()));
        return new ResponseEntity(commonResponseVO,HttpStatus.OK);
    }










}



