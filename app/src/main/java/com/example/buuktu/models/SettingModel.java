package com.example.buuktu.models;

import android.graphics.drawable.Drawable;

public class SettingModel {
    private String name;
    private Drawable drawable;
    private String value;
    public SettingModel(String name, Drawable drawable) {
        this.name = name;
        this.drawable = drawable;
    }
    public SettingModel(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Drawable getDrawable() {
        return drawable;
    }
    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }
}
