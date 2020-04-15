package com.tothenew.bootcamp.service.userService;

import com.tothenew.bootcamp.constants.JwtUtility;
import com.tothenew.bootcamp.entity.User.*;
import com.tothenew.bootcamp.entity.Role.SellerUserRole;
import com.tothenew.bootcamp.enums.UsersType;
import com.tothenew.bootcamp.exceptionHandling.UserAlreadyRegisteredException;
import com.tothenew.bootcamp.pojo.user.UserSellerPojo;
import com.tothenew.bootcamp.entity.Role.Role;
import com.tothenew.bootcamp.exceptionHandling.FieldMissingException;
import com.tothenew.bootcamp.repositories.RoleRepository;
import com.tothenew.bootcamp.repositories.SellerRepository;
import com.tothenew.bootcamp.configurations.email.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;

@Service
public class SellerService {

    @Autowired
    SellerRepository sellerRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    EmailServiceImpl emailService;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();



    //------------------------------------------------------------------------------------------------------------->
    /***
     *
     * @param
     * @return true
     * @description=
     * this api will receive json in this format to marshal
     * {
     *       	"first_name":"deepak",
     *       	"last_name":"kalra",
     *       	"email":"dee@g.com",
     *       	"addressList":[{"city":"rohini","state":"delhi"},{"city":"vikaspuri","state":"delhi"}],
     *       	"sellerBlock":{
     *       	    "gst":"12345",
     *       	    "company_name":"ttn",
     *       	    "company_contact":"9818124789"
     *                },
     *       	"user_role":{
     *       		"role": {
     *       			"authority":"user"
     *                      }
     *                      }
     *                      }
     */
    @Transactional
    public boolean registerSeller(UserSellerPojo userpojo){
        Seller seller =new Seller();
        try {
            seller.setFirst_name(userpojo.getFirst_name());
            seller.setLast_name(userpojo.getLast_name());
            seller.setContact(userpojo.getContact());
            seller.setEmail(userpojo.getEmail());
            seller.setPassword(userpojo.getPassword());
            seller.setImage(userpojo.getImage());
            seller.setMiddle_name(userpojo.getMiddle_name());
            seller.setAddressList(userpojo.getAddressListOfEntityAddress());
        }
        catch (NullPointerException e){}
        /*
        everything except email can be tolerated blank till it is addedd into database
        but if email is blank.. then exception will be raised
         */
        try {
            if (seller.getEmail()==null){
                throw new FieldMissingException();
            }
            verifySellerAlreadyExist(seller.getEmail());
        }
        catch (NullPointerException e){}
        SellerUserRole user_role = new SellerUserRole();
        Role role = roleRepository.findByAuthority(UsersType.SELLER.toString());

        //user_role <- role
        user_role.setRole(role);


        try {
            String pass = seller.getPassword();
            seller.setPassword(passwordEncoder.encode(pass));


            SellerOrganizationDetails sellerOrganizationDetails = userpojo.getSellerBlockOfEntity();


            seller.setSellerOrganizationDetails(sellerOrganizationDetails);
            sellerOrganizationDetails.setSeller(seller);

        } catch (NullPointerException ex) {

            throw new FieldMissingException();

        }

        //seller <- seller role
        seller.setSellerUserRole(user_role);
        user_role.setSeller(seller);

        sellerRepository.save(seller);

        String jwt= JwtUtility.createJWT(5,"activation", seller.getEmail(),UsersType.SELLER.toString());
        String userActivationLink = ServletUriComponentsBuilder.fromCurrentRequest().replacePath("seller/activation?token={jwt}&uemail={uemail}").buildAndExpand(jwt, seller.getEmail()).toUriString();
        emailService.sendSimpleMessage(seller.getEmail(),"Activation Link",userActivationLink);

        return true;
    }




//--------------------------------------------------------------------------------------------------------------->
    /***
     *
     * @param email
     * @return boolean
     * @description
     * ->receives the email
     * ->find user against it using the userByEmail method of customerService.class
     * ->if user exist checked by callings its get first name method .. then raised UserAlreadyRegisteredException
     * ->else return false that user dont exist in database;
     */

    public boolean verifySellerAlreadyExist(String email){

        Seller seller =sellerRepository.findByEmail(email);
        try {
            seller.getFirst_name();
            throw new UserAlreadyRegisteredException();

        }
        catch (NullPointerException e){
            return false;
        }

    }

    //---------------------------------------------------------------------------------------------------------------->
    /***
     *
     * @param token
     * @param email
     * @return boolean
     * @description
     * activating user
     * calling verify Token method of JwtUtility class
     */
    @Transactional
    public boolean activateSeller(String token,String email){
        boolean resultJWTverification=    JwtUtility.verifyJwtToken(token,"activation",email,UsersType.SELLER.toString());
        if (resultJWTverification){
            sellerRepository.setSellerActiveStatus(1,email);
            return true;
        }

        return false;
    }





    //--------------------------------------------------------------------------------------------------------------->
    @Transactional
    public boolean updateSellerPassword(String email,String newPass){
        sellerRepository.setSellerPassword(passwordEncoder.encode( newPass),email);
        return true;
    }



}
