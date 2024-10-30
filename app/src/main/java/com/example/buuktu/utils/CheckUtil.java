package com.example.buuktu.utils;

import android.util.Patterns;
import android.widget.TextView;

import com.example.buuktu.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Pattern;

public class CheckUtil {
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
