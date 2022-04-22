package com.khedmetna.entities;

public class TypeActivite {

    private int id;
    private String nom;

    public TypeActivite(int id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    public TypeActivite(String nom) {
        this.nom = nom;
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

}
