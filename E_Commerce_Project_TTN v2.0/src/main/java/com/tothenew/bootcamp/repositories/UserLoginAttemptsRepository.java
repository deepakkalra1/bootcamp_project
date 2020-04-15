package com.tothenew.bootcamp.repositories;

import com.tothenew.bootcamp.entity.UserLoginAttempts;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserLoginAttemptsRepository extends CrudRepository<UserLoginAttempts,Integer> {

    @Query(value = "select attempts from user_invalid_login_attempts where email=:email order by id DESC limit 1",nativeQuery = true)
    int findNumberOfAttemptsViaEmail(String email);


    @Query(value = "select * from user_invalid_login_attempts where email=:email",nativeQuery = true)
    UserLoginAttempts findByEmail(String email);
}
