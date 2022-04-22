package com.khedmetna.entities;

public class User {

    private int id;
    private String login;
    private String roles;
    private String mdp;
    private String nom;
    private String image;

    public User(int id, String login, String roles, String mdp, String nom, String image) {
        this.id = id;
        this.login = login;
        this.roles = roles;
        this.mdp = mdp;
        this.nom = nom;
        this.image = image;
    }

    public User(String login, String roles, String mdp, String nom, String image) {
        this.login = login;
        this.roles = roles;
        this.mdp = mdp;
        this.nom = nom;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
