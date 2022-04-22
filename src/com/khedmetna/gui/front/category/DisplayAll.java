package com.khedmetna.gui.front.category;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.khedmetna.entities.Category;
import com.khedmetna.services.CategoryService;

import java.util.ArrayList;

public class DisplayAll extends Form {

    Label nameLabel;
    Button chooseBtn;
    Container btnsContainer;

    public DisplayAll(Form previous) {
        super("Categories", new BoxLayout(BoxLayout.Y_AXIS));
        addGUIs();
        super.getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    private void addGUIs() {
        ArrayList<Category> listCategorys = CategoryService.getInstance().getAll();

        if (listCategorys.size() > 0) {
            for (Category category : listCategorys) {
                Component model = makeCategoryModel(category);
                this.add(model);
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    private Component makeCategoryModel(Category category) {
        Container categoryModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        categoryModel.setUIID("containerRounded");

        nameLabel = new Label("Name : " + category.getName());
        nameLabel.setUIID("labelDefault");

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");

        chooseBtn = new Button("Afficher Produits");
        chooseBtn.addActionListener(l -> new DisplayProdByCat(this, category.getId()).show());

        btnsContainer.add(BorderLayout.CENTER, chooseBtn);

        categoryModel.addAll(
                nameLabel,
                btnsContainer
        );

        return categoryModel;
    }
}
