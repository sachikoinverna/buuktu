package com.example.buuktu.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;

import com.airbnb.lottie.LottieAnimationView;
import com.example.buuktu.R;
import com.example.buuktu.utils.CheckUtil;
import com.example.buuktu.utils.EfectsUtils;
import com.example.buuktu.views.MainActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class EditPasswordUserDialog extends Dialog implements View.OnClickListener {
    ImageButton ib_accept_dialog,ib_close_dialog ;
    TextInputLayout et_newpasswordRepeatFull,et_newpasswordFull,et_oldpasswordFull;
    TextInputEditText et_oldpassword,et_newPassword,et_newPasswordRepeat;
    MainActivity context;
    LottieAnimationView animationView;
    public EditPasswordUserDialog(@NonNull MainActivity context) {
        super(context);
        this.context = context;

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_password_user_dialog);
        initComponents();
         setListeners();

        setDialogProperties();
    }
    private void initComponents(){
        ib_close_dialog = findViewById(R.id.ib_close_dialog);
        ib_accept_dialog = findViewById(R.id.ib_accept_dialog);
        et_oldpassword =findViewById(R.id.et_oldpassword);
        et_oldpasswordFull = findViewById(R.id.et_oldpasswordFull);

        et_newPassword  = findViewById(R.id.et_newPassword);
        et_newpasswordFull = findViewById(R.id.et_newPasswordFull);

        et_newPasswordRepeat  = findViewById(R.id.et_newPasswordRepeat);
        et_newpasswordRepeatFull = findViewById(R.id.et_newPasswordRepeatFull);
        animationView = findViewById(R.id.anim_edit);
    }
    private void setListeners(){
        ib_accept_dialog.setOnClickListener(this);
        ib_close_dialog.setOnClickListener(this);
    }
    private void setVisibility(boolean loadingMode){
        et_oldpasswordFull.setVisibility(loadingMode?View.GONE:View.VISIBLE);
        et_newpasswordFull.setVisibility(loadingMode?View.GONE:View.VISIBLE);
        et_newpasswordRepeatFull.setVisibility(loadingMode?View.GONE:View.VISIBLE);
        ib_close_dialog.setVisibility(loadingMode?View.GONE:View.VISIBLE);
        ib_accept_dialog.setVisibility(loadingMode?View.GONE:View.VISIBLE);
        animationView.setVisibility(loadingMode?View.VISIBLE:View.GONE);
    }
private void saveNewPassword(){
    setVisibility(true);
    if(CheckUtil.handlerCheckNewIsTheSameAsOld(context,et_newPassword,et_oldpassword,et_newpasswordFull)) {
        if (CheckUtil.handlerCheckPassword(context, et_newPassword, et_newpasswordFull) && CheckUtil.handlerCheckPasswordRepeat(context, et_newPasswordRepeat, et_newPassword, et_newpasswordRepeatFull)) {
                AuthCredential credential = EmailAuthProvider.getCredential(FirebaseAuth.getInstance().getCurrentUser().getEmail(), et_oldpassword.getText().toString());
                Completable.timer(2, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> FirebaseAuth.getInstance().getCurrentUser().reauthenticate(credential).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // Si la contraseña actual es correcta, cambiamos la contraseña
                                FirebaseAuth.getInstance().getCurrentUser().updatePassword(et_newPassword.getText().toString()).addOnCompleteListener(updateTask -> {
                                    if (updateTask.isSuccessful()) {
                                        successfail("success");
                                    } else {
                                        successfail("fail");
                                    }
                                });
                            } else {
                                EfectsUtils.setAnimationsDialog("fail", animationView);
                                setVisibility(false);
                                et_oldpasswordFull.setError(context.getString(R.string.actual_password_incorrect));
                            }
                        }));
        }
    }
    }
    private void successfail(String mode){
        EfectsUtils.setAnimationsDialog(mode, animationView);
        delayedDismiss();
    }
    private void delayedDismiss() {
        Completable.timer(5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::dismiss);
    }
    private void setDialogProperties(){
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ib_close_dialog) {
            dismiss();
        }else if (v.getId() == R.id.ib_accept_dialog) {
            saveNewPassword();
        }
    }
}
