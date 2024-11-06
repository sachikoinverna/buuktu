package com.example.buuktu.controllers;

import android.content.Intent;
import android.view.View;

import com.example.buuktu.R;
import com.example.buuktu.utils.CheckUtil;
import com.example.buuktu.views.Login;
import com.example.buuktu.views.Register;

public class LoginController implements View.OnClickListener {
    private final Login login;

    public LoginController(Login login) {
        this.login = login;
    }

    private void handlerClearFields(){

    }
    private void handlerGoToRegister(){
        Intent intent = new Intent(login.getApplicationContext(), Register  .class);
        login.startActivity(intent);
    }
    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.bt_loginToRegister) {
            handlerGoToRegister();
        }
    }
}
