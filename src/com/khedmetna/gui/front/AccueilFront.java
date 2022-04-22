package com.khedmetna.gui.front;

import com.codename1.components.ImageViewer;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.khedmetna.MainApp;
import com.khedmetna.gui.LoginForm;
import com.khedmetna.utils.Statics;

public class AccueilFront extends Form {

    public static Form accueilFrontForm;
    Resources theme = UIManager.initFirstTheme("/theme");
    Label label;

    public AccueilFront() {
        super(new BorderLayout());
        accueilFrontForm = this;
        addGUIs();
    }

    private void addGUIs() {
        Tabs tabs = new Tabs();

        tabs.addTab("Activites", FontImage.MATERIAL_LOCAL_ACTIVITY, 5,
                new com.khedmetna.gui.front.activite.DisplayAll()
        );
        tabs.addTab("Produits", FontImage.MATERIAL_PRODUCTION_QUANTITY_LIMITS, 5,
                new com.khedmetna.gui.front.produit.DisplayAll()
        );
        tabs.addTab("Promotions", FontImage.MATERIAL_MONEY, 5,
                new com.khedmetna.gui.front.promo.DisplayAll()
        );
        tabs.addTab("Menu", FontImage.MATERIAL_MENU, 5, moreGUI());

        this.add(BorderLayout.CENTER, tabs);
    }

    private Container moreGUI() {

        ImageViewer userImage;
        if (MainApp.getSession().getImage() != null) {
            String url = Statics.USER_IMAGE_URL + MainApp.getSession().getImage();
            Image image = URLImage.createToStorage(
                    EncodedImage.createFromImage(theme.getImage("person.jpg").fill(150, 150), false),
                    url,
                    url,
                    URLImage.RESIZE_SCALE
            );
            userImage = new ImageViewer(image);
        } else {
            userImage = new ImageViewer(theme.getImage("person.jpg").fill(150, 150));
        }
        userImage.setFocusable(false);
        userImage.setUIID("candidatImage");
        label = new Label(MainApp.getSession().getNom());
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
                profileButton(),
                typeActiviteButton(),
                categoriesButton(),
                codePromoButton(),
                commentaireButton(),
                guideButton(),
                reclamationButton(),
                commandesButton(),
                panierButton(),
                livraisonButton()
        );

        return (menuContainer);
    }

    private Button categoriesButton() {
        Button button = new Button("    Categories");
        button.setUIID("buttonMenu");
        button.setMaterialIcon(FontImage.MATERIAL_CATEGORY);
        button.addActionListener(action -> new com.khedmetna.gui.front.category.DisplayAll(this).show());
        return button;
    }

    private Button codePromoButton() {
        Button button = new Button("    Codes promo");
        button.setUIID("buttonMenu");
        button.setMaterialIcon(FontImage.MATERIAL_MONEY);
        button.addActionListener(action -> new com.khedmetna.gui.front.codepromo.DisplayAll(this).show());
        return button;
    }

    private Button commentaireButton() {
        Button button = new Button("    Commentaires");
        button.setUIID("buttonMenu");
        button.setMaterialIcon(FontImage.MATERIAL_COMMENT);
        button.addActionListener(action -> new com.khedmetna.gui.front.commentaire.DisplayAll(this).show());
        return button;
    }

    private Button guideButton() {
        Button button = new Button("    Guides");
        button.setUIID("buttonMenu");
        button.setMaterialIcon(FontImage.MATERIAL_BOOK);
        button.addActionListener(action -> new com.khedmetna.gui.front.guide.DisplayAll(this).show());
        return button;
    }

    private Button likeButton() {
        Button button = new Button("    Likes");
        button.setUIID("buttonMenu");
        button.setMaterialIcon(FontImage.MATERIAL_FAVORITE);
        button.addActionListener(action -> new com.khedmetna.gui.front.like.DisplayAll(this).show());
        return button;
    }

    private Button reclamationButton() {
        Button button = new Button("    Reclamations");
        button.setUIID("buttonMenu");
        button.setMaterialIcon(FontImage.MATERIAL_ERROR);
        button.addActionListener(action -> new com.khedmetna.gui.front.reclamation.DisplayAll(this).show());
        return button;
    }

    private Button typeActiviteButton() {
        Button button = new Button("    Types d'activites");
        button.setUIID("buttonMenu");
        button.setMaterialIcon(FontImage.MATERIAL_HIKING);
        button.addActionListener(action -> new com.khedmetna.gui.front.typeActivite.DisplayAll(this).show());
        return button;
    }

    private Button profileButton() {
        Button button = new Button("    Mon profil");
        button.setUIID("buttonMenu");
        button.setMaterialIcon(FontImage.MATERIAL_PERSON);
        button.addActionListener(action -> new com.khedmetna.gui.front.user.Profil(this).show());
        return button;
    }

    private Button panierButton() {
        Button button = new Button("    Panier");
        button.setUIID("buttonMenu");
        button.setMaterialIcon(FontImage.MATERIAL_SHOPPING_CART);
        button.addActionListener(action -> new com.khedmetna.gui.front.panier.DisplayAll(this).show());
        return button;
    }

    private Button commandesButton() {
        Button button = new Button("    Commandes");
        button.setUIID("buttonMenu");
        button.setMaterialIcon(FontImage.MATERIAL_AIRPLANEMODE_ON);
        button.addActionListener(action -> new com.khedmetna.gui.front.commande.DisplayAll(this).show());
        return button;
    }

    private Button livraisonButton() {
        Button button = new Button("    Livraison");
        button.setUIID("buttonMenu");
        button.setMaterialIcon(FontImage.MATERIAL_AIRPLANEMODE_OFF);
        button.addActionListener(action -> new com.khedmetna.gui.front.livraison.DisplayAll(this).show());
        return button;
    }
}
