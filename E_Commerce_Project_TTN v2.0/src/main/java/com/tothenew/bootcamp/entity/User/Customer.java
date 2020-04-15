package com.tothenew.bootcamp.entity.User;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.List;
import com.tothenew.bootcamp.entity.Role.CustomerUserRole;

@Entity
@Table(name = "user")
public class Customer extends User{

    @OneToOne(mappedBy = "customer",cascade = CascadeType.ALL,targetEntity = CustomerUserRole.class)
    private CustomerUserRole customerUserRole;


    public CustomerUserRole getCustomerUserRole() {
        return customerUserRole;
    }

    public void setCustomerUserRole(CustomerUserRole customerUserRole) {
        this.customerUserRole = customerUserRole;
    }
}
