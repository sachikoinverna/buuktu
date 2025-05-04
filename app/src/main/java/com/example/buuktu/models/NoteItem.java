package com.example.buuktu.models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;

public class NoteItem {
    String title;
    String content;
    Timestamp last_update;
    String UID_USER;
    String UID;
    public static NoteItem fromSnapshot(DocumentSnapshot document) {
        if (document == null || !document.exists()) {
            return null;
        }
        NoteItem noteItem = new NoteItem();
        noteItem.setTitle(document.getString("title"));
        noteItem.setContent(document.getString("content"));
        noteItem.setUID(document.getString("UID"));
        noteItem.setUID_USER(document.getString("UID_USER"));
        noteItem.setLast_update(document.getTimestamp("last_update"));
        return noteItem;
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

    public void setUID_USER(String UID_USER) {
        this.UID_USER = UID_USER;
    }

    public void setLast_update(Timestamp last_update) {
        this.last_update = last_update;
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
