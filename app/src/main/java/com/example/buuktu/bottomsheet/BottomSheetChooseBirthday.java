package com.example.buuktu.bottomsheet;

import static com.example.buuktu.R.layout.custom_spinner_style;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListPopupWindow;
import android.widget.RadioButton;
import android.widget.Spinner;

import androidx.annotation.Nullable;

import com.example.buuktu.CreateCharacterkie;
import com.example.buuktu.R;
import com.example.buuktu.views.MainActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BottomSheetChooseBirthday extends BottomSheetDialogFragment implements View.OnClickListener{
    RadioButton rb_unknown_birthday,rb_full_birthday,rb_day_month_birthday,rb_month_year_birthday,rb_month_birthday,rb_year_birthday,rb_checked;
    ImageButton bt_day_1,bt_day_2,bt_day_3,bt_day_4,bt_day_5,bt_day_6,bt_day_7,bt_day_8,bt_day_9,bt_day_10,bt_day_11,bt_day_12,bt_day_13,bt_day_14,bt_day_15,bt_day_16,bt_day_17,bt_day_18,bt_day_19,bt_day_20,bt_day_21,bt_day_22,bt_day_23,bt_day_24,bt_day_25,bt_day_26,bt_day_27,bt_day_28,bt_day_29,bt_day_30,bt_day_31,bt_previous_month_birthday_selector,bt_next_month_birthday_selector,bt_show_day_month_birthday_selector,bt_show_year_selector;
    int option;
    Context context;
    List<RadioButton> allRadioButtons = new ArrayList<>();
    TextInputLayout et_yearBirthdayCreateFull;
    TextInputEditText et_yearBirthdayCreate;
    String optionString;
    public BottomSheetChooseBirthday(int option) {
        this.option = option;
    }
    CreateCharacterkie createCharacterkie;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
    ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.choose_birthday_dialog,
                container, false);
        initComponents(v);
        setInitialOption();
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
        rb_unknown_birthday = view.findViewById(R.id.rb_unknown_birthday);
        rb_full_birthday = view.findViewById(R.id.rb_full_birthday);
        rb_day_month_birthday = view.findViewById(R.id.rb_day_month_birthday);
        rb_month_year_birthday= view.findViewById(R.id.rb_month_year_birthday);
        rb_month_birthday= view.findViewById(R.id.rb_month_birthday);
        rb_year_birthday= view.findViewById(R.id.rb_year_birthday);
        et_yearBirthdayCreateFull = view.findViewById(R.id.et_yearBirthdayCreateFull);
        et_yearBirthdayCreate = view.findViewById(R.id.et_yearBirthdayCreate);
         allRadioButtons.add(rb_full_birthday);
        allRadioButtons.add(rb_day_month_birthday);
        allRadioButtons.add(rb_unknown_birthday);
        allRadioButtons.add(rb_month_year_birthday);
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
                    // activar el que se pulsó
                }else {
                }
            });
        }
    }
    private int getFormatForSelectedOption(int radioButtonId) {
        if(radioButtonId==R.id.rb_full_birthday) {
            return 0; // dd/MM/yyyy
        }else if(radioButtonId==R.id.rb_day_month_birthday) {
            return 1; // dd/MM
        }else if(radioButtonId==R.id.rb_month_year_birthday) {
            return 3; // MM/yyyy
        }
        else if(radioButtonId==R.id.rb_month_birthday) {
            return 6; // MM
        }
        else if(radioButtonId==R.id.rb_year_birthday) {
            return 7; // yy
        }
        return -1;
    }
    private void setInitialOption(){
        if(option==R.id.rb_unknown_birthday){
            rb_unknown_birthday.setChecked(true);
        } else if (option==R.id.rb_full_birthday) {
            rb_full_birthday.setChecked(true);
        }else if(option==R.id.rb_day_month_birthday){
            rb_day_month_birthday.setChecked(true);
        } else if (option==R.id.rb_month_year_birthday) {
            rb_month_year_birthday.setChecked(true);
        }else if(option==R.id.rb_month_birthday){
            rb_month_birthday.setChecked(true);
        } else if (option==R.id.rb_year_birthday) {
            rb_year_birthday.setChecked(true);
        }
    }
    private String formatDate(int year, int month, int day, int formatType) {
        String result;
        switch (formatType) {
            case 0: // dd/MM/yyyy
                result = String.format(Locale.getDefault(), "%02d/%02d/%04d", day, month + 1, year);
                break;
            case 1: // dd/MM
                result = String.format(Locale.getDefault(), "%02d/%02d", day, month + 1);
                break;
            case 2: // dd/yy
                result = String.format(Locale.getDefault(), "%02d/%02d", day, year % 100);
                break;
            case 3: // MM/yyyy
                result = String.format(Locale.getDefault(), "%02d/%04d", month + 1, year);
                break;
            case 4: // dd/yyyy
                result = String.format(Locale.getDefault(), "%02d/%04d", day, year);
                break;
            case 5: // dd
                result = String.format(Locale.getDefault(), "%02d", day);
                break;
            case 6: // MM
                result = String.format(Locale.getDefault(), "%02d", month + 1);
                break;
            case 7: // yy
                result = String.format(Locale.getDefault(), "%02d", year % 100);
                break;
            default:
                result = "";
        }
        return result;
    }
    private void hideShowField(DatePicker datePicker, int formatType) {
        // Aquí podemos ocultar el campo de fecha según el tipo de formato seleccionado
        try {
            // Reflexión para acceder a las vistas internas del DatePicker
            Field dayField = datePicker.getClass().getDeclaredField("mDaySpinner");
            dayField.setAccessible(true);
            Spinner daySpinner = (Spinner) dayField.get(datePicker);
            if (formatType == 5 || formatType == 6) { // Si solo seleccionamos mes o año, ocultar día
                daySpinner.setVisibility(View.GONE);
            } else {
                daySpinner.setVisibility(View.VISIBLE); // Asegurar que el día se muestre si no estamos en un formato que lo oculte
            }

            Field monthField = datePicker.getClass().getDeclaredField("mMonthSpinner");
            monthField.setAccessible(true);
            Spinner monthSpinner = (Spinner) monthField.get(datePicker);
            if (formatType == 4 || formatType == 6) { // Si solo seleccionamos día o año, ocultar mes
                monthSpinner.setVisibility(View.GONE);
            } else {
                monthSpinner.setVisibility(View.VISIBLE); // Asegurar que el mes se muestre si no estamos en un formato que lo oculte
            }

            Field yearField = datePicker.getClass().getDeclaredField("mYearSpinner");
            yearField.setAccessible(true);
            Spinner yearSpinner = (Spinner) yearField.get(datePicker);
            if (formatType == 4 || formatType == 5) { // Si solo seleccionamos día o mes, ocultar año
                yearSpinner.setVisibility(View.GONE);
            } else {
                yearSpinner.setVisibility(View.VISIBLE); // Asegurar que el año se muestre si no estamos en un formato que lo oculte
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
      //  if(v.getId() == R.id.bt_save_status_characterkie){
         /*   if(rb_other.isChecked()){
                createCharacterkie.setOptionStatusString(et_otherStatusCharacterkie.getText().toString());
            }else {
                createCharacterkie.setOptionGenderString(rb_checked.getText().toString());
            }
            createCharacterkie.setOptionStatus(rb_checked.getId());
        }*/
    }
}
