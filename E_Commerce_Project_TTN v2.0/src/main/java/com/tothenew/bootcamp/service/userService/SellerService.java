package com.tothenew.bootcamp.service.userService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tothenew.bootcamp.constants.JwtUtility;
import com.tothenew.bootcamp.constants.RegularExp;
import com.tothenew.bootcamp.entity.User.*;
import com.tothenew.bootcamp.entity.Role.SellerUserRole;
import com.tothenew.bootcamp.enums.StatusCode;
import com.tothenew.bootcamp.enums.UsersType;
import com.tothenew.bootcamp.exceptionHandling.GiveMessageException;
import com.tothenew.bootcamp.exceptionHandling.UserAlreadyRegisteredException;
import com.tothenew.bootcamp.pojo.CommonResponseVO;
import com.tothenew.bootcamp.pojo.user.UserSellerPojo;
import com.tothenew.bootcamp.entity.Role.Role;
import com.tothenew.bootcamp.exceptionHandling.FieldMissingException;
import com.tothenew.bootcamp.repositories.AddressRepository;
import com.tothenew.bootcamp.repositories.RoleRepository;
import com.tothenew.bootcamp.repositories.SellerRepository;
import com.tothenew.bootcamp.configurations.email.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class SellerService {

    @Autowired
    SellerRepository sellerRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    EmailServiceImpl emailService;

    @Autowired
    AddressRepository addressRepository;

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
//            if (userpojo.getAddressListOfEntityAddress().size()>1){
//                throw new GiveMessageException(Arrays.asList(StatusCode.FAILED.toString())
//                ,Arrays.asList("Seller can have only ONE address for its organization")
//                );
//            }
            userpojo.getAddressListOfEntityAddress().forEach(address -> {
                address.setOrg_address(true);
            });

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





    //------------------------------------------------------------------------------------------------------------>
    /***
     *
     * @param token
     * @return
     * @throws JsonProcessingException
     * 1->put basic detail of seller and org detail
     * 2->seller can have multiple address of its org... on behalf of orgAddress field in db.. which tell whether this address
     * is of seller org or not..
     * if it is so.. then add it into profile and return
     */
    public CommonResponseVO<HashMap> getSellerProfile(String token) throws JsonProcessingException {
        String username=JwtUtility.findUsernameFromToken(token);
        Seller seller = sellerRepository.findByEmail(username);
        HashMap userProfile = new HashMap();
        userProfile.put("firstName", seller.getFirst_name());
        userProfile.put("middleName", seller.getMiddle_name());
        userProfile.put("lastName", seller.getLast_name());
        userProfile.put("email", seller.getEmail());
        userProfile.put("image", seller.getImage());
        userProfile.put("gstNumber",seller.getSellerOrganizationDetails().getGst());
        userProfile.put("companyName",seller.getSellerOrganizationDetails().getCompany_name());
        userProfile.put("companyContact",seller.getSellerOrganizationDetails().getCompany_contact());
        int[] addressCount = new int[]{0};
        seller.getAddressList().forEach(address -> {
            if (address.isOrg_address()==true){
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("addressLine",address.getAddress_line());
                hashMap.put("city",address.getCity());
                hashMap.put("state",address.getState());
                hashMap.put("zipCode",String.valueOf( address.getZip_code()));
                hashMap.put("country",address.getCountry());
               userProfile.put("address_"+addressCount[0],hashMap);
               addressCount[0]++;
            }
        });
        CommonResponseVO<HashMap> commonResponseVO = new CommonResponseVO<>();
        commonResponseVO.setData(userProfile);
        return commonResponseVO;
    }







    //---------------------------------------------------------------------------------------------------------------->
    /**
     *eg-
     * {
     * 	"companyName":"onTweak",
     * 	"companyContact":"9818124789",
     * 	"gst":"123456789"
     * }
     */
    @Transactional
    public void updateSellerProfile(String token, HashMap newData) {
        String username = JwtUtility.findUsernameFromToken(token);
        Seller seller = sellerRepository.findByEmail(username);
        Set<Map.Entry<String, String>> entrySet= newData.entrySet();
        SellerOrganizationDetails sellerOrganizationDetails = seller.getSellerOrganizationDetails();
        for (Map.Entry<String, String> entry:entrySet){
            if (entry.getKey().equals("firstName")){
                seller.setFirst_name(entry.getValue());
            }
            else if (entry.getKey().equals("middleName")){
                seller.setMiddle_name(entry.getValue());
            }
            else if (entry.getKey().equals("lastName")){
                seller.setLast_name(entry.getValue());
            }
            else if (entry.getKey().equals("companyContact")){
                sellerOrganizationDetails.setCompany_contact(entry.getValue());
                seller.setSellerOrganizationDetails(seller.getSellerOrganizationDetails());
            }
            else if (entry.getKey().equals("companyName")){
                sellerOrganizationDetails.setCompany_name(entry.getValue());
                seller.setSellerOrganizationDetails(sellerOrganizationDetails);
            }
            else if (entry.getKey().equals("gst")){
                sellerOrganizationDetails.setGst(entry.getValue());
                seller.setSellerOrganizationDetails(sellerOrganizationDetails);
            }
//            else if (RegularExp.verifyIfAddressIsAskedForUpdationInSellerProfileUpdate(entry.getKey())){
//                 entry.getValue();
//                seller.getAddressList().forEach(address -> {
//                    if (address.isOrg_address()){
//                        if (address.getId()==Integer.parseInt())
//                    }
//                });
//            }
            else {
                throw new GiveMessageException(Arrays.asList( StatusCode.NOT_VALID_FORMAT.toString()));
            }
        }
        sellerRepository.save(seller);
    }




    //---------------------------------------------------------------------------------------------------->
    /**
     * @param token
     * @param oldPassword
     * @param newPassword
     * @param confirmPassword
     * @Description password reset api for customer when he is logged in
     */
    public void updateSellerPasswordOnLogedIn(String token,String oldPassword,String newPassword,String confirmPassword){
        String username = JwtUtility.findUsernameFromToken(token);
        Seller seller = sellerRepository.findByEmail(username);
        if (passwordEncoder.matches(oldPassword,seller.getPassword()) ==false){
            throw new GiveMessageException(Arrays.asList(StatusCode.OLD_PASSWORD_MISMATCH.toString()));
        }

        if (newPassword.equals(confirmPassword)==false){
            throw new GiveMessageException(Arrays.asList(StatusCode.NEW_PASSWORD_MISMATCH.toString()));
        }
        if(RegularExp.verifyPasswordCorrectness(newPassword)){
            sellerRepository.setSellerPassword(passwordEncoder.encode(newPassword),username);
            emailService.sendSimpleMessage(username,"Password Change Alert","Your password has been updated ~ DATED: "+new Date());

        }else {
            throw new GiveMessageException(Arrays.asList(StatusCode.NOT_VALID_FORMAT.toString()),
                    Arrays.asList("Password must contain 8 letters and 1 should be numeric and 1 alphabet minimum")
            );
        }
    }






    public void updateSellerAddress(String token,HashMap<String, String> addressHashmap){

        String username = JwtUtility.findUsernameFromToken(token);
        Seller seller = sellerRepository.findByEmail(username);
        Optional <Address> address;

        try{
            address= addressRepository.findById( ( Integer.parseInt( addressHashmap.get("id") ) ) );
        }
        catch (NullPointerException e){
            throw new GiveMessageException(Arrays.asList(StatusCode.NOT_VALID_FORMAT.toString()));
        }
        catch (NumberFormatException e){
            throw new GiveMessageException(Arrays.asList(StatusCode.NOT_VALID_FORMAT.toString()));
        }

        Address updatedAddress;
        if (address.isPresent()){
            if (address.get().isOrg_address()==false){
                throw new GiveMessageException(Arrays.asList(StatusCode.INVALID.toString())
                ,Arrays.asList("Address ID does not belongs to Seller Organization")
                );
            }
            updatedAddress =address.get();
        }else {
            throw  new GiveMessageException(Arrays.asList(StatusCode.DOES_NOT_EXIST.toString()),
                    Arrays.asList("Address Id provided Does not Exist")
            );
        }

        Set<Map.Entry<String, String>> addressHashmapData = addressHashmap.entrySet();
        for (Map.Entry<String, String> entry: addressHashmapData){
            if (entry.getKey().equals("id")){
                continue;
            }

            if (entry.getKey().equals("address_line")){
                updatedAddress.setAddress_line(entry.getValue());
            }
            else if (entry.getKey().equals("city")){
                updatedAddress.setCity(entry.getValue());
            }
            else if (entry.getKey().equals("state")){
                updatedAddress.setState(entry.getValue());
            }
            else if (entry.getKey().equals("country")){
                updatedAddress.setCountry(entry.getValue());
            }
            else if (entry.getKey().equals("zip_code")){
                try {
                    updatedAddress.setZip_code(Integer.parseInt(entry.getValue()));
                    if (entry.getValue().length()<4 || entry.getValue().length()>10){
                        throw new GiveMessageException(Arrays.asList(StatusCode.NOT_VALID_FORMAT.toString()),
                                Arrays.asList("zip code can not be greater than of 10 digit and can not be of less than 4 digit")
                        );
                    }
                }
                catch (RuntimeException e){
                    throw new GiveMessageException(Arrays.asList(StatusCode.NOT_VALID_FORMAT.toString()),
                            Arrays.asList("Zip code was not in valid format")
                    );
                }
            }
            else if (entry.getKey().equals("label")){
                updatedAddress.setLabel(entry.getValue());
            }
            else {
                throw new GiveMessageException(Arrays.asList(StatusCode.NOT_VALID_FORMAT.toString()),
                        Arrays.asList("Keys naming were not in required format corresponding to fields")
                );
            }
        }
        addressRepository.save(updatedAddress);
    }


}
