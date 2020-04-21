package com.tothenew.bootcamp.service.userService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sun.tools.javac.util.ArrayUtils;
import com.tothenew.bootcamp.constants.JwtUtility;
import com.tothenew.bootcamp.constants.RegularExp;
import com.tothenew.bootcamp.entity.User.*;
import com.tothenew.bootcamp.enums.AddressLabel;
import com.tothenew.bootcamp.enums.StatusCode;
import com.tothenew.bootcamp.enums.UsersType;
import com.tothenew.bootcamp.exceptionHandling.GiveMessageException;
import com.tothenew.bootcamp.pojo.CommonResponseVO;
import com.tothenew.bootcamp.pojo.user.AddressPojo;
import com.tothenew.bootcamp.pojo.user.UserCustomerPojo;
import com.tothenew.bootcamp.entity.Role.Role;
import com.tothenew.bootcamp.exceptionHandling.FieldMissingException;
import com.tothenew.bootcamp.exceptionHandling.UserAlreadyRegisteredException;
import com.tothenew.bootcamp.repositories.AddressRepository;
import com.tothenew.bootcamp.repositories.CustomerRepository;
import com.tothenew.bootcamp.repositories.RoleRepository;
import com.tothenew.bootcamp.service.TokenService;
import com.tothenew.bootcamp.configurations.email.EmailServiceImpl;
import org.graalvm.compiler.nodes.memory.address.AddressNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.tothenew.bootcamp.entity.Role.CustomerUserRole;
import javax.transaction.Transactional;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.Map.Entry;

@Service
public class CustomerService {
    @Autowired
    CustomerRepository customer_repository;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    RoleRepository role_repository;

    @Autowired
    EmailServiceImpl emailService;

    @Autowired
    TokenService tokenService;

    @Autowired
    AddressRepository addressRepository;

    //----------------------------------------------------------------------------------------------------------->

