package com.example.buuktu.models;

import com.google.firebase.Timestamp;

public class NoteItem {
    String title;
    String content;
    Timestamp last_update;
    String UID_USER;
    String UID;
    public NoteItem(String content, String title,String UID_USER) {
        this.content = content;
        this.title = title;
        this.last_update = Timestamp.now();
        this.UID_USER=UID_USER;
    }
    public NoteItem(String content, String title,String UID_USER,Timestamp last_update,String UID) {
        this.content = content;
        this.title = title;
        this.last_update = last_update;
        this.UID_USER=UID_USER;
        this.UID=UID;
    }
    public NoteItem() {
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public String getUID_USER() {
        return UID_USER;
    }

    public Timestamp getLast_update() {
        return last_update;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }
}
