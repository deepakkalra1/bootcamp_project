package com.tothenew.bootcamp.entity.ProductContent;

import com.tothenew.bootcamp.configurations.jpa.entityAuditable.Auditable;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "category")
public class Category extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Product> productList;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parentCategory;


    @Version
    private int version;

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

    public List<Product> getProductSet() {
        return productList;
    }

    public void setProductSet(List<Product> productSet) {
        this.productList = productSet;
    }

    public Category getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
