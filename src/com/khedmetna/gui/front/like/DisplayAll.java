package com.khedmetna.gui.front.like;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.khedmetna.entities.Like;
import com.khedmetna.services.LikeService;

import java.util.ArrayList;

public class DisplayAll extends Form {

    public static Like currentLike = null;

    Label nomLabel, rateLabel, noteLabel, commentaireLabel;
    Container btnsContainer;

    public DisplayAll(Form previous) {
        super("Likes", new BoxLayout(BoxLayout.Y_AXIS));
        addGUIs();
        super.getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    private void addGUIs() {
        ArrayList<Like> listLikes = LikeService.getInstance().getAll();

        if (listLikes.size() > 0) {
            for (Like like : listLikes) {
                Component model = makeLikeModel(like);
                this.add(model);
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    private Component makeLikeModel(Like like) {
        Container likeModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        likeModel.setUIID("containerRounded");

        nomLabel = new Label("Nom : " + like.getNom());

        nomLabel.setUIID("labelDefault");

        rateLabel = new Label("Rate : " + like.getRate());

        rateLabel.setUIID("labelDefault");

        noteLabel = new Label("Note : " + like.getNote());

        noteLabel.setUIID("labelDefault");

        commentaireLabel = new Label("Commentaire : " + like.getCommentaire());

        commentaireLabel.setUIID("labelDefault");

        commentaireLabel = new Label("Commentaire : " + like.getCommentaire().getDescription());
        commentaireLabel.setUIID("labelDefault");

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");

        likeModel.addAll(
                nomLabel, rateLabel, noteLabel, commentaireLabel,
                btnsContainer
        );

        return likeModel;
    }
}
