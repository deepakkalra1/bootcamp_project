package com.tothenew.bootcamp.entity.User;

import com.tothenew.bootcamp.configurations.jpa.entityAuditable.Auditable;

import javax.persistence.*;

@Entity
@Table(name = "address")
public class Address  extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

   // @NotBlank(message = "City must be provided")
   private String city;

    //@NotBlank(message = "State must be provided")
    private String state;

    //@NotBlank(message = "Country must be provided")
    private String country;

    //@NotBlank(message = "House AddressPojo must be provided")
    private String address_line;

    //@NotBlank(message = "Zip code must be provided")
    private int zip_code;

    //@NotBlank(message = "Label must be provided")
    private String label;


    //specify whether the address is of organization of seller or not
    @Transient
    boolean org_address=false;

    //getters and setters


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddress_line() {
        return address_line;
    }

    public void setAddress_line(String address_line) {
        this.address_line = address_line;
    }

    public int getZip_code() {
        return zip_code;
    }

    public void setZip_code(int zip_code) {
        this.zip_code = zip_code;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isOrg_address() {
        return org_address;
    }

    public void setOrg_address(boolean org_address) {
        this.org_address = org_address;
    }
}
