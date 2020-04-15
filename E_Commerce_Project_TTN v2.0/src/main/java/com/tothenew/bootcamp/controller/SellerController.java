package com.tothenew.bootcamp.controller;


import com.tothenew.bootcamp.enums.StatusCode;
import com.tothenew.bootcamp.pojo.CommonResponseVO;
import com.tothenew.bootcamp.pojo.user.UserSellerPojo;
import com.tothenew.bootcamp.service.userService.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
public class SellerController {

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
    public ResponseEntity SellerRegistration(@RequestBody UserSellerPojo user) {

        boolean registered=false;
        registered= sellerService.registerSeller(user);

        if(registered ){
            CommonResponseVO commonResponseVO = new CommonResponseVO(Arrays.asList(StatusCode.SUCCESS.toString()));

            return new ResponseEntity(commonResponseVO,HttpStatus.CREATED);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);

    }





    //------------------------------------------------------------------------------------------------------------->

    }



