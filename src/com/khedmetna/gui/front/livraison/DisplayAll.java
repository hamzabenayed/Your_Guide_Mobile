package com.khedmetna.gui.front.livraison;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.khedmetna.entities.Livraison;
import com.khedmetna.services.LivraisonService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DisplayAll extends Form {

    public static Livraison currentLivraison = null;

    public DisplayAll(Form previous) {
        super("Livraisons", new BoxLayout(BoxLayout.Y_AXIS));
        addGUIs();
        super.getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    private void addGUIs() {
        ArrayList<Livraison> listLivraisons = LivraisonService.getInstance().getAll();

        if (listLivraisons.size() > 0) {
            for (Livraison livraison : listLivraisons) {
                Component model = makeLivraisonModel(livraison);
                this.add(model);
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    Label dateLabel, nomLivreurLabel, prenomLivreurLabel, telLivreurLabel, adresseLivraisonLabel, panierLabel;

    Container btnsContainer;

    private Component makeLivraisonModel(Livraison livraison) {
        Container livraisonModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        livraisonModel.setUIID("containerRounded");

        dateLabel = new Label("Date : " + new SimpleDateFormat("dd-MM-yyyy").format(livraison.getDate()));
        dateLabel.setUIID("labelDefault");

        nomLivreurLabel = new Label("NomLivreur : " + livraison.getNomLivreur());
        nomLivreurLabel.setUIID("labelDefault");

        prenomLivreurLabel = new Label("PrenomLivreur : " + livraison.getPrenomLivreur());
        prenomLivreurLabel.setUIID("labelDefault");

        telLivreurLabel = new Label("TelLivreur : " + livraison.getTelLivreur());
        telLivreurLabel.setUIID("labelDefault");

        adresseLivraisonLabel = new Label("AdresseLivraison : " + livraison.getAdresseLivraison());
        adresseLivraisonLabel.setUIID("labelDefault");

        panierLabel = new Label("Panier : " + livraison.getPanier().getDescription());
        panierLabel.setUIID("labelDefault");

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");

        livraisonModel.addAll(
                dateLabel, nomLivreurLabel, prenomLivreurLabel, telLivreurLabel, adresseLivraisonLabel, panierLabel,
                btnsContainer
        );

        return livraisonModel;
    }
}
