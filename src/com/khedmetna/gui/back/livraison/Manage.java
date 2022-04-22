package com.khedmetna.gui.back.livraison;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;

import com.khedmetna.entities.Panier;

import com.khedmetna.entities.Livraison;
import com.khedmetna.services.LivraisonService;

public class Manage extends Form {

    Livraison currentLivraison;

    public static Panier selectedPanier;

    Label dateLabel, nomLivreurLabel, prenomLivreurLabel, telLivreurLabel, adresseLivraisonLabel, panierLabel;
    TextField nomLivreurTF,
            prenomLivreurTF,
            telLivreurTF,
            adresseLivraisonTF,
            panierTF, elemTF;
    PickerComponent dateTF;

    Label selectedPanierLabel;
    Button selectPanierButton;

    Button manageButton;

    Form previous;

    public Manage(Form previous) {
        super(DisplayAll.currentLivraison == null ? "Ajouter" : "Modifier", new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;

        selectedPanier = null;

        currentLivraison = DisplayAll.currentLivraison;

        addGUIs();
        addActions();

        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    public void refreshPanier() {
        selectedPanierLabel.setText(selectedPanier.getDescription());
        selectPanierButton.setText("Modifier le panier");
        this.refreshTheme();
    }

    private void addGUIs() {

        dateTF = PickerComponent.createDate(null).label("Date");

        nomLivreurLabel = new Label("NomLivreur : ");
        nomLivreurLabel.setUIID("labelDefault");
        nomLivreurTF = new TextField();
        nomLivreurTF.setHint("Tapez le nomLivreur");

        prenomLivreurLabel = new Label("PrenomLivreur : ");
        prenomLivreurLabel.setUIID("labelDefault");
        prenomLivreurTF = new TextField();
        prenomLivreurTF.setHint("Tapez le prenomLivreur");

        telLivreurLabel = new Label("TelLivreur : ");
        telLivreurLabel.setUIID("labelDefault");
        telLivreurTF = new TextField();
        telLivreurTF.setHint("Tapez le telLivreur");

        adresseLivraisonLabel = new Label("AdresseLivraison : ");
        adresseLivraisonLabel.setUIID("labelDefault");
        adresseLivraisonTF = new TextField();
        adresseLivraisonTF.setHint("Tapez le adresseLivraison");

        panierLabel = new Label("Panier : ");
        panierLabel.setUIID("labelDefault");
        panierTF = new TextField();
        panierTF.setHint("Tapez le panier");

        panierLabel = new Label("panier : ");
        panierLabel.setUIID("labelDefault");
        selectedPanierLabel = new Label("null");
        selectPanierButton = new Button("Choisir panier");
        selectPanierButton.addActionListener(l -> new ChoosePanier(this).show());

        if (currentLivraison == null) {

            manageButton = new Button("Ajouter");
        } else {

            dateTF.getPicker().setDate(currentLivraison.getDate());
            nomLivreurTF.setText(currentLivraison.getNomLivreur());
            prenomLivreurTF.setText(currentLivraison.getPrenomLivreur());
            telLivreurTF.setText(currentLivraison.getTelLivreur());
            adresseLivraisonTF.setText(currentLivraison.getAdresseLivraison());

            selectedPanier = currentLivraison.getPanier();

            panierLabel = new Label("panier : ");
            panierLabel.setUIID("labelDefault");
            selectedPanierLabel = new Label("null");
            selectedPanierLabel.setText(selectedPanier.getDescription());
            selectPanierButton.setText("Modifier le panier");

            manageButton = new Button("Modifier");
        }
        manageButton.setUIID("buttonMain");

        Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        container.setUIID("containerRounded");

        container.addAll(
                dateTF,
                nomLivreurLabel, nomLivreurTF,
                prenomLivreurLabel, prenomLivreurTF,
                telLivreurLabel, telLivreurTF,
                adresseLivraisonLabel, adresseLivraisonTF,
                panierLabel,
                selectedPanierLabel, selectPanierButton,
                manageButton
        );

        this.addAll(container);
    }

    private void addActions() {

        if (currentLivraison == null) {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = LivraisonService.getInstance().add(
                            new Livraison(
                                    dateTF.getPicker().getDate(),
                                    nomLivreurTF.getText(),
                                    prenomLivreurTF.getText(),
                                    telLivreurTF.getText(),
                                    adresseLivraisonTF.getText(),
                                    selectedPanier
                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Livraison ajouté avec succes", new Command("Ok"));
                        showBackAndRefresh();
                    } else {
                        Dialog.show("Erreur", "Erreur d'ajout de livraison. Code d'erreur : " + responseCode, new Command("Ok"));
                    }
                }
            });
        } else {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = LivraisonService.getInstance().edit(
                            new Livraison(
                                    currentLivraison.getId(),
                                    dateTF.getPicker().getDate(),
                                    nomLivreurTF.getText(),
                                    prenomLivreurTF.getText(),
                                    telLivreurTF.getText(),
                                    adresseLivraisonTF.getText(),
                                    selectedPanier
                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Livraison modifié avec succes", new Command("Ok"));
                        showBackAndRefresh();
                    } else {
                        Dialog.show("Erreur", "Erreur de modification de livraison. Code d'erreur : " + responseCode, new Command("Ok"));
                    }
                }
            });
        }
    }

    private void showBackAndRefresh() {
        ((DisplayAll) previous).refresh();
        previous.showBack();
    }

    private boolean controleDeSaisie() {

        if (dateTF.getPicker().getDate() == null) {
            Dialog.show("Avertissement", "Veuillez saisir la date", new Command("Ok"));
            return false;
        }

        if (nomLivreurTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le nomLivreur", new Command("Ok"));
            return false;
        }

        if (prenomLivreurTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le prenomLivreur", new Command("Ok"));
            return false;
        }

        if (telLivreurTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le telLivreur", new Command("Ok"));
            return false;
        }

        if (adresseLivraisonTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le adresseLivraison", new Command("Ok"));
            return false;
        }

        if (selectedPanier == null) {
            Dialog.show("Avertissement", "Veuillez choisir un panier", new Command("Ok"));
            return false;
        }

        return true;
    }
}
