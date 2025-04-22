package com.example.buuktu.models;

import android.graphics.drawable.Drawable;

import java.util.Date;

public class Characterkie {
    private String UID;
    private String name;
    private String surname;
    private String pronouns;
    private String birthday;
    private String horoscope;
    private Date deathday;
    private boolean alive;
    private boolean photo_default;
    private Drawable photo;
    private boolean draft;
    boolean characterkiePrivate;

    public boolean isCharacterkiePrivate() {
        return characterkiePrivate;
    }

    public void setCharacterkiePrivate(boolean characterkiePrivate) {
        this.characterkiePrivate = characterkiePrivate;
    }

    public Characterkie(String UID, String name) {
        this.UID=UID;
        this.name=name;
    }

    public boolean isDraft() {
        return draft;
    }

    public void setDraft(boolean draft) {
        this.draft = draft;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public Date getDeathday() {
        return deathday;
    }

    public void setDeathday(Date deathday) {
        this.deathday = deathday;
    }

    public String getHoroscope() {
        return horoscope;
    }

    public void setHoroscope(String horoscope) {
        this.horoscope = horoscope;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPronouns() {
        return pronouns;
    }

    public void setPronouns(String pronouns) {
        this.pronouns = pronouns;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }
    public boolean isPhoto_default() {
        return photo_default;
    }
    public void setPhoto_default(boolean photo_default) {
        this.photo_default = photo_default;
    }
    public Drawable getPhoto() {
        return photo;
    }
    public void setPhoto(Drawable photo) {
        this.photo = photo;
    }
}
