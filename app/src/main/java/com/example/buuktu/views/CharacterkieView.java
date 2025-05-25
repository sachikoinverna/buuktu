package com.example.buuktu.views;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.buuktu.R;
import com.example.buuktu.models.CharacterkieModel;
import com.example.buuktu.models.UserkieModel;
import com.example.buuktu.models.WorldkieModel;
import com.example.buuktu.utils.DrawableUtils;
import com.example.buuktu.utils.NavigationUtils;
import com.google.firebase.storage.StorageReference;


public class CharacterkieView extends Fragment implements View.OnClickListener {
    TextView tv_birthdayViewCharacterkie,tv_pronounsViewCharacterkie,tv_genderViewCharacterkie,tv_separator_basic_info_characterkie_view,tv_basic_info_characterkie_view,tv_nameCharacterkieView,tv_nameUserCharacterkieView,tv_usernameCharacterkieView,tv_nameWorldkieViewCharacterkie,tv_locked_characterkie;
    ImageButton bt_basic_info_characterkies_view,ib_back,ib_save,ib_characterkieView;
    ImageView iv_locked_characterkie,iv_background_basic_info_characterkie,iv_basic_data_characterkie_view;
    String UID,UID_AUTHOR,UID_WORLDKIE,mode;
    UserkieModel userkieModel;
    FragmentManager fragmentManager;
    WorldkieModel worldkieModel;
    MainActivity mainActivity;
    CharacterkieModel characterkieModel;
    boolean isBasicInfoVisible;

    public CharacterkieView() {
    }
    public static CharacterkieView newInstance() {
        return new CharacterkieView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mode = getArguments().getString("mode");
            UID = getArguments().getString("UID");
            UID_WORLDKIE = getArguments().getString("UID_WORLDKIE");
            UID_AUTHOR = getArguments().getString("UID_AUTHOR");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_characterkie_view, container, false);
        initComponents(view);
        setVisibility();
        getCharacterkie();



        setListeners();
        return view;
    }
    private void getCharacterkie(){
        mainActivity.getCollectionCharacterkies().document(UID).addSnapshotListener((documentSnapshot, ex) -> {
            if (ex != null) return;


            if (documentSnapshot != null) {
                characterkieModel = CharacterkieModel.fromSnapshot(documentSnapshot);
                tv_nameCharacterkieView.setText(characterkieModel.getName());
                getStrings(characterkieModel.getPronouns(),"pronouns");
                getStrings(characterkieModel.getGender(),"gender");
                getBirthday();
                getProfilePhoto();
                mainActivity.getCollectionWorldkies().document(UID_WORLDKIE).addSnapshotListener((documentSnap, e) -> {
                    if (e != null) return;

                    if (documentSnap != null) {
                        worldkieModel = WorldkieModel.fromSnapshot(documentSnap);
                        tv_nameWorldkieViewCharacterkie.setText(worldkieModel.getName());

                mainActivity.getCollectionUsers().document(UID_AUTHOR).addSnapshotListener((document, exx) -> {
                            if (exx != null) return;


                            if (document != null) {
                                userkieModel = UserkieModel.fromSnapshot(document);
                                tv_nameUserCharacterkieView.setText(userkieModel.getName());
                                tv_usernameCharacterkieView.setText(userkieModel.getUsername());
                                if(((userkieModel.isProfile_private() && mode.equals("other")) || (!worldkieModel.isWorldkie_private()&& mode.equals("other")) || (!characterkieModel.isCharacterkie_private()&&mode.equals("other")))){
                                    tv_locked_characterkie.setVisibility(View.VISIBLE);
                                    iv_locked_characterkie.setVisibility(View.VISIBLE);
                                    iv_background_basic_info_characterkie.setVisibility(View.GONE);
                                    tv_birthdayViewCharacterkie.setVisibility(View.GONE);
                                    tv_pronounsViewCharacterkie.setVisibility(View.GONE);
                                    tv_genderViewCharacterkie.setVisibility(View.GONE);
                                    bt_basic_info_characterkies_view.setVisibility(View.GONE);
                                    tv_basic_info_characterkie_view.setVisibility(View.GONE);
                                            iv_basic_data_characterkie_view.setVisibility(View.GONE);
                                    tv_separator_basic_info_characterkie_view.setVisibility(View.GONE);
                                }


                            }
                        }
                );
                    }
                });

            }
        });
    }
    private void getBirthday()
    {
        if (characterkieModel.getBirthday_format().equalsIgnoreCase(mainActivity.getString(R.string.unknown_fem))) {

            tv_birthdayViewCharacterkie.setText(mainActivity.getString(R.string.birthday) + ": " + mainActivity.getString(R.string.unknown_fem).toLowerCase());
        } else if (characterkieModel.getBirthday_format().equals(mainActivity.getString(R.string.dd_mm_yy))) {
            tv_birthdayViewCharacterkie.setText(mainActivity.getString(R.string.birthday) + ": " + characterkieModel.getBirthday());

        } else if (characterkieModel.getBirthday_format().equals(mainActivity.getString(R.string.mm_yy))) {
            tv_birthdayViewCharacterkie.setText(mainActivity.getString(R.string.birthday) + ": " + characterkieModel.getBirthday());
        } else if (characterkieModel.getBirthday_format().equals(mainActivity.getString(R.string.mm))) {
            tv_birthdayViewCharacterkie.setText(mainActivity.getString(R.string.birthday) + ": " + characterkieModel.getBirthday());
        } else if (characterkieModel.getBirthday_format().equals(mainActivity.getString(R.string.yyyy))) {
            tv_birthdayViewCharacterkie.setText(mainActivity.getString(R.string.birthday) + ": " + characterkieModel.getBirthday());
        }
    }
    private void getStrings(String key,String option){
        int resId = mainActivity.getResources().getIdentifier(key, "string", mainActivity.getPackageName());

        if (resId != 0) {
            String textString = mainActivity.getString(resId);
            switch (option) {
                case "gender":
                    tv_genderViewCharacterkie.setText(mainActivity.getString(R.string.gender) + ": " + textString.toLowerCase());
                    break;
                case "pronouns":
                    tv_pronounsViewCharacterkie.setText(mainActivity.getString(R.string.pronouns) + ": " + textString.toLowerCase());
                    break;
            }
        } else {
            switch (option) {
                case "gender":
                    tv_genderViewCharacterkie.setText(mainActivity.getString(R.string.gender)+": "+characterkieModel.getGender().toLowerCase());
                    break;
                case "pronouns":
                    tv_pronounsViewCharacterkie.setText(mainActivity.getString(R.string.pronouns)+": "+characterkieModel.getPronouns().toLowerCase());
                    break;
            }

        }
    }
    private void setListeners(){
        ib_back.setOnClickListener(this);
        bt_basic_info_characterkies_view.setOnClickListener(this);
    }
    private void initComponents(View view){
        tv_locked_characterkie = view.findViewById(R.id.tv_locked_characterkie);
        iv_locked_characterkie = view.findViewById(R.id.iv_locked_characterkie);
        tv_birthdayViewCharacterkie = view.findViewById(R.id.tv_characterkiesPreviewWorldkie);
        tv_genderViewCharacterkie = view.findViewById(R.id.tv_genderViewCharacterkie);
        tv_pronounsViewCharacterkie = view.findViewById(R.id.tv_pronounsViewCharacterkie);
        ib_characterkieView = view.findViewById(R.id.ib_characterkieView);
        tv_nameCharacterkieView = view.findViewById(R.id.tv_nameCharacterkieView);
        tv_nameUserCharacterkieView = view.findViewById(R.id.tv_nameUserCharacterkieView);
        tv_usernameCharacterkieView = view.findViewById(R.id.tv_usernameCharacterkieView);
        tv_nameWorldkieViewCharacterkie = view.findViewById(R.id.tv_nameWorldkieViewCharacterkie);
        bt_basic_info_characterkies_view = view.findViewById(R.id.bt_basic_info_characterkies_view);
        iv_background_basic_info_characterkie = view.findViewById(R.id.iv_background_basic_info_characterkie);
        tv_basic_info_characterkie_view = view.findViewById(R.id.tv_basic_info_characterkie_view);
        iv_basic_data_characterkie_view = view.findViewById(R.id.iv_basic_data_characterkie_view);
        tv_separator_basic_info_characterkie_view = view.findViewById(R.id.tv_separator_basic_info_characterkie_view);
        mainActivity = (MainActivity)getActivity();
        ib_save = mainActivity.getIb_save();
        ib_back = mainActivity.getBackButton();
        fragmentManager = mainActivity.getSupportFragmentManager();
        UID_AUTHOR = mode.equals("other") ? getArguments().getString("UID_AUTHOR") : mainActivity.getUID();
        showHideBasicInfo();
    }
