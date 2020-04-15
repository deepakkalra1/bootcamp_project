package com.tothenew.bootcamp.repositories;

import com.tothenew.bootcamp.entity.User.Admin;
import com.tothenew.bootcamp.entity.User.Customer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface AdminRepository extends CrudRepository<Admin,Integer> {


    //--------------------------------------------------------------------------------------------------------------->
    Admin findByEmail(String email);

    //--------------------------------------------------------------------------------------------------------------->
    Admin findById(int id);


    @Query(value = "select user.id,user.creation_date,user.last_modified_date,first_name,middle_name,last_name,email,contact,is_active,version,is_deleted,password from user,user_role where user.id=user_role.user_id AND user_role.role_id=1",nativeQuery = true)
    public List<Admin> findAllAdmin(Pageable pageable);

    @Query(value = "select is_active from user where email=:emailVar",nativeQuery = true)
    Boolean findAdminActiveStatus(String emailVar);

    @Query(value = "select is_deleted from user where email=:emailVar",nativeQuery = true)
    Boolean findAdminDeletedStatus(String emailVar);

    @Transactional
    @Modifying
    @Query(value = "update user SET is_active=:active_status where email=:user_email",nativeQuery = true)
    void setAdminActiveStatus(int active_status,String user_email);

    @Transactional
    @Modifying
    @Query(value = "update user SET password=:new_password where email=:user_email",nativeQuery = true)
    void setAdminPassword(String new_password,String user_email);
}
