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

public class BottomSheetChooseState extends BottomSheetDialogFragment {
    TextInputLayout et_otherStatusCharacterkieFilled;
    TextInputEditText et_otherStatusCharacterkie;
    RadioButton rb_alive_status_characterkie,rb_dead_status_characterkie,rb_comatose_status_characterkie,rb_unknown_status_characterkie,rb_other_gender_characterkie,rb_unknown_gender_characterkie;
    List<RadioButton> allRadioButtons = new ArrayList<>();
    Context context;
    CreateCharacterkie createCharacterkie;
    int option;
    public BottomSheetChooseState(int option) {
        this.option = option;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
    ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_default_image_profile_photo,
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
        et_otherStatusCharacterkie = view.findViewById(R.id.et_otherStatusCharacterkieFilled);
        rb_alive_status_characterkie = view.findViewById(R.id.rb_alive_status_characterkie);
        rb_dead_status_characterkie = view.findViewById(R.id.rb_dead_status_characterkie);
        rb_comatose_status_characterkie = view.findViewById(R.id.rb_comatose_status_characterkie);
        rb_unknown_status_characterkie= view.findViewById(R.id.rb_unknown_status_characterkie);
        rb_other_gender_characterkie= view.findViewById(R.id.rb_other_gender_characterkie);
        rb_unknown_gender_characterkie = view.findViewById(R.id.rb_unknown_gender_characterkie);
        allRadioButtons.add(rb_dead_status_characterkie);
        allRadioButtons.add(rb_comatose_status_characterkie);
        allRadioButtons.add(rb_alive_status_characterkie);
        allRadioButtons.add(rb_unknown_status_characterkie);
        allRadioButtons.add(rb_other_gender_characterkie);
        allRadioButtons.add(rb_unknown_gender_characterkie);

        for (RadioButton rb : allRadioButtons) {
            rb.setOnClickListener(v -> {
                for (RadioButton other : allRadioButtons) {
                    other.setChecked(false);
                }
                rb.setChecked(true);
                createCharacterkie.setOptionGender(rb.getId());
                createCharacterkie.setOptionGenderString(rb.getText().toString());
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
}
