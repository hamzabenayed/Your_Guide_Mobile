package com.khedmetna.gui.back.livraison;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.khedmetna.entities.Panier;
import com.khedmetna.services.PanierService;

import java.util.ArrayList;

public class ChoosePanier extends Form {

    Form previousForm;

    public ChoosePanier(Form previous) {
        super("Choisir un panier", new BoxLayout(BoxLayout.Y_AXIS));

        previousForm = previous;
        addGUIs();

        super.getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    public void refresh() {
        this.removeAll();
        addGUIs();
        this.refreshTheme();
    }

    private void addGUIs() {
        ArrayList<Panier> listPaniers = PanierService.getInstance().getAll();
        if (listPaniers.size() > 0) {
            for (Panier paniers : listPaniers) {
                this.add(makePanierModel(paniers));
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    Label livraisonLabel, descriptionLabel;
    Button chooseBtn;
    Container btnsContainer;

    private Component makePanierModel(Panier panier) {
        Container panierModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        panierModel.setUIID("containerRounded");

        descriptionLabel = new Label("Description : " + panier.getDescription());
        descriptionLabel.setUIID("labelDefault");

        livraisonLabel = new Label("Livraison : " + panier.getDescription());
        livraisonLabel.setUIID("labelDefault");

        chooseBtn = new Button("Choisir");
        chooseBtn.addActionListener(l -> {
            Manage.selectedPanier = panier;
            ((Manage) previousForm).refreshPanier();
            previousForm.showBack();
        });

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");
        btnsContainer.add(BorderLayout.CENTER, chooseBtn);

        panierModel.addAll(
                livraisonLabel, descriptionLabel,
                btnsContainer
        );

        return panierModel;
    }
}
