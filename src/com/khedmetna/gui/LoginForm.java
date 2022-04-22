package com.khedmetna.gui;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.khedmetna.MainApp;
import com.khedmetna.entities.User;
import com.khedmetna.gui.back.AccueilBack;
import com.khedmetna.gui.front.AccueilFront;
import com.khedmetna.services.UserService;

public class LoginForm extends Form {

    public static Form loginForm;
    Label loginLabel, passwordLabel, inscriptionLabel;
    TextField tfEmail, tfPassword;
    Button btnConnexion, btnInscription;
    Button backendBtn;

    public LoginForm() {
        super("Connexion", new BoxLayout(BoxLayout.Y_AXIS));
        loginForm = this;
        addGUIs();
        addActions();
    }

    private void addGUIs() {

        loginLabel = new Label("Email :");
        tfEmail = new TextField("", "Entrez votre email");

        passwordLabel = new Label("Mot de passe :");
        tfPassword = new TextField("", "Tapez votre mot de passe", 20, TextField.PASSWORD);

        btnConnexion = new Button("Connexion");
        btnConnexion.setUIID("buttonMain");

        inscriptionLabel = new Label("Besoin d'un compte ?");
        btnInscription = new Button("Inscription");
        backendBtn = new Button("Espace admin");

        this.addAll(
                loginLabel, tfEmail,
                passwordLabel, tfPassword,
                btnConnexion, inscriptionLabel, btnInscription,
                backendBtn
        );
    }

    private void addActions() {
        btnConnexion.addActionListener(action -> {
            User user = UserService.getInstance().verifierMotDePasse(tfEmail.getText(), tfPassword.getText());
            if (user != null) {
                login(user);
            } else {
                Dialog.show("Erreur", "Identifiants invalides", new Command("Ok"));
            }
        });

        btnInscription.addActionListener(action -> new RegisterForm().show());
        backendBtn.addActionListener(action -> login(
                new User(
                        "Admin", "ROLE_ADMIN", "Admin", "", null
                )
        ));
    }

    private void login(User user) {
        MainApp.setSession(user);

        if (user.getRoles().equals("ROLE_ADMIN")) {
            new AccueilBack().show();
        } else {
            new AccueilFront().show();
        }
    }
}
