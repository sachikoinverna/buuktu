package com.example.buuktu.models;

import java.util.Date;

public class UserModel {
    private String email;
    private String password;
    private String name;
    private String surname;
    private String pronouns;
    private Date birthday;
    private String username;
    private String number;
    private String UID;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserModel() {
    }

    public UserModel(String email, String password, String name, String surname, String pronouns, Date birthday, String username, String number) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.pronouns = pronouns;
        this.birthday = birthday;
        this.username = username;
        this.number = number;
    }

    public UserModel(String email, String password, String name, String surname, String pronouns, Date birthday, String username, String number, String UID) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.pronouns = pronouns;
        this.birthday = birthday;
        this.username = username;
        this.number = number;
        this.UID = UID;
    }
}
