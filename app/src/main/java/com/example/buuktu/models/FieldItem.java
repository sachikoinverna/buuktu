package com.example.buuktu.models;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class FieldItem {
    private String component;
    private String kie;
    private List<String> options;
    private String type;
    private String name;
    private String icon;

    public FieldItem() {
    }

    public static FieldItem fromSnapshot(DocumentSnapshot document) {
        if (document == null || !document.exists()) {
            return null;
        }
        FieldItem fieldItem = new FieldItem();
        fieldItem.setName(document.getString("name"));
        fieldItem.setComponent(document.getString("component"));
        if (document.contains("type")) {
            fieldItem.setType(document.getString("type"));

        }
        // Verificar si el campo "options" existe en el documento antes de intentar obtenerlo
        if (document.contains("options")) {
            fieldItem.setOptions((List<String>) document.get("options"));
        } else {
            // Si "options" no está presente, inicializar la lista como null o una lista vacía, según tu preferencia.
            // Por ejemplo:
            fieldItem.setOptions(null); // O new ArrayList<>();
        }

        // Similar para el campo "icon" si también puede ser opcional
            fieldItem.setIcon(document.getString("icon"));

        return fieldItem;
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

    public void setComponent(String component) {
        this.component = component;
    }

    public void setKie(String kie) {
        this.kie = kie;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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
