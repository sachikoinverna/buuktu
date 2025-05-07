package com.example.buuktu.models;

public class BirthdayDate {
    int year;
    int day;
    int month;
    int option;
    public BirthdayDate(int year, int day, int month, int option) {
        this.year = year;
        this.day = day;
        this.month = month;
        this.option=option;
    }

    public int getOption() {
        return option;
    }

    public void setOption(int option) {
        this.option = option;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }
}
