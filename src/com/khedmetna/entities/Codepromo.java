package com.khedmetna.entities;

import java.util.Date;

public class Codepromo {

    private int id;
    private String code;
    private Date dateDebut;
    private Date dateFin;

    public Codepromo(int id, String code, Date dateDebut, Date dateFin) {
        this.id = id;
        this.code = code;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    public Codepromo(String code, Date dateDebut, Date dateFin) {
        this.code = code;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

}
