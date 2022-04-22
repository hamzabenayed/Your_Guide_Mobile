package com.khedmetna.gui.back.reclamation;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.khedmetna.entities.Reclamation;
import com.khedmetna.services.ReclamationService;

public class Manage extends Form {

    Reclamation currentReclamation;

    Label dateLabel, typeLabel, etatLabel, descriptionLabel;
    TextField typeTF,
            etatTF,
            descriptionTF;
    PickerComponent dateTF;

    Button manageButton;

    Form previous;

    public Manage(Form previous) {
        super(DisplayAll.currentReclamation == null ? "Ajouter" : "Modifier", new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;

        currentReclamation = DisplayAll.currentReclamation;

        addGUIs();
        addActions();

        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    private void addGUIs() {

        dateTF = PickerComponent.createDate(null).label("Date");

        typeLabel = new Label("Type : ");
        typeLabel.setUIID("labelDefault");
        typeTF = new TextField();
        typeTF.setHint("Tapez le type");

        etatLabel = new Label("Etat : ");
        etatLabel.setUIID("labelDefault");
        etatTF = new TextField();
        etatTF.setHint("Tapez le etat");

        descriptionLabel = new Label("Description : ");
        descriptionLabel.setUIID("labelDefault");
        descriptionTF = new TextField();
        descriptionTF.setHint("Tapez le description");

        manageButton = new Button("Ajouter ");

        Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        container.setUIID("containerRounded");

        container.addAll(
                dateTF, typeLabel, typeTF, etatLabel, etatTF, descriptionLabel, descriptionTF,
                manageButton
        );

        this.addAll(container);
    }

    private void addActions() {

        manageButton.addActionListener(action -> {
            if (controleDeSaisie()) {
                int responseCode = ReclamationService.getInstance().add(
                        new Reclamation(
                                dateTF.getPicker().getDate(),
                                typeTF.getText(),
                                etatTF.getText(),
                                descriptionTF.getText()
                        )
                );
                if (responseCode == 200) {
                    Dialog.show("Succés", "Reclamation ajouté avec succes", new Command("Ok"));
                    showBackAndRefresh();
                } else {
                    Dialog.show("Erreur", "Erreur d'ajout de reclamation. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            }
        });
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

        if (typeTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le type", new Command("Ok"));
            return false;
        }

        if (etatTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le etat", new Command("Ok"));
            return false;
        }

        if (descriptionTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le description", new Command("Ok"));
            return false;
        }

        return true;
    }
}
