package com.example.buuktu.utils;

import android.content.Context;
import android.os.Build;
import android.util.Patterns;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.buuktu.R;
import com.example.buuktu.views.Register;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.regex.Pattern;

public class CheckUtil {
    private static FirebaseFirestore db= FirebaseFirestore.getInstance();
    private static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    public static Boolean checkExistentUsername(String username){
        final Boolean[] exists = {false};
        CollectionReference dbUsers = db.collection("Users");
        dbUsers.whereEqualTo("username", username).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){
                    exists[0] = false;
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                exists[0] = true;
            }
        });
        return exists[0];
    }
    public static Boolean checkExistentEmail(String email){
        final Boolean[] exists = {false};
        firebaseAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<String> signInMethods = task.getResult().getSignInMethods();
                if (signInMethods != null && !signInMethods.isEmpty()) {
                    exists[0] = true;
                } else {
                    exists[0] = false;
                }
            }
        });
        return exists[0];
    }
    public static boolean checkSpecialChar(String text)
    {
//        return text.matches("[\\p{Punct}&&[^_]]\\p{Cntrl}\\p{S}\\s");

        return text.matches("[!@#$%&*()+=|<>?{}.,]");    }
    public static boolean checkNumbers(String text){
            return text.matches(".*\\d.*"); // Verifica si hay algún dígito en el texto

    }
    public static boolean checkEmailStructure(String email){
            return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    public static boolean checkTextEmpty(TextInputEditText textInputEditText){
            return textInputEditText.getText().toString().trim().isEmpty();
    }
    public static boolean checkPasswordLength(String password){
            return password.length()<8;
    }
    public static boolean checkUppercase(String password){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return password != null && password.chars().anyMatch(Character::isUpperCase);
        }
        return false;
    }
    public static boolean checkSpecialCharacter(String password){
        return password != null && password.matches(".*[\\W_].*");
    }

    public static void setErrorMessage(String error, TextView textView) {
        textView.setText(error);
    }
}
