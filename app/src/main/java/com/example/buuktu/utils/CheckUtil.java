package com.example.buuktu.utils;

import android.content.Context;
import android.os.Build;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.buuktu.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.lang.reflect.Field;

public class CheckUtil {
    public static boolean handlerCheckPassword(Context context,TextInputEditText textInputEditText,TextInputLayout textInputLayout) {
        String text = textInputEditText.getText().toString();
        if (CheckUtil.checkTextEmpty(text)) {
            CheckUtil.setErrorMessage(context.getString(R.string.passwordErrorEmpty), textInputLayout);
            return false;

        } else if (text.length() < 8) {
            CheckUtil.setErrorMessage(context.getString(R.string.passwordTooShort), textInputLayout);
            return false;

        } else if (!CheckUtil.checkSpecialCharacter(text)) {
            CheckUtil.setErrorMessage(context.getString(R.string.passwordErrorSpecialChar), textInputLayout);
            return false;
        } else if (!CheckUtil.checkUppercase(text)) {
            CheckUtil.setErrorMessage(context.getString(R.string.passwordErrorUppercase), textInputLayout);
            return false;
        }
        CheckUtil.setErrorMessage(null, textInputLayout);
        return true;
    }
    public static boolean handlerCheckPasswordRepeat(Context context,TextInputEditText textInputEditTextRepeat,TextInputEditText textInputEditTextOriginal,TextInputLayout textInputLayout) {
        String textRepeat = textInputEditTextRepeat.getText().toString();
        String textOriginal = textInputEditTextOriginal.getText().toString();
        if (!textRepeat.equals(textOriginal)) {
            CheckUtil.setErrorMessage(context.getString(R.string.passwordErrorRepeat), textInputLayout);
            return false;
        }
        CheckUtil.setErrorMessage(null, textInputLayout);
        return true;
    }
    public static boolean handlerCheckNewIsTheSameAsOld(Context context,TextInputEditText textInputEditTextNew,TextInputEditText textInputEditTextOld,TextInputLayout textInputLayoutNew) {
        String textNew = textInputEditTextNew.getText().toString();
        String textOld = textInputEditTextOld.getText().toString();
        if (!textNew.equals(textOld)) {
            CheckUtil.setErrorMessage(context.getString(R.string.passwordErrorRepeat), textInputLayoutNew);
            return false;
        }
        CheckUtil.setErrorMessage(null, textInputLayoutNew);
        return true;
    }
    public static boolean handlerCheckNewIsTheSameAsOld(Context context,TextInputEditText textInputEditTextNew,String old,TextInputLayout textInputLayoutNew) {
        String textNew = textInputEditTextNew.getText().toString();
        if (!textNew.equals(old)) {
            CheckUtil.setErrorMessage(context.getString(R.string.passwordErrorRepeat), textInputLayoutNew);
            return false;
        }
        CheckUtil.setErrorMessage(null, textInputLayoutNew);
        return true;
    }
    public static boolean handlerCheckUser(Context context,TextInputEditText textInputEditText,TextInputLayout textInputLayout) {
        String text = textInputEditText.getText().toString();
        if (CheckUtil.checkTextEmpty(text)) {
            CheckUtil.setErrorMessage(context.getString(R.string.userErrorEmpty), textInputLayout);
            return false;
        }
        CheckUtil.setErrorMessage(null, textInputLayout);
        return true;
    }
    public static boolean handlerCheckBirthdayDate(Context context, TextInputEditText textInputEditText, TextInputLayout textInputLayout) {
        String text = textInputEditText.getText().toString();
        if (CheckUtil.checkTextEmpty(text)) {
            CheckUtil.setErrorMessage(context.getString(R.string.birthdayErrorEmpty), textInputLayout);
            return false;
        }
        CheckUtil.setErrorMessage(null, textInputLayout);
        return true;
    }
    public static boolean handlerCheckTelephone(Context context,TextInputEditText textInputEditText,TextInputLayout textInputLayout) {
        String text = textInputEditText.getText().toString();
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        Phonenumber.PhoneNumber number = null;
        try {
            number = phoneUtil.parse(text, "ES");
        } catch (NumberParseException e) {
            System.err.println("NumberParseException was thrown: " + e.toString());
        }
        if (CheckUtil.checkTextEmpty(text)) {
            CheckUtil.setErrorMessage(context.getString(R.string.telephoneErrorEmpty), textInputLayout);
            return false;
        } else if (!phoneUtil.isValidNumber(number)) {
            CheckUtil.setErrorMessage("Numero incorrecto", textInputLayout);
            return false;
        }
        CheckUtil.setErrorMessage(null, textInputLayout);
        return true;
    }

    public static boolean handlerCheckName(Context context,TextInputEditText textInputEditText,TextInputLayout textInputLayout) {
        String text = textInputEditText.getText().toString();
        if (CheckUtil.checkTextEmpty(text)) {
            CheckUtil.setErrorMessage(context.getString(R.string.nameErrorEmpty), textInputLayout);
            return false;
        } else if (CheckUtil.checkNumbers(text)){
            CheckUtil.setErrorMessage(context.getString(R.string.numberErrorTextField), textInputLayout);
            return false;
        }
        CheckUtil.setErrorMessage(null, textInputLayout);
        return true;
    }
    public static boolean handlerCheckPronouns(Context context,TextInputEditText textInputEditText,TextInputLayout textInputLayout) {
        String text = textInputEditText.getText().toString();
        if (CheckUtil.checkTextEmpty(text)) {
            CheckUtil.setErrorMessage(context.getString(R.string.pronounsErrorEmpty), textInputLayout);
            return false;
        }
        CheckUtil.setErrorMessage(null, textInputLayout);
        return true;
    }
    public static boolean handlerCheckEmail(Context context,TextInputEditText textInputEditText,TextInputLayout textInputLayout) {
        String text = textInputEditText.getText().toString();
        if (CheckUtil.checkTextEmpty(text)) {
            CheckUtil.setErrorMessage(context.getString(R.string.emailErrorEmpty), textInputLayout);
            return false;
        } else if (!CheckUtil.checkEmailStructure(text)) {
            CheckUtil.setErrorMessage(context.getString(R.string.emailErrorFormat), textInputLayout);
            return false;
        }
        CheckUtil.setErrorMessage(null, textInputLayout);
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

    public static void setErrorMessage(String error, TextInputLayout textInputLayout) {
        textInputLayout.setError(error);
    }
}
