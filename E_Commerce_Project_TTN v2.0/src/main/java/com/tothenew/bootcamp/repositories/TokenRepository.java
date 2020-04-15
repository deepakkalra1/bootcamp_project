package com.tothenew.bootcamp.repositories;

import com.tothenew.bootcamp.entity.Token;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface TokenRepository extends CrudRepository<Token,Integer> {
    @Query(value = "select * from tokens_generated where email=:email",nativeQuery = true)
    List<Token> findTokensByEmail(String email);


    @Modifying
    @Query(value = "delete from tokens_generated where id=:id",nativeQuery = true)
    void deleteTokenWithId(int id);
}
