package com.example.buuktu.models;

import android.os.Build;

import java.time.LocalDate;
import java.util.Random;

public class NumberOfTheDay {
    private final int numberOfTheDay;

    public NumberOfTheDay(int min, int max) {
        this.numberOfTheDay = obtainNumberOfTheDay(min, max);
    }
    public static int obtainNumberOfTheDay(int min, int max) {
        LocalDate fechaActual = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            fechaActual = LocalDate.now();
        }

        // Generamos una semilla más única combinando año, mes y día
        int semilla = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            semilla = fechaActual.getYear() * 10000 + fechaActual.getMonthValue() * 100 + fechaActual.getDayOfMonth();
        }

        Random random = new Random(semilla);
        return random.nextInt((max - min) + 1) + min;
    }
    public int getNumberOfTheDay() {
        return numberOfTheDay;
    }

    public static void main(String[] args) {
        NumberOfTheDay number = new NumberOfTheDay(1, 100);
        System.out.println("Número del día: " + number);
    }
}
