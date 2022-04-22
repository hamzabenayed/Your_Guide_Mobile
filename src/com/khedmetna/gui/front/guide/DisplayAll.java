package com.khedmetna.gui.front.guide;

import com.codename1.components.ImageViewer;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.khedmetna.entities.Guide;
import com.khedmetna.services.GuideService;
import com.khedmetna.utils.Statics;

import java.util.ArrayList;

public class DisplayAll extends Form {

    public static Guide currentGuide = null;
    Resources theme = UIManager.initFirstTheme("/theme");

    Label nomLabel, telLabel, activiteLabel;
    ImageViewer imageIV;
    Container btnsContainer;

    public DisplayAll(Form previous) {
        super("Guides", new BoxLayout(BoxLayout.Y_AXIS));
        addGUIs();
        super.getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    private void addGUIs() {
        ArrayList<Guide> listGuides = GuideService.getInstance().getAll();

        if (listGuides.size() > 0) {
            for (Guide guide : listGuides) {
                Component model = makeGuideModel(guide);
                this.add(model);
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    private Component makeGuideModel(Guide guide) {
        Container guideModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        guideModel.setUIID("containerRounded");

        nomLabel = new Label("Nom : " + guide.getNom());

        nomLabel.setUIID("labelDefault");

        telLabel = new Label("Tel : " + guide.getTel());

        telLabel.setUIID("labelDefault");

        activiteLabel = new Label("Activite : " + guide.getActivite());

        activiteLabel.setUIID("labelDefault");

        activiteLabel = new Label("Activite : " + guide.getActivite().getNom());
        activiteLabel.setUIID("labelDefault");

        if (guide.getImage() != null) {
            String url = Statics.GUIDE_IMAGE_URL + guide.getImage();
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

        guideModel.addAll(
                nomLabel, telLabel, activiteLabel,
                imageIV,
                btnsContainer
        );

        return guideModel;
    }
}
