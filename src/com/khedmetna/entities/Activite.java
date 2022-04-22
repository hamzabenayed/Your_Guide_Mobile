package com.khedmetna.entities;

import java.util.Date;

public class Activite {

    private int id;
    private String nom;
    private String lieu;
    private String description;
    private String image;
    private TypeActivite typeActivite;
    private String longitude;
    private String lattitude;

    public Activite(int id, String nom, String lieu, String description, String image, TypeActivite typeActivite, String longitude, String lattitude) {
        this.id = id;
        this.nom = nom;
        this.lieu = lieu;
        this.description = description;
        this.image = image;
        this.typeActivite = typeActivite;
        this.longitude = longitude;
        this.lattitude = lattitude;
    }

    public Activite(String nom, String lieu, String description, String image, TypeActivite typeActivite, String longitude, String lattitude) {
        this.nom = nom;
        this.lieu = lieu;
        this.description = description;
        this.image = image;
        this.typeActivite = typeActivite;
        this.longitude = longitude;
        this.lattitude = lattitude;
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

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public TypeActivite getTypeActivite() {
        return typeActivite;
    }

    public void setTypeActivite(TypeActivite typeActivite) {
        this.typeActivite = typeActivite;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLattitude() {
        return lattitude;
    }

    public void setLattitude(String lattitude) {
        this.lattitude = lattitude;
    }

}
