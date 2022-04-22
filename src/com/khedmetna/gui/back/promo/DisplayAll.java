package com.khedmetna.gui.back.promo;

import com.codename1.components.ImageViewer;
import com.codename1.components.InteractionDialog;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.khedmetna.entities.Promo;
import com.khedmetna.services.PromoService;
import com.khedmetna.utils.Statics;

import java.util.ArrayList;

public class DisplayAll extends Form {

    public static Promo currentPromo = null;
    Resources theme = UIManager.initFirstTheme("/theme");
    Button addBtn;
    Label pourcentageLabel, codepromoLabel;
    ImageViewer imageIV;
    Button editBtn, deleteBtn;
    Container btnsContainer;

    public DisplayAll(Form previous) {
        super("Promos", new BoxLayout(BoxLayout.Y_AXIS));

        addGUIs();
        addActions();

        super.getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    public void refresh() {
        this.removeAll();
        addGUIs();
        addActions();
        this.refreshTheme();
    }

    private void addGUIs() {
        addBtn = new Button("Ajouter");
        addBtn.setUIID("buttonWhiteCenter");

        this.add(addBtn);

        ArrayList<Promo> listPromos = PromoService.getInstance().getAll();
        if (listPromos.size() > 0) {
            for (Promo listPromo : listPromos) {
                this.add(makePromoModel(listPromo));
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    private void addActions() {
        addBtn.addActionListener(action -> {
            currentPromo = null;
            new Manage(this).show();
        });
    }

    private Component makePromoModel(Promo promo) {
        Container promoModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        promoModel.setUIID("containerRounded");

        pourcentageLabel = new Label("Pourcentage : " + promo.getPourcentage());

        pourcentageLabel.setUIID("labelDefault");

        codepromoLabel = new Label("Codepromo : " + promo.getCodepromo());

        codepromoLabel.setUIID("labelDefault");

        codepromoLabel = new Label("Codepromo : " + promo.getCodepromo().getCode());
        codepromoLabel.setUIID("labelDefault");

        if (promo.getImage() != null) {
            String url = Statics.PROMO_IMAGE_URL + promo.getImage();
            Image image = URLImage.createToStorage(
                    EncodedImage.createFromImage(theme.getImage("default.jpg").fill(1100, 500), false),
                    url,
                    url,
                    URLImage.RESIZE_SCALE
            );
            imageIV = new ImageViewer(image);
        } else {
            imageIV = new ImageViewer(theme.getImage("default.jpg").fill(1100, 500));
        }
        imageIV.setFocusable(false);

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");

        editBtn = new Button("Modifier");
        editBtn.setUIID("buttonMain");
        editBtn.addActionListener(action -> {
            currentPromo = promo;
            new Manage(this).show();
        });

        deleteBtn = new Button("Supprimer");
        deleteBtn.setUIID("buttonDanger");
        deleteBtn.addActionListener(action -> {
            InteractionDialog dlg = new InteractionDialog("Confirmer la suppression");
            dlg.setLayout(new BorderLayout());
            dlg.add(BorderLayout.CENTER, new Label("Voulez vous vraiment supprimer ce promo ?"));
            Button btnClose = new Button("Annuler");
            btnClose.addActionListener((ee) -> dlg.dispose());
            Button btnConfirm = new Button("Confirmer");
            btnConfirm.addActionListener(actionConf -> {
                int responseCode = PromoService.getInstance().delete(promo.getId());

                if (responseCode == 200) {
                    currentPromo = null;
                    dlg.dispose();
                    promoModel.remove();
                    this.refreshTheme();
                } else {
                    Dialog.show("Erreur", "Erreur de suppression du promo. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            });
            Container btnContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            btnContainer.addAll(btnClose, btnConfirm);
            dlg.addComponent(BorderLayout.SOUTH, btnContainer);
            dlg.show(800, 800, 0, 0);
        });

        btnsContainer.add(BorderLayout.CENTER, editBtn);
        btnsContainer.add(BorderLayout.EAST, deleteBtn);

        promoModel.addAll(
                pourcentageLabel, codepromoLabel,
                imageIV,
                btnsContainer
        );

        return promoModel;
    }
}
