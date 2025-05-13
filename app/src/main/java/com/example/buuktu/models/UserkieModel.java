package com.example.buuktu.models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class UserkieModel {
    private String name;
    private String pronouns;
    private Timestamp birthday;
    private String username;
    private String number;
    private String UID;
    private String email;
    private boolean photo_default;
    private boolean profile_private;
    private String photo_id;

    public String getPhoto_id() {
        return photo_id;
    }

    public void setPhoto_id(String photo_id) {
        this.photo_id = photo_id;
    }

    public String getEmail() {
        return email;
    }

    public Timestamp getBirthday() {
        return birthday;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getUsername() {
        return username;
    }

    public void setBirthday(Timestamp birthday) {
        this.birthday = birthday;
    }

    public String getPronouns() {
        return pronouns;
    }

    public void setPronouns(String pronouns) {
        this.pronouns = pronouns;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPhoto_default() {
        return photo_default;
    }

    public void setPhoto_default(boolean photo_default) {
        this.photo_default = photo_default;
    }

    public boolean isProfile_private() {
        return profile_private;
    }

    public void setProfile_private(boolean profile_private) {
        this.profile_private = profile_private;
    }

    public UserkieModel() {
    }
    public static UserkieModel fromSnapshot(DocumentSnapshot document) {
        if (document == null || !document.exists()) {
            return null;
        }
        UserkieModel userkieModel = new UserkieModel();
        userkieModel.setName(document.getString("name"));
        userkieModel.setUsername(document.getString("username"));
        userkieModel.setUID(document.getId());
        userkieModel.setBirthday(document.getTimestamp("birthday"));
        userkieModel.setEmail(document.getString("email"));
        userkieModel.setPhoto_default(document.getBoolean("photo_default"));
        if(document.contains("photo_id")){
            userkieModel.setPhoto_id(document.getString("photo_id"));
        }
        userkieModel.setProfile_private(document.getBoolean("profile_private"));
        userkieModel.setNumber(document.getString("number"));
        userkieModel.setPronouns(document.getString("pronouns"));
        return userkieModel;
    }
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        Field[] fields = this.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(this);
                if (value != null) {
                    map.put(field.getName(), value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return map;
    }

}
