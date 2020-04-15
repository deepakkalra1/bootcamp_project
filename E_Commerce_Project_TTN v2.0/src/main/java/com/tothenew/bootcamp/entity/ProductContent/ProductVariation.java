package com.tothenew.bootcamp.entity.ProductContent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tothenew.bootcamp.constants.JsonConversion;

import javax.persistence.*;
import java.util.HashMap;

@Entity
@Table(name = "product_variation")
public class ProductVariation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Integer quantity_available;
    private Integer price;

    private String metadata;

    @Transient
    private HashMap<String,String> metadataHashmap;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;


    @Version
    int version;


    //-------------------------------------------------------------------------------------------------------------->
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public HashMap<String, String> getMetadataHashmap() {
        return metadataHashmap;
    }

    public void setMetadataHashmap(HashMap<String, String> metadataHashmap) {
        this.metadataHashmap = metadataHashmap;
    }

    public void jsonMetadataStringSerialize() throws JsonProcessingException {
        this.metadata= JsonConversion.jsonSerialization(metadataHashmap);
    }

    public void jsonMetadataStringDeserialize() throws JsonProcessingException {
        metadataHashmap=JsonConversion.jsonDeserialization(this.metadata);
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}