    /***
     *
     * @param
     * @return boolean
     * @description
     * ->validating if it already exist..then throw UserAlreadyRegisteredException
     * ->handle exception
     * ->if not registered already, then register user
     * ->putting his role, user_role and address inside it
     */
    @Transactional
    public boolean registerCustomer(UserCustomerPojo userpojo) throws IOException {

        Customer customer_ = new Customer();
        try {
            customer_.setFirst_name(userpojo.getFirst_name());
            customer_.setLast_name(userpojo.getLast_name());
            customer_.setContact(userpojo.getContact());
            customer_.setEmail(userpojo.getEmail());
            customer_.setPassword(userpojo.getPassword());
            customer_.setMiddle_name(userpojo.getMiddle_name());
            customer_.setAddressList(userpojo.getAddressListOfEntityAddress());
            customer_.setImage(userpojo.getImage());
        } catch (NullPointerException e) {
        }

        if (userpojo.getImageBytes()!=null){


            int len = userpojo.getImageBytes().length;
            int i=0;
            byte[] imageBytes = new byte[len];
            for (byte b:userpojo.getImageBytes()){
                imageBytes[i]=b;
                i++;
            }
            Path path = Paths.get(System.getProperty("user.dir")+"/src/main/resources/images/profileImages/"+userpojo.getEmail());
            Files.write(path,imageBytes);
            customer_.setImage(System.getProperty("user.dir")+"/src/main/resources/images/profileImages/"+userpojo.getEmail());
        }

        /*
        everything except email can be tolerated blank till it is addedd into database
        but if email is blank.. then exception will be raised
         */
        try {
            if (customer_.getEmail() == null) {
                throw new FieldMissingException();
            }

            //verifying here if user already exist.. then throw UserALreadyExistException
            verifyCustomerAlreadyExist(customer_.getEmail());

        } catch (NullPointerException e) {
        }
        CustomerUserRole user_role = new CustomerUserRole();
        user_role.setCustomer(customer_);
        Role role = role_repository.findByAuthority(UsersType.CUSTOMER.toString());
        //user_role <- role
        user_role.setRole(role);

        String pass = customer_.getPassword();
        customer_.setPassword(passwordEncoder.encode(pass));

        //user <- user role
        customer_.setCustomerUserRole(user_role);
        customer_repository.save(customer_);
        String jwt = JwtUtility.createJWT(5, "activation", customer_.getEmail(), UsersType.CUSTOMER.toString());
        String userActivationLink = ServletUriComponentsBuilder.fromCurrentRequest().replacePath("user/activation?token={jwt}").buildAndExpand(jwt).toUriString();
        emailService.sendSimpleMessage(customer_.getEmail(), "Activation Link", userActivationLink);
        tokenService.storeTokenInDB(jwt);
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

    public boolean verifyCustomerAlreadyExist(String email) {

        Customer customer = customer_repository.findByEmail(email);
        try {
            customer.getFirst_name();
            throw new UserAlreadyRegisteredException();

        } catch (NullPointerException e) {
            return false;
        }

    }






    //---------------------------------------------------------------------------------------------------------------->

    /***
     * @param token
     * @param email
     * @return boolean
     * @description
     * activating user
     * calling verify Token method of JwtUtility class
     */
    @Transactional
    public boolean activateCustomer(String token, String email) {
        boolean resultJWTverification = JwtUtility.verifyJwtToken(token, "activation", email, UsersType.CUSTOMER.toString());
        if (resultJWTverification) {
            customer_repository.setCustomerActiveStatus(1, email);
            return true;
        }

        return false;
    }





    //--------------------------------------------------------------------------------------------------------------->
    @Transactional
    public boolean updateCustomerPassword(String email, String newPass) {
        customer_repository.setCustomerPassword(newPass, email);
        return true;
    }





    //------------------------------------------------------------------------------------------------------------->
    /***
     *
     * @param username
     * @return
     * @throws JsonProcessingException
     * @description API- to provide user pofile when he is logged in
     * 1->fetching fthe userbasic details from username
     * 2->creating a hashmap for mandatory record to be send
     * 3-> creating common response and putting hashmapdata in response and returning it
     */
    public CommonResponseVO<HashMap<String, String>> getCustomerProfile(String username) throws JsonProcessingException {

        Customer customer = customer_repository.findByEmail(username);
        HashMap<String, String> userProfile = new HashMap<>();
        userProfile.put("firstName", customer.getFirst_name());
        userProfile.put("middleName", customer.getMiddle_name());
        userProfile.put("lastName", customer.getLast_name());
        userProfile.put("contact", customer.getContact());
        userProfile.put("email", customer.getEmail());
        userProfile.put("image", customer.getImage());

        CommonResponseVO<HashMap<String, String>> commonResponseVO = new CommonResponseVO<>();
        commonResponseVO.setData(userProfile);
        return commonResponseVO;
    }





    //--------------------------------------------------------------------------------------------------------------->
    public CommonResponseVO getCustomerAddresses(String username) {
        int[] v=new int[]{0};
        Address[] addressList = new Address[customer_repository.findByEmail(username).getAddressList().size()];
        customer_repository.findByEmail(username).getAddressList().forEach(address -> {
            addressList[v[0]++]=address;
        });
        CommonResponseVO<Address[]> commonResponseVO = new CommonResponseVO<>();
        commonResponseVO.setData(addressList);
        return commonResponseVO;
    }





    //---------------------------------------------------------------------------------------------------------------->
    @Transactional
    public void updateCustomerProfile(String username, HashMap newData) {

        Customer customer = customer_repository.findByEmail(username);
        Set<Entry<String, String>> entrySet= newData.entrySet();
        for (Entry<String, String> entry:entrySet){
            if (entry.getKey().equals("firstName")){
                customer.setFirst_name(entry.getValue());
            }
            else if (entry.getKey().equals("middleName")){
                customer.setMiddle_name(entry.getValue());
            }
            else if (entry.getKey().equals("lastName")){
                    customer.setLast_name(entry.getValue());
            }
            else if (entry.getKey().equals("contact")){
                    customer.setContact(entry.getValue());
            }
            else {
                throw new GiveMessageException(Arrays.asList( StatusCode.NOT_VALID_FORMAT.toString()));
            }


        }
        customer_repository.save(customer);
        }






        //---------------------------------------------------------------------------------------------------->
    /**
      * @param token
     * @param oldPassword
     * @param newPassword
     * @param confirmPassword
     * @Description password reset api for customer when he is logged in
     */
    public void updateCustomerPasswordOnLogedIn(String token,HashMap<String, String> passwordData){
        if (passwordData.size()!=3){
            throw new GiveMessageException(Arrays.asList(StatusCode.NOT_VALID_FORMAT.toString()),
                    Arrays.asList("only 3 keys are required = 'newPassword','confirmPassword','oldPassword'"));
        }
        String newPassword=passwordData.get("newPassword");
        String confirmPassword=passwordData.get("confirmPassword");
        String oldPassword=passwordData.get("oldPassword");
        if (newPassword==null || confirmPassword==null || oldPassword==null){
            throw new GiveMessageException(Arrays.asList(StatusCode.NOT_VALID_FORMAT.toString()),
                    Arrays.asList("Any of 3 key is not provided"));
        }
            String username = JwtUtility.findUsernameFromToken(token);
            Customer customer = customer_repository.findByEmail(username);
        if (passwordEncoder.matches(oldPassword,customer.getPassword()) ==false){
            throw new GiveMessageException(Arrays.asList(StatusCode.OLD_PASSWORD_MISMATCH.toString()));
        }

        if (newPassword.equals(confirmPassword)==false){
            throw new GiveMessageException(Arrays.asList(StatusCode.NEW_PASSWORD_MISMATCH.toString()));
        }
            if(RegularExp.verifyPasswordCorrectness(newPassword)){
                customer_repository.setCustomerPassword(passwordEncoder.encode(newPassword),username);
                emailService.sendSimpleMessage(username,"Password Change Alert","Your password has been updated ~ DATED: "+new Date());

            }else {
                throw new GiveMessageException(Arrays.asList(StatusCode.NOT_VALID_FORMAT.toString()),
                        Arrays.asList("Password must contain 8 letters and 1 should be numeric and 1 alphabet minimum")
                        );
            }
        }





        //-------------------------------------------------------------------------------------------------------->
        public void addCustomerAddress(String token,AddressPojo addressPojo){

            String username = JwtUtility.findUsernameFromToken(token);
            Customer customer = customer_repository.findByEmail(username);
            Address address = new Address();
            address.setAddress_line(addressPojo.getAddress_line());
            address.setCity(addressPojo.getCity());
            address.setState(addressPojo.getState());
            address.setZip_code(addressPojo.getZip_code());
            address.setCountry(addressPojo.getCountry());
            if(addressPojo.getLabel().toLowerCase().equals("home")){
                address.setLabel(AddressLabel.HOME.toString());
            }else if(addressPojo.getLabel().toLowerCase().equals("work")){
                address.setLabel(AddressLabel.WORK.toString());
            }else if (addressPojo.getLabel().toLowerCase().equals("other")){
                address.setLabel(AddressLabel.OTHER.toString());
            }
            else {
                throw new GiveMessageException(Arrays.asList(StatusCode.NOT_VALID_FORMAT.toString()));
            }
            List<Address> addresses = customer.getAddressList();
            if (addresses==null){
                addresses=new ArrayList<Address>();
                addresses.add(address);

            }else {
                addresses.add(address);
            }

            customer.setAddressList(addresses);
            customer_repository.save(customer);
        }





        //------------------------------------------------------------------------------------------------------->
    /****
     * @param token
     * @param addressID
     * 1->find user name from token
     * 2->find customer with that username
     * 3->exist variable will be incremented if that address is found while forEach looping
     * else DOES_NOT_EXIST exception msg
     * 4->at last delete that address with its id
     */
    @Transactional
        public void deleteCustomerAddressWithAddressID(String token,int addressID){
            String username = JwtUtility.findUsernameFromToken(token);
            Customer customer = customer_repository.findByEmail(username);
            int[] exist = new int[]{0};
            List<Address> addresses = customer.getAddressList();
            if (addresses==null){

                throw new GiveMessageException(Arrays.asList(StatusCode.EMPTY.toString()));
            }
            addresses.forEach(address -> {
                if (addressID==address.getId()){


                    exist[0]++;
                }
            });

            if (exist[0]==0){
                throw new GiveMessageException(Arrays.asList(StatusCode.DOES_NOT_EXIST.toString()));
            }
        addressRepository.deleteById(addressID);
        }





        //------------------------------------------------------------------------------------------------------->
    /***
     * @param token,hasmap
     * 1->fetch username,customer obj and find its id then
     * 2->check id exist in hashmap or not..else exception
     * 3-> check if address exist against that id ...else exception
     * 4-> fetch the address object which corresponds to addressId in hashmap
     * 5-> loop on hashmap
     * 6-> update the new data ..according to the fields avaible in address entity
     * 7-> if any field is found in hashmap which is not in address entity then exception
     */
    public void updateCustomerAddress(String token,HashMap<String, String> addressHashmap){

        String username = JwtUtility.findUsernameFromToken(token);
        Customer customer = customer_repository.findByEmail(username);
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
               updatedAddress =address.get();
           }else {
               throw  new GiveMessageException(Arrays.asList(StatusCode.DOES_NOT_EXIST.toString()),
                       Arrays.asList("Address Id provided Does not Exist")
                       );
           }

           Set<Entry<String, String>> addressHashmapData = addressHashmap.entrySet();
           for (Entry<String, String> entry: addressHashmapData){
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





        //-------------------------------------------------------------------------------------------------------->
    }
