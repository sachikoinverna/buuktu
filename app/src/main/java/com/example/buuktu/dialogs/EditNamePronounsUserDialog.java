package com.example.buuktu.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;

import com.airbnb.lottie.LottieAnimationView;
import com.example.buuktu.R;
import com.example.buuktu.utils.CheckUtil;
import com.example.buuktu.utils.EfectsUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class EditNamePronounsUserDialog extends Dialog implements View.OnClickListener {
    ImageButton ib_accept_dialog,ib_close_dialog ;
    TextInputLayout et_namepronounsFull;
    TextInputEditText et_namepronouns;
    final String type;
    final String value;
    final DocumentReference documentReference;
    Context context;
    String lastName="", lastPronouns="",lastEmail="",name,pronouns,email;
    LottieAnimationView animationViewCreateEdit;
    public EditNamePronounsUserDialog(@NonNull Context context, String type,String value,DocumentReference documentReference) {
        super(context);
        this.type=type;
        this.value=value;
        this.documentReference=documentReference;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_edittext_user_dialog);
        context = getContext();
        initComponents();

        setListeners();
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        String name = context.getResources().getString(R.string.name);
        String pronouns = context.getResources().getString(R.string.pronouns);
        String email = context.getResources().getString(R.string.email);
        if(type.equals(name)){
                et_namepronounsFull.setHint(name);
                et_namepronouns.setInputType(InputType.TYPE_CLASS_TEXT);
                et_namepronounsFull.setStartIconDrawable(R.drawable.twotone_add_circle_24);}
        else if(type.equals(pronouns)) {
            et_namepronounsFull.setHint(pronouns);
            et_namepronouns.setInputType(InputType.TYPE_CLASS_TEXT);
            et_namepronounsFull.setStartIconDrawable(R.drawable.twotone_add_circle_24);
        } else if (type.equals(email)) {
                et_namepronounsFull.setHint(email);
                et_namepronouns.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                et_namepronounsFull.setStartIconDrawable(R.drawable.twotone_email_24);
        }
        et_namepronouns.setText(value);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }
    private void initComponents(){
        et_namepronounsFull = findViewById(R.id.et_namepronounsFull);
        et_namepronouns = findViewById(R.id.et_namepronouns);
        ib_accept_dialog = findViewById(R.id.ib_accept_dialog);
        ib_close_dialog = findViewById(R.id.ib_close_dialog);
        animationViewCreateEdit = findViewById(R.id.anim_edit);
    }
    private void setListeners(){
        ib_close_dialog.setOnClickListener(this);
        ib_accept_dialog.setOnClickListener(this);
    }
    private void saveInfo(){
        if(type.equals(name)) {
                saveName();
        } else if (type.equals(pronouns)) {
            savePronouns();
        } else if (type.equals(email)) {
                saveEmail();
        }
    }
    private void saveName(){
        if(CheckUtil.handlerCheckName(context,et_namepronouns,et_namepronounsFull)) {
            EfectsUtils.setAnimationsDialog("start",animationViewCreateEdit);

            Completable.timer(2, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                        Map<String, Object> worldkieData = new HashMap<>();
                        worldkieData.put("name", et_namepronouns.getText().toString());
                        documentReference.update(worldkieData).addOnCompleteListener(updateTask -> {
                            if (updateTask.isSuccessful()) {
                                EfectsUtils.setAnimationsDialog("success",animationViewCreateEdit);
                                delayedDismiss();
                            } else {
                                EfectsUtils.setAnimationsDialog("fail",animationViewCreateEdit);
                                delayedDismiss();
                            }
                        });
                    });
        }
    }
    private void delayedDismiss() {
        Completable.timer(5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::dismiss);
    }
    private void saveEmail(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String oldEmail = user.getEmail();
        String newEmail = et_namepronouns.getText().toString();
        if (!CheckUtil.handlerCheckNewIsTheSameAsOld(context,et_namepronouns,oldEmail,et_namepronounsFull)) {
            EfectsUtils.setAnimationsDialog("start",animationViewCreateEdit);
            Completable.timer(2, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> user.updateEmail(newEmail).addOnCompleteListener(updateTask -> {
                        if (updateTask.isSuccessful()) {
                            EfectsUtils.setAnimationsDialog("success",animationViewCreateEdit);
                            delayedDismiss();
                        } else {
                            EfectsUtils.setAnimationsDialog("fail",animationViewCreateEdit);
                            delayedDismiss();
                        }
                    })
                    );
        }
    }
    private void savePronouns(){
        String newPronouns = et_namepronouns.getText().toString();
        if(CheckUtil.handlerCheckPronouns(context,et_namepronouns,et_namepronounsFull)) {
            EfectsUtils.setAnimationsDialog("start",animationViewCreateEdit);

            Completable.timer(3, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                                Map<String, Object> worldkieData = new HashMap<>();
                                worldkieData.put("pronouns", newPronouns);
                                documentReference.update(worldkieData).addOnCompleteListener(updateTask -> {
                                    if (updateTask.isSuccessful()) {
                                        EfectsUtils.setAnimationsDialog("success",animationViewCreateEdit);

                                        delayedDismiss();
                                    } else {
                                        EfectsUtils.setAnimationsDialog("fail",animationViewCreateEdit);

                                        delayedDismiss();
                                    }
                                });
                            }
                    );
        }
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ib_close_dialog) {
            dismiss();
        }else if (v.getId() == R.id.ib_accept_dialog) {
            saveInfo();
        }
    }
}
