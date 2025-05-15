package com.example.buuktu.models;

import java.time.LocalDate;
import java.util.Random;

public class NumberOfTheDay {
    public static int obtainNumberOfTheDay(int min, int max) {
        LocalDate fechaActual = LocalDate.now();
        int semilla = fechaActual.getYear() * 10000 + fechaActual.getMonthValue() * 100 + fechaActual.getDayOfMonth();
        Random random = new Random(semilla);
        return random.nextInt((max - min) + 1) + min;
    }
}
