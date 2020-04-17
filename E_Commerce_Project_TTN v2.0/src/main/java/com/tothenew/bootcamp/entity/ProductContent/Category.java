package com.tothenew.bootcamp.entity.ProductContent;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tothenew.bootcamp.configurations.jpa.entityAuditable.Auditable;

import javax.persistence.*;
import java.util.LinkedHashMap;
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

    //createdd for purpose of sending metadata values also with the category info... since it is a list.. so
    //created hasmap below which will be sent.. when i ll convert each content of list into hashmap and assign them to
    //clinkedHashmap below
    @JsonIgnore
    @Transient
    @OneToMany
    @JoinColumn(name = "category_id")
    List<CategoryMetadataValue> categoryMetadataValues;

    @JsonIgnore
    @Transient
    LinkedHashMap linkedCategoryValueHashMap;
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


    public List<CategoryMetadataValue> getCategoryMetadataValues() {
        return categoryMetadataValues;
    }

    public void setCategoryMetadataValues(List<CategoryMetadataValue> categoryMetadataValues) {
        this.categoryMetadataValues = categoryMetadataValues;
    }

    public LinkedHashMap getLinkedCategoryValueHashMap() {
        return linkedCategoryValueHashMap;
    }

    public void setLinkedCategoryValueHashMap(LinkedHashMap linkedCategoryValueHashMap) {
        this.linkedCategoryValueHashMap = linkedCategoryValueHashMap;
    }
}
