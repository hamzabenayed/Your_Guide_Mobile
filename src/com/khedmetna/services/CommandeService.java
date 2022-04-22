package com.khedmetna.services;

import com.codename1.components.InfiniteProgress;
import com.codename1.io.*;
import com.codename1.ui.events.ActionListener;
import com.khedmetna.entities.Commande;
import com.khedmetna.utils.Statics;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommandeService {

    public static CommandeService instance = null;
    public int resultCode;
    private ConnectionRequest cr;
    private ArrayList<Commande> listCommandes;

    private CommandeService() {
        cr = new ConnectionRequest();
    }

    public static CommandeService getInstance() {
        if (instance == null) {
            instance = new CommandeService();
        }
        return instance;
    }

    public ArrayList<Commande> getAll() {
        listCommandes = new ArrayList<>();

        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/commande");
        cr.setHttpMethod("GET");

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                if (cr.getResponseCode() == 200) {
                    listCommandes = getList();
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

        return listCommandes;
    }

    private ArrayList<Commande> getList() {
        try {
            Map<String, Object> parsedJson = new JSONParser().parseJSON(new CharArrayReader(
                    new String(cr.getResponseData()).toCharArray()
            ));
            List<Map<String, Object>> list = (List<Map<String, Object>>) parsedJson.get("root");

            for (Map<String, Object> obj : list) {
                Commande commande = new Commande(
                        (int) Float.parseFloat(obj.get("id").toString()),
                        new SimpleDateFormat("dd-MM-yyyy").parse((String) obj.get("date")),
                        (String) obj.get("etat"),
                        (String) obj.get("commentaire"),
                        (String) obj.get("adresse"),
                        (String) obj.get("totalcost"),
                        (String) obj.get("product")
                );

                listCommandes.add(commande);
            }
        } catch (IOException | ParseException ex) {
            ex.printStackTrace();
        }
        return listCommandes;
    }

    public int add(Commande commande) {
        return manage(commande, false);
    }

    public int edit(Commande commande) {
        return manage(commande, true);
    }

    public int manage(Commande commande, boolean isEdit) {

        cr = new ConnectionRequest();

        cr.setHttpMethod("POST");
        if (isEdit) {
            cr.setUrl(Statics.BASE_URL + "/commande/edit");
            cr.addArgument("id", String.valueOf(commande.getId()));
        } else {
            cr.setUrl(Statics.BASE_URL + "/commande/add");
        }
        cr.addArgument("date", new SimpleDateFormat("dd-MM-yyyy").format(commande.getDate()));
        cr.addArgument("etat", commande.getEtat());
        cr.addArgument("commentaire", commande.getCommentaire());
        cr.addArgument("adresse", commande.getAdresse());
        cr.addArgument("totalcost", commande.getTotalcost());
        cr.addArgument("product", commande.getProduct());

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

    public int delete(int commandeId) {
        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/commande/delete");
        cr.setHttpMethod("POST");
        cr.addArgument("id", String.valueOf(commandeId));

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
