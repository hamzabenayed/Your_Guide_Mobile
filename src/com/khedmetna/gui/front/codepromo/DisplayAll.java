package com.khedmetna.gui.front.codepromo;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.khedmetna.entities.Codepromo;
import com.khedmetna.services.CodepromoService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DisplayAll extends Form {

    Label codeLabel, dateDebutLabel, dateFinLabel;
    Container btnsContainer;

    public DisplayAll(Form previous) {
        super("Codepromos", new BoxLayout(BoxLayout.Y_AXIS));
        addGUIs();
        super.getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    private void addGUIs() {
        ArrayList<Codepromo> listCodepromos = CodepromoService.getInstance().getAll();

        if (listCodepromos.size() > 0) {
            for (Codepromo codepromo : listCodepromos) {
                Component model = makeCodepromoModel(codepromo);
                this.add(model);
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

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");

        codepromoModel.addAll(
                codeLabel, dateDebutLabel, dateFinLabel,
                btnsContainer
        );

        return codepromoModel;
    }
}
