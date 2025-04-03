package com.example.buuktu.models;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.Date;

public class WorldkieModel implements Serializable {
    private String UID;
    private String UID_AUTHOR;
    private String name;
    private Date creation_date;
    private Date last_update;
    private Drawable photo;
    private boolean photo_default;
    private boolean worldkie_private;
    private int drawable;
    private boolean borrador;

    public WorldkieModel() {
    }

    public WorldkieModel(String UID, String UID_AUTHOR, String name, Date creation_date, Date last_update) {
        this.UID = UID;
        this.UID_AUTHOR = UID_AUTHOR;
        this.name = name;
        this.creation_date = creation_date;
        this.last_update = last_update;
    }
    public WorldkieModel(String UID, String UID_AUTHOR,int drawable, String name, Date creation_date,boolean photo_default,Date last_update, boolean worldkie_private) {
        this.UID = UID;
        this.UID_AUTHOR = UID_AUTHOR;
        this.drawable = drawable;
        this.name = name;
        this.creation_date = creation_date;
        this.last_update = last_update;
        this.worldkie_private = worldkie_private;
        this.photo_default = photo_default;
    }
    public WorldkieModel(String UID_AUTHOR, String name) {
        this.UID_AUTHOR = UID_AUTHOR;
        this.name = name;
        this.creation_date = new Date();
        this.last_update = creation_date;
    }
    public WorldkieModel(String UID,String UID_AUTHOR, String name, Drawable photo, boolean photo_default, boolean worldkie_private) {
        this.UID = UID;
        this.UID_AUTHOR = UID_AUTHOR;
        this.name = name;
        this.photo = photo;
        this.photo_default = photo_default;
        this.worldkie_private = worldkie_private;
    }
    public WorldkieModel(String UID, String name, boolean photo_default, boolean worldkie_private) {
        this.UID = UID;
        this.name = name;
        this.photo_default=photo_default;
        this.worldkie_private=worldkie_private;
    }

    public boolean isWorldkie_private() {
        return worldkie_private;
    }

    public void setWorldkie_private(boolean worldkie_private) {
        this.worldkie_private = worldkie_private;
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
    public boolean isPhoto_default() {
        return photo_default;
    }
    public void setPhoto_default(boolean photo_default) {
        this.photo_default = photo_default;
    }
}
