package com.khedmetna.services;

import com.codename1.components.InfiniteProgress;
import com.codename1.io.*;
import com.codename1.ui.events.ActionListener;
import com.khedmetna.entities.Codepromo;
import com.khedmetna.utils.Statics;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CodepromoService {

    public static CodepromoService instance = null;
    public int resultCode;
    private ConnectionRequest cr;
    private ArrayList<Codepromo> listCodepromos;

    private CodepromoService() {
        cr = new ConnectionRequest();
    }

    public static CodepromoService getInstance() {
        if (instance == null) {
            instance = new CodepromoService();
        }
        return instance;
    }

    public ArrayList<Codepromo> getAll() {
        listCodepromos = new ArrayList<>();

        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/codepromo");
        cr.setHttpMethod("GET");

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                if (cr.getResponseCode() == 200) {
                    listCodepromos = getList();
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

        return listCodepromos;
    }

    private ArrayList<Codepromo> getList() {
        try {
            Map<String, Object> parsedJson = new JSONParser().parseJSON(new CharArrayReader(
                    new String(cr.getResponseData()).toCharArray()
            ));
            List<Map<String, Object>> list = (List<Map<String, Object>>) parsedJson.get("root");

            for (Map<String, Object> obj : list) {
                Codepromo codepromo = new Codepromo(
                        (int) Float.parseFloat(obj.get("id").toString()),
                        (String) obj.get("code"),
                        new SimpleDateFormat("dd-MM-yyyy").parse((String) obj.get("dateDebut")),
                        new SimpleDateFormat("dd-MM-yyyy").parse((String) obj.get("dateFin"))
                );

                listCodepromos.add(codepromo);
            }
        } catch (IOException | ParseException ex) {
            ex.printStackTrace();
        }
        return listCodepromos;
    }

    public int add(Codepromo codepromo) {
        return manage(codepromo, false);
    }

    public int edit(Codepromo codepromo) {
        return manage(codepromo, true);
    }

    public int manage(Codepromo codepromo, boolean isEdit) {

        cr = new ConnectionRequest();

        cr.setHttpMethod("POST");
        if (isEdit) {
            cr.setUrl(Statics.BASE_URL + "/codepromo/edit");
            cr.addArgument("id", String.valueOf(codepromo.getId()));
        } else {
            cr.setUrl(Statics.BASE_URL + "/codepromo/add");
        }
        cr.addArgument("code", codepromo.getCode());
        cr.addArgument("dateDebut", new SimpleDateFormat("dd-MM-yyyy").format(codepromo.getDateDebut()));
        cr.addArgument("dateFin", new SimpleDateFormat("dd-MM-yyyy").format(codepromo.getDateFin()));

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

    public int delete(int codepromoId) {
        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/codepromo/delete");
        cr.setHttpMethod("POST");
        cr.addArgument("id", String.valueOf(codepromoId));

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
