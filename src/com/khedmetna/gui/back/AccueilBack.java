package com.khedmetna.gui.back;

import com.codename1.components.ImageViewer;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.khedmetna.MainApp;
import com.khedmetna.gui.LoginForm;
import com.khedmetna.utils.Statics;

public class AccueilBack extends Form {

    Resources theme = UIManager.initFirstTheme("/theme");
    Label label;

    public AccueilBack() {
        super("Menu", new BoxLayout(BoxLayout.Y_AXIS));
        addGUIs();
    }

    private void addGUIs() {
        ImageViewer userImage;
        if (MainApp.getSession().getImage() != null) {
            String url = Statics.USER_IMAGE_URL + MainApp.getSession().getImage();
            Image image = URLImage.createToStorage(
                    EncodedImage.createFromImage(theme.getImage("person.jpg").fill(200, 200), false),
                    url,
                    url,
                    URLImage.RESIZE_SCALE
            );
            userImage = new ImageViewer(image);
        } else {
            userImage = new ImageViewer(theme.getImage("person.jpg").fill(200, 200));
        }
        userImage.setUIID("candidatImage");
        label = new Label("Admin" + MainApp.getSession().getNom());
        label.setUIID("links");
        Button btnDeconnexion = new Button();
        btnDeconnexion.setUIID("buttonLogout");
        btnDeconnexion.setMaterialIcon(FontImage.MATERIAL_LOGOUT);
        btnDeconnexion.addActionListener(action -> {
            MainApp.setSession(null);
            LoginForm.loginForm.showBack();
        });

        Container userContainer = new Container(new BorderLayout());
        userContainer.setUIID("userContainer");
        userContainer.add(BorderLayout.WEST, userImage);
        userContainer.add(BorderLayout.CENTER, label);
        userContainer.add(BorderLayout.EAST, btnDeconnexion);

        Container menuContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        menuContainer.isScrollableY();
        menuContainer.addAll(
                userContainer,
                userButton(),
                activitesButton(),
                typeActiviteButton(),
                produitButton(),
                categoriesButton(),
                promoButton(),
                codePromoButton(),
                commentaireButton(),
                likeButton(),
                guideButton(),
                reclamationButton(),
                panierButton(),
                commandesButton(),
                livraisonButton()
        );

        this.add(menuContainer);
    }

    private Button activitesButton() {
        Button button = new Button("    Activites");
        button.setUIID("buttonMenu");
        button.setMaterialIcon(FontImage.MATERIAL_LOCAL_ACTIVITY);
        button.addActionListener(action -> new com.khedmetna.gui.back.activite.DisplayAll(this).show());
        return button;
    }

    private Button categoriesButton() {
        Button button = new Button("    Categories");
        button.setUIID("buttonMenu");
        button.setMaterialIcon(FontImage.MATERIAL_CATEGORY);
        button.addActionListener(action -> new com.khedmetna.gui.back.category.DisplayAll(this).show());
        return button;
    }

    private Button codePromoButton() {
        Button button = new Button("    Codes promo");
        button.setUIID("buttonMenu");
        button.setMaterialIcon(FontImage.MATERIAL_MONEY);
        button.addActionListener(action -> new com.khedmetna.gui.back.codepromo.DisplayAll(this).show());
        return button;
    }

    private Button commentaireButton() {
        Button button = new Button("    Commentaires");
        button.setUIID("buttonMenu");
        button.setMaterialIcon(FontImage.MATERIAL_COMMENT);
        button.addActionListener(action -> new com.khedmetna.gui.back.commentaire.DisplayAll(this).show());
        return button;
    }

    private Button guideButton() {
        Button button = new Button("    Guides");
        button.setUIID("buttonMenu");
        button.setMaterialIcon(FontImage.MATERIAL_BOOK);
        button.addActionListener(action -> new com.khedmetna.gui.back.guide.DisplayAll(this).show());
        return button;
    }

    private Button likeButton() {
        Button button = new Button("    Likes");
        button.setUIID("buttonMenu");
        button.setMaterialIcon(FontImage.MATERIAL_FAVORITE);
        button.addActionListener(action -> new com.khedmetna.gui.back.like.DisplayAll(this).show());
        return button;
    }

    private Button produitButton() {
        Button button = new Button("    Produits");
        button.setUIID("buttonMenu");
        button.setMaterialIcon(FontImage.MATERIAL_PRODUCTION_QUANTITY_LIMITS);
        button.addActionListener(action -> new com.khedmetna.gui.back.produit.DisplayAll(this).show());
        return button;
    }

    private Button promoButton() {
        Button button = new Button("    Promotions");
        button.setUIID("buttonMenu");
        button.setMaterialIcon(FontImage.MATERIAL_MONEY);
        button.addActionListener(action -> new com.khedmetna.gui.back.promo.DisplayAll(this).show());
        return button;
    }

    private Button reclamationButton() {
        Button button = new Button("    Reclamations");
        button.setUIID("buttonMenu");
        button.setMaterialIcon(FontImage.MATERIAL_ERROR);
        button.addActionListener(action -> new com.khedmetna.gui.back.reclamation.DisplayAll(this).show());
        return button;
    }

    private Button typeActiviteButton() {
        Button button = new Button("    Types d'activites");
        button.setUIID("buttonMenu");
        button.setMaterialIcon(FontImage.MATERIAL_HIKING);
        button.addActionListener(action -> new com.khedmetna.gui.back.typeActivite.DisplayAll(this).show());
        return button;
    }

    private Button userButton() {
        Button button = new Button("    Utilisateurs");
        button.setUIID("buttonMenu");
        button.setMaterialIcon(FontImage.MATERIAL_PERSON);
        button.addActionListener(action -> new com.khedmetna.gui.back.user.DisplayAll(this).show());
        return button;
    }

    private Button panierButton() {
        Button button = new Button("    Panier");
        button.setUIID("buttonMenu");
        button.setMaterialIcon(FontImage.MATERIAL_SHOPPING_CART);
        button.addActionListener(action -> new com.khedmetna.gui.back.panier.DisplayAll(this).show());
        return button;
    }

    private Button commandesButton() {
        Button button = new Button("    Commandes");
        button.setUIID("buttonMenu");
        button.setMaterialIcon(FontImage.MATERIAL_AIRPLANEMODE_ON);
        button.addActionListener(action -> new com.khedmetna.gui.back.commande.DisplayAll(this).show());
        return button;
    }

    private Button livraisonButton() {
        Button button = new Button("    Livraison");
        button.setUIID("buttonMenu");
        button.setMaterialIcon(FontImage.MATERIAL_AIRPLANEMODE_OFF);
        button.addActionListener(action -> new com.khedmetna.gui.back.livraison.DisplayAll(this).show());
        return button;
    }
}
