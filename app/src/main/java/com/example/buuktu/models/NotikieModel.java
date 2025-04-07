package com.example.buuktu.models;

import com.google.firebase.Timestamp;

public class NotikieModel {
    String message;
    Timestamp date;
    int icon;

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

    public NotikieModel(String message, Timestamp date, int icon) {
        this.message = message;
        this.date = date;
        this.icon = icon;
    }
}
