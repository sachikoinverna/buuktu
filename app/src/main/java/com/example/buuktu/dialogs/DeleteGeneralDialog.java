package com.example.buuktu.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.buuktu.R;

public class DeleteGeneralDialog extends Dialog implements View.OnClickListener {
    ImageButton ib_accept_dialog,ib_close_dialog ;
    Context context;
    public interface OnDialogDelClickListener {
        void onAccept();
        void onCancel();
    }
    private OnDialogDelClickListener listener;
    public DeleteGeneralDialog(@NonNull Context context) {
        super(context);
        this.context=context;
    }
    public void setOnDialogClickListener(OnDialogDelClickListener listener) {
        this.listener = listener;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* if (context instanceof Fragment)
        {
        }*/
            setContentView(R.layout.delete_notekie_dialog);
        //   ib_next_incorrect = findViewById(R.id.ib_next_incorrect);
        //ib_home_incorrect = findViewById(R.id.ib_home_incorrect);
        ib_close_dialog = findViewById(R.id.ib_close_dialog);
        ib_accept_dialog = findViewById(R.id.ib_accept_dialog);
        //ib_next_incorrect.setOnClickListener(this);
       // ib_home_incorrect.setOnClickListener(this);
        ib_close_dialog.setOnClickListener(this);
        ib_accept_dialog.setOnClickListener(this);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        //if (!nextLevelUnlocked) {
        //    ib_next_incorrect.setVisibility(View.GONE);
        //}
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ib_close_dialog) {
            if (listener != null) {
                listener.onCancel();
            }
            dismiss();
        } else if (v.getId() == R.id.ib_accept_dialog) {
            if (listener != null) {
                listener.onAccept();
            }
        }
    }
}
