package com.khedmetna.gui.front.activite;

import com.codename1.components.ImageViewer;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.khedmetna.entities.Activite;
import com.khedmetna.gui.front.AccueilFront;
import com.khedmetna.services.ActiviteService;
import com.khedmetna.utils.Statics;

import java.util.ArrayList;

public class DisplayAll extends Form {

    Resources theme = UIManager.initFirstTheme("/theme");
    TextField searchTF;
    ArrayList<Component> componentModels;
    Label nomLabel, lieuLabel, descriptionLabel, typeActiviteLabel, longitudeLabel, lattitudeLabel;
    ImageViewer imageIV;
    Container btnsContainer;

    public DisplayAll() {
        super("Activites", new BoxLayout(BoxLayout.Y_AXIS));
        addGUIs();
        super.getToolbar().hideToolbar();
    }

    private void addGUIs() {
        ArrayList<Activite> listActivites = ActiviteService.getInstance().getAll();
        componentModels = new ArrayList<>();

        searchTF = new TextField("", "Chercher un activite");  //
        searchTF.addDataChangedListener((d, t) -> {
            if (componentModels.size() > 0) {
                for (Component componentModel : componentModels) {
                    this.removeComponent(componentModel);
                }
            }
            componentModels = new ArrayList<>();
            for (Activite activite : listActivites) {
                if (activite.getNom().startsWith(searchTF.getText())) {
                    Component model = makeActiviteModel(activite);
                    this.add(model);
                    componentModels.add(model);
                }
            }
            this.revalidate();
        });
        this.add(searchTF);

        if (listActivites.size() > 0) {
            for (Activite activite : listActivites) {
                Component model = makeActiviteModel(activite);
                this.add(model);
                componentModels.add(model);
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

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");

        activiteModel.addAll(
                nomLabel, lieuLabel, descriptionLabel, typeActiviteLabel, longitudeLabel, lattitudeLabel,
                imageIV,
                btnsContainer
        );

        try {
            float longitude = Float.parseFloat(activite.getLongitude());
            float lattitude = Float.parseFloat(activite.getLattitude());

            Button mapBtn = new Button("Afficher dans maps");
            mapBtn.addActionListener(l -> {
                new MapForm(AccueilFront.accueilFrontForm, "Activite : " + activite.getNom(), longitude, lattitude).show();
            });
            activiteModel.add(mapBtn);
        } catch (NumberFormatException | NullPointerException ignored) {

        }

        return activiteModel;
    }
}
