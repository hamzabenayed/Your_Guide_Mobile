package com.khedmetna.gui.front.user;

import com.codename1.components.ImageViewer;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.khedmetna.MainApp;
import com.khedmetna.entities.User;
import com.khedmetna.services.UserService;
import com.khedmetna.utils.Statics;

public class Profil extends Form {

    public static User currentUser = null;

    Resources theme = UIManager.initFirstTheme("/theme");

    Label loginLabel, rolesLabel, mdpLabel, nomLabel;
    ImageViewer imageIV;
    Button editProfileBTN;

    public Profil(Form previous) {
        super("Mon profil", new BoxLayout(BoxLayout.Y_AXIS));

        currentUser = UserService.getInstance().getById(MainApp.getSession().getId());

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

        currentUser = UserService.getInstance().getById(MainApp.getSession().getId());
        this.add(makeUserModel(currentUser));

        editProfileBTN = new Button("Modifier mon profil");
        this.add(editProfileBTN);
    }

    private void addActions() {
        editProfileBTN.addActionListener(action -> {
            new ModifierProfil(this).show();
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

        userModel.addAll(
                imageIV,
                loginLabel, rolesLabel, mdpLabel, nomLabel
        );

        return userModel;
    }
}
