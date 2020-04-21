package com.tothenew.bootcamp.entity.User;

import javax.persistence.*;

import com.tothenew.bootcamp.entity.Role.SellerUserRole;

@Entity
@Table(name = "user")
public class Seller extends User{

    @OneToOne(mappedBy = "seller",cascade = CascadeType.ALL)
    private SellerOrganizationDetails sellerOrganizationDetails;

    @OneToOne(mappedBy = "seller",cascade = CascadeType.ALL)
    private SellerUserRole sellerUserRole;


    public SellerUserRole getSellerUserRole() {
        return sellerUserRole;
    }

    public void setSellerUserRole(SellerUserRole sellerUserRole) {
        this.sellerUserRole = sellerUserRole;
    }




    //getter and setters------------------------------------------------------------------------------------->
    public SellerOrganizationDetails getSellerOrganizationDetails() {
        return sellerOrganizationDetails;
    }

    public void setSellerOrganizationDetails(SellerOrganizationDetails sellerOrganizationDetails) {
        this.sellerOrganizationDetails = sellerOrganizationDetails;
    }


}

