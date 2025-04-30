package com.example.buuktu.models;

import java.util.List;

public class FieldItem {
    private String component;
    private String kie;
    private List<String> options;
    private String type;
    private String name;
    private String icon;

    // Constructor para elementos con RadioButton
    public FieldItem(String component, String kie, List<String> options,String icon, String name) {
        this.component = component;
        this.kie = kie;
        this.options = options;
        this.icon = icon;
    }

    // Constructor para elementos EditText
    public FieldItem(String component, String kie, String type, String icon, String name) {
        this.component = component;
        this.kie = kie;
        this.type = type;
        this.icon = icon;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldItem fieldItem = (FieldItem) o;
        // Compara por un identificador único, como el 'type' o 'name' si son únicos
        return java.util.Objects.equals(type, fieldItem.type) &&
                java.util.Objects.equals(name, fieldItem.name);
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

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }
}
