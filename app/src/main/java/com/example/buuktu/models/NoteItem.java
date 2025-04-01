package com.example.buuktu.models;

public class NoteItem {
    String title;
    String content;
    String color;

    public NoteItem(String content, String title) {
        this.content = content;
        this.title = title;
    }
    public NoteItem(String content, String title, String color) {
        this(content, title);
        this.color = color;
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
    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }
}
