package com.example.buuktu.models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;

public class NotekieModel {
    String title;
    String content;
    Timestamp last_update;
    String UID_USER;
    String UID;
    public static NotekieModel fromSnapshot(DocumentSnapshot document) {
        if (document == null || !document.exists()) {
            return null;
        }
        NotekieModel notekieModel = new NotekieModel();
        notekieModel.setTitle(document.getString("title"));
        notekieModel.setContent(document.getString("content"));
        notekieModel.setUID(document.getString("UID"));
        notekieModel.setUID_USER(document.getString("UID_USER"));
        notekieModel.setLast_update(document.getTimestamp("last_update"));
        return notekieModel;
    }
    public NotekieModel() {
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public void setUID_USER(String UID_USER) {
        this.UID_USER = UID_USER;
    }

    public void setLast_update(Timestamp last_update) {
        this.last_update = last_update;
    }

    public String getUID_USER() {
        return UID_USER;
    }

    public Timestamp getLast_update() {
        return last_update;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }
}
