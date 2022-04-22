package com.khedmetna.gui.front.typeActivite;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.khedmetna.entities.TypeActivite;
import com.khedmetna.services.TypeActiviteService;

import java.util.ArrayList;

public class DisplayAll extends Form {

    TextField searchTF;
    ArrayList<Component> componentModels;
    Label nomLabel;
    Container btnsContainer;

    public DisplayAll(Form previous) {
        super("TypeActivites", new BoxLayout(BoxLayout.Y_AXIS));
        addGUIs();
        super.getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    private void addGUIs() {
        ArrayList<TypeActivite> listTypeActivites = TypeActiviteService.getInstance().getAll();
        componentModels = new ArrayList<>();

        searchTF = new TextField("", "Chercher un typeActivite");
        searchTF.addDataChangedListener((d, t) -> {
            if (componentModels.size() > 0) {
                for (Component componentModel : componentModels) {
                    this.removeComponent(componentModel);
                }
            }
            componentModels = new ArrayList<>();
            for (TypeActivite typeActivite : listTypeActivites) {
                if (typeActivite.getNom().startsWith(searchTF.getText())) {
                    Component model = makeTypeActiviteModel(typeActivite);
                    this.add(model);
                    componentModels.add(model);
                }
            }
            this.revalidate();
        });
        this.add(searchTF);

        if (listTypeActivites.size() > 0) {
            for (TypeActivite typeActivite : listTypeActivites) {
                Component model = makeTypeActiviteModel(typeActivite);
                this.add(model);
                componentModels.add(model);
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

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");

        typeActiviteModel.addAll(
                nomLabel,
                btnsContainer
        );

        return typeActiviteModel;
    }
}
