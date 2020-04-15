package com.tothenew.bootcamp.pojo.user;

import com.tothenew.bootcamp.entity.User.Address;
import com.tothenew.bootcamp.entity.User.SellerOrganizationDetails;

import java.util.ArrayList;
import java.util.List;

public class UserSellerPojo {
    private int id;
    private String email;
    private String first_name;
    private String middle_name;
    private String last_name;
    private String password;
    private String contact;
    private boolean is_deleted;
    private boolean is_active;
    private List<AddressPojo> addressList;
    private SellerOrganizationDetailsPojo sellerOrganizationDetailsPojo;
    private String image;

    //------------------------------------------------------------------------------------------------------------->


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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
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

    public List<AddressPojo> getAddressList() {
        return addressList;
    }
    public List<com.tothenew.bootcamp.entity.User.Address> getAddressListOfEntityAddress() {
        List<com.tothenew.bootcamp.entity.User.Address> addresses =
                new ArrayList<Address>();

        addressList.forEach(address->{
            com.tothenew.bootcamp.entity.User.Address add =
                    new com.tothenew.bootcamp.entity.User.Address();
            add.setAddress_line(address.getAddress_line());
            add.setCity(address.getCity());
            add.setState(address.getState());
            add.setCountry(address.getCountry());
            add.setZip_code(address.getZip_code());
            add.setLabel(address.getLabel());
            addresses.add(add);

        });

        return addresses;
    }

    public void setAddressList(List<AddressPojo> addressList) {
        this.addressList = addressList;
    }

    public SellerOrganizationDetailsPojo getSellerOrganizationDetailsPojo() {
        return sellerOrganizationDetailsPojo;
    }

    public SellerOrganizationDetails getSellerBlockOfEntity() {
        SellerOrganizationDetails sellerOrganizationDetails = new SellerOrganizationDetails();
        sellerOrganizationDetails.setGst(sellerOrganizationDetailsPojo.getGst());
        sellerOrganizationDetails.setCompany_name(sellerOrganizationDetailsPojo.getCompany_name());
        sellerOrganizationDetails.setCompany_contact(sellerOrganizationDetailsPojo.getCompany_contact());

        return sellerOrganizationDetails;
    }

    public void setSellerOrganizationDetailsPojo(SellerOrganizationDetailsPojo sellerOrganizationDetailsPojo) {
        this.sellerOrganizationDetailsPojo = sellerOrganizationDetailsPojo;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
