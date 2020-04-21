package com.tothenew.bootcamp.entity;

import javax.persistence.*;

@Entity
@Table(name = "tokens_generated")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String token;
    String email;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
