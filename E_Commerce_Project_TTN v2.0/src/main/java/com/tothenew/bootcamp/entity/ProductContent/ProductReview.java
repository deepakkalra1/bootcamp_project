package com.tothenew.bootcamp.entity.ProductContent;

import com.tothenew.bootcamp.configurations.jpa.entityAuditable.Auditable;
import com.tothenew.bootcamp.entity.User.Customer;

import javax.persistence.*;

@Entity
@Table(name = "product_review")
public class ProductReview extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String review;

    private int rating;


    @OneToOne
    @JoinColumn(name = "product_id",referencedColumnName = "id")
    Product productWhoseReviewItIs;


    @OneToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    Customer customerWhoReviewIt;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }


    public Product getProductWhoseReviewItIs() {
        return productWhoseReviewItIs;
    }

    public void setProductWhoseReviewItIs(Product productWhoseReviewItIs) {
        this.productWhoseReviewItIs = productWhoseReviewItIs;
    }

    public Customer getCustomerWhoReviewIt() {
        return customerWhoReviewIt;
    }

    public void setCustomerWhoReviewIt(Customer customerWhoReviewIt) {
        this.customerWhoReviewIt = customerWhoReviewIt;
    }
}
