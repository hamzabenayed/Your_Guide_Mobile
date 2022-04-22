package com.khedmetna.gui.back.promo;

import com.codename1.capture.Capture;
import com.codename1.components.ImageViewer;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.khedmetna.entities.Codepromo;
import com.khedmetna.entities.Promo;
import com.khedmetna.services.PromoService;
import com.khedmetna.utils.Statics;

import java.io.IOException;

public class Manage extends Form {

    public static Codepromo selectedCodepromo;
    Resources theme = UIManager.initFirstTheme("/theme");
    String selectedImage;
    boolean imageEdited = false;
    Promo currentPromo;
    Label pourcentageLabel, codepromoLabel, imageLabel;
    TextField pourcentageTF,
            codepromoTF,
            imageTF;

    Label selectedCodepromoLabel;
    Button selectCodepromoButton;

    ImageViewer imageIV;
    Button selectImageButton;

    Button manageButton;

    Form previous;

    public Manage(Form previous) {
        super(DisplayAll.currentPromo == null ? "Ajouter" : "Modifier", new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;

        selectedCodepromo = null;

        currentPromo = DisplayAll.currentPromo;

        addGUIs();
        addActions();

        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    public void refreshCodepromo() {
        selectedCodepromoLabel.setText(selectedCodepromo.getCode());
        this.refreshTheme();
    }

    private void addGUIs() {

        pourcentageLabel = new Label("Pourcentage : ");
        pourcentageLabel.setUIID("labelDefault");
        pourcentageTF = new TextField();
        pourcentageTF.setHint("Tapez le pourcentage");

        codepromoLabel = new Label("Codepromo : ");
        codepromoLabel.setUIID("labelDefault");
        codepromoTF = new TextField();
        codepromoTF.setHint("Tapez le codepromo");

        codepromoLabel = new Label("codepromo : ");
        codepromoLabel.setUIID("labelDefault");
        selectedCodepromoLabel = new Label("null");
        selectCodepromoButton = new Button("Choisir codepromo");
        selectCodepromoButton.addActionListener(l -> new ChooseCodepromo(this).show());

        imageLabel = new Label("Image : ");
        imageLabel.setUIID("labelDefault");
        selectImageButton = new Button("Ajouter une image");

        if (currentPromo == null) {

            imageIV = new ImageViewer(theme.getImage("default.jpg").fill(1100, 500));

            manageButton = new Button("Ajouter");
        } else {

            pourcentageTF.setText(String.valueOf(currentPromo.getPourcentage()));

            selectedCodepromo = currentPromo.getCodepromo();

            codepromoLabel = new Label("codepromo : ");
            codepromoLabel.setUIID("labelDefault");
            selectedCodepromoLabel = new Label("null");
            selectedCodepromoLabel.setText(selectedCodepromo.getCode());

            if (currentPromo.getImage() != null) {
                String url = Statics.PROMO_IMAGE_URL + currentPromo.getImage();
                selectedImage = currentPromo.getImage();
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
                pourcentageLabel, pourcentageTF,
                codepromoLabel,
                selectedCodepromoLabel, selectCodepromoButton,
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

        if (currentPromo == null) {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = PromoService.getInstance().add(
                            new Promo(
                                    Float.parseFloat(pourcentageTF.getText()),
                                    selectedCodepromo,
                                    selectedImage
                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Promo ajouté avec succes", new Command("Ok"));
                        showBackAndRefresh();
                    } else {
                        Dialog.show("Erreur", "Erreur d'ajout de promo. Code d'erreur : " + responseCode, new Command("Ok"));
                    }
                }
            });
        } else {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = PromoService.getInstance().edit(
                            new Promo(
                                    currentPromo.getId(),
                                    Float.parseFloat(pourcentageTF.getText()),
                                    selectedCodepromo,
                                    selectedImage
                            ), imageEdited
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Promo modifié avec succes", new Command("Ok"));
                        showBackAndRefresh();
                    } else {
                        Dialog.show("Erreur", "Erreur de modification de promo. Code d'erreur : " + responseCode, new Command("Ok"));
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

        if (pourcentageTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le pourcentage", new Command("Ok"));
            return false;
        }
        try {
            Float.parseFloat(pourcentageTF.getText());
        } catch (NumberFormatException e) {
            Dialog.show("Avertissement", pourcentageTF.getText() + " n'est pas un nombre valide", new Command("Ok"));
            return false;
        }

        if (selectedCodepromo == null) {
            Dialog.show("Avertissement", "Veuillez choisir un codepromo", new Command("Ok"));
            return false;
        }

        if (selectedImage == null) {
            Dialog.show("Avertissement", "Veuillez choisir une image", new Command("Ok"));
            return false;
        }

        return true;
    }
}
