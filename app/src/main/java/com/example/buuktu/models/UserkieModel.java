package com.example.buuktu.models;

import android.graphics.drawable.Drawable;

import java.util.Date;

public class UserkieModel {
    private String name;
    private String pronouns;
    private Date birthday;
    private String username;
    private String number;
    private String UID;
    private String email;
    private boolean photo_default;
    private boolean profile_private;
    private String surname;
    private int drawable;
    public String getEmail() {
        return email;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getPronouns() {
        return pronouns;
    }

    public void setPronouns(String pronouns) {
        this.pronouns = pronouns;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPhoto_default() {
        return photo_default;
    }

    public void setPhoto_default(boolean photo_default) {
        this.photo_default = photo_default;
    }

    public boolean isProfile_private() {
        return profile_private;
    }

    public void setProfile_private(boolean profile_private) {
        this.profile_private = profile_private;
    }

    public UserkieModel() {
    }

    public UserkieModel(String email, String UID, String name, String pronouns, Date birthday, String username, String number, boolean photo_default, boolean profile_private) {
        this.email = email;
        this.UID = UID;
        this.name = name;
        this.pronouns = pronouns;
        this.birthday = birthday;
        this.username = username;
        this.number = number;
        this.photo_default = photo_default;
        this.profile_private = profile_private;
    }
    public UserkieModel(String UID, String name, int drawable, String username, boolean photo_default, boolean profile_private) {
        this.UID = UID;
        this.name = name;
        this.username = username;
        this.photo_default = photo_default;
        this.profile_private = profile_private;
        this.drawable = drawable;
    }
}
