package com.khedmetna.gui.back.activite;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.khedmetna.entities.TypeActivite;
import com.khedmetna.services.TypeActiviteService;

import java.util.ArrayList;

public class ChooseTypeActivite extends Form {

    Form previousForm;
    Label nomLabel;
    Button chooseBtn;
    Container btnsContainer;

    public ChooseTypeActivite(Form previous) {
        super("Choisir un typeActivite", new BoxLayout(BoxLayout.Y_AXIS));

        previousForm = previous;
        addGUIs();

        super.getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    public void refresh() {
        this.removeAll();
        addGUIs();
        this.refreshTheme();
    }

    private void addGUIs() {
        ArrayList<TypeActivite> listTypeActivites = TypeActiviteService.getInstance().getAll();
        if (listTypeActivites.size() > 0) {
            for (TypeActivite typeActivites : listTypeActivites) {
                this.add(makeTypeActiviteModel(typeActivites));
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    private Component makeTypeActiviteModel(TypeActivite typeActivite) {
        Container typeActiviteModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        typeActiviteModel.setUIID("containerRounded");

        nomLabel = new Label("Nom : " + typeActivite.getNom());
        nomLabel.setUIID("labelDefault");

        chooseBtn = new Button("Choisir");
        chooseBtn.addActionListener(l -> {
            Manage.selectedTypeActivite = typeActivite;
            ((Manage) previousForm).refreshTypeActivite();
            previousForm.showBack();
        });

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");
        btnsContainer.add(BorderLayout.CENTER, chooseBtn);

        typeActiviteModel.addAll(
                nomLabel,
                btnsContainer
        );

        return typeActiviteModel;
    }
}
