package com.example.buuktu.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.InputType;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;

import com.example.buuktu.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class EditNamePronounsUserDialog extends Dialog implements View.OnClickListener {
    ImageButton ib_accept_dialog,ib_close_dialog ;
    TextInputLayout et_namepronounsFull;
    TextInputEditText et_namepronouns;
    String type;
    OnDialogEditClickListener listener;
    public interface OnDialogEditClickListener {
        void onAccept();
        void onCancel();
    }
    public EditNamePronounsUserDialog(@NonNull Context context, String type) {
        super(context);
        this.type=type;
    }
    public void setOnDialogClickListener(OnDialogEditClickListener listener) {
        this.listener = listener;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_edittext_user_dialog);
        et_namepronounsFull = findViewById(R.id.et_namepronounsFull);
        et_namepronouns = findViewById(R.id.et_namepronouns);
        ib_accept_dialog = findViewById(R.id.ib_accept_dialog);
        ib_close_dialog = findViewById(R.id.ib_close_dialog);
        ib_close_dialog.setOnClickListener(this);
        ib_accept_dialog.setOnClickListener(this);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        if(type.equals("Nombre")){
            et_namepronounsFull.setHint("Nombre");
            et_namepronouns.setInputType(InputType.TYPE_CLASS_TEXT);
            et_namepronounsFull.setStartIconDrawable(R.drawable.twotone_add_circle_24);
        } else if (type.equals("Pronombres")) {
            et_namepronounsFull.setHint("Pronombres");
            et_namepronouns.setInputType(InputType.TYPE_CLASS_TEXT);
            et_namepronounsFull.setStartIconDrawable(R.drawable.twotone_add_circle_24);
        } else if (type.equals("Correo electronico")) {
            et_namepronounsFull.setHint("Email");
            et_namepronouns.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            et_namepronounsFull.setStartIconDrawable(R.drawable.twotone_email_24);

        }
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ib_close_dialog) {
            if (listener != null) {
                listener.onCancel();
            }
            dismiss();
        }else if (v.getId() == R.id.ib_accept_dialog) {
            if (listener != null) {
                listener.onAccept();
            }
            dismiss();
        }
    }
}
