package com.tothenew.bootcamp.repositories;

import com.tothenew.bootcamp.entity.User.Customer;
import com.tothenew.bootcamp.entity.User.Seller;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface SellerRepository extends CrudRepository<Seller, Integer> {

    //--------------------------------------------------------------------------------------------------------------->
    Seller findByEmail(String email);

    //--------------------------------------------------------------------------------------------------------------->
    Seller findById(int id);


    @Query(value = "select is_active from user  where email=:email",nativeQuery = true)
    int findSellerActiveStatus(String email);

    @Query(value = "select user.id,user.created_date,user.last_modified_by,user.created_by,user.last_modified_date,first_name,middle_name,last_name,email,image,user.version,contact,is_active,is_deleted,password  from user,user_role where user.id=user_role.user_id AND user_role.role_id=2",nativeQuery = true)
    public List<Seller> findAllSellers(Pageable pageable);

    @Transactional
    @Modifying
    @Query(value = "update user SET is_active=:active_status where email=:user_email",nativeQuery = true)
    void setSellerActiveStatus(int active_status,String user_email);


    @Transactional
    @Modifying
    @Query(value = "update user SET password=:new_password where email=:user_email",nativeQuery = true)
    void setSellerPassword(String new_password,String user_email);
}
