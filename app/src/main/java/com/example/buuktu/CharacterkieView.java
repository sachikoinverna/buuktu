package com.example.buuktu;

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

import com.example.buuktu.models.Characterkie;
import com.example.buuktu.models.UserkieModel;
import com.example.buuktu.models.WorldkieModel;
import com.example.buuktu.utils.DrawableUtils;
import com.example.buuktu.utils.NavigationUtils;
import com.example.buuktu.views.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CharacterkieView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CharacterkieView extends Fragment implements View.OnClickListener {
    TextView tv_birthdayViewCharacterkie,tv_pronounsViewCharacterkie,tv_genderViewCharacterkie,tv_nameCharacterkieView,tv_nameUserCharacterkieView,tv_usernameCharacterkieView,tv_nameWorldkieViewCharacterkie,tv_locked_characterkie;
    FirebaseFirestore db;
    ImageButton ib_worldkieView,ib_back,ib_save,ib_characterkieView;
    ImageView iv_locked_characterkie;
    FirebaseAuth firebaseAuth;
    String UID,UID_AUTHOR,UID_WORLDKIE,lastPhotoId="",mode;
    UserkieModel userkieModel;
    FragmentManager fragmentManager;
    CollectionReference collectionUserkies,collectionWorldkies,collectionStuffkies,collectionCharacterkies;
    WorldkieModel worldkieModel;
    MainActivity mainActivity;
    Characterkie characterkieModel;
    FirebaseStorage firebaseStorage;
    public CharacterkieView() {
        // Required empty public constructor
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_characterkie_view, container, false);
        initComponents(view);
        setVisibility();
        if (mode.equals("self")) {
            firebaseAuth = FirebaseAuth.getInstance();
            ib_worldkieView.setOnClickListener(this);
        }
        ib_worldkieView.setVisibility(mode.equals("self") ? View.VISIBLE : View.INVISIBLE);
        ib_back.setVisibility(mode.equals("self") ? View.GONE : View.VISIBLE);
        UID_AUTHOR = mode.equals("other") ? getArguments().getString("UID_AUTHOR") : firebaseAuth.getUid();
        setVar();
        getProfilePhoto();
        firebaseStorage = FirebaseStorage.getInstance();

        collectionWorldkies.document(UID).addSnapshotListener((documentSnapshot, e) -> {
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
        collectionUserkies.document(UID_AUTHOR).addSnapshotListener((document, exx) -> {
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

                    collectionCharacterkies.document(UID).addSnapshotListener((documentSnapshot, ex) -> {
                        if (ex != null) {
                            Log.e("Error", ex.getMessage());
                            Toast.makeText(getContext(), "Error al escuchar cambios: " + ex.getMessage(), LENGTH_LONG).show();
                            return;
                        }

                        if (documentSnapshot != null) {

                            characterkieModel = new Characterkie(
                                    documentSnapshot.getId(),
                                    documentSnapshot.getString("name")
                            );
                        }
                    });
        setListeners();
        getProfilePhoto();
        return view;
    }
    private void setListeners(){
        ib_back.setOnClickListener(this);
    }
    private void initComponents(View view){
        tv_birthdayViewCharacterkie = view.findViewById(R.id.tv_birthdayViewCharacterkie);
        tv_genderViewCharacterkie = view.findViewById(R.id.tv_genderViewCharacterkie);
        tv_pronounsViewCharacterkie = view.findViewById(R.id.tv_pronounsViewCharacterkie);
        ib_characterkieView = view.findViewById(R.id.ib_characterkieView);
        tv_nameCharacterkieView = view.findViewById(R.id.tv_nameCharacterkieView);
        tv_nameUserCharacterkieView = view.findViewById(R.id.tv_nameUserCharacterkieView);
        tv_usernameCharacterkieView = view.findViewById(R.id.tv_usernameCharacterkieView);
        tv_nameWorldkieViewCharacterkie = view.findViewById(R.id.tv_nameWorldkieViewCharacterkie);
        mainActivity = (MainActivity)getActivity();
        ib_save = mainActivity.getIb_save();
        ib_back = mainActivity.getBackButton();
        fragmentManager = mainActivity.getSupportFragmentManager();
    }
private void setVisibility(){
    ib_save.setVisibility(View.GONE);
    ib_back.setVisibility(View.VISIBLE);
}
private void setVar(){
    db = FirebaseFirestore.getInstance();
    collectionWorldkies = db.collection("Worldkies");
    collectionUserkies = db.collection("Users");
    collectionCharacterkies = db.collection("Characterkies");
}
private void getProfilePhoto() {
    if (characterkieModel.isPhoto_default()) {
        String id_photo = characterkieModel.getPhoto_id();
        int resId = mainActivity.getResources().getIdentifier(id_photo, "mipmap", mainActivity.getPackageName());

        if (resId != 0 && (!lastPhotoId.equals(id_photo))) {
            Drawable drawable = ContextCompat.getDrawable(mainActivity, resId);
            ib_worldkieView.setImageDrawable(drawable);
            try {
                DrawableUtils.personalizarImagenCuadradoButton(mainActivity, 115 / 6, 7, R.color.brownMaroon, drawable, ib_worldkieView);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            lastPhotoId = id_photo;
        }
    } else {
        StorageReference userFolderRef = FirebaseStorage.getInstance("gs://buuk-tu-characterkies").getReference(UID);

        userFolderRef.listAll().addOnSuccessListener(listResult -> {
            for (StorageReference item : listResult.getItems()) {
                if (item.getName().startsWith("cover")) {
                    item.getDownloadUrl().addOnSuccessListener(uri -> {
                        // try {
                        DrawableUtils.personalizarImagenCuadradoButton(mainActivity, 115 / 7, 7, R.color.greenWhatever, uri, ib_worldkieView);
                        //} catch (IOException e) {
                        //    throw new RuntimeException(e);
                        // }
                    });
                }
            }
        }).addOnFailureListener(e -> Log.e("Storage", "Error listando archivos: " + e.getMessage()));


        }
    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.ib_back){
            NavigationUtils.goBack(fragmentManager,mainActivity);
        }
    }
}