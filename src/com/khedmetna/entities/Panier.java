package com.khedmetna.entities;

import java.util.Date;

public class Panier {

    private int id;
    private String description;

    public Panier(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public Panier(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