private void setVisibility(){
    ib_save.setVisibility(View.GONE);
    ib_back.setVisibility(View.VISIBLE);
}

private void getProfilePhoto() {
    if (characterkieModel.isPhoto_default()) {
        int resId = mainActivity.getResources().getIdentifier(characterkieModel.getPhoto_id(), "mipmap", mainActivity.getPackageName());

        if (resId != 0) {
            Drawable drawable = ContextCompat.getDrawable(mainActivity, resId);
            ib_characterkieView.setImageDrawable(drawable);
                DrawableUtils.personalizarImagenCuadradoButton(mainActivity, 115 / 6, 7, R.color.brownMaroon, drawable, ib_characterkieView);
        }
    } else {
        mainActivity.getFirebaseStorageCharacterkies().getReference(UID).listAll().addOnSuccessListener(listResult -> {
            for (StorageReference item : listResult.getItems()) {
                if (item.getName().startsWith("cover")) {
                    item.getDownloadUrl().addOnSuccessListener(uri -> {
                        DrawableUtils.personalizarImagenCuadradoButton(mainActivity, 115 / 7, 7, R.color.greenWhatever, uri, ib_characterkieView);

                    });
                }
            }
        });
    }
    }
    private void showHideBasicInfo(){
        tv_birthdayViewCharacterkie.setVisibility(isBasicInfoVisible ? View.GONE : View.VISIBLE);
        tv_genderViewCharacterkie.setVisibility(isBasicInfoVisible ? View.GONE : View.VISIBLE);
        tv_pronounsViewCharacterkie.setVisibility(isBasicInfoVisible ? View.GONE : View.VISIBLE);
        bt_basic_info_characterkies_view.setBackgroundResource(isBasicInfoVisible ? R.drawable.twotone_arrow_drop_down_circle_24:R.drawable.twotone_arrow_circle_up_24);
        isBasicInfoVisible = !isBasicInfoVisible;
    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.ib_back){
            NavigationUtils.goBack(fragmentManager,mainActivity);
        }else if (v.getId()==R.id.bt_basic_info_characterkies_view) {
            showHideBasicInfo();
        }
    }
}