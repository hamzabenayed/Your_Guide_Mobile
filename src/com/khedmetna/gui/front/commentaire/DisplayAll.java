package com.khedmetna.gui.front.commentaire;

import com.codename1.components.InteractionDialog;
import com.codename1.components.ToastBar;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.khedmetna.MainApp;
import com.khedmetna.entities.Commentaire;
import com.khedmetna.entities.Like;
import com.khedmetna.services.CommentaireService;
import com.khedmetna.services.LikeService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DisplayAll extends Form {

    public static Commentaire currentCommentaire = null;
    Button addBtn;
    Label descriptionLabel, dateLabel;
    Button editBtn, deleteBtn;
    Container btnsContainer;

    public DisplayAll(Form previous) {
        super("Commentaires", new BoxLayout(BoxLayout.Y_AXIS));
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
            for (Commentaire commentaire : listCommentaires) {
                Component model = makeCommentaireModel(commentaire);
                this.add(model);
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }

        TextField textField = new TextField("", "Tapez la description du commentaire");

        addBtn = new Button("Nouveau commentaire");
        addBtn.setUIID("buttonWhiteCenter");
        addBtn.addActionListener(action -> {
            Commentaire commentaire = new Commentaire(
                    textField.getText(),
                    null,
                    null
            );
            CommentaireService.getInstance().add(commentaire);

            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                @Override
                public void run() {
                    ToastBar.getInstance().setPosition(TOP);
                    ToastBar.Status status = ToastBar.getInstance().createStatus();
                    status.setShowProgressIndicator(false);
                    status.setMessage("Commentaire ajouté avec succes" + MainApp.getSession().getNom());
                    status.setExpires(5000);
                    status.show();
                }
            },
                    2000
            );

            this.refresh();
        });
        this.add(textField);
        this.add(addBtn);
    }

    private Component makeCommentaireModel(Commentaire commentaire) {
        Container commentaireModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        commentaireModel.setUIID("containerRounded");

        descriptionLabel = new Label("Description : " + commentaire.getDescription());
        descriptionLabel.setUIID("labelDefault");

        dateLabel = new Label("Date : " + new SimpleDateFormat("dd-MM-yyyy").format(commentaire.getDate()));
        dateLabel.setUIID("labelDefault");

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");

        Button likeButton = new Button(FontImage.MATERIAL_FAVORITE_OUTLINE);

        int count = 0;
        for (Like like : commentaire.getLikes()) {
            if (like.getRate() == MainApp.getSession().getId()) {
                likeButton.setMaterialIcon(FontImage.MATERIAL_FAVORITE);
            }
            count++;
        }

        likeButton.setText(count + "");

        final int[] countFinal = {count};
        likeButton.addActionListener(l -> {
            int s = LikeService.getInstance().likeDislike(new Like(
                    "nom",
                    MainApp.getSession().getId(),
                    1,
                    commentaire
            ));

            if (s == 200) {
                countFinal[0] += 1;
                likeButton.setMaterialIcon(FontImage.MATERIAL_FAVORITE);
            } else {
                countFinal[0] -= 1;
                likeButton.setMaterialIcon(FontImage.MATERIAL_FAVORITE_OUTLINE);
            }
            likeButton.setText(String.valueOf(countFinal[0]));
            this.revalidate();
        });

        editBtn = new Button("Modifier");
        editBtn.setUIID("buttonMain");
        editBtn.addActionListener(action -> {
            currentCommentaire = commentaire;
            new Manage(this).show();
        });

        deleteBtn = new Button(FontImage.MATERIAL_DELETE);
        deleteBtn.setUIID("buttonDanger");
        deleteBtn.addActionListener(action -> {
            InteractionDialog dlg = new InteractionDialog("Confirmer la suppression");
            dlg.setLayout(new BorderLayout());
            dlg.add(BorderLayout.CENTER, new Label("Voulez vous vraiment supprimer ce commentaire ?"));
            Button btnClose = new Button("Annuler");
            btnClose.addActionListener((ee) -> dlg.dispose());
            Button btnConfirm = new Button("Confirmer");
            btnConfirm.addActionListener(actionConf -> {
                int responseCode = CommentaireService.getInstance().delete(commentaire.getId());

                if (responseCode == 200) {
                    currentCommentaire = null;
                    dlg.dispose();
                    commentaireModel.remove();
                    this.refreshTheme();
                } else {
                    Dialog.show("Erreur", "Erreur de suppression du commentaire. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            });
            Container btnContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            btnContainer.addAll(btnClose, btnConfirm);
            dlg.addComponent(BorderLayout.SOUTH, btnContainer);
            dlg.show(800, 800, 0, 0);
        });

        btnsContainer.add(BorderLayout.WEST, editBtn);
        btnsContainer.add(BorderLayout.CENTER, deleteBtn);
        btnsContainer.add(BorderLayout.EAST, likeButton);

        commentaireModel.addAll(
                dateLabel, descriptionLabel,
                btnsContainer
        );

        return commentaireModel;
    }

}
