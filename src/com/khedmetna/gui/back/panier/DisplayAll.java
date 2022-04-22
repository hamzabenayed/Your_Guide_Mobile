package com.khedmetna.gui.back.panier;

import com.codename1.components.ImageViewer;
import com.codename1.components.InteractionDialog;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.khedmetna.entities.Panier;
import com.khedmetna.services.PanierService;
import com.khedmetna.utils.Statics;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DisplayAll extends Form {

    
    public static Panier currentPanier = null;
    Button addBtn;

    public DisplayAll(Form previous) {
        super("Paniers", new BoxLayout(BoxLayout.Y_AXIS));

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

        ArrayList<Panier> listPaniers = PanierService.getInstance().getAll();
        if (listPaniers.size() > 0) {
            for (Panier listPanier : listPaniers) {
                this.add(makePanierModel(listPanier));
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    private void addActions() {
        addBtn.addActionListener(action -> {
            currentPanier = null;
            new Manage(this).show();
        });
    }

    Label descriptionLabel  ;
    
    Button editBtn, deleteBtn;
    Container btnsContainer;

    private Component makePanierModel(Panier panier) {
        Container panierModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        panierModel.setUIID("containerRounded");
        
        
        descriptionLabel = new Label("Description : " + panier.getDescription());
        descriptionLabel.setUIID("labelDefault");
        
        
        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");

        editBtn = new Button("Modifier");
        editBtn.setUIID("buttonMain");
        editBtn.addActionListener(action -> {
            currentPanier = panier;
            new Manage(this).show();
        });

        deleteBtn = new Button("Supprimer");
        deleteBtn.setUIID("buttonDanger");
        deleteBtn.addActionListener(action -> {
            InteractionDialog dlg = new InteractionDialog("Confirmer la suppression");
            dlg.setLayout(new BorderLayout());
            dlg.add(BorderLayout.CENTER, new Label("Voulez vous vraiment supprimer ce panier ?"));
            Button btnClose = new Button("Annuler");
            btnClose.addActionListener((ee) -> dlg.dispose());
            Button btnConfirm = new Button("Confirmer");
            btnConfirm.addActionListener(actionConf -> {
                int responseCode = PanierService.getInstance().delete(panier.getId());

                if (responseCode == 200) {
                    currentPanier = null;
                    dlg.dispose();
                    panierModel.remove();
                    this.refreshTheme();
                } else {
                    Dialog.show("Erreur", "Erreur de suppression du panier. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            });
            Container btnContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            btnContainer.addAll(btnClose, btnConfirm);
            dlg.addComponent(BorderLayout.SOUTH, btnContainer);
            dlg.show(800, 800, 0, 0);
        });

        btnsContainer.add(BorderLayout.CENTER, editBtn);
        btnsContainer.add(BorderLayout.EAST, deleteBtn);

        panierModel.addAll(
                descriptionLabel,
                
                btnsContainer
        );

        return panierModel;
    }
}