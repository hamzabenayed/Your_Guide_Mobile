package com.khedmetna.gui.back.livraison;

import com.codename1.components.ImageViewer;
import com.codename1.components.InteractionDialog;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.khedmetna.entities.Livraison;
import com.khedmetna.services.LivraisonService;
import com.khedmetna.utils.Statics;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DisplayAll extends Form {

    public static Livraison currentLivraison = null;
    Button addBtn;

    public DisplayAll(Form previous) {
        super("Livraisons", new BoxLayout(BoxLayout.Y_AXIS));

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

        ArrayList<Livraison> listLivraisons = LivraisonService.getInstance().getAll();
        if (listLivraisons.size() > 0) {
            for (Livraison listLivraison : listLivraisons) {
                this.add(makeLivraisonModel(listLivraison));
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    private void addActions() {
        addBtn.addActionListener(action -> {
            currentLivraison = null;
            new Manage(this).show();
        });
    }

    Label dateLabel, nomLivreurLabel, prenomLivreurLabel, telLivreurLabel, adresseLivraisonLabel, panierLabel;

    Button editBtn, deleteBtn;
    Container btnsContainer;

    private Component makeLivraisonModel(Livraison livraison) {
        Container livraisonModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        livraisonModel.setUIID("containerRounded");

        dateLabel = new Label("Date : " + new SimpleDateFormat("dd-MM-yyyy").format(livraison.getDate()));
        dateLabel.setUIID("labelDefault");

        nomLivreurLabel = new Label("NomLivreur : " + livraison.getNomLivreur());
        nomLivreurLabel.setUIID("labelDefault");

        prenomLivreurLabel = new Label("PrenomLivreur : " + livraison.getPrenomLivreur());
        prenomLivreurLabel.setUIID("labelDefault");

        telLivreurLabel = new Label("TelLivreur : " + livraison.getTelLivreur());
        telLivreurLabel.setUIID("labelDefault");

        adresseLivraisonLabel = new Label("AdresseLivraison : " + livraison.getAdresseLivraison());
        adresseLivraisonLabel.setUIID("labelDefault");

        if (livraison.getPanier() != null) {
            panierLabel = new Label("Panier : " + livraison.getPanier().getDescription());
            panierLabel.setUIID("labelDefault");
        }

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");

        editBtn = new Button("Modifier");
        editBtn.setUIID("buttonMain");
        editBtn.addActionListener(action -> {
            currentLivraison = livraison;
            new Manage(this).show();
        });

        deleteBtn = new Button("Supprimer");
        deleteBtn.setUIID("buttonDanger");
        deleteBtn.addActionListener(action -> {
            InteractionDialog dlg = new InteractionDialog("Confirmer la suppression");
            dlg.setLayout(new BorderLayout());
            dlg.add(BorderLayout.CENTER, new Label("Voulez vous vraiment supprimer ce livraison ?"));
            Button btnClose = new Button("Annuler");
            btnClose.addActionListener((ee) -> dlg.dispose());
            Button btnConfirm = new Button("Confirmer");
            btnConfirm.addActionListener(actionConf -> {
                int responseCode = LivraisonService.getInstance().delete(livraison.getId());

                if (responseCode == 200) {
                    currentLivraison = null;
                    dlg.dispose();
                    livraisonModel.remove();
                    this.refreshTheme();
                } else {
                    Dialog.show("Erreur", "Erreur de suppression du livraison. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            });
            Container btnContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            btnContainer.addAll(btnClose, btnConfirm);
            dlg.addComponent(BorderLayout.SOUTH, btnContainer);
            dlg.show(800, 800, 0, 0);
        });

        btnsContainer.add(BorderLayout.CENTER, editBtn);
        btnsContainer.add(BorderLayout.EAST, deleteBtn);

        livraisonModel.addAll(
                dateLabel, nomLivreurLabel, prenomLivreurLabel, telLivreurLabel, adresseLivraisonLabel, panierLabel,
                btnsContainer
        );

        return livraisonModel;
    }
}
