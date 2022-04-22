package com.khedmetna.gui.back.guide;

import com.codename1.components.ImageViewer;
import com.codename1.components.InteractionDialog;
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
    Button addBtn;
    Label nomLabel, telLabel, activiteLabel;
    ImageViewer imageIV;
    Button editBtn, deleteBtn;
    Container btnsContainer;

    public DisplayAll(Form previous) {
        super("Guides", new BoxLayout(BoxLayout.Y_AXIS));

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

        ArrayList<Guide> listGuides = GuideService.getInstance().getAll();
        if (listGuides.size() > 0) {
            for (Guide listGuide : listGuides) {
                this.add(makeGuideModel(listGuide));
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    private void addActions() {
        addBtn.addActionListener(action -> {
            currentGuide = null;
            new Manage(this).show();
        });
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

        editBtn = new Button("Modifier");
        editBtn.setUIID("buttonMain");
        editBtn.addActionListener(action -> {
            currentGuide = guide;
            new Manage(this).show();
        });

        deleteBtn = new Button("Supprimer");
        deleteBtn.setUIID("buttonDanger");
        deleteBtn.addActionListener(action -> {
            InteractionDialog dlg = new InteractionDialog("Confirmer la suppression");
            dlg.setLayout(new BorderLayout());
            dlg.add(BorderLayout.CENTER, new Label("Voulez vous vraiment supprimer ce guide ?"));
            Button btnClose = new Button("Annuler");
            btnClose.addActionListener((ee) -> dlg.dispose());
            Button btnConfirm = new Button("Confirmer");
            btnConfirm.addActionListener(actionConf -> {
                int responseCode = GuideService.getInstance().delete(guide.getId());

                if (responseCode == 200) {
                    currentGuide = null;
                    dlg.dispose();
                    guideModel.remove();
                    this.refreshTheme();
                } else {
                    Dialog.show("Erreur", "Erreur de suppression du guide. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            });
            Container btnContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            btnContainer.addAll(btnClose, btnConfirm);
            dlg.addComponent(BorderLayout.SOUTH, btnContainer);
            dlg.show(800, 800, 0, 0);
        });

        btnsContainer.add(BorderLayout.CENTER, editBtn);
        btnsContainer.add(BorderLayout.EAST, deleteBtn);

        guideModel.addAll(
                nomLabel, telLabel, activiteLabel,
                imageIV,
                btnsContainer
        );

        return guideModel;
    }
}
