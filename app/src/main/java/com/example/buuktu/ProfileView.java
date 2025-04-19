package com.example.buuktu;

import static android.widget.Toast.LENGTH_LONG;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buuktu.adapters.CharacterkiesUserPreviewAdapter;
import com.example.buuktu.adapters.WorldkiesUserPreviewAdapter;
import com.example.buuktu.dialogs.InfoFutureFunctionDialog;
import com.example.buuktu.models.Characterkie;
import com.example.buuktu.models.StuffkieModel;
import com.example.buuktu.models.UserkieModel;
import com.example.buuktu.models.WorldkieModel;
import com.example.buuktu.utils.DrawableUtils;
import com.example.buuktu.views.MainActivity;
import com.google.android.gms.common.util.UidVerifier;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileView extends Fragment {
    String mode;
    private ImageButton ib_profileView,ib_profileViewEdit;
    private TextView tv_usernameProfileView,tv_nameProfileView;
    ScrollView sv_pa;
    MaterialCardView cv_characterkiesPreviewUserSelf,cv_stuffkiesPreviewUserSelf,cv_worldkiesPreviewUserSelf;
    ArrayList<Characterkie> characterkieArrayList;
    ArrayList<StuffkieModel> stuffkieArrayList;
    ArrayList<WorldkieModel> worldkieArrayList;
    FirebaseFirestore db;
    RecyclerView rc_characterkiePreviewUserSelf,rc_stuffkiePreviewUserSelf,rc_worldkiePreviewUserSelf;
    CharacterkiesUserPreviewAdapter characterkiesUserPreviewAdapter;
    StuffkiesUserPreviewAdapter stuffkiesUserPreviewAdapter;
    WorldkiesUserPreviewAdapter worldkiesUserPreviewAdapter;
    CollectionReference collectionUserkies;
    CollectionReference collectionWorldkies;
    CollectionReference collectionStuffkies;
    CollectionReference collectionCharacterkies;
    FirebaseAuth firebaseAuth;
    String UID;
    UserkieModel userkieModel;
    ImageButton ib_save;
    public ProfileView() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProfileView.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileView newInstance(String param1, String param2) {
        return new ProfileView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mode = getArguments().getString("mode");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_view, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        MainActivity mainActivity = (MainActivity)getActivity();
        ib_save = mainActivity.getIb_save();
        ib_save.setVisibility(View.GONE);
        rc_worldkiePreviewUserSelf = view.findViewById(R.id.rc_worldkiePreviewUserSelf);
        rc_stuffkiePreviewUserSelf = view.findViewById(R.id.rc_stuffkiePreviewUserSelf);
        rc_characterkiePreviewUserSelf = view.findViewById(R.id.rc_characterkiePreviewUserSelf);
        cv_worldkiesPreviewUserSelf = view.findViewById(R.id.cv_worldkiesPreviewUserSelf);
        cv_stuffkiesPreviewUserSelf = view.findViewById(R.id.cv_stuffkiesPreviewUserSelf);
        cv_characterkiesPreviewUserSelf = view.findViewById(R.id.cv_characterkiesPreviewUserSelf);
        ib_profileView = view.findViewById(R.id.ib_profileView);
        ib_profileViewEdit = view.findViewById(R.id.ib_profileViewEdit);
        tv_usernameProfileView = view.findViewById(R.id.tv_usernameProfileView);
        tv_nameProfileView = view.findViewById(R.id.tv_nameProfileView);
        switch (mode)
        {
            case "self":
                firebaseAuth = FirebaseAuth.getInstance();
                UID = firebaseAuth.getUid();
                ib_profileViewEdit.setVisibility(View.VISIBLE);
                ib_profileViewEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        InfoFutureFunctionDialog infoFutureFunctionDialog = new InfoFutureFunctionDialog(getContext());
                        infoFutureFunctionDialog.show();
                    }
                });
                break;
            case "other":
                UID = getArguments().getString("UID");
                ib_profileViewEdit.setVisibility(View.INVISIBLE);
                break;
        }
        DrawableUtils.personalizarImagenCircleButton(getContext(), DrawableUtils.drawableToBitmap(ib_profileView.getDrawable()), ib_profileView, R.color.brownBrown);
        ib_profileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        db = FirebaseFirestore.getInstance();
        characterkieArrayList = new ArrayList<>();
        characterkieArrayList.add(new Characterkie("232532", "lALA"));
        characterkieArrayList.add(new Characterkie("232532", "lALSA"));
        characterkieArrayList.add(new Characterkie("232532", "lALSFA"));
        characterkieArrayList.add(new Characterkie("232532", "lAFLSA"));
        characterkieArrayList.add(new Characterkie("232532", "lASLAF"));
        characterkiesUserPreviewAdapter = new CharacterkiesUserPreviewAdapter(characterkieArrayList, getContext());
        rc_characterkiePreviewUserSelf.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rc_characterkiePreviewUserSelf.setAdapter(characterkiesUserPreviewAdapter);
        worldkieArrayList = new ArrayList<>();
        characterkieArrayList.add(new Characterkie("232532", "lALA"));
        characterkiesUserPreviewAdapter = new CharacterkiesUserPreviewAdapter(characterkieArrayList, getContext());
        rc_characterkiePreviewUserSelf.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rc_characterkiePreviewUserSelf.setAdapter(characterkiesUserPreviewAdapter);

        stuffkieArrayList = new ArrayList<>();
        characterkieArrayList.add(new Characterkie("232532", "lALA"));

        collectionUserkies = db.collection("Users");
        collectionStuffkies = db.collection("Stuffkies");
        collectionCharacterkies = db.collection("Characterkies");
        collectionWorldkies = db.collection("Worldkies");
        collectionUserkies.document(UID).addSnapshotListener((documentSnapshot, e) -> {
            if (e != null) {
                Log.e("Error", e.getMessage());
                Toast.makeText(getContext(), "Error al escuchar cambios: " + e.getMessage(), LENGTH_LONG).show();
                return;
            }

            if (documentSnapshot != null) {
                userkieModel = new UserkieModel(firebaseAuth.getUid(),documentSnapshot.getString("name"),R.drawable.add_button,documentSnapshot.getString("username"),documentSnapshot.getBoolean("photo_default"),true);
                tv_nameProfileView.setText(userkieModel.getName());
                tv_usernameProfileView.setText(userkieModel.getUsername());
            }
        });
        collectionStuffkies.addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e != null) {
                Log.e("Error", e.getMessage());
                Toast.makeText(getContext(), "Error al escuchar cambios: " + e.getMessage(), LENGTH_LONG).show();
                return;
            }

            if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                stuffkieArrayList.clear(); // Limpia la lista antes de agregar nuevos datos

                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                    //if (documentSnapshot.getBoolean("photo_default")) {
                    if (!documentSnapshot.getId().equals(firebaseAuth.getUid())) {

                        Drawable drawable = getResources().getDrawable(R.drawable.worldkie_default);
                        StuffkieModel stuffkieModel = new StuffkieModel(
                                documentSnapshot.getId(),
                                documentSnapshot.getString("name"),
                                Boolean.TRUE.equals(documentSnapshot.getBoolean("stuffkie_private")),
                                R.drawable.cloudlogin
                        );
                        Log.d("StuffkiesSearch", "Stuffkie encontrado: " + documentSnapshot.getString("name"));

                        stuffkieArrayList.add(stuffkieModel);
                        updateRecyclerViewStuffkies(stuffkieArrayList);
                    }
                }
            }
        });
        collectionWorldkies.addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e != null) {
                Log.e("Error", e.getMessage());
                Toast.makeText(getContext(), "Error al escuchar cambios: " + e.getMessage(), LENGTH_LONG).show();
                return;
            }

            if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                worldkieArrayList.clear(); // Limpia la lista antes de agregar nuevos datos

                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                    //if (documentSnapshot.getBoolean("photo_default")) {
                    if (!documentSnapshot.getId().equals(firebaseAuth.getUid())) {

                        Drawable drawable = getResources().getDrawable(R.drawable.worldkie_default);
                        WorldkieModel worldkieModel = new WorldkieModel(
                                documentSnapshot.getId(),
                                documentSnapshot.getString("UID_AUTHOR"),
                                R.drawable.cloudlogin,
                                documentSnapshot.getString("name"), documentSnapshot.getDate("creation_date"),
                                true, documentSnapshot.getDate("last_update"), documentSnapshot.getBoolean("worldkie_private")
                        );
                        Log.d("StuffkiesSearch", "Stuffkie encontrado: " + documentSnapshot.getString("name"));

                        worldkieArrayList.add(worldkieModel);
                        updateRecyclerViewWorldkies(worldkieArrayList);
                    }
                }
            }
        });
        collectionCharacterkies.addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e != null) {
                Log.e("Error", e.getMessage());
                Toast.makeText(getContext(), "Error al escuchar cambios: " + e.getMessage(), LENGTH_LONG).show();
                return;
            }

            if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                characterkieArrayList.clear(); // Limpia la lista antes de agregar nuevos datos

                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                    //if (documentSnapshot.getBoolean("photo_default")) {
                    if (!documentSnapshot.getId().equals(firebaseAuth.getUid())) {

                        Drawable drawable = getResources().getDrawable(R.drawable.worldkie_default);
                        Characterkie characterkieModel = new Characterkie(
                                documentSnapshot.getId(),
                                documentSnapshot.getString("name")
                        );
                        Log.d("StuffkiesSearch", "Stuffkie encontrado: " + documentSnapshot.getString("name"));

                        characterkieArrayList.add(characterkieModel);
                        updateRecyclerViewCharacterkies(characterkieArrayList);
                    }
                }
            }
        });
        getProfilePhoto();
        return view;
    }
    private void getProfilePhoto(){
        db.collection("Users").document(UID).addSnapshotListener((queryDocumentSnapshot, e) -> {
            if (e != null) {
                Log.e("Error", e.getMessage());
                Toast.makeText(getContext(), "Error al escuchar cambios: " + e.getMessage(), LENGTH_LONG).show();
                return;
            }
            //boolean photo_default = queryDocumentSnapshot.getBoolean("photo_default");
            if(userkieModel.isPhoto_default()) {
                String id_photo = queryDocumentSnapshot.getString("photo_id");
                int resId = getResources().getIdentifier(id_photo, "mipmap", getContext().getPackageName());

                if (resId != 0) {
                    Drawable drawable = ContextCompat.getDrawable(getContext(), resId);
                    ib_profileView.setImageDrawable(drawable);
                    DrawableUtils.personalizarImagenCircleButton(getContext(), DrawableUtils.drawableToBitmap(drawable), ib_profileView, R.color.brownMaroon);
                }

            } else {
                StorageReference userFolderRef = FirebaseStorage.getInstance("gs://buuk-tu-users").getReference(UID);//.child().child(UID);

                userFolderRef.listAll().addOnSuccessListener(listResult -> {
                    for (StorageReference item : listResult.getItems()) {
                        if (item.getName().startsWith("profile")) {
                            item.getBytes(5 * 1024 * 1024).addOnSuccessListener(bytes -> {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                Bitmap bitmapScaled = Bitmap.createScaledBitmap(bitmap, 80, 80, false);
                                DrawableUtils.personalizarImagenCircleButton(getContext(), bitmapScaled, ib_profileView, R.color.brownMaroon);
                            });
                            break;
                        }
                    }
                });
            }
/*.addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al buscar imagen", Toast.LENGTH_SHORT).show();
                    Log.e("Storage", "Error listando archivos: " + e.getMessage());
                })*/;


            // }
            //}
        });//)
    }
    private void updateRecyclerViewStuffkies(ArrayList<StuffkieModel> stuffkieArrayList) {
        stuffkiesUserPreviewAdapter = new StuffkiesUserPreviewAdapter(stuffkieArrayList,getContext());
        rc_stuffkiePreviewUserSelf.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rc_stuffkiePreviewUserSelf.setAdapter(stuffkiesUserPreviewAdapter);
    }
    private void updateRecyclerViewWorldkies(ArrayList<WorldkieModel> worldkieArrayList) {
        worldkiesUserPreviewAdapter = new WorldkiesUserPreviewAdapter(worldkieArrayList,getContext());
        rc_worldkiePreviewUserSelf.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rc_worldkiePreviewUserSelf.setAdapter(worldkiesUserPreviewAdapter);
    }
    private void updateRecyclerViewCharacterkies(ArrayList<Characterkie> characterkieArrayList) {
        characterkiesUserPreviewAdapter = new CharacterkiesUserPreviewAdapter(characterkieArrayList,getContext());
        rc_characterkiePreviewUserSelf.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rc_characterkiePreviewUserSelf.setAdapter(characterkiesUserPreviewAdapter);
    }
}