package com.tothenew.bootcamp.entity.Role;

import com.tothenew.bootcamp.entity.Role.Role;
import com.tothenew.bootcamp.entity.User.Seller;

import javax.persistence.*;

@Entity
@Table(name = "user_role")
public class SellerUserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private Seller seller;

    @OneToOne
    @JoinColumn(name = "role_id")
    private Role role;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }
}
