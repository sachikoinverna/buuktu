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
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.buuktu.bottomsheet.BottomSheetChooseBirthday;
import com.example.buuktu.bottomsheet.BottomSheetChooseGender;
import com.example.buuktu.bottomsheet.BottomSheetChoosePronouns;
import com.example.buuktu.bottomsheet.BottomSheetChooseState;
import com.example.buuktu.bottomsheet.BottomSheetProfilePhoto;
import com.example.buuktu.dialogs.CreateEditGeneralDialog;
import com.example.buuktu.models.Characterkie;
import com.example.buuktu.utils.DrawableUtils;
import com.example.buuktu.utils.EfectsUtils;
import com.example.buuktu.utils.NavigationUtils;
import com.example.buuktu.utils.RoundedBorderSquareTransformation;
import com.example.buuktu.views.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateCharacterkie#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateCharacterkie extends Fragment implements View.OnClickListener {
    FragmentManager fragmentManager;
    FragmentActivity activity;
    //ImageView
    TextView tv_basic_info_characterkies,tv_birthday_characterkie,tv_status_characterkie,tv_pronouns_characterkie,tv_gender_characterkie;
    Button bt_birthday_characterkie,bt_pronouns_characterkie,bt_gender_characterkie,bt_state_characterkie;
ImageButton bt_basic_info_characterkies;
    ImageButton ib_select_img_create_characterkie,ib_back,ib_save;
    Uri image;
    BottomSheetDialogFragment bottomSheetProfilePhoto;
    BottomSheetChoosePronouns bottomSheetChoosePronouns;
    BottomSheetChooseGender bottomSheetChooseGender;
    BottomSheetChooseState bottomSheetChooseState;
    Switch tb_characterkiePrivacity,tb_characterkieDraft;
    FirebaseFirestore db;
    CollectionReference characterkieCollection,fieldkiesRef;
    DocumentReference characterRef;
    TextInputEditText textInputEditText,et_nameCharacterkieCreate;
    FirebaseAuth firebaseAuth;
    TextInputLayout et_nameCharacterkieCreateFull;
    ConstraintLayout constraintLayout;
    Context context;
    String UID, worldkie_id, source,name,characterkie_id, userkie_id;
    boolean privacity, draft,isAllFabsVisible,isBasicInfoVisible=false;
    Characterkie characterkie;
    private int optionPronouns, optionBirthday,optionGender,optionStatus;
    String optionPronounsString, optionBirthdayString,optionGenderString,optionStatusString;
    int year,day,month;
    CreateEditGeneralDialog dialog;
    private final FirebaseStorage storage = FirebaseStorage.getInstance("gs://buuk-tu-characterkies");

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
            this.userkie_id = getArguments().getString("userkie_id");
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
        dialog = new CreateEditGeneralDialog(getContext());

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
                    characterkie = Characterkie.fromSnapshot(queryDocumentSnapshot);

                    tb_characterkiePrivacity.setChecked(characterkie.isCharacterkie_private());
                    if(characterkie.isCharacterkie_private()) {
                        tb_characterkieDraft.setChecked(draft);
                        tb_characterkieDraft.setVisibility(View.VISIBLE);
                    }
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
        setOptionStatusString(optionStatusString);
        setOptionGenderString(optionGenderString);
        setOptionPronounsString(optionPronounsString);
        setOptionBirthdayString(optionBirthdayString);
        return view;
    }

    public void setOptionPronouns(int optionPronouns) {
        this.optionPronouns = optionPronouns;
    }

    public void setOptionBirthday(int optionBirthday) {
        this.optionBirthday = optionBirthday;
    }

    public void setOptionGender(int optionGender) {
        this.optionGender = optionGender;
    }

    public void setOptionStatus(int optionStatus) {
        this.optionStatus = optionStatus;
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
        optionPronouns = R.id.rb_unknown_pronouns_characterkie;
        optionBirthday = R.id.rb_unknown_birthday;
        optionGender = R.id.rb_unknown_gender_characterkie;
        optionStatus = R.id.rb_unknown_status_characterkie;
        optionPronounsString = getOptionTextByRadioButtonId(optionPronouns,R.layout.choose_pronouns_dialog);
        optionGenderString = getOptionTextByRadioButtonId(optionGender,R.layout.choose_gender_dialog);
        optionStatusString = getOptionTextByRadioButtonId(optionStatus,R.layout.choose_status_dialog);
        ib_select_img_create_characterkie.setTag(DrawableUtils.getMipmapName(context,R.mipmap.photoworldkieone));

    }


    public void setOptionPronounsString(String optionPronounsString) {
        this.optionPronounsString = optionPronounsString;
        bt_pronouns_characterkie.setText(optionPronounsString);
        characterkie.setPronouns(optionPronounsString);
    }

    public String getOptionStatusString() {
        return optionStatusString;
    }

    public void setOptionStatusString(String optionStatusString) {
        this.optionStatusString = optionStatusString;
        bt_state_characterkie.setText(optionStatusString);
        characterkie.setStatus(optionPronounsString);

    }
    public void setOptionBirthdayString(String optionBirthdayString) {
        this.optionBirthdayString = optionBirthdayString;
        bt_birthday_characterkie.setText(optionBirthdayString);

        characterkie.setBirthday(optionBirthdayString);
    }

    public String getOptionGenderString() {
        return optionGenderString;
    }

    public void setOptionGenderString(String optionGenderString) {
        this.optionGenderString = optionGenderString;
        bt_gender_characterkie.setText(optionGenderString);
        characterkie.setGender(optionPronounsString);

    }

    private void initComponents(View view){
        ib_select_img_create_characterkie = view.findViewById(R.id.ib_select_img_create_characterkie);
        tb_characterkieDraft=view.findViewById(R.id.tb_characterkieDraft);
        tb_characterkiePrivacity=view.findViewById(R.id.tb_CharacterkiePrivacity);
        bt_birthday_characterkie = view.findViewById(R.id.bt_birthday_characterkie);
        bt_pronouns_characterkie = view.findViewById(R.id.bt_pronouns_characterkie);
        bt_gender_characterkie = view.findViewById(R.id.bt_gender_characterkie);
        bt_state_characterkie = view.findViewById(R.id.bt_state_characterkie);
        et_nameCharacterkieCreate = view.findViewById(R.id.et_nameCharacterkieCreate);
        et_nameCharacterkieCreateFull = view.findViewById(R.id.et_nameCharacterkieCreateFull);
        tv_birthday_characterkie = view.findViewById(R.id.tv_birthday_characterkie);
        tv_status_characterkie = view.findViewById(R.id.tv_status_characterkie);

        tv_gender_characterkie = view.findViewById(R.id.tv_gender_characterkie);

        tv_pronouns_characterkie = view.findViewById(R.id.tv_pronouns_characterkie);

        constraintLayout = view.findViewById(R.id.constraint_create_characterkie);
        tv_basic_info_characterkies = view.findViewById(R.id.tv_basic_info_characterkies);
        bt_basic_info_characterkies = view.findViewById(R.id.bt_basic_info_characterkies);
        hideBasicInfo();
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
    private String getOptionTextByRadioButtonId(int id, int idLayout){
        View view = LayoutInflater.from(context).inflate(idLayout, null);
        RadioButton radioButton = view.findViewById(id);
        return radioButton.getText().toString();
    }
    private void setListeners(){
        ib_select_img_create_characterkie.setOnClickListener(this);
        ib_save.setOnClickListener(this);
        bt_birthday_characterkie.setOnClickListener(this);
        bt_pronouns_characterkie.setOnClickListener(this);
        bt_gender_characterkie.setOnClickListener(this);
        bt_state_characterkie.setOnClickListener(this);
        bt_basic_info_characterkies.setOnClickListener(this);
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
    public void editMode(Characterkie characterkie){
        et_nameCharacterkieCreate.setText(characterkie.getName());
        tb_characterkiePrivacity.setChecked(characterkie.isCharacterkie_private());

        if(!characterkie.isCharacterkie_private()){
            tb_characterkiePrivacity.setVisibility(View.GONE);
        }
    }
    private void showHideBasicInfo(){
        if(!isBasicInfoVisible){
            bt_birthday_characterkie.setVisibility(View.VISIBLE);
            bt_gender_characterkie.setVisibility(View.VISIBLE);
            bt_pronouns_characterkie.setVisibility(View.VISIBLE);
            bt_state_characterkie.setVisibility(View.VISIBLE);
            tv_status_characterkie.setVisibility(View.VISIBLE);
            tv_pronouns_characterkie.setVisibility(View.VISIBLE);
            tv_gender_characterkie.setVisibility(View.VISIBLE);
            tv_birthday_characterkie.setVisibility(View.VISIBLE);
            bt_basic_info_characterkies.setBackgroundResource(R.drawable.twotone_arrow_circle_up_24);
        }else{
            hideBasicInfo();
            bt_basic_info_characterkies.setBackgroundResource(R.drawable.twotone_arrow_drop_down_circle_24);
        }
        isBasicInfoVisible = !isBasicInfoVisible;
    }
    private void hideBasicInfo(){
        bt_birthday_characterkie.setVisibility(View.GONE);
        bt_gender_characterkie.setVisibility(View.GONE);
        bt_pronouns_characterkie.setVisibility(View.GONE);
        bt_state_characterkie.setVisibility(View.GONE);
        tv_status_characterkie.setVisibility(View.GONE);
        tv_pronouns_characterkie.setVisibility(View.GONE);
        tv_gender_characterkie.setVisibility(View.GONE);
        tv_birthday_characterkie.setVisibility(View.GONE);
    }
    private void addDataToFirestore(){
        String name = et_nameCharacterkieCreate.getText().toString();
        if(!name.equals("")){
            dialog.show();
            LottieAnimationView animationView = dialog.findViewById(R.id.anim_create_edit);
            animationView.setAnimation(R.raw.reading_anim);
            animationView.playAnimation();
            Completable.timer(3, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                                if (source.equals("device")) {
                                        characterkie = new Characterkie(worldkie_id,userkie_id, name, optionPronounsString,optionBirthdayString,false,tb_characterkieDraft.isChecked(),tb_characterkiePrivacity.isChecked(),"",optionStatusString,optionGenderString);
                                } else {
                                        characterkie = new Characterkie(worldkie_id,userkie_id, name, optionPronounsString,optionBirthdayString,true,tb_characterkieDraft.isChecked(),tb_characterkiePrivacity.isChecked(), ib_select_img_create_characterkie.getTag().toString(),optionStatusString,optionGenderString);
                                }
                                Task<DocumentReference> addTask = db.collection("Characterkies").add(characterkie);

                                addTask.addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                        if (task.isSuccessful()) {

                                            if (source.equals("device")) {
                                                StorageReference userRef = storage.getReference().child(task.getResult().getId());
                                                userRef.child("profile" + DrawableUtils.getExtensionFromUri(getContext(), image)).putFile(image);

                                            }
                                            animationView.setAnimation(R.raw.success_anim);
                                            animationView.playAnimation();
                                            Completable.timer(2, TimeUnit.SECONDS)
                                                    .subscribeOn(Schedulers.io())
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .subscribe(() -> {
                                                        dialog.dismiss();
                                                        NavigationUtils.goBack(fragmentManager, activity);
                                                    });

                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        animationView.setAnimation(R.raw.fail_anim);
                                        animationView.playAnimation();
                                        Completable.timer(3, TimeUnit.SECONDS)
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(() -> {
                                                    animationView.setVisibility(View.GONE);
                                                    dialog.dismiss();
                                                });
                                    }
                                });
                            }
                    );
        }
        }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    private void editDataFirestore(){
       /* if(!et_nameWorldkieCreate.getText().toString().equals("")) {
            dialog.show();
            LottieAnimationView animationView = dialog.findViewById(R.id.anim_create_edit);
            animationView.setAnimation(R.raw.reading_anim);
            animationView.playAnimation();
            Completable.timer(3, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                                getValues();
                                CollectionReference dbWorldkies = db.collection("Worldkies");
                                Date last_update = new Date();
                                Map<String, Object> worldkieData = new HashMap<>();
                                if (!name.equals(worldkieModel.getName())) {
                                    worldkieData.put("name", name);
                                }
                                worldkieData.put("last_update", last_update);
                                //boolean isDefaultImage = (boolean) createWorldkie.getIB_profile_photo().getTag(R.drawable.worldkie_default);
                                if (source.equals("app") && !ib_select_img_create_worldkie.getTag().toString().equals(worldkieModel.getId_photo())) {
                                    worldkieData.put("id_photo", ib_select_img_create_worldkie.getTag().toString());
                                }
                                if (worldkieModel.isPhoto_default() && source.equals("device")) {
                                    worldkieData.put("photo_default", false);

                                } else if (!worldkieModel.isPhoto_default() && source.equals("app")) {
                                    worldkieData.put("photo_default", true);

                                }
                                if (worldkieModel.isWorldkie_private() != tb_worldkiePrivacity.isChecked()) {
                                    worldkieData.put("worldkie_private", tb_worldkiePrivacity.isChecked());
                                }
                                if (worldkieModel.isDraft() != tb_worldkieDraft.isChecked()) {
                                    worldkieData.put("draft", tb_worldkieDraft.isChecked());
                                }
                                dbWorldkies.document(worldkie_id).update(worldkieData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        if (source.equals("device")) {
                                            StorageReference userRef = storage.getReference().child(worldkie_id);
                                            userRef.child("profile" + DrawableUtils.getExtensionFromUri(getContext(), image)).putFile(image);

                                        }
                                        animationView.setAnimation(R.raw.success_anim);
                                        animationView.playAnimation();
                                        Completable.timer(3, TimeUnit.SECONDS)
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(() -> {
                                                    animationView.setVisibility(View.GONE);
                                                    dialog.dismiss();
                                                    NavigationUtils.goBack(fragmentManager, activity);
                                                });

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        animationView.setAnimation(R.raw.fail_anim);
                                        animationView.playAnimation();
                                        Completable.timer(5, TimeUnit.SECONDS)
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(() -> {
                                                    animationView.setVisibility(View.GONE);
                                                    dialog.dismiss();
                                                });
                                    }
                                });
                            }
                    );
        }*/
    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.ib_back){
            NavigationUtils.goBack(fragmentManager,activity);
        } else if (v.getId()==R.id.ib_save) {
            if(worldkie_id == null){
                    addDataToFirestore();
                }else{
                    editDataFirestore();
                }
        } else if (v.getId()==R.id.ib_select_img_create_characterkie) {
            selectImage();
        } else if(v.getId()==R.id.bt_birthday_characterkie){
            BottomSheetChooseBirthday bottomSheetChooseBirthday = new BottomSheetChooseBirthday(optionBirthday,1,1,1);
            bottomSheetChooseBirthday.show(getChildFragmentManager(), bottomSheetChooseBirthday.getTag());
        } else if (v.getId()==R.id.bt_pronouns_characterkie) {
            bottomSheetChoosePronouns = new BottomSheetChoosePronouns(optionPronouns,optionPronounsString);
            bottomSheetChoosePronouns.show(getChildFragmentManager(), bottomSheetChoosePronouns.getTag());
        } else if (v.getId()==R.id.bt_gender_characterkie) {
            bottomSheetChooseGender = new BottomSheetChooseGender(optionGender,optionGenderString);
            bottomSheetChooseGender.show(getChildFragmentManager(), bottomSheetChooseGender.getTag());
        } else if(v.getId()==R.id.bt_state_characterkie){
            bottomSheetChooseState = new BottomSheetChooseState(optionStatus,optionStatusString);
            bottomSheetChooseState.show(getChildFragmentManager(), bottomSheetChooseState.getTag());
        } else if (v.getId()==R.id.bt_basic_info_characterkies) {
            showHideBasicInfo();
        }
    }
}