package com.khedmetna.gui.back.commande;

import com.codename1.components.InteractionDialog;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.khedmetna.entities.Commande;
import com.khedmetna.services.CommandeService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DisplayAll extends Form {

    public static Commande currentCommande = null;
    Button addBtn;
    Label dateLabel, etatLabel, commentaireLabel, adresseLabel, totalcostLabel, productLabel;
    Button editBtn, deleteBtn;
    Container btnsContainer;

    public DisplayAll(Form previous) {
        super("Commandes", new BoxLayout(BoxLayout.Y_AXIS));

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

        ArrayList<Commande> listCommandes = CommandeService.getInstance().getAll();
        if (listCommandes.size() > 0) {
            for (Commande listCommande : listCommandes) {
                this.add(makeCommandeModel(listCommande));
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    private void addActions() {
        addBtn.addActionListener(action -> {
            currentCommande = null;
            new Manage(this).show();
        });
    }

    private Component makeCommandeModel(Commande commande) {
        Container commandeModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        commandeModel.setUIID("containerRounded");

        dateLabel = new Label("Date : " + new SimpleDateFormat("dd-MM-yyyy").format(commande.getDate()));

        dateLabel.setUIID("labelDefault");

        etatLabel = new Label("Etat : " + commande.getEtat());

        etatLabel.setUIID("labelDefault");

        commentaireLabel = new Label("Commentaire : " + commande.getCommentaire());

        commentaireLabel.setUIID("labelDefault");

        adresseLabel = new Label("Adresse : " + commande.getAdresse());

        adresseLabel.setUIID("labelDefault");

        totalcostLabel = new Label("Totalcost : " + commande.getTotalcost());

        totalcostLabel.setUIID("labelDefault");

        productLabel = new Label("Product : " + commande.getProduct());

        productLabel.setUIID("labelDefault");

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");

        editBtn = new Button("Modifier");
        editBtn.setUIID("buttonMain");
        editBtn.addActionListener(action -> {
            currentCommande = commande;
            new Manage(this).show();
        });

        deleteBtn = new Button("Supprimer");
        deleteBtn.setUIID("buttonDanger");
        deleteBtn.addActionListener(action -> {
            InteractionDialog dlg = new InteractionDialog("Confirmer la suppression");
            dlg.setLayout(new BorderLayout());
            dlg.add(BorderLayout.CENTER, new Label("Voulez vous vraiment supprimer ce commande ?"));
            Button btnClose = new Button("Annuler");
            btnClose.addActionListener((ee) -> dlg.dispose());
            Button btnConfirm = new Button("Confirmer");
            btnConfirm.addActionListener(actionConf -> {
                int responseCode = CommandeService.getInstance().delete(commande.getId());

                if (responseCode == 200) {
                    currentCommande = null;
                    dlg.dispose();
                    commandeModel.remove();
                    this.refreshTheme();
                } else {
                    Dialog.show("Erreur", "Erreur de suppression du commande. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            });
            Container btnContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            btnContainer.addAll(btnClose, btnConfirm);
            dlg.addComponent(BorderLayout.SOUTH, btnContainer);
            dlg.show(800, 800, 0, 0);
        });

        btnsContainer.add(BorderLayout.CENTER, editBtn);
        btnsContainer.add(BorderLayout.EAST, deleteBtn);

        commandeModel.addAll(
                dateLabel, etatLabel, commentaireLabel, adresseLabel, totalcostLabel, productLabel,
                btnsContainer
        );

        return commandeModel;
    }
}
