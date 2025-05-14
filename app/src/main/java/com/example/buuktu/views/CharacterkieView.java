package com.example.buuktu.views;

import static android.widget.Toast.LENGTH_LONG;

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
import android.widget.Toast;

import com.example.buuktu.R;
import com.example.buuktu.models.CharacterkieModel;
import com.example.buuktu.models.UserkieModel;
import com.example.buuktu.models.WorldkieModel;
import com.example.buuktu.utils.DrawableUtils;
import com.example.buuktu.utils.NavigationUtils;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;


public class CharacterkieView extends Fragment implements View.OnClickListener {
    TextView tv_birthdayViewCharacterkie,tv_pronounsViewCharacterkie,tv_genderViewCharacterkie,tv_nameCharacterkieView,tv_nameUserCharacterkieView,tv_usernameCharacterkieView,tv_nameWorldkieViewCharacterkie,tv_locked_characterkie,tv_statusViewCharacterkie;
    ImageButton bt_basic_info_characterkies_view,ib_back,ib_save,ib_characterkieView;
    ImageView iv_locked_characterkie;
    String UID,UID_AUTHOR,UID_WORLDKIE,lastPhotoId="",mode;
    UserkieModel userkieModel;
    FragmentManager fragmentManager;
    WorldkieModel worldkieModel;
    MainActivity mainActivity;
    CharacterkieModel characterkieModel;
    boolean isBasicInfoVisible;
    final String[]meses= new String[]{"Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"};
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_characterkie_view, container, false);
        initComponents(view);
        setVisibility();
        if (mode.equals("self")) {
        }
      //  ib_worldkieView.setVisibility(mode.equals("self") ? View.VISIBLE : View.INVISIBLE);
        UID_AUTHOR = mode.equals("other") ? getArguments().getString("UID_AUTHOR") : mainActivity.getUID();
        mainActivity.getCollectionWorldkies().document(UID_WORLDKIE).addSnapshotListener((documentSnapshot, e) -> {
            if (e != null) {
                Log.e("Error", e.getMessage());
                Toast.makeText(getContext(), "Error al escuchar cambios: " + e.getMessage(), LENGTH_LONG).show();
                return;
            }

            if (documentSnapshot != null) {
                worldkieModel = WorldkieModel.fromSnapshot(documentSnapshot);
                tv_nameWorldkieViewCharacterkie.setText(worldkieModel.getName());
            }
        });
        mainActivity.getCollectionUsers().document(UID_AUTHOR).addSnapshotListener((document, exx) -> {
                    if (exx != null) {
                        Log.e("Error", exx.getMessage());
                        Toast.makeText(getContext(), "Error al escuchar cambios: " + exx.getMessage(), LENGTH_LONG).show();
                        return;
                    }

                    if (document != null) {
                        UserkieModel userkieModel = UserkieModel.fromSnapshot(document);
                        tv_nameUserCharacterkieView.setText(userkieModel.getName());
                        tv_usernameCharacterkieView.setText(userkieModel.getUsername());
                    }
                }
        );

                    mainActivity.getCollectionCharacterkies().document(UID).addSnapshotListener((documentSnapshot, ex) -> {
                        if (ex != null) {
                            Log.e("Error", ex.getMessage());
                            Toast.makeText(getContext(), "Error al escuchar cambios: " + ex.getMessage(), LENGTH_LONG).show();
                            return;
                        }

                        if (documentSnapshot != null) {
                            characterkieModel = CharacterkieModel.fromSnapshot(documentSnapshot);
                            tv_nameCharacterkieView.setText(characterkieModel.getName());
                            getStrings(characterkieModel.getPronouns(),"pronouns");
                            getStrings(characterkieModel.getGender(),"gender");
                            getStrings(characterkieModel.getStatus(),"status");
                            getStrings(characterkieModel.getBirthday_format(),"birthday");
                            getProfilePhoto();
                        }
                    });
        setListeners();
        return view;
    }
    private void getStrings(String key,String option){
        int resId = mainActivity.getResources().getIdentifier(key, "string", mainActivity.getPackageName());

        if (resId != 0) {
            String textString = mainActivity.getString(resId);
            switch (option) {
                case "gender":
                    tv_genderViewCharacterkie.setText(mainActivity.getString(R.string.gender) + ": " + textString);
                    break;
                case "pronouns":
                    tv_pronounsViewCharacterkie.setText(mainActivity.getString(R.string.pronouns) + ": " + textString);
                    break;
                case "status":
                    tv_statusViewCharacterkie.setText(mainActivity.getString(R.string.status) + ": " + textString);
                    break;
                case "birthday":
                    if (characterkieModel.getBirthday_format().equalsIgnoreCase(mainActivity.getString(R.string.unknown_fem))) {

                        tv_birthdayViewCharacterkie.setText(mainActivity.getString(R.string.birthday) + ": " + mainActivity.getString(R.string.unknown_fem));
                    } else if (characterkieModel.getBirthday_format().equals(mainActivity.getString(R.string.dd_mm_yy))) {
                        tv_birthdayViewCharacterkie.setText(mainActivity.getString(R.string.birthday) + ": " + characterkieModel.getBirthday());

                    } else if (characterkieModel.getBirthday_format().equals(mainActivity.getString(R.string.mm_yy))) {
                        String[] month = characterkieModel.getBirthday().split("/");
                        tv_birthdayViewCharacterkie.setText(mainActivity.getString(R.string.birthday) + ": " + meses[Integer.parseInt(month[0])] + " de " + month[1]);
                    } else if (characterkieModel.getBirthday_format().equals(mainActivity.getString(R.string.mm))) {
                        tv_birthdayViewCharacterkie.setText(mainActivity.getString(R.string.birthday) + ": " + meses[Integer.parseInt(characterkieModel.getBirthday())]);
                    } else if (characterkieModel.getBirthday_format().equals(mainActivity.getString(R.string.yyyy))) {
                        tv_birthdayViewCharacterkie.setText(mainActivity.getString(R.string.birthday) + ": " + characterkieModel.getBirthday());
                    }
                    break;
            }
        } else {
            switch (option) {
                case "gender":
                    tv_genderViewCharacterkie.setText(mainActivity.getString(R.string.gender)+": "+characterkieModel.getGender());
                    break;
                case "pronouns":
                    tv_pronounsViewCharacterkie.setText(mainActivity.getString(R.string.pronouns)+": "+characterkieModel.getPronouns());
                    break;
                case "status":
                    tv_statusViewCharacterkie.setText(mainActivity.getString(R.string.status)+": "+characterkieModel.getGender());
                    break;
            }

        }
    }
    private void setListeners(){
        ib_back.setOnClickListener(this);
        bt_basic_info_characterkies_view.setOnClickListener(this);
    }
    private void initComponents(View view){
        tv_birthdayViewCharacterkie = view.findViewById(R.id.tv_birthdayViewCharacterkie);
        tv_genderViewCharacterkie = view.findViewById(R.id.tv_genderViewCharacterkie);
        tv_pronounsViewCharacterkie = view.findViewById(R.id.tv_pronounsViewCharacterkie);
        tv_statusViewCharacterkie = view.findViewById(R.id.tv_statusViewCharacterkie);
        ib_characterkieView = view.findViewById(R.id.ib_characterkieView);
        tv_nameCharacterkieView = view.findViewById(R.id.tv_nameCharacterkieView);
        tv_nameUserCharacterkieView = view.findViewById(R.id.tv_nameUserCharacterkieView);
        tv_usernameCharacterkieView = view.findViewById(R.id.tv_usernameCharacterkieView);
        tv_nameWorldkieViewCharacterkie = view.findViewById(R.id.tv_nameWorldkieViewCharacterkie);
        bt_basic_info_characterkies_view = view.findViewById(R.id.bt_basic_info_characterkies_view);
        mainActivity = (MainActivity)getActivity();
        ib_save = mainActivity.getIb_save();
        ib_back = mainActivity.getBackButton();
        fragmentManager = mainActivity.getSupportFragmentManager();
        showHideBasicInfo();
    }
