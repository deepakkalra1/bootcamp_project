package com.tothenew.bootcamp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tothenew.bootcamp.constants.JwtUtility;
import com.tothenew.bootcamp.pojo.CommonResponseVO;
import com.tothenew.bootcamp.pojo.user.AddressPojo;
import com.tothenew.bootcamp.pojo.user.UserCustomerPojo;
import com.tothenew.bootcamp.enums.StatusCode;
import com.tothenew.bootcamp.configurations.email.EmailServiceImpl;
import com.tothenew.bootcamp.service.userService.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;

@RestController
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @Autowired
    EmailServiceImpl emailService;


    //-------------------------------------------------------------------------------------------------------------->
    /***
     *
     * @param user_
     * @return Response Entity
     * @description=
     * Registration of a user along with his address and user_role
     *     user<- user_role , list< address >
     *    ->value returned by registerCustomer will tell whether it has been registered or not
     *         This is representation how data is supposed to be send to this api
     *         {
     * 	"first_name":"deepak",
     * 	"last_name":"kalra",
     * 	"addressList":[{"city":"rohini","state":"delhi"},{"city":"vikaspuri","state":"delhi"}],
     * 	"user_role":{
     * 		"role": {
     * 			"authority":"user"
     *                }}}
     */
    @PostMapping("/register/customer")
    public ResponseEntity CustomerRegistration(@RequestBody UserCustomerPojo user_)  {
        boolean newRegistered=false;
        newRegistered= customerService.registerCustomer(user_);
       if(newRegistered ){
           CommonResponseVO commonResponseVO = new CommonResponseVO(Arrays.asList(StatusCode.SUCCESS.toString()));

           return new ResponseEntity(commonResponseVO,HttpStatus.CREATED);
       }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }





    //------------------------------------------------------------------------------------------------------------->
    /*
     *@description API-> to view customer profile when he is loggedin
     * 1->fetching the username from the token using JwtUtility class
     * 2-> calling getCustomerProfile from customer service class
     */
    @GetMapping("/user/customer/profile")
    public ResponseEntity viewCustomerProfile(@RequestHeader(value = "Authorization")String tokenString )
            throws JsonProcessingException, IllegalAccessException, InstantiationException, InvocationTargetException
    {
        String token = tokenString.split(" ")[1];
        String username =    JwtUtility.findUsernameFromToken(token);
           CommonResponseVO commonResponseVO= customerService.getCustomerProfile(username);
            return new ResponseEntity(commonResponseVO,HttpStatus.OK);
    }





    //-------------------------------------------------------------------------------------------------------------->
    @GetMapping("/user/customer/address")
    public ResponseEntity viewCustomerAddressList(@RequestHeader(value = "Authorization") String tokenString){

        String token = tokenString.split(" ")[1];
        String username = JwtUtility.findUsernameFromToken(token);
        CommonResponseVO commonResponseVO = customerService.getCustomerAddresses(username);
        return new ResponseEntity(commonResponseVO,HttpStatus.OK);
    }




    @PutMapping("/user/customer/profile/update")
    public ResponseEntity customerProfileUpdation(@RequestBody HashMap hashMap,
                                                  @RequestHeader(value = "Authorization")String tokenString){
        String token = tokenString.split(" ")[1];
        String username = JwtUtility.findUsernameFromToken(token);
        customerService.updateCustomerProfile(username,hashMap);
        CommonResponseVO commonResponseVO = new CommonResponseVO(Arrays.asList(StatusCode.SUCCESS.toString()));
        return new ResponseEntity(commonResponseVO,HttpStatus.OK);

    }





    //---------------------------------------------------------------------------------------------------------->
    @PutMapping("/user/customer/password/update/{oldPassword}/{newPassword}/{confirmPassword}")
    public ResponseEntity customerProfileUpdation(@PathVariable(value = "oldPassword")String oldPassword,
                                                  @PathVariable(value = "newPassword")String newPassword,
                                                  @PathVariable(value = "confirmPassword")String confirmPassword,
                                                  @RequestHeader(value = "Authorization")String tokenString)
    {

        String token = tokenString.split(" ")[1];
        System.out.println(token);
        customerService.updateCustomerPasswordOnLogedIn(token,oldPassword,newPassword,confirmPassword);
        CommonResponseVO commonResponseVO = new CommonResponseVO(Arrays.asList(StatusCode.SUCCESS.toString()));
        return new ResponseEntity(commonResponseVO,HttpStatus.OK);
    }





    //---------------------------------------------------------------------------------------------------------->
    @PostMapping("/user/customer/add-address")
    public ResponseEntity customerAddAddress(@Valid @RequestBody AddressPojo addressPojo,
                                                  @RequestHeader(value = "Authorization")String tokenString)
    {
        String token = tokenString.split(" ")[1];
        customerService.addCustomerAddress(token,addressPojo);
        CommonResponseVO commonResponseVO = new CommonResponseVO(Arrays.asList(StatusCode.SUCCESS.toString()));
        return new ResponseEntity(commonResponseVO,HttpStatus.OK);
    }





    //----------------------------------------------------------------------------------------------------------->
    @DeleteMapping("/user/customer/delete-address/{addressId}")
    public ResponseEntity customerDeleteAddress(@PathVariable(name = "addressId")int addressId,
                                             @RequestHeader(value = "Authorization")String tokenString)
    {

        String token = tokenString.split(" ")[1];
        customerService.deleteCustomerAddressWithAddressID(token,addressId);
        CommonResponseVO commonResponseVO = new CommonResponseVO(Arrays.asList(StatusCode.SUCCESS.toString()));
        return new ResponseEntity(commonResponseVO,HttpStatus.OK);
    }





    //----------------------------------------------------------------------------------------------------------->
    @PutMapping("/user/customer/update-address")
    public ResponseEntity customerUpdateAddress(@RequestBody HashMap<String, String> addressHashmap,
                                             @RequestHeader(value = "Authorization")String tokenString)
    {

        String token = tokenString.split(" ")[1];
        customerService.updateCustomerAddress(token,addressHashmap);
        CommonResponseVO commonResponseVO = new CommonResponseVO(Arrays.asList(StatusCode.SUCCESS.toString()));
        return new ResponseEntity(commonResponseVO,HttpStatus.OK);
    }
}
