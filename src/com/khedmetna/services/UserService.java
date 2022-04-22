package com.khedmetna.services;

import com.codename1.components.InfiniteProgress;
import com.codename1.io.*;
import com.codename1.ui.events.ActionListener;
import com.khedmetna.entities.User;
import com.khedmetna.utils.Statics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserService {

    public static UserService instance = null;
    public int resultCode;
    User user;
    private ConnectionRequest cr;
    private ArrayList<User> listUsers;

    private UserService() {
        cr = new ConnectionRequest();
    }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    public User verifierMotDePasse(String login, String password) {
        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/user/verif");
        cr.setHttpMethod("POST");
        cr.addArgument("login", login);
        cr.addArgument("mdp", password);

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                if (cr.getResponseCode() == 200) {
                    user = getOne();
                } else {
                    user = null;
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

        return user;
    }

    public ArrayList<User> getAll() {
        listUsers = new ArrayList<>();

        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/user");
        cr.setHttpMethod("GET");

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                if (cr.getResponseCode() == 200) {
                    listUsers = getList();
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

        return listUsers;
    }

    public User getById(int userId) {
        listUsers = new ArrayList<>();

        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/user/show");
        cr.setHttpMethod("POST");
        cr.addArgument("id", String.valueOf(userId));

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                if (cr.getResponseCode() == 200) {
                    user = getOne();
                } else {
                    user = null;
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

        return user;
    }

    private User getOne() {
        try {
            Map<String, Object> obj = new JSONParser().parseJSON(new CharArrayReader(
                    new String(cr.getResponseData()).toCharArray()
            ));

            return new User(
                    (int) Float.parseFloat(obj.get("id").toString()),
                    (String) obj.get("login"),
                    (String) obj.get("roles"),
                    (String) obj.get("mdp"),
                    (String) obj.get("nom"),
                    (String) obj.get("image")
            );

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private ArrayList<User> getList() {
        try {
            Map<String, Object> parsedJson = new JSONParser().parseJSON(new CharArrayReader(
                    new String(cr.getResponseData()).toCharArray()
            ));
            List<Map<String, Object>> list = (List<Map<String, Object>>) parsedJson.get("root");

            for (Map<String, Object> obj : list) {
                User user = new User(
                        (int) Float.parseFloat(obj.get("id").toString()),
                        (String) obj.get("login"),
                        (String) obj.get("roles"),
                        (String) obj.get("mdp"),
                        (String) obj.get("nom"),
                        (String) obj.get("image")
                );

                listUsers.add(user);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return listUsers;
    }

    public int add(User user) {
        return manage(user, false, true);
    }

    public int edit(User user, boolean imageEdited) {
        return manage(user, true, imageEdited);
    }

    public int manage(User user, boolean isEdit, boolean imageEdited) {

        MultipartRequest cr = new MultipartRequest();
        cr.setFilename("file", "User.jpg");
        cr.setHttpMethod("POST");

        if (isEdit) {
            cr.setUrl(Statics.BASE_URL + "/user/edit");
            cr.addArgumentNoEncoding("id", String.valueOf(user.getId()));
        } else {
            cr.setUrl(Statics.BASE_URL + "/user/add");
        }

        if (imageEdited) {
            try {
                cr.addData("file", user.getImage(), "image/jpeg");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            cr.addArgumentNoEncoding("image", user.getImage());
        }

        cr.addArgumentNoEncoding("login", user.getLogin());
        cr.addArgumentNoEncoding("roles", user.getRoles());
        cr.addArgumentNoEncoding("mdp", user.getMdp());
        cr.addArgumentNoEncoding("nom", user.getNom());

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

    public int delete(int userId) {
        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/user/delete");
        cr.setHttpMethod("POST");
        cr.addArgument("id", String.valueOf(userId));

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
