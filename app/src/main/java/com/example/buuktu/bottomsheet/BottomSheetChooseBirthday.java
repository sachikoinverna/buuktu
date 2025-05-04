package com.example.buuktu.bottomsheet;

import static com.example.buuktu.R.layout.custom_spinner_style;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
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

public class BottomSheetChooseBirthday extends BottomSheetDialogFragment{
    RadioButton rb_unknown_birthday,rb_full_birthday,rb_day_month_birthday,rb_month_year_birthday,rb_day_year_birthday,rb_day_birthday,rb_month_birthday,rb_year_birthday;
    int option;
    Context context;
    DatePicker datePicker;
    List<RadioButton> allRadioButtons = new ArrayList<>();
    Spinner sp_day_birthday;
    TextInputLayout et_birthdayCreateCharacterkieFull;
    TextInputEditText et_birthdayCreateCharacterkie;
    String[] days = new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
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
        sp_day_birthday = view.findViewById(R.id.sp_day_birthday);
        rb_unknown_birthday = view.findViewById(R.id.rb_unknown_birthday);
        rb_full_birthday = view.findViewById(R.id.rb_full_birthday);
        rb_day_month_birthday = view.findViewById(R.id.rb_day_month_birthday);
        rb_month_year_birthday= view.findViewById(R.id.rb_month_year_birthday);
        rb_day_year_birthday= view.findViewById(R.id.rb_day_year_birthday);
        rb_day_birthday= view.findViewById(R.id.rb_day_birthday);
        rb_month_birthday= view.findViewById(R.id.rb_month_birthday);
        rb_year_birthday= view.findViewById(R.id.rb_year_birthday);
         datePicker = view.findViewById(R.id.datePicker);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.custom_spinner_style, days);
        adapter.setDropDownViewResource(custom_spinner_style);
        sp_day_birthday.setAdapter(adapter);
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
                createCharacterkie.setOptionBirthday(rb.getId());
                createCharacterkie.setOptionGenderString(rb.getText().toString());
                if(rb.getId()==R.id.rb_unknown_birthday){
                    datePicker.setVisibility(View.GONE);
                    // activar el que se pulsó
                }else {
                    datePicker.setVisibility(View.VISIBLE);
                    hideField(datePicker,getFormatForSelectedOption(rb.getId()));
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
        else if(radioButtonId==R.id.rb_day_year_birthday) {
            return 4; // dd/yyyy
        }
        else if(radioButtonId==R.id.rb_day_birthday) {
            return 5; // dd
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
            datePicker.setVisibility(View.GONE);
        } else if (option==R.id.rb_full_birthday) {
            rb_full_birthday.setChecked(true);
        }else if(option==R.id.rb_day_month_birthday){
            rb_day_month_birthday.setChecked(true);
        } else if (option==R.id.rb_month_year_birthday) {
            rb_month_year_birthday.setChecked(true);
        }else if(option==R.id.rb_day_year_birthday){
            rb_day_year_birthday.setChecked(true);
        } else if (option==R.id.rb_day_birthday) {
            rb_day_birthday.setChecked(true);
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
    private void hideField(DatePicker datePicker, int formatType) {
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

}
