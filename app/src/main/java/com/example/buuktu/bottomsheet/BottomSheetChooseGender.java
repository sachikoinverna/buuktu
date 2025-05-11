package com.example.buuktu.bottomsheet;

import android.app.Dialog;
import android.content.Context;
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

public class BottomSheetChooseGender extends BottomSheetDialogFragment implements View.OnClickListener {
    TextInputLayout et_otherGendersCharacterkieFilled;
    TextInputEditText et_otherGendersCharacterkie;
    RadioButton rb_man_chracterkie,rb_woman_chracterkie,rb_gender_fluid_chracterkie,rb_no_binary_chracterkie,rb_other_gender_characterkie,rb_unknown_gender_characterkie,rb_checked;
    final List<RadioButton> allRadioButtons = new ArrayList<>();
    Context context;
    CreateCharacterkie createCharacterkie;
    final int option;
    final String optionString;
    ImageButton bt_save_gender_characterkie;
    public BottomSheetChooseGender(int option, String optionString) {
        this.option = option;
        this.optionString = optionString;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
    ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.choose_gender_dialog,
                container, false);
        initComponents(v);
        fillArrays();
        setListeners();
        return v;
    }
    private void initComponents(View view){
        context = getContext();
        if(getActivity() instanceof MainActivity) {
            if (getParentFragment() instanceof CreateCharacterkie) {
                createCharacterkie = (CreateCharacterkie) getParentFragment();
            }
        }
        et_otherGendersCharacterkieFilled = view.findViewById(R.id.et_otherGendersCharacterkieFilled);
        et_otherGendersCharacterkie = view.findViewById(R.id.et_otherGendersCharacterkie);
        rb_man_chracterkie = view.findViewById(R.id.rb_gender_male);
        rb_woman_chracterkie = view.findViewById(R.id.rb_gender_female);
        rb_gender_fluid_chracterkie = view.findViewById(R.id.rb_gender_fluid);
        rb_no_binary_chracterkie= view.findViewById(R.id.rb_gender_nonbinary);
        rb_other_gender_characterkie= view.findViewById(R.id.rb_other_gender_characterkie);
        rb_unknown_gender_characterkie = view.findViewById(R.id.rb_gender_unknown);
        bt_save_gender_characterkie = view.findViewById(R.id.bt_save_gender_characterkie);
        if(option!=R.id.rb_other_gender_characterkie){
            et_otherGendersCharacterkieFilled.setVisibility(View.GONE);
        }else{
            et_otherGendersCharacterkie.setText(optionString);
        }
    }
    private void fillArrays(){
        allRadioButtons.add(rb_woman_chracterkie);
        allRadioButtons.add(rb_gender_fluid_chracterkie);
        allRadioButtons.add(rb_man_chracterkie);
        allRadioButtons.add(rb_no_binary_chracterkie);
        allRadioButtons.add(rb_other_gender_characterkie);
        allRadioButtons.add(rb_unknown_gender_characterkie);
    }
    private void setListeners(){
        for (RadioButton rb : allRadioButtons) {
            rb.setOnClickListener(v -> {
                for (RadioButton other : allRadioButtons) {
                    other.setChecked(false);
                }
                rb.setChecked(true);
                rb_checked = v.findViewById(rb.getId());
                et_otherGendersCharacterkieFilled.setVisibility(rb.getId()==R.id.rb_other_gender_characterkie?View.VISIBLE:View.GONE);

                    if(rb.getId()==option){
                        rb.setChecked(true);
                    }
            });
            if(rb.getId()==option){
                rb.setChecked(true);
            }
        }
        bt_save_gender_characterkie.setOnClickListener(this);
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
    private void saveGender(){
        int idChecked = rb_checked.getId();
        if (idChecked != option) {
            if (idChecked == R.id.rb_gender_unknown) {
                String gender = et_otherGendersCharacterkie.getText().toString();
                if (gender.isEmpty()) {
                    et_otherGendersCharacterkieFilled.setError("Error");
                    et_otherGendersCharacterkie.requestFocus();
                    return;
                }
                createCharacterkie.setOptionGenderString(gender);
                createCharacterkie.getCharacterkie().setStatus(gender);
            } else {
                createCharacterkie.setOptionGenderString(rb_checked.getText().toString());
                createCharacterkie.getCharacterkie().setStatus(rb_checked.getTag().toString());
            }
            createCharacterkie.setOptionGender(rb_checked.getId());
            dismiss();
        }
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_save_gender_characterkie) {
            saveGender();
        }
    }
}
