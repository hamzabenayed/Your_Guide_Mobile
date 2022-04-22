package com.khedmetna.services;

import com.codename1.components.InfiniteProgress;
import com.codename1.io.*;
import com.codename1.ui.events.ActionListener;
import com.khedmetna.entities.TypeActivite;
import com.khedmetna.utils.Statics;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TypeActiviteService {

    public static TypeActiviteService instance = null;
    public int resultCode;
    private ConnectionRequest cr;
    private ArrayList<TypeActivite> listTypeActivites;

    private TypeActiviteService() {
        cr = new ConnectionRequest();
    }

    public static TypeActiviteService getInstance() {
        if (instance == null) {
            instance = new TypeActiviteService();
        }
        return instance;
    }

    public ArrayList<TypeActivite> getAll() {
        listTypeActivites = new ArrayList<>();

        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/typeActivite");
        cr.setHttpMethod("GET");

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                if (cr.getResponseCode() == 200) {
                    listTypeActivites = getList();
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

        return listTypeActivites;
    }

    private ArrayList<TypeActivite> getList() {
        try {
            Map<String, Object> parsedJson = new JSONParser().parseJSON(new CharArrayReader(
                    new String(cr.getResponseData()).toCharArray()
            ));
            List<Map<String, Object>> list = (List<Map<String, Object>>) parsedJson.get("root");

            for (Map<String, Object> obj : list) {
                TypeActivite typeActivite = new TypeActivite(
                        (int) Float.parseFloat(obj.get("id").toString()),
                        (String) obj.get("nom")
                );

                listTypeActivites.add(typeActivite);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return listTypeActivites;
    }

    public int add(TypeActivite typeActivite) {
        return manage(typeActivite, false);
    }

    public int edit(TypeActivite typeActivite) {
        return manage(typeActivite, true);
    }

    public int manage(TypeActivite typeActivite, boolean isEdit) {

        cr = new ConnectionRequest();

        cr.setHttpMethod("POST");
        if (isEdit) {
            cr.setUrl(Statics.BASE_URL + "/typeActivite/edit");
            cr.addArgument("id", String.valueOf(typeActivite.getId()));
        } else {
            cr.setUrl(Statics.BASE_URL + "/typeActivite/add");
        }
        cr.addArgument("nom", typeActivite.getNom());

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

    public int delete(int typeActiviteId) {
        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/typeActivite/delete");
        cr.setHttpMethod("POST");
        cr.addArgument("id", String.valueOf(typeActiviteId));

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
