package com.khedmetna.services;

import com.codename1.components.InfiniteProgress;
import com.codename1.io.*;
import com.codename1.ui.events.ActionListener;

import com.khedmetna.entities.Panier;

import com.khedmetna.entities.Livraison;
import com.khedmetna.utils.Statics;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LivraisonService {

    public static LivraisonService instance = null;
    public int resultCode;
    private ConnectionRequest cr;
    private ArrayList<Livraison> listLivraisons;

    private LivraisonService() {
        cr = new ConnectionRequest();
    }

    public static LivraisonService getInstance() {
        if (instance == null) {
            instance = new LivraisonService();
        }
        return instance;
    }

    public ArrayList<Livraison> getAll() {
        listLivraisons = new ArrayList<>();

        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/livraison");
        cr.setHttpMethod("GET");

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                if (cr.getResponseCode() == 200) {
                    listLivraisons = getList();
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

        return listLivraisons;
    }

    private ArrayList<Livraison> getList() {
        try {
            Map<String, Object> parsedJson = new JSONParser().parseJSON(new CharArrayReader(
                    new String(cr.getResponseData()).toCharArray()
            ));
            List<Map<String, Object>> list = (List<Map<String, Object>>) parsedJson.get("root");

            for (Map<String, Object> obj : list) {
                Livraison livraison = new Livraison(
                        (int) Float.parseFloat(obj.get("id").toString()),
                        new SimpleDateFormat("dd-MM-yyyy").parse((String) obj.get("date")),
                        (String) obj.get("nomLivreur"),
                        (String) obj.get("prenomLivreur"),
                        (String) obj.get("telLivreur"),
                        (String) obj.get("adresseLivraison"),
                        makePanier((Map<String, Object>) obj.get("panier"))
                );

                listLivraisons.add(livraison);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return listLivraisons;
    }

    public Panier makePanier(Map<String, Object> obj) {
        if (obj == null) {
            return null;
        }
        return new Panier(
                (int) Float.parseFloat(obj.get("id").toString()),
                (String) obj.get("description")
        );
    }

    public int add(Livraison livraison) {
        return manage(livraison, false);
    }

    public int edit(Livraison livraison) {
        return manage(livraison, true);
    }

    public int manage(Livraison livraison, boolean isEdit) {

        cr = new ConnectionRequest();

        cr.setHttpMethod("POST");
        if (isEdit) {
            cr.setUrl(Statics.BASE_URL + "/livraison/edit");
            cr.addArgument("id", String.valueOf(livraison.getId()));
        } else {
            cr.setUrl(Statics.BASE_URL + "/livraison/add");
        }
        cr.addArgument("date", new SimpleDateFormat("dd-MM-yyyy").format(livraison.getDate()));
        cr.addArgument("nomLivreur", livraison.getNomLivreur());
        cr.addArgument("prenomLivreur", livraison.getPrenomLivreur());
        cr.addArgument("telLivreur", livraison.getTelLivreur());
        cr.addArgument("adresseLivraison", livraison.getAdresseLivraison());
        cr.addArgument("panier", String.valueOf(livraison.getPanier().getId()));

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

    public int delete(int livraisonId) {
        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/livraison/delete");
        cr.setHttpMethod("POST");
        cr.addArgument("id", String.valueOf(livraisonId));

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
