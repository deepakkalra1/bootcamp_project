package com.tothenew.bootcamp.pojo.user;

public class SellerOrganizationDetailsPojo {
    private int id;
    private String gst;
    private String company_contact;
    private String company_name;

    //------------------------------------------------------------------------------------------------------------>


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public String getCompany_contact() {
        return company_contact;
    }

    public void setCompany_contact(String company_contact) {
        this.company_contact = company_contact;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }
}
