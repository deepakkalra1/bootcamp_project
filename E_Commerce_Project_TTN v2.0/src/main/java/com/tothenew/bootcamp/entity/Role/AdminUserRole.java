package com.tothenew.bootcamp.entity.Role;

import com.tothenew.bootcamp.entity.User.Admin;
import com.tothenew.bootcamp.entity.User.Customer;

import javax.persistence.*;


@Entity
@Table(name = "user_role")
public class AdminUserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private Admin admin;

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

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }
}
