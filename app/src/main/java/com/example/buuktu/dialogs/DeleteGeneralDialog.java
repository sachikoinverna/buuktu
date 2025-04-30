package com.example.buuktu.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.buuktu.R;

public class DeleteGeneralDialog extends Dialog implements View.OnClickListener {
    ImageButton ib_accept_dialog,ib_close_dialog ;
    Context context;
    String mode;
    TextView tv_title_del, tv_text_del;
    ImageView iv_photo_del;
    static String modeNotekie = "notekie";
    static String modeStuffkie = "stuffkie";
    static String modeWorldkie = "worldkie";
    static String modeCharacterkie = "characterkie";
    public interface OnDialogDelClickListener {
        void onAccept();
        void onCancel();
    }
    private OnDialogDelClickListener listener;
    public DeleteGeneralDialog(@NonNull Context context, String mode) {
        super(context);
        this.context=context;
        this.mode=mode;
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
        setContentView(R.layout.delete_general_dialog);
        ib_close_dialog = findViewById(R.id.ib_close_dialog);
        ib_accept_dialog = findViewById(R.id.ib_accept_dialog);
        iv_photo_del = findViewById(R.id.iv_photo_del);
        tv_text_del = findViewById(R.id.tv_text_del);

        //tv_text_del = findViewById(R.id.tv);
        tv_title_del = findViewById(R.id.tv_title_del);
        tv_title_del.setText("Cuidado");
        if(mode.equals(modeNotekie)){
            iv_photo_del.setImageResource(R.mipmap.img_del_notes);
            tv_text_del.setText("¿Deseas eliminar la nota? No podras recuperarla.");
        } else if (mode.equals(modeStuffkie)) {
            iv_photo_del.setImageResource(R.mipmap.img_del_stuffkie);
            tv_text_del.setText("¿Deseas eliminar el stuffkie? No podras recuperarla.");
        } else if (mode.equals(modeWorldkie)) {
            iv_photo_del.setImageResource(R.mipmap.img_del_worldkie);
            tv_text_del.setText("¿Deseas eliminar el worldkie? No podras recuperarla.");
        } else if (mode.equals(modeCharacterkie)) {
            iv_photo_del.setImageResource(R.mipmap.img_del_characterkie);
            tv_text_del.setText("¿Deseas eliminar el characterkie? No podras recuperarla.");
        }
        //tv_text_del = findViewById(R.id.tv);
        ib_close_dialog.setOnClickListener(this);
        ib_accept_dialog.setOnClickListener(this);
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
        } else if (v.getId() == R.id.ib_accept_dialog) {
            if (listener != null) {
                listener.onAccept();
            }
        }
    }
}
