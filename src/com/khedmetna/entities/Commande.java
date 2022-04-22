package com.khedmetna.entities;

import java.util.Date;

public class Commande {

    private int id;
    private Date date;
    private String etat;
    private String commentaire;
    private String adresse;
    private String totalcost;
    private String product;

    public Commande(int id, Date date, String etat, String commentaire, String adresse, String totalcost, String product) {
        this.id = id;
        this.date = date;
        this.etat = etat;
        this.commentaire = commentaire;
        this.adresse = adresse;
        this.totalcost = totalcost;
        this.product = product;
    }

    public Commande(Date date, String etat, String commentaire, String adresse, String totalcost, String product) {
        this.date = date;
        this.etat = etat;
        this.commentaire = commentaire;
        this.adresse = adresse;
        this.totalcost = totalcost;
        this.product = product;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTotalcost() {
        return totalcost;
    }

    public void setTotalcost(String totalcost) {
        this.totalcost = totalcost;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

}
