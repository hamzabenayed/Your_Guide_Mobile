package com.khedmetna.entities;

public class Guide {

    private int id;
    private String nom;
    private int tel;
    private Activite activite;
    private String image;

    public Guide(int id, String nom, int tel, Activite activite, String image) {
        this.id = id;
        this.nom = nom;
        this.tel = tel;
        this.activite = activite;
        this.image = image;
    }

    public Guide(String nom, int tel, Activite activite, String image) {
        this.nom = nom;
        this.tel = tel;
        this.activite = activite;
        this.image = image;
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

    public int getTel() {
        return tel;
    }

    public void setTel(int tel) {
        this.tel = tel;
    }

    public Activite getActivite() {
        return activite;
    }

    public void setActivite(Activite activite) {
        this.activite = activite;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
