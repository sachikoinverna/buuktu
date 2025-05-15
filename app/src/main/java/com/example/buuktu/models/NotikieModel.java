package com.example.buuktu.models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;

public class NotikieModel {
    String message;
    Timestamp date;
    int icon;
    String UID_USER;
    public int getIcon() {
        return icon;
    }

    public Timestamp getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public NotikieModel(String message, Timestamp date, int icon, String UID_USER) {
        this.message = message;
        this.date = date;
        this.icon = icon;
        this.UID_USER = UID_USER;
    }

    public NotikieModel() {
    }

    public static NotikieModel fromSnapshot(DocumentSnapshot document) {
        if (document == null || !document.exists()) {
            return null;
        }
        NotikieModel notikieModel = new NotikieModel();
        notikieModel.setMessage(document.getString("message"));
        notikieModel.setDate(document.getTimestamp("date"));
        notikieModel.setIcon(Math.toIntExact(document.getLong("icon")));
        return notikieModel;
    }
}
