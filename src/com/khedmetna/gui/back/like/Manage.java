package com.khedmetna.gui.back.like;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.khedmetna.entities.Commentaire;
import com.khedmetna.entities.Like;
import com.khedmetna.services.LikeService;

public class Manage extends Form {

    public static Commentaire selectedCommentaire;
    Like currentLike;
    Label nomLabel, rateLabel, noteLabel, commentaireLabel;
    TextField nomTF,
            rateTF,
            noteTF,
            commentaireTF;

    Label selectedCommentaireLabel;
    Button selectCommentaireButton;

    Button manageButton;

    Form previous;

    public Manage(Form previous) {
        super(DisplayAll.currentLike == null ? "Ajouter" : "Modifier", new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;

        selectedCommentaire = null;

        currentLike = DisplayAll.currentLike;

        addGUIs();
        addActions();

        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    public void refreshCommentaire() {
        selectedCommentaireLabel.setText(selectedCommentaire.getDescription());
        this.refreshTheme();
    }

    private void addGUIs() {

        nomLabel = new Label("Nom : ");
        nomLabel.setUIID("labelDefault");
        nomTF = new TextField();
        nomTF.setHint("Tapez le nom");

        rateLabel = new Label("Rate : ");
        rateLabel.setUIID("labelDefault");
        rateTF = new TextField();
        rateTF.setHint("Tapez le rate");

        noteLabel = new Label("Note : ");
        noteLabel.setUIID("labelDefault");
        noteTF = new TextField();
        noteTF.setHint("Tapez le note");

        commentaireLabel = new Label("Commentaire : ");
        commentaireLabel.setUIID("labelDefault");
        commentaireTF = new TextField();
        commentaireTF.setHint("Tapez le commentaire");

        commentaireLabel = new Label("commentaire : ");
        commentaireLabel.setUIID("labelDefault");
        selectedCommentaireLabel = new Label("null");
        selectCommentaireButton = new Button("Choisir commentaire");
        selectCommentaireButton.addActionListener(l -> new ChooseCommentaire(this).show());

        if (currentLike == null) {

            manageButton = new Button("Ajouter");
        } else {

            nomTF.setText(currentLike.getNom());
            rateTF.setText(String.valueOf(currentLike.getRate()));
            noteTF.setText(String.valueOf(currentLike.getNote()));

            selectedCommentaire = currentLike.getCommentaire();

            commentaireLabel = new Label("commentaire : ");
            commentaireLabel.setUIID("labelDefault");
            selectedCommentaireLabel = new Label("null");
            selectedCommentaireLabel.setText(selectedCommentaire.getDescription());

            manageButton = new Button("Modifier");
        }
        manageButton.setUIID("buttonMain");

        Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        container.setUIID("containerRounded");

        container.addAll(
                nomLabel, nomTF,
                rateLabel, rateTF,
                noteLabel, noteTF,
                commentaireLabel,
                selectedCommentaireLabel, selectCommentaireButton,
                manageButton
        );

        this.addAll(container);
    }

    private void addActions() {

        if (currentLike == null) {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = LikeService.getInstance().add(
                            new Like(
                                    nomTF.getText(),
                                    (int) Float.parseFloat(rateTF.getText()),
                                    (int) Float.parseFloat(noteTF.getText()),
                                    selectedCommentaire
                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Like ajouté avec succes", new Command("Ok"));
                        showBackAndRefresh();
                    } else {
                        Dialog.show("Erreur", "Erreur d'ajout de like. Code d'erreur : " + responseCode, new Command("Ok"));
                    }
                }
            });
        } else {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = LikeService.getInstance().edit(
                            new Like(
                                    currentLike.getId(),
                                    nomTF.getText(),
                                    (int) Float.parseFloat(rateTF.getText()),
                                    (int) Float.parseFloat(noteTF.getText()),
                                    selectedCommentaire
                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Like modifié avec succes", new Command("Ok"));
                        showBackAndRefresh();
                    } else {
                        Dialog.show("Erreur", "Erreur de modification de like. Code d'erreur : " + responseCode, new Command("Ok"));
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

        if (nomTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le nom", new Command("Ok"));
            return false;
        }

        if (rateTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le rate", new Command("Ok"));
            return false;
        }
        try {
            Float.parseFloat(rateTF.getText());
        } catch (NumberFormatException e) {
            Dialog.show("Avertissement", rateTF.getText() + " n'est pas un nombre valide", new Command("Ok"));
            return false;
        }

        if (noteTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le note", new Command("Ok"));
            return false;
        }
        try {
            Float.parseFloat(noteTF.getText());
        } catch (NumberFormatException e) {
            Dialog.show("Avertissement", noteTF.getText() + " n'est pas un nombre valide", new Command("Ok"));
            return false;
        }

        if (selectedCommentaire == null) {
            Dialog.show("Avertissement", "Veuillez choisir un commentaire", new Command("Ok"));
            return false;
        }

        return true;
    }
}
