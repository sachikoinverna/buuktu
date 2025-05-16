package com.example.buuktu.utils;

import android.content.Context;
import android.util.Patterns;

import com.example.buuktu.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

public class CheckUtil {
    public static boolean handlerCheckPassword(Context context,TextInputEditText textInputEditText,TextInputLayout textInputLayout) {
        if (!CheckUtil.checkTextEmpty(context,textInputLayout,textInputEditText.getText().toString())) {
            return false;
        } else if ( textInputEditText.getText().toString().length() < 8) {
            CheckUtil.setErrorMessage(context.getString(R.string.passwordTooShort), textInputLayout);
            return false;

        } else if (!CheckUtil.checkSpecialCharacter( textInputEditText.getText().toString())) {
            CheckUtil.setErrorMessage(context.getString(R.string.passwordErrorSpecialChar), textInputLayout);
            return false;
        } else if (!CheckUtil.checkUppercase( textInputEditText.getText().toString())) {
            CheckUtil.setErrorMessage(context.getString(R.string.passwordErrorUppercase), textInputLayout);
            return false;
        }
        CheckUtil.setErrorMessage(null, textInputLayout);
        return true;
    }
    public static boolean handlerCheckPasswordRepeat(Context context,TextInputEditText textInputEditTextRepeat,TextInputEditText textInputEditTextOriginal,TextInputLayout textInputLayout) {
        if (!textInputEditTextRepeat.getText().toString().equals(textInputEditTextOriginal.getText().toString())) {
            CheckUtil.setErrorMessage(context.getString(R.string.passwordErrorRepeat), textInputLayout);
            return false;
        }
        CheckUtil.setErrorMessage(null, textInputLayout);
        return true;
    }
    public static boolean handlerCheckNewIsTheSameAsOld(Context context,TextInputEditText textInputEditTextNew,TextInputEditText textInputEditTextOld,TextInputLayout textInputLayoutNew) {

        if (textInputEditTextNew.getText().toString().equals(textInputEditTextOld.getText().toString())) {
            CheckUtil.setErrorMessage(context.getString(R.string.passwordErrorRepeat), textInputLayoutNew);
            return false;
        }
        CheckUtil.setErrorMessage(null, textInputLayoutNew);
        return true;
    }
    public static boolean handlerCheckBirthdayDate(Context context, TextInputEditText textInputEditText, TextInputLayout textInputLayout) {
        if (!CheckUtil.checkTextEmpty(context,textInputLayout,textInputEditText.getText().toString())) {
            return false;
        }
        CheckUtil.setErrorMessage(null, textInputLayout);
        return true;
    }
    public static boolean handlerCheckTelephone(Context context,TextInputEditText textInputEditText,TextInputLayout textInputLayout) {

        if (!CheckUtil.checkTextEmpty(context,textInputLayout,textInputEditText.getText().toString())) {
            return false;
        } else {
            try {
                PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
                Phonenumber.PhoneNumber number = phoneUtil.parse(textInputEditText.getText().toString(), "ES");

                if (!phoneUtil.isValidNumber(number)) {
                    CheckUtil.setErrorMessage(context.getString(R.string.incorrect_telephone), textInputLayout);
                    return false;
                }
            } catch (NumberParseException e) {
                CheckUtil.setErrorMessage(context.getString(R.string.incorrect_telephone), textInputLayout);
                return false;
            }
        }
        CheckUtil.setErrorMessage(null, textInputLayout);
        return true;
    }

    public static boolean handlerCheckName(Context context,TextInputEditText textInputEditText,TextInputLayout textInputLayout) {
        if (!CheckUtil.checkTextEmpty(context,textInputLayout,textInputEditText.getText().toString())) {
            return false;
        } else if (CheckUtil.checkNumbers(textInputEditText.getText().toString())){
            CheckUtil.setErrorMessage(context.getString(R.string.numberErrorTextField), textInputLayout);
            return false;
        }
        CheckUtil.setErrorMessage(null, textInputLayout);
        return true;
    }
    public static boolean handlerCheckPronouns(Context context,TextInputEditText textInputEditText,TextInputLayout textInputLayout) {
        if (!CheckUtil.checkTextEmpty(context,textInputLayout,textInputEditText.getText().toString())) {
            return false;
        }
        CheckUtil.setErrorMessage(null, textInputLayout);
        return true;
    }
    //Comprobueba si una cadena de texto contiene uno o más caracteres escritos en mayúscula.
    public static boolean checkNumbers(String text){
            return text.matches(".*\\d.*"); // Verifica si hay algún dígito en el texto

    }
    public static boolean checkEmail(Context context,TextInputEditText textInputEditText,TextInputLayout textInputLayout){
        if (!CheckUtil.checkTextEmpty(context,textInputLayout,textInputEditText.getText().toString())) {
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(textInputEditText.getText().toString()).matches()) {
            CheckUtil.setErrorMessage(context.getString(R.string.emailErrorFormat), textInputLayout);
            return false;
        }
        CheckUtil.setErrorMessage(null, textInputLayout);
        return true;
    }
    public static boolean handlerCheckUsernameEmpty(Context context,TextInputEditText textInputEditText,TextInputLayout textInputLayout) {
        if (!CheckUtil.checkTextEmpty(context,textInputLayout,textInputEditText.getText().toString())) {
            return false;
        }
        CheckUtil.setErrorMessage(null, textInputLayout);
        return true;
    }
    public static boolean checkTextEmpty(Context context, TextInputLayout textInputLayout, String text){
            if(text.isEmpty()){
                CheckUtil.setErrorMessage(context.getString(R.string.fieldErrorEmpty), textInputLayout);
                return false;
            }
            return true;
    }

    //Comprobueba si una cadena de texto contiene uno o más caracteres escritos en mayúscula.
    public static boolean checkUppercase(String text){
            return text.chars().anyMatch(Character::isUpperCase);
    }
    public static boolean checkSpecialCharacter(String password){
        return password.matches(".*[\\W_].*");
    }

    //Estable
    public static void setErrorMessage(String error, TextInputLayout textInputLayout) {
        textInputLayout.setError(error);
    }
}
