package com.example.buuktu.models;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.security.keystore.SecureKeyImportUnavailableException;

import com.google.firebase.Timestamp;
import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.Date;

public class WorldkieModel implements Serializable {
    private String UID;
    private String UID_AUTHOR;
    private String name;
    private Timestamp creation_date;
    private Timestamp last_update;
    String id_photo;
    private Drawable photo;
    private boolean photo_default;
    private boolean worldkie_private;
    private boolean draft;

    public WorldkieModel() {
    }

    public WorldkieModel(String UID_AUTHOR, Timestamp creation_date, String name, boolean photo_default, boolean worldkie_private) {
        this.UID_AUTHOR = UID_AUTHOR;
        this.creation_date = creation_date;
        this.name = name;
        this.photo_default = photo_default;
        this.worldkie_private = worldkie_private;
        this.last_update = Timestamp.now(); // Inicializar last_update a la hora actual
    }

    // Constructor 2: UID_AUTHOR, CREATIONDATE, LASTUPDATE, NAME, PHOTODEFAULT, WORLDKIEPRIVATE, PHOTOID
    public WorldkieModel(String UID_AUTHOR, Timestamp creation_date, Timestamp last_update, String name, boolean photo_default, boolean worldkie_private, String id_photo) {
        this.UID_AUTHOR = UID_AUTHOR;
        this.creation_date = creation_date;
        this.last_update = last_update;
        this.name = name;
        this.photo_default = photo_default;
        this.worldkie_private = worldkie_private;
        this.id_photo = id_photo;
    }

    // Constructor 3: UID_AUTHOR, CREATIONDATE, NAME, PHOTODEFAULT, WORLDKIEPRIVATE, DRAFT
    public WorldkieModel(String UID_AUTHOR, String name, boolean photo_default, boolean worldkie_private, boolean draft) {
        this.UID_AUTHOR = UID_AUTHOR;
        this.creation_date = Timestamp.now();
        this.name = name;
        this.photo_default = photo_default;
        this.worldkie_private = worldkie_private;
        this.draft = draft;
        this.last_update = Timestamp.now(); // Inicializar last_update a la hora actual
    }
    public WorldkieModel(String UID_AUTHOR, String name, boolean photo_default, boolean worldkie_private) {
        this.UID_AUTHOR = UID_AUTHOR;
        this.creation_date = Timestamp.now();
        this.name = name;
        this.photo_default = photo_default;
        this.worldkie_private = worldkie_private;
        this.last_update = Timestamp.now(); // Inicializar last_update a la hora actual
    }
    public WorldkieModel(String UID_AUTHOR, String name, boolean photo_default, boolean worldkie_private, boolean draft, String id_photo) {
        this.UID_AUTHOR = UID_AUTHOR;
        this.creation_date = Timestamp.now();
        this.name = name;
        this.photo_default = photo_default;
        this.worldkie_private = worldkie_private;
        this.draft = draft;
        this.last_update = Timestamp.now(); // Inicializar last_update a la hora actual
        this.id_photo=id_photo;
    }
    public WorldkieModel(String UID_AUTHOR, String name, boolean photo_default, boolean worldkie_private, String id_photo) {
        this.UID_AUTHOR = UID_AUTHOR;
        this.creation_date = Timestamp.now();
        this.name = name;
        this.photo_default = photo_default;
        this.worldkie_private = worldkie_private;
        this.last_update = Timestamp.now(); // Inicializar last_update a la hora actual
        this.id_photo=id_photo;
    }
    // Constructor 4: UID_AUTHOR, CREATIONDATE, LASTUPDATE, NAME, PHOTODEFAULT, WORLDKIEPRIVATE, PHOTOID, DRAFT
    public WorldkieModel(String UID_AUTHOR, String name, boolean photo_default, boolean worldkie_private, String id_photo, boolean draft) {
        this.UID_AUTHOR = UID_AUTHOR;
        this.creation_date = Timestamp.now();
        this.last_update = Timestamp.now();
        this.name = name;
        this.photo_default = photo_default;
        this.worldkie_private = worldkie_private;
        this.id_photo = id_photo;
        this.draft = draft;
    }
    public WorldkieModel(String UID,String UID_AUTHOR, Timestamp creation_date, String name, boolean photo_default, boolean worldkie_private) {
        this.UID=UID;
        this.UID_AUTHOR = UID_AUTHOR;
        this.creation_date = creation_date;
        this.name = name;
        this.photo_default = photo_default;
        this.worldkie_private = worldkie_private;
        this.last_update = Timestamp.now(); // Inicializar last_update a la hora actual
    }

