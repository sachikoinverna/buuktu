package com.example.buuktu.models;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.util.List;

public class WordOfTheDay {
    public static void obtenerPalabraDelDia(FirebaseCallback callback) {
            FirebaseFirestore.getInstance().collection("Wordkies")  // Cambia "palabras" por el nombre de tu colección
                .whereEqualTo("type", "Wordkie of the day")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        DocumentSnapshot document = querySnapshot.getDocuments().get(0);

                        if (document.contains("words")) {
                            List<String> words = (List<String>) document.get("words");
                            if (words != null && LocalDate.now().getDayOfYear() - 1 < words.size()) {
                                String palabraDelDia = words.get(LocalDate.now().getDayOfYear() - 1); // El índice empieza en 0
                                callback.onCallback(palabraDelDia);
                            } else {
                                callback.onCallback("Palabra no encontrada");
                            }
                        } else {
                            callback.onCallback("Campo 'words' no encontrado en el documento");
                        }
                    } else {
                        callback.onCallback("Documento no encontrado");
                    }
                })
                .addOnFailureListener(e -> callback.onCallback("Error al obtener la palabra"));
    }

    public interface FirebaseCallback {
        void onCallback(String palabra);
    }}
