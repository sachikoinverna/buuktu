package com.example.buuktu.models;

import com.google.firebase.Timestamp;

public class UserkieModel {
    private String name;
    private String pronouns;
    private Timestamp birthday;
    private String username;
    private String number;
    private String UID;
    private String email;
    private boolean photo_default;
    private boolean profile_private;
    private String photo_id;

    public String getPhoto_id() {
        return photo_id;
    }

    public void setPhoto_id(String photo_id) {
        this.photo_id = photo_id;
    }

    public String getEmail() {
        return email;
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

    public Timestamp getBirthday() {
        return birthday;
    }

    public void setBirthday(Timestamp birthday) {
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

    public UserkieModel(String photo_id, boolean profile_private, boolean photo_default, String email, String number, String username, Timestamp birthday, String pronouns, String name) {
        this.photo_id = photo_id;
        this.profile_private = profile_private;
        this.photo_default = photo_default;
        this.email = email;
        this.number = number;
        this.username = username;
        this.birthday = birthday;
        this.pronouns = pronouns;
        this.name = name;
    }

    public UserkieModel(String name, String pronouns, boolean profile_private) {
        this.name = name;
        this.pronouns = pronouns;
        this.profile_private = profile_private;
    }

    public UserkieModel(String name, String username, boolean profile_private, boolean photo_default, String photo_id) {
        this.name = name;
        this.username = username;
        this.profile_private = profile_private;
        this.photo_default = photo_default;
        this.photo_id = photo_id;
    }
    public UserkieModel(String name, String username, boolean profile_private, boolean photo_default) {
        this.name = name;
        this.username = username;
        this.profile_private = profile_private;
        this.photo_default = photo_default;
    }
    public UserkieModel(String UID,String name, String username, boolean profile_private, boolean photo_default, String photo_id) {
        this.UID = UID;
        this.name = name;
        this.username = username;
        this.profile_private = profile_private;
        this.photo_default = photo_default;
        this.photo_id = photo_id;
    }
    public UserkieModel(String UID,String name, String username, boolean profile_private, boolean photo_default) {
        this.UID=UID;
        this.name = name;
        this.username = username;
        this.profile_private = profile_private;
        this.photo_default = photo_default;
    }
    public UserkieModel(String name, String pronouns, Timestamp birthday, String username, String number, String email, boolean photo_default, boolean profile_private) {
        this.name = name;
        this.pronouns = pronouns;
        this.birthday = birthday;
        this.username = username;
        this.number = number;
        this.email = email;
        this.photo_default = photo_default;
        this.profile_private = profile_private;
    }

}
