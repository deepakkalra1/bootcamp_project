package com.tothenew.bootcamp.entity.ProductContent;

import com.tothenew.bootcamp.configurations.jpa.entityAuditable.Auditable;

import javax.persistence.*;

@Entity
@Table(name = "category_metadata_values")
public class CategoryMetadataValue extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "category_key_id",referencedColumnName = "id")
    CategoryMetadataField categoryMetadataField;

    @OneToOne
    @JoinColumn(name = "category_id",referencedColumnName = "id")
    Category category;

    @Column(name = "category_value")
    String categoryValue;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public CategoryMetadataField getCategoryMetadataField() {
        return categoryMetadataField;
    }

    public void setCategoryMetadataField(CategoryMetadataField categoryMetadataField) {
        this.categoryMetadataField = categoryMetadataField;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getCategoryValue() {
        return categoryValue;
    }

    public void setCategoryValue(String categoryValue) {
        this.categoryValue = categoryValue;
    }
}
