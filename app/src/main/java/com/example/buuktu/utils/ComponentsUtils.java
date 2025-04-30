package com.example.buuktu.utils;

import android.content.Context;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;

import com.example.buuktu.R;
import com.example.buuktu.models.FieldItem;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComponentsUtils {
    public static int lastAddedFieldId = -1; // Registro del último campo añadido

    public static void setLastAddedFieldId(int lastAddedFieldId) {
        ComponentsUtils.lastAddedFieldId = lastAddedFieldId;
    }

    public static void customTextInputEditText(TextInputEditText textInputEditText) {
        textInputEditText.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
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

    public static void createComponent(Context context, FieldItem fieldItem, int switchId1, int switchId2, ConstraintLayout constraintLayout) {
        switch (fieldItem.getComponent()) {
            case "TextInputEditText":
                createTextInputEditText(fieldItem.getType(), fieldItem.getName(), context, constraintLayout, switchId1, switchId2);
                break;
            //case "RadioButton":
            //    createRadioButton(fieldItem.getName(), context, constraintLayout, switchId1, switchId2);
            //    break;
            // case "Switch":
        }
    }

    public static void createTextInputEditText(String type, String name, Context context, ConstraintLayout constraintLayout, int switchId1, int switchId2) {

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
            lastAddedFieldId = getLastFieldId(constraintLayout, switchId1);
        }
            addConstraintsForView(constraintLayout, textInputLayout.getId(), lastAddedFieldId, switchId1, switchId2);
        lastAddedFieldId = textInputLayout.getId();
        createImageButton(context, constraintLayout, textInputLayout.getId(), switchId1, switchId2);
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
        addConstraintsForView(constraintLayout, radioGroup.getId(), getLastFieldBeforeSwitch(constraintLayout, radioGroup.getId()), 0, 0);
    }

    public static void customTextView(TextView textView) {
        textView.setTextAlignment(TextInputLayout.TEXT_ALIGNMENT_CENTER);
        textView.setGravity(Gravity.CENTER);
        textView.setEms(10);
    }

    public void createTextView(String name, Context context, ConstraintLayout constraintLayout, int switchId1, int switchId2) {
        TextView textView = new TextView(context);
        textView.setId(View.generateViewId());
        customTextView(textView);
        constraintLayout.addView(textView);
        addConstraintsForView(constraintLayout, textView.getId(), getLastFieldBeforeSwitch(constraintLayout, 0), switchId1, switchId2);
    }

    public static void customButton(Button button) {
        button.setTextAlignment(TextInputLayout.TEXT_ALIGNMENT_CENTER);
        button.setGravity(Gravity.CENTER);
        button.setEms(10);
    }

    public void createButton(String name, Context context, ConstraintLayout constraintLayout, int switchId1, int switchId2) {
        Button button = new Button(context);
        button.setId(View.generateViewId());
        customButton(button);
        constraintLayout.addView(button);
        addConstraintsForView(constraintLayout, button.getId(), getLastFieldBeforeSwitch(constraintLayout, 0), switchId1, switchId2);
        //createImageButton(context, constraintLayout, button.getId());
    }

    public static void customImageButton(ImageButton imageButton) {
        imageButton.setImageResource(R.drawable.twotone_cancel_24);
    }

    public static void createImageButton(Context context, ConstraintLayout constraintLayout, int relatedViewId, int switchId1, int switchId2) {
        ImageButton imageButton = new ImageButton(context);
        imageButton.setId(View.generateViewId());
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(70, 70);
        imageButton.setLayoutParams(layoutParams);
        customImageButton(imageButton);
        imageButton.setTag(relatedViewId);

        constraintLayout.addView(imageButton);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);
        constraintSet.connect(imageButton.getId(), ConstraintSet.END, relatedViewId, ConstraintSet.END, 8);
        constraintSet.connect(imageButton.getId(), ConstraintSet.TOP, relatedViewId, ConstraintSet.TOP, 8);
        constraintSet.applyTo(constraintLayout);
        imageButton.setOnClickListener(v -> {
            // Lógica para eliminar el TextInputLayout
            View view = getViewById(constraintLayout, relatedViewId);
            constraintLayout.removeView(view);
            constraintLayout.removeView(imageButton);
            // Notificar a la Activity o Fragment
            lastAddedFieldId = getLastFieldBeforeSwitch(constraintLayout, 0);

            updateConstraintsAfterRemovalSingle(constraintLayout, relatedViewId, switchId1, switchId2);


        });
    }

    private static void updateConstraintsAfterRemovalSingle(ConstraintLayout layout, int removedViewId, int switchId1, int switchId2) {
        List<Integer> dynamicFieldIds = new ArrayList<>();
        Map<Integer, Integer> fieldButtonMap = new HashMap<>();
        boolean foundName = false;

        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);

            // Excluir botones fijos, y switches
            if (child.getId() == R.id.fb_more_createCharacterkie ||
                    child.getId() == R.id.fb_add_field_createCharacterkie ||
                    child.getId() == R.id.ib_select_img_create_characterkie ||
                    child.getId() == switchId1 || child.getId() == switchId2)
                continue;

            if (child.getId() == R.id.et_nameCharacterkieCreateFull) {
                foundName = true;
                continue;
            }

            if (foundName) {
                boolean isCampo = child instanceof EditText ||
                        child instanceof TextView ||
                        child instanceof TextInputLayout ||
                        child instanceof RadioGroup;
                if (isCampo && child.getId() != removedViewId) {
                    dynamicFieldIds.add(child.getId());
                } else if (child instanceof ImageButton) {
                    // Aquí usamos el tag para asociar el ImageButton con su campo
                    Object tag = child.getTag();
                    if (tag != null && tag instanceof Integer) {
                        int fieldId = (Integer) tag;
                        // Solo agregar si ese campo ya se encuentra en dynamicFieldIds
                        if (dynamicFieldIds.contains(fieldId)) {
                            fieldButtonMap.put(fieldId, child.getId());
                        }
                    }
                }
            }
        }

        ConstraintSet set = new ConstraintSet();
        set.clone(layout);

        if (!dynamicFieldIds.isEmpty()) {
            set.clear(dynamicFieldIds.get(0), ConstraintSet.TOP);
            set.connect(dynamicFieldIds.get(0), ConstraintSet.TOP, R.id.et_nameCharacterkieCreateFull, ConstraintSet.BOTTOM, 16);

            if (fieldButtonMap.containsKey(dynamicFieldIds.get(0))) {
                int buttonId = fieldButtonMap.get(dynamicFieldIds.get(0));
                set.clear(buttonId, ConstraintSet.TOP);
                set.clear(buttonId, ConstraintSet.END);
                set.connect(buttonId, ConstraintSet.TOP, dynamicFieldIds.get(0), ConstraintSet.TOP);
                set.connect(buttonId, ConstraintSet.END, dynamicFieldIds.get(0), ConstraintSet.END, 8);
            }

            for (int i = 1; i < dynamicFieldIds.size(); i++) {
                set.clear(dynamicFieldIds.get(i), ConstraintSet.TOP);
                set.connect(dynamicFieldIds.get(i), ConstraintSet.TOP, dynamicFieldIds.get(i - 1), ConstraintSet.BOTTOM, 16);

                if (fieldButtonMap.containsKey(dynamicFieldIds.get(i))) {
                    int buttonId = fieldButtonMap.get(dynamicFieldIds.get(i));
                    set.clear(buttonId, ConstraintSet.TOP);
                    set.clear(buttonId, ConstraintSet.END);
                    set.connect(buttonId, ConstraintSet.TOP, dynamicFieldIds.get(i), ConstraintSet.TOP);
                    set.connect(buttonId, ConstraintSet.END, dynamicFieldIds.get(i), ConstraintSet.END, 8);
                }
            }
            // Handle switches placement
            if (dynamicFieldIds.size() > 0) {
                set.clear(switchId1, ConstraintSet.TOP);
                set.connect(switchId1, ConstraintSet.TOP, dynamicFieldIds.get(dynamicFieldIds.size() - 1), ConstraintSet.BOTTOM, 8);

                set.clear(switchId2, ConstraintSet.TOP);
                set.connect(switchId2, ConstraintSet.TOP, dynamicFieldIds.get(dynamicFieldIds.size() - 1), ConstraintSet.BOTTOM, 8);
            } else {
                set.clear(switchId1, ConstraintSet.TOP);
                set.connect(switchId1, ConstraintSet.TOP, R.id.et_nameCharacterkieCreateFull, ConstraintSet.BOTTOM, 16);

                set.clear(switchId2, ConstraintSet.TOP);
                set.connect(switchId2, ConstraintSet.TOP, R.id.et_nameCharacterkieCreateFull, ConstraintSet.BOTTOM, 16);
            }


        } else {
            // Handle switches placement when there are no dynamic fields
            set.clear(switchId1, ConstraintSet.TOP);
            set.connect(switchId1, ConstraintSet.TOP, R.id.et_nameCharacterkieCreateFull, ConstraintSet.BOTTOM, 16);

            set.clear(switchId2, ConstraintSet.TOP);
            set.connect(switchId2, ConstraintSet.TOP, R.id.et_nameCharacterkieCreateFull, ConstraintSet.BOTTOM, 16);
        }

        set.applyTo(layout);

        // Código para reubicar los botones fijos y demás elementos (sin cambios)
        ConstraintSet btnSet = new ConstraintSet();
        btnSet.clone(layout);

        btnSet.clear(R.id.fb_more_createCharacterkie, ConstraintSet.END);
        btnSet.clear(R.id.fb_more_createCharacterkie, ConstraintSet.BOTTOM);
        btnSet.connect(R.id.fb_more_createCharacterkie, ConstraintSet.END, ConstraintLayout.LayoutParams.PARENT_ID, ConstraintSet.END, 10);
        btnSet.connect(R.id.fb_more_createCharacterkie, ConstraintSet.BOTTOM, ConstraintLayout.LayoutParams.PARENT_ID, ConstraintSet.BOTTOM, 10);

        btnSet.clear(R.id.fb_add_field_createCharacterkie, ConstraintSet.END);
        btnSet.clear(R.id.fb_add_field_createCharacterkie, ConstraintSet.BOTTOM);
        btnSet.connect(R.id.fb_add_field_createCharacterkie, ConstraintSet.END, ConstraintLayout.LayoutParams.PARENT_ID, ConstraintSet.END, 10);
        btnSet.connect(R.id.fb_add_field_createCharacterkie, ConstraintSet.BOTTOM, R.id.fb_more_createCharacterkie, ConstraintSet.TOP, 10);

        btnSet.clear(R.id.ib_select_img_create_characterkie, ConstraintSet.TOP);
        btnSet.clear(R.id.ib_select_img_create_characterkie, ConstraintSet.START);
        btnSet.connect(R.id.ib_select_img_create_characterkie, ConstraintSet.TOP, ConstraintLayout.LayoutParams.PARENT_ID, ConstraintSet.TOP, 24);
        btnSet.connect(R.id.ib_select_img_create_characterkie, ConstraintSet.START, ConstraintLayout.LayoutParams.PARENT_ID, ConstraintSet.START, 106);

        btnSet.applyTo(layout);
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
                if (i < constraintLayout.getChildCount())
                {
                    child = constraintLayout.getChildAt(i);
                    if (child instanceof EditText || child instanceof Button || child instanceof TextView || child instanceof TextInputLayout || child instanceof RadioGroup) {
                        lastFieldId = child.getId();
                    }
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
            if (i + 1 < constraintLayout.getChildCount() && constraintLayout.getChildAt(i + 1).getId() == lastFieldAddedId) {
                if (child instanceof EditText || child instanceof Button || child instanceof TextView || child instanceof TextInputLayout || child instanceof RadioGroup) {
                    lastFieldId = child.getId();
                }
                break;
            }

        }
        return lastFieldId;
    }

    private static void addConstraintsForView(ConstraintLayout constraintLayout, int viewId, int lastFieldId, int switchId1, int switchId2) {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);
        if (lastFieldId != -1) {
            constraintSet.connect(viewId, ConstraintSet.TOP, lastFieldId, ConstraintSet.BOTTOM, 16);
        } else {
            constraintSet.connect(viewId, ConstraintSet.TOP, R.id.et_nameCharacterkieCreateFull, ConstraintSet.BOTTOM, 16);
        }
        constraintSet.connect(viewId, ConstraintSet.START, ConstraintLayout.LayoutParams.PARENT_ID, ConstraintSet.START, 16);
        constraintSet.connect(viewId, ConstraintSet.END, ConstraintLayout.LayoutParams.PARENT_ID, ConstraintSet.END, 16);

        // Handle switch constraints.
        if (switchId1 != 0 && switchId2 != 0) { // Check if both switchIds are valid.
            constraintSet.clear(switchId1, ConstraintSet.TOP);
            constraintSet.clear(switchId2, ConstraintSet.TOP);

            constraintSet.connect(switchId1, ConstraintSet.TOP, viewId, ConstraintSet.BOTTOM, 8);
            constraintSet.connect(switchId1, ConstraintSet.START, ConstraintLayout.LayoutParams.PARENT_ID, ConstraintSet.START, 16);
            constraintSet.connect(switchId1, ConstraintSet.END, switchId2, ConstraintSet.START, 8);

            constraintSet.connect(switchId2, ConstraintSet.TOP, viewId, ConstraintSet.BOTTOM, 8);
            constraintSet.connect(switchId2, ConstraintSet.END, ConstraintLayout.LayoutParams.PARENT_ID, ConstraintSet.END, 16);
            constraintSet.connect(switchId2, ConstraintSet.START, switchId1, ConstraintSet.END, 8);

        } else if (switchId1 != 0) { //if only switchId1 is valid
            constraintSet.clear(switchId1, ConstraintSet.TOP);
            constraintSet.connect(switchId1, ConstraintSet.TOP, viewId, ConstraintSet.BOTTOM, 16);
            constraintSet.connect(switchId1, ConstraintSet.START, ConstraintLayout.LayoutParams.PARENT_ID, ConstraintSet.START, 16);
            constraintSet.connect(switchId1, ConstraintSet.END, ConstraintLayout.LayoutParams.PARENT_ID, ConstraintSet.END, 16);

        } else if (switchId2 != 0) { //If only switchId2 is valid.
            constraintSet.clear(switchId2, ConstraintSet.TOP);
            constraintSet.connect(switchId2, ConstraintSet.TOP, viewId, ConstraintSet.BOTTOM, 16);
            constraintSet.connect(switchId2, ConstraintSet.START, ConstraintLayout.LayoutParams.PARENT_ID, ConstraintSet.START, 16);
            constraintSet.connect(switchId2, ConstraintSet.END, ConstraintLayout.LayoutParams.PARENT_ID, ConstraintSet.END, 16);
        }

        constraintSet.applyTo(constraintLayout);
    }


    private static void addConstraintsForView(ConstraintLayout constraintLayout, int viewId, int lastFieldId) {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);
        if (lastFieldId != -1) {
            constraintSet.connect(viewId, ConstraintSet.TOP, lastFieldId, ConstraintSet.BOTTOM, 16);
        } else {
            constraintSet.connect(viewId, ConstraintSet.TOP, R.id.et_nameCharacterkieCreateFull, ConstraintSet.BOTTOM, 16);
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
