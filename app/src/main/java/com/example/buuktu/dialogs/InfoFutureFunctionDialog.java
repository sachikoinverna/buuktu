package com.example.buuktu.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;

import com.example.buuktu.R;

public class InfoFutureFunctionDialog extends Dialog implements View.OnClickListener {
    ImageButton ib_next_incorrect,ib_home_incorrect,ib_close_dialog ;
    public InfoFutureFunctionDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.future_function_dialog);
        ib_close_dialog = findViewById(R.id.ib_close_dialog);

        ib_close_dialog.setOnClickListener(this);
        setCanceledOnTouchOutside(false);
        setCancelable(false);

        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ib_close_dialog) {
            dismiss();
        }
    }
}
