package com.example.buuktu.utils;

import com.google.android.material.textfield.TextInputEditText;

public class SetUtil {
    public static void setEditTextError(String error, TextInputEditText textInputEditText) {
        textInputEditText.setError(error);
    }
}
