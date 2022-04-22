package com.khedmetna.gui.back.reclamation;

import com.codename1.components.InteractionDialog;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.khedmetna.entities.Reclamation;
import com.khedmetna.services.ReclamationService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DisplayAll extends Form {

    public static Reclamation currentReclamation = null;
    Button addBtn;
    Label dateLabel, typeLabel, etatLabel, descriptionLabel;
    Button editBtn, deleteBtn;

    Container btnsContainer;

    public DisplayAll(Form previous) {
        super("Reclamations", new BoxLayout(BoxLayout.Y_AXIS));

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
        addBtn = new Button("Ajouter ");
        addBtn.setUIID("buttonWhiteCenter");

        this.add(addBtn);

        ArrayList<Reclamation> listReclamations = ReclamationService.getInstance().getAll();
        if (listReclamations.size() > 0) {
            for (Reclamation listReclamation : listReclamations) {
                this.add(makeReclamationModel(listReclamation));
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    private void addActions() {
        addBtn.addActionListener(action -> {
            currentReclamation = null;
            new Manage(this).show();
        });
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

        editBtn = new Button("Modifier");
        editBtn.setUIID("buttonMain");
        editBtn.addActionListener(action -> {
            currentReclamation = reclamation;
            new Manage(this).show();
        });

        deleteBtn = new Button("Supprimer");
        deleteBtn.setUIID("buttonDanger");
        deleteBtn.addActionListener(action -> {
            InteractionDialog dlg = new InteractionDialog("Confirmer la suppression");
            dlg.setLayout(new BorderLayout());
            dlg.add(BorderLayout.CENTER, new Label("Voulez vous vraiment supprimer ce reclamation ?"));
            Button btnClose = new Button("Annuler");
            btnClose.addActionListener((ee) -> dlg.dispose());
            Button btnConfirm = new Button("Confirmer");
            btnConfirm.addActionListener(actionConf -> {
                int responseCode = ReclamationService.getInstance().delete(reclamation.getId());

                if (responseCode == 200) {
                    currentReclamation = null;
                    dlg.dispose();
                    reclamationModel.remove();
                    this.refreshTheme();
                } else {
                    Dialog.show("Erreur", "Erreur de suppression du reclamation. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            });
            Container btnContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            btnContainer.addAll(btnClose, btnConfirm);
            dlg.addComponent(BorderLayout.SOUTH, btnContainer);
            dlg.show(800, 800, 0, 0);
        });

        btnsContainer.add(BorderLayout.CENTER, editBtn);
        btnsContainer.add(BorderLayout.EAST, deleteBtn);

        reclamationModel.addAll(
                dateLabel, typeLabel, etatLabel, descriptionLabel,
                btnsContainer
        );

        return reclamationModel;
    }
}
