package com.tothenew.bootcamp.repositories;

import com.tothenew.bootcamp.entity.User.Address;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

public interface AddressRepository extends CrudRepository<Address,Integer>
{
    @Modifying
    @Query(value = "delete from address where id=:id",nativeQuery = true)
    void deleteById(int id);
}
