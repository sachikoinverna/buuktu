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
import com.example.buuktu.models.NumberOfTheDay;
import com.example.buuktu.utils.DateUtils;

public class PeriodNumbersDialog extends Dialog implements View.OnClickListener{
    TextView tv_period_number_title,tv_period_number;
    ImageButton ib_close_period_number_dialog;
    private Handler handler;
    private Runnable checkDateRunnable;
    String lastDate;
    public PeriodNumbersDialog(@NonNull Context context) {
        super(context);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.period_numbers_dialog);
        handler = new Handler();
        initComponents();

        ib_close_period_number_dialog.setOnClickListener(this);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        updateNumberOfTheDay(tv_period_number);

        // Configurar el Runnable para chequear el cambio de día
        checkDateRunnable = new Runnable() {
            @Override
            public void run() {
                if (DateUtils.hasDateChanged(lastDate)) {
                    updateNumberOfTheDay(tv_period_number);
                }
                handler.postDelayed(this, 60000); // Revisar cada minuto
            }
        };
        handler.post(checkDateRunnable);
    }
    private void initComponents(){
        tv_period_number = findViewById(R.id.tv_period_number);
        tv_period_number_title = findViewById(R.id.tv_period_number_title);
        ib_close_period_number_dialog = findViewById(R.id.ib_close_period_number_dialog);
    }
    private void updateNumberOfTheDay(TextView textView){
        int number = NumberOfTheDay.obtainNumberOfTheDay(0,300);
        tv_period_number.setText(String.valueOf(number));
        lastDate = DateUtils.getCurrentDate();
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ib_close_period_number_dialog) {
            dismiss();
        }
    }
}
