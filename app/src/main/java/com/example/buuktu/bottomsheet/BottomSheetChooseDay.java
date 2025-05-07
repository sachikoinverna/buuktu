package com.example.buuktu.bottomsheet;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;

import androidx.annotation.Nullable;

import com.example.buuktu.CreateCharacterkie;
import com.example.buuktu.R;
import com.example.buuktu.views.MainActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class BottomSheetChooseDay extends BottomSheetDialogFragment implements View.OnClickListener {
    ImageButton ib_save_day_dialog,ib_back_day_dialog;
    int option;
    Context context;
    List<Button> allRadioButtons = new ArrayList<>();
    Button bt_day_one,bt_day_two,bt_day_three,bt_day_four,bt_day_five,bt_day_six,bt_day_seven,bt_day_eight,bt_day_nine,bt_day_ten,bt_day_eleven,bt_day_twelve;
    public BottomSheetChooseDay(int option) {
        this.option = option;
    }
    CreateCharacterkie createCharacterkie;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
    ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.choose_birthday_dialog,
                container, false);
        initComponents(v);
      //  setInitialOption();
        context = getContext();
        if(getActivity() instanceof MainActivity) {
            if (getParentFragment() instanceof CreateCharacterkie) {
                createCharacterkie = (CreateCharacterkie) getParentFragment();
            }
        }
        return v;
    }
    private void initComponents(View view){
        context = getContext();

        allRadioButtons.add(rb_full_birthday);
        allRadioButtons.add(rb_day_month_birthday);
        allRadioButtons.add(rb_unknown_birthday);
        allRadioButtons.add(rb_month_year_birthday);
        allRadioButtons.add(rb_day_year_birthday);
        allRadioButtons.add(rb_day_birthday);
        allRadioButtons.add(rb_month_birthday);
        allRadioButtons.add(rb_year_birthday);
        for (RadioButton rb : allRadioButtons) {
            rb.setOnClickListener(v -> {
                for (RadioButton other : allRadioButtons) {
                    other.setChecked(false);
                }
                rb.setChecked(true);
                rb_checked = view.findViewById(rb.getId());
                if(rb.getId()==R.id.rb_unknown_birthday){
                    datePicker.setVisibility(View.GONE);
                    // activar el que se pulsó
                }else {
                    datePicker.setVisibility(View.VISIBLE);
                 //   hideField(datePicker,getFormatForSelectedOption(rb.getId()));
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.ib_back_day_dialog){

        } else if (v.getId()==R.id.ib_save_day_dialog) {

        }
    }
}
