package com.khedmetna.gui.back.typeActivite;

import com.codename1.components.InteractionDialog;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.khedmetna.entities.TypeActivite;
import com.khedmetna.services.TypeActiviteService;

import java.util.ArrayList;

public class DisplayAll extends Form {

    public static TypeActivite currentTypeActivite = null;
    Button addBtn;
    Label nomLabel;
    Button editBtn, deleteBtn;
    Container btnsContainer;

    public DisplayAll(Form previous) {
        super("TypeActivites", new BoxLayout(BoxLayout.Y_AXIS));

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

        ArrayList<TypeActivite> listTypeActivites = TypeActiviteService.getInstance().getAll();
        if (listTypeActivites.size() > 0) {
            for (TypeActivite listTypeActivite : listTypeActivites) {
                this.add(makeTypeActiviteModel(listTypeActivite));
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    private void addActions() {
        addBtn.addActionListener(action -> {
            currentTypeActivite = null;
            new Manage(this).show();
        });
    }

    private Component makeTypeActiviteModel(TypeActivite typeActivite) {
        Container typeActiviteModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        typeActiviteModel.setUIID("containerRounded");

        nomLabel = new Label("Nom : " + typeActivite.getNom());

        nomLabel.setUIID("labelDefault");

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");

        editBtn = new Button("Modifier");
        editBtn.setUIID("buttonMain");
        editBtn.addActionListener(action -> {
            currentTypeActivite = typeActivite;
            new Manage(this).show();
        });

        deleteBtn = new Button("Supprimer");
        deleteBtn.setUIID("buttonDanger");
        deleteBtn.addActionListener(action -> {
            InteractionDialog dlg = new InteractionDialog("Confirmer la suppression");
            dlg.setLayout(new BorderLayout());
            dlg.add(BorderLayout.CENTER, new Label("Voulez vous vraiment supprimer ce typeActivite ?"));
            Button btnClose = new Button("Annuler");
            btnClose.addActionListener((ee) -> dlg.dispose());
            Button btnConfirm = new Button("Confirmer");
            btnConfirm.addActionListener(actionConf -> {
                int responseCode = TypeActiviteService.getInstance().delete(typeActivite.getId());

                if (responseCode == 200) {
                    currentTypeActivite = null;
                    dlg.dispose();
                    typeActiviteModel.remove();
                    this.refreshTheme();
                } else {
                    Dialog.show("Erreur", "Erreur de suppression du typeActivite. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            });
            Container btnContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            btnContainer.addAll(btnClose, btnConfirm);
            dlg.addComponent(BorderLayout.SOUTH, btnContainer);
            dlg.show(800, 800, 0, 0);
        });

        btnsContainer.add(BorderLayout.CENTER, editBtn);
        btnsContainer.add(BorderLayout.EAST, deleteBtn);

        typeActiviteModel.addAll(
                nomLabel,
                btnsContainer
        );

        return typeActiviteModel;
    }
}
