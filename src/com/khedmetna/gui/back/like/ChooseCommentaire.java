package com.khedmetna.gui.back.like;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.khedmetna.entities.Commentaire;
import com.khedmetna.services.CommentaireService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ChooseCommentaire extends Form {

    Form previousForm;
    Label descriptionLabel, dateLabel;
    Button chooseBtn;
    Container btnsContainer;

    public ChooseCommentaire(Form previous) {
        super("Choisir un commentaire", new BoxLayout(BoxLayout.Y_AXIS));

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
        ArrayList<Commentaire> listCommentaires = CommentaireService.getInstance().getAll();
        if (listCommentaires.size() > 0) {
            for (Commentaire commentaires : listCommentaires) {
                this.add(makeCommentaireModel(commentaires));
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    private Component makeCommentaireModel(Commentaire commentaire) {
        Container commentaireModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        commentaireModel.setUIID("containerRounded");

        descriptionLabel = new Label("Description : " + commentaire.getDescription());
        descriptionLabel.setUIID("labelDefault");

        dateLabel = new Label("Date : " + new SimpleDateFormat("dd-MM-yyyy").format(commentaire.getDate()));
        dateLabel.setUIID("labelDefault");

        chooseBtn = new Button("Choisir");
        chooseBtn.addActionListener(l -> {
            Manage.selectedCommentaire = commentaire;
            ((Manage) previousForm).refreshCommentaire();
            previousForm.showBack();
        });

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");
        btnsContainer.add(BorderLayout.CENTER, chooseBtn);

        commentaireModel.addAll(
                descriptionLabel, dateLabel,
                btnsContainer
        );

        return commentaireModel;
    }
}
