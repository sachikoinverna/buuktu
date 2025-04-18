package com.example.buuktu.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.buuktu.R;

public class CreateEditGeneralDialog extends Dialog {
    ImageButton ib_accept_dialog,ib_close_dialog ;
    Context context;
    String mode;
    TextView tv_text_create_edit;
    static String modeNotekie = "notekie";
    static String modeStuffkie = "stuffkie";
    static String modeWorldkie = "worldkie";
    static String modeCharacterkie = "characterkie";
    static boolean create;
    public CreateEditGeneralDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* if (context instanceof Fragment)
        {
        }*/
        setContentView(R.layout.create_edit_general_dialog);
        tv_text_create_edit = findViewById(R.id.tv_text_create_edit);
        //ib_close_dialog = findViewById(R.id.ib_close_dialog);
        //ib_accept_dialog = findViewById(R.id.ib_accept_dialog);
        //tv_text_del = findViewById(R.id.tv);
        /*if(mode.equals(modeNotekie)){

        } else if (mode.equals(modeStuffkie)) {

        } else if (mode.equals(modeWorldkie)) {

        } else if (mode.equals(modeCharacterkie)) {

        }*/
        //tv_text_del = findViewById(R.id.tv);
        //ib_close_dialog.setOnClickListener(this);
       // ib_accept_dialog.setOnClickListener(this);
        setCanceledOnTouchOutside(false);
        setCancelable(false);

        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }
}
