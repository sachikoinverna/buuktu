package com.example.buuktu.models;

import java.util.Date;

public class StuffkieModel {
    private String UID;
    private String name;
    private Date creation_date;
    private String WORDLKIE_ID;
    private String AUTHOR_ID;
    private boolean draft;
    private boolean stuffkie_private;
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

    public void setUID(String UID) {
        this.UID = UID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreation_date(Date creation_date) {
        this.creation_date = creation_date;
    }

    public String getWORDLKIE_ID() {
        return WORDLKIE_ID;
    }

    public void setWORDLKIE_ID(String WORDLKIE_ID) {
        this.WORDLKIE_ID = WORDLKIE_ID;
    }

    public String getAUTHOR_ID() {
        return AUTHOR_ID;
    }

    public void setAUTHOR_ID(String AUTHOR_ID) {
        this.AUTHOR_ID = AUTHOR_ID;
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
