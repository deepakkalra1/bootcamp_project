package com.tothenew.bootcamp.service;

import com.tothenew.bootcamp.constants.JwtUtility;
import com.tothenew.bootcamp.enums.StatusCode;
import com.tothenew.bootcamp.enums.UsersType;
import com.tothenew.bootcamp.exceptionHandling.GiveMessageException;
import com.tothenew.bootcamp.exceptionHandling.UserNotRegisteredException;
import com.tothenew.bootcamp.repositories.RoleRepository;
import com.tothenew.bootcamp.configurations.email.EmailServiceImpl;
import com.tothenew.bootcamp.service.userService.CustomerService;
import com.tothenew.bootcamp.service.userService.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Arrays;

@Service
public class ActivationService {
    @Autowired
    CustomerService customerService;

    @Autowired
    SellerService sellerService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    EmailServiceImpl emailService;

    @Autowired
    TokenService tokenService;




    //------------------------------------------------------------------------------------------------------------------->
    public void userActivation(String token) {
        try{
        tokenService.consumeTokenInDB(token);

        String email = JwtUtility.findUsernameFromToken(token);
            String role = JwtUtility.whatIsRoleInToken(token);
            JwtUtility.verifyJwtToken(token,StatusCode.ACTIVATION.toString(),email,role);

        if (role.equals(UsersType.CUSTOMER.toString())) {
            boolean resultStatus = customerService.activateCustomer(token, email);
            if (resultStatus == false) {
                String jwt = JwtUtility.createJWT(5, StatusCode.ACTIVATION.toString(), email, UsersType.CUSTOMER.toString());
                String userActivationLink = ServletUriComponentsBuilder.fromCurrentRequest().replacePath("user/activation?token={jwt}").buildAndExpand(jwt).toUriString();
                emailService.sendSimpleMessage(email, "Activation Link", userActivationLink);
                throw new GiveMessageException(Arrays.asList(StatusCode.FAILED.toString()));
            }
        }
        // if the role is seller
        else if (role.equals(UsersType.SELLER)) {

            boolean resultStatus = sellerService.activateSeller(token, email);
            if (resultStatus==false) {
                String jwt = JwtUtility.createJWT(5, StatusCode.ACTIVATION.toString(), email, UsersType.SELLER.toString());
                String userActivationLink = ServletUriComponentsBuilder.fromCurrentRequest().replacePath("user/activation?token={jwt}").buildAndExpand(jwt).toUriString();
                emailService.sendSimpleMessage(email, "Activation Link", userActivationLink);
                throw new GiveMessageException(Arrays.asList(StatusCode.FAILED.toString()));
            }
        }
        // is user is admin
        else if (role.equals(UsersType.ADMIN)) {


        }
    }catch (NullPointerException e){
            throw new GiveMessageException(Arrays.asList(StatusCode.TOKEN_INVALID.toString()));
        }

    }






    //----------------------------------------------------------------------------------------------------------------->
    public void resendActivationCodeOnEmail(String email){
        try {
            String role = roleRepository.findAuthorityWithEmail(email);
            if (role.equals(UsersType.CUSTOMER.toString())) {
                String jwt = JwtUtility.createJWT(5, StatusCode.ACTIVATION.toString(), email, UsersType.CUSTOMER.toString());
                String userActivationLink = ServletUriComponentsBuilder.fromCurrentRequest().replacePath("user/activation?token={jwt}").buildAndExpand(jwt).toUriString();
                emailService.sendSimpleMessage(email, "Activation Link", userActivationLink);

            } else if (role.equals(UsersType.SELLER.toString())) {
                String jwt = JwtUtility.createJWT(5, StatusCode.ACTIVATION.toString(), email, UsersType.SELLER.toString());
                String userActivationLink = ServletUriComponentsBuilder.fromCurrentRequest().replacePath("user/activation?token={jwt}").buildAndExpand(jwt).toUriString();
                emailService.sendSimpleMessage(email, "Activation Link", userActivationLink);

            } else if (role.equals(UsersType.ADMIN.toString())) {

                String jwt = JwtUtility.createJWT(5, StatusCode.ACTIVATION.toString(), email, UsersType.ADMIN.toString());
                String userActivationLink = ServletUriComponentsBuilder.fromCurrentRequest().replacePath("user/activation?token={jwt}").buildAndExpand(jwt).toUriString();
                emailService.sendSimpleMessage(email, "Activation Link", userActivationLink);
            }

        }
        catch (NullPointerException e){
            throw new UserNotRegisteredException();
        }

    }
}
