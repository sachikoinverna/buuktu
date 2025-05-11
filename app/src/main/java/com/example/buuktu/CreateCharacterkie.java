package com.example.buuktu;

import static android.widget.Toast.LENGTH_LONG;

import android.content.res.Resources;
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
import com.example.buuktu.models.WorldkieModel;
import com.example.buuktu.utils.CheckUtil;
import com.example.buuktu.utils.DrawableUtils;
import com.example.buuktu.utils.EfectsUtils;
import com.example.buuktu.utils.NavigationUtils;
import com.example.buuktu.utils.RoundedBorderSquareTransformation;
import com.example.buuktu.views.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
    CollectionReference characterkieCollection;
    DocumentReference characterRef;
    TextInputEditText textInputEditText,et_nameCharacterkieCreate;
    FirebaseAuth firebaseAuth;
    TextInputLayout et_nameCharacterkieCreateFull;
    ConstraintLayout constraintLayout;
    String UID, worldkie_id, source,name,characterkie_id, userkie_id,gender,pronouns,birthday,status;
    boolean privacity, draft,isBasicInfoVisible=false;
    Characterkie characterkie;
    private int optionPronouns, optionBirthday,optionGender,optionStatus;
    String optionPronounsString, optionBirthdayString,optionGenderString,optionStatusString;
    int year,day,month;
    CreateEditGeneralDialog dialog;
    private final FirebaseStorage storage = FirebaseStorage.getInstance("gs://buuk-tu-characterkies");
    Resources res;
    String packageName;
    MainActivity mainActivity;
    LottieAnimationView animationView;
    public CreateCharacterkie() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CreateCharacterkie.
     */
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

        firebaseAuth = FirebaseAuth.getInstance();
        UID = firebaseAuth.getUid();
        db = FirebaseFirestore.getInstance();
        characterkieCollection =db.collection("Characterkies");

        bottomSheetProfilePhoto = new BottomSheetProfilePhoto();
        initComponents(view);
        setListeners();
        res = mainActivity.getResources();
        packageName = mainActivity.getPackageName();
        dialog = new CreateEditGeneralDialog(mainActivity);
        animationView = dialog.getAnimationView();
        gender = mainActivity.getResources().getString(R.string.gender);
        pronouns = mainActivity.getResources().getString(R.string.pronouns);
        birthday = mainActivity.getResources().getString(R.string.birthday);
        status = mainActivity.getResources().getString(R.string.status);
        if(characterkie_id == null){
            try {
                createMode();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else{
            characterkieCollection.document(characterkie_id).addSnapshotListener((queryDocumentSnapshot, e) -> {
                if (e != null) {
                    return;
                }
                if (queryDocumentSnapshot!=null) {
                    characterkie = Characterkie.fromSnapshot(queryDocumentSnapshot);

                    tb_characterkiePrivacity.setChecked(characterkie.isCharacterkie_private());
                        tb_characterkieDraft.setChecked(characterkie.isDraft());
                        tb_characterkieDraft.setVisibility(characterkie.isCharacterkie_private()?View.VISIBLE:View.INVISIBLE);
                    et_nameCharacterkieCreate.setText(name);
                    getStrings(characterkie.getPronouns(),"Pronouns");
                    getStrings(characterkie.getGender(),"Gender");
                    getStrings(characterkie.getPronouns(),"Pronouns");
                    getStrings(characterkie.getBirthday_format(),"Birthday");
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
    private void getStrings(String key,String option){
        int resId = mainActivity.getResources().getIdentifier(key, "string", mainActivity.getPackageName());

        if (resId != 0) {
            String textString = mainActivity.getString(resId);
            if(option.equals(gender)) {
                optionGender = mainActivity.getResources().getIdentifier("rb" + key, "id", mainActivity.getPackageName());
                bt_gender_characterkie.setText(textString);
            } else if (option.equals(pronouns)) {
                optionPronouns = mainActivity.getResources().getIdentifier("rb" + key, "id", mainActivity.getPackageName());
                bt_pronouns_characterkie.setText(textString);
            } else if (option.equals(status)) {
                optionStatus = mainActivity.getResources().getIdentifier("rb" + key, "id", mainActivity.getPackageName());
                bt_state_characterkie.setText(textString);
            } else if (option.equals(birthday)) {
                optionBirthday = mainActivity.getResources().getIdentifier("rb" + key, "id", mainActivity.getPackageName());
                bt_state_characterkie.setText(characterkie.getBirthday());
            }
        } else {
            if(option.equals(gender)) {
                optionGender = R.id.rb_other_gender_characterkie;
                bt_gender_characterkie.setText(characterkie.getGender());
            } else if (option.equals(pronouns)) {
                optionPronouns = R.id.rb_other_characterkie;
                bt_pronouns_characterkie.setText(characterkie.getPronouns());
            } else if (option.equals(status)) {
                    optionStatus = R.id.rb_other_status_characterkie;
                    bt_state_characterkie.setText(characterkie.getStatus());
            }

        }
    }
    public Characterkie getCharacterkie() {
        return characterkie;
    }

    public void setCharacterkie(Characterkie characterkie) {
        this.characterkie = characterkie;
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
                String id_photo = characterkie.getPhoto_id();
                int resId = mainActivity.getResources().getIdentifier(id_photo, "mipmap", mainActivity.getPackageName());

                if (resId != 0) {
                    Drawable drawable = ContextCompat.getDrawable(mainActivity, resId);
                    ib_select_img_create_characterkie.setImageDrawable(drawable);
                    source = "app";
                    ib_select_img_create_characterkie.setTag(DrawableUtils.getMipmapName(mainActivity,resId));

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
        characterkie = new Characterkie();

        characterkie.setUID_AUTHOR(userkie_id);
        characterkie.setUID_WORLDKIE(worldkie_id);
        characterkie.setDraft(false);
        characterkie.setCharacterkie_private(false);
        optionPronouns = R.id.rb_pronouns_unknown_characterkie;
        optionBirthday = R.id.rb_unknown_birthday;
        optionGender = R.id.rb_gender_unknown;
        optionStatus = R.id.rb_status_unknown;
        optionPronounsString = getOptionTextByRadioButtonId(optionPronouns,R.layout.choose_pronouns_dialog);
        optionGenderString = getOptionTextByRadioButtonId(optionGender,R.layout.choose_gender_dialog);
        optionStatusString = getOptionTextByRadioButtonId(optionStatus,R.layout.choose_status_dialog);
        optionBirthdayString = getOptionTextByRadioButtonId(optionBirthday,R.layout.choose_birthday_dialog);
        bt_pronouns_characterkie.setText(optionPronounsString);
        bt_gender_characterkie.setText(optionGenderString);
        bt_state_characterkie.setText(optionStatusString);
        bt_birthday_characterkie.setText(optionBirthdayString);
        // characterkie.setGender();

        ib_select_img_create_characterkie.setTag(DrawableUtils.getMipmapName(mainActivity,R.mipmap.photoworldkieone));

    }

    public void setValuesRadioButton(){

    }
    public void setOptionPronounsString(String optionPronounsString) {
        this.optionPronounsString = optionPronounsString;
        bt_pronouns_characterkie.setText(optionPronounsString);
    }

    public String getOptionStatusString() {
        return optionStatusString;
    }

    public void setOptionStatusString(String optionStatusString) {
        this.optionStatusString = optionStatusString;
        bt_state_characterkie.setText(optionStatusString);
        //characterkie.setStatus();
    }
    public void setOptionBirthdayString(String optionBirthdayString) {
        this.optionBirthdayString = optionBirthdayString;
    }

    public String getOptionGenderString() {
        return optionGenderString;
    }

    public void setOptionGenderString(String optionGenderString) {
        this.optionGenderString = optionGenderString;
        bt_gender_characterkie.setText(optionGenderString);
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
        mainActivity = (MainActivity) getActivity();
        ib_back = mainActivity.getBackButton();
        ib_save = mainActivity.getIb_save();
        tv_gender_characterkie = view.findViewById(R.id.tv_gender_characterkie);
        fragmentManager = mainActivity.getSupportFragmentManager();
        tv_pronouns_characterkie = view.findViewById(R.id.tv_pronouns_characterkie);

        constraintLayout = view.findViewById(R.id.constraint_create_characterkie);
        tv_basic_info_characterkies = view.findViewById(R.id.tv_basic_info_characterkies);
        bt_basic_info_characterkies = view.findViewById(R.id.bt_basic_info_characterkies);
        showHideBasicInfo();
        initVisibility();
    }
    private String getOptionTextByRadioButtonId(int id, int idLayout){
        View view = LayoutInflater.from(mainActivity).inflate(idLayout, null);
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
        tb_characterkiePrivacity.setOnCheckedChangeListener((buttonView, isChecked) -> {
            tb_characterkieDraft.setVisibility(isChecked?View.VISIBLE:View.INVISIBLE);
            characterkie.setCharacterkie_private(isChecked);
        });
        tb_characterkieDraft.setOnCheckedChangeListener((buttonView, isChecked) -> characterkie.setDraft(isChecked));
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
        Drawable drawable = ContextCompat.getDrawable(mainActivity, R.mipmap.photocharacterkieone);
        DrawableUtils.personalizarImagenCircleButton(mainActivity,DrawableUtils.drawableToBitmap(drawable),ib_select_img_create_characterkie,R.color.brownMaroon);
    }
    public void setSelectedProfilePhoto(Drawable image){
        ib_select_img_create_characterkie.setImageDrawable(image);
    }
    public void setDate(){
        if(optionBirthday == R.id.rb_unknown_birthday){
            optionBirthdayString = "Unknown";
        } else if (optionBirthday==R.id.rb_full_birthday) {
            optionBirthdayString = day+"/"+month+"/"+year;
        }else if (optionBirthday==R.id.rb_month_year_birthday) {
            optionBirthdayString = month+"/"+year;
        }else if (optionBirthday==R.id.rb_month_birthday) {
            optionBirthdayString = String.valueOf(month);
        }else if (optionBirthday==R.id.rb_year_birthday) {
            optionBirthdayString = String.valueOf(year);
        }
        bt_birthday_characterkie.setText(optionBirthdayString);
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
        tb_characterkieDraft.setChecked(characterkie.isDraft());
        tb_characterkieDraft.setVisibility(characterkie.isCharacterkie_private()?View.GONE : View.VISIBLE);
    }
    private void showHideBasicInfo(){
        bt_birthday_characterkie.setVisibility(isBasicInfoVisible ? View.GONE : View.VISIBLE);
        bt_gender_characterkie.setVisibility(isBasicInfoVisible ? View.GONE : View.VISIBLE);
        bt_pronouns_characterkie.setVisibility(isBasicInfoVisible ? View.GONE : View.VISIBLE);
        bt_state_characterkie.setVisibility(isBasicInfoVisible ? View.GONE : View.VISIBLE);
        tv_status_characterkie.setVisibility(isBasicInfoVisible ? View.GONE : View.VISIBLE);
        tv_pronouns_characterkie.setVisibility(isBasicInfoVisible ? View.GONE : View.VISIBLE);
        tv_gender_characterkie.setVisibility(isBasicInfoVisible ? View.GONE : View.VISIBLE);
        tv_birthday_characterkie.setVisibility(isBasicInfoVisible ? View.GONE : View.VISIBLE);
        bt_basic_info_characterkies.setBackgroundResource(isBasicInfoVisible ? R.drawable.twotone_arrow_drop_down_circle_24:R.drawable.twotone_arrow_circle_up_24);
        isBasicInfoVisible = !isBasicInfoVisible;
    }
    public void setPhotoNoDefault(){
        characterkie.setPhoto_default(false);
        characterkie.setPhoto_id(null);
    }
    public void setPhotoDefault(){
        characterkie.setPhoto_default(true);
        characterkie.setPhoto_id(ib_select_img_create_characterkie.getTag().toString());
    }
    private void addDataToFirestore(){
        if(CheckUtil.handlerCheckName(mainActivity,et_nameCharacterkieCreate,et_nameCharacterkieCreateFull)){
            dialog.show();
            EfectsUtils.setAnimationsDialog("start",animationView);
            Completable.timer(3, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                                Task<DocumentReference> addTask = characterkieCollection.add(characterkie.toMap());

                                addTask.addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {

                                        if (!characterkie.isPhoto_default()) {
                                            StorageReference userRef = storage.getReference().child(task.getResult().getId());
                                            userRef.child("profile" + DrawableUtils.getExtensionFromUri(getContext(), image)).putFile(image);

                                        }
                                        EfectsUtils.setAnimationsDialog("success",animationView);
                                        Completable.timer(2, TimeUnit.SECONDS)
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(() -> {
                                                    dialog.dismiss();
                                                    NavigationUtils.goBack(fragmentManager, mainActivity);
                                                });

                                    }
                                }).addOnFailureListener(e -> {
                                    EfectsUtils.setAnimationsDialog("fail",animationView);
                                    Completable.timer(3, TimeUnit.SECONDS)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(() -> {
                                                dialog.dismiss();
                                            });
                                });
                            }
                    );
        }
    }
    private void getValues(){
        characterkie.setName(et_nameCharacterkieCreate.getText().toString());
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
        if(CheckUtil.handlerCheckName(mainActivity,et_nameCharacterkieCreate,et_nameCharacterkieCreateFull)) {
            dialog.show();
            EfectsUtils.setAnimationsDialog("start",animationView);

            Completable.timer(3, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                                characterkieCollection.document(characterkie_id).update(characterkie.toMap()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        if (!characterkie.isPhoto_default()) {
                                            StorageReference userRef = storage.getReference().child(worldkie_id);
                                            userRef.child("profile" + DrawableUtils.getExtensionFromUri(getContext(), image)).putFile(image);

                                        }
                                        EfectsUtils.setAnimationsDialog("success",animationView);

                                        Completable.timer(3, TimeUnit.SECONDS)
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(() -> {
                                                    dialog.dismiss();
                                                    NavigationUtils.goBack(fragmentManager,mainActivity);
                                                });

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        EfectsUtils.setAnimationsDialog("fail",animationView);
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
        }
    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.ib_back){
            NavigationUtils.goBack(fragmentManager,mainActivity);
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