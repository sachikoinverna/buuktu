package com.example.buuktu.bottomsheet;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.Nullable;

import com.example.buuktu.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class BottomSheetChooseGender extends BottomSheetDialogFragment {
    TextInputLayout et_otherGendersCharacterkieFilled;
    TextInputEditText et_otherGendersCharacterkie;
    RadioButton rb_man_chracterkie,rb_woman_chracterkie,rb_gender_fluid_chracterkie,rb_no_binary_chracterkie,rb_other_gender_characterkie,rb_unknown_gender_characterkie;
    List<RadioButton> allRadioButtons = new ArrayList<>();
    Context context;
    int option;
    public BottomSheetChooseGender(int option) {
        this.option = option;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
    ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.choose_gender_dialog,
                container, false);
        initComponents(v);
        return v;
    }
    private void initComponents(View view){
        et_otherGendersCharacterkieFilled = view.findViewById(R.id.et_otherGendersCharacterkieFilled);
        et_otherGendersCharacterkie = view.findViewById(R.id.et_otherGendersCharacterkie);
        rb_man_chracterkie = view.findViewById(R.id.rb_man_chracterkie);
        rb_woman_chracterkie = view.findViewById(R.id.rb_woman_chracterkie);
        rb_gender_fluid_chracterkie = view.findViewById(R.id.rb_gender_fluid_chracterkie);
        rb_no_binary_chracterkie= view.findViewById(R.id.rb_no_binary_chracterkie);
        rb_other_gender_characterkie= view.findViewById(R.id.rb_other_gender_characterkie);
        rb_unknown_gender_characterkie = view.findViewById(R.id.rb_unknown_gender_characterkie);
        allRadioButtons.add(rb_woman_chracterkie);
        allRadioButtons.add(rb_gender_fluid_chracterkie);
        allRadioButtons.add(rb_man_chracterkie);
        allRadioButtons.add(rb_no_binary_chracterkie);
        allRadioButtons.add(rb_other_gender_characterkie);
        allRadioButtons.add(rb_unknown_gender_characterkie);

        for (RadioButton rb : allRadioButtons) {
            rb.setOnClickListener(v -> {
                for (RadioButton other : allRadioButtons) {
                    other.setChecked(false);
                }
                rb.setChecked(true);
                if(rb.getId()==R.id.rb_other_gender_characterkie){
                    et_otherGendersCharacterkieFilled.setVisibility(View.VISIBLE);
                    // activar el que se pulsó
                }else {
                    et_otherGendersCharacterkieFilled.setVisibility(View.GONE);
                }
            });
            if(rb.getId()==option){
                rb.setChecked(true);
            }
        }
        if(option==R.id.rb_unknown_gender_characterkie){
            et_otherGendersCharacterkieFilled.setVisibility(View.GONE);
        }
    }
}
