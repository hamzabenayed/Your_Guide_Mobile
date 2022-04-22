package com.khedmetna.services;

import com.codename1.components.InfiniteProgress;
import com.codename1.io.*;
import com.codename1.ui.events.ActionListener;
import com.khedmetna.entities.Activite;
import com.khedmetna.entities.Guide;
import com.khedmetna.utils.Statics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GuideService {

    public static GuideService instance = null;
    public int resultCode;
    private ConnectionRequest cr;
    private ArrayList<Guide> listGuides;

    private GuideService() {
        cr = new ConnectionRequest();
    }

    public static GuideService getInstance() {
        if (instance == null) {
            instance = new GuideService();
        }
        return instance;
    }

    public ArrayList<Guide> getAll() {
        listGuides = new ArrayList<>();

        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/guide");
        cr.setHttpMethod("GET");

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                if (cr.getResponseCode() == 200) {
                    listGuides = getList();
                }

                cr.removeResponseListener(this);
            }
        });

        try {
            cr.setDisposeOnCompletion(new InfiniteProgress().showInfiniteBlocking());
            NetworkManager.getInstance().addToQueueAndWait(cr);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listGuides;
    }

    private ArrayList<Guide> getList() {
        try {
            Map<String, Object> parsedJson = new JSONParser().parseJSON(new CharArrayReader(
                    new String(cr.getResponseData()).toCharArray()
            ));
            List<Map<String, Object>> list = (List<Map<String, Object>>) parsedJson.get("root");

            for (Map<String, Object> obj : list) {
                Guide guide = new Guide(
                        (int) Float.parseFloat(obj.get("id").toString()),
                        (String) obj.get("nom"),
                        (int) Float.parseFloat(obj.get("tel").toString()),
                        makeActivite((Map<String, Object>) obj.get("activite")),
                        (String) obj.get("image")
                );

                listGuides.add(guide);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return listGuides;
    }

    public Activite makeActivite(Map<String, Object> obj) {
        if (obj == null) {
            return null;
        }

        return new Activite(
                (int) Float.parseFloat(obj.get("id").toString()),
                (String) obj.get("nom"),
                (String) obj.get("lieu"),
                (String) obj.get("description"),
                (String) obj.get("image"),
                null,
                (String) obj.get("longitude"),
                (String) obj.get("lattitude")
        );
    }

    public int add(Guide guide) {
        return manage(guide, false, true);
    }

    public int edit(Guide guide, boolean imageEdited) {
        return manage(guide, true, imageEdited);
    }

    public int manage(Guide guide, boolean isEdit, boolean imageEdited) {

        MultipartRequest cr = new MultipartRequest();
        cr.setFilename("file", "Guide.jpg");

        cr.setHttpMethod("POST");
        if (isEdit) {
            cr.setUrl(Statics.BASE_URL + "/guide/edit");
            cr.addArgumentNoEncoding("id", String.valueOf(guide.getId()));
        } else {
            cr.setUrl(Statics.BASE_URL + "/guide/add");
        }

        if (imageEdited) {
            try {
                cr.addData("file", guide.getImage(), "image/jpeg");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            cr.addArgumentNoEncoding("image", guide.getImage());
        }

        cr.addArgumentNoEncoding("nom", guide.getNom());
        cr.addArgumentNoEncoding("tel", String.valueOf(guide.getTel()));
        cr.addArgumentNoEncoding("activite", String.valueOf(guide.getActivite().getId()));

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                resultCode = cr.getResponseCode();
                cr.removeResponseListener(this);
            }
        });
        try {
            cr.setDisposeOnCompletion(new InfiniteProgress().showInfiniteBlocking());
            NetworkManager.getInstance().addToQueueAndWait(cr);
        } catch (Exception ignored) {

        }
        return resultCode;
    }

    public int delete(int guideId) {
        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/guide/delete");
        cr.setHttpMethod("POST");
        cr.addArgument("id", String.valueOf(guideId));

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                cr.removeResponseListener(this);
            }
        });

        try {
            cr.setDisposeOnCompletion(new InfiniteProgress().showInfiniteBlocking());
            NetworkManager.getInstance().addToQueueAndWait(cr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cr.getResponseCode();
    }
}
