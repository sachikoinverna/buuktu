package com.example.buuktu.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.location.GnssAntennaInfo;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.buuktu.R;
import com.example.buuktu.listeners.OnDialogPeriodNumbersListener;
import com.example.buuktu.listeners.OnDialogPeriodWordsListener;

import java.util.List;

public class PeriodNumbersDialog extends Dialog implements View.OnClickListener{
    TextView tv_period_number_title,tv_period_number;
    ImageButton ib_close_period_number_dialog;
    OnDialogPeriodNumbersListener listener;
    public PeriodNumbersDialog(@NonNull Context context) {
        super(context);
    }
    public void onDialogPeriodWordsListener(@NonNull OnDialogPeriodNumbersListener listener){
        this.listener = listener;
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ib_close_period_word_dialog) {
            if (listener != null) {
                listener.onCancel();
            }
            dismiss();
        }    }
}
