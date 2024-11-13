package com.example.buuktu.models;

import java.util.Date;

public class UserModel {
    private String name;
    private String surname;
    private String pronouns;
    private Date birthday;
    private String username;
    private String number;
    private String UID;
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getPronouns() {
        return pronouns;
    }

    public void setPronouns(String pronouns) {
        this.pronouns = pronouns;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserModel() {
    }

    public UserModel(String email,String UID, String name, String surname, String pronouns, Date birthday, String username, String number) {
        this.email = email;
        this.UID = UID;
        this.name = name;
        this.surname = surname;
        this.pronouns = pronouns;
        this.birthday = birthday;
        this.username = username;
        this.number = number;
    }
}
