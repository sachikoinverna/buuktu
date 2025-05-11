package com.example.buuktu.models;

public class CardItem {
    private final int iconResId;
    private final String text;

    public CardItem(int iconResId, String text) {
        this.iconResId = iconResId;
        this.text = text;
    }

    public int getIconResId() {
        return iconResId;
    }

    public String getText() {
        return text;
    }
}
