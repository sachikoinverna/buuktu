package com.example.buuktu.utils;

import android.content.Context;
import android.os.Build;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.buuktu.R;
import com.example.buuktu.listeners.OnEmailCheckedCallback;
import com.example.buuktu.views.Register;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.function.Consumer;
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
    public static void checkExistentEmail(String email,final OnEmailCheckedCallback callback){
        final Boolean[] exists = {false};
        firebaseAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<String> signInMethods = task.getResult().getSignInMethods();
                callback.onEmailChecked(signInMethods != null && !signInMethods.isEmpty());
            } else {
                callback.onEmailChecked(false);
            }

        });

    }
    public static boolean handlerCheckPassword(Context context,TextInputEditText textInputEditText,TextView textViewError) {
        String text = textInputEditText.getText().toString();
        if (CheckUtil.checkTextEmpty(text)) {
            CheckUtil.setErrorMessage(context.getString(R.string.passwordErrorEmpty), textViewError);
            return false;

        } else if (text.length() < 8) {
            CheckUtil.setErrorMessage(String.valueOf((R.string.passwordTooShort)), textViewError);
            return false;

        } else if (!CheckUtil.checkSpecialCharacter(text)) {
            CheckUtil.setErrorMessage(context.getString(R.string.passwordErrorSpecialChar), textViewError);
            return false;
        } else if (!CheckUtil.checkUppercase(text)) {
            CheckUtil.setErrorMessage(context.getString(R.string.passwordErrorUppercase), textViewError);
            return false;
        }
        CheckUtil.setErrorMessage(null, textViewError);
        return true;
    }
    public static boolean handlerCheckPasswordRepeat(Context context,TextInputEditText textInputEditTextRepeat,TextInputEditText textInputEditTextOriginal,TextView textViewError) {
        String textRepeat = textInputEditTextRepeat.getText().toString();
        String textOriginal = textInputEditTextOriginal.getText().toString();
        if (!textRepeat.equals(textOriginal)) {
            CheckUtil.setErrorMessage(context.getString(R.string.passwordErrorRepeat), textViewError);
            return false;
        }
        CheckUtil.setErrorMessage(null, textViewError);
        return true;
    }
    public static boolean handlerCheckUser(Context context,TextInputEditText textInputEditText,TextView textViewError) {
        String text = textInputEditText.getText().toString();
        if (CheckUtil.checkTextEmpty(text)) {
            CheckUtil.setErrorMessage(context.getString(R.string.userErrorEmpty), textViewError);
            return false;
        } else if (CheckUtil.checkExistentUsername(text)) {
            CheckUtil.setErrorMessage(context.getString(R.string.userErrorExists), textViewError);
            return false;
        }
        CheckUtil.setErrorMessage(null, textViewError);
        return true;
    }
    public static boolean handlerCheckBirthdayDate(Context context, TextInputEditText textInputEditText, TextView textViewError) {
        String text = textInputEditText.getText().toString();
        if (CheckUtil.checkTextEmpty(text)) {
            CheckUtil.setErrorMessage(context.getString(R.string.birthdayErrorEmpty), textViewError);
            return false;
        }
        CheckUtil.setErrorMessage(null, textViewError);
        return true;
    }
    public static boolean handlerCheckTelephone(Context context,TextInputEditText textInputEditText,TextView textViewError) {
        String text = textInputEditText.getText().toString();
        if (CheckUtil.checkTextEmpty(text)) {
            CheckUtil.setErrorMessage(context.getString(R.string.telephoneErrorEmpty), textViewError);
            return false;
        }
        CheckUtil.setErrorMessage(null, textViewError);
        return true;
    }

    public static boolean handlerCheckName(Context context,TextInputEditText textInputEditText,TextView textViewError) {
        String text = textInputEditText.getText().toString();
        if (CheckUtil.checkTextEmpty(text)) {
            CheckUtil.setErrorMessage(context.getString(R.string.nameErrorEmpty), textViewError);
            return false;
        } else if (!CheckUtil.checkNumbers(text)){
            CheckUtil.setErrorMessage(context.getString(R.string.numberErrorTextField), textViewError);
            return false;
        }
        CheckUtil.setErrorMessage(null, textViewError);
        return true;
    }
    public static boolean handlerCheckPronouns(Context context,TextInputEditText textInputEditText,TextView textViewError) {
        String text = textInputEditText.getText().toString();
        if (CheckUtil.checkTextEmpty(text)) {
            CheckUtil.setErrorMessage(context.getString(R.string.pronounsErrorEmpty), textViewError);
            return false;
        }
        CheckUtil.setErrorMessage(null, textViewError);
        return true;
    }
    public static boolean handlerCheckEmail(Context context,TextInputEditText textInputEditText,TextView textViewError) {
        String text = textInputEditText.getText().toString();
        boolean valid[]={true};
        if (CheckUtil.checkTextEmpty(text)) {
            CheckUtil.setErrorMessage(context.getString(R.string.emailErrorEmpty), textViewError);
            return false;
        } else if (!CheckUtil.checkEmailStructure(text)) {
            CheckUtil.setErrorMessage(context.getString(R.string.emailErrorFormat), textViewError);
            return false;
        }else {
            CheckUtil.checkExistentEmail(text, exists -> {
                if (exists) {
                    CheckUtil.setErrorMessage("Email existente", textViewError);
                    valid[0]=false;
                }

            });
        }
        if(!valid[0]){
            return false;
        }
        CheckUtil.setErrorMessage(null, textViewError);
        return true;
    }
    public static boolean checkSpecialChar(String text)
    {
        return text.matches("[!@#$%&*()+=|<>?{}.,]");
    }
    public static boolean checkNumbers(String text){
            return text.matches(".*\\d.*"); // Verifica si hay algún dígito en el texto

    }
    public static boolean checkEmailStructure(String email){
            return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    public static boolean checkTextEmpty(String text){
            return text.isEmpty();
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
