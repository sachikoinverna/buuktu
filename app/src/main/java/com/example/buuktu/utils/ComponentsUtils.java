package com.example.buuktu.utils;

import android.content.Context;
import android.printservice.CustomPrinterIconCallback;
import android.text.Editable;
import android.text.InputType;
import android.text.PrecomputedText;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;

import com.example.buuktu.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.w3c.dom.Text;

import java.security.AlgorithmParameterGeneratorSpi;
import java.util.List;

public class ComponentsUtils {
    public static void customTextInputEditText(Context context, TextInputEditText textInputEditText, TextInputLayout.LayoutParams params) {
            textInputEditText.setLayoutParams(new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
            ));

        textInputEditText.setGravity(Gravity.CENTER);  // Usa 'setGravity' para alinear el texto
        textInputEditText.setEms(10);
        textInputEditText.setHintTextColor(ContextCompat.getColorStateList(context, R.color.black)); // Color del hint
    }

    public static void customTextInputLayout(Context context, TextInputLayout textInputLayout, ConstraintLayout.LayoutParams layoutParams, ConstraintLayout constraintLayout) {
        textInputLayout.setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_OUTLINE); // Modo "Outlined"
        textInputLayout.setBoxStrokeColor(ContextCompat.getColor(context, R.color.black)); // Color del borde
        textInputLayout.setBoxBackgroundColor(ContextCompat.getColor(context, R.color.white)); // Color de fondo
        textInputLayout.setHintTextColor(ContextCompat.getColorStateList(context, R.color.brownBrown)); // Color del hint
        layoutParams.setMargins(16, 16, 16, 16); // Márgenes para espaciado
        textInputLayout.setLayoutParams(layoutParams);
        textInputLayout.setCounterEnabled(true);
        float radioEsquinasDP = 58f;  // Radio de las esquinas en dp
        float radioEsquinasPx = DimensionsUtils.convertDpToPx(radioEsquinasDP, context);
        textInputLayout.setBoxCornerRadii(radioEsquinasPx, radioEsquinasPx, radioEsquinasPx, radioEsquinasPx);
        textInputLayout.setStartIconTintList(ContextCompat.getColorStateList(context, R.color.black)); // Color del icono
        textInputLayout.setStartIconDrawable(ContextCompat.getDrawable(context, R.drawable.sharp_emoji_nature_24));
        textInputLayout.setHint(context.getResources().getText(R.string.project_id)); // Establecer el hint
        textInputLayout.setCounterMaxLength(50); // Cambia 50 por la cantidad que desees

        // Asignar un id único para el TextInputLayout
        textInputLayout.setId(View.generateViewId());

        // Asignar un id único para el TextInputEditText
        textInputLayout.getChildAt(0).setId(View.generateViewId()); // Este es el TextInputEditText dentro del TextInputLayout

        // Asegurarse de que todos los hijos del ConstraintLayout tengan un ID
        for (int i = 0; i < constraintLayout.getChildCount(); i++) {
            View child = constraintLayout.getChildAt(i);
            if (child.getId() == View.NO_ID) {
                child.setId(View.generateViewId());
            }
        }
        constraintLayout.addView(textInputLayout);
        // Aplica las restricciones usando ConstraintSet después de asignar los IDs
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);

        // Coloca el nuevo campo debajo del TextInputLayout pero antes del ToggleButton
        constraintSet.connect(textInputLayout.getId(), ConstraintSet.TOP, R.id.et_nameCharacterkieCreateFull, ConstraintSet.BOTTOM, 16); // Después del TextInputLayout
        constraintSet.connect(textInputLayout.getId(), ConstraintSet.START, ConstraintLayout.LayoutParams.PARENT_ID, ConstraintSet.START, 16);
        constraintSet.connect(textInputLayout.getId(), ConstraintSet.END, ConstraintLayout.LayoutParams.PARENT_ID, ConstraintSet.END, 16);

        // Coloca el ToggleButton debajo del nuevo campo
        constraintSet.connect(R.id.tb_CharacterkiePrivacity, ConstraintSet.TOP, textInputLayout.getId(), ConstraintSet.BOTTOM, 16); // Encima del ToggleButton

        // Aplica las restricciones al ConstraintLayout
        constraintSet.applyTo(constraintLayout);
    }


    public static void createTextInputEditText(String type, String name, Context context, ConstraintLayout constraintLayout) {
        // Crear un TextInputLayout
        TextInputLayout textInputLayout = new TextInputLayout(context);
        textInputLayout.setId(View.generateViewId());

        // Crear un TextInputEditText
        TextInputEditText textInputEditText = new TextInputEditText(context);
        textInputEditText.setId(View.generateViewId());
        textInputLayout.addView(textInputEditText);

        // Establecer el tipo de entrada basado en el tipo proporcionado
        if (type.equals("Decimal")) {
            textInputEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        } else if (type.equals("String")) {
            textInputEditText.setInputType(InputType.TYPE_CLASS_TEXT);
        } else if (type.equals("Number")) {
            textInputEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        }

        // Crear LayoutParams para ConstraintLayout para el TextInputLayout
        ConstraintLayout.LayoutParams layoutParamsTextInputLayout = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );

        // Crear LayoutParams para el TextInputEditText dentro del TextInputLayout
        TextInputLayout.LayoutParams layoutParamsTextInputEditText = new TextInputLayout.LayoutParams(
                TextInputLayout.LayoutParams.MATCH_PARENT,
                TextInputLayout.LayoutParams.WRAP_CONTENT
        );

        customTextInputEditText(context, textInputEditText, layoutParamsTextInputEditText);

        // Llamar a los métodos para personalizar el TextInputLayout y TextInputEditText
        customTextInputLayout(context, textInputLayout, layoutParamsTextInputLayout, constraintLayout);
// Añadir el TextInputLayout al ConstraintLayout primero

        // Agregar el TextInputEditText al TextInputLayout

    }
    public static void customRadioButton(Context context, TextInputEditText textInputEditText,LinearLayout.LayoutParams params){
        textInputEditText.setLayoutParams(params);
        textInputEditText.setTextAlignment(TextInputLayout.TEXT_ALIGNMENT_CENTER);
        textInputEditText.setGravity(Gravity.CENTER);
        textInputEditText.setEms(10);
        textInputEditText.setHintTextColor(ContextCompat.getColorStateList(context, R.color.black));

    }
    public void createRadioButton(List<String> options,Context context) {
        // Crear los RadioButtons dinámicamente y añadirlos al BottomSheet
        for (String option : options) {
            RadioButton radioButton = new RadioButton(context);
            radioButton.setText(option);
            radioButton.setButtonDrawable(R.drawable.radiobutton_custom);
            // ColorStateList colorStateList = new ColorStateList();
            //  radioButton.setButtonTintList(ColorStateList.valueOf(R.color.brownBrown));
            RadioGroup radioGroup = new RadioGroup(context);
            radioGroup.addView(radioButton);
            // Añadir al BottomSheet o a un contenedor específico
        //    bottomSheetContainer.addView(radioGroup);
        }
    }
    private int getLastFieldBeforeSwitch(ConstraintLayout constraintLayout, int switchId) {
        int lastFieldId = -1;

        for (int i = 0; i < constraintLayout.getChildCount(); i++) {
            View child = constraintLayout.getChildAt(i);
            if (child.getId() == switchId) {
                break; // Si encontramos el switch, detenemos la búsqueda
            }
            lastFieldId = child.getId(); // Guardamos el ID del último campo encontrado
        }
        return lastFieldId;
    }
}
