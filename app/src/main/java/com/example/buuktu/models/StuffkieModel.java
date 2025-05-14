package com.example.buuktu.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.firestore.DocumentSnapshot;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class StuffkieModel {
    private String UID;
    private String name;
    private String WORDLKIE_UID;
    private String AUTHOR_UID;
    private boolean draft;
    private boolean stuffkie_private;
    private boolean photo_default;
    private String photo_id;
    public StuffkieModel() {
    }

    public StuffkieModel(String UID, String name, boolean stuffkie_private, boolean photo_default,String photo_id, boolean draft){
        this.UID=UID;
        this.name=name;
        this.stuffkie_private = stuffkie_private;
        this.photo_default = photo_default;
        this.photo_id=photo_id;
        this.draft=draft;
    }
    public static StuffkieModel fromSnapshot(DocumentSnapshot document){
        if (document == null || !document.exists()) {
            return null;
        }
        StuffkieModel stuffkieModel = new StuffkieModel();
        stuffkieModel.setName(document.getString("name"));
        stuffkieModel.setAUTHOR_UID(document.getString("AUTHOR_UID"));
        stuffkieModel.setUID(document.getId());
        stuffkieModel.setWORDLKIE_UID(document.getString("WORDLKIE_UID"));
        stuffkieModel.setStuffkie_private(document.getBoolean("stuffkie_private"));
        stuffkieModel.setDraft(document.getBoolean("draft"));
        stuffkieModel.setPhoto_default(document.getBoolean("photo_default"));
        if (document.contains("photo_id")) {
            stuffkieModel.setPhoto_id(document.getString("photo_id"));

        }
        return stuffkieModel;
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
    @Exclude

    public String getUID() {
        return UID;
    }

    public String getName() {
        return name;
    }

    @Exclude
    public void setUID(String UID) {
        this.UID = UID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto_id() {
        return photo_id;
    }

    public void setPhoto_id(String photo_id) {
        this.photo_id = photo_id;
    }

    public String getWORDLKIE_UID() {
        return WORDLKIE_UID;
    }

    public void setWORDLKIE_UID(String WORDLKIE_UID) {
        this.WORDLKIE_UID = WORDLKIE_UID;
    }

    public String getAUTHOR_UID() {
        return AUTHOR_UID;
    }

    public void setAUTHOR_UID(String AUTHOR_UID) {
        this.AUTHOR_UID = AUTHOR_UID;
    }

    public void setDraft(boolean draft) {
        this.draft = draft;
    }

    public void setStuffkie_private(boolean stuffkie_private) {
        this.stuffkie_private = stuffkie_private;
    }

    public void setPhoto_default(boolean photo_default) {
        this.photo_default = photo_default;
    }

    public boolean isDraft() {
        return draft;
    }

    public boolean isStuffkie_private() {
        return stuffkie_private;
    }


    public boolean isPhoto_default() {
        return photo_default;
    }
}
