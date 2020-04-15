package com.tothenew.bootcamp.service.userService;

import com.tothenew.bootcamp.entity.User.Customer;
import com.tothenew.bootcamp.entity.User.Seller;
import com.tothenew.bootcamp.enums.StatusCode;
import com.tothenew.bootcamp.enums.UsersType;
import com.tothenew.bootcamp.exceptionHandling.GiveMessageException;
import com.tothenew.bootcamp.exceptionHandling.UserNotRegisteredException;
import com.tothenew.bootcamp.pojo.CommonResponseVO;
import com.tothenew.bootcamp.repositories.CustomerRepository;
import com.tothenew.bootcamp.repositories.RoleRepository;
import com.tothenew.bootcamp.repositories.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


@Service
public class AdminService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    SellerRepository sellerRepository;

    @Autowired
    RoleRepository roleRepository;


    //---------------------------------------------------------------------------------------------------------------->
    /***
     * @return
     * @desciption
     * returns all customers by the request of admin
     */
    public CommonResponseVO findAllCustomers(){
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.ASC,"id");
        List<Customer> customers= customerRepository.findAllCustomers(pageable);

       HashMap<Integer,HashMap<String, String>> allCustomersHashmap = new HashMap<>();
        int i[]=new int[]{0};
        customers.forEach(c->{
            HashMap <String, String> userDetail = new HashMap<>();
          userDetail.put("id",String.valueOf(c.getId()));
          userDetail.put("First Name",c.getFirst_name());
          userDetail.put("Last Name",c.getLast_name());
          userDetail.put("Email",c.getEmail());
          userDetail.put("Contact",c.getContact());
          userDetail.put("Is Active",String.valueOf(c.isIs_active()));
            userDetail.put("Is Deleted",String.valueOf(c.isIs_deleted()));
            allCustomersHashmap.put(i[0],userDetail);
            ++i[0];
        });


        CommonResponseVO<HashMap<Integer, HashMap<String, String>>> commonResponseVO = new CommonResponseVO<>();
        commonResponseVO.setData(allCustomersHashmap);
        return commonResponseVO;
    }





    //---------------------------------------------------------------------------------------------------------------->
    public CommonResponseVO findAllSellers(){
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.ASC,"id");
        List<Seller> sellers= sellerRepository.findAllSellers(pageable);

        HashMap<Integer,HashMap<String, String>> allSellerHashmap = new HashMap<>();

        sellers.forEach(c->{
            System.out.println(c.getId());
        });


        int i[]=new int[]{0};
        sellers.forEach(c->{
            HashMap <String, String> userDetail = new HashMap<>();
            userDetail.put("id",String.valueOf(c.getId()));
            userDetail.put("First Name",c.getFirst_name());
            userDetail.put("Last Name",c.getLast_name());
            userDetail.put("Email",c.getEmail());
            userDetail.put("Contact",c.getContact());
            userDetail.put("Is Active",String.valueOf(c.isIs_active()));
            userDetail.put("Is Deleted",String.valueOf(c.isIs_deleted()));
            userDetail.put("Company Name",c.getSellerOrganizationDetails().getCompany_name());
            userDetail.put("Company Contact",c.getSellerOrganizationDetails().getCompany_contact());
            userDetail.put("Gst Number",c.getSellerOrganizationDetails().getGst());
            allSellerHashmap.put(i[0],userDetail);
            ++i[0];
        });


        CommonResponseVO<HashMap<Integer, HashMap<String, String>>> commonResponseVO = new CommonResponseVO<>();
        commonResponseVO.setData(allSellerHashmap);
        return commonResponseVO;
    }





    //---------------------------------------------------------------------------------------------------------------->
    @Transactional
    public void activateCustomer(String email) {
        try {

            String role = roleRepository.findAuthorityWithEmail(email);
            if (role.equals(UsersType.CUSTOMER.toString())) {
                Customer customer = customerRepository.findByEmail(email);
                    //if user is exist in db but is deleted
                    if (customer.isIs_deleted()) {
                        throw new UserNotRegisteredException();
                    } else {
                        //if user is already active
                        if (customer.isIs_active()) {
                            throw new GiveMessageException(Arrays.asList(StatusCode.FAILED.toString()), Arrays.asList("Customer is already Active"));
                        } else {
                            //if user is neither delted not it is active.. so activate it here
                            customerRepository.setCustomerActiveStatus(1, email);
                        }
                    }
            }
            else {
                throw new GiveMessageException(Arrays.asList(StatusCode.FAILED.toString()),Arrays.asList("Only Customers can be Activated via this API"));
            }
        }
        catch (NullPointerException e){
            throw new UserNotRegisteredException();
        }
    }





    //---------------------------------------------------------------------------------------------------------------->
    @Transactional
    public void de_activateCustomer(String email) {
        try {

            String role = roleRepository.findAuthorityWithEmail(email);

            if (role.equals(UsersType.CUSTOMER.toString())) {
                Customer customer = customerRepository.findByEmail(email);
                //if user is exist in db but is deleted
                if (customer.isIs_deleted()) {
                    throw new UserNotRegisteredException();
                } else {
                    //if user is not de-activated
                    if (customer.isIs_active()) {
                        customerRepository.setCustomerActiveStatus(0, email);
                    } else {
                        //if user is already de-active
                        throw new GiveMessageException(Arrays.asList(StatusCode.FAILED.toString()), Arrays.asList("Customer is already De-Active"));
                    }
                }
            }
            else {
                throw new GiveMessageException(Arrays.asList(StatusCode.FAILED.toString()),Arrays.asList("Only Customers can be De-Activated via this API"));
            }
        }
        catch (NullPointerException e){
            throw new UserNotRegisteredException();
        }
    }





    //---------------------------------------------------------------------------------------------------------------->
    @Transactional
    public void activateSeller(String email) {
        try {
            String role = roleRepository.findAuthorityWithEmail(email);
            if (role.equals(UsersType.SELLER.toString())) {
                Seller seller = sellerRepository.findByEmail(email);
                //if user is exist in db but is deleted
                if (seller.isIs_deleted()) {
                    throw new UserNotRegisteredException();
                } else {
                    //if user is already active
                    if (seller.isIs_active()) {
                        throw new GiveMessageException(Arrays.asList(StatusCode.FAILED.toString()), Arrays.asList("Seller is already Active"));
                    } else {
                        //if user is neither delted not it is active.. so activate it here
                        sellerRepository.setSellerActiveStatus(1, email);
                    }
                }
            }
            else {
                throw new GiveMessageException(Arrays.asList(StatusCode.FAILED.toString()),Arrays.asList("Only Sellers can be Activated via this API"));
            }
        }
        catch (NullPointerException e){
            throw new UserNotRegisteredException();
        }
    }





    //---------------------------------------------------------------------------------------------------------------->
    @Transactional
    public void de_activateSeller(String email) {
        try {
            String role = roleRepository.findAuthorityWithEmail(email);
            if (role.equals(UsersType.SELLER.toString())) {
                Seller seller = sellerRepository.findByEmail(email);
                //if user is exist in db but is deleted
                if (seller.isIs_deleted()) {
                    throw new UserNotRegisteredException();
                } else {

                    if (seller.isIs_active()) {
                        sellerRepository.setSellerActiveStatus(0, email);

                    } else {

                        throw new GiveMessageException(Arrays.asList(StatusCode.FAILED.toString()), Arrays.asList("Seller is already De-Active"));
                    }
                }
            }
            else {
                throw new GiveMessageException(Arrays.asList(StatusCode.FAILED.toString()),Arrays.asList("Only Sellers can be De-Activated via this API"));
            }
        }
        catch (NullPointerException e){
            throw new UserNotRegisteredException();
        }
    }





    }
