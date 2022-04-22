package com.khedmetna.gui.front.user;

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

public class ModifierProfil extends Form {

    Resources theme = UIManager.initFirstTheme("/theme");
    String selectedImage;
    boolean imageEdited = false;

    User currentUser;

    Label loginLabel, mdpLabel, nomLabel, imageLabel;
    TextField loginTF,
            mdpTF,
            nomTF;

    ImageViewer imageIV;
    Button selectImageButton;

    Button manageButton;

    Form previous;

    public ModifierProfil(Form previous) {
        super("Modifier mon profil", new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;

        currentUser = Profil.currentUser;

        addGUIs();
        addActions();

        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    private void addGUIs() {

        loginLabel = new Label("Email : ");
        loginLabel.setUIID("labelDefault");
        loginTF = new TextField();
        loginTF.setHint("Tapez l'email");

        mdpLabel = new Label("Mot de passe : ");
        mdpLabel.setUIID("labelDefault");
        mdpTF = new TextField();
        mdpTF.setHint("Tapez le mot de passe");

        nomLabel = new Label("Nom : ");
        nomLabel.setUIID("labelDefault");
        nomTF = new TextField();
        nomTF.setHint("Tapez le nom");

        imageLabel = new Label("Image : ");
        imageLabel.setUIID("labelDefault");
        selectImageButton = new Button("Ajouter une image");

        loginTF.setText(currentUser.getLogin());
        nomTF.setText(currentUser.getNom());
        selectedImage = currentUser.getImage();

        if (currentUser.getImage() != null) {
            String url = Statics.USER_IMAGE_URL + currentUser.getImage();
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
        manageButton.setUIID("buttonMain");

        Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        container.setUIID("containerRounded");

        container.addAll(
                imageLabel, imageIV,
                selectImageButton,
                loginLabel, loginTF, mdpLabel, mdpTF, nomLabel, nomTF,
                manageButton
        );

        this.addAll(container);
    }

    private void addActions() {
        selectImageButton.addActionListener(a -> {
            selectedImage = Capture.capturePhoto(500, -1);
            try {
                imageEdited = true;
                imageIV.setImage(Image.createImage(selectedImage));
            } catch (IOException e) {
                e.printStackTrace();
            }
            selectImageButton.setText("Modifier l'image");
        });
        manageButton.addActionListener(action -> {
            if (controleDeSaisie()) {
                int responseCode = UserService.getInstance().edit(
                        new User(
                                currentUser.getId(),
                                loginTF.getText(),
                                "ROLE_USER",
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

    private void showBackAndRefresh() {
        ((Profil) previous).refresh();
        previous.showBack();
    }

    private boolean controleDeSaisie() {

        if (loginTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir l'email", new Command("Ok"));
            return false;
        }

        if (mdpTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le mot de passe", new Command("Ok"));
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
