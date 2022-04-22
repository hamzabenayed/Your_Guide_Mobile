package com.khedmetna.gui.front.commande;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.khedmetna.entities.Commande;
import com.khedmetna.services.CommandeService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DisplayAll extends Form {

    public static Commande currentCommande = null;

    Label dateLabel, etatLabel, commentaireLabel, adresseLabel, totalcostLabel, productLabel;
    Container btnsContainer;

    public DisplayAll(Form previous) {
        super("Commandes", new BoxLayout(BoxLayout.Y_AXIS));
        addGUIs();
        super.getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    private void addGUIs() {
        ArrayList<Commande> listCommandes = CommandeService.getInstance().getAll();

        if (listCommandes.size() > 0) {
            for (Commande commande : listCommandes) {
                Component model = makeCommandeModel(commande);
                this.add(model);
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
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

        commandeModel.addAll(
                dateLabel, etatLabel, commentaireLabel, adresseLabel, totalcostLabel, productLabel,
                btnsContainer
        );

        return commandeModel;
    }
}
