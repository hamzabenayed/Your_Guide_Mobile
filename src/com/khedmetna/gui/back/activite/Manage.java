package com.khedmetna.gui.back.activite;

import com.codename1.capture.Capture;
import com.codename1.components.ImageViewer;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.khedmetna.entities.Activite;
import com.khedmetna.entities.TypeActivite;
import com.khedmetna.services.ActiviteService;
import com.khedmetna.utils.Statics;

import java.io.IOException;

public class Manage extends Form {

    public static TypeActivite selectedTypeActivite;
    Resources theme = UIManager.initFirstTheme("/theme");
    String selectedImage;
    boolean imageEdited = false;
    Activite currentActivite;
    Label nomLabel, lieuLabel, descriptionLabel, imageLabel, typeActiviteLabel, longitudeLabel, lattitudeLabel;
    TextField nomTF,
            lieuTF,
            descriptionTF,
            typeActiviteTF,
            longitudeTF,
            lattitudeTF;

    Label selectedTypeActiviteLabel;
    Button selectTypeActiviteButton;

    ImageViewer imageIV;
    Button selectImageButton;

    Button manageButton;

    Form previous;

    public Manage(Form previous) {
        super(DisplayAll.currentActivite == null ? "Ajouter" : "Modifier", new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;

        selectedTypeActivite = null;

        currentActivite = DisplayAll.currentActivite;

        addGUIs();
        addActions();

        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    public void refreshTypeActivite() {
        selectedTypeActiviteLabel.setText(selectedTypeActivite.getNom());
        selectTypeActiviteButton.setText("Choisir typeActivite");
        this.refreshTheme();
    }

    private void addGUIs() {

        nomLabel = new Label("Nom : ");
        nomLabel.setUIID("labelDefault");
        nomTF = new TextField();
        nomTF.setHint("Tapez le nom");

        lieuLabel = new Label("Lieu : ");
        lieuLabel.setUIID("labelDefault");
        lieuTF = new TextField();
        lieuTF.setHint("Tapez le lieu");

        descriptionLabel = new Label("Description : ");
        descriptionLabel.setUIID("labelDefault");
        descriptionTF = new TextField();
        descriptionTF.setHint("Tapez le description");

        typeActiviteLabel = new Label("TypeActivite : ");
        typeActiviteLabel.setUIID("labelDefault");
        typeActiviteTF = new TextField();
        typeActiviteTF.setHint("Tapez le typeActivite");

        longitudeLabel = new Label("Longitude : ");
        longitudeLabel.setUIID("labelDefault");
        longitudeTF = new TextField();
        longitudeTF.setHint("Tapez le longitude");

        lattitudeLabel = new Label("Lattitude : ");
        lattitudeLabel.setUIID("labelDefault");
        lattitudeTF = new TextField();
        lattitudeTF.setHint("Tapez le lattitude");

        typeActiviteLabel = new Label("typeActivite : ");
        typeActiviteLabel.setUIID("labelDefault");
        selectedTypeActiviteLabel = new Label("null");
        selectTypeActiviteButton = new Button("Choisir typeActivite");
        selectTypeActiviteButton.addActionListener(l -> new ChooseTypeActivite(this).show());

        imageLabel = new Label("Image : ");
        imageLabel.setUIID("labelDefault");
        selectImageButton = new Button("Ajouter une image");

        if (currentActivite == null) {

            imageIV = new ImageViewer(theme.getImage("default.jpg").fill(1100, 500));

            manageButton = new Button("Ajouter");
        } else {

            nomTF.setText(currentActivite.getNom());
            lieuTF.setText(currentActivite.getLieu());
            descriptionTF.setText(currentActivite.getDescription());
            longitudeTF.setText(currentActivite.getLongitude());
            lattitudeTF.setText(currentActivite.getLattitude());
            selectedTypeActivite = currentActivite.getTypeActivite();

            typeActiviteLabel = new Label("typeActivite : ");
            typeActiviteLabel.setUIID("labelDefault");
            selectedTypeActiviteLabel = new Label("null");
            selectedTypeActiviteLabel.setText(selectedTypeActivite.getNom());

            if (currentActivite.getImage() != null) {
                String url = Statics.ACTIVITE_IMAGE_URL + currentActivite.getImage();
                selectedImage = currentActivite.getImage();
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
                lieuLabel, lieuTF,
                descriptionLabel, descriptionTF,
                longitudeLabel, longitudeTF,
                lattitudeLabel, lattitudeTF,
                typeActiviteLabel,
                selectedTypeActiviteLabel, selectTypeActiviteButton,
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

        if (currentActivite == null) {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = ActiviteService.getInstance().add(
                            new Activite(
                                    nomTF.getText(),
                                    lieuTF.getText(),
                                    descriptionTF.getText(),
                                    selectedImage,
                                    selectedTypeActivite,
                                    longitudeTF.getText(),
                                    lattitudeTF.getText()
                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Activite ajouté avec succes", new Command("Ok"));
                        showBackAndRefresh();
                    } else {
                        Dialog.show("Erreur", "Erreur d'ajout de activite. Code d'erreur : " + responseCode, new Command("Ok"));
                    }
                }
            });
        } else {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = ActiviteService.getInstance().edit(
                            new Activite(
                                    currentActivite.getId(),
                                    nomTF.getText(),
                                    lieuTF.getText(),
                                    descriptionTF.getText(),
                                    selectedImage,
                                    selectedTypeActivite,
                                    longitudeTF.getText(),
                                    lattitudeTF.getText()
                            ), imageEdited
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Activite modifié avec succes", new Command("Ok"));
                        showBackAndRefresh();
                    } else {
                        Dialog.show("Erreur", "Erreur de modification de activite. Code d'erreur : " + responseCode, new Command("Ok"));
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

        if (lieuTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le lieu", new Command("Ok"));
            return false;
        }

        if (descriptionTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le description", new Command("Ok"));
            return false;
        }

        if (longitudeTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le longitude", new Command("Ok"));
            return false;
        }

        if (lattitudeTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le lattitude", new Command("Ok"));
            return false;
        }

        if (selectedTypeActivite == null) {
            Dialog.show("Avertissement", "Veuillez choisir un typeActivite", new Command("Ok"));
            return false;
        }

        if (selectedImage == null) {
            Dialog.show("Avertissement", "Veuillez choisir une image", new Command("Ok"));
            return false;
        }

        return true;
    }
}
