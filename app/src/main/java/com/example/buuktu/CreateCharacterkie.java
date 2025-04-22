package com.example.buuktu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.DrawableRes;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Switch;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.buuktu.adapters.RoundedBorderSquareTransformation;
import com.example.buuktu.bottomsheet.BottomSheetChooseComponents;
import com.example.buuktu.bottomsheet.BottomSheetProfilePhoto;
import com.example.buuktu.listeners.OnFieldDeletedListener;
import com.example.buuktu.models.CardItem;
import com.example.buuktu.models.FieldItem;
import com.example.buuktu.utils.ComponentsUtils;
import com.example.buuktu.utils.DrawableUtils;
import com.example.buuktu.utils.NavigationUtils;
import com.example.buuktu.views.MainActivity;
import com.example.buuktu.views.WorldkieMenu;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateCharacterkie#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateCharacterkie extends Fragment implements View.OnClickListener, OnFieldDeletedListener {
    FragmentManager fragmentManager;
    FragmentActivity activity;
    ImageButton ib_select_img_create_characterkie,ib_back,ib_save;
    Uri image;
    BottomSheetProfilePhoto bottomSheetProfilePhoto;
    String source;
    Switch tb_characterkiePrivacity,tb_characterkieDraft;
    List<FieldItem> fieldsAdded = new ArrayList<>();
    List<CardItem> cardItems = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference fieldkiesRef = db.collection("Fieldkies");
    List<FieldItem> fieldsNotAdded = new ArrayList<>();
    DocumentReference characterRef;
    TextInputEditText textInputEditText;
    TextInputLayout et_nameCharacterkieCreateFull;
    ConstraintLayout constraintLayout;
    Context context;
    FloatingActionButton fb_add_field_createCharacterkie,fb_more_createCharacterkie;
    public CreateCharacterkie() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CreateCharacterkie.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateCharacterkie newInstance() {
        return new CreateCharacterkie();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_create_characterkie, container, false);
        MainActivity mainActivity = (MainActivity) getActivity();
        ib_back = mainActivity.getBackButton();
        ib_back.setVisibility(View.VISIBLE);
        ib_save = mainActivity.getIb_save();
        ib_save.setVisibility(View.VISIBLE);
        context = getContext();

        ComponentsUtils.setLastAddedFieldId(-1);
        fragmentManager = requireActivity().getSupportFragmentManager();
        activity = requireActivity();
        bottomSheetProfilePhoto = new BottomSheetProfilePhoto();
        // characterRef = db.collection("CharacterKies").document(characterId);
        getFields();
        fieldsNotAdded.add(new FieldItem("EditText","Characterky","Texto",R.drawable.sharp_emoji_nature_24));
        initComponents(view);
        setListeners();
        try {
            putDefaultImage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return view;
    }
    private void initComponents(View view){
        ib_select_img_create_characterkie = view.findViewById(R.id.ib_select_img_create_characterkie);
        tb_characterkieDraft=view.findViewById(R.id.tb_characterkieDraft);
        tb_characterkiePrivacity=view.findViewById(R.id.tb_CharacterkiePrivacity);
        fb_add_field_createCharacterkie = view.findViewById(R.id.fb_add_field_createCharacterkie);
        fb_more_createCharacterkie = view.findViewById(R.id.fb_more_createCharacterkie);
        textInputEditText = view.findViewById(R.id.et_nameCharacterkieCreate);
        et_nameCharacterkieCreateFull = view.findViewById(R.id.et_nameCharacterkieCreateFull);
        constraintLayout = view.findViewById(R.id.constraint_create_characterkie);
        source = "app";
        tb_characterkieDraft.setVisibility(View.GONE);
        tb_characterkiePrivacity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    tb_characterkieDraft.setVisibility(View.VISIBLE);
                }else{
                    tb_characterkieDraft.setVisibility(View.GONE);
                }
            }
        });
    }
    private void setListeners(){
        ib_select_img_create_characterkie.setOnClickListener(this);
        ib_save.setOnClickListener(this);
        fb_add_field_createCharacterkie.setOnClickListener(this);
        fb_more_createCharacterkie.setOnClickListener(this);
    }
    public Drawable getSelectedProfilePhoto()
    {
        return ib_select_img_create_characterkie.getDrawable();
    }
    public void setSource(String source) {
        this.source = source;
    }
    public void setSelectedProfilePhoto(@DrawableRes int imageResId){
        int cornerRadius = 150 / 6;
        int borderWidth = 7;
        int borderColor = ContextCompat.getColor(getContext(), R.color.brownMaroon);

        RequestOptions requestOptions = new RequestOptions()
                //.override(150, 150)
                .centerCrop()
                .transform(new RoundedBorderSquareTransformation(cornerRadius, borderWidth, borderColor));

        Glide.with(getContext())
                .load(imageResId) // 👍 Esto sí pasa por la transformación
                .apply(requestOptions)
                .into(ib_select_img_create_characterkie);
    }
    private void putDefaultImage() throws IOException {
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.photocharacterkieone);
        DrawableUtils.personalizarImagenCircleButton(getContext(),DrawableUtils.drawableToBitmap(drawable),ib_select_img_create_characterkie,R.color.brownMaroon);
    }
    public void setSelectedProfilePhoto(Drawable image){
        ib_select_img_create_characterkie.setImageDrawable(image);
    }

    public String getSource() {
        return source;
    }
    public void setImageUri(Uri image){
        this.image=image;
    }
    public void selectImage(){
        bottomSheetProfilePhoto.show(getChildFragmentManager(),"BottomSheetProfilePhoto");
    }
    public ImageButton getIb_select_img_create_worldkie() {
        return ib_select_img_create_characterkie;
    }
    /* private void getCharacterkieFields(){
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
       }*/
    private void getFields(){
        fieldkiesRef.whereEqualTo("kie", "stuffkie").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots == null || queryDocumentSnapshots.isEmpty()) {
                        Log.e("Firestore", "No se encontraron documentos en Fieldkies");
                        return;
                    }
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) { // ← ¡Aquí está el cambio!
                        String component = document.getString("component");
                        Log.e("Firestore", component.toString());
                        String kie = document.getString("kie");
                        List<String> options = (List<String>) document.get("options");
                        String type = document.getString("type");
                        String iconName = document.getString("icon");
                        int icon = getResources().getIdentifier(iconName, "drawable", context.getPackageName());

                        String uid = document.getId();

                        // Solo agrega los campos que no han sido añadidos

                        if (!fieldsAdded.contains(uid)) {
                            FieldItem fieldItem=null;
                            if (component.equals("RadioButton")) {
                                fieldItem = new FieldItem(component, kie, options,icon);
                            }
                            else if (component.equals("EditText")){
                                fieldItem = new FieldItem(component, kie, type,icon);
                            }
                            fieldsNotAdded.add(fieldItem);
                        }
                    }
                    cargarCamposEnBottomSheet(fieldsNotAdded);
                }).addOnFailureListener(e -> {
                    Log.e("Firestore", "Error al obtener los campos", e);
                });
    }
    public void cargarCamposEnBottomSheet(List<FieldItem> camposList) {
        // Recorrer los campos y añadirlos al BottomSheet

    }

    public void removeFieldBottomSheet(FieldItem item){
        fieldsNotAdded.remove(item);
    }
    private void actualizarUIConCampo(FieldItem fieldItem) {
        // Dependiendo del tipo de campo, añade la vista correcta
        View nuevoCampo;
        if (fieldItem.getComponent().equals("TextInputEditText")) {
            //ComponentsUtils.createComponent(this,fieldItem,R.id.tb_CharacterkiePrivacity,constraintLayout);
        } else if (fieldItem.getComponent().equals("RadioButton")) {
            RadioButton radioButton = new RadioButton(context);
            radioButton.setText(fieldItem.getName());
            nuevoCampo = radioButton;
        } else {
            return;
        }

        // Agregar el nuevo campo al layout
        // constraintLayout.addView(nuevoCampo);
    }
    private void actualizarBottomSheetConCampo(FieldItem fieldItem) {
        // Dependiendo del tipo de campo, añade la vista correcta
        View nuevoCampo;
        if (fieldItem.getComponent().equals("TextInputEditText")) {
            ComponentsUtils.createComponent(context,fieldItem,R.id.tb_CharacterkiePrivacity,constraintLayout);
        } else if (fieldItem.getComponent().equals("RadioButton")) {
            RadioButton radioButton = new RadioButton(context);
            radioButton.setText(fieldItem.getName());
            nuevoCampo = radioButton;
        } else {
            return;
        }

        // Agregar el nuevo campo al layout
        // constraintLayout.addView(nuevoCampo);
    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.ib_back){
            NavigationUtils.goBack(fragmentManager,activity);
        } else if (v.getId()==R.id.ib_save) {
            /*if(worldkie_id == null){
                    addDataToFirestore();
                }else{
                    editDataFirestore();
                }*/
        } else if (v.getId()==R.id.ib_select_img_create_characterkie) {
            selectImage();
        } else if (v.getId()==R.id.fb_add_field_createCharacterkie) {
            BottomSheetChooseComponents bottomSheetFragment = new BottomSheetChooseComponents(context, constraintLayout, this, fieldsNotAdded);
            bottomSheetFragment.show(getChildFragmentManager(), bottomSheetFragment.getTag());
        }
    }

    @Override
    public void onFieldDeleted(FieldItem fieldItem) {
        if (!fieldsAdded.contains(fieldItem)) {
            fieldsAdded.add(fieldItem);
            fieldsNotAdded.remove(fieldItem);
            actualizarUIConCampo(fieldItem);
        }
    }
}