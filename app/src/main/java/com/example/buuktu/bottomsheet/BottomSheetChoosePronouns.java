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

public class BottomSheetChoosePronouns extends BottomSheetDialogFragment implements View.OnClickListener {
    TextInputEditText et_otherPronounsCharacterkie;
    TextInputLayout et_otherPronounsCharacterkieFilled;
    RadioButton rb_ella_la_le_a_characterkie,rb_el_lo_le_o_characterkie,rb_elle__le_e_characterkie,rb_ella_la_a_characterkie,rb_ellx_lx_x_characterkie,rb_other_characterkie,rb_unknown_pronouns_characterkie,rb_checked;
    List<RadioButton> allRadioButtons = new ArrayList<>();
    Context context;
    CreateCharacterkie createCharacterkie;
    String optionString;
    int option;
    public BottomSheetChoosePronouns(int option, String optionString) {
        this.option = option;
        this.optionString = optionString;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
    ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.choose_pronouns_dialog,
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
        et_otherPronounsCharacterkie = view.findViewById(R.id.et_otherPronounsCharacterkie);
        et_otherPronounsCharacterkieFilled = view.findViewById(R.id.et_otherPronounsCharacterkieFilled);
        rb_ella_la_le_a_characterkie = view.findViewById(R.id.rb_pronouns_option_fem);
        rb_el_lo_le_o_characterkie = view.findViewById(R.id.rb_pronouns_option_neutral_one);
        rb_elle__le_e_characterkie = view.findViewById(R.id.rb_pronouns_option_masc);
        rb_ella_la_a_characterkie= view.findViewById(R.id.rb_pronouns_option_neutral_two);
        rb_ellx_lx_x_characterkie= view.findViewById(R.id.rb_pronouns_option_neutral_three);
        rb_other_characterkie= view.findViewById(R.id.rb_other_characterkie);
        rb_unknown_pronouns_characterkie = view.findViewById(R.id.rb_unknown_pronouns_characterkie);
        allRadioButtons.add(rb_el_lo_le_o_characterkie);
        allRadioButtons.add(rb_elle__le_e_characterkie);
        allRadioButtons.add(rb_ella_la_le_a_characterkie);
        allRadioButtons.add(rb_ella_la_a_characterkie);
        allRadioButtons.add(rb_ellx_lx_x_characterkie);
        allRadioButtons.add(rb_other_characterkie);
        allRadioButtons.add(rb_unknown_pronouns_characterkie);

        for (RadioButton rb : allRadioButtons) {
            rb.setOnClickListener(v -> {
                for (RadioButton other : allRadioButtons) {
                    other.setChecked(false);
                }
                rb.setChecked(true);
                rb_checked = view.findViewById(rb.getId());
                if(rb.getId()==R.id.rb_other_characterkie){
                    et_otherPronounsCharacterkieFilled.setVisibility(View.VISIBLE);
                    // activar el que se pulsó
                }else {
                    et_otherPronounsCharacterkieFilled.setVisibility(View.GONE);
                }
            });
            if(rb.getId()==option){
                rb.setChecked(true);
            }
            if(option==R.id.rb_other_characterkie){
                et_otherPronounsCharacterkie.setText(optionString);
            }
        }
        if(option==R.id.rb_unknown_gender_characterkie){
            et_otherPronounsCharacterkieFilled.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.bt_save_status_characterkie) {
            String stringEditText = et_otherPronounsCharacterkie.getText().toString();
            int idChecked = rb_checked.getId();
            if (idChecked != option) {
                if (idChecked == R.id.rb_other_characterkie) {
                    createCharacterkie.getCharacterkie().setPronouns(stringEditText);

                    createCharacterkie.setOptionStatusString(stringEditText);
                } else {
                    createCharacterkie.setOptionPronounsString(rb_checked.getText().toString());
                    createCharacterkie.getCharacterkie().setPronouns(rb_checked.getTag().toString());
                }
                createCharacterkie.setOptionPronouns(idChecked);
            } else {
                if (idChecked == R.id.rb_other_characterkie && !optionString.equals(stringEditText)) {
                    createCharacterkie.setOptionStatusString(stringEditText);
                }
            }
        }
    }
}
