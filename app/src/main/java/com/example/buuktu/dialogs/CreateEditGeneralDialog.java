package com.example.buuktu.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.airbnb.lottie.LottieAnimationView;
import com.example.buuktu.R;

public class CreateEditGeneralDialog extends Dialog {
    private LottieAnimationView animationView;
    public CreateEditGeneralDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.create_edit_general_dialog);
        initComponents();
        setDialogProperties();

    }
    private void initComponents(){
        animationView = findViewById(R.id.anim_create_edit);
    }
    private void setDialogProperties(){
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }
    public LottieAnimationView getAnimationView() {
        return animationView;
    }
}