private void setVisibility(){
    ib_save.setVisibility(View.GONE);
    ib_back.setVisibility(View.VISIBLE);
}

private void getProfilePhoto() {
    if (characterkieModel.isPhoto_default()) {
        String id_photo = characterkieModel.getPhoto_id();
        int resId = mainActivity.getResources().getIdentifier(id_photo, "mipmap", mainActivity.getPackageName());

        if (resId != 0 && (!lastPhotoId.equals(id_photo))) {
            Drawable drawable = ContextCompat.getDrawable(mainActivity, resId);
            ib_characterkieView.setImageDrawable(drawable);
            try {
                DrawableUtils.personalizarImagenCuadradoButton(mainActivity, 115 / 6, 7, R.color.brownMaroon, drawable, ib_characterkieView);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            lastPhotoId = id_photo;
        }
    } else {
        StorageReference userFolderRef = mainActivity.getFirebaseStorageCharacterkies().getReference(UID);

        userFolderRef.listAll().addOnSuccessListener(listResult -> {
            for (StorageReference item : listResult.getItems()) {
                if (item.getName().startsWith("cover")) {
                    item.getDownloadUrl().addOnSuccessListener(uri -> {
                        // try {
                        DrawableUtils.personalizarImagenCuadradoButton(mainActivity, 115 / 7, 7, R.color.greenWhatever, uri, ib_characterkieView);
                        //} catch (IOException e) {
                        //    throw new RuntimeException(e);
                        // }
                    });
                }
            }
        }).addOnFailureListener(e -> Log.e("Storage", "Error listando archivos: " + e.getMessage()));


        }
    }
    private void showHideBasicInfo(){
        tv_birthdayViewCharacterkie.setVisibility(isBasicInfoVisible ? View.GONE : View.VISIBLE);
        tv_genderViewCharacterkie.setVisibility(isBasicInfoVisible ? View.GONE : View.VISIBLE);
        tv_pronounsViewCharacterkie.setVisibility(isBasicInfoVisible ? View.GONE : View.VISIBLE);
        tv_statusViewCharacterkie.setVisibility(isBasicInfoVisible ? View.GONE : View.VISIBLE);
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