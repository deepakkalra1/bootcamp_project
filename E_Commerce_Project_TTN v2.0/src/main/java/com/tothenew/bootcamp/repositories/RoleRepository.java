package com.tothenew.bootcamp.repositories;

import com.tothenew.bootcamp.entity.Role.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Integer> {

    Role findByAuthority(String authority);

    @Query(value = "select authority from role, ( select email,role_id from user left join user_role on user.id=user_role.user_id ) tab where tab.role_id = role.id AND tab.email =:email",nativeQuery = true)
    String findAuthorityWithEmail(String email);

}
