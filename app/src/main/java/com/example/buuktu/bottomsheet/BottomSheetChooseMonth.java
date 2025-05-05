package com.example.buuktu.bottomsheet;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.example.buuktu.CreateCharacterkie;
import com.example.buuktu.R;
import com.example.buuktu.views.MainActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class BottomSheetChooseMonth extends BottomSheetDialogFragment {
    int option;
    Context context;
    List<Button> allButtons = new ArrayList<>();
    Button bt_day_one,bt_day_two,bt_day_three,bt_day_four,bt_day_five,bt_day_six,bt_day_seven,bt_day_eight,bt_day_nine,bt_day_ten,bt_day_eleven,bt_day_twelve;
    public BottomSheetChooseMonth(int option) {
        this.option = option;
    }
    CreateCharacterkie createCharacterkie;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
    ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.choose_month_dialog,
                container, false);
        initComponents(v);
       // setInitialOption();
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
        bt_day_one =view.findViewById(R.id.bt_month_one);
                bt_day_two=view.findViewById(R.id.bt_day_two);
                bt_day_three=view.findViewById(R.id.bt_day_three);
                bt_day_four= view.findViewById(R.id.bt_month_four);
                bt_day_five=view.findViewById(R.id.bt_day_five);
                bt_day_six=view.findViewById(R.id.bt_month_six);
                bt_day_seven=view.findViewById(R.id.bt_day_seven);
                bt_day_eight=view.findViewById(R.id.bt_month_eight);
                bt_day_nine=view.findViewById(R.id.bt_month_nine);
                bt_day_ten=view.findViewById(R.id.bt_day_ten);
                bt_day_eleven=view.findViewById(R.id.bt_month_eleven);
                bt_day_twelve=view.findViewById(R.id.bt_month_twelve);
        allButtons.add(bt_day_one);
        allButtons.add(bt_day_two);
        allButtons.add(bt_day_three);
        allButtons.add(bt_day_four);
        allButtons.add(bt_day_five);
        allButtons.add(bt_day_six);
        allButtons.add(bt_day_seven);
        allButtons.add(bt_day_eight);
        allButtons.add(bt_day_nine);
        allButtons.add(bt_day_ten);
        allButtons.add(bt_day_eleven);
        allButtons.add(bt_day_twelve);
        for (Button bt : allButtons) {
            bt.setOnClickListener(v -> {
                bt.setBackgroundTintList(ColorStateList.valueOf(R.color.white));
                for (Button bt2 : allButtons){
                    if(bt2.getId()!=bt.getId()) {
                     //   bt2.setBackgroundTintList(ColorStateList.valueOf(Integer.parseInt("#B39DDB")));
                    }
                }
            });
        }
    }
}
