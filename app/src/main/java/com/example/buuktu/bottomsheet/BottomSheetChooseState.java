package com.example.buuktu.bottomsheet;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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

public class BottomSheetChooseState extends BottomSheetDialogFragment implements View.OnClickListener {
    TextInputLayout et_otherStatusCharacterkieFilled;
    TextInputEditText et_otherStatusCharacterkie;
    ImageButton bt_save_status_characterkie;
    RadioButton rb_alive_status_characterkie,rb_dead_status_characterkie,rb_unknown_status_characterkie,rb_other_status_characterkie,rb_checked;
    List<RadioButton> allRadioButtons = new ArrayList<>();
    Context context;
    CreateCharacterkie createCharacterkie;
    int option;
    String optionString;
    public BottomSheetChooseState(int option,String optionString) {
        this.option = option;
        this.optionString = optionString;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
    ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.choose_status_dialog,
                container, false);
        intiComponents(v);
        context = getContext();
        if(getActivity() instanceof MainActivity) {
            if (getParentFragment() instanceof CreateCharacterkie) {
                createCharacterkie = (CreateCharacterkie) getParentFragment();
            }
        }
        return v;
    }
    private void intiComponents(View view){
        et_otherStatusCharacterkieFilled = view.findViewById(R.id.et_otherStatusCharacterkieFilled);
        et_otherStatusCharacterkie = view.findViewById(R.id.et_otherStatusCharacterkie);
        rb_alive_status_characterkie = view.findViewById(R.id.rb_alive_status_characterkie);
        rb_dead_status_characterkie = view.findViewById(R.id.rb_dead_status_characterkie);
        rb_unknown_status_characterkie= view.findViewById(R.id.rb_unknown_status_characterkie);
        rb_other_status_characterkie= view.findViewById(R.id.rb_other_status_characterkie);
        bt_save_status_characterkie = view.findViewById(R.id.bt_save_status_characterkie);
        allRadioButtons.add(rb_dead_status_characterkie);
        allRadioButtons.add(rb_alive_status_characterkie);
        allRadioButtons.add(rb_unknown_status_characterkie);
        allRadioButtons.add(rb_other_status_characterkie);

        for (RadioButton rb : allRadioButtons) {
            rb.setOnClickListener(v -> {
                for (RadioButton other : allRadioButtons) {
                    other.setChecked(false);
                }
                rb.setChecked(true);
                rb_checked = view.findViewById(rb.getId());

                if(rb.getId()==R.id.rb_other_status_characterkie){
                    et_otherStatusCharacterkieFilled.setVisibility(View.VISIBLE);
                    // activar el que se pulsó
                }else {
                    et_otherStatusCharacterkieFilled.setVisibility(View.GONE);
                }
            });
            if(rb.getId()==option){
                rb.setChecked(true);
            }
        }
        if(option==R.id.rb_unknown_gender_characterkie){
            et_otherStatusCharacterkieFilled.setVisibility(View.GONE);
        }
    }
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.bt_save_status_characterkie){
            if(rb_other_status_characterkie.isChecked()){
                createCharacterkie.setOptionStatusString(et_otherStatusCharacterkie.getText().toString());
            }else {
                createCharacterkie.setOptionStatusString(rb_checked.getText().toString());
            }
            createCharacterkie.setOptionStatus(rb_checked.getId());
        }
    }
}
