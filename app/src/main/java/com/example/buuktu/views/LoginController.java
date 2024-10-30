package com.example.buuktu.views;

import android.view.View;

import com.example.buuktu.R;

public class LoginController implements View.OnFocusChangeListener {
    @Override
    public void onFocusChange(View view, boolean b) {
        if(view.getId()== R.id.et_emailLogin){
            if (!b) {
                //checkEmail(et_emailLogin.getText().toString());
            }else if (b){
                //setErrorMessage(null,et_emailLogin);
            }
        } else if (view.getId()==R.id.et_passwordLogin) {
            
        }
    }
    private void handlerClearFields(){

    }
}
