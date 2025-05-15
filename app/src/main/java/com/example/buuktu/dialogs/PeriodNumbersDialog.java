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
import com.example.buuktu.models.NumberOfTheDay;
import com.example.buuktu.utils.DateUtils;
import com.example.buuktu.utils.DrawableUtils;

public class PeriodNumbersDialog extends Dialog implements View.OnClickListener{
    TextView tv_period_number;
    ImageButton ib_close_period_number_dialog;
    ImageView iv_period_number;
    private Handler handler = new Handler();
    private Runnable checkDateRunnable;
    String lastDate;
    Context context;
    public PeriodNumbersDialog(@NonNull Context context) {
        super(context);
        this.context=context;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.period_numbers_dialog);
        initComponents();
        customizeImage();
        setListeners();
        setDialogProperties();
        updateNumberOfTheDay();
        checkNewDay();

    }
    private void customizeImage(){
            DrawableUtils.personalizarImagenCuadradoButton(context,12,3,R.color.purple1, iv_period_number.getDrawable(), iv_period_number);
    }
    private void checkNewDay(){
        checkDateRunnable = new Runnable() {
            @Override
            public void run() {
                //Comprueba si la fecha actual es la misma que la ultima registrada.
                if (DateUtils.hasDateChanged(lastDate)) {
                    //
                    updateNumberOfTheDay();
                }
                //Se
                handler.postDelayed(this, 60000);
            }
        };
        handler.post(checkDateRunnable);
    }
    private void setDialogProperties(){
        //El dialogo no se cerrara si el usuario toca fuera de este.
        setCanceledOnTouchOutside(false);
        //El dialogo no se cerrara si el usuario va atras.
        setCancelable(false);
        //El fondo del dialogo es transparente.
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }
    private void setListeners(){
        ib_close_period_number_dialog.setOnClickListener(this);
    }
    private void initComponents(){
        tv_period_number = findViewById(R.id.tv_period_number);
        ib_close_period_number_dialog = findViewById(R.id.ib_close_period_number_dialog);
        iv_period_number = findViewById(R.id.iv_period_number);
    }
    private void updateNumberOfTheDay(){
        // Obtiene el número del día, el cual estará entre 0 y 300.
        int number = NumberOfTheDay.obtainNumberOfTheDay(0,300);
        // Establece el número del día y lo muestra como texto en el TextView.
        tv_period_number.setText(String.valueOf(number));
        //Actualiza la fecha.
        lastDate = DateUtils.getCurrentDate();
    }
    @Override
    public void onClick(View v) {
        // Verifica si el botón pulsado corresponde al de cierre.
        if (v.getId() == R.id.ib_close_period_number_dialog) {
            // Cierra el diálogo actual.
            dismiss();
        }
    }
}
