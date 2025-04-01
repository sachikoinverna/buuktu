package com.example.buuktu.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.buuktu.R;
import com.example.buuktu.listeners.OnDialogInfoClickListener;
import com.example.buuktu.listeners.OnDialogPeriodWordsListener;
import com.example.buuktu.models.WordOfTheDay;
import com.example.buuktu.utils.DateUtils;

import java.time.LocalDate;
import java.util.Date;

public class PeriodWordsDialog extends Dialog implements View.OnClickListener{
    ImageButton ib_close_period_word_dialog ;
    TextView tv_period_word_title, tv_period_word;
    private OnDialogPeriodWordsListener listener;
    private Handler handler;
    private Runnable checkDateRunnable;
    String lastDate;
    public PeriodWordsDialog(@NonNull Context context) {
        super(context);
    }
    public void setOnDialogClickListener(OnDialogPeriodWordsListener listener) {
        this.listener = listener;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.period_words_dialog);
        handler = new Handler();
        tv_period_word = findViewById(R.id.tv_period_word);
        tv_period_word_title = findViewById(R.id.tv_period_word_title);
        //   ib_next_incorrect = findViewById(R.id.ib_next_incorrect);
        //ib_home_incorrect = findViewById(R.id.ib_home_incorrect);
        ib_close_period_word_dialog = findViewById(R.id.ib_close_period_word_dialog);
        //ib_next_incorrect.setOnClickListener(this);
       // ib_home_incorrect.setOnClickListener(this);
        ib_close_period_word_dialog.setOnClickListener(this);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        //if (!nextLevelUnlocked) {
        //    ib_next_incorrect.setVisibility(View.GONE);
        //}

        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        updateWordOfTheDay(tv_period_word);

        // Configurar el Runnable para chequear el cambio de día
        checkDateRunnable = new Runnable() {
            @Override
            public void run() {
                if (DateUtils.hasDateChanged(lastDate)) {
                    updateWordOfTheDay(tv_period_word);
                }
                handler.postDelayed(this, 60000); // Revisar cada minuto
            }
        };
        handler.post(checkDateRunnable);
    }
    private void updateWordOfTheDay(TextView wordTextView) {
        WordOfTheDay.obtenerPalabraDelDia(new WordOfTheDay.FirebaseCallback() {
            @Override
            public void onCallback(String palabra) {
                wordTextView.setText(palabra);
                lastDate = DateUtils.getCurrentDate();
            }
        });
    }


    @Override
    public void onClick(View v) {
        //  if (v.getId() == R.id.ib_next_incorrect) {
        //   if (listener != null) {
        //       listener.onNext();
        //    }
        //    dismiss();
        if (v.getId() == R.id.ib_close_period_word_dialog) {
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
