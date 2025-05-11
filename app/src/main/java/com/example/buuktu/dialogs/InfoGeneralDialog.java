package com.example.buuktu.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;

import com.example.buuktu.R;

public class InfoGeneralDialog extends Dialog implements View.OnClickListener {
    ImageButton ib_close_dialog ;
    static String modeNotekie = "notekie";
    static String modeStuffkie = "stuffkie";
    static String modeWorldkie = "worldkie";
   String mode;
    static String modeCharacterkie = "characterkie";
    public InfoGeneralDialog(@NonNull Context context,String mode)
    {
        super(context);
        this.mode=mode;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(mode.equals("future_function")) {
            setContentView(R.layout.future_function_dialog);
        } else if (mode.equals("search")) {
            setContentView(R.layout.info_search_dialog);
        }else if (mode.equals("inspo")) {
            setContentView(R.layout.info_inspo_dialog);
        } else if(mode.equals("notekies")){
            setContentView(R.layout.info_notekies_dialog);
        } else if (mode.equals("notikies")) {
            setContentView(R.layout.info_notikies_dialog);
        } else if(mode.equals("characterkies")){
            setContentView(R.layout.info_characterkies_dialog);
        } else if(mode.equals("stuffkies")){
            setContentView(R.layout.info_stuffkies_dialog);
        } else if (mode.equals("worldkies")) {
            setContentView(R.layout.info_worldkies_dialog);
        }
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
