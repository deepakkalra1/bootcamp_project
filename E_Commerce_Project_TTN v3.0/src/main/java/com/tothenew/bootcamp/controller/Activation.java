package com.tothenew.bootcamp.controller;

import com.tothenew.bootcamp.enums.StatusCode;
import com.tothenew.bootcamp.pojo.CommonResponseVO;
import com.tothenew.bootcamp.pojo.user.UserCustomerPojo;
import com.tothenew.bootcamp.service.ActivationService;
import com.tothenew.bootcamp.configurations.email.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

@RestController
public class Activation {
    @Autowired
    TokenStore tokenStore;

    @Autowired
    ActivationService activationService;

    @Autowired
    EmailServiceImpl emailService;
    //--------------------------------------------------------------------------------------------------------------->


    @GetMapping("/")
    public String homePage() {
        return "Welcome to deepak kalra's E-Commerce application"; }





    //---------------------------------------------------------------------------------------------------------------->
    /***
     * @param token
     * @param
     * @return
     * @desciption
     * will receive token and username.. then find role from token wtih utility method
     * and having knowing ROLE-> process is done accordingly
     * if token date is above than current... username is equal to which is provided in token...AND subject matches
     * user will be activated in db
     * else if any problm occur.. unauthorized access
     */
    @GetMapping("/user/activation")
    public ResponseEntity userActivation(@RequestParam(value = "token") String token ){
        activationService.userActivation(token);
        return new ResponseEntity
                        (
                                new CommonResponseVO(Arrays.asList(StatusCode.ACCOUNT_ACTIVATED.toString())),
                                HttpStatus.OK
                        );

    }





    //---------------------------------------------------------------------------------------------------------------->
    @GetMapping("/doLogout")
    public String logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null) {
            String tokenValue = authHeader.replace("Bearer", "").trim();
            OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
            tokenStore.removeAccessToken(accessToken);
        }
        return "{'status':" + StatusCode.SUCCESS + "}";
    }





    //----------------------------------------------------------------------------------------------------------------->

    /***
     * @param email
     * @return
     * @desciption
     * resending the activation code.. if token is expired
     * 1->fetching role from email using roleRepository
     * 2-> acording to role.. futher process is decided
     * 3->if email is valid and exist.. then go ahead ELSE UserDoesNotExistException
     * 4-> if registered then send token on it mail
     */
    @PostMapping("/user/activation/resend/{email}")
    public ResponseEntity resendActivationLink(@PathVariable(value = "email")String email ) {
            activationService.resendActivationCodeOnEmail(email);
        return new ResponseEntity
                (
                        new CommonResponseVO(Arrays.asList(StatusCode.LINK_GENERATED.toString())),
                        HttpStatus.OK
                );
    }





    //--------------------------------------------------------------------------------------------------------------->
}
