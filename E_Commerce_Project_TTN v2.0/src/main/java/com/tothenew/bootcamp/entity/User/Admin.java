package com.tothenew.bootcamp.entity.User;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import com.tothenew.bootcamp.entity.Role.AdminUserRole;

@Entity
@Table(name = "user")
public class Admin extends User {
    @OneToOne(mappedBy = "admin",cascade = CascadeType.ALL)
    private AdminUserRole adminUserRole;


    public AdminUserRole getAdminUserRole() {
        return adminUserRole;
    }

    public void setAdminUserRole(AdminUserRole adminUserRole) {
        this.adminUserRole = adminUserRole;
    }
}
