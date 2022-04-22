package com.khedmetna.gui.front.produit;

import com.codename1.components.ImageViewer;
import com.codename1.components.ShareButton;
import com.codename1.io.FileSystemStorage;
import com.codename1.io.Log;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.ImageIO;
import com.codename1.ui.util.Resources;
import com.khedmetna.entities.Produit;
import com.khedmetna.gui.front.AccueilFront;
import com.khedmetna.services.ProduitService;
import com.khedmetna.utils.Statics;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class DisplayAll extends Form {

    public static Produit currentProduit = null;
    Resources theme = UIManager.initFirstTheme("/theme");

    Label nomLabel, descriptionLabel, prixLabel, categoryLabel;
    ImageViewer imageIV;
    Container btnsContainer;

    public DisplayAll() {
        super("Produits", new BoxLayout(BoxLayout.Y_AXIS));
        addGUIs();
        super.getToolbar().remove();
    }

    private void addGUIs() {
        ArrayList<Produit> listProduits = ProduitService.getInstance().getAll();

        if (listProduits.size() > 0) {
            for (Produit produit : listProduits) {
                Component model = makeProduitModel(produit);
                this.add(model);
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    private Component makeProduitModel(Produit produit) {
        Container produitModel = makeModelWithoutButtons(produit);

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");

        Button btnAfficherScreenshot = new Button("Partager");
        btnAfficherScreenshot.addActionListener(listener -> share(produit));

        btnsContainer.add(BorderLayout.CENTER, btnAfficherScreenshot);

        produitModel.addAll(
                btnsContainer
        );

        return produitModel;
    }

    private Container makeModelWithoutButtons(Produit produit) {
        Container produitModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        produitModel.setUIID("containerRounded");

        nomLabel = new Label("Nom : " + produit.getNom());

        nomLabel.setUIID("labelDefault");

        descriptionLabel = new Label("Description : " + produit.getDescription());

        descriptionLabel.setUIID("labelDefault");

        prixLabel = new Label("Prix : " + produit.getPrix());

        prixLabel.setUIID("labelDefault");

        categoryLabel = new Label("Category : " + produit.getCategory());

        categoryLabel.setUIID("labelDefault");

        categoryLabel = new Label("Category : " + produit.getCategory().getName());
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

        produitModel.addAll(
                nomLabel, descriptionLabel, prixLabel, categoryLabel,
                imageIV
        );

        return produitModel;
    }

    private void share(Produit produit) {
        Form form = new Form();
        form.add(new Label("Produit " + produit.getNom()));
        form.add(makeModelWithoutButtons(produit));
        String imageFile = FileSystemStorage.getInstance().getAppHomePath() + "screenshot.png";
        Image screenshot = Image.createImage(
                com.codename1.ui.Display.getInstance().getDisplayWidth(),
                com.codename1.ui.Display.getInstance().getDisplayHeight()
        );
        form.revalidate();
        form.setVisible(true);
        form.paintComponent(screenshot.getGraphics(), true);
        form.removeAll();
        try (OutputStream os = FileSystemStorage.getInstance().openOutputStream(imageFile)) {
            ImageIO.getImageIO().save(screenshot, os, ImageIO.FORMAT_PNG, 1);
        } catch (IOException err) {
            Log.e(err);
        }
        Form screenShotForm = new Form("Partager le produit", new BoxLayout(BoxLayout.Y_AXIS));
        ImageViewer screenshotViewer = new ImageViewer(screenshot.fill(1000, 2000));
        screenshotViewer.setFocusable(false);
        screenshotViewer.setUIID("screenshot");
        ShareButton btnPartager = new ShareButton();
        btnPartager.setText("Partager ");
        btnPartager.setTextPosition(LEFT);
        btnPartager.setImageToShare(imageFile, "image/png");
        btnPartager.setTextToShare(produit.toString()); 
        screenShotForm.addAll(screenshotViewer, btnPartager);
        screenShotForm.getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> AccueilFront.accueilFrontForm.showBack());
        screenShotForm.show();
        // FIN API PARTAGE
    }
}
