package com.khedmetna.gui.back.typeActivite;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.khedmetna.entities.TypeActivite;
import com.khedmetna.services.TypeActiviteService;

public class Manage extends Form {

    TypeActivite currentTypeActivite;
    Label nomLabel;
    TextField nomTF;
    Button manageButton;
    Form previous;

    public Manage(Form previous) {
        super(DisplayAll.currentTypeActivite == null ? "Ajouter" : "Modifier", new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;

        currentTypeActivite = DisplayAll.currentTypeActivite;

        addGUIs();
        addActions();
        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    private void addGUIs() {
        nomLabel = new Label("Nom : ");
        nomLabel.setUIID("labelDefault");
        nomTF = new TextField();
        nomTF.setHint("Tapez le nom");

        if (currentTypeActivite == null) {
            manageButton = new Button("Ajouter");
        } else {
            nomTF.setText(currentTypeActivite.getNom());
            manageButton = new Button("Modifier");
        }
        manageButton.setUIID("buttonMain");

        Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        container.setUIID("containerRounded");

        container.addAll(
                nomLabel, nomTF,
                manageButton
        );

        this.addAll(container);
    }

    private void addActions() {

        if (currentTypeActivite == null) {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = TypeActiviteService.getInstance().add(
                            new TypeActivite(
                                    nomTF.getText()
                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "TypeActivite ajouté avec succes", new Command("Ok"));
                        showBackAndRefresh();
                    } else {
                        Dialog.show("Erreur", "Erreur d'ajout de typeActivite. Code d'erreur : " + responseCode, new Command("Ok"));
                    }
                }
            });
        } else {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = TypeActiviteService.getInstance().edit(
                            new TypeActivite(
                                    currentTypeActivite.getId(),
                                    nomTF.getText()
                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "TypeActivite modifié avec succes", new Command("Ok"));
                        showBackAndRefresh();
                    } else {
                        Dialog.show("Erreur", "Erreur de modification de typeActivite. Code d'erreur : " + responseCode, new Command("Ok"));
                    }
                }
            });
        }
    }

    private void showBackAndRefresh() {
        ((DisplayAll) previous).refresh();
        previous.showBack();
    }

    private boolean controleDeSaisie() {

        if (nomTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le nom", new Command("Ok"));
            return false;
        }

        return true;
    }
}
