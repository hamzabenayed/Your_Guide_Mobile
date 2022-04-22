package com.khedmetna.services;

import com.codename1.components.InfiniteProgress;
import com.codename1.io.*;
import com.codename1.ui.events.ActionListener;
import com.khedmetna.entities.Category;
import com.khedmetna.entities.Produit;
import com.khedmetna.utils.Statics;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProduitService {

    public static ProduitService instance = null;
    public int resultCode;
    private ConnectionRequest cr;
    private ArrayList<Produit> listProduits;

    private ProduitService() {
        cr = new ConnectionRequest();
    }

    public static ProduitService getInstance() {
        if (instance == null) {
            instance = new ProduitService();
        }
        return instance;
    }

    public ArrayList<Produit> getByCat(int idCategorie) {
        listProduits = new ArrayList<>();

        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/produit/cat=" + idCategorie);
        cr.setHttpMethod("GET");

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                if (cr.getResponseCode() == 200) {
                    listProduits = getList();
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

        return listProduits;
    }

    public ArrayList<Produit> getAll() {
        listProduits = new ArrayList<>();

        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/produit");
        cr.setHttpMethod("GET");

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                if (cr.getResponseCode() == 200) {
                    listProduits = getList();
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

        return listProduits;
    }

    private ArrayList<Produit> getList() {
        try {
            Map<String, Object> parsedJson = new JSONParser().parseJSON(new CharArrayReader(
                    new String(cr.getResponseData()).toCharArray()
            ));
            List<Map<String, Object>> list = (List<Map<String, Object>>) parsedJson.get("root");

            for (Map<String, Object> obj : list) {
                Produit produit = new Produit(
                        (int) Float.parseFloat(obj.get("id").toString()),
                        (String) obj.get("nom"),
                        (String) obj.get("description"),
                        (int) Float.parseFloat(obj.get("prix").toString()),
                        (String) obj.get("image"),
                        makeCategory((Map<String, Object>) obj.get("category"))
                );

                listProduits.add(produit);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return listProduits;
    }

    public Category makeCategory(Map<String, Object> obj) {
        if (obj == null) {
            return null;
        }
        return new Category(
                (int) Float.parseFloat(obj.get("id").toString()),
                (String) obj.get("name")
        );
    }

    public int add(Produit produit) {
        return manage(produit, false, true);
    }

    public int edit(Produit produit, boolean imageEdited) {
        return manage(produit, true, imageEdited);
    }

    public int manage(Produit produit, boolean isEdit, boolean imageEdited) {

        MultipartRequest cr = new MultipartRequest();
        cr.setFilename("file", "Produit.jpg");

        cr.setHttpMethod("POST");
        if (isEdit) {
            cr.setUrl(Statics.BASE_URL + "/produit/edit");
            cr.addArgumentNoEncoding("id", String.valueOf(produit.getId()));
        } else {
            cr.setUrl(Statics.BASE_URL + "/produit/add");
        }

        if (imageEdited) {
            try {
                cr.addData("file", produit.getImage(), "image/jpeg");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            cr.addArgumentNoEncoding("image", produit.getImage());
        }

        cr.addArgumentNoEncoding("nom", produit.getNom());
        cr.addArgumentNoEncoding("description", produit.getDescription());
        cr.addArgumentNoEncoding("prix", String.valueOf(produit.getPrix()));
        cr.addArgumentNoEncoding("category", String.valueOf(produit.getCategory().getId()));

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

    public int delete(int produitId) {
        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/produit/delete");
        cr.setHttpMethod("POST");
        cr.addArgument("id", String.valueOf(produitId));

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
