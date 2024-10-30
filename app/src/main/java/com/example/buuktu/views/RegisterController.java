package com.example.buuktu.views;

import static android.os.Build.VERSION_CODES.R;

import android.view.View;

import com.example.buuktu.utils.CheckUtil;

public class RegisterController implements View.OnFocusChangeListener {
    @Override
    public void onFocusChange(View view, boolean b) {
        /*if(view.getId()==){
            if (!b) {
                checkName(et_nameRegister.getText().toString());
            }else if (b){
                //setErrorMessage(null,et_nameRegister);
            }
        } else if (view.getId()==R.id.et_surnameRegister) {
            if (!b) {
                handlerCheckSurname(et_surnameRegister.getText().toString());
            } else if (b) {
                CheckUtil.setErrorMessage(null,et_surnameRegister);
            }

        } else if (view.getId()==R.id.dp_birthday) {

        } else if (view.getId()==R.id.et_userRegister) {

        } else if (view.getId()==R.id.et_pronounsRegister){

        } else if (view.getId()==R.id.et_emailRegister){

        }else if (view.getId()==R.id.et_password) {

        }else if (view.getId()==R.id.et_passwordRepeat) {

        } else if (view.getId()==R.id.et_telephoneRegister) {

        }*/
    }
    private boolean handlerCheckSurname(String surname){
      /*  if(CheckUtil.checkTextEmpty(surname)){
            CheckUtil.setErrorMessage(getString(R.string.surnameErrorEmpty),et_surnameRegister);
            return false;
        }else if (!CheckUtil.checkNumbers(surname)){
            CheckUtil.setErrorMessage(getString(R.string.numberErrorTextField),et_surnameRegister);
            return false;
        }*/
        return true;
    }
    private boolean handlerCheckPassword(String password){
      /*  if (CheckUtil.checkTextEmpty(password)){
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
        CheckUtil.setErrorMessage(null,et_password);*/
        return true;
    }
    private boolean handlerCheckTelephone(){
       /* if(CheckUtil.checkTextEmpty(et_telephoneRegister)){
            CheckUtil.setErrorMessage(getString(R.string.telephoneErrorEmpty),et_telephoneRegister);
            return false;
        }*/
        return true;
    }
    private boolean handlerCheckEmail(String email){
        /*if(CheckUtil.checkTextEmpty(R.id.et_emailRegister)){
            CheckUtil.setErrorMessage(getString(R.string.emailErrorEmpty),et_emailRegister);
            return false;
        }
        if (!CheckUtil.checkEmailStructure(email)){
            CheckUtil.setErrorMessage(getString(R.string.emailErrorFormat),et_emailRegister);
            return false;
        }*/
        return true;
    }
}
