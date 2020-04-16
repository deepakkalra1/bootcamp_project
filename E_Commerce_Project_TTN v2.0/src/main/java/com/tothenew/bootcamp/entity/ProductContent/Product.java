package com.tothenew.bootcamp.entity.ProductContent;

import com.tothenew.bootcamp.configurations.jpa.entityAuditable.Auditable;
import com.tothenew.bootcamp.entity.User.Seller;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "product")
public class Product extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String description;

    private boolean is_cancellable;
    private boolean is_returnable;
    private boolean is_active;
    private String brand;


    @ManyToOne
    @JoinColumn(name = "seller_user_id")
    private Seller seller_seller;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL)
    private List<ProductVariation> productVariationlist;





//--------------------------------------------------------------------------------------------------------------->



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isIs_cancellable() {
        return is_cancellable;
    }

    public void setIs_cancellable(boolean is_cancellable) {
        this.is_cancellable = is_cancellable;
    }

    public boolean isIs_returnable() {
        return is_returnable;
    }

    public void setIs_returnable(boolean is_returnable) {
        this.is_returnable = is_returnable;
    }

    public boolean isIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Seller getSeller_seller() {
        return seller_seller;
    }

    public void setSeller_seller(Seller seller_seller) {
        this.seller_seller = seller_seller;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<ProductVariation> getProductVariationlist() {
        return productVariationlist;
    }

    public void setProductVariationlist(List<ProductVariation> productVariationlist) {
        this.productVariationlist = productVariationlist;
    }
}
