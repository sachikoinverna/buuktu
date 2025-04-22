package com.example.buuktu;

import static android.widget.Toast.LENGTH_LONG;

import android.animation.Animator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.example.buuktu.utils.EfectsUtils;
import com.example.buuktu.utils.NavigationUtils;
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
public class ProfileView extends Fragment implements View.OnClickListener {
    String mode;
    private ImageButton ib_profileView,ib_profileViewEdit,ib_save,ib_back;
    ImageView iv_locked_profile;
    private TextView tv_usernameProfileView,tv_nameProfileView,tv_worldkiesPreviewUserkie,tv_stuffkiesPreviewUserkie,tv_characterkiesPreviewUserkie,tv_locked_profile;
    ScrollView sv_pa;
    MaterialCardView cv_characterkiesPreviewUserkie,cv_stuffkiesPreviewUserkie,cv_worldkiesPreviewUserkie;
    ArrayList<Characterkie> characterkieArrayList;
    ArrayList<StuffkieModel> stuffkieArrayList;
    ArrayList<WorldkieModel> worldkieArrayList;
    FirebaseFirestore db;
    RecyclerView rc_characterkiePreviewUserkie,rc_stuffkiePreviewUserkie,rc_worldkiePreviewUserkie;
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
    FragmentManager fragmentManager;
    FragmentActivity activity;

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
        MainActivity mainActivity = (MainActivity) getActivity();
        ib_save = mainActivity.getIb_save();
        ib_back = mainActivity.getBackButton();
        ib_save.setVisibility(View.GONE);
        fragmentManager = requireActivity().getSupportFragmentManager();
        activity = requireActivity();
        initComponents(view);
        switch (mode) {
            case "self":
                firebaseAuth = FirebaseAuth.getInstance();
                UID = firebaseAuth.getUid();
                ib_profileViewEdit.setVisibility(View.VISIBLE);
                ib_profileViewEdit.setOnClickListener(this);
                ib_back.setVisibility(View.GONE);
                break;
            case "other":
                UID = getArguments().getString("UID");
                ib_profileViewEdit.setVisibility(View.INVISIBLE);
                ib_back.setOnClickListener(this);
                ib_back.setVisibility(View.VISIBLE);
                break;
        }
        DrawableUtils.personalizarImagenCircleButton(getContext(), DrawableUtils.drawableToBitmap(ib_profileView.getDrawable()), ib_profileView, R.color.brownBrown);
        db = FirebaseFirestore.getInstance();
        characterkieArrayList = new ArrayList<>();
        characterkiesUserPreviewAdapter = new CharacterkiesUserPreviewAdapter(characterkieArrayList, getContext());
        rc_characterkiePreviewUserkie.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rc_characterkiePreviewUserkie.setAdapter(characterkiesUserPreviewAdapter);

        worldkieArrayList = new ArrayList<>();
        worldkiesUserPreviewAdapter = new WorldkiesUserPreviewAdapter(worldkieArrayList, getContext(), fragmentManager); // <--- Crear el adaptador correcto
        rc_worldkiePreviewUserkie.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rc_worldkiePreviewUserkie.setAdapter(worldkiesUserPreviewAdapter); // <--- Asignar el adaptador correcto

