package com.khedmetna.gui.back.codepromo;

import com.codename1.components.InteractionDialog;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.khedmetna.entities.Codepromo;
import com.khedmetna.services.CodepromoService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DisplayAll extends Form {

    public static Codepromo currentCodepromo = null;
    Button addBtn;
    Label codeLabel, dateDebutLabel, dateFinLabel;
    Button editBtn, deleteBtn;
    Container btnsContainer;

    public DisplayAll(Form previous) {
        super("Codepromos", new BoxLayout(BoxLayout.Y_AXIS));

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

        ArrayList<Codepromo> listCodepromos = CodepromoService.getInstance().getAll();
        if (listCodepromos.size() > 0) {
            for (Codepromo listCodepromo : listCodepromos) {
                this.add(makeCodepromoModel(listCodepromo));
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    private void addActions() {
        addBtn.addActionListener(action -> {
            currentCodepromo = null;
            new Manage(this).show();
        });
    }

    private Component makeCodepromoModel(Codepromo codepromo) {
        Container codepromoModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        codepromoModel.setUIID("containerRounded");

        codeLabel = new Label("Code : " + codepromo.getCode());
        codeLabel.setUIID("labelDefault");

        dateDebutLabel = new Label("DateDebut : " + new SimpleDateFormat("dd-MM-yyyy").format(codepromo.getDateDebut()));
        dateDebutLabel.setUIID("labelDefault");
        dateFinLabel = new Label("DateFin : " + new SimpleDateFormat("dd-MM-yyyy").format(codepromo.getDateFin()));
        dateFinLabel.setUIID("labelDefault");

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");

        editBtn = new Button("Modifier");
        editBtn.setUIID("buttonMain");
        editBtn.addActionListener(action -> {
            currentCodepromo = codepromo;
            new Manage(this).show();
        });

        deleteBtn = new Button("Supprimer");
        deleteBtn.setUIID("buttonDanger");
        deleteBtn.addActionListener(action -> {
            InteractionDialog dlg = new InteractionDialog("Confirmer la suppression");
            dlg.setLayout(new BorderLayout());
            dlg.add(BorderLayout.CENTER, new Label("Voulez vous vraiment supprimer ce codepromo ?"));
            Button btnClose = new Button("Annuler");
            btnClose.addActionListener((ee) -> dlg.dispose());
            Button btnConfirm = new Button("Confirmer");
            btnConfirm.addActionListener(actionConf -> {
                int responseCode = CodepromoService.getInstance().delete(codepromo.getId());

                if (responseCode == 200) {
                    currentCodepromo = null;
                    dlg.dispose();
                    codepromoModel.remove();
                    this.refreshTheme();
                } else {
                    Dialog.show("Erreur", "Erreur de suppression du codepromo. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            });
            Container btnContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            btnContainer.addAll(btnClose, btnConfirm);
            dlg.addComponent(BorderLayout.SOUTH, btnContainer);
            dlg.show(800, 800, 0, 0);
        });

        btnsContainer.add(BorderLayout.CENTER, editBtn);
        btnsContainer.add(BorderLayout.EAST, deleteBtn);

        codepromoModel.addAll(
                codeLabel, dateDebutLabel, dateFinLabel,
                btnsContainer
        );

        return codepromoModel;
    }
}