    public WorldkieModel(String UID,String UID_AUTHOR, Timestamp creation_date, String name, boolean photo_default, boolean worldkie_private,Timestamp last_update) {
        this.UID=UID;
        this.UID_AUTHOR = UID_AUTHOR;
        this.creation_date = creation_date;
        this.name = name;
        this.photo_default = photo_default;
        this.worldkie_private = worldkie_private;
        this.last_update = last_update; // Inicializar last_update a la hora actual
    }
    public WorldkieModel(String UID, String name, boolean photo_default) {
        this.UID=UID;
        this.name = name;
        this.photo_default = photo_default;
        this.last_update = Timestamp.now(); // Inicializar last_update a la hora actual
    }
    public WorldkieModel(String UID,String name, boolean photo_default, String id_photo) {
        this.UID=UID;
        this.name = name;
        this.photo_default = photo_default;
        this.id_photo=id_photo;
        this.last_update = Timestamp.now();
    }
    // Constructor 2: UID_AUTHOR, CREATIONDATE, LASTUPDATE, NAME, PHOTODEFAULT, WORLDKIEPRIVATE, PHOTOID
    public WorldkieModel(String UID,String UID_AUTHOR, Timestamp creation_date, Timestamp last_update, String name, boolean photo_default, boolean worldkie_private, String id_photo) {
        this.UID=UID;
        this.UID_AUTHOR = UID_AUTHOR;
        this.creation_date = creation_date;
        this.last_update = last_update;
        this.name = name;
        this.photo_default = photo_default;
        this.worldkie_private = worldkie_private;
        this.id_photo = id_photo;
    }

    // Constructor 3: UID_AUTHOR, CREATIONDATE, NAME, PHOTODEFAULT, WORLDKIEPRIVATE, DRAFT
    public WorldkieModel(String UID,String UID_AUTHOR, Timestamp creation_date, String name, boolean photo_default, boolean worldkie_private, boolean draft) {
        this.UID=UID;
        this.UID_AUTHOR = UID_AUTHOR;
        this.creation_date = creation_date;
        this.name = name;
        this.photo_default = photo_default;
        this.worldkie_private = worldkie_private;
        this.draft = draft;
        this.last_update = Timestamp.now(); // Inicializar last_update a la hora actual
    }

    // Constructor 4: UID_AUTHOR, CREATIONDATE, LASTUPDATE, NAME, PHOTODEFAULT, WORLDKIEPRIVATE, PHOTOID, DRAFT
    public WorldkieModel(String UID,String UID_AUTHOR, Timestamp creation_date, Timestamp last_update, String name, boolean photo_default, boolean worldkie_private, String id_photo, boolean draft) {
        this.UID=UID;
        this.UID_AUTHOR = UID_AUTHOR;
        this.creation_date = creation_date;
        this.last_update = last_update;
        this.name = name;
        this.photo_default = photo_default;
        this.worldkie_private = worldkie_private;
        this.id_photo = id_photo;
        this.draft = draft;
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

    public Timestamp getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(Timestamp creation_date) {
        this.creation_date = creation_date;
    }

    public Timestamp getLast_update() {
        return last_update;
    }

    public void setLast_update(Timestamp last_update) {
        this.last_update = last_update;
    }
    public boolean isPhoto_default() {
        return photo_default;
    }
    public void setPhoto_default(boolean photo_default) {
        this.photo_default = photo_default;
    }

    public String getId_photo() {
        return id_photo;
    }

    public void setId_photo(String id_photo) {
        this.id_photo = id_photo;
    }

    public boolean isDraft() {
        return draft;
    }

    public void setDraft(boolean draft) {
        this.draft = draft;
    }
}
