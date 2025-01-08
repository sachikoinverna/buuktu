package com.example.buuktu.models;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import java.util.Date;

public class WorldkieModel {
    private String UID;
    private String UID_AUTHOR;
    private String name;
    private Date creation_date;
    private Date last_update;
    private Drawable photo;

    public WorldkieModel(String UID, String UID_AUTHOR, String name, Date creation_date, Date last_update, Drawable photo) {
        this.UID = UID;
        this.UID_AUTHOR = UID_AUTHOR;
        this.name = name;
        this.creation_date = creation_date;
        this.last_update = last_update;
        this.photo = photo;
    }
    public WorldkieModel(String UID, String name, Drawable photo) {
        this.UID = UID;
        this.name = name;
        this.photo = photo;
    }
    public Drawable getPhoto() {
        return photo;
    }

    public void setPhoto(Drawable photo) {
        this.photo = photo;
    }

    public String getUID() {
        return UID;
    }

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

    public Date getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(Date creation_date) {
        this.creation_date = creation_date;
    }

    public Date getLast_update() {
        return last_update;
    }

    public void setLast_update(Date last_update) {
        this.last_update = last_update;
    }
}
