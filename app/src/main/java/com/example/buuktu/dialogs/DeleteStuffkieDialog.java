package com.example.buuktu.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;

import com.example.buuktu.R;
import com.example.buuktu.listeners.OnDialogInfoClickListener;

public class DeleteStuffkieDialog extends Dialog implements View.OnClickListener {
    ImageButton ib_next_incorrect,ib_home_incorrect,ib_close_dialog ;
    private OnDialogInfoClickListener listener;
    public DeleteStuffkieDialog(@NonNull Context context) {
        super(context);
    }
    public void setOnDialogClickListener(OnDialogInfoClickListener listener) {
        this.listener = listener;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_characterkies_dialog);
        //   ib_next_incorrect = findViewById(R.id.ib_next_incorrect);
        //ib_home_incorrect = findViewById(R.id.ib_home_incorrect);
        ib_close_dialog = findViewById(R.id.ib_close_dialog);
        //ib_next_incorrect.setOnClickListener(this);
       // ib_home_incorrect.setOnClickListener(this);
        ib_close_dialog.setOnClickListener(this);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        //if (!nextLevelUnlocked) {
        //    ib_next_incorrect.setVisibility(View.GONE);
        //}
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    @Override
    public void onClick(View v) {
        //  if (v.getId() == R.id.ib_next_incorrect) {
        //   if (listener != null) {
        //       listener.onNext();
        //    }
        //    dismiss();
        if (v.getId() == R.id.ib_close_dialog) {
            if (listener != null) {
                listener.onCancel();
            }
            dismiss();
        }// else if (v.getId() == R.id.ib_retry_incorrect) {
          /*  if (listener != null) {
                listener.onRetry();
            }
            dismiss();
        }*/
    }
}
