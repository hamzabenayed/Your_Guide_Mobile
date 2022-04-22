package com.khedmetna.entities;

public class Produit {

    private int id;
    private String nom;
    private String description;
    private int prix;
    private String image;
    private Category category;

    public Produit(int id, String nom, String description, int prix, String image, Category category) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.prix = prix;
        this.image = image;
        this.category = category;
    }

    public Produit(String nom, String description, int prix, String image, Category category) {
        this.nom = nom;
        this.description = description;
        this.prix = prix;
        this.image = image;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String toString(){
        return   " Nom : " +nom;
    }

}
