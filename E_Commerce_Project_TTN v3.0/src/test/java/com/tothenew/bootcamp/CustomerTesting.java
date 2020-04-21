package com.tothenew.bootcamp;

import com.tothenew.bootcamp.entity.User.Customer;
import com.tothenew.bootcamp.repositories.CustomerRepository;
import com.tothenew.bootcamp.repositories.RoleRepository;
import com.tothenew.bootcamp.service.userService.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.Modifying;

import javax.transaction.Transactional;

@SpringBootTest
public class CustomerTesting {

    @Autowired
    CustomerService customerService;

    @Autowired
    RoleRepository role_repository;

    @Autowired
    CustomerRepository customerRepository;


    @Test
    public void testingEmail(){

//        System.out.println("Email is getting validated properly");
//        System.out.println(customerService.verifyCustomerEmail("deepa923424kka.l.ra@gmail.com"));
    }


    @Test
    public void role_repo_testing(){
        System.out.println(role_repository.findByAuthority("user").getId());

    }


    @Test
    @Modifying
    @Transactional
    public void updatingEveryUserVersion(){
//        Iterable<Customer> customers= customerRepository.findAll();
//        customers.forEach(customer -> {
//            customer.setVersion(0);
//            customerRepository.save(customer);
//        });
        Customer customer = customerRepository.findByEmail("deepak.kalra1@tothenew.com");

        customerRepository.save(customer);
    }


}
