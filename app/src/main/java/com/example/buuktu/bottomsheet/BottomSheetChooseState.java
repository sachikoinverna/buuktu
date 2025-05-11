package com.example.buuktu.bottomsheet;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RadioButton;

import androidx.annotation.Nullable;

import com.example.buuktu.CreateCharacterkie;
import com.example.buuktu.R;
import com.example.buuktu.views.MainActivity;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class BottomSheetChooseState extends BottomSheetDialogFragment implements View.OnClickListener {
    TextInputLayout et_otherStatusCharacterkieFilled;
    TextInputEditText et_otherStatusCharacterkie;
    ImageButton bt_save_status_characterkie;
    RadioButton rb_alive_status_characterkie,rb_dead_status_characterkie,rb_unknown_status_characterkie,rb_other_status_characterkie,rb_checked;
    final List<RadioButton> allRadioButtons = new ArrayList<>();
    CreateCharacterkie createCharacterkie;
    final int option;
    final String optionString;
    public BottomSheetChooseState(int option,String optionString) {
        this.option = option;
        this.optionString = optionString;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
    ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.choose_status_dialog,
                container, false);
        intComponents(v);
        if(getActivity() instanceof MainActivity) {
            if (getParentFragment() instanceof CreateCharacterkie) {
                createCharacterkie = (CreateCharacterkie) getParentFragment();
            }
        }
        return v;
    }
    private void intComponents(View view){
        et_otherStatusCharacterkieFilled = view.findViewById(R.id.et_otherStatusCharacterkieFilled);
        et_otherStatusCharacterkie = view.findViewById(R.id.et_otherStatusCharacterkie);
        rb_alive_status_characterkie = view.findViewById(R.id.rb_status_alive);
        rb_dead_status_characterkie = view.findViewById(R.id.rb_status_dead);
        rb_unknown_status_characterkie= view.findViewById(R.id.rb_status_unknown);
        rb_other_status_characterkie= view.findViewById(R.id.rb_other_status_characterkie);
        bt_save_status_characterkie = view.findViewById(R.id.bt_status_save_characterkie);
        allRadioButtons.add(rb_dead_status_characterkie);
        allRadioButtons.add(rb_alive_status_characterkie);
        allRadioButtons.add(rb_unknown_status_characterkie);
        allRadioButtons.add(rb_other_status_characterkie);
        setListeners();
        if(option!=R.id.rb_other_status_characterkie){
            et_otherStatusCharacterkieFilled.setVisibility(View.GONE);
        }else{
            et_otherStatusCharacterkie.setText(optionString);
        }
    }
    private void setListeners(){
        for (RadioButton rb : allRadioButtons) {
            rb.setOnClickListener(v -> {
                for (RadioButton other : allRadioButtons) {
                    other.setChecked(false);
                }
                rb.setChecked(true);
                rb_checked = rb;
                et_otherStatusCharacterkieFilled.setVisibility(rb.getId()==R.id.rb_other_status_characterkie?View.VISIBLE:View.GONE);
            });
            if(rb.getId()==option){
                rb.setChecked(true);
                rb_checked=rb;
            }
        }

        bt_save_status_characterkie.setOnClickListener(this);
    }
    private void saveOption(){
        int idChecked = rb_checked.getId();
        if (idChecked != option) {
            if (idChecked==R.id.rb_status_unknown) {
                String textString = et_otherStatusCharacterkie.getText().toString();
                if (textString.isEmpty()) {
                    et_otherStatusCharacterkieFilled.setError("Este campo no puede estar vacío");
                    et_otherStatusCharacterkie.requestFocus();

                    return; // 🚫 No cerrar
                }
                createCharacterkie.getCharacterkie().setStatus(textString);
                createCharacterkie.setOptionStatusString(textString);
            } else {
                createCharacterkie.setOptionStatusString(rb_checked.getText().toString());
                createCharacterkie.getCharacterkie().setStatus(rb_checked.getTag().toString());
            }
            createCharacterkie.setOptionStatus(rb_checked.getId());
            dismiss(); // ✅ Solo se cierra si todo está bien
        }
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        dialog.setCanceledOnTouchOutside(false); // No se cierra al tocar fuera

        dialog.setOnShowListener(dialogInterface -> {
            BottomSheetDialog d = (BottomSheetDialog) dialogInterface;
            FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                BottomSheetBehavior<?> behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setHideable(false); // No se puede deslizar para cerrar
                behavior.setDraggable(false); // Opcional: bloquear arrastre
            }
        });

        return dialog;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.bt_status_save_characterkie) {
        saveOption();
        }
    }
}
