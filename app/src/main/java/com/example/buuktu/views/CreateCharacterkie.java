package com.example.buuktu.views;

import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.ToggleButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.buuktu.R;
import com.example.buuktu.bottomsheet.BottomSheetChooseComponents;
import com.example.buuktu.listeners.OnFieldDeletedListener;
import com.example.buuktu.models.FieldItem;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class CreateCharacterkie extends AppCompatActivity implements OnFieldDeletedListener {
    List<String> fieldsAdded = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference fieldkiesRef = db.collection("Fieldkies");
    List<FieldItem> fieldsNotAdded = new ArrayList<>();
    DocumentReference characterRef;
    TextInputEditText textInputEditText;
    TextInputLayout et_nameCharacterkieCreateFull;
    Switch tb_CharacterkiePrivacity;
    ConstraintLayout constraintLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_characterkie);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        textInputEditText = findViewById(R.id.et_nameCharacterkieCreate);
        tb_CharacterkiePrivacity = findViewById(R.id.tb_CharacterkiePrivacity);
        et_nameCharacterkieCreateFull = findViewById(R.id.et_nameCharacterkieCreateFull);
        constraintLayout = findViewById(R.id.constraint_create_characterkie);

        // characterRef = db.collection("CharacterKies").document(characterId);

    }
 /*   private void getCharacterkieFields(){
        characterRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Map<String, Object> characterData = documentSnapshot.getData();
                List<Map<String, Object>> fields = (List<Map<String, Object>>) characterData.get("fields");

                // Buscar el campo "mascotas" y recuperar sus valores
                for (Map<String, Object> field : fields) {
                    String fieldId = (String) field.get("fieldId");
                    Object value = field.get("value");
                    if (value instanceof Map) {
                        // Es un campo con opciones como "Otros"
                        Map<String, Object> valueMap = (Map<String, Object>) value;
                        String selected = (String) valueMap.get("selected");
                        String custom = (String) valueMap.get("custom");
                        // Usar estos valores para mostrar el campo "Otros" con el valor personalizado
                    } else {
                        // Es un valor simple (String, número, etc.)
                        String simpleValue = (String) value;
                        // Mostrar el valor correspondiente en el UI
                    }
                    if (fieldId.equals("mascotas")) {
                        if (value instanceof List) {
                            List<Map<String, Object>> mascotas = (List<Map<String, Object>>) value;

                            // Ahora procesamos las mascotas
                            for (Map<String, Object> mascota : mascotas) {
                                if (mascota.containsKey("uid")) {
                                    // Mascota registrada, obtenemos el uid y buscamos su información
                                    String mascotaUid = (String) mascota.get("uid");
                                    // Aquí puedes hacer una consulta para obtener la información de la mascota registrada
                                    getMascotaByUid(mascotaUid); // Este método recuperaría los datos de la mascota desde la base de datos
                                } else if (mascota.containsKey("nombre")) {
                                    // Mascota no registrada, solo mostramos su nombre
                                    String nombreMascota = (String) mascota.get("nombre");
                                    // Muestra el nombre de la mascota en la UI
                                    Log.d("Mascota No Registrada", nombreMascota); // Muestra el nombre
                                }
                            }
                        }
                    }
                }
            }
    }
    private void getFields(){
        fieldkiesRef.whereEqualTo("kie", "stuffkie").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                            if (queryDocumentSnapshots == null || queryDocumentSnapshots.isEmpty()) {
                                Log.e("Firestore", "No se encontraron documentos en Fieldkies");
                                return;
                            }
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots.getDocuments()) { // ← ¡Aquí está el cambio!
                                String component = document.getString("component");
                                String kie = document.getString("kie");
                                List<String> options = (List<String>) document.get("options");
                                String type = document.getString("type");
                                String uid = document.getId();
                        // Solo agrega los campos que no han sido añadidos
                                FieldItem fieldItem;
                                if (component.equals("RadioButton")) {
                                    FieldItem campo = new FieldItem(component, kie, options);
                                } else if (component.equals("")){
                                    FieldItem campo = new FieldItem(component, kie, type);
                                }
                                if (!fieldsAdded.contains(uid)) {

                            camposList.add(campo);
                        }
                    }
    }
    cargarCamposEnBottomSheet(camposList);
    })
            .addOnFailureListener(e -> {
        Log.e("Firestore", "Error al obtener los campos", e);
    });
}*/
/*public void createRadioButton(List<String> options) {
    // Crear los RadioButtons dinámicamente y añadirlos al BottomSheet
    for (String option : options) {
        RadioButton radioButton = new RadioButton(getContext());
        radioButton.setText(option);
        radioButton.setButtonDrawable(R.drawable.radiobutton_custom);
       // ColorStateList colorStateList = new ColorStateList();
      //  radioButton.setButtonTintList(ColorStateList.valueOf(R.color.brownBrown));
        RadioGroup radioGroup = new RadioGroup(getContext());
        // Añadir al BottomSheet o a un contenedor específico
        bottomSheetContainer.addView(radioGroup);
    }
}
public void createTextInputEditText(String type) {
    // Crear un EditText según el tipo de dato (Decimal, etc.)
    TextInputEditText textInputEditText = new TextInputEditText(this);
    TextInputLayout textInputLayout = new TextInputLayout(this);
    textInputLayout.setHint(this.getResources().getText(R.string.project_id));
    textInputLayout.setCounterEnabled(true);
    float radioEsquinasDP = 58f;  // Radio de las esquinas en dp
    float radioEsquinasPx = convertDpToPx(radioEsquinasDP);
    textInputLayout.setStartIconTintList(ContextCompat.getColorStateList(this, R.color.black)); // Reemplaza con el color deseado
    textInputEditText.setHintTextColor(ContextCompat.getColorStateList(this, R.color.black));
    // Establecer el radio de las esquinas de TextInputLayout (todas las esquinas)
    textInputLayout.setBoxCornerRadius(radioEsquinasPx);
    int LayoutParams = ConstraintLayout.LayoutParams.MATCH_PARENT;
    int LayoutPa = ConstraintLayout.LayoutParams.WRAP_CONTENT;
    textInputLayout.setLengthCounter(new TextInputLayout.LengthCounter() {
        @Override
        public int countLength(@Nullable Editable text) {
            return 0;
        }
    });
    textInputEditText.setTextAlignment(TextInputLayout.TEXT_ALIGNMENT_CENTER);
    textInputEditText.setGravity(Gravity.CENTER);
    textInputEditText.setWidth(LayoutPa);
    // EditText.
    if (type.equals("Decimal")) {
        textInputEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
    } else if (type.equals("String")) {
        textInputEditText.setInputType(InputType.TYPE_CLASS_TEXT);
    }
    textInputLayout.addView(textInputEditText);
    // Añadir al BottomSheet o a un contenedor específico
    bottomSheetContainer.addView(editText);
}*/
/*public void cargarCamposEnBottomSheet(List<Campo> camposList) {
    // Recorrer los campos y añadirlos al BottomSheet
    for (Campo campo : camposList) {
        if (campo.getComponent().equals("RadioButton")) {
            // Crear RadioButtons con las opciones
            createRadioButton(campo.getOptions());
        } else if (campo.getComponent().equals("EditText")) {
            // Crear EditText según el tipo de dato
            createEditText(campo.getType());
        }
    }
}*/

    public void openBottomSheet(View view) {
        BottomSheetChooseComponents bottomSheetFragment = new BottomSheetChooseComponents(this,constraintLayout,this);
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
    }

    @Override
    public void onFieldDeleted(String fieldName) {
        fieldsAdded.remove(fieldName);
       // fieldsNotAdded.add(fieldName);
    }
    private void actualizarBottomSheet() {
        // Lógica para actualizar el BottomSheet
    }
}