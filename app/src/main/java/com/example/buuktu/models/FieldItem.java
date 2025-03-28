package com.example.buuktu.models;

import java.util.List;

public class FieldItem {
    private String component;
    private String kie;
    private List<String> options;
    private String type;

    // Constructor para elementos con RadioButton
    public FieldItem(String component, String kie, List<String> options) {
        this.component = component;
        this.kie = kie;
        this.options = options;
    }
    // Constructor para elementos EditText
    public FieldItem(String component, String kie, String type) {
        this.component = component;
        this.kie = kie;
        this.type = type;
    }
    // Getters y Setters
    public String getComponent() {
        return component;
    }

    public String getKie() {
        return kie;
    }

    public List<String> getOptions() {
        return options;
    }

    public String getType() {
        return type;
    }
}
