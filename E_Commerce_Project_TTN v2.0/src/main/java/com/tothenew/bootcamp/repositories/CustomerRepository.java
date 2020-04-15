package com.tothenew.bootcamp.repositories;

import com.tothenew.bootcamp.entity.User.Customer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface CustomerRepository extends CrudRepository<Customer,Integer> {

    //--------------------------------------------------------------------------------------------------------------->
    Customer findByEmail(String email);

    //--------------------------------------------------------------------------------------------------------------->
    Customer findById(int id);


    @Query(value = "select user.id,user.creation_date,user.last_modified_date,first_name,middle_name,last_name,email,user.version,contact,is_active,is_deleted,password,image from user,user_role where user.id=user_role.user_id AND user_role.role_id=3",nativeQuery = true)
   public List<Customer> findAllCustomers(Pageable pageable);

    @Query(value = "select is_active from user where email=:emailVar",nativeQuery = true)
    Boolean findCustomerActiveStatus(String emailVar);

    @Query(value = "select is_deleted from user where email=:emailVar",nativeQuery = true)
    Boolean findCustomerDeletedStatus(String emailVar);

    @Transactional
    @Modifying
    @Query(value = "update user SET is_active=:active_status where email=:user_email",nativeQuery = true)
    void setCustomerActiveStatus(int active_status,String user_email);

    @Transactional
    @Modifying
    @Query(value = "update user SET password=:new_password where email=:user_email",nativeQuery = true)
    void setCustomerPassword(String new_password,String user_email);
}
