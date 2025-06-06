package com.example.buuktu.views;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.airbnb.lottie.LottieAnimationView;
import com.example.buuktu.R;
import com.example.buuktu.bottomsheet.BottomSheetChooseBirthday;
import com.example.buuktu.bottomsheet.BottomSheetChooseGender;
import com.example.buuktu.bottomsheet.BottomSheetChoosePronouns;
import com.example.buuktu.bottomsheet.BottomSheetChooseState;
import com.example.buuktu.bottomsheet.BottomSheetProfilePhoto;
import com.example.buuktu.dialogs.CreateEditGeneralDialog;
import com.example.buuktu.models.CharacterkieModel;
import com.example.buuktu.utils.CheckUtil;
import com.example.buuktu.utils.DrawableUtils;
import com.example.buuktu.utils.EfectsUtils;
import com.example.buuktu.utils.NavigationUtils;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class CreateCharacterkie extends Fragment implements View.OnClickListener {
    FragmentManager fragmentManager;
    TextView tv_basic_info_characterkies,tv_birthday_characterkie,tv_status_characterkie,tv_pronouns_characterkie,tv_gender_characterkie;
    Button bt_birthday_characterkie,bt_pronouns_characterkie,bt_gender_characterkie,bt_state_characterkie;
ImageButton bt_basic_info_characterkies;
    ImageButton ib_select_img_create_characterkie,ib_back,ib_save;
    Uri image;
    String[] meses;
    BottomSheetDialogFragment bottomSheetProfilePhoto;
    BottomSheetChoosePronouns bottomSheetChoosePronouns;
    BottomSheetChooseGender bottomSheetChooseGender;
    BottomSheetChooseState bottomSheetChooseState;
    Switch tb_characterkiePrivacity,tb_characterkieDraft;
    TextInputEditText et_nameCharacterkieCreate;
    TextInputLayout et_nameCharacterkieCreateFull;
    String worldkie_id,characterkie_id,gender,pronouns,birthday,status;
    boolean isBasicInfoVisible=false;
    CharacterkieModel characterkie;
    private int optionPronouns, optionBirthday,optionGender,optionStatus;
    String optionPronounsString, optionBirthdayString,optionGenderString,optionStatusString;
    int year,day,month;
    CreateEditGeneralDialog dialog;
    MainActivity mainActivity;
    LottieAnimationView animationView;
    public CreateCharacterkie() {
    }

    public static CreateCharacterkie newInstance() {
        return new CreateCharacterkie();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if(getArguments().containsKey("worldkie_id")) {
                this.worldkie_id = getArguments().getString("worldkie_id");
            }
                if(getArguments().containsKey("characterkie_id")) {
                    this.characterkie_id = getArguments().getString("characterkie_id");
                }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_create_characterkie, container, false);

        initComponents(view);
        setListeners();


        setCharacterkieModel();
        return view;
    }
    private void setCharacterkieModel(){
        if(characterkie_id == null){
            try {
                createMode();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else{
            mainActivity.getCollectionCharacterkies().document(characterkie_id).addSnapshotListener((queryDocumentSnapshot, e) -> {

                if (e != null) return;
                if (queryDocumentSnapshot!=null) {
                    characterkie = CharacterkieModel.fromSnapshot(queryDocumentSnapshot);
                    editMode();
                }
            });

        }
    }
    public void editMode(){
        et_nameCharacterkieCreate.setText(characterkie.getName());
        tb_characterkiePrivacity.setChecked(characterkie.isCharacterkie_private());
        tb_characterkieDraft.setChecked(characterkie.isDraft());
        tb_characterkieDraft.setVisibility(characterkie.isCharacterkie_private()?View.GONE : View.VISIBLE);
        getStrings(characterkie.getPronouns(),"pronouns");
        getStrings(characterkie.getGender(),"gender");
        getStrings(characterkie.getStatus(),"status");
        getStrings(characterkie.getBirthday_format(),"birthday");

        optionPronounsString = getOptionTextByRadioButtonId(optionPronouns,R.layout.choose_pronouns_dialog);
        optionGenderString = getOptionTextByRadioButtonId(optionGender,R.layout.choose_gender_dialog);
        optionStatusString = getOptionTextByRadioButtonId(optionStatus,R.layout.choose_status_dialog);
     //   optionBirthdayString = getOptionTextByRadioButtonId(optionBirthday,R.layout.choose_birthday_dialog);
        if(characterkie.getBirthday_format().equals(getString(R.string.unknown_fem))){
            optionBirthdayString = getString(R.string.unknown_fem);
        }else{
            optionBirthdayString = characterkie.getBirthday();
        }
        getImage();
    }

    private void getStrings(String key, String option){
        int resId = mainActivity.getResources().getIdentifier(key, "string", mainActivity.getPackageName());

        if (resId != 0) {
            String textString = mainActivity.getString(resId);
            if(option.equals(gender)) {
                optionGender = mainActivity.getResources().getIdentifier("rb_" + key, "id", mainActivity.getPackageName());
                bt_gender_characterkie.setText(textString);
            } else if (option.equals(pronouns)) {
                optionPronouns = mainActivity.getResources().getIdentifier("rb_" + key, "id", mainActivity.getPackageName());
                bt_pronouns_characterkie.setText(textString);
            } else if (option.equals(status)) {
                optionStatus = mainActivity.getResources().getIdentifier("rb_" + key, "id", mainActivity.getPackageName());
                bt_state_characterkie.setText(textString);
            } else if (option.equals(birthday)) {
                optionBirthday = mainActivity.getResources().getIdentifier("rb_" + key, "id", mainActivity.getPackageName());
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
    public CharacterkieModel getCharacterkie() {
        return characterkie;
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
                int resId = mainActivity.getResources().getIdentifier(characterkie.getPhoto_id(), "mipmap", mainActivity.getPackageName());

                if (resId != 0) {
                    Drawable drawable = ContextCompat.getDrawable(mainActivity, resId);
                    ib_select_img_create_characterkie.setImageDrawable(drawable);
                    ib_select_img_create_characterkie.setTag(DrawableUtils.getMipmapName(mainActivity,resId));
                        DrawableUtils.personalizarImagenCuadradoButton(getContext(),150/7,7,R.color.brownMaroon,drawable, ib_select_img_create_characterkie);
                        ib_select_img_create_characterkie.setVisibility(View.VISIBLE);
                        EfectsUtils.startCircularReveal(drawable,ib_select_img_create_characterkie);
                }
        } else {
            mainActivity.getFirebaseStorageWorldkies().getReference(worldkie_id).listAll().addOnSuccessListener(listResult -> {
                for (StorageReference item : listResult.getItems()) {
                    if (item.getName().startsWith("cover")) {
                        item.getDownloadUrl().addOnSuccessListener(uri -> {
                            DrawableUtils.personalizarImagenCuadradoButton(getContext(),150/6,7,R.color.brownMaroon,uri,ib_select_img_create_characterkie);
                            ib_select_img_create_characterkie.setVisibility(View.VISIBLE);
                            EfectsUtils.startCircularReveal(ib_select_img_create_characterkie.getDrawable(),ib_select_img_create_characterkie);
                        });
                    }
                }
            });
        }
    }
    public void createMode() throws IOException {
        et_nameCharacterkieCreate.setText("");
        tb_characterkiePrivacity.setChecked(false);
        tb_characterkieDraft.setVisibility(View.GONE);
        meses= new String[]{mainActivity.getString(R.string.january),mainActivity.getString(R.string.february),mainActivity.getString(R.string.march),mainActivity.getString(R.string.april),mainActivity.getString(R.string.may),mainActivity.getString(R.string.june),mainActivity.getString(R.string.july),mainActivity.getString(R.string.august),mainActivity.getString(R.string.september),mainActivity.getString(R.string.october),mainActivity.getString(R.string.november),mainActivity.getString(R.string.december)};
        dialog = new CreateEditGeneralDialog(mainActivity);
        putDefaultImage();
        characterkie = new CharacterkieModel();

        characterkie.setUID_AUTHOR(mainActivity.getUID());
        characterkie.setUID_WORLDKIE(worldkie_id);
        characterkie.setDraft(false);
        characterkie.setCharacterkie_private(false);
        optionPronouns = R.id.rb_pronouns_unknown;
        optionBirthday = R.id.rb_unknown_birthday;
        optionGender = R.id.rb_gender_unknown;
        optionStatus = R.id.rb_status_unknown;
        characterkie.setPronouns("pronouns_unknown");
        characterkie.setGender("gender_unknown");
        characterkie.setStatus("status_unknown");
        characterkie.setBirthday("unknown");
        characterkie.setBirthday_format("unknown");
        optionPronounsString = getOptionTextByRadioButtonId(optionPronouns,R.layout.choose_pronouns_dialog);
        optionGenderString = getOptionTextByRadioButtonId(optionGender,R.layout.choose_gender_dialog);
        optionStatusString = getOptionTextByRadioButtonId(optionStatus,R.layout.choose_status_dialog);
        optionBirthdayString = getOptionTextByRadioButtonId(optionBirthday,R.layout.choose_birthday_dialog);
        bt_pronouns_characterkie.setText(optionPronounsString);
        bt_gender_characterkie.setText(optionGenderString);
        bt_state_characterkie.setText(optionStatusString);
        bt_birthday_characterkie.setText(optionBirthdayString);
        // characterkie.setGender();
        characterkie.setPhoto_default(true);

        ib_select_img_create_characterkie.setTag(DrawableUtils.getMipmapName(mainActivity,R.mipmap.photocharacterkieone));
        characterkie.setPhoto_id(ib_select_img_create_characterkie.getTag().toString());
            putDefaultImage();

    }

    public void setOptionPronounsString(String optionPronounsString) {
        this.optionPronounsString = optionPronounsString;
        bt_pronouns_characterkie.setText(optionPronounsString);
    }


    public void setOptionStatusString(String optionStatusString) {
        this.optionStatusString = optionStatusString;
        bt_state_characterkie.setText(optionStatusString);
    }


    public void setOptionGenderString(String optionGenderString) {
        this.optionGenderString = optionGenderString;
       // optionGender = mainActivity.getResources().getIdentifier("rb" + key, "id", mainActivity.getPackageName());

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
        tv_basic_info_characterkies = view.findViewById(R.id.tv_basic_info_characterkies);
        bt_basic_info_characterkies = view.findViewById(R.id.bt_basic_info_characterkies);
        bottomSheetProfilePhoto = new BottomSheetProfilePhoto();
        gender = mainActivity.getResources().getString(R.string.gender);
        pronouns = mainActivity.getResources().getString(R.string.pronouns);
        birthday = mainActivity.getResources().getString(R.string.birthday);
        status = mainActivity.getResources().getString(R.string.status);
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

    private void putDefaultImage(){
        DrawableUtils.personalizarImagenCircleButton(mainActivity,DrawableUtils.drawableToBitmap(ContextCompat.getDrawable(mainActivity, R.mipmap.photocharacterkieone)),ib_select_img_create_characterkie,R.color.brownMaroon);
    }
    public void setSelectedProfilePhoto(Drawable image){
        ib_select_img_create_characterkie.setImageDrawable(image);
    }
    public void setDate(){
        if(optionBirthday == R.id.rb_unknown_birthday){
            optionBirthdayString = mainActivity.getString(R.string.unknown_fem);
            characterkie.setBirthday(mainActivity.getString(R.string.unknown_fem));

            characterkie.setBirthday_format(mainActivity.getString(R.string.unknown_fem));
            bt_birthday_characterkie.setText(optionBirthdayString);
        } else if (optionBirthday==R.id.rb_full_birthday) {
            optionBirthdayString = day+"/"+month+"/"+year;
            characterkie.setBirthday(optionBirthdayString);
            characterkie.setBirthday_format(mainActivity.getString(R.string.dd_mm_yy));
            bt_birthday_characterkie.setText(optionBirthdayString);
        }else if (optionBirthday==R.id.rb_month_year_birthday) {
            optionBirthdayString = month+"/"+year;
            characterkie.setBirthday(optionBirthdayString);
            characterkie.setBirthday_format(mainActivity.getString(R.string.mm_yy));
            bt_birthday_characterkie.setText(optionBirthdayString);

        }else if (optionBirthday==R.id.rb_month_birthday) {
            optionBirthdayString = String.valueOf(month);
            characterkie.setBirthday(optionBirthdayString);
            characterkie.setBirthday_format(mainActivity.getString(R.string.mm));
            bt_birthday_characterkie.setText(meses[month]);
        }else if (optionBirthday==R.id.rb_year_birthday) {
            optionBirthdayString = String.valueOf(year);
            characterkie.setBirthday(optionBirthdayString);
            characterkie.setBirthday_format(mainActivity.getString(R.string.yyyy));
            bt_birthday_characterkie.setText(optionBirthdayString);
        }
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
            Completable.timer(3, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                                Task<DocumentReference> addTask = mainActivity.getCollectionCharacterkies().add(characterkie.toMap());
                                addTask.addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        characterkie_id = addTask.getResult().getId();
                                        success();}
                                }).addOnFailureListener(e -> fail());});
    }
    private void uploadNewImage(){
        if (!characterkie.isPhoto_default()) {
            mainActivity.getFirebaseStorageCharacterkies().getReference().child(characterkie_id).child("cover" + DrawableUtils.getExtensionFromUri(mainActivity, image)).putFile(image);

        }
    }
    private void success(){
        uploadNewImage();
        EfectsUtils.setAnimationsDialog("success", animationView);
        delayedDismiss();
    }
    private void fail(){
        EfectsUtils.setAnimationsDialog("fail", animationView);
        delayedDismiss();
    }
    private void delayedDismiss() {
        Completable.timer(2, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> dialog.dismiss());
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setDay(int day) {
        this.day = day;
    }



    public void setMonth(int month) {
        this.month = month;
    }
    private void save(){
        if(CheckUtil.handlerCheckName(mainActivity,et_nameCharacterkieCreate,et_nameCharacterkieCreateFull)) {
            characterkie.setName(et_nameCharacterkieCreate.getText().toString());
            dialog.show();
            animationView = dialog.getAnimationView();
            if(characterkie_id == null){
                addDataToFirestore();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.ib_back){
            NavigationUtils.goBack(fragmentManager,mainActivity);
        } else if (v.getId()==R.id.ib_save) {
            save();
        } else if (v.getId()==R.id.ib_select_img_create_characterkie) {
            selectImage();
        } else if(v.getId()==R.id.bt_birthday_characterkie){
            BottomSheetChooseBirthday bottomSheetChooseBirthday = new BottomSheetChooseBirthday(optionBirthday,optionBirthdayString,meses);
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