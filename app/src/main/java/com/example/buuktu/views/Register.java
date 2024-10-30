package com.example.buuktu.views;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.DatePicker;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.buuktu.R;
import com.example.buuktu.utils.CheckUtil;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {
    public TextInputEditText dp_birthday;
    public TextInputEditText et_nameRegister;
    public TextInputEditText et_surnameRegister;
    public TextInputEditText et_pronounsRegister;
    public TextInputEditText et_userRegister;
    public TextInputEditText et_emailRegister;
    public TextInputEditText et_passwordRepeat;
    public TextInputEditText et_password;
    public TextInputEditText et_telephoneRegister;
    Calendar calendar;
    int yearC;
    int monthC;
    int dayC;
    String errorMailFormat;
    String dateSelected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        dp_birthday = findViewById(R.id.dp_birthday);
        et_nameRegister = findViewById(R.id.et_nameRegister);
        et_surnameRegister = findViewById(R.id.et_surnameRegister);
        et_pronounsRegister = findViewById(R.id.et_pronounsRegister);
        et_userRegister = findViewById(R.id.et_userRegister);
        et_emailRegister = findViewById(R.id.et_emailRegister);
        et_password = findViewById(R.id.et_password);
        et_passwordRepeat = findViewById(R.id.et_passwordRepeat);
        et_telephoneRegister = findViewById(R.id.et_telephoneRegister);
        CheckUtil.setErrorMessage(null,et_nameRegister);
        CheckUtil.setErrorMessage(null,et_surnameRegister);
        CheckUtil.setErrorMessage(null,et_pronounsRegister);
        CheckUtil.setErrorMessage(null,et_emailRegister);
        CheckUtil.setErrorMessage(null,et_password);
        CheckUtil.setErrorMessage(null,et_telephoneRegister);
        CheckUtil.setErrorMessage(  null,dp_birthday);
        //setErrorMessage("",tv_passwordErrorRepeat);
        calendar = Calendar.getInstance();
        yearC = calendar.get(Calendar.YEAR);
        monthC = calendar.get(Calendar.MONTH);
        dayC = calendar.get(Calendar.DAY_OF_MONTH);
    }
    public boolean checkName(String name){
        if(CheckUtil.checkTextEmpty(et_nameRegister)){
            CheckUtil.setErrorMessage(getString(R.string.nameErrorEmpty),et_nameRegister);
            return false;
        }else if (!CheckUtil.checkNumbers(name)){
             CheckUtil.setErrorMessage(getString(R.string.numberErrorTextField),et_nameRegister);
            return false;
        }
        return true;
    }
    public boolean checkSurname(String surname){
        if(CheckUtil.checkTextEmpty(et_surnameRegister)){
            CheckUtil.setErrorMessage(getString(R.string.surnameErrorEmpty),et_surnameRegister);
            return false;
        }else if (!CheckUtil.checkNumbers(surname)){
            CheckUtil.setErrorMessage(getString(R.string.numberErrorTextField),et_surnameRegister);
            return false;
        }
        return true;
    }

    public boolean checkEmailStructure(String email){
        try {
            Pattern pattern = Patterns.EMAIL_ADDRESS;
            return pattern.matcher(email).matches();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public boolean checkPronouns(String pronouns){
        if(CheckUtil.checkTextEmpty(et_pronounsRegister)){
            CheckUtil.setErrorMessage(getString(R.string.pronounsErrorEmpty),et_pronounsRegister);
            return false;
        }else if (!CheckUtil.checkNumbers(pronouns)){
            //setErrorMessage(getString(R.string.numberErrorTextField),);
            return false;
        }
        return true;
    }
    public boolean checkEmail(String email){
        if(CheckUtil.checkTextEmpty(et_emailRegister)){
            CheckUtil.setErrorMessage(getString(R.string.emailErrorEmpty),et_emailRegister);
            return false;
        }
        if (!checkEmailStructure(email)){
                CheckUtil.setErrorMessage(getString(R.string.emailErrorFormat),et_emailRegister);
                return false;
        }
        return true;
    }

    public TextInputEditText getDp_birthday() {
        return dp_birthday;
    }
    public TextInputEditText getEt_nameRegister() {
        return et_nameRegister;
    }
    public TextInputEditText getEt_surnameRegister() {
        return et_surnameRegister;
    }
        public TextInputEditText getEt_pronounsRegister() {
            return et_pronounsRegister;
        }

    public boolean checkTelephone(){
        if(CheckUtil.checkTextEmpty(et_telephoneRegister)){
            CheckUtil.setErrorMessage(getString(R.string.telephoneErrorEmpty),et_telephoneRegister);
            return false;
        }
        return true;
    }
    public boolean checkPassword(String password){
        if (CheckUtil.checkTextEmpty(et_password)){
            CheckUtil.setErrorMessage(getString(R.string.passwordErrorEmpty),et_password);
        } else if (password.length()<8) {
            CheckUtil.setErrorMessage(getString(R.string.passwordTooShort),et_password);
            return false;
        }else if(!CheckUtil.checkSpecialCharacter(password)){
            CheckUtil.setErrorMessage(getString(R.string.passwordErrorSpecialChar),et_password);
            return false;
        } else if (!CheckUtil.checkUppercase(password)){
            CheckUtil.setErrorMessage(getString(R.string.passwordErrorUppercase),et_password);
            return false;
        }
        CheckUtil.setErrorMessage(null,et_password);
        return true;
    }
    public boolean checkSpecialCharacter(String password){
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (c >= 33 && c <= 46 || c == 64) {
                return true;
            }
        }
        return false;
    }

    public boolean checkPasswordRepeat(String password, String passwordRepeat){
        if (!password.equals(passwordRepeat)) {
            CheckUtil.setErrorMessage(getString(R.string.passwordErrorRepeat),et_passwordRepeat);
        }
        CheckUtil.setErrorMessage(null,et_passwordRepeat);
        return true;
    }
    public boolean checkUser(){
        if(CheckUtil.checkTextEmpty(et_userRegister)){
            CheckUtil.setErrorMessage(getString(R.string.userErrorEmpty),et_userRegister);
            return false;
        }
        return true;
    }
    public boolean checkBirthdayDate(){
        if(CheckUtil.checkTextEmpty(dp_birthday)){
            CheckUtil.setErrorMessage(getString(R.string.birthdayErrorEmpty),dp_birthday);
            return false;
        }
        return true;
    }
    public boolean checkAllFields(){
        if(checkName(et_nameRegister.getText().toString()) && checkSurname(et_surnameRegister.getText().toString()) && checkBirthdayDate() && checkUser() && checkEmail(et_emailRegister.getText().toString()) && checkPassword(et_password.getText().toString())&& checkPasswordRepeat(et_password.getText().toString(),et_passwordRepeat.getText().toString())&& checkPronouns(et_pronounsRegister.getText().toString())){
            return true;
        }
            return false;
    }
    public void register(){
        if(checkAllFields()){

        }else{

        }
    }
    public void showDatePickerDialog(View view)
    {
        DatePickerDialog date = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                dp_birthday.setText(""+day+"/"+(month+1)+"/"+year);
                dayC= day;
                monthC= month;
                yearC= year;
            }
        },yearC,monthC,dayC);
        date.show();
    }
}
