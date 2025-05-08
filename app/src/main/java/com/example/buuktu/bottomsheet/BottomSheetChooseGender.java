package com.example.buuktu.bottomsheet;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.Nullable;

import com.example.buuktu.CreateCharacterkie;
import com.example.buuktu.R;
import com.example.buuktu.views.MainActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class BottomSheetChooseGender extends BottomSheetDialogFragment implements View.OnClickListener {
    TextInputLayout et_otherGendersCharacterkieFilled;
    TextInputEditText et_otherGendersCharacterkie;
    RadioButton rb_man_chracterkie,rb_woman_chracterkie,rb_gender_fluid_chracterkie,rb_no_binary_chracterkie,rb_other_gender_characterkie,rb_unknown_gender_characterkie,rb_checked;
    List<RadioButton> allRadioButtons = new ArrayList<>();
    Context context;
    CreateCharacterkie createCharacterkie;
    int option;
    String optionString;
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
        context = getContext();
        if(getActivity() instanceof MainActivity) {
            if (getParentFragment() instanceof CreateCharacterkie) {
                createCharacterkie = (CreateCharacterkie) getParentFragment();
            }
        }
        return v;
    }
    private void initComponents(View view){
        et_otherGendersCharacterkieFilled = view.findViewById(R.id.et_otherGendersCharacterkieFilled);
        et_otherGendersCharacterkie = view.findViewById(R.id.et_otherGendersCharacterkie);
        rb_man_chracterkie = view.findViewById(R.id.rb_gender_man_chracterkie);
        rb_woman_chracterkie = view.findViewById(R.id.rb_gender_woman_chracterkie);
        rb_gender_fluid_chracterkie = view.findViewById(R.id.rb_gender_fluid_chracterkie);
        rb_no_binary_chracterkie= view.findViewById(R.id.rb_gender_no_binary_chracterkie);
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
                rb_checked = view.findViewById(rb.getId());
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

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.bt_save_status_characterkie){
            if(rb_other_gender_characterkie.isChecked()){
                String gender = et_otherGendersCharacterkie.getText().toString();

                createCharacterkie.setOptionGenderString(gender);
                createCharacterkie.getCharacterkie().setStatus(gender);
            }else {
                createCharacterkie.setOptionGenderString(rb_checked.getText().toString());
            }
            createCharacterkie.setOptionGender(rb_checked.getId());
            createCharacterkie.getCharacterkie().setStatus(rb_checked.getTag().toString());
        }
    }
}
