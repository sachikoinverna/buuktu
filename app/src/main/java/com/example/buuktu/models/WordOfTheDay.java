package com.example.buuktu.models;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.util.List;

public class WordOfTheDay {
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static void obtenerPalabraDelDia(FirebaseCallback callback) {
        // Fecha actual para obtener el día del año
            int dayOfYear = LocalDate.now().getDayOfYear();


        // Obtén el documento donde type = "Worldkie of the day"
        db.collection("Wordkies")  // Cambia "palabras" por el nombre de tu colección
                .whereEqualTo("type", "Wordkie of the day")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        // Obtén el primer documento que cumple con la condición
                        DocumentSnapshot document = querySnapshot.getDocuments().get(0);

                        // Extrae el array "words" del documento
                        if (document.contains("words")) {
                            // Obtén la palabra correspondiente al día actual
                            List<String> words = (List<String>) document.get("words");
                            if (words != null && dayOfYear - 1 < words.size()) {
                                String palabraDelDia = words.get(dayOfYear - 1); // El índice empieza en 0
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
