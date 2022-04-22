package com.khedmetna.gui.front.promo;

import com.codename1.components.ImageViewer;
import com.codename1.components.ToastBar;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.khedmetna.entities.Promo;
import com.khedmetna.services.PromoService;
import com.khedmetna.utils.Statics;

import java.util.ArrayList;
import java.util.Collections;

public class DisplayAll extends Form {

    public static Promo currentPromo = null;
    public static String promoCompareVar;
    Resources theme = UIManager.initFirstTheme("/theme");
    PickerComponent sortPicker;

    Label pourcentageLabel, codepromoLabel;
    ImageViewer imageIV;
    Container btnsContainer;
    ArrayList<Component> componentModels;
    ArrayList<Promo> listPromos;

    public DisplayAll() {
        super("Promos", new BoxLayout(BoxLayout.Y_AXIS));
        addGUIs();
        super.getToolbar().hideToolbar();
    }

    private void addGUIs() {
        listPromos = PromoService.getInstance().getAll();
        componentModels = new ArrayList<>();

        sortPicker = PickerComponent.createStrings("codepromo", "dateRetour", "nbrPromours", "prix", "ville").label("Trier par");
        Button sortButton = new Button("Trier");
        sortButton.addActionListener((l) -> {
            if (componentModels.size() > 0) {
                for (Component componentModel : componentModels) {
                    this.removeComponent(componentModel);
                }
            }
            componentModels = new ArrayList<>();
            ArrayList<Promo> sortedList = listPromos;
            promoCompareVar = sortPicker.getPicker().getSelectedString();
            Collections.sort(sortedList);
            for (Promo promo : sortedList) {
                Component model = makePromoModel(promo);
                this.add(model);
                componentModels.add(model);
            }
            this.revalidate();

            ToastBar.getInstance().setPosition(BOTTOM);
            ToastBar.Status status = ToastBar.getInstance().createStatus();
            status.setShowProgressIndicator(false);
            status.setMessage("Trié par " + sortPicker.getPicker().getSelectedString());
            status.setExpires(5000);
            status.show();
        });
        Container sortContainer = new Container(new BorderLayout());

        sortContainer.add(BorderLayout.WEST, sortPicker);
        sortContainer.add(BorderLayout.EAST, sortButton);

        this.add(sortContainer);
        if (listPromos.size() > 0) {
            for (Promo promo : listPromos) {
                Component model = makePromoModel(promo);
                this.add(model);
                componentModels.add(model);
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
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

        promoModel.addAll(
                pourcentageLabel, codepromoLabel,
                imageIV,
                btnsContainer
        );

        return promoModel;
    }
}
