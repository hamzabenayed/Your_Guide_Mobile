package com.khedmetna.gui;

import com.codename1.capture.Capture;
import com.codename1.components.ImageViewer;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.khedmetna.entities.User;
import com.khedmetna.services.UserService;

import java.io.IOException;

public class RegisterForm extends Form {

    Resources theme = UIManager.initFirstTheme("/theme");
    String selectedImage;

    Label emailLabel, mdpLabel, passwordConfirmationLabel, nomLabel, imageLabel, loginLabel;
    TextField loginTF;
    TextField mdpTF;
    TextField passwordConfirmationTF;
    TextField nomTF;

    ImageViewer imageIV;
    Button selectImageButton;

    Button registerButton, loginButton;

    public RegisterForm() {
        super("Inscription", new BoxLayout(BoxLayout.Y_AXIS));
        addGUIs();
        addActions();
    }

    private void addGUIs() {

        emailLabel = new Label("Email : ");
        emailLabel.setUIID("labelDefault");
        loginTF = new TextField();
        loginTF.setHint("Tapez le email");

        nomLabel = new Label("Nom : ");
        nomLabel.setUIID("labelDefault");
        nomTF = new TextField();
        nomTF.setHint("Tapez le nom");

        mdpLabel = new Label("Mot de passe : ");
        mdpLabel.setUIID("labelDefault");
        mdpTF = new TextField("", "Tapez votre mot de passe", 20, TextField.PASSWORD);

        passwordConfirmationLabel = new Label("Confirmation du mot de passe : ");
        passwordConfirmationLabel.setUIID("labelDefault");
        passwordConfirmationTF = new TextField("", "Retapez votre mot de passe", 20, TextField.PASSWORD);

        imageLabel = new Label("Image : ");
        imageLabel.setUIID("labelDefault");
        selectImageButton = new Button("Ajouter une image");

        registerButton = new Button("S'inscrire");
        registerButton.setUIID("buttonMain");

        loginLabel = new Label("Vous avez deja un compte ?");
        loginLabel.setUIID("labelCenterSmall");
        loginButton = new Button("Connexion");

        imageIV = new ImageViewer(theme.getImage("default.jpg").fill(500, 500));

        this.addAll(
                emailLabel, loginTF,
                nomLabel, nomTF,
                mdpLabel, mdpTF,
                passwordConfirmationLabel, passwordConfirmationTF,
                imageLabel, imageIV,
                selectImageButton,
                registerButton,
                loginLabel, loginButton
        );
    }

    private void addActions() {
        selectImageButton.addActionListener(a -> {
            selectedImage = Capture.capturePhoto(900, -1);
            try {
                imageIV.setImage(Image.createImage(selectedImage));
            } catch (IOException e) {
                e.printStackTrace();
            }
            selectImageButton.setText("Modifier l'image");
        });

        registerButton.addActionListener(l -> {
            if (controleDeSaisie()) {
                int responseCode = UserService.getInstance().add(
                        new User(
                                loginTF.getText(),
                                "ROLE_USER",
                                mdpTF.getText(),
                                nomTF.getText(),
                                selectedImage
                        )
                );
                if (responseCode == 200) {
                    Dialog.show("Succés", "Inscription effectué avec succes", new Command("Ok"));
                    LoginForm.loginForm.showBack();
                } else if (responseCode == 203) {
                    Dialog.show("Erreur", "Cet email existe deja ", new Command("Ok"));
                } else {
                    Dialog.show("Erreur", "Erreur d'ajout de user. Code d'erreur : " + responseCode, new Command("Ok"));
                    LoginForm.loginForm.showBack();
                }
            }
        });

        loginButton.addActionListener(l -> LoginForm.loginForm.showBack());
    }

    private boolean controleDeSaisie() {

        if (loginTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir l'email", new Command("Ok"));
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

        if (passwordConfirmationLabel.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir la confirmation du mot de passe", new Command("Ok"));
            return false;
        }

        if (!passwordConfirmationTF.getText().equals(mdpTF.getText())) {
            Dialog.show("Avertissement", "Mot de passe et confirmation doivent etre identiques", new Command("Ok"));
            return false;
        }

        return true;
    }
}
