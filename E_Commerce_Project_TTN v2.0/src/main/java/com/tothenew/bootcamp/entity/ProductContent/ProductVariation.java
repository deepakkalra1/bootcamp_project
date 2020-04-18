package com.tothenew.bootcamp.entity.ProductContent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tothenew.bootcamp.configurations.jpa.entityAuditable.Auditable;
import com.tothenew.bootcamp.constants.JsonConversion;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "product_variation")
public class ProductVariation  extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Integer quantity_available;
    private Integer price;

    @Transient
    private String metadata;

    @ElementCollection
    @CollectionTable(name = "product_variation_metadata"
    ,joinColumns = {@JoinColumn(name = "product_variation_id",referencedColumnName = "id")}
    )
    @MapKeyColumn(name = "key_field")
    @Column(name = "value_field")
    private Map<String,String> metadataHashmap;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;


    boolean is_active =true;

    String primary_image;

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

    public Map<String, String> getMetadataHashmap() {
        return metadataHashmap;
    }

    public void setMetadataHashmap(Map<String, String> metadataHashmap) {
        this.metadataHashmap = metadataHashmap;
    }

    public void jsonMetadataStringSerialize() throws JsonProcessingException {
        this.metadata= JsonConversion.jsonSerialization(metadataHashmap);
    }

    public void jsonMetadataStringDeserialize() throws JsonProcessingException {
        metadataHashmap=JsonConversion.jsonDeserialization(this.metadata);
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

