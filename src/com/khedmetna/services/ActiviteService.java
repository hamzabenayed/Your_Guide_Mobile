package com.khedmetna.services;

import com.codename1.components.InfiniteProgress;
import com.codename1.io.*;
import com.codename1.ui.events.ActionListener;
import com.khedmetna.entities.Activite;
import com.khedmetna.entities.TypeActivite;
import com.khedmetna.utils.Statics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ActiviteService {

    public static ActiviteService instance = null;
    public int resultCode;
    private ConnectionRequest cr;
    private ArrayList<Activite> listActivites;

    private ActiviteService() {
        cr = new ConnectionRequest();
    }

    public static ActiviteService getInstance() {
        if (instance == null) {
            instance = new ActiviteService();
        }
        return instance;
    }

    public ArrayList<Activite> getAll() {
        listActivites = new ArrayList<>();

        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/activite");
        cr.setHttpMethod("GET");

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                if (cr.getResponseCode() == 200) {
                    listActivites = getList();
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

        return listActivites;
    }

    private ArrayList<Activite> getList() {
        try {
            Map<String, Object> parsedJson = new JSONParser().parseJSON(new CharArrayReader(
                    new String(cr.getResponseData()).toCharArray()
            ));
            List<Map<String, Object>> list = (List<Map<String, Object>>) parsedJson.get("root");

            for (Map<String, Object> obj : list) {
                Activite activite = new Activite(
                        (int) Float.parseFloat(obj.get("id").toString()),
                        (String) obj.get("nom"),
                        (String) obj.get("lieu"),
                        (String) obj.get("description"),
                        (String) obj.get("image"),
                        makeTypeActivite((Map<String, Object>) obj.get("typeActivite")),
                        (String) obj.get("longitude"),
                        (String) obj.get("lattitude")
                );

                listActivites.add(activite);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return listActivites;
    }

    public TypeActivite makeTypeActivite(Map<String, Object> obj) {
        if (obj == null) {
            return null;
        }
        return new TypeActivite(
                (int) Float.parseFloat(obj.get("id").toString()),
                (String) obj.get("nom")
        );
    }

    public int add(Activite activite) {
        return manage(activite, false, true);
    }

    public int edit(Activite activite, boolean imageEdited) {
        return manage(activite, true, imageEdited);
    }

    public int manage(Activite activite, boolean isEdit, boolean imageEdited) {

        MultipartRequest cr = new MultipartRequest();
        cr.setFilename("file", "Activite.jpg");

        cr.setHttpMethod("POST");
        if (isEdit) {
            cr.setUrl(Statics.BASE_URL + "/activite/edit");
            cr.addArgumentNoEncoding("id", String.valueOf(activite.getId()));
        } else {
            cr.setUrl(Statics.BASE_URL + "/activite/add");
        }

        if (imageEdited) {
            try {
                cr.addData("file", activite.getImage(), "image/jpeg");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            cr.addArgumentNoEncoding("image", activite.getImage());
        }

        cr.addArgumentNoEncoding("nom", activite.getNom());
        cr.addArgumentNoEncoding("lieu", activite.getLieu());
        cr.addArgumentNoEncoding("description", activite.getDescription());
        cr.addArgumentNoEncoding("typeActivite", String.valueOf(activite.getTypeActivite().getId()));
        cr.addArgumentNoEncoding("longitude", activite.getLongitude());
        cr.addArgumentNoEncoding("lattitude", activite.getLattitude());

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

    public int delete(int activiteId) {
        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/activite/delete");
        cr.setHttpMethod("POST");
        cr.addArgument("id", String.valueOf(activiteId));

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
