package com.khedmetna.entities;

import com.khedmetna.gui.front.promo.DisplayAll;

public class Promo implements Comparable<Promo> {

    private int id;
    private float pourcentage;
    private Codepromo codepromo;
    private String image;

    public Promo(int id, float pourcentage, Codepromo codepromo, String image) {
        this.id = id;
        this.pourcentage = pourcentage;
        this.codepromo = codepromo;
        this.image = image;
    }

    public Promo(float pourcentage, Codepromo codepromo, String image) {
        this.pourcentage = pourcentage;
        this.codepromo = codepromo;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getPourcentage() {
        return pourcentage;
    }

    public void setPourcentage(float pourcentage) {
        this.pourcentage = pourcentage;
    }

    public Codepromo getCodepromo() {
        return codepromo;
    }

    public void setCodepromo(Codepromo codepromo) {
        this.codepromo = codepromo;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int compareTo(Promo promo) {
        switch (DisplayAll.promoCompareVar) {
            case "codepromo":
                return this.codepromo.getCode().compareTo(promo.getCodepromo().getCode());
            case "pourcentage":
                return Float.compare(this.pourcentage, promo.pourcentage);
            default:
                return 0;
        }
    }
}
