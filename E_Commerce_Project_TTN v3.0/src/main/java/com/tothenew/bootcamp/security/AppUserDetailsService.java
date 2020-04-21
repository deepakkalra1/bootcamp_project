/***
 * not being used...
 * bcoz my custom authenticationProvider
 * instead of this automated
 */







package com.tothenew.bootcamp.security;

import com.tothenew.bootcamp.constants.JwtUtility;
import com.tothenew.bootcamp.entity.User.Admin;
import com.tothenew.bootcamp.entity.User.Customer;
import com.tothenew.bootcamp.entity.User.Seller;
import com.tothenew.bootcamp.enums.StatusCode;
import com.tothenew.bootcamp.enums.UsersType;
import com.tothenew.bootcamp.exceptionHandling.UserNotRegisteredException;
import com.tothenew.bootcamp.repositories.*;
import com.tothenew.bootcamp.configurations.email.EmailServiceImpl;
import com.tothenew.bootcamp.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Arrays;

@Service
public class AppUserDetailsService implements UserDetailsService {

    @Autowired
    AdminRepository adminRepository;
    @Autowired
    EmailServiceImpl emailService;

    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    SellerRepository sellerRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    TokenService tokenService;


    //--------------------------------------------------------------------------------------------------------------->
    /***
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     * @description
     * search user with provided username
     * if exist and is active... then spring security verifies it
     * bcoz we return that user putting in UserDetail implemented class=appUser
     */
    @Override
    public  UserDetails  loadUserByUsername(String username) throws UsernameNotFoundException {

        AppUser appUser;
        try {
            //does not matter whether it is customer or seller..just supposed to have its username,pass,role

            String role = roleRepository.findAuthorityWithEmail(username);

            if (role.equals(UsersType.CUSTOMER.toString())){
                Customer usersCommon = customerRepository.findByEmail(username);

                            if (!usersCommon.isIs_active() ){

                String jwt= JwtUtility.createJWT(5,"activation",usersCommon.getEmail(),role);
                String userActivationLink = ServletUriComponentsBuilder.fromCurrentRequest().replacePath("user/activation?token={jwt}").buildAndExpand(jwt).toUriString();
                emailService.sendSimpleMessage(usersCommon.getEmail(),"Activation Link",userActivationLink);
                tokenService.storeTokenInDB(jwt);
                throw new RuntimeException(StatusCode.ACCOUNT_DEACTIVATED.toString());
            }
            else if (usersCommon.isIs_deleted()){
                throw new RuntimeException(StatusCode.ACCOUNT_DELETED.toString());
            }
            appUser = new AppUser(usersCommon.getEmail(), usersCommon.getPassword(), Arrays.asList(new GrantAuthorityImpl("ROLE_" + usersCommon.getCustomerUserRole().getRole().getAuthority())));

                return appUser;


            }









            else  if (role.equals(UsersType.SELLER.toString())){

                Seller usersCommon = sellerRepository.findByEmail(username);

                if (!usersCommon.isIs_active() ){

                    String jwt= JwtUtility.createJWT(5,"activation",usersCommon.getEmail(),role);
                    String userActivationLink = ServletUriComponentsBuilder.fromCurrentRequest().replacePath("user/activation?token={jwt}").buildAndExpand(jwt).toUriString();
                    emailService.sendSimpleMessage(usersCommon.getEmail(),"Activation Link",userActivationLink);
                    tokenService.storeTokenInDB(jwt);
                    throw new RuntimeException(StatusCode.ACCOUNT_DEACTIVATED.toString());
                }
                else if (usersCommon.isIs_deleted()){
                    throw new RuntimeException(StatusCode.ACCOUNT_DELETED.toString());
                }
                appUser = new AppUser(usersCommon.getEmail(), usersCommon.getPassword(), Arrays.asList(new GrantAuthorityImpl("ROLE_" + usersCommon.getSellerUserRole().getRole().getAuthority())));

                return appUser;
            }








            else  if (role.equals(UsersType.ADMIN.toString())){


                Admin usersCommon = adminRepository.findByEmail(username);

                if (!usersCommon.isIs_active() ){

                    String jwt= JwtUtility.createJWT(5,"activation",usersCommon.getEmail(),role);
                    String userActivationLink = ServletUriComponentsBuilder.fromCurrentRequest().replacePath("user/activation?token={jwt}").buildAndExpand(jwt).toUriString();
                    emailService.sendSimpleMessage(usersCommon.getEmail(),"Activation Link",userActivationLink);
                    tokenService.storeTokenInDB(jwt);
                    throw new RuntimeException(StatusCode.ACCOUNT_DEACTIVATED.toString());
                }
                else if (usersCommon.isIs_deleted()){
                    throw new RuntimeException(StatusCode.ACCOUNT_DELETED.toString());
                }
                appUser = new AppUser(usersCommon.getEmail(), usersCommon.getPassword(), Arrays.asList(new GrantAuthorityImpl("ROLE_" + usersCommon.getAdminUserRole().getRole().getAuthority())));

                return appUser;

            }
            }
        catch (NullPointerException e){ throw new UserNotRegisteredException(); }
        throw new RuntimeException(StatusCode.NOT_REGISTERED.toString());

    }




    //---------------------------------------------------------------------------------------------------------------->
}


