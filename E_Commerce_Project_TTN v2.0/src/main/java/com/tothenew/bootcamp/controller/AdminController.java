package com.tothenew.bootcamp.controller;

import com.tothenew.bootcamp.enums.StatusCode;
import com.tothenew.bootcamp.pojo.CommonResponseVO;
import com.tothenew.bootcamp.repositories.RoleRepository;
import com.tothenew.bootcamp.service.userService.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
public class AdminController {

    @Autowired
    AdminService adminService;

    @Autowired
    RoleRepository roleRepository;



    //---------------------------------------------------------------------------------------------------------------->
    @GetMapping("/user/admin/view-all-customers")
//    @GetMapping("/admin/customers")
    public ResponseEntity allCustomersView(){
        CommonResponseVO commonResponseVO = adminService.findAllCustomers();
        return new ResponseEntity(commonResponseVO, HttpStatus.OK);
    }





    //---------------------------------------------------------------------------------------------------------------->
    @GetMapping("/user/admin/view-all-sellers")
    public ResponseEntity allSellersView(){
        CommonResponseVO commonResponseVO = adminService.findAllSellers();
        return new ResponseEntity(commonResponseVO, HttpStatus.OK);
    }





    //----------------------------------------------------------------------------------------------------------------->
    /**
     *
     * @param email
     * @return
     * @Description
     * 1->whether email id exist and is not deleted and not and if it is of customer
     * 2-> if already activated
     * 3->if not activated, THEN activate id  AND send user email of activation
     */
    @PutMapping("/user/admin/activate-customer/{email}")
    public ResponseEntity activateCustomer(@PathVariable(value = "email") String email){
                adminService.activateCustomer(email);
                return new ResponseEntity(new CommonResponseVO(Arrays.asList(StatusCode.SUCCESS.toString())), HttpStatus.OK);
    }





    //--------------------------------------------------------------------------------------------------------------->
    @PutMapping("/user/admin/activate-seller/{email}")
    public ResponseEntity activateSeller(@PathVariable(value = "email") String email){
        adminService.activateSeller(email);
        return new ResponseEntity(new CommonResponseVO(Arrays.asList(StatusCode.SUCCESS.toString())), HttpStatus.OK);
    }





    //---------------------------------------------------------------------------------------------------------------->
    @PutMapping("/user/admin/deactivate-customer/{email}")
    public ResponseEntity deactivateCustomer(@PathVariable(value = "email") String email){
        adminService.de_activateCustomer(email);
        return new ResponseEntity(new CommonResponseVO(Arrays.asList(StatusCode.SUCCESS.toString())), HttpStatus.OK);
    }





    //---------------------------------------------------------------------------------------------------------------->
    @PutMapping("/user/admin/deactivate-seller/{email}")
    public ResponseEntity deactivateSeller(@PathVariable(value = "email") String email){
        adminService.de_activateSeller(email);
        return new ResponseEntity(new CommonResponseVO(Arrays.asList(StatusCode.SUCCESS.toString())), HttpStatus.OK);
    }
}
