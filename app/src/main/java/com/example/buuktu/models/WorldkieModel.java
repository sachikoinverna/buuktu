package com.example.buuktu.models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Exclude;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class WorldkieModel implements Serializable {
    private String UID;
    private String UID_AUTHOR;
    private String name;
    private Timestamp creation_date;
    private Timestamp last_update;
    String id_photo;
    private boolean photo_default;
    private boolean worldkie_private;
    private boolean draft;

    public WorldkieModel() {
    }

    public static WorldkieModel fromSnapshot(DocumentSnapshot document) {
        if (document == null || !document.exists()) {
            return null;
        }
        WorldkieModel worldkieModel = new WorldkieModel();
        worldkieModel.setName(document.getString("name"));
        worldkieModel.setUID(document.getId());
        worldkieModel.setUID_AUTHOR(document.getString("UID_AUTHOR"));
        worldkieModel.setLast_update(document.getTimestamp("last_update"));
        worldkieModel.setCreation_date(document.getTimestamp("creation_date"));
        worldkieModel.setPhoto_default(document.getBoolean("photo_default"));
        worldkieModel.setWorldkie_private(document.getBoolean("worldkie_private"));

        if (document.contains("id_photo")) {
            worldkieModel.setId_photo(document.getString("id_photo"));

        }
            worldkieModel.setDraft(document.getBoolean("draft"));
        return worldkieModel;
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

    public boolean isWorldkie_private() {
        return worldkie_private;
    }

    public void setWorldkie_private(boolean worldkie_private) {
        this.worldkie_private = worldkie_private;
    }
    @Exclude

    public String getUID() {
        return UID;
    }
    @Exclude
    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getUID_AUTHOR() {
        return UID_AUTHOR;
    }

    public void setUID_AUTHOR(String UID_AUTHOR) {
        this.UID_AUTHOR = UID_AUTHOR;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(Timestamp creation_date) {
        this.creation_date = creation_date;
    }

    public Timestamp getLast_update() {
        return last_update;
    }

    public void setLast_update(Timestamp last_update) {
        this.last_update = last_update;
    }
    public boolean isPhoto_default() {
        return photo_default;
    }
    public void setPhoto_default(boolean photo_default) {
        this.photo_default = photo_default;
    }

    public String getId_photo() {
        return id_photo;
    }

    public void setId_photo(String id_photo) {
        this.id_photo = id_photo;
    }

    public boolean isDraft() {
        return draft;
    }

    public void setDraft(boolean draft) {
        this.draft = draft;
    }
}
