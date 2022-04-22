package com.khedmetna.gui.back.produit;

import com.codename1.capture.Capture;
import com.codename1.components.ImageViewer;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.khedmetna.entities.Category;
import com.khedmetna.entities.Produit;
import com.khedmetna.services.ProduitService;
import com.khedmetna.utils.Statics;

import java.io.IOException;

public class Manage extends Form {

    public static Category selectedCategory;
    Resources theme = UIManager.initFirstTheme("/theme");
    String selectedImage;
    boolean imageEdited = false;
    Produit currentProduit;
    Label nomLabel, descriptionLabel, prixLabel, imageLabel, categoryLabel;
    TextField nomTF,
            descriptionTF,
            prixTF,
            imageTF,
            categoryTF;

    Label selectedCategoryLabel;
    Button selectCategoryButton;

    ImageViewer imageIV;
    Button selectImageButton;

    Button manageButton;

    Form previous;

    public Manage(Form previous) {
        super(DisplayAll.currentProduit == null ? "Ajouter" : "Modifier", new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;

        selectedCategory = null;

        currentProduit = DisplayAll.currentProduit;

        addGUIs();
        addActions();

        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    public void refreshCategory() {
        selectedCategoryLabel.setText(selectedCategory.getName());
        this.refreshTheme();
    }

    private void addGUIs() {

        nomLabel = new Label("Nom : ");
        nomLabel.setUIID("labelDefault");
        nomTF = new TextField();
        nomTF.setHint("Tapez le nom");

        descriptionLabel = new Label("Description : ");
        descriptionLabel.setUIID("labelDefault");
        descriptionTF = new TextField();
        descriptionTF.setHint("Tapez le description");

        prixLabel = new Label("Prix : ");
        prixLabel.setUIID("labelDefault");
        prixTF = new TextField();
        prixTF.setHint("Tapez le prix");

        categoryLabel = new Label("Category : ");
        categoryLabel.setUIID("labelDefault");
        categoryTF = new TextField();
        categoryTF.setHint("Tapez le category");

        categoryLabel = new Label("category : ");
        categoryLabel.setUIID("labelDefault");
        selectedCategoryLabel = new Label("null");
        selectCategoryButton = new Button("Choisir categorie");
        selectCategoryButton.addActionListener(l -> new ChooseCategory(this).show());

        imageLabel = new Label("Image : ");
        imageLabel.setUIID("labelDefault");
        selectImageButton = new Button("Ajouter une image");

        if (currentProduit == null) {

            imageIV = new ImageViewer(theme.getImage("default.jpg").fill(1100, 500));

            manageButton = new Button("Ajouter");
        } else {

            nomTF.setText(currentProduit.getNom());
            descriptionTF.setText(currentProduit.getDescription());
            prixTF.setText(String.valueOf(currentProduit.getPrix()));

            selectedCategory = currentProduit.getCategory();

            categoryLabel = new Label("category : ");
            categoryLabel.setUIID("labelDefault");
            selectedCategoryLabel = new Label("null");
            selectedCategoryLabel.setText(selectedCategory.getName());

            if (currentProduit.getImage() != null) {
                selectedImage = currentProduit.getImage();
                String url = Statics.PRODUIT_IMAGE_URL + currentProduit.getImage();
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

            manageButton = new Button("Modifier");
        }
        manageButton.setUIID("buttonMain");

        Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        container.setUIID("containerRounded");

        container.addAll(
                imageLabel, imageIV,
                selectImageButton,
                nomLabel, nomTF,
                descriptionLabel, descriptionTF,
                prixLabel, prixTF,
                categoryLabel,
                selectedCategoryLabel, selectCategoryButton,
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

        if (currentProduit == null) {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = ProduitService.getInstance().add(
                            new Produit(
                                    nomTF.getText(),
                                    descriptionTF.getText(),
                                    (int) Float.parseFloat(prixTF.getText()),
                                    selectedImage,
                                    selectedCategory
                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Produit ajouté avec succes", new Command("Ok"));
                        showBackAndRefresh();
                    } else {
                        Dialog.show("Erreur", "Erreur d'ajout de produit. Code d'erreur : " + responseCode, new Command("Ok"));
                    }
                }
            });
        } else {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = ProduitService.getInstance().edit(
                            new Produit(
                                    currentProduit.getId(),
                                    nomTF.getText(),
                                    descriptionTF.getText(),
                                    (int) Float.parseFloat(prixTF.getText()),
                                    selectedImage,
                                    selectedCategory
                            ), imageEdited
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Produit modifié avec succes", new Command("Ok"));
                        showBackAndRefresh();
                    } else {
                        Dialog.show("Erreur", "Erreur de modification de produit. Code d'erreur : " + responseCode, new Command("Ok"));
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

        if (nomTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le nom", new Command("Ok"));
            return false;
        }

        if (descriptionTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le description", new Command("Ok"));
            return false;
        }

        if (prixTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le prix", new Command("Ok"));
            return false;
        }
        try {
            Float.parseFloat(prixTF.getText());
        } catch (NumberFormatException e) {
            Dialog.show("Avertissement", prixTF.getText() + " n'est pas un nombre valide", new Command("Ok"));
            return false;
        }

        if (selectedCategory == null) {
            Dialog.show("Avertissement", "Veuillez choisir un category", new Command("Ok"));
            return false;
        }

        if (selectedImage == null) {
            Dialog.show("Avertissement", "Veuillez choisir une image", new Command("Ok"));
            return false;
        }

        return true;
    }
}
