package com.khedmetna.entities;

import java.util.Date;

public class Livraison {

    private int id;
    private Date date;
    private String nomLivreur;
    private String prenomLivreur;
    private String telLivreur;
    private String adresseLivraison;
    private Panier panier;

    public Livraison(int id, Date date, String nomLivreur, String prenomLivreur, String telLivreur, String adresseLivraison, Panier panier) {
        this.id = id;
        this.date = date;
        this.nomLivreur = nomLivreur;
        this.prenomLivreur = prenomLivreur;
        this.telLivreur = telLivreur;
        this.adresseLivraison = adresseLivraison;
        this.panier = panier;
    }

    public Livraison(Date date, String nomLivreur, String prenomLivreur, String telLivreur, String adresseLivraison, Panier panier) {
        this.date = date;
        this.nomLivreur = nomLivreur;
        this.prenomLivreur = prenomLivreur;
        this.telLivreur = telLivreur;
        this.adresseLivraison = adresseLivraison;
        this.panier = panier;
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

    public String getNomLivreur() {
        return nomLivreur;
    }

    public void setNomLivreur(String nomLivreur) {
        this.nomLivreur = nomLivreur;
    }

    public String getPrenomLivreur() {
        return prenomLivreur;
    }

    public void setPrenomLivreur(String prenomLivreur) {
        this.prenomLivreur = prenomLivreur;
    }

    public String getTelLivreur() {
        return telLivreur;
    }

    public void setTelLivreur(String telLivreur) {
        this.telLivreur = telLivreur;
    }

    public String getAdresseLivraison() {
        return adresseLivraison;
    }

    public void setAdresseLivraison(String adresseLivraison) {
        this.adresseLivraison = adresseLivraison;
    }

    public Panier getPanier() {
        return panier;
    }

    public void setPanier(Panier panier) {
        this.panier = panier;
    }

}
