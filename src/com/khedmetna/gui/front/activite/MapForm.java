/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khedmetna.gui.front.activite;

import com.codename1.googlemaps.MapContainer;
import com.codename1.maps.Coord;
import com.codename1.ui.Display;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Toolbar;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.plaf.Style;

public class MapForm extends Form {

    MapContainer cnt = null;
    float longitude, lattitude;

    public MapForm(Form previous, String title, float longitude, float lattitude) {
        super("this");
        this.longitude = longitude;
        this.lattitude = lattitude;

        addGUIs();

        super.setToolbar(new Toolbar());
        super.setTitle(title);
        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    public void addGUIs() {

        try {
            cnt = new MapContainer("AIzaSyD3GgFpAwKTASktfV7WMlj24DxGa39Ztdo");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Style s = new Style();
        s.setFgColor(0xff0000);
        s.setBgTransparency(0);
        FontImage markerImg = FontImage.createMaterial(FontImage.MATERIAL_PLACE, s, Display.getInstance().convertToPixels(3));

        //36.8189700    10.1657900
        System.out.println("coords : " + longitude + " , " + lattitude);
        cnt.zoom(new Coord(longitude, lattitude), 15);
        cnt.setCameraPosition(new Coord(longitude, lattitude));

        this.setLayout(new BorderLayout());
        this.addComponent(BorderLayout.CENTER, cnt);
    }

}
