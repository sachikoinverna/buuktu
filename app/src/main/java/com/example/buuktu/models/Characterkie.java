package com.example.buuktu.models;

import android.graphics.drawable.Drawable;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Exclude;

import java.util.Date;

public class Characterkie {
    String UID_WORLDKIE;
    String UID_AUTHOR;
    private String UID;
    private String name;
    private String pronouns;
    private String birthday;
    private boolean photo_default;
    private boolean draft;
    boolean characterkie_private;
    private String photo_id;
    private String status;
    private String gender;
    public Characterkie() {
    }

    public Characterkie(String UID, String name) {
        this.UID=UID;
        this.name=name;
    }

    public Characterkie(String UID_WORLDKIE, String UID_AUTHOR, String name, String pronouns, String birthday, boolean photo_default, boolean draft, boolean characterkie_private, String photo_id, String status, String gender) {
        this.UID_WORLDKIE = UID_WORLDKIE;
        this.UID_AUTHOR = UID_AUTHOR;
        this.name = name;
        this.pronouns = pronouns;
        this.birthday = birthday;
        this.photo_default = photo_default;
        this.draft = draft;
        this.characterkie_private = characterkie_private;
        this.photo_id = photo_id;
        this.status = status;
        this.gender = gender;
    }

    public static Characterkie fromSnapshot(DocumentSnapshot document) {
        if (document == null || !document.exists()) {
            return null;
        }
        Characterkie characterkie = new Characterkie();
        characterkie.setName(document.getString("name"));
        characterkie.setUID(document.getId());

        characterkie.setBirthday(document.getString("birthday"));
        characterkie.setPhoto_default(document.getBoolean("photo_default"));
        characterkie.setDraft(document.getBoolean("draft"));
        characterkie.setCharacterkie_private(document.getBoolean("characterkie_private"));
        characterkie.setPhoto_id(document.getString("photo_id"));
        characterkie.setPronouns(document.getString("pronouns"));
        return characterkie;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPhoto_id(String photo_id) {
        this.photo_id = photo_id;
    }

    public String getPhoto_id() {
        return photo_id;
    }

    public String getUID_AUTHOR() {
        return UID_AUTHOR;
    }

    public void setUID_AUTHOR(String UID_AUTHOR) {
        this.UID_AUTHOR = UID_AUTHOR;
    }

    public boolean isCharacterkie_private() {
        return characterkie_private;
    }

    public void setCharacterkie_private(boolean characterkie_private) {
        this.characterkie_private = characterkie_private;
    }

    public boolean isDraft() {
        return draft;
    }

    public void setDraft(boolean draft) {
        this.draft = draft;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Exclude
    public String getUID() {
        return UID;
    }

    public String getUID_WORLDKIE() {
        return UID_WORLDKIE;
    }


    public void setUID_WORLDKIE(String UID_WORLDKIE) {
        this.UID_WORLDKIE = UID_WORLDKIE;
    }
    @Exclude
    public void setUID(String UID) {
        this.UID = UID;
    }
    public boolean isPhoto_default() {
        return photo_default;
    }
    public void setPhoto_default(boolean photo_default) {
        this.photo_default = photo_default;
    }
}
