package com.khedmetna.gui.back.category;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.khedmetna.entities.Category;
import com.khedmetna.services.CategoryService;

public class Manage extends Form {

    Category currentCategory;

    Label nameLabel;
    TextField nameTF;

    Button manageButton;

    Form previous;

    public Manage(Form previous) {
        super(DisplayAll.currentCategory == null ? "Ajouter" : "Modifier", new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;

        currentCategory = DisplayAll.currentCategory;

        addGUIs();
        addActions();

        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    private void addGUIs() {

        nameLabel = new Label("Name : ");
        nameLabel.setUIID("labelDefault");
        nameTF = new TextField();
        nameTF.setHint("Tapez le name");

        if (currentCategory == null) {

            manageButton = new Button("Ajouter");
        } else {

            nameTF.setText(currentCategory.getName());

            manageButton = new Button("Modifier");
        }
        manageButton.setUIID("buttonMain");

        Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        container.setUIID("containerRounded");

        container.addAll(
                nameLabel, nameTF,
                manageButton
        );

        this.addAll(container);
    }

    private void addActions() {

        if (currentCategory == null) {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = CategoryService.getInstance().add(
                            new Category(
                                    nameTF.getText()
                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Category ajouté avec succes", new Command("Ok"));
                        showBackAndRefresh();
                    } else {
                        Dialog.show("Erreur", "Erreur d'ajout de category. Code d'erreur : " + responseCode, new Command("Ok"));
                    }
                }
            });
        } else {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = CategoryService.getInstance().edit(
                            new Category(
                                    currentCategory.getId(),
                                    nameTF.getText()
                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Category modifié avec succes", new Command("Ok"));
                        showBackAndRefresh();
                    } else {
                        Dialog.show("Erreur", "Erreur de modification de category. Code d'erreur : " + responseCode, new Command("Ok"));
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

        if (nameTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le name", new Command("Ok"));
            return false;
        }

        return true;
    }
}
