package com.example.buuktu.utils;

import android.content.Context;
import android.os.Build;
import android.util.Patterns;
import android.widget.TextView;

import com.example.buuktu.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

public class CheckUtil {
    public static boolean handlerCheckPassword(Context context,TextInputEditText textInputEditText,TextView textViewError) {
        String text = textInputEditText.getText().toString();
        if (CheckUtil.checkTextEmpty(text)) {
            CheckUtil.setErrorMessage(context.getString(R.string.passwordErrorEmpty), textViewError);
            return false;

        } else if (text.length() < 8) {
            CheckUtil.setErrorMessage(context.getString(R.string.passwordTooShort), textViewError);
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
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        Phonenumber.PhoneNumber number = null;
        try {
            number = phoneUtil.parse(text, "ES");
        } catch (NumberParseException e) {
            System.err.println("NumberParseException was thrown: " + e.toString());
        }
        if (CheckUtil.checkTextEmpty(text)) {
            CheckUtil.setErrorMessage(context.getString(R.string.telephoneErrorEmpty), textViewError);
            return false;
        } else if (!phoneUtil.isValidNumber(number)) {
            CheckUtil.setErrorMessage("Numero incorrecto", textViewError);
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
        } else if (CheckUtil.checkNumbers(text)){
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
        if (CheckUtil.checkTextEmpty(text)) {
            CheckUtil.setErrorMessage(context.getString(R.string.emailErrorEmpty), textViewError);
            return false;
        } else if (!CheckUtil.checkEmailStructure(text)) {
            CheckUtil.setErrorMessage(context.getString(R.string.emailErrorFormat), textViewError);
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
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    public static boolean checkTextEmpty(String text){
            return text.isEmpty();
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
