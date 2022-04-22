package com.khedmetna.gui.back.promo;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.khedmetna.entities.Codepromo;
import com.khedmetna.services.CodepromoService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ChooseCodepromo extends Form {

    Form previousForm;
    Label codeLabel, dateDebutLabel, dateFinLabel;
    Button chooseBtn;
    Container btnsContainer;

    public ChooseCodepromo(Form previous) {
        super("Choisir un codepromo", new BoxLayout(BoxLayout.Y_AXIS));

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
        ArrayList<Codepromo> listCodepromos = CodepromoService.getInstance().getAll();
        if (listCodepromos.size() > 0) {
            for (Codepromo codepromos : listCodepromos) {
                this.add(makeCodepromoModel(codepromos));
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    private Component makeCodepromoModel(Codepromo codepromo) {
        Container codepromoModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        codepromoModel.setUIID("containerRounded");

        codeLabel = new Label("Code : " + codepromo.getCode());
        codeLabel.setUIID("labelDefault");

        dateDebutLabel = new Label("DateDebut : " + new SimpleDateFormat("dd-MM-yyyy").format(codepromo.getDateDebut()));
        dateDebutLabel.setUIID("labelDefault");

        dateFinLabel = new Label("DateFin : " + new SimpleDateFormat("dd-MM-yyyy").format(codepromo.getDateFin()));
        dateFinLabel.setUIID("labelDefault");

        chooseBtn = new Button("Choisir");
        chooseBtn.addActionListener(l -> {
            Manage.selectedCodepromo = codepromo;
            ((Manage) previousForm).refreshCodepromo();
            previousForm.showBack();
        });

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");
        btnsContainer.add(BorderLayout.CENTER, chooseBtn);

        codepromoModel.addAll(
                codeLabel, dateDebutLabel, dateFinLabel,
                btnsContainer
        );

        return codepromoModel;
    }
}
