package com.example.buuktu.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.airbnb.lottie.LottieAnimationView;
import com.example.buuktu.R;
import com.example.buuktu.utils.CheckUtil;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class EditPasswordUserDialog extends Dialog implements View.OnClickListener {
    ImageButton ib_accept_dialog,ib_close_dialog ;
    TextView tv_title;
    TextInputLayout et_newpasswordRepeatFull,et_newpasswordFull,et_oldpasswordFull;
    TextInputEditText et_oldpassword,et_newPassword,et_newPasswordRepeat;
    Context context;
    public EditPasswordUserDialog(@NonNull Context context) {
        super(context);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_password_user_dialog);
        context = getContext();
        ib_close_dialog = findViewById(R.id.ib_close_dialog);
        ib_accept_dialog = findViewById(R.id.ib_accept_dialog);
        tv_title = findViewById(R.id.tv_edit_title);
         et_oldpassword =findViewById(R.id.et_oldpassword);
        et_oldpasswordFull = findViewById(R.id.et_oldpasswordFull);

        et_newPassword  = findViewById(R.id.et_newPassword);
         et_newpasswordFull = findViewById(R.id.et_newPasswordFull);

        et_newPasswordRepeat  = findViewById(R.id.et_newPasswordRepeat);
         et_newpasswordRepeatFull = findViewById(R.id.et_newPasswordRepeatFull);
        ib_accept_dialog.setOnClickListener(this);
        ib_close_dialog.setOnClickListener(this);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }
private void saveNewPassword(){
    tv_title.setText("Cuidado");

    LottieAnimationView animationView = findViewById(R.id.anim_edit);

    tv_title.setVisibility(View.GONE);

    ib_close_dialog.setVisibility(View.GONE);
    ib_accept_dialog.setVisibility(View.GONE);
    animationView.setAnimation(R.raw.reading_anim);
    animationView.playAnimation();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String newPassword = et_newPassword.getText().toString();
    String newPasswordRepeat = et_newPasswordRepeat.getText().toString();
    if(CheckUtil.handlerCheckNewIsTheSameAsOld(context,et_newPassword,et_oldpassword,et_newpasswordFull)) {
        if (CheckUtil.handlerCheckPassword(context, et_newPassword, et_newpasswordFull) && CheckUtil.handlerCheckPasswordRepeat(context, et_newPasswordRepeat, et_newPassword, et_newpasswordRepeatFull)) {
            if (newPassword.equals(newPasswordRepeat)) {
                String email = user.getEmail();

                String oldPassword = et_oldpassword.getText().toString();
                AuthCredential credential = EmailAuthProvider.getCredential(email, oldPassword);

                user.reauthenticate(credential).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Si la contraseña actual es correcta, cambiamos la contraseña
                        user.updatePassword(newPassword).addOnCompleteListener(updateTask -> {
                            if (updateTask.isSuccessful()) {
                                animationView.setAnimation(R.raw.success_anim);
                                animationView.playAnimation();
                                Completable.timer(5, TimeUnit.SECONDS)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(() -> {
                                            animationView.setVisibility(View.GONE);
                                            dismiss();
                                        });
                            } else {
                                animationView.setAnimation(R.raw.fail_anim);
                                animationView.playAnimation();
                                Completable.timer(5, TimeUnit.SECONDS)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(() -> {
                                            animationView.setVisibility(View.GONE);
                                            dismiss();
                                        });
                            }
                        });
                    } else {
                        animationView.setAnimation(R.raw.fail_anim);
                        animationView.playAnimation();
                        Completable.timer(5, TimeUnit.SECONDS)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(() -> {
                                    animationView.setVisibility(View.GONE);
                                    dismiss();
                                });
                        et_oldpasswordFull.setError("Contraseña actual incorrecta");
                    }
                });

            }
        }
    }
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
