package com.khedmetna.services;

import com.codename1.components.InfiniteProgress;
import com.codename1.io.*;
import com.codename1.ui.events.ActionListener;
import com.khedmetna.entities.Codepromo;
import com.khedmetna.entities.Promo;
import com.khedmetna.utils.Statics;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PromoService {

    public static PromoService instance = null;
    public int resultCode;
    private ConnectionRequest cr;
    private ArrayList<Promo> listPromos;

    private PromoService() {
        cr = new ConnectionRequest();
    }

    public static PromoService getInstance() {
        if (instance == null) {
            instance = new PromoService();
        }
        return instance;
    }

    public ArrayList<Promo> getAll() {
        listPromos = new ArrayList<>();

        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/promo");
        cr.setHttpMethod("GET");

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                if (cr.getResponseCode() == 200) {
                    listPromos = getList();
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

        return listPromos;
    }

    private ArrayList<Promo> getList() {
        try {
            Map<String, Object> parsedJson = new JSONParser().parseJSON(new CharArrayReader(
                    new String(cr.getResponseData()).toCharArray()
            ));
            List<Map<String, Object>> list = (List<Map<String, Object>>) parsedJson.get("root");

            for (Map<String, Object> obj : list) {
                Promo promo = new Promo(
                        (int) Float.parseFloat(obj.get("id").toString()),
                        Float.parseFloat(obj.get("pourcentage").toString()),
                        makeCodepromo((Map<String, Object>) obj.get("codepromo")),
                        (String) obj.get("image")
                );

                listPromos.add(promo);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return listPromos;
    }

    public Codepromo makeCodepromo(Map<String, Object> obj) {
        if (obj == null) {
            return null;
        }

        try {
            return new Codepromo(
                    (int) Float.parseFloat(obj.get("id").toString()),
                    (String) obj.get("code"),
                    new SimpleDateFormat("dd-MM-yyyy").parse((String) obj.get("dateDebut")),
                    new SimpleDateFormat("dd-MM-yyyy").parse((String) obj.get("dateFin"))
            );
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int add(Promo promo) {
        return manage(promo, false, true);
    }

    public int edit(Promo promo, boolean imageEdited) {
        return manage(promo, true, imageEdited);
    }

    public int manage(Promo promo, boolean isEdit, boolean imageEdited) {

        MultipartRequest cr = new MultipartRequest();
        cr.setFilename("file", "Promo.jpg");

        cr.setHttpMethod("POST");
        if (isEdit) {
            cr.setUrl(Statics.BASE_URL + "/promo/edit");
            cr.addArgumentNoEncoding("id", String.valueOf(promo.getId()));
        } else {
            cr.setUrl(Statics.BASE_URL + "/promo/add");
        }

        if (imageEdited) {
            try {
                cr.addData("file", promo.getImage(), "image/jpeg");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            cr.addArgumentNoEncoding("image", promo.getImage());
        }

        cr.addArgumentNoEncoding("pourcentage", String.valueOf(promo.getPourcentage()));
        cr.addArgumentNoEncoding("codepromo", String.valueOf(promo.getCodepromo().getId()));

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

    public int delete(int promoId) {
        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/promo/delete");
        cr.setHttpMethod("POST");
        cr.addArgument("id", String.valueOf(promoId));

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
