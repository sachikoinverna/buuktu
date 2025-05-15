package com.example.buuktu.models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/** @noinspection CallToPrintStackTrace*/
public class NotekieModel {
    String title;
    String text;
    Timestamp last_update;
    String UID_USER;
    String UID;
    public static NotekieModel fromSnapshot(DocumentSnapshot document) {
        if (document == null || !document.exists()) {
            return null;
        }
        NotekieModel notekieModel = new NotekieModel();
        notekieModel.setTitle(document.getString("title"));
        notekieModel.setText(document.getString("text"));
        notekieModel.setUID(document.getId());
        notekieModel.setUID_USER(document.getString("UID_USER"));
        notekieModel.setLast_update(document.getTimestamp("last_update"));
        return notekieModel;
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
    public NotekieModel() {
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }

    public void setUID_USER(String UID_USER) {
        this.UID_USER = UID_USER;
    }

    public void setLast_update(Timestamp last_update) {
        this.last_update = last_update;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }
}
