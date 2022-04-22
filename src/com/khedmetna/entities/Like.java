package com.khedmetna.entities;

public class Like {

    private int id;
    private String nom;
    private int rate;
    private int note;
    private Commentaire commentaire;

    public Like(int id, String nom, int rate, int note, Commentaire commentaire) {
        this.id = id;
        this.nom = nom;
        this.rate = rate;
        this.note = note;
        this.commentaire = commentaire;
    }

    public Like(String nom, int rate, int note, Commentaire commentaire) {
        this.nom = nom;
        this.rate = rate;
        this.note = note;
        this.commentaire = commentaire;
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

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getNote() {
        return note;
    }

    public void setNote(int note) {
        this.note = note;
    }

    public Commentaire getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(Commentaire commentaire) {
        this.commentaire = commentaire;
    }

}
