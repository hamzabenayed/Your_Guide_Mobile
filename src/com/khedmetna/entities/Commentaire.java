package com.khedmetna.entities;

import java.util.ArrayList;
import java.util.Date;

public class Commentaire {

    private int id;
    private String description;
    private Date date;
    private ArrayList<Like> likes;

    public Commentaire(int id, String description, Date date, ArrayList<Like> likes) {
        this.id = id;
        this.description = description;
        this.date = date;
        this.likes = likes;
    }

    public Commentaire(String description, Date date, ArrayList<Like> likes) {
        this.description = description;
        this.date = date;
        this.likes = likes;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ArrayList<Like> getLikes() {
        return likes;
    }

    public void setLikes(ArrayList<Like> likes) {
        this.likes = likes;
    }
}
