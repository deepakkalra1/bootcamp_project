package com.tothenew.bootcamp.pojo;

import com.tothenew.bootcamp.entity.ProductContent.Product;

import javax.persistence.*;
import java.util.Map;

public class ProductVariationPojo {

    private int id;
    private Integer quantity_available;
    private Integer price;
    private Map<String,String> metadataHashmap;
    private int product_id;
    boolean is_active ;
    String primary_image;


    //------------------------------------------------------------------------------------------------------>

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getQuantity_available() {
        return quantity_available;
    }

    public void setQuantity_available(Integer quantity_available) {
        this.quantity_available = quantity_available;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Map<String, String> getMetadataHashmap() {
        return metadataHashmap;
    }

    public void setMetadataHashmap(Map<String, String> metadataHashmap) {
        this.metadataHashmap = metadataHashmap;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public boolean isIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    public String getPrimary_image() {
        return primary_image;
    }

    public void setPrimary_image(String primary_image) {
        this.primary_image = primary_image;
    }
}
