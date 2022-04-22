package com.khedmetna.gui.front.reclamation;

import com.codename1.components.ImageViewer;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.khedmetna.entities.Reclamation;
import com.khedmetna.services.ReclamationService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DisplayAll extends Form {

    public static Reclamation currentReclamation = null;
        Resources theme = UIManager.initFirstTheme("/theme");
    Button addBtn;
    Label dateLabel, typeLabel, etatLabel, descriptionLabel;
    Container btnsContainer;
    ArrayList<Component> componentModels;

    public DisplayAll(Form previous) {
        super("Reclamations", new BoxLayout(BoxLayout.Y_AXIS));
                
        addGUIs();
        super.getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    public void refresh() {
        this.removeAll();
        addGUIs();
        this.refreshTheme();
    }

    private void addGUIs() {
        addBtn = new Button("Ajouter une nouvelle reclamation");
        addBtn.setUIID("buttonWhiteCenter");

        addBtn.addActionListener(action -> {
            currentReclamation = null;
            new Manage(this).show();
        });
        this.add(addBtn);

        ArrayList<Reclamation> listReclamations = ReclamationService.getInstance().getAll();
        if (listReclamations.size() > 0) {
            for (Reclamation reclamation : listReclamations) {
                Component model = makeReclamationModel(reclamation);
                this.add(model);
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    private Component makeReclamationModel(Reclamation reclamation) {
        Container reclamationModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        reclamationModel.setUIID("containerRounded");

        dateLabel = new Label("Date : " + new SimpleDateFormat("dd-MM-yyyy").format(reclamation.getDate()));
        dateLabel.setUIID("labelDefault");

        typeLabel = new Label("Type : " + reclamation.getType());
        typeLabel.setUIID("labelDefault");

        etatLabel = new Label("Etat : " + reclamation.getEtat());
        etatLabel.setUIID("labelDefault");

        descriptionLabel = new Label("Description : " + reclamation.getDescription());

        descriptionLabel.setUIID("labelDefault");

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");

        reclamationModel.addAll(
                dateLabel, typeLabel, etatLabel, descriptionLabel,
                btnsContainer
        );

        return reclamationModel;
    }
}
