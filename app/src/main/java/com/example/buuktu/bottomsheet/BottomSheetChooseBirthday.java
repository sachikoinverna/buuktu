package com.example.buuktu.bottomsheet;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.buuktu.R;
import com.example.buuktu.utils.CheckUtil;
import com.example.buuktu.views.CreateCharacterkie;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class BottomSheetChooseBirthday extends BottomSheetDialogFragment implements View.OnClickListener {
    final int option;
    final List<RadioButton> allRadioButtons = new ArrayList<>();
    final String optionBirthdayString;
    RadioButton rb_unknown_birthday, rb_full_birthday, rb_month_year_birthday, rb_month_birthday, rb_year_birthday, rb_checked;
    ImageButton bt_day_1, bt_day_2, bt_day_3, bt_day_4, bt_day_5, bt_day_6, bt_day_7, bt_day_8, bt_day_9, bt_day_10, bt_day_11, bt_day_12, bt_day_13, bt_day_14, bt_day_15, bt_day_16, bt_day_17, bt_day_18, bt_day_19, bt_day_20, bt_day_21, bt_day_22, bt_day_23, bt_day_24, bt_day_25, bt_day_26, bt_day_27, bt_day_28, bt_day_29, bt_day_30, bt_day_31, bt_previous_month_birthday_selector, bt_next_month_birthday_selector, bt_show_day_month_birthday_selector, bt_selected, ib_save_date_dialog;
    Context context;
    TextInputLayout et_yearBirthdayCreateFull;
    TextInputEditText et_yearBirthdayCreate;
    TextView tv_current_month_birthday_selector, tv_head_day_month_birthday_selector, tv_separator_day_month_birthday_selector;
    int month, day, year;
    ImageButton[] dayButtons;
    ImageView iv_head_day_month_birthday_selector, iv_background_day_month_selector;
    boolean daysMonthsSelectorVisible, yearFieldVisible, daysOptionVisible, monthVisible, yearOptionVisible;
    TextView[] tvDays;
    String[] meses;
    Drawable arrow_down, arrow_up;
    CreateCharacterkie createCharacterkie;

    public BottomSheetChooseBirthday(int option, String optionBirthdayString, String[] meses) {
        this.option = option;
        this.optionBirthdayString = optionBirthdayString;
        this.meses = meses;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
    ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.choose_birthday_dialog,
                container, false);
        initComponents(v);
        setInitialOption();
        return v;
    }

    private void initComponents(View view) {
        context = getContext();
        createCharacterkie = (CreateCharacterkie) getParentFragment();
        rb_unknown_birthday = view.findViewById(R.id.rb_unknown_birthday);
        rb_full_birthday = view.findViewById(R.id.rb_full_birthday);
        rb_month_year_birthday = view.findViewById(R.id.rb_month_year_birthday);
        rb_month_birthday = view.findViewById(R.id.rb_month_birthday);
        rb_year_birthday = view.findViewById(R.id.rb_year_birthday);
        et_yearBirthdayCreateFull = view.findViewById(R.id.et_yearBirthdayCreateFull);
        et_yearBirthdayCreate = view.findViewById(R.id.et_yearBirthdayCreate);
        bt_show_day_month_birthday_selector = view.findViewById(R.id.bt_show_day_month_birthday_selector);
        bt_next_month_birthday_selector = view.findViewById(R.id.bt_next_month_birthday_selector);
        bt_previous_month_birthday_selector = view.findViewById(R.id.bt_previous_month_birthday_selector);
        tv_current_month_birthday_selector = view.findViewById(R.id.tv_current_month_birthday_selector);
        tv_head_day_month_birthday_selector = view.findViewById(R.id.tv_head_day_month_birthday_selector);
        tv_separator_day_month_birthday_selector = view.findViewById(R.id.tv_separator_day_month_birthday_selector);
        iv_head_day_month_birthday_selector = view.findViewById(R.id.iv_head_day_month_birthday_selector);
        iv_background_day_month_selector = view.findViewById(R.id.iv_background_day_month_selector);
        arrow_down = ContextCompat.getDrawable(requireContext(), R.drawable.twotone_arrow_drop_down_circle_24);
        arrow_up = ContextCompat.getDrawable(requireContext(), R.drawable.twotone_arrow_circle_up_24);
        ib_save_date_dialog = view.findViewById(R.id.ib_save_date_dialog);
        setListeners();
        allRadioButtons.add(rb_full_birthday);
        allRadioButtons.add(rb_unknown_birthday);
        allRadioButtons.add(rb_month_year_birthday);
        allRadioButtons.add(rb_month_birthday);
        allRadioButtons.add(rb_year_birthday);
        rb_checked = view.findViewById(option);
        for (RadioButton rb : allRadioButtons) {
            rb.setOnClickListener(v -> {
                rb.setChecked(true);
                rb_checked = view.findViewById(rb.getId());
                setFields();
                for (RadioButton rb2 : allRadioButtons) {
                    if (rb2.getId() != rb_checked.getId()) {
                        rb2.setChecked(false);
                    }
                }
            });
        }

        dayButtons = new ImageButton[31];
        tvDays = new TextView[31];  // Array que almacenará los TextView de los días
        setArrays(view);

    }

    private void setArrays(View view) {
        for (int i = 0; i < 31; i++) {
            String tv_day_id = "tv_day_" + (i + 1); // Genera el ID dinámicamente: tv_day_1, tv_day_2, ...
            String bt_day_id = "bt_day_" + (i + 1);
            int tv_day_id_full = getResources().getIdentifier(tv_day_id, "id", context.getPackageName()); // Obtiene el ID del recurso
            tvDays[i] = view.findViewById(tv_day_id_full);  // Asocia el TextView a cada posición en el array
            int bt_day_id_full = getResources().getIdentifier(bt_day_id, "id", context.getPackageName()); // Obtiene el ID del recurso
            dayButtons[i] = view.findViewById(bt_day_id_full);  // Asocia el TextView a cada posición en el array
            int finalI = i; // Necesario para usar dentro del listener
            dayButtons[i].setOnClickListener(v -> {
                bt_selected.setImageTintList(ColorStateList.valueOf(Color.parseColor("#9FA8DA")));

                bt_selected = (ImageButton) v;
                int color = getResources().getColor(R.color.brownMaroon, null); // Usando un color de recursos
                day = Integer.parseInt(tvDays[finalI].getText().toString());
// Aplica el tint al ImageButton
                bt_selected.setImageTintList(ColorStateList.valueOf(color));
            });
        }
        bt_selected = dayButtons[0];
        int color = getResources().getColor(R.color.brownMaroon, null); // Usando un color de recursos
        bt_selected.setImageTintList(ColorStateList.valueOf(color));

    }

    private void setListeners() {
        bt_show_day_month_birthday_selector.setOnClickListener(this);
        bt_previous_month_birthday_selector.setOnClickListener(this);
        bt_next_month_birthday_selector.setOnClickListener(this);
        ib_save_date_dialog.setOnClickListener(this);
    }

    private void showHiddeDaysMonths() {
        daysMonthsSelectorVisible = !daysMonthsSelectorVisible;
        int days = getDaysMonth();

        if (daysMonthsSelectorVisible) {
            bt_previous_month_birthday_selector.setVisibility(month == 1 && monthVisible ? View.GONE : View.VISIBLE);
            bt_next_month_birthday_selector.setVisibility(month == 12 && monthVisible ? View.GONE : View.VISIBLE);
        } else {
            bt_previous_month_birthday_selector.setVisibility(View.GONE);
            bt_next_month_birthday_selector.setVisibility(View.GONE);
        }
        if (monthVisible) {
            tv_current_month_birthday_selector.setVisibility(daysMonthsSelectorVisible ? View.VISIBLE : View.GONE);

        }
        if (daysOptionVisible) {
            for (int i = 0; i < days; i++) {
                dayButtons[i].setVisibility(daysMonthsSelectorVisible ? View.VISIBLE : View.GONE);
                tvDays[i].setVisibility(daysMonthsSelectorVisible ? View.VISIBLE : View.GONE);
            }
        }

        bt_show_day_month_birthday_selector.setImageDrawable(daysMonthsSelectorVisible ? arrow_up : arrow_down);

    }

    private int getDaysMonth() {
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
            return 31;
        } else if (month == 4 || month == 6 || month == 9 || month == 11) {
            return 30;
        } else {
            String year = et_yearBirthdayCreate.getText().toString();
            if (!year.isEmpty()) {
                int yearInt = Integer.parseInt(year);
                return isLeapYear(yearInt) ? 29 : 28;
            } else {
                return 28;
            }
        }
    }

    private boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    private void setInitialOption() {
        for (RadioButton rb : allRadioButtons) {
            if (option == rb.getId()) {
                rb.setChecked(true);
            }
        }
        if (rb_checked.getId() == R.id.rb_unknown_birthday) {
            month = 1;
            day = 1;
        } else if (rb_checked.getId() == R.id.rb_full_birthday) {
            String[] date = optionBirthdayString.split("/");
            day = Integer.parseInt(date[0]);
            month = Integer.parseInt(date[1]);
            year = Integer.parseInt(date[2]);

        } else if (rb_checked.getId() == R.id.rb_month_year_birthday) {
            String[] date = optionBirthdayString.split("/");
            month = Integer.parseInt(date[1]);
            year = Integer.parseInt(date[2]);
            day = 1;
        } else if (rb_checked.getId() == R.id.rb_month_birthday) {
            month = Integer.parseInt(optionBirthdayString);
            day = 1;
        } else if (rb_checked.getId() == R.id.rb_year_birthday) {
            year = Integer.parseInt(optionBirthdayString);
            et_yearBirthdayCreate.setText(year);
        }
        tv_current_month_birthday_selector.setText(month == 1 ? String.valueOf(meses[0]) : meses[month - 1]);
        setFields();
    }

    private void setFields() {
        if (rb_checked.getId() == R.id.rb_unknown_birthday) {
            // activar el que se pulsó
            hideShowField(false, false, false);
        } else if (rb_checked.getId() == R.id.rb_full_birthday) {
            hideShowField(true, true, true);
        } else if (rb_checked.getId() == R.id.rb_month_year_birthday) {
            hideShowField(false, true, true);
        } else if (rb_checked.getId() == R.id.rb_month_birthday) {
            hideShowField(false, true, false);
        } else if (rb_checked.getId() == R.id.rb_year_birthday) {
            hideShowField(false, false, true);
        }
    }

    private void hideShowField(boolean days, boolean months, boolean years) {
        tv_separator_day_month_birthday_selector.setVisibility(days || months ? View.VISIBLE : View.GONE);
        iv_head_day_month_birthday_selector.setVisibility(days || months ? View.VISIBLE : View.GONE);
        bt_show_day_month_birthday_selector.setVisibility(days || months ? View.VISIBLE : View.GONE);
        iv_background_day_month_selector.setVisibility(days || months ? View.VISIBLE : View.GONE);
        bt_show_day_month_birthday_selector.setImageDrawable(arrow_down);
        tv_head_day_month_birthday_selector.setVisibility(days || months ? View.VISIBLE : View.GONE);

        for (int i = 0; i < dayButtons.length; i++) {
            dayButtons[i].setVisibility(View.GONE);
            tvDays[i].setVisibility(View.GONE);
        }
        daysMonthsSelectorVisible = false;
        daysOptionVisible = days;
        yearFieldVisible = years;
        monthVisible = months;
        bt_next_month_birthday_selector.setVisibility(View.GONE);
        bt_previous_month_birthday_selector.setVisibility(View.GONE);
        tv_current_month_birthday_selector.setVisibility(View.GONE);
        et_yearBirthdayCreateFull.setVisibility(years ? View.VISIBLE : View.GONE);
        tv_head_day_month_birthday_selector.setText(days ? "DD/MM" : "MM");

    }

    private void nextMonth() {
        month++;
        updateDaysView();
    }

    private void previousMonth() {
        month--;
        updateDaysView();
    }

    private void updateDaysView() {
        int days = getDaysMonth();
        for (int i = 0; i < 31; i++) {
            dayButtons[i].setVisibility(i < days && daysOptionVisible ? View.VISIBLE : View.GONE);
            tvDays[i].setVisibility(i < days && daysOptionVisible ? View.VISIBLE : View.GONE);
        }
        tv_current_month_birthday_selector.setText(meses[month - 1]);
        bt_previous_month_birthday_selector.setVisibility(month == 1 ? View.GONE : View.VISIBLE);
        bt_next_month_birthday_selector.setVisibility(month == 12 ? View.GONE : View.VISIBLE);
    }

    private void saveData() {
        createCharacterkie.setOptionBirthday(rb_checked.getId());
        if (rb_checked.getId() == R.id.rb_year_birthday) {
            if (CheckUtil.checkTextEmpty(getContext(),et_yearBirthdayCreateFull,et_yearBirthdayCreate.getText().toString())) return;
            createCharacterkie.setYear(Integer.parseInt(et_yearBirthdayCreate.getText().toString()));
        } else if (rb_checked.getId() == R.id.rb_month_birthday) {
            createCharacterkie.setMonth(month);
        } else if (rb_checked.getId() == R.id.rb_month_year_birthday) {
            createCharacterkie.setMonth(month);
            createCharacterkie.setYear(year);
        } else if (rb_checked.getId() == R.id.rb_full_birthday) {
            if (!CheckUtil.checkTextEmpty(getContext(),et_yearBirthdayCreateFull,et_yearBirthdayCreate.getText().toString())) return;
            createCharacterkie.setYear(Integer.parseInt(et_yearBirthdayCreate.getText().toString()));
            createCharacterkie.setMonth(month);
            createCharacterkie.setDay(day);
        }
        createCharacterkie.setDate();
        createCharacterkie.getCharacterkie().setBirthday_format(rb_checked.getText().toString());
        dismiss();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        dialog.setCanceledOnTouchOutside(false); // No se cierra al tocar fuera

        dialog.setOnShowListener(dialogInterface -> {
            BottomSheetDialog d = (BottomSheetDialog) dialogInterface;
            FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                BottomSheetBehavior<?> behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setHideable(false); // No se puede deslizar para cerrar
                behavior.setDraggable(false); // Opcional: bloquear arrastre
            }
        });

        return dialog;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_show_day_month_birthday_selector) {
            showHiddeDaysMonths();
        } else if (v.getId() == R.id.bt_next_month_birthday_selector) {
            nextMonth();
        } else if (v.getId() == R.id.bt_previous_month_birthday_selector) {
            previousMonth();
        } else if (v.getId() == R.id.ib_save_date_dialog) {
            saveData();
        }
    }
}
