package com.khedmetna.gui.back.commande;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.khedmetna.entities.Commande;
import com.khedmetna.services.CommandeService;

public class Manage extends Form {

    Commande currentCommande;

    Label dateLabel, etatLabel, commentaireLabel, adresseLabel, totalcostLabel, productLabel;
    TextField etatTF,
            commentaireTF,
            adresseTF,
            totalcostTF,
            productTF;
    PickerComponent dateTF;

    Button manageButton;

    Form previous;

    public Manage(Form previous) {
        super(DisplayAll.currentCommande == null ? "Ajouter" : "Modifier", new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;

        currentCommande = DisplayAll.currentCommande;

        addGUIs();
        addActions();

        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    private void addGUIs() {

        dateTF = PickerComponent.createDate(null).label("Date");

        etatLabel = new Label("Etat : ");
        etatLabel.setUIID("labelDefault");
        etatTF = new TextField();
        etatTF.setHint("Tapez le etat");

        commentaireLabel = new Label("Commentaire : ");
        commentaireLabel.setUIID("labelDefault");
        commentaireTF = new TextField();
        commentaireTF.setHint("Tapez le commentaire");

        adresseLabel = new Label("Adresse : ");
        adresseLabel.setUIID("labelDefault");
        adresseTF = new TextField();
        adresseTF.setHint("Tapez le adresse");

        totalcostLabel = new Label("Totalcost : ");
        totalcostLabel.setUIID("labelDefault");
        totalcostTF = new TextField();
        totalcostTF.setHint("Tapez le totalcost");

        productLabel = new Label("Product : ");
        productLabel.setUIID("labelDefault");
        productTF = new TextField();
        productTF.setHint("Tapez le product");

        if (currentCommande == null) {

            manageButton = new Button("Ajouter");
        } else {

            dateTF.getPicker().setDate(currentCommande.getDate());
            etatTF.setText(currentCommande.getEtat());
            commentaireTF.setText(currentCommande.getCommentaire());
            adresseTF.setText(currentCommande.getAdresse());
            totalcostTF.setText(currentCommande.getTotalcost());
            productTF.setText(currentCommande.getProduct());

            manageButton = new Button("Modifier");
        }
        manageButton.setUIID("buttonMain");

        Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        container.setUIID("containerRounded");

        container.addAll(
                dateTF, etatLabel, etatTF, commentaireLabel, commentaireTF, adresseLabel, adresseTF, totalcostLabel, totalcostTF, productLabel, productTF,
                manageButton
        );

        this.addAll(container);
    }

    private void addActions() {

        if (currentCommande == null) {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = CommandeService.getInstance().add(
                            new Commande(
                                    dateTF.getPicker().getDate(),
                                    etatTF.getText(),
                                    commentaireTF.getText(),
                                    adresseTF.getText(),
                                    totalcostTF.getText(),
                                    productTF.getText()
                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Commande ajouté avec succes", new Command("Ok"));
                        showBackAndRefresh();
                    } else {
                        Dialog.show("Erreur", "Erreur d'ajout de commande. Code d'erreur : " + responseCode, new Command("Ok"));
                    }
                }
            });
        } else {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = CommandeService.getInstance().edit(
                            new Commande(
                                    currentCommande.getId(),
                                    dateTF.getPicker().getDate(),
                                    etatTF.getText(),
                                    commentaireTF.getText(),
                                    adresseTF.getText(),
                                    totalcostTF.getText(),
                                    productTF.getText()
                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Commande modifié avec succes", new Command("Ok"));
                        showBackAndRefresh();
                    } else {
                        Dialog.show("Erreur", "Erreur de modification de commande. Code d'erreur : " + responseCode, new Command("Ok"));
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

        if (etatTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le etat", new Command("Ok"));
            return false;
        }

        if (commentaireTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le commentaire", new Command("Ok"));
            return false;
        }

        if (adresseTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le adresse", new Command("Ok"));
            return false;
        }

        if (totalcostTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le totalcost", new Command("Ok"));
            return false;
        }

        if (productTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le product", new Command("Ok"));
            return false;
        }

        return true;
    }
}
