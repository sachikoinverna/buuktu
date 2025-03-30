package com.example.buuktu.utils;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;

import com.example.buuktu.R;
import com.example.buuktu.listeners.OnFieldDeletedListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class ComponentsUtils {
    private static int lastAddedFieldId = -1; // Registro del último campo añadido

    public static void customTextInputEditText(TextInputEditText textInputEditText) {
        textInputEditText.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        ));
        textInputEditText.setGravity(Gravity.CENTER);
        textInputEditText.setEms(10);
    }

    public static void customTextInputLayout(Context context, TextInputLayout textInputLayout) {
        textInputLayout.setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_OUTLINE);
        textInputLayout.setBoxStrokeColor(ContextCompat.getColor(context, R.color.black));
        textInputLayout.setBoxBackgroundColor(ContextCompat.getColor(context, R.color.white));
        textInputLayout.setHintTextColor(ContextCompat.getColorStateList(context, R.color.brownBrown));
        textInputLayout.setCounterEnabled(true);
        float radioEsquinasDP = 58f;
        float radioEsquinasPx = DimensionsUtils.convertDpToPx(radioEsquinasDP, context);
        Log.d("TextInputLayout", "radioEsquinasPx: " + radioEsquinasPx);
        textInputLayout.setBoxCornerRadii(radioEsquinasPx, radioEsquinasPx, radioEsquinasPx, radioEsquinasPx);
        textInputLayout.setStartIconTintList(ContextCompat.getColorStateList(context, R.color.black));
        textInputLayout.setStartIconDrawable(ContextCompat.getDrawable(context, R.drawable.sharp_emoji_nature_24));
        textInputLayout.setHint(context.getResources().getText(R.string.project_id));
        textInputLayout.setCounterMaxLength(50);
    }

    public static void createTextInputEditText(String type, String name, Context context, ConstraintLayout constraintLayout, int switchId, OnFieldDeletedListener listener) {

        TextInputLayout textInputLayout = new TextInputLayout(context, null, com.google.android.material.R.style.Widget_MaterialComponents_TextInputLayout_OutlinedBox);
        TextInputEditText textInputEditText = new TextInputEditText(context);
        textInputLayout.addView(textInputEditText);
        if (type.equals("Decimal")) {
            textInputEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        } else if (type.equals("String")) {
            textInputEditText.setInputType(InputType.TYPE_CLASS_TEXT);
        } else if (type.equals("Number")) {
            textInputEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        } else if (type.equals("Date")) {
            textInputEditText.setInputType(InputType.TYPE_CLASS_DATETIME);
        }

        ConstraintLayout.LayoutParams layoutParamsTextInputLayout = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        textInputLayout.setLayoutParams(layoutParamsTextInputLayout);

        customTextInputEditText(textInputEditText);
        customTextInputLayout(context, textInputLayout);

        textInputLayout.setId(View.generateViewId());
        textInputLayout.getChildAt(0).setId(View.generateViewId());

        constraintLayout.addView(textInputLayout);
        if (lastAddedFieldId == -1) {
            lastAddedFieldId = getLastFieldId(constraintLayout, switchId);
        }
        addConstraintsForView(constraintLayout, textInputLayout.getId(), lastAddedFieldId, switchId);
        lastAddedFieldId = textInputLayout.getId();
        createImageButton(context, constraintLayout, textInputLayout.getId(),listener,switchId);
    }

    public static void customRadioButton(RadioButton radioButton) {
        radioButton.setButtonDrawable(R.drawable.radiobutton_custom);
    }

    public void createRadioButton(List<String> options, Context context, ConstraintLayout constraintLayout, String typeView) {
        RadioGroup radioGroup = new RadioGroup(context);
        radioGroup.setId(View.generateViewId());
        constraintLayout.addView(radioGroup);
        for (String option : options) {
            RadioButton radioButton = new RadioButton(context);
            radioButton.setText(option);
            customRadioButton(radioButton);
            radioGroup.addView(radioButton);
        }
        addConstraintsForView(constraintLayout, radioGroup.getId(),getLastFieldBeforeSwitch(constraintLayout, radioGroup.getId()),0);
    }

    public static void customTextView(TextView textView) {
        textView.setTextAlignment(TextInputLayout.TEXT_ALIGNMENT_CENTER);
        textView.setGravity(Gravity.CENTER);
        textView.setEms(10);
    }

    public void createTextView(String name, Context context, ConstraintLayout constraintLayout, int switchId) {
        TextView textView = new TextView(context);
        textView.setId(View.generateViewId());
        customTextView(textView);
        constraintLayout.addView(textView);
        addConstraintsForView(constraintLayout, textView.getId(), getLastFieldBeforeSwitch(constraintLayout, switchId), switchId);
    }

    public static void customButton(Button button) {
        button.setTextAlignment(TextInputLayout.TEXT_ALIGNMENT_CENTER);
        button.setGravity(Gravity.CENTER);
        button.setEms(10);
    }

    public void createButton(String name, Context context, ConstraintLayout constraintLayout, int switchId) {
        Button button = new Button(context);
        button.setId(View.generateViewId());
        customButton(button);
        constraintLayout.addView(button);
        addConstraintsForView(constraintLayout, button.getId(), getLastFieldBeforeSwitch(constraintLayout, switchId), switchId);
        //createImageButton(context, constraintLayout, button.getId());
    }

    public static void customImageButton(ImageButton imageButton) {
        imageButton.setImageResource(R.drawable.twotone_cancel_24);
    }

    public static void createImageButton(Context context, ConstraintLayout constraintLayout, int relatedViewId, OnFieldDeletedListener listener,int switchId) {
        ImageButton imageButton = new ImageButton(context);
        imageButton.setId(View.generateViewId());
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(70, 70);
        imageButton.setLayoutParams(layoutParams);
        customImageButton(imageButton);
        constraintLayout.addView(imageButton);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);
        constraintSet.connect(imageButton.getId(), ConstraintSet.END, relatedViewId, ConstraintSet.END, 8);
        constraintSet.connect(imageButton.getId(), ConstraintSet.TOP, relatedViewId, ConstraintSet.TOP, 8);
        constraintSet.applyTo(constraintLayout);
        imageButton.setOnClickListener(v -> {
            // Lógica para eliminar el TextInputLayout
            View view = getViewById(constraintLayout, relatedViewId);
            int id = getLastFieldAfterLastFieldAdded(constraintLayout, relatedViewId);
            int id2 = getLastFieldBeforeLastFieldAdded(constraintLayout, relatedViewId);
            constraintLayout.removeView(view);
            constraintLayout.removeView(imageButton);
            // Notificar a la Activity o Fragment

            listener.onFieldDeleted("Patatas");
            int previousId = switchId; // O el elemento más cercano antes de la vista eliminada
            for (int i = 0; i < constraintLayout.getChildCount(); i++) {
                View child = constraintLayout.getChildAt(i);
                    ConstraintSet constraintSet2 = new ConstraintSet();
                    constraintSet2.clone(constraintLayout);
                    if(child.getId() == R.id.ib_select_img_create_characterkie){
                        constraintSet2.connect(child.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 24);
                    }else if(child.getId() == R.id.ib_delete_img_create_characterkie){
                        constraintSet2.connect(child.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 58);
                    }
                    constraintSet2.connect(child.getId(), ConstraintSet.TOP, previousId, ConstraintSet.BOTTOM, 16);
                    constraintSet2.applyTo(constraintLayout);
                    previousId = child.getId();
                }

            /*addConstraintsForView(constraintLayout,id, id2);*/
            // Lógica para eliminar el TextInputLayout
          //  View view = getViewById(constraintLayout, relatedViewId);
           // constraintLayout.removeView(view);

// Eliminar también el ImageButton
            //constraintLayout.removeView(imageButton);

// Actualizar las restricciones de los campos siguientes
            //updateConstraintsAfterRemoval(constraintLayout, relatedViewId);

        });
    }
    private static void updateConstraintsAfterRemoval(ConstraintLayout constraintLayout, int removedViewId) {
        // Obtener el ID del último campo agregado antes de la vista eliminada
        int lastFieldId = getLastFieldBeforeSwitch(constraintLayout, removedViewId);

        // Iterar sobre todos los elementos en el layout y actualizar sus restricciones
        for (int i = 0; i < constraintLayout.getChildCount(); i++) {
            View child = constraintLayout.getChildAt(i);

            // Si el elemento está por debajo del eliminado, actualizar su posición
            if (child.getId() != View.NO_ID) {
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(constraintLayout);

                // Actualizar las restricciones: mover hacia arriba para ocupar el espacio vacío
                constraintSet.connect(child.getId(), ConstraintSet.TOP, lastFieldId, ConstraintSet.BOTTOM, 16);
                constraintSet.applyTo(constraintLayout);

                // Actualizar el lastFieldId para los siguientes elementos
                lastFieldId = child.getId();
            }
        }
    }

    public static View getViewById(ViewGroup parent, int viewId) {
        // Obtener la View por su ID
        View view = parent.findViewById(viewId);
        return view;
    }

    private static int getLastFieldBeforeSwitch(ConstraintLayout constraintLayout, int switchId) {
        int lastFieldId = -1;
        for (int i = 0; i < constraintLayout.getChildCount(); i++) {
            View child = constraintLayout.getChildAt(i);
            if (child.getId() == switchId) {
                break;
            }
            if (child instanceof EditText || child instanceof Button || child instanceof TextView || child instanceof TextInputLayout || child instanceof RadioGroup) {
                lastFieldId = child.getId();
            }
        }
        return lastFieldId;
    }
    private static int getLastFieldAfterLastFieldAdded(ConstraintLayout constraintLayout, int lastFieldAddedId) {
        int lastFieldId = -1;
        for (int i = 0; i < constraintLayout.getChildCount(); i++) {
            View child = constraintLayout.getChildAt(i);
            if (child.getId() == lastFieldAddedId) {
                i++;
                child = constraintLayout.getChildAt(i);
                if (child instanceof EditText || child instanceof Button || child instanceof TextView || child instanceof TextInputLayout || child instanceof RadioGroup) {
                    lastFieldId = child.getId();
                }
                break;
            }

        }
        return lastFieldId;
    }
    private static int getLastFieldBeforeLastFieldAdded(ConstraintLayout constraintLayout, int lastFieldAddedId) {
        int lastFieldId = -1;
        for (int i = 0; i < constraintLayout.getChildCount(); i++) {
            View child = constraintLayout.getChildAt(i);
            if (constraintLayout.getChildAt(i+1).getId()==lastFieldAddedId) {
                child = constraintLayout.getChildAt(i);
                if (child instanceof EditText || child instanceof Button || child instanceof TextView || child instanceof TextInputLayout || child instanceof RadioGroup) {
                    lastFieldId = child.getId();
                }
                break;
            }

        }
        return lastFieldId;
    }
    private static void addConstraintsForView(ConstraintLayout constraintLayout, int viewId, int lastFieldId, int switchId) {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);
        if (lastFieldId != -1) {
            constraintSet.connect(viewId, ConstraintSet.TOP, lastFieldId, ConstraintSet.BOTTOM, 16);
        } else {
            constraintSet.connect(viewId, ConstraintSet.TOP, ConstraintLayout.LayoutParams.PARENT_ID, ConstraintSet.TOP, 16);
        }
        constraintSet.connect(viewId, ConstraintSet.START, ConstraintLayout.LayoutParams.PARENT_ID, ConstraintSet.START, 16);
        constraintSet.connect(viewId, ConstraintSet.END, ConstraintLayout.LayoutParams.PARENT_ID, ConstraintSet.END, 16);
        if (switchId != 0) {
            constraintSet.connect(switchId, ConstraintSet.TOP, viewId, ConstraintSet.BOTTOM, 16);
        }
        constraintSet.applyTo(constraintLayout);
    }
    private static void addConstraintsForView(ConstraintLayout constraintLayout, int viewId, int lastFieldId) {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);
        if (lastFieldId != -1) {
            constraintSet.connect(viewId, ConstraintSet.TOP, lastFieldId, ConstraintSet.BOTTOM, 16);
        } else {
            constraintSet.connect(viewId, ConstraintSet.TOP, ConstraintLayout.LayoutParams.PARENT_ID, ConstraintSet.TOP, 16);
        }
        constraintSet.connect(viewId, ConstraintSet.START, ConstraintLayout.LayoutParams.PARENT_ID, ConstraintSet.START, 16);
        constraintSet.connect(viewId, ConstraintSet.END, ConstraintLayout.LayoutParams.PARENT_ID, ConstraintSet.END, 16);
        constraintSet.applyTo(constraintLayout);
    }
    private static int getLastFieldId(ConstraintLayout constraintLayout, int switchId) {
        int lastFieldId = -1;
        for (int i = 0; i < constraintLayout.getChildCount(); i++) {
            View child = constraintLayout.getChildAt(i);
            if (child.getId() == switchId) {
                break;
            }
            if (child instanceof EditText || child instanceof Button || child instanceof TextView || child instanceof TextInputLayout || child instanceof RadioGroup) {
                lastFieldId = child.getId();
            }
        }
        return lastFieldId;
    }
}