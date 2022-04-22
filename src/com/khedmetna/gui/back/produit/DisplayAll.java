package com.khedmetna.gui.back.produit;

import com.codename1.components.ImageViewer;
import com.codename1.components.InteractionDialog;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.khedmetna.entities.Produit;
import com.khedmetna.services.ProduitService;
import com.khedmetna.utils.Statics;

import java.util.ArrayList;

public class DisplayAll extends Form {

    public static Produit currentProduit = null;
    Resources theme = UIManager.initFirstTheme("/theme");
    Button addBtn;
    Label nomLabel, descriptionLabel, prixLabel, categoryLabel;
    ImageViewer imageIV;
    Button editBtn, deleteBtn;
    Container btnsContainer;

    public DisplayAll(Form previous) {
        super("Produits", new BoxLayout(BoxLayout.Y_AXIS));

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

        ArrayList<Produit> listProduits = ProduitService.getInstance().getAll();
        if (listProduits.size() > 0) {
            for (Produit listProduit : listProduits) {
                this.add(makeProduitModel(listProduit));
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    private void addActions() {
        addBtn.addActionListener(action -> {
            currentProduit = null;
            new Manage(this).show();
        });
    }

    private Component makeProduitModel(Produit produit) {
        Container produitModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        produitModel.setUIID("containerRounded");

        nomLabel = new Label("Nom : " + produit.getNom());

        nomLabel.setUIID("labelDefault");

        descriptionLabel = new Label("Description : " + produit.getDescription());

        descriptionLabel.setUIID("labelDefault");

        prixLabel = new Label("Prix : " + produit.getPrix());

        prixLabel.setUIID("labelDefault");

        categoryLabel = new Label("Categorie : " + produit.getCategory());
        categoryLabel.setUIID("labelDefault");

        categoryLabel = new Label("Categorie : " + produit.getCategory().getName());
        categoryLabel.setUIID("labelDefault");

        if (produit.getImage() != null) {
            String url = Statics.PRODUIT_IMAGE_URL + produit.getImage();
            Image image = URLImage.createToStorage(
                    EncodedImage.createFromImage(theme.getImage("default.jpg").fill(1100, 500), false),
                    url,
                    url,
                    URLImage.RESIZE_SCALE
            );
            imageIV = new ImageViewer(image);
        } else {
            imageIV = new ImageViewer(theme.getImage("default.jpg").fill(1100, 500));
        }
        imageIV.setFocusable(false);

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");

        editBtn = new Button("Modifier");
        editBtn.setUIID("buttonMain");
        editBtn.addActionListener(action -> {
            currentProduit = produit;
            new Manage(this).show();
        });

        deleteBtn = new Button("Supprimer");
        deleteBtn.setUIID("buttonDanger");
        deleteBtn.addActionListener(action -> {
            InteractionDialog dlg = new InteractionDialog("Confirmer la suppression");
            dlg.setLayout(new BorderLayout());
            dlg.add(BorderLayout.CENTER, new Label("Voulez vous vraiment supprimer ce produit ?"));
            Button btnClose = new Button("Annuler");
            btnClose.addActionListener((ee) -> dlg.dispose());
            Button btnConfirm = new Button("Confirmer");
            btnConfirm.addActionListener(actionConf -> {
                int responseCode = ProduitService.getInstance().delete(produit.getId());

                if (responseCode == 200) {
                    currentProduit = null;
                    dlg.dispose();
                    produitModel.remove();
                    this.refreshTheme();
                } else {
                    Dialog.show("Erreur", "Erreur de suppression du produit. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            });
            Container btnContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            btnContainer.addAll(btnClose, btnConfirm);
            dlg.addComponent(BorderLayout.SOUTH, btnContainer);
            dlg.show(800, 800, 0, 0);
        });

        btnsContainer.add(BorderLayout.CENTER, editBtn);
        btnsContainer.add(BorderLayout.EAST, deleteBtn);

        produitModel.addAll(
                nomLabel, descriptionLabel, prixLabel, categoryLabel,
                imageIV,
                btnsContainer
        );

        return produitModel;
    }
}
