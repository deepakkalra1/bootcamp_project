package com.tothenew.bootcamp.controller;

import com.tothenew.bootcamp.enums.StatusCode;
import com.tothenew.bootcamp.pojo.CommonResponseVO;
import com.tothenew.bootcamp.service.ForgetPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
public class ForgotPassword {
    @Autowired
    ForgetPasswordService forgetPasswordService;

    //--------------------------------------------------------------------------------------------------------------->
    /***
     * @param email
     * @return
     * @description
     * if isActive=false - > return first activate ur acc
     * if active -> send link having token for reset password in header and username also and link->/user/reset-password/token/email;
     * link = user/reset-password/(token)&(username)/(old password)/(new password) @PathVariable
     */

    @PostMapping("/user/reset-password/token-generator/{email}")
    public ResponseEntity resetPasswordUsersTokenGen(@PathVariable() String email) {
        forgetPasswordService.generateTokenForPasswordReset(email);
        return new ResponseEntity(new CommonResponseVO(
                Arrays.asList(StatusCode.LINK_GENERATED.toString())
        ), HttpStatus.OK);
    }





    //---------------------------------------------------------------------------------------------------------------->
    @PutMapping("user/reset-password/{token}/{newPassword}/{confirmPassword}")
    public ResponseEntity resetPassUser(@PathVariable(value = "token")String token,@PathVariable(value = "confirmPassword")String confirmPassword
                                        ,@PathVariable(value = "newPassword")String newPassword){

        forgetPasswordService.resetUserPassword(token,newPassword,confirmPassword);
        return new ResponseEntity(new CommonResponseVO(Arrays.asList(StatusCode.SUCCESS.toString())),HttpStatus.OK);
    }





    //---------------------------------------------------------------------------------------------------------------->

}
