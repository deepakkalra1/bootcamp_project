package com.tothenew.bootcamp.entity.User;

import com.tothenew.bootcamp.configurations.jpa.entityAuditable.UserAuditable;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.List;

@MappedSuperclass
public class User extends UserAuditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    //@NotBlank(message = "Email can not be blank")
    @Email
    private String email;

    //@NotBlank(message = "First name can not be blank")
    private String first_name;


    private String middle_name;

   // @NotBlank(message = "Last name can not be blank")
    private String last_name;

    //@NotBlank(message = "Password can not be blank")
    //@Size(min = 8, message = "Min 8 character password is required")
    private String password;

    //@NotBlank(message = "Contact can not be blank")
 //   @Size(max = 10, min = 10, message = "Digits should be 10")
    private String contact;

    @Version
    int version;

    private boolean is_deleted=false;
    private boolean is_active=false;
    String image;



    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private List<Address> addressList;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getMiddle_name() {
        return middle_name;
    }

    public void setMiddle_name(String middle_name) {
        this.middle_name = middle_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(boolean is_deleted) {
        this.is_deleted = is_deleted;
    }

    public boolean isIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public List<Address> getAddressList() {

        return addressList;
    }

    public void setAddressList(List<Address> addressList) {
        this.addressList = addressList;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
