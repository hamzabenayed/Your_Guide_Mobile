package com.khedmetna.services;

import com.codename1.components.InfiniteProgress;
import com.codename1.io.*;
import com.codename1.ui.events.ActionListener;
import com.khedmetna.entities.Category;
import com.khedmetna.utils.Statics;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CategoryService {

    public static CategoryService instance = null;
    public int resultCode;
    private ConnectionRequest cr;
    private ArrayList<Category> listCategorys;

    private CategoryService() {
        cr = new ConnectionRequest();
    }

    public static CategoryService getInstance() {
        if (instance == null) {
            instance = new CategoryService();
        }
        return instance;
    }

    public ArrayList<Category> getAll() {
        listCategorys = new ArrayList<>();

        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/category");
        cr.setHttpMethod("GET");

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                if (cr.getResponseCode() == 200) {
                    listCategorys = getList();
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

        return listCategorys;
    }

    private ArrayList<Category> getList() {
        try {
            Map<String, Object> parsedJson = new JSONParser().parseJSON(new CharArrayReader(
                    new String(cr.getResponseData()).toCharArray()
            ));
            List<Map<String, Object>> list = (List<Map<String, Object>>) parsedJson.get("root");

            for (Map<String, Object> obj : list) {
                Category category = new Category(
                        (int) Float.parseFloat(obj.get("id").toString()),
                        (String) obj.get("name")
                );

                listCategorys.add(category);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return listCategorys;
    }

    public int add(Category category) {
        return manage(category, false);
    }

    public int edit(Category category) {
        return manage(category, true);
    }

    public int manage(Category category, boolean isEdit) {

        cr = new ConnectionRequest();

        cr.setHttpMethod("POST");
        if (isEdit) {
            cr.setUrl(Statics.BASE_URL + "/category/edit");
            cr.addArgument("id", String.valueOf(category.getId()));
        } else {
            cr.setUrl(Statics.BASE_URL + "/category/add");
        }
        cr.addArgument("name", category.getName());

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

    public int delete(int categoryId) {
        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/category/delete");
        cr.setHttpMethod("POST");
        cr.addArgument("id", String.valueOf(categoryId));

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
