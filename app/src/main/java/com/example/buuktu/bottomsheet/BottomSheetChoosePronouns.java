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

public class BottomSheetChoosePronouns extends BottomSheetDialogFragment {
    TextInputEditText et_otherPronounsCharacterkie;
    TextInputLayout et_otherPronounsCharacterkieFilled;
    RadioButton rb_ella_la_le_a_characterkie,rb_el_lo_le_o_characterkie,rb_elle__le_e_characterkie,rb_ella_la_a_characterkie,rb_elle_le_characterkie,rb_ellx_lx_x_characterkie,rb_other_characterkie;
    List<RadioButton> allRadioButtons = new ArrayList<>();
    Context context;
    int option;
    public BottomSheetChoosePronouns(int option) {
        this.option = option;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
    ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.choose_pronouns_dialog,
                container, false);
        initComponents(v);
        return v;
    }
    private void initComponents(View view){
        et_otherPronounsCharacterkie = view.findViewById(R.id.et_otherPronounsCharacterkie);
        et_otherPronounsCharacterkieFilled = view.findViewById(R.id.et_otherPronounsCharacterkieFilled);
        rb_ella_la_le_a_characterkie = view.findViewById(R.id.rb_ella_la_le_a_characterkie);
        rb_el_lo_le_o_characterkie = view.findViewById(R.id.rb_el_lo_le_o_characterkie);
        rb_elle__le_e_characterkie = view.findViewById(R.id.rb_elle__le_e_characterkie);
        rb_ella_la_a_characterkie= view.findViewById(R.id.rb_ella_la_a_characterkie);
        rb_elle_le_characterkie= view.findViewById(R.id.rb_elle_le_characterkie);
        rb_ellx_lx_x_characterkie= view.findViewById(R.id.rb_ellx_lx_x_characterkie);
        rb_other_characterkie= view.findViewById(R.id.rb_other_characterkie);
        allRadioButtons.add(rb_el_lo_le_o_characterkie);
        allRadioButtons.add(rb_elle__le_e_characterkie);
        allRadioButtons.add(rb_ella_la_le_a_characterkie);
        allRadioButtons.add(rb_ella_la_a_characterkie);
        allRadioButtons.add(rb_elle_le_characterkie);
        allRadioButtons.add(rb_ellx_lx_x_characterkie);
        allRadioButtons.add(rb_other_characterkie);
        for (RadioButton rb : allRadioButtons) {
            rb.setOnClickListener(v -> {
                for (RadioButton other : allRadioButtons) {
                    other.setChecked(false);
                }
                rb.setChecked(true);
                if(rb.getId()==R.id.rb_other_characterkie){
                    et_otherPronounsCharacterkieFilled.setVisibility(View.GONE);
                    // activar el que se pulsó
                }else {
                    et_otherPronounsCharacterkieFilled.setVisibility(View.VISIBLE);
                }
            });
        }
    }
}
