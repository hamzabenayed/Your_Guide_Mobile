package com.khedmetna.gui.back.user;

import com.codename1.capture.Capture;
import com.codename1.components.ImageViewer;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.khedmetna.entities.User;
import com.khedmetna.services.UserService;
import com.khedmetna.utils.Statics;

import java.io.IOException;

public class Manage extends Form {

    Resources theme = UIManager.initFirstTheme("/theme");
    String selectedImage;
    boolean imageEdited = false;

    User currentUser;

    Label loginLabel, rolesLabel, mdpLabel, nomLabel, imageLabel;
    TextField loginTF,
            rolesTF,
            mdpTF,
            nomTF;

    ImageViewer imageIV;
    Button selectImageButton;

    Button manageButton;

    Form previous;

    public Manage(Form previous) {
        super(DisplayAll.currentUser == null ? "Ajouter" : "Modifier", new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;

        currentUser = DisplayAll.currentUser;

        addGUIs();
        addActions();

        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    private void addGUIs() {

        loginLabel = new Label("Email : ");
        loginLabel.setUIID("labelDefault");
        loginTF = new TextField();
        loginTF.setHint("Tapez l'email");

        rolesLabel = new Label("Roles : ");
        rolesLabel.setUIID("labelDefault");
        rolesTF = new TextField();
        rolesTF.setHint("Tapez le roles");

        mdpLabel = new Label("Mot de passe : ");
        mdpLabel.setUIID("labelDefault");
        mdpTF = new TextField();
        mdpTF.setHint("Tapez le Mot de passe");

        nomLabel = new Label("Nom : ");
        nomLabel.setUIID("labelDefault");
        nomTF = new TextField();
        nomTF.setHint("Tapez le nom");

        imageLabel = new Label("Image : ");
        imageLabel.setUIID("labelDefault");
        selectImageButton = new Button("Ajouter une image");

        if (currentUser == null) {

            imageIV = new ImageViewer(theme.getImage("default.jpg").fill(500, 500));

            manageButton = new Button("Ajouter");
        } else {

            loginTF.setText(currentUser.getLogin());
            rolesTF.setText(currentUser.getRoles());
            mdpTF.setText(currentUser.getMdp());
            nomTF.setText(currentUser.getNom());

            if (currentUser.getImage() != null) {
                String url = Statics.USER_IMAGE_URL + currentUser.getImage();
                selectedImage = currentUser.getImage();
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

            manageButton = new Button("Modifier");
        }
        manageButton.setUIID("buttonMain");

        Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        container.setUIID("containerRounded");

        container.addAll(
                imageLabel, imageIV,
                selectImageButton,
                loginLabel, loginTF, rolesLabel, rolesTF, mdpLabel, mdpTF, nomLabel, nomTF,
                manageButton
        );

        this.addAll(container);
    }

    private void addActions() {

        selectImageButton.addActionListener(a -> {
            selectedImage = Capture.capturePhoto(900, -1);
            try {
                imageEdited = true;
                imageIV.setImage(Image.createImage(selectedImage));
            } catch (IOException e) {
                e.printStackTrace();
            }
            selectImageButton.setText("Modifier l'image");
        });

        if (currentUser == null) {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = UserService.getInstance().add(
                            new User(
                                    loginTF.getText(),
                                    rolesTF.getText(),
                                    mdpTF.getText(),
                                    nomTF.getText(),
                                    selectedImage
                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "User ajouté avec succes", new Command("Ok"));
                        showBackAndRefresh();
                    } else {
                        Dialog.show("Erreur", "Erreur d'ajout de user. Code d'erreur : " + responseCode, new Command("Ok"));
                    }
                }
            });
        } else {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = UserService.getInstance().edit(
                            new User(
                                    currentUser.getId(),
                                    loginTF.getText(),
                                    rolesTF.getText(),
                                    mdpTF.getText(),
                                    nomTF.getText(),
                                    selectedImage
                            ), imageEdited
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "User modifié avec succes", new Command("Ok"));
                        showBackAndRefresh();
                    } else {
                        Dialog.show("Erreur", "Erreur de modification de user. Code d'erreur : " + responseCode, new Command("Ok"));
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

        if (loginTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir l'email", new Command("Ok"));
            return false;
        }

        if (rolesTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le roles", new Command("Ok"));
            return false;
        }

        if (mdpTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le Mot de passe", new Command("Ok"));
            return false;
        }

        if (nomTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le nom", new Command("Ok"));
            return false;
        }

        if (selectedImage == null) {
            Dialog.show("Avertissement", "Veuillez choisir une image", new Command("Ok"));
            return false;
        }

        return true;
    }
}
