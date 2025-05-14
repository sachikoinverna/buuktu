package com.example.buuktu.models;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Exclude;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class CharacterkieModel {
    String UID_WORLDKIE;
    String UID_AUTHOR;
    private String UID;
    private String name;
    private String pronouns;
    private  String birthday;
    private String birthday_format;
    private boolean photo_default;
    private boolean draft;
    boolean characterkie_private;
    private String photo_id;
    private String status;
    private String gender;
    public CharacterkieModel() {
    }

    public static CharacterkieModel fromSnapshot(DocumentSnapshot document) {
        if (document == null || !document.exists()) {
            return null;
        }
        CharacterkieModel characterkie = new CharacterkieModel();
        characterkie.setName(document.getString("name"));
        characterkie.setUID(document.getId());
        characterkie.setUID_AUTHOR((document.getString("UID_AUTHOR")));
        characterkie.setUID_WORLDKIE((document.getString("UID_WORLDKIE")));
        characterkie.setBirthday(document.getString("birthday"));
        characterkie.setBirthday_format(document.getString("birthday_format"));
        characterkie.setGender(document.getString("gender"));
        characterkie.setStatus(document.getString("status"));
        characterkie.setPhoto_default(document.getBoolean("photo_default"));
        characterkie.setDraft(document.getBoolean("draft"));
        characterkie.setCharacterkie_private(document.getBoolean("characterkie_private"));
        if(document.contains("photo_id")) {
            characterkie.setPhoto_id(document.getString("photo_id"));
        }
        characterkie.setPronouns(document.getString("pronouns"));
        return characterkie;
    }
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        Field[] fields = this.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(this);
                if (value != null) {
                    map.put(field.getName(), value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return map;
    }

    public String getBirthday_format() {
        return birthday_format;
    }

    public void setBirthday_format(String birthday_format) {
        this.birthday_format = birthday_format;
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
