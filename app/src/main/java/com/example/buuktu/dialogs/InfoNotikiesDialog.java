package com.example.buuktu.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;

import com.example.buuktu.R;
import com.example.buuktu.listeners.OnDialogInfoClickListener;

public class InfoNotikiesDialog extends Dialog implements View.OnClickListener {
    ImageButton ib_close_dialog ;
    private OnDialogInfoClickListener listener;
    public InfoNotikiesDialog(@NonNull Context context) {
        super(context);
    }
    public void setOnDialogClickListener(OnDialogInfoClickListener listener) {
        this.listener = listener;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_notikies_dialog);
        ib_close_dialog = findViewById(R.id.ib_close_dialog);
        ib_close_dialog.setOnClickListener(this);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ib_close_dialog) {
            if (listener != null) {
                listener.onCancel();
            }
            dismiss();
        }
    }
}
