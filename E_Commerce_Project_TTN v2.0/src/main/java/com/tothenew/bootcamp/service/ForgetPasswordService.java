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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Arrays;
import java.util.Date;

@Service
public class ForgetPasswordService {

    @Autowired
    EmailServiceImpl emailService;
    @Autowired
    SellerService sellerService;
    @Autowired
    CustomerService customerService;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    TokenService tokenService;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void generateTokenForPasswordReset(String email){

        try {
            String role = roleRepository.findAuthorityWithEmail(email);
            if (role.equals(UsersType.CUSTOMER.toString())) {
                String jwt = JwtUtility.createJWT(10, StatusCode.PASSWORD_RESET.toString(), email, UsersType.CUSTOMER.toString());
                String resetPasswordLink = ServletUriComponentsBuilder.fromCurrentRequest().replacePath("user/reset-password/{token}").buildAndExpand(jwt).toUriString() +
                        "     Click the link and Enter your new password";
                tokenService.storeTokenInDB(jwt);
                emailService.sendSimpleMessage(email, "Reset Password Link", resetPasswordLink);
            }
            else if (role.equals(UsersType.SELLER.toString())) {
                String jwt = JwtUtility.createJWT(10, StatusCode.PASSWORD_RESET.toString(), email, UsersType.SELLER.toString());
                String resetPasswordLink = ServletUriComponentsBuilder.fromCurrentRequest().replacePath("user/reset-password/{token}").buildAndExpand(jwt).toUriString() +
                        "     Click the link and Enter your new password";
                tokenService.storeTokenInDB(jwt);
                emailService.sendSimpleMessage(email, "Reset Password Link", resetPasswordLink);
            }
            else if (role.equals(UsersType.ADMIN.toString())) {
                String jwt = JwtUtility.createJWT(10, StatusCode.PASSWORD_RESET.toString(), email, UsersType.ADMIN.toString());
                String resetPasswordLink = ServletUriComponentsBuilder.fromCurrentRequest().replacePath("user/reset-password/{token}").buildAndExpand(jwt).toUriString() +
                        "     Click the link and Enter your new password";
                tokenService.storeTokenInDB(jwt);
                emailService.sendSimpleMessage(email, "Reset Password Link", resetPasswordLink);
            }
        }
        catch (NullPointerException e){
            throw new UserNotRegisteredException();
        }

    }

    public void resetUserPassword(String token,String newPassword,String confirmPassword) {
            try {

        if (confirmPassword.equals(newPassword) == false) {
            throw new GiveMessageException(Arrays.asList(StatusCode.PASSWORD_MISMATCH.toString()));
        }
        tokenService.consumeTokenInDB(token);
        //verify token..if fails->return response of bad request
        String role = JwtUtility.whatIsRoleInToken(token);
        String email = JwtUtility.findUsernameFromToken(token);
        if (JwtUtility.verifyJwtToken(token, StatusCode.PASSWORD_RESET.toString(), email, role)) {
            if (role.equals(UsersType.CUSTOMER.toString())) {
                customerService.updateCustomerPassword(email, passwordEncoder.encode(newPassword));
                emailService.sendSimpleMessage(email,"Password Reset Alert","Your password has been recovered ~ DATED: "+new Date());

            } else if (role.equals(UsersType.SELLER.toString())) {
                sellerService.updateSellerPassword(email, passwordEncoder.encode(newPassword));
                emailService.sendSimpleMessage(email,"Password Reset Alert","Your password has been recovered ~ DATED: "+new Date());


            } else if (role.equals(UsersType.ADMIN.toString())) {
                //yet to be filled for admin
            }
        }
    }
            catch(NullPointerException e){
                throw  new GiveMessageException(Arrays.asList(StatusCode.FAILED.toString()));
            }

    }

}
