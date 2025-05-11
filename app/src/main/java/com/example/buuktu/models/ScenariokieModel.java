package com.example.buuktu.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.firestore.DocumentSnapshot;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ScenariokieModel {
    private String UID;
    private String name;
    private String WORDLKIE_UID;
    private String AUTHOR_UID;
    private boolean draft;
    private boolean scenariokie_private;
    private boolean photo_default;
    private String photo_id;

    public ScenariokieModel() {
    }

    public ScenariokieModel(String UID, String name, String WORDLKIE_UID, String AUTHOR_UID, boolean draft, boolean scenariokie_private, boolean photo_default, String photo_id) {
        this.UID = UID;
        this.name = name;
        this.WORDLKIE_UID = WORDLKIE_UID;
        this.AUTHOR_UID = AUTHOR_UID;
        this.draft = draft;
        this.scenariokie_private = scenariokie_private;
        this.photo_default = photo_default;
        this.photo_id = photo_id;
    }
    public static ScenariokieModel fromSnapshot(DocumentSnapshot document){
        if (document == null || !document.exists()) {
            return null;
        }
        ScenariokieModel scenariokieModel = new ScenariokieModel();
        scenariokieModel.setName(document.getString("name"));
        scenariokieModel.setAUTHOR_UID(document.getString("AUTHOR_UID"));
        scenariokieModel.setUID(document.getString("UID"));
        scenariokieModel.setWORDLKIE_UID(document.getString("WORLDKIE_UID"));
        scenariokieModel.setScenariokie_private(document.getBoolean("scenario_private"));
        scenariokieModel.setDraft(document.getBoolean("draft"));
        scenariokieModel.setPhoto_default(document.getBoolean("photo_default"));
        if (document.contains("photo_id")) {
            scenariokieModel.setPhoto_id(document.getString("photo_id"));

        }
        return scenariokieModel;
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
    @Exclude
    public String getUID() {
        return UID;
    }
    @Exclude
    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWORDLKIE_UID() {
        return WORDLKIE_UID;
    }

    public void setWORDLKIE_UID(String WORDLKIE_UID) {
        this.WORDLKIE_UID = WORDLKIE_UID;
    }

    public String getAUTHOR_UID() {
        return AUTHOR_UID;
    }

    public void setAUTHOR_UID(String AUTHOR_UID) {
        this.AUTHOR_UID = AUTHOR_UID;
    }

    public boolean isDraft() {
        return draft;
    }

    public void setDraft(boolean draft) {
        this.draft = draft;
    }

    public boolean isScenariokie_private() {
        return scenariokie_private;
    }

    public void setScenariokie_private(boolean scenariokie_private) {
        this.scenariokie_private = scenariokie_private;
    }

    public boolean isPhoto_default() {
        return photo_default;
    }

    public void setPhoto_default(boolean photo_default) {
        this.photo_default = photo_default;
    }

    public String getPhoto_id() {
        return photo_id;
    }

    public void setPhoto_id(String photo_id) {
        this.photo_id = photo_id;
    }
}
