package com.khedmetna.gui.back.guide;

import com.codename1.components.ImageViewer;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.khedmetna.entities.Activite;
import com.khedmetna.services.ActiviteService;
import com.khedmetna.utils.Statics;

import java.util.ArrayList;

public class ChooseActivite extends Form {

    Form previousForm;
    Resources theme = UIManager.initFirstTheme("/theme");

    Label nomLabel, lieuLabel, descriptionLabel, typeActiviteLabel, longitudeLabel, lattitudeLabel;
    Button chooseBtn;
    ImageViewer imageIV;
    Container btnsContainer;

    public ChooseActivite(Form previous) {
        super("Choisir un activite", new BoxLayout(BoxLayout.Y_AXIS));

        previousForm = previous;
        addGUIs();

        super.getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    public void refresh() {
        this.removeAll();
        addGUIs();
        this.refreshTheme();
    }

    private void addGUIs() {
        ArrayList<Activite> listActivites = ActiviteService.getInstance().getAll();
        if (listActivites.size() > 0) {
            for (Activite activites : listActivites) {
                this.add(makeActiviteModel(activites));
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    private Component makeActiviteModel(Activite activite) {
        Container activiteModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        activiteModel.setUIID("containerRounded");

        nomLabel = new Label("Nom : " + activite.getNom());
        nomLabel.setUIID("labelDefault");

        lieuLabel = new Label("Lieu : " + activite.getLieu());
        lieuLabel.setUIID("labelDefault");

        descriptionLabel = new Label("Description : " + activite.getDescription());
        descriptionLabel.setUIID("labelDefault");

        typeActiviteLabel = new Label("TypeActivite : " + activite.getTypeActivite());

        typeActiviteLabel.setUIID("labelDefault");

        longitudeLabel = new Label("Longitude : " + activite.getLongitude());

        longitudeLabel.setUIID("labelDefault");

        lattitudeLabel = new Label("Lattitude : " + activite.getLattitude());

        lattitudeLabel.setUIID("labelDefault");

        typeActiviteLabel = new Label("TypeActivite : " + activite.getTypeActivite().getNom());
        typeActiviteLabel.setUIID("labelDefault");

        if (activite.getImage() != null) {
            String url = Statics.ACTIVITE_IMAGE_URL + activite.getImage();
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

        chooseBtn = new Button("Choisir");
        chooseBtn.addActionListener(l -> {
            Manage.selectedActivite = activite;
            ((Manage) previousForm).refreshActivite();
            previousForm.showBack();
        });

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");
        btnsContainer.add(BorderLayout.CENTER, chooseBtn);

        activiteModel.addAll(
                nomLabel, lieuLabel, descriptionLabel, typeActiviteLabel, longitudeLabel, lattitudeLabel,
                imageIV,
                btnsContainer
        );

        return activiteModel;
    }
}
