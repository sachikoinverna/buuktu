package com.example.buuktu.controllers;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.buuktu.R;
import com.example.buuktu.models.UserModel;
import com.example.buuktu.utils.CheckUtil;
import com.example.buuktu.views.Login;
import com.example.buuktu.views.Register;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.Instant;
import java.util.Calendar;
import java.sql.Date;

public class RegisterController implements View.OnFocusChangeListener, View.OnClickListener {
    Calendar calendar;
    int yearC;
    int monthC;
    int dayC;
    private final Register register;
    private FirebaseAuth auth;
    FirebaseFirestore dbFire;
    private FirebaseFirestore db;

    public RegisterController(Register register) {
        this.register = register;
        calendar = Calendar.getInstance();
        yearC = calendar.get(Calendar.YEAR);
        monthC = calendar.get(Calendar.MONTH);
        dayC = calendar.get(Calendar.DAY_OF_MONTH);
     //   FirebaseApp.initializeApp(register);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }
    @Override
    public void onFocusChange(View view, boolean b) {
        if(view.getId()== R.id.et_nameRegister){
            if (!b) {
                handlerCheckName();
            }else if (b){
                CheckUtil.setErrorMessage(null,register.getTv_nameRegister());
            }
        } else if (view.getId()==R.id.et_surnameRegister) {
            if (!b) {
                handlerCheckSurname();
            } else if (b) {
                CheckUtil.setErrorMessage(null,register.getTv_surnameRegister());
            }

        }else if (view.getId()==R.id.dp_birthday) {
            if (!b) {
                handlerCheckBirthdayDate();
            } else if (b) {
                CheckUtil.setErrorMessage(null,register.getTv_birthdayRegister());

            }
        } else if (view.getId()==R.id.et_userRegister) {
            if(!b){
                handlerCheckUser();
            } else if (b) {
                CheckUtil.setErrorMessage(null,register.getTv_usernameRegister());
            }
        } else if (view.getId()==R.id.et_pronounsRegister){
            if(!b){
                handlerCheckPronouns();
            } else if (b) {
                CheckUtil.setErrorMessage(null,register.getTv_pronounsRegister());
            }
        } else if (view.getId()==R.id.et_emailRegister){
            if(!b){
                handlerCheckEmail();
            } else if (b) {
                CheckUtil.setErrorMessage(null,register.getTv_emailRegister());
            }
        }
        else if (view.getId()==R.id.et_password) {
            if (!b) {
                handlerCheckPassword();
            } else if (b) {
                CheckUtil.setErrorMessage(null,register.getTv_passwordRegister());
            }
        }else if (view.getId()==R.id.et_passwordRepeat) {
            if(!b){
                handlerCheckPasswordRepeat();
            } else if (b) {
                CheckUtil.setErrorMessage(null,register.getTv_passwordRepeatRegister());
            }
        } else if (view.getId()==R.id.et_telephoneRegister) {
            if(!b){
                handlerCheckTelephone();
            } else if (b) {
                CheckUtil.setErrorMessage(null,register.getTv_telephoneRegister());
            }
        }
    }
    private boolean handlerCheckName(){
        if(CheckUtil.checkTextEmpty(register.getEt_nameRegister())){
            CheckUtil.setErrorMessage(register.getString(R.string.nameErrorEmpty),register.getTv_nameRegister());
            return false;
        }else if (!CheckUtil.checkNumbers(register.getEt_nameRegister().getText().toString())){
            CheckUtil.setErrorMessage(register.getString(R.string.numberErrorTextField),register.getTv_nameRegister());
            return false;
        }
        return true;
    }
    private boolean handlerCheckPronouns(){
        if(CheckUtil.checkTextEmpty(register.getEt_pronounsRegister())){
            CheckUtil.setErrorMessage(register.getString(R.string.pronounsErrorEmpty),register.getTv_pronounsRegister());
            return false;
        }
        return true;
    }
    private boolean handlerCheckSurname(){
        if(CheckUtil.checkTextEmpty(register.getEt_surnameRegister())){
            CheckUtil.setErrorMessage(register.getString(R.string.surnameErrorEmpty),register.getTv_surnameRegister());
            return false;
        }else if (!CheckUtil.checkNumbers(register.getEt_surnameRegister().getText().toString())){
            CheckUtil.setErrorMessage(register.getString(R.string.numberErrorTextField),register.getTv_surnameRegister());
            return false;
        }
        return true;
    }
    private boolean handlerCheckPassword(){
        if (CheckUtil.checkTextEmpty(register.getEt_passwordRegister())){
            CheckUtil.setErrorMessage(register.getString(R.string.passwordErrorEmpty),register.getTv_passwordRegister());
        } else if (register.getEt_passwordRegister().getText().toString().length()<8) {
            CheckUtil.setErrorMessage(register.getString(R.string.passwordTooShort),register.getTv_passwordRegister());
            return false;
        }else if(!CheckUtil.checkSpecialCharacter(register.getEt_passwordRegister().getText().toString())){
            CheckUtil.setErrorMessage(register.getString(R.string.passwordErrorSpecialChar),register.getTv_passwordRegister());
            return false;
        } else if (!CheckUtil.checkUppercase(register.getEt_passwordRegister().getText().toString())){
            CheckUtil.setErrorMessage(register.getString(R.string.passwordErrorUppercase),register.getTv_passwordRegister());
            return false;
        }
        CheckUtil.setErrorMessage(null,register.getTv_passwordRegister());
        return true;
    }
    public boolean handlerCheckPasswordRepeat(){
        if (!register.getEt_passwordRegister().equals(register.getEt_passwordRepeat())) {
            CheckUtil.setErrorMessage(register.getString(R.string.passwordErrorRepeat),register.getTv_passwordRepeatRegister());
        }
        CheckUtil.setErrorMessage(null,register.getTv_passwordRepeatRegister());
        return true;
    }
    private boolean handlerCheckTelephone(){
       if(CheckUtil.checkTextEmpty(register.getEt_telephoneRegister())){
            CheckUtil.setErrorMessage(register.getString(R.string.telephoneErrorEmpty),register.getEt_telephoneRegister());
            return false;
        }
        return true;
    }
    private boolean handlerCheckEmail(){
        if(CheckUtil.checkTextEmpty(register.getEt_emailRegister())){
            CheckUtil.setErrorMessage(register.getString(R.string.emailErrorEmpty),register.getTv_emailRegister());
            return false;
        }
        if (!CheckUtil.checkEmailStructure(register.getEt_emailRegister().toString())){
            CheckUtil.setErrorMessage(register.getString(R.string.emailErrorFormat),register.getTv_emailRegister());
            return false;
        }
        return true;
    }
    public boolean handlerCheckUser(){
        if(CheckUtil.checkTextEmpty(register.getEt_userRegister())){
            CheckUtil.setErrorMessage(register.getString(R.string.userErrorEmpty),register.getTv_usernameRegister());
            return false;
        }
        return true;
    }
    private boolean handlerCheckBirthdayDate(){
        if(CheckUtil.checkTextEmpty(register.getDp_birthday())){
            CheckUtil.setErrorMessage(register.getString(R.string.birthdayErrorEmpty),register.getDp_birthday());
            return false;
        }
        return true;
    }
    private void handlerGoToRegister(){
        Intent intent = new Intent(register.getApplicationContext(),Login.class);
        register.startActivity(intent);
    }
    private boolean checkAllFields(){
        if(handlerCheckName() && handlerCheckSurname() && handlerCheckBirthdayDate() && handlerCheckUser() && handlerCheckEmail() && handlerCheckPassword()&& handlerCheckPasswordRepeat()&& handlerCheckPronouns()){
            return true;
        }
        return false;
    }
    private void register(){
        if(checkAllFields()){

        }else{

        }
    }
    private void showDatePickerDialog()
    {
        DatePickerDialog date = new DatePickerDialog(register, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                register.getDp_birthday().setText(""+day+"/"+(month+1)+"/"+year);
                dayC= day;
                monthC= month;
                yearC= year;
            }
        },yearC,monthC,dayC);
        date.show();
    }
    @Override
    public void onClick(View view) {
            if(view.getId()==R.id.bt_registerToLogin) {
                handlerGoToRegister();
            } else if (view.getId()==R.id.dp_birthday) {
                showDatePickerDialog();
            } else if (view.getId()==R.id.bt_register) {
                addDataToFirestore();
            }
    }
    private void addDataToFirestore() {

        // creating a collection reference
        // for our Firebase Firetore database.
     /*   if(checkAllFields()) {
            auth.createUserWithEmailAndPassword(register.getEt_emailRegister().getText().toString(), register.getEt_passwordRegister().getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                Toast.makeText(register, "Signup Successful", Toast.LENGTH_SHORT).show();
                                CollectionReference dbUsers = db.collection("Users");

                                // adding our data to our courses object class.
                                UserModel user = new UserModel(register.getEt_emailRegister().getText().toString(),task.getResult().getUser().getUid(), register.getEt_nameRegister().getText().toString(), register.getEt_surnameRegister().getText().toString(), register.getEt_pronounsRegister().getText().toString(), Date.valueOf(register.getDp_birthday().getText().toString()), register.getEt_userRegister().getText().toString(), register.getEt_telephoneRegister().getText().toString());

                                // below method is use to add data to Firebase Firestore.
                                DocumentReference documentRef = dbUsers.document();

                                //.document(uid)
                                documentRef.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(register, "Your Course has been added to Firebase Firestore", Toast.LENGTH_SHORT).show();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // this method is called when the data addition process is failed.
                                        // displaying a toast message when data addition is failed.
                                        Toast.makeText(register, "Fail to add course \n" + e, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                switch (task.getException().getMessage()) {
                                    case "auth/email-already-in-use":
                                        Toast.makeText(register, "Ya existe una cuenta con el correo electronico", Toast.LENGTH_LONG).show();
                                        break;
                                    default:
                                        break;
                                }
                                Toast.makeText(register, "Signup Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });*/
       // }
    }
}
