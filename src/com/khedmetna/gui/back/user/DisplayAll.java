package com.khedmetna.gui.back.user;

import com.codename1.components.ImageViewer;
import com.codename1.components.InteractionDialog;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.khedmetna.entities.User;
import com.khedmetna.services.UserService;
import com.khedmetna.utils.Statics;

import java.util.ArrayList;

public class DisplayAll extends Form {

    public static User currentUser = null;
    Resources theme = UIManager.initFirstTheme("/theme");
    Button addBtn;
    Label loginLabel, rolesLabel, mdpLabel, nomLabel;
    ImageViewer imageIV;
    Button editBtn, deleteBtn;
    Container btnsContainer;

    public DisplayAll(Form previous) {
        super("Users", new BoxLayout(BoxLayout.Y_AXIS));

        addGUIs();
        addActions();

        super.getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    public void refresh() {
        this.removeAll();
        addGUIs();
        addActions();
        this.refreshTheme();
    }

    private void addGUIs() {
        addBtn = new Button("Ajouter");
        addBtn.setUIID("buttonWhiteCenter");

        this.add(addBtn);

        ArrayList<User> listUsers = UserService.getInstance().getAll();
        if (listUsers.size() > 0) {
            for (User listUser : listUsers) {
                this.add(makeUserModel(listUser));
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    private void addActions() {
        addBtn.addActionListener(action -> {
            currentUser = null;
            new Manage(this).show();
        });
    }

    private Component makeUserModel(User user) {
        Container userModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        userModel.setUIID("containerRounded");

        loginLabel = new Label("Email : " + user.getLogin());
        loginLabel.setUIID("labelDefault");

        rolesLabel = new Label("Roles : " + user.getRoles());
        rolesLabel.setUIID("labelDefault");

        mdpLabel = new Label("Mot de passe : " + user.getMdp());

        mdpLabel.setUIID("labelDefault");

        nomLabel = new Label("Nom : " + user.getNom());

        nomLabel.setUIID("labelDefault");

        if (user.getImage() != null) {
            String url = Statics.USER_IMAGE_URL + user.getImage();
            Image image = URLImage.createToStorage(
                    EncodedImage.createFromImage(theme.getImage("default.jpg").fill(500, 500), false),
                    url,
                    url,
                    URLImage.RESIZE_SCALE
            );
            imageIV = new ImageViewer(image);
        } else {
            imageIV = new ImageViewer(theme.getImage("default.jpg").fill(500, 500));
        }
        imageIV.setFocusable(false);

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");

        editBtn = new Button("Modifier");
        editBtn.setUIID("buttonMain");
        editBtn.addActionListener(action -> {
            currentUser = user;
            new Manage(this).show();
        });

        deleteBtn = new Button("Supprimer");
        deleteBtn.setUIID("buttonDanger");
        deleteBtn.addActionListener(action -> {
            InteractionDialog dlg = new InteractionDialog("Confirmer la suppression");
            dlg.setLayout(new BorderLayout());
            dlg.add(BorderLayout.CENTER, new Label("Voulez vous vraiment supprimer ce user ?"));
            Button btnClose = new Button("Annuler");
            btnClose.addActionListener((ee) -> dlg.dispose());
            Button btnConfirm = new Button("Confirmer");
            btnConfirm.addActionListener(actionConf -> {
                int responseCode = UserService.getInstance().delete(user.getId());

                if (responseCode == 200) {
                    currentUser = null;
                    dlg.dispose();
                    userModel.remove();
                    this.refreshTheme();
                } else {
                    Dialog.show("Erreur", "Erreur de suppression du user. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            });
            Container btnContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            btnContainer.addAll(btnClose, btnConfirm);
            dlg.addComponent(BorderLayout.SOUTH, btnContainer);
            dlg.show(800, 800, 0, 0);
        });

        btnsContainer.add(BorderLayout.CENTER, editBtn);
        btnsContainer.add(BorderLayout.EAST, deleteBtn);

        userModel.addAll(
                imageIV,
                loginLabel, rolesLabel, mdpLabel, nomLabel,
                btnsContainer
        );

        return userModel;
    }
}
