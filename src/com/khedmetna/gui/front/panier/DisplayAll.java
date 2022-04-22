package com.khedmetna.gui.front.panier;

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

    public DisplayAll(Form previous) {
        super("Paniers", new BoxLayout(BoxLayout.Y_AXIS));
        addGUIs();
        super.getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    private void addGUIs() {
        ArrayList<Panier> listPaniers = PanierService.getInstance().getAll();
       
        if (listPaniers.size() > 0) {
            for (Panier panier : listPaniers) {
                Component model = makePanierModel(panier);
                this.add(model);
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    Label descriptionLabel  ;
    
    Container btnsContainer;

    private Component makePanierModel(Panier panier) {
        Container panierModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        panierModel.setUIID("containerRounded");

        
        
        descriptionLabel = new Label("Description : " + panier.getDescription());
        descriptionLabel.setUIID("labelDefault");
        

        

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");

        panierModel.addAll(
                descriptionLabel,
                
                btnsContainer
        );

        return panierModel;
    }
}