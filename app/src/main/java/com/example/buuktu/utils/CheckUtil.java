package com.example.buuktu.utils;

import android.content.Context;
import android.util.Patterns;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.buuktu.R;
import com.example.buuktu.views.Register;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.regex.Pattern;

public class CheckUtil {
    private static FirebaseFirestore db= FirebaseFirestore.getInstance();
    public static Boolean checkExistentUsername(Context context){
        final Boolean[] exists = {false};
        CollectionReference dbUsers = db.collection("Users");
        dbUsers.whereEqualTo("username", "chikoritaxserperior").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){
                    Toast.makeText(context, "Ya existe un usuario con ese nombre", Toast.LENGTH_LONG).show();
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
    public static boolean checkNumbers(String text){
        for ( int i=0;i<=text.length();i++){
            for (int z=0;z<10;z++){
                if (Integer.valueOf(text)==z){
                    return false;
                }
            }
        }
        return true;
    }
    public static boolean checkEmailStructure(String email){
        try {
            Pattern pattern = Patterns.EMAIL_ADDRESS;
            return pattern.matcher(email).matches();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static boolean checkTextEmpty(TextInputEditText textInputEditText){
        if(textInputEditText.getText().toString().equals("")){
            return true;
        }
        return false;
    }
    public static boolean checkPasswordLength(String password){
        if(password.length()<8){
            return false;
        }
        return true;
    }
    public static boolean checkUppercase(String password){
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (Character.isUpperCase(c))
                return true;
        }
        return false;
    }
    public static boolean checkSpecialCharacter(String password){
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (c >= 33 && c <= 46 || c == 64) {
                return true;
            }
        }
        return false;
    }

    public static void setErrorMessage(String error, TextView textView) {
        textView.setText(error);
    }
}
