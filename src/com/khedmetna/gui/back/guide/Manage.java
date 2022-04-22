package com.khedmetna.gui.back.guide;

import com.codename1.capture.Capture;
import com.codename1.components.ImageViewer;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.khedmetna.entities.Activite;
import com.khedmetna.entities.Guide;
import com.khedmetna.services.GuideService;
import com.khedmetna.utils.Statics;

import java.io.IOException;

public class Manage extends Form {

    public static Activite selectedActivite;
    Resources theme = UIManager.initFirstTheme("/theme");
    String selectedImage;
    boolean imageEdited = false;
    Guide currentGuide;
    Label nomLabel, telLabel, activiteLabel, imageLabel;
    TextField nomTF,
            telTF,
            activiteTF,
            imageTF;

    Label selectedActiviteLabel;
    Button selectActiviteButton;

    ImageViewer imageIV;
    Button selectImageButton;

    Button manageButton;

    Form previous;

    public Manage(Form previous) {
        super(DisplayAll.currentGuide == null ? "Ajouter" : "Modifier", new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;

        selectedActivite = null;

        currentGuide = DisplayAll.currentGuide;

        addGUIs();
        addActions();

        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    public void refreshActivite() {
        selectedActiviteLabel.setText(selectedActivite.getNom());
        this.refreshTheme();
    }

    private void addGUIs() {

        nomLabel = new Label("Nom : ");
        nomLabel.setUIID("labelDefault");
        nomTF = new TextField();
        nomTF.setHint("Tapez le nom");

        telLabel = new Label("Tel : ");
        telLabel.setUIID("labelDefault");
        telTF = new TextField();
        telTF.setHint("Tapez le tel");

        activiteLabel = new Label("Activite : ");
        activiteLabel.setUIID("labelDefault");
        activiteTF = new TextField();
        activiteTF.setHint("Tapez le activite");

        activiteLabel = new Label("activite : ");
        activiteLabel.setUIID("labelDefault");
        selectedActiviteLabel = new Label("null");
        selectActiviteButton = new Button("Choisir activite");
        selectActiviteButton.addActionListener(l -> new ChooseActivite(this).show());

        imageLabel = new Label("Image : ");
        imageLabel.setUIID("labelDefault");
        selectImageButton = new Button("Ajouter une image");

        if (currentGuide == null) {

            imageIV = new ImageViewer(theme.getImage("default.jpg").fill(1100, 500));

            manageButton = new Button("Ajouter");
        } else {

            nomTF.setText(currentGuide.getNom());
            telTF.setText(String.valueOf(currentGuide.getTel()));

            selectedActivite = currentGuide.getActivite();

            activiteLabel = new Label("activite : ");
            activiteLabel.setUIID("labelDefault");
            selectedActiviteLabel = new Label("null");
            selectedActiviteLabel.setText(selectedActivite.getNom());

            if (currentGuide.getImage() != null) {
                String url = Statics.GUIDE_IMAGE_URL + currentGuide.getImage();
                selectedImage = currentGuide.getImage();
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
                telLabel, telTF,
                activiteLabel,
                selectedActiviteLabel, selectActiviteButton,
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

        if (currentGuide == null) {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = GuideService.getInstance().add(
                            new Guide(
                                    nomTF.getText(),
                                    (int) Float.parseFloat(telTF.getText()),
                                    selectedActivite,
                                    selectedImage
                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Guide ajouté avec succes", new Command("Ok"));
                        showBackAndRefresh();
                    } else {
                        Dialog.show("Erreur", "Erreur d'ajout de guide. Code d'erreur : " + responseCode, new Command("Ok"));
                    }
                }
            });
        } else {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = GuideService.getInstance().edit(
                            new Guide(
                                    currentGuide.getId(),
                                    nomTF.getText(),
                                    (int) Float.parseFloat(telTF.getText()),
                                    selectedActivite,
                                    selectedImage
                            ), imageEdited
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Guide modifié avec succes", new Command("Ok"));
                        showBackAndRefresh();
                    } else {
                        Dialog.show("Erreur", "Erreur de modification de guide. Code d'erreur : " + responseCode, new Command("Ok"));
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

        if (telTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le tel", new Command("Ok"));
            return false;
        }
        try {
            Float.parseFloat(telTF.getText());
        } catch (NumberFormatException e) {
            Dialog.show("Avertissement", telTF.getText() + " n'est pas un nombre valide", new Command("Ok"));
            return false;
        }

        if (selectedActivite == null) {
            Dialog.show("Avertissement", "Veuillez choisir un activite", new Command("Ok"));
            return false;
        }

        if (selectedImage == null) {
            Dialog.show("Avertissement", "Veuillez choisir une image", new Command("Ok"));
            return false;
        }

        return true;
    }
}
