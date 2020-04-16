package com.tothenew.bootcamp.entity.ProductContent;

import com.tothenew.bootcamp.configurations.jpa.entityAuditable.Auditable;

import javax.persistence.*;
import java.util.Map;

@Entity
@Table(name = "category_metadata_field")
public class CategoryMetadataField extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "category_key")
    private String categoryKey;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryKey() {
        return categoryKey;
    }

    public void setCategoryKey(String categoryKey) {
        this.categoryKey = categoryKey;
    }

}
