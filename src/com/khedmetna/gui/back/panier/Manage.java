package com.khedmetna.gui.back.panier;


import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;

import com.khedmetna.entities.Panier;
import com.khedmetna.services.PanierService;
import com.khedmetna.utils.Statics;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class Manage extends Form {

    

    Panier currentPanier;

    
        
    Label descriptionLabel ;
    TextField 
    descriptionTF  ,elemTF;
    
    
    
    Button manageButton;

    Form previous;

    public Manage(Form previous) {
        super(DisplayAll.currentPanier == null ?  "Ajouter" :  "Modifier", new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;

        
        currentPanier = DisplayAll.currentPanier;

        addGUIs();
        addActions();

        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    

    private void addGUIs() {
        
        
        descriptionLabel = new Label("Description : ");
        descriptionLabel.setUIID("labelDefault");
        descriptionTF = new TextField();
        descriptionTF.setHint("Tapez le description");

        

        

        if (currentPanier == null) {
            
            
            manageButton = new Button("Ajouter");
        } else {
    
            
            descriptionTF.setText(currentPanier.getDescription());
            
            

            manageButton = new Button("Modifier");
        }
        manageButton.setUIID("buttonMain");

        Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        container.setUIID("containerRounded");

        container.addAll(
            
            descriptionLabel, descriptionTF,
            
            manageButton
        );

        this.addAll(container);
    }

    private void addActions() {
        
        if (currentPanier == null) {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = PanierService.getInstance().add(
                            new Panier(
                                    
                                    
                                    descriptionTF.getText()
                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Panier ajouté avec succes", new Command("Ok"));
                        showBackAndRefresh();
                    } else {
                        Dialog.show("Erreur", "Erreur d'ajout de panier. Code d'erreur : " + responseCode, new Command("Ok"));
                    }
                }
            });
        } else {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = PanierService.getInstance().edit(
                            new Panier(
                                    currentPanier.getId(),
                                    
                                    
                                    descriptionTF.getText()

                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Panier modifié avec succes", new Command("Ok"));
                        showBackAndRefresh();
                    } else {
                        Dialog.show("Erreur", "Erreur de modification de panier. Code d'erreur : " + responseCode, new Command("Ok"));
                    }
                }
            });
        }
    }

    private void showBackAndRefresh(){
        ((DisplayAll) previous).refresh();
        previous.showBack();
    }

    private boolean controleDeSaisie() {

        
        
        if (descriptionTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le description", new Command("Ok"));
            return false;
        }
        
        
        

        

        
             
        return true;
    }
}