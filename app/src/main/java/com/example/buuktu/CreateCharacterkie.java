package com.example.buuktu;

import static android.widget.Toast.LENGTH_LONG;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.buuktu.bottomsheet.BottomSheetChooseBirthday;
import com.example.buuktu.bottomsheet.BottomSheetChooseGender;
import com.example.buuktu.bottomsheet.BottomSheetChoosePronouns;
import com.example.buuktu.bottomsheet.BottomSheetProfilePhoto;
import com.example.buuktu.models.CardItem;
import com.example.buuktu.models.Characterkie;
import com.example.buuktu.models.FieldItem;
import com.example.buuktu.utils.DrawableUtils;
import com.example.buuktu.utils.EfectsUtils;
import com.example.buuktu.utils.NavigationUtils;
import com.example.buuktu.utils.RoundedBorderSquareTransformation;
import com.example.buuktu.views.MainActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateCharacterkie#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateCharacterkie extends Fragment implements View.OnClickListener {
    FragmentManager fragmentManager;
    FragmentActivity activity;
    Button bt_birthday_characterkie,bt_pronouns_characterkie,bt_gender_characterkie;
    ImageButton ib_select_img_create_characterkie,ib_back,ib_save;
    Uri image;
    BottomSheetDialogFragment bottomSheetProfilePhoto;
    BottomSheetChoosePronouns bottomSheetChoosePronouns;
    BottomSheetChooseGender bottomSheetChooseGender;
    Switch tb_characterkiePrivacity,tb_characterkieDraft;
    List<FieldItem> fieldsAdded = new ArrayList<>();
    List<CardItem> cardItems = new ArrayList<>();
    FirebaseFirestore db;
    CollectionReference characterkieCollection,fieldkiesRef;
    List<FieldItem> fieldsNotAdded = new ArrayList<>();
    DocumentReference characterRef;
    TextInputEditText textInputEditText,et_nameCharacterkieCreate;
    FirebaseAuth firebaseAuth;
    TextInputLayout et_nameCharacterkieCreateFull;
    ConstraintLayout constraintLayout;
    Context context;
    String UID, worldkie_id, source,name,characterkie_id;
    boolean privacity, draft,isAllFabsVisible;
    Characterkie characterkie;
    private int ultimoIdAñadido = R.id.tb_CharacterkiePrivacity; // Empezar debajo del título
    private int optionPronouns, optionBirthday,optionGender;
    String optionPronounsString, optionBirthdayString,optionGenderString;
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
            this.worldkie_id = getArguments().getString("worldkie_id");
            this.characterkie_id = getArguments().getString("characterkie_id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_create_characterkie, container, false);
        MainActivity mainActivity = (MainActivity) getActivity();
        ib_back = mainActivity.getBackButton();
        ib_save = mainActivity.getIb_save();
        context = getContext();
        firebaseAuth = FirebaseAuth.getInstance();
        UID = firebaseAuth.getUid();
        db = FirebaseFirestore.getInstance();
        fragmentManager = requireActivity().getSupportFragmentManager();
        activity = requireActivity();
        characterkie = new Characterkie();
        bottomSheetProfilePhoto = new BottomSheetProfilePhoto();
        initComponents(view);
        setListeners();
        //fieldsNotAdded.add(new FieldItem("EditText","Characterky","Texto",R.drawable.sharp_emoji_nature_24));
        if(characterkie_id == null){
            try {
                createMode();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else{
            characterkieCollection.document(characterkie_id).addSnapshotListener((queryDocumentSnapshot, e) -> {
                if (e != null) {
                    Log.e("Error", e.getMessage());
                    Toast.makeText(getContext(), "Error al escuchar cambios: " + e.getMessage(), LENGTH_LONG).show();
                    return;
                }
                if (queryDocumentSnapshot.exists()) {
                    String name = queryDocumentSnapshot.getString("name");
                    characterkie.setName(name);
                    boolean characterkie_private = queryDocumentSnapshot.getBoolean("characterkie_private");
                    characterkie.setCharacterkiePrivate(characterkie_private);
                    tb_characterkiePrivacity.setChecked(characterkie_private);
                    if(characterkie_private) {
                        boolean draft = queryDocumentSnapshot.getBoolean("draft");
                        characterkie.setDraft(draft);
                        tb_characterkieDraft.setChecked(draft);
                        tb_characterkieDraft.setVisibility(View.VISIBLE);
                    }
                    boolean photo_default = queryDocumentSnapshot.getBoolean("photo_default");
                    characterkie.setPhoto_default(photo_default);
                    et_nameCharacterkieCreate.setText(name);
                    getImage();
                }
            });

            editMode(characterkie);
        }
        try {
            putDefaultImage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return view;
    }
    private void initVisibility(){
        ib_back.setVisibility(View.VISIBLE);
        ib_save.setVisibility(View.VISIBLE);
    }
    private void getImage(){
        if(characterkie.isPhoto_default()){
            characterkieCollection.document(characterkie_id).addSnapshotListener((queryDocumentSnapshot, e) -> {
                       /* if (e != null) {
                            Log.e("Error", e.getMessage());
                            Toast.makeText(getContext(), "Error al escuchar cambios: " + e.getMessage(), LENGTH_LONG).show();
                            return;
                        }*/
                String id_photo = queryDocumentSnapshot.getString("id_photo");
                int resId = getContext().getResources().getIdentifier(id_photo, "mipmap", getContext().getPackageName());

                if (resId != 0) {
                    Drawable drawable = ContextCompat.getDrawable(getContext(), resId);
                    ib_select_img_create_characterkie.setImageDrawable(drawable);
                    source = "app";
                    ib_select_img_create_characterkie.setTag(DrawableUtils.getMipmapName(context,resId));

                    try {
                        DrawableUtils.personalizarImagenCuadradoButton(getContext(),150/7,7,R.color.brownMaroon,drawable, ib_select_img_create_characterkie);
                        ib_select_img_create_characterkie.setVisibility(View.VISIBLE);
                        EfectsUtils.startCircularReveal(drawable,ib_select_img_create_characterkie);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
        } else {
            StorageReference userFolderRef = FirebaseStorage.getInstance("gs://buuk-tu-worldkies").getReference(worldkie_id);

            userFolderRef.listAll().addOnSuccessListener(listResult -> {
                for (StorageReference item : listResult.getItems()) {
                    if (item.getName().startsWith("cover")) {
                        item.getDownloadUrl().addOnSuccessListener(uri -> {
                            DrawableUtils.personalizarImagenCuadradoButton(getContext(),150/6,7,R.color.brownMaroon,uri,ib_select_img_create_characterkie);
                            ib_select_img_create_characterkie.setVisibility(View.VISIBLE);
                            EfectsUtils.startCircularReveal(ib_select_img_create_characterkie.getDrawable(),ib_select_img_create_characterkie);
                            source = "device";
                        });
                    }
                }
                ;
            });
        }
    }
    public void createMode() throws IOException {
        et_nameCharacterkieCreate.setText("");
        tb_characterkiePrivacity.setChecked(false);
        tb_characterkieDraft.setVisibility(View.GONE);
        putDefaultImage();
        source = "app";
        optionPronouns = R.id.rb_other_characterkie;
        optionBirthday = R.id.rb_unknown_birthday;
        optionGender = R.id.rb_unknown_gender_characterkie;
        ib_select_img_create_characterkie.setTag(DrawableUtils.getMipmapName(context,R.mipmap.photoworldkieone));

    }

    public int getOptionPronouns() {
        return optionPronouns;
    }

    public void setOptionPronouns(int optionPronouns) {
        this.optionPronouns = optionPronouns;
    }

    public int getOptionBirthday() {
        return optionBirthday;
    }

    public void setOptionBirthday(int optionBirthday) {
        this.optionBirthday = optionBirthday;
    }

    public int getOptionGender() {
        return optionGender;
    }

    public void setOptionGender(int optionGender) {
        this.optionGender = optionGender;
    }

    public String getOptionPronounsString() {
        return optionPronounsString;
    }

    public void setOptionPronounsString(String optionPronounsString) {
        this.optionPronounsString = optionPronounsString;
    }

    public String getOptionBirthdayString() {
        return optionBirthdayString;
    }

    public void setOptionBirthdayString(String optionBirthdayString) {
        this.optionBirthdayString = optionBirthdayString;
    }

    public String getOptionGenderString() {
        return optionGenderString;
    }

    public void setOptionGenderString(String optionGenderString) {
        this.optionGenderString = optionGenderString;
    }

    private void initComponents(View view){
        ib_select_img_create_characterkie = view.findViewById(R.id.ib_select_img_create_characterkie);
        tb_characterkieDraft=view.findViewById(R.id.tb_characterkieDraft);
        tb_characterkiePrivacity=view.findViewById(R.id.tb_CharacterkiePrivacity);
        bt_birthday_characterkie = view.findViewById(R.id.bt_birthday_characterkie);
        bt_pronouns_characterkie = view.findViewById(R.id.bt_pronouns_characterkie);
        bt_gender_characterkie = view.findViewById(R.id.bt_gender_characterkie);
        et_nameCharacterkieCreate = view.findViewById(R.id.et_nameCharacterkieCreate);
        et_nameCharacterkieCreateFull = view.findViewById(R.id.et_nameCharacterkieCreateFull);
        constraintLayout = view.findViewById(R.id.constraint_create_characterkie);
        source = "app";
        tb_characterkieDraft.setVisibility(View.INVISIBLE);
        tb_characterkiePrivacity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    tb_characterkieDraft.setVisibility(View.VISIBLE);
                }else{
                    tb_characterkieDraft.setVisibility(View.INVISIBLE);
                }
            }
        });
        characterkieCollection = db.collection("Characterkies");
        fieldkiesRef = db.collection("Fieldkies");
        initVisibility();
    }
    private void setListeners(){
        ib_select_img_create_characterkie.setOnClickListener(this);
        ib_save.setOnClickListener(this);
        bt_birthday_characterkie.setOnClickListener(this);
        bt_pronouns_characterkie.setOnClickListener(this);
        bt_gender_characterkie.setOnClickListener(this);
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
    public void editMode(Characterkie characterkie){
        et_nameCharacterkieCreate.setText(characterkie.getName());
        tb_characterkiePrivacity.setChecked(characterkie.isCharacterkiePrivate());

        if(!characterkie.isCharacterkiePrivate()){
            tb_characterkiePrivacity.setVisibility(View.GONE);
        }
        //obtenerImagen();
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
        } else if(v.getId()==R.id.bt_birthday_characterkie){
            BottomSheetChooseBirthday bottomSheetChooseBirthday = new BottomSheetChooseBirthday(optionBirthday);
            bottomSheetChooseBirthday.show(getChildFragmentManager(), bottomSheetChooseBirthday.getTag());
        } else if (v.getId()==R.id.bt_pronouns_characterkie) {
            bottomSheetChoosePronouns = new BottomSheetChoosePronouns(optionPronouns);
            bottomSheetChoosePronouns.show(getChildFragmentManager(), bottomSheetChoosePronouns.getTag());
        } else if (v.getId()==R.id.bt_gender_characterkie) {
            bottomSheetChooseGender = new BottomSheetChooseGender(optionGender);
            bottomSheetChooseGender.show(getChildFragmentManager(), bottomSheetChooseGender.getTag());
        }
    }
}