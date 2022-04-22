package com.khedmetna.services;

import com.codename1.components.InfiniteProgress;
import com.codename1.io.*;
import com.codename1.ui.events.ActionListener;
import com.khedmetna.entities.Commentaire;
import com.khedmetna.entities.Like;
import com.khedmetna.utils.Statics;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LikeService {

    public static LikeService instance = null;
    public int resultCode;
    private ConnectionRequest cr;
    private ArrayList<Like> listLikes;

    private LikeService() {
        cr = new ConnectionRequest();
    }

    public static LikeService getInstance() {
        if (instance == null) {
            instance = new LikeService();
        }
        return instance;
    }

    public int likeDislike(Like like) {
        listLikes = new ArrayList<>();

        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/like/likeDislike");
        cr.setHttpMethod("POST");

        cr.addArgument("nom", like.getNom());
        cr.addArgument("rate", String.valueOf(like.getRate()));
        cr.addArgument("note", String.valueOf(like.getNote()));
        cr.addArgument("commentaire", String.valueOf(like.getCommentaire().getId()));

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

    public ArrayList<Like> getAll() {
        listLikes = new ArrayList<>();

        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/like");
        cr.setHttpMethod("GET");

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                if (cr.getResponseCode() == 200) {
                    listLikes = getList();
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

        return listLikes;
    }

    private ArrayList<Like> getList() {
        try {
            Map<String, Object> parsedJson = new JSONParser().parseJSON(new CharArrayReader(
                    new String(cr.getResponseData()).toCharArray()
            ));
            List<Map<String, Object>> list = (List<Map<String, Object>>) parsedJson.get("root");

            for (Map<String, Object> obj : list) {
                Like like = new Like(
                        (int) Float.parseFloat(obj.get("id").toString()),
                        (String) obj.get("nom"),
                        (int) Float.parseFloat(obj.get("rate").toString()),
                        (int) Float.parseFloat(obj.get("note").toString()),
                        makeCommentaire((Map<String, Object>) obj.get("commentaire"))
                );

                listLikes.add(like);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return listLikes;
    }

    public Commentaire makeCommentaire(Map<String, Object> obj) {
        if (obj == null) {
            return null;
        }

        try {
            return new Commentaire(
                    (int) Float.parseFloat(obj.get("id").toString()),
                    (String) obj.get("description"),
                    new SimpleDateFormat("dd-MM-yyyy").parse((String) obj.get("date")),
                    null
            );
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int add(Like like) {
        return manage(like, false);
    }

    public int edit(Like like) {
        return manage(like, true);
    }

    public int manage(Like like, boolean isEdit) {

        cr = new ConnectionRequest();

        cr.setHttpMethod("POST");
        if (isEdit) {
            cr.setUrl(Statics.BASE_URL + "/like/edit");
            cr.addArgument("id", String.valueOf(like.getId()));
        } else {
            cr.setUrl(Statics.BASE_URL + "/like/add");
        }
        cr.addArgument("nom", like.getNom());
        cr.addArgument("rate", String.valueOf(like.getRate()));
        cr.addArgument("note", String.valueOf(like.getNote()));
        cr.addArgument("commentaire", String.valueOf(like.getCommentaire().getId()));

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

    public int delete(int likeId) {
        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/like/delete");
        cr.setHttpMethod("POST");
        cr.addArgument("id", String.valueOf(likeId));

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
