package com.khedmetna.services;

import com.codename1.components.InfiniteProgress;
import com.codename1.io.*;
import com.codename1.ui.events.ActionListener;
import com.khedmetna.entities.Reclamation;
import com.khedmetna.utils.Statics;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReclamationService {

    public static ReclamationService instance = null;
    public int resultCode;
    private ConnectionRequest cr;
    private ArrayList<Reclamation> listReclamations;

    private ReclamationService() {
        cr = new ConnectionRequest();
    }

    public static ReclamationService getInstance() {
        if (instance == null) {
            instance = new ReclamationService();
        }
        return instance;
    }

    public ArrayList<Reclamation> getAll() {
        listReclamations = new ArrayList<>();

        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/reclamation");
        cr.setHttpMethod("GET");

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                if (cr.getResponseCode() == 200) {
                    listReclamations = getList();
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

        return listReclamations;
    }

    private ArrayList<Reclamation> getList() {
        try {
            Map<String, Object> parsedJson = new JSONParser().parseJSON(new CharArrayReader(
                    new String(cr.getResponseData()).toCharArray()
            ));
            List<Map<String, Object>> list = (List<Map<String, Object>>) parsedJson.get("root");

            for (Map<String, Object> obj : list) {
                Reclamation reclamation = new Reclamation(
                        (int) Float.parseFloat(obj.get("id").toString()),
                        new SimpleDateFormat("dd-MM-yyyy").parse((String) obj.get("date")),
                        (String) obj.get("type"),
                        (String) obj.get("etat"),
                        (String) obj.get("description")
                );

                listReclamations.add(reclamation);
            }
        } catch (IOException | ParseException ex) {
            ex.printStackTrace();
        }
        return listReclamations;
    }

    public int add(Reclamation reclamation) {
        return manage(reclamation, false);
    }

    public int edit(Reclamation reclamation) {
        return manage(reclamation, true);
    }

    public int manage(Reclamation reclamation, boolean isEdit) {

        cr = new ConnectionRequest();

        cr.setHttpMethod("POST");
        if (isEdit) {
            cr.setUrl(Statics.BASE_URL + "/reclamation/edit");
            cr.addArgument("id", String.valueOf(reclamation.getId()));
        } else {
            cr.setUrl(Statics.BASE_URL + "/reclamation/add");
        }
        cr.addArgument("date", new SimpleDateFormat("dd-MM-yyyy").format(reclamation.getDate()));
        cr.addArgument("type", reclamation.getType());
        cr.addArgument("etat", reclamation.getEtat());
        cr.addArgument("description", reclamation.getDescription());

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

    public int delete(int reclamationId) {
        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/reclamation/delete");
        cr.setHttpMethod("POST");
        cr.addArgument("id", String.valueOf(reclamationId));

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
