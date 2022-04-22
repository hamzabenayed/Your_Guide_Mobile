package com.khedmetna.gui.back.produit;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.khedmetna.entities.Category;
import com.khedmetna.services.CategoryService;

import java.util.ArrayList;

public class ChooseCategory extends Form {

    Form previousForm;
    Label nameLabel;
    Button chooseBtn;
    Container btnsContainer;

    public ChooseCategory(Form previous) {
        super("Choisir un category", new BoxLayout(BoxLayout.Y_AXIS));

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
        ArrayList<Category> listCategorys = CategoryService.getInstance().getAll();
        if (listCategorys.size() > 0) {
            for (Category categorys : listCategorys) {
                this.add(makeCategoryModel(categorys));
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

        chooseBtn = new Button("Choisir");
        chooseBtn.addActionListener(l -> {
            Manage.selectedCategory = category;
            ((Manage) previousForm).refreshCategory();
            previousForm.showBack();
        });

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");
        btnsContainer.add(BorderLayout.CENTER, chooseBtn);

        categoryModel.addAll(
                nameLabel,
                btnsContainer
        );

        return categoryModel;
    }
}
