package com.example.buuktu.models;

import java.util.Date;

public class StuffkieModel {
    private String UID;
    private String name;
    private Date creation_date;
    private String BOOKIE_ID;
    private boolean borrador;
    private boolean stuffkie_private;
    private int drawable;
    private boolean photo_default;

    public StuffkieModel(String UID, String name, boolean stuffkie_private,boolean photo_default){
        this.UID=UID;
        this.name=name;
        this.stuffkie_private=stuffkie_private;
        this.stuffkie_private = photo_default;
    }

    public String getUID() {
        return UID;
    }

    public String getName() {
        return name;
    }

    public Date getCreation_date() {
        return creation_date;
    }

    public String getBOOKIE_ID() {
        return BOOKIE_ID;
    }

    public boolean isBorrador() {
        return borrador;
    }

    public boolean isStuffkie_private() {
        return stuffkie_private;
    }

    public int getDrawable() {
        return drawable;
    }

    public boolean isPhoto_default() {
        return photo_default;
    }
}
