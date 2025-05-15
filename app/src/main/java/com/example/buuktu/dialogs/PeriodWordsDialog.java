package com.example.buuktu.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.buuktu.R;
import com.example.buuktu.models.WordOfTheDay;
import com.example.buuktu.utils.DateUtils;
import com.example.buuktu.utils.DrawableUtils;

public class PeriodWordsDialog extends Dialog implements View.OnClickListener{
    ImageButton ib_close_period_word_dialog ;
    TextView tv_period_word;
    ImageView iv_period_word;
    private Handler handler = new Handler();
    private Runnable checkDateRunnable;
    String lastDate;
    Context context;
    public PeriodWordsDialog(@NonNull Context context) {
        super(context);
        this.context=context;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.period_words_dialog);
        initComponents();
        customizeImage();
        setListeners();
        setDialogProperties();
        updateWordOfTheDay(tv_period_word);
        checkNewDay();

    }
    private void customizeImage(){
            DrawableUtils.personalizarImagenCuadradoButton(context,12,3,R.color.purple1,iv_period_word.getDrawable(),iv_period_word);

    }
    private void checkNewDay(){
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
    private void setDialogProperties(){
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }
    private void initComponents(){
        tv_period_word = findViewById(R.id.tv_period_word);
        ib_close_period_word_dialog = findViewById(R.id.ib_close_period_word_dialog);
        iv_period_word = findViewById(R.id.iv_period_word);
    }
    private void setListeners(){
        ib_close_period_word_dialog.setOnClickListener(this);
    }
    private void updateWordOfTheDay(TextView wordTextView) {
        WordOfTheDay.obtenerPalabraDelDia(palabra -> {
            wordTextView.setText(palabra);
            lastDate = DateUtils.getCurrentDate();
        });
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ib_close_period_word_dialog) {
            dismiss();
        }
    }
}
