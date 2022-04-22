package com.khedmetna.gui.back.activite;

import com.codename1.components.ImageViewer;
import com.codename1.components.InteractionDialog;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.khedmetna.entities.Activite;
import com.khedmetna.services.ActiviteService;
import com.khedmetna.utils.Statics;

import java.util.ArrayList;

public class DisplayAll extends Form {

    public static Activite currentActivite = null;
    Resources theme = UIManager.initFirstTheme("/theme");
    Button addBtn;
    Label nomLabel, lieuLabel, descriptionLabel, typeActiviteLabel, longitudeLabel, lattitudeLabel;
    ImageViewer imageIV;
    Button editBtn, deleteBtn;
    Container btnsContainer;

    public DisplayAll(Form previous) {
        super("Activites", new BoxLayout(BoxLayout.Y_AXIS));

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

        ArrayList<Activite> listActivites = ActiviteService.getInstance().getAll();
        if (listActivites.size() > 0) {
            for (Activite listActivite : listActivites) {
                this.add(makeActiviteModel(listActivite));
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    private void addActions() {
        addBtn.addActionListener(action -> {
            currentActivite = null;
            new Manage(this).show();
        });
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

        editBtn = new Button("Modifier");
        editBtn.setUIID("buttonMain");
        editBtn.addActionListener(action -> {
            currentActivite = activite;
            new Manage(this).show();
        });

        deleteBtn = new Button("Supprimer");
        deleteBtn.setUIID("buttonDanger");
        deleteBtn.addActionListener(action -> {
            InteractionDialog dlg = new InteractionDialog("Confirmer la suppression");
            dlg.setLayout(new BorderLayout());
            dlg.add(BorderLayout.CENTER, new Label("Voulez vous vraiment supprimer ce activite ?"));
            Button btnClose = new Button("Annuler");
            btnClose.addActionListener((ee) -> dlg.dispose());
            Button btnConfirm = new Button("Confirmer");
            btnConfirm.addActionListener(actionConf -> {
                int responseCode = ActiviteService.getInstance().delete(activite.getId());

                if (responseCode == 200) {
                    currentActivite = null;
                    dlg.dispose();
                    activiteModel.remove();
                    this.refreshTheme();
                } else {
                    Dialog.show("Erreur", "Erreur de suppression du activite. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            });
            Container btnContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            btnContainer.addAll(btnClose, btnConfirm);
            dlg.addComponent(BorderLayout.SOUTH, btnContainer);
            dlg.show(800, 800, 0, 0);
        });

        btnsContainer.add(BorderLayout.CENTER, editBtn);
        btnsContainer.add(BorderLayout.EAST, deleteBtn);

        activiteModel.addAll(
                nomLabel, lieuLabel, descriptionLabel, typeActiviteLabel, longitudeLabel, lattitudeLabel,
                imageIV,
                btnsContainer
        );

        return activiteModel;
    }
}
