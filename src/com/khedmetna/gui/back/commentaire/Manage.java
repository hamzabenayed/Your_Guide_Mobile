package com.khedmetna.gui.back.commentaire;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.khedmetna.entities.Commentaire;
import com.khedmetna.services.CommentaireService;

public class Manage extends Form {

    Commentaire currentCommentaire;

    Label descriptionLabel, dateLabel;
    TextField descriptionTF;
    PickerComponent dateTF;

    Button manageButton;

    Form previous;

    public Manage(Form previous) {
        super(com.khedmetna.gui.back.commentaire.DisplayAll.currentCommentaire == null ? "Ajouter" : "Modifier", new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;

        currentCommentaire = com.khedmetna.gui.back.commentaire.DisplayAll.currentCommentaire;

        addGUIs();
        addActions();

        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    private void addGUIs() {

        descriptionLabel = new Label("Description : ");
        descriptionLabel.setUIID("labelDefault");
        descriptionTF = new TextField();
        descriptionTF.setHint("Tapez le description");
        dateTF = PickerComponent.createDate(null).label("Date");

        if (currentCommentaire == null) {
            manageButton = new Button("Ajouter");
        } else {
            descriptionTF.setText(currentCommentaire.getDescription());
            dateTF.getPicker().setDate(currentCommentaire.getDate());
            manageButton = new Button("Modifier");
        }
        manageButton.setUIID("buttonMain");

        Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        container.setUIID("containerRounded");

        container.addAll(
                descriptionLabel, descriptionTF, dateTF,
                manageButton
        );

        this.addAll(container);
    }

    private void addActions() {

        if (currentCommentaire == null) {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = CommentaireService.getInstance().add(
                            new Commentaire(
                                    descriptionTF.getText(),
                                    dateTF.getPicker().getDate(),
                                    null
                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Commentaire ajouté avec succes", new Command("Ok"));
                        showBackAndRefresh();
                    } else {
                        Dialog.show("Erreur", "Erreur d'ajout de commentaire. Code d'erreur : " + responseCode, new Command("Ok"));
                    }
                }
            });
        } else {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = CommentaireService.getInstance().edit(
                            new Commentaire(
                                    currentCommentaire.getId(),
                                    descriptionTF.getText(),
                                    dateTF.getPicker().getDate(),
                                    null
                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Commentaire modifié avec succes", new Command("Ok"));
                        showBackAndRefresh();
                    } else {
                        Dialog.show("Erreur", "Erreur de modification de commentaire. Code d'erreur : " + responseCode, new Command("Ok"));
                    }
                }
            });
        }
    }

    private void showBackAndRefresh() {
        ((com.khedmetna.gui.back.commentaire.DisplayAll) previous).refresh();
        previous.showBack();
    }

    private boolean controleDeSaisie() {

        if (descriptionTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le description", new Command("Ok"));
            return false;
        }

        if (dateTF.getPicker().getDate() == null) {
            Dialog.show("Avertissement", "Veuillez saisir la date", new Command("Ok"));
            return false;
        }

        return true;
    }
}
