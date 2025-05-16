package com.example.buuktu.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.buuktu.R;
import com.example.buuktu.utils.DrawableUtils;
import com.example.buuktu.views.MainActivity;

public class InfoGeneralDialog extends Dialog implements View.OnClickListener {
    ImageButton ib_close_dialog ;
   String mode;
   ImageView iv_info_dialog;
   MainActivity context;
    public InfoGeneralDialog(@NonNull MainActivity context, String mode)
    {
        super(context);
        this.context=context;
        this.mode=mode;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initComponents();
        customizeImage();
        setListeners();
        setDialogProperties();
    }
private void initComponents(){
    switch (mode) {
        case "future_function":
            setContentView(R.layout.future_function_dialog);
            break;
        case "search":
            setContentView(R.layout.info_search_dialog);
            break;
        case "inspo":
            setContentView(R.layout.info_inspo_dialog);
            break;
        case "notekies":
            setContentView(R.layout.info_notekies_dialog);
            break;
        case "notikies":
            setContentView(R.layout.info_notikies_dialog);
            break;
        case "characterkies":
            setContentView(R.layout.info_characterkies_dialog);
            break;
        case "stuffkies":
            setContentView(R.layout.info_stuffkies_dialog);
            break;
        case "worldkies":
            setContentView(R.layout.info_worldkies_dialog);
            break;
        case "settings":
            setContentView(R.layout.info_settings);
            break;
        case "scenariokies":
            setContentView(R.layout.info_scenariokies_dialog);
            break;
        case "profile":
            setContentView(R.layout.info_profile);
            break;
        case "challenges":
            setContentView(R.layout.info_desafios_dialog);
            break;
    }
    ib_close_dialog = findViewById(R.id.ib_close_dialog);
    iv_info_dialog = findViewById(R.id.iv_info_dialog);

}
    private void customizeImage(){
        DrawableUtils.personalizarImagenCuadradoButton(context,30,7,R.color.purple1, iv_info_dialog.getDrawable(), iv_info_dialog);
    }
private void setListeners(){
    ib_close_dialog.setOnClickListener(this);
}
private void setDialogProperties(){
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
