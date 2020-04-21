//package com.tothenew.bootcamp;
//
//import com.tothenew.bootcamp.entity.Role.Role;
//import com.tothenew.bootcamp.entity.User.*;
//import com.tothenew.bootcamp.repositories.CustomerRepository;
//import com.tothenew.bootcamp.repositories.RoleRepository;
//import com.tothenew.bootcamp.repositories.UsersBasicDetailsRepository;
//import com.tothenew.bootcamp.configurations.email.EmailServiceImpl;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.Arrays;
//
//@SpringBootTest
//public class CommonTesting {
//
//
//@Autowired
//EmailServiceImpl emailService;
//@Autowired
//UsersBasicDetailsRepository usersBasicDetailsRepository;
//    @Autowired
//    CustomerRepository customerRepository;
//    @Autowired
//    RoleRepository roleRepository;
//    @Test
//    public void jwtTokenFetchingTesting(){
////     String token= JwtUtility.createJWT(5,"testing","deepakkalra4@gmail.com");
////        JwtUtility.verifyJwtToken(token,"testing","deepakkalra4@gmail.com");
//
//    }
//
//    @Test
//    public void email3(){
//
//        emailService.sendSimpleMessage("deepak.kalra1@tothenew.com","ttest","tefasdf");
//
//    }
//
//    @Test
//    public void userCommonTest(){
////        UsersBasicDetails usersCommon = usersBasicDetailsRepository.findByEmail("deepak.kalra1@tothenew.com");
////        System.out.println(usersCommon.getFirst_name());
////        System.out.println(usersCommon.getSellerUserRole().getRole().getAuthority());
////        Optional optional= usersBasicDetailsRepository.findById(2);
////        optional.ifPresent(data->{
////            System.out.println(data);
////        });
//
//        Customer customer = new Customer();
//        customer.setFirst_name("check6test");
//        customer.setLast_name("adsfasf");
//        customer.setEmail("deeeeeeeeeeeeeeeeeeeeeeeeeeepak@gm.com");
//        Role role = roleRepository.findByAuthority("CUSTOMER");
//        Address address1 = new Address();
//        address1.setCountry("taliban");
//        address1.setLabel("asdfadsf");
//        address1.setZip_code(12312);
//        address1.setState("asfasdf");
//        address1.setAddress_line("afadsfasdfasdfas");
//        customer.setAddressList(Arrays.asList(address1));
//        CustomerUserRole customerUserRole = new CustomerUserRole();
//        customerUserRole.setRole(role);
//        customerUserRole.setCustomer(customer);
//        customer.setCustomerUserRole(customerUserRole);
//        customerRepository.save(customer);
//
////        customer = customerRepository.findById(23);
////        System.out.println(customer.getEmail());
//
//    }
//
//    @Test
//    public  void userCommonTestinng(){
//        UsersBasicDetails usersBasicDetails = usersBasicDetailsRepository.findByEmail("deepakkalra4@gmail.com");
//        System.out.println(
//                usersBasicDetails.getFirst_name()
//        );
//    }
//
//}
