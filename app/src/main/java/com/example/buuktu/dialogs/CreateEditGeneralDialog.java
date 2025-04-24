package com.example.buuktu.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.buuktu.R;

public class CreateEditGeneralDialog extends Dialog {
    TextView tv_text_create_edit;

    String text;
    public CreateEditGeneralDialog(@NonNull Context context, String text) {
        super(context);
        this.text=text;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.create_edit_general_dialog);
        tv_text_create_edit = findViewById(R.id.tv_text_create_edit);
        tv_text_create_edit.setText(text);

        setCanceledOnTouchOutside(false);
        setCancelable(false);

        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }
}
