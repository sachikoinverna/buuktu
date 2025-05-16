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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;

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
    String name,pronouns,email;
    LottieAnimationView animationViewCreateEdit;
    public EditNamePronounsUserDialog(@NonNull Context context, String type,String value,DocumentReference documentReference) {
        super(context);
        this.context=context;
        this.type=type;
        this.value=value;
        this.documentReference=documentReference;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_edittext_user_dialog);
        initComponents();

        setListeners();
        name = context.getString(R.string.name);
        pronouns = context.getString(R.string.pronouns);
        if(type.equals(name)){
                et_namepronouns.setInputType(InputType.TYPE_CLASS_TEXT);
                et_namepronounsFull.setStartIconDrawable(R.drawable.twotone_add_circle_24);}
        else if(type.equals(pronouns)) {
            et_namepronouns.setInputType(InputType.TYPE_CLASS_TEXT);
            et_namepronounsFull.setStartIconDrawable(R.drawable.twotone_add_circle_24);
        }
        et_namepronounsFull.setHint(type);
        et_namepronouns.setText(value);
        setDialogProperties();
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
        }
    }
    private void changeVisibility(){
        animationViewCreateEdit.setVisibility(View.VISIBLE);
        et_namepronounsFull.setVisibility(View.GONE);
        ib_accept_dialog.setVisibility(View.GONE);
        ib_close_dialog.setVisibility(View.GONE);
    }
    private void saveName(){
        if(CheckUtil.handlerCheckName(context,et_namepronouns,et_namepronounsFull)) {
            changeVisibility();
            Completable.timer(2, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> documentReference.update("name", et_namepronouns.getText().toString()).addOnCompleteListener(updateTask -> {
                        if (updateTask.isSuccessful()) {
                                successFail("success");
                        } else {
                            successFail("fail");
                        }
                    }));
        }
    }
    private void successFail(String mode){
        EfectsUtils.setAnimationsDialog(mode,animationViewCreateEdit);
        delayedDismiss();
    }
    private void delayedDismiss() {
        Completable.timer(5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::dismiss);
    }
    private void savePronouns(){
        if(CheckUtil.handlerCheckPronouns(context,et_namepronouns,et_namepronounsFull)) {
            changeVisibility();
            Completable.timer(3, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> documentReference.update("pronouns", et_namepronouns.getText().toString()).addOnCompleteListener(updateTask -> {
                        if (updateTask.isSuccessful()) {
                            successFail("success");
                        } else {
                            successFail("fail");
                        }
                    })
                    );
        }
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
            saveInfo();
        }
    }
}