        stuffkieArrayList = new ArrayList<>();
        stuffkiesUserPreviewAdapter = new StuffkiesUserPreviewAdapter(stuffkieArrayList, getContext()); // Asegúrate de que este también esté inicializado
        rc_stuffkiePreviewUserkie.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rc_stuffkiePreviewUserkie.setAdapter(stuffkiesUserPreviewAdapter);
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
                userkieModel = new UserkieModel(firebaseAuth.getUid(), documentSnapshot.getString("name"), R.drawable.thumb_custom, documentSnapshot.getString("username"), documentSnapshot.getBoolean("photo_default"), documentSnapshot.getBoolean("private"));
                tv_nameProfileView.setText(userkieModel.getName());
                tv_usernameProfileView.setText(userkieModel.getUsername());
                if ((!userkieModel.isProfile_private() && mode.equals("other")) || (mode.equals("self"))){
                    collectionStuffkies.addSnapshotListener((queryDocumentSnapshots, ex) -> {
                        if (ex != null) {
                            Log.e("Error", ex.getMessage());
                            Toast.makeText(getContext(), "Error al escuchar cambios: " + e.getMessage(), LENGTH_LONG).show();
                            return;
                        }

                        if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                            stuffkieArrayList.clear(); // Limpia la lista antes de agregar nuevos datos

                            boolean foundData = false; // Add a flag

                            for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                                //if (documentSnapshot.getBoolean("photo_default")) {
                                if (doc.getString("UID_AUTHOR").equals(UID)) {

                                    Drawable drawable = getResources().getDrawable(R.drawable.thumb_custom);
                                    StuffkieModel stuffkieModel = new StuffkieModel(
                                            doc.getId(),
                                            doc.getString("name"),
                                            Boolean.TRUE.equals(doc.getBoolean("stuffkie_private")),
                                            R.drawable.thumb_custom
                                    );
                                    Log.d("StuffkiesSearch", "Stuffkie encontrado: " + doc.getString("name"));

                                    stuffkieArrayList.add(stuffkieModel);
                                    foundData = true; // Set the flag to true if data is found

                                }
                            }
                            if (foundData) {
                                tv_stuffkiesPreviewUserkie.setVisibility(View.VISIBLE);
                                cv_stuffkiesPreviewUserkie.setVisibility(View.VISIBLE);
                                updateRecyclerViewStuffkies(stuffkieArrayList);

                            }
                        } else {
                            tv_stuffkiesPreviewUserkie.setVisibility(View.GONE);
                            cv_stuffkiesPreviewUserkie.setVisibility(View.GONE);
                            updateRecyclerViewStuffkies(new ArrayList<>());

                        }
                    });
                    collectionWorldkies.addSnapshotListener((queryDocumentSnapshots, ex) -> {
                        if (ex != null) {
                            Log.e("Error", ex.getMessage());
                            Toast.makeText(getContext(), "Error al escuchar cambios: " + e.getMessage(), LENGTH_LONG).show();
                            return;
                        }

                        if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                            worldkieArrayList.clear(); // Limpia la lista antes de agregar nuevos datos
                            boolean foundData = false; // Add a flag

                            for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                                //if (documentSnapshot.getBoolean("photo_default")) {
                                if (doc.getString("UID_AUTHOR").equals(UID)) {

                                    Drawable drawable = getResources().getDrawable(R.drawable.thumb_custom);
                                    WorldkieModel worldkieModel = new WorldkieModel(
                                            doc.getId(),
                                            doc.getString("UID_AUTHOR"),
                                            R.drawable.thumb_custom,
                                            doc.getString("name"), doc.getDate("creation_date"),
                                            true, doc.getDate("last_update"), doc.getBoolean("worldkie_private")
                                    );
                                    Log.d("StuffkiesSearch", "Stuffkie encontrado: " + documentSnapshot.getString("name"));

                                    worldkieArrayList.add(worldkieModel);
                                    foundData = true; // Set the flag to true if data is found
                                }
                            }
                            if (foundData) {
                                tv_worldkiesPreviewUserkie.setVisibility(View.VISIBLE);
                                cv_worldkiesPreviewUserkie.setVisibility(View.VISIBLE);
                                updateRecyclerViewWorldkies(worldkieArrayList);
                            } else {
                                tv_worldkiesPreviewUserkie.setVisibility(View.GONE);
                                cv_worldkiesPreviewUserkie.setVisibility(View.GONE);
                                updateRecyclerViewWorldkies(new ArrayList<>());
                            }
                        } else {
                            tv_worldkiesPreviewUserkie.setVisibility(View.GONE);
                            cv_worldkiesPreviewUserkie.setVisibility(View.GONE);
                            updateRecyclerViewWorldkies(new ArrayList<>());
                        }
                    });
                    collectionCharacterkies.addSnapshotListener((queryDocumentSnapshots, ex) -> {
                        if (ex != null) {
                            Log.e("Error", e.getMessage());
                            Toast.makeText(getContext(), "Error al escuchar cambios: " + e.getMessage(), LENGTH_LONG).show();
                            return;
                        }

                        if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                            characterkieArrayList.clear(); // Limpia la lista antes de agregar nuevos datos
                            boolean foundData = false; // Add a flag

                            for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                                //if (documentSnapshot.getBoolean("photo_default")) {
                                if (doc.getString("UID_AUTHOR").equals(UID)) {

                                    Drawable drawable = getResources().getDrawable(R.drawable.thumb_custom);
                                    Characterkie characterkieModel = new Characterkie(
                                            doc.getId(),
                                            doc.getString("name")
                                    );
                                    Log.d("StuffkiesSearch", "Stuffkie encontrado: " + doc.getString("name"));

                                    characterkieArrayList.add(characterkieModel);
                                    foundData = true; // Set the flag to true if data is found
                                }
                            }
                            if (foundData) {
                                tv_characterkiesPreviewUserkie.setVisibility(View.VISIBLE);
                                cv_characterkiesPreviewUserkie.setVisibility(View.VISIBLE);
                                updateRecyclerViewCharacterkies(characterkieArrayList);
                            } else {
                                tv_characterkiesPreviewUserkie.setVisibility(View.GONE);
                                cv_characterkiesPreviewUserkie.setVisibility(View.GONE);
                                updateRecyclerViewCharacterkies(new ArrayList<>());

                            }
                        }
                        ;
                    });
                }
                else{
                    tv_characterkiesPreviewUserkie.setVisibility(View.GONE);
                    cv_characterkiesPreviewUserkie.setVisibility(View.GONE);
                    tv_worldkiesPreviewUserkie.setVisibility(View.GONE);
                    cv_worldkiesPreviewUserkie.setVisibility(View.GONE);
                    tv_stuffkiesPreviewUserkie.setVisibility(View.GONE);
                    cv_stuffkiesPreviewUserkie.setVisibility(View.GONE);
                    iv_locked_profile.setVisibility(View.VISIBLE);
                    tv_locked_profile.setVisibility(View.VISIBLE);
                }
            }
        });

        getProfilePhoto();
        return view;

    }
    private void initComponents(View view){
        rc_worldkiePreviewUserkie = view.findViewById(R.id.rc_worldkiePreviewUserkie);
        rc_stuffkiePreviewUserkie = view.findViewById(R.id.rc_stuffkiePreviewUserkie);
        rc_characterkiePreviewUserkie = view.findViewById(R.id.rc_characterkiePreviewUserkie);
        cv_worldkiesPreviewUserkie = view.findViewById(R.id.cv_worldkiesPreviewUserkie);
        cv_stuffkiesPreviewUserkie = view.findViewById(R.id.cv_stuffkiesPreviewUserkie);
        cv_characterkiesPreviewUserkie = view.findViewById(R.id.cv_characterkiesPreviewUserkie);
        ib_profileView = view.findViewById(R.id.ib_profileView);
        ib_profileViewEdit = view.findViewById(R.id.ib_profileViewEdit);
        tv_usernameProfileView = view.findViewById(R.id.tv_usernameProfileView);
        tv_nameProfileView = view.findViewById(R.id.tv_nameProfileView);
        tv_characterkiesPreviewUserkie = view.findViewById(R.id.tv_characterkiesPreviewUserkie);
        tv_stuffkiesPreviewUserkie = view.findViewById(R.id.tv_stuffkiesPreviewUserkie);
        tv_worldkiesPreviewUserkie = view.findViewById(R.id.tv_worldkiesPreviewUserkie);
        iv_locked_profile =view.findViewById(R.id.iv_locked_profile);
        tv_locked_profile = view.findViewById(R.id.tv_locked_profile);
    }
    private void getProfilePhoto(){
        ib_profileView.setVisibility(View.INVISIBLE);
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
                               // ib_profileView.setImageDrawable(bitmapScaled);
                                DrawableUtils.personalizarImagenCircleButton(getContext(), bitmapScaled, ib_profileView, R.color.brownMaroon);
                            });
                            break;
                        }
                    }
                });
            }
            ib_profileView.setVisibility(View.VISIBLE);
            EfectsUtils.startCircularReveal(ib_profileView.getDrawable(),ib_profileView);
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
        rc_stuffkiePreviewUserkie.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rc_stuffkiePreviewUserkie.setAdapter(stuffkiesUserPreviewAdapter);
    }
    private void updateRecyclerViewWorldkies(ArrayList<WorldkieModel> worldkieArrayList) {
        worldkiesUserPreviewAdapter = new WorldkiesUserPreviewAdapter(worldkieArrayList,getContext(),fragmentManager);
        rc_worldkiePreviewUserkie.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rc_worldkiePreviewUserkie.setAdapter(worldkiesUserPreviewAdapter);
    }
    private void updateRecyclerViewCharacterkies(ArrayList<Characterkie> characterkieArrayList) {
        characterkiesUserPreviewAdapter = new CharacterkiesUserPreviewAdapter(characterkieArrayList,getContext());
        rc_characterkiePreviewUserkie.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rc_characterkiePreviewUserkie.setAdapter(characterkiesUserPreviewAdapter);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.ib_back){
            NavigationUtils.goBack(fragmentManager,activity);
        } else if (v.getId() == R.id.ib_profileView) {
            InfoFutureFunctionDialog infoFutureFunctionDialog = new InfoFutureFunctionDialog(getContext());
            infoFutureFunctionDialog.show();
        }
    }
}