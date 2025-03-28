package com.example.buuktu.utils;

public class StringUtils {
    public static String reemplazarUltimaLetra(String palabra, char nuevaLetra) {
        if (palabra == null || palabra.isEmpty()) return palabra; // Manejo de errores

        return palabra.substring(0, palabra.length() - 1) + nuevaLetra;
    }
}
