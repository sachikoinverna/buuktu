package com.example.buuktu;

import static android.widget.Toast.LENGTH_LONG;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buuktu.adapters.CharacterkiesUserPreviewAdapter;
import com.example.buuktu.adapters.StuffkiesUserPreviewAdapter;
import com.example.buuktu.dialogs.InfoFutureFunctionDialog;
import com.example.buuktu.models.Characterkie;
import com.example.buuktu.models.StuffkieModel;
import com.example.buuktu.models.UserkieModel;
import com.example.buuktu.models.WorldkieModel;
import com.example.buuktu.utils.DrawableUtils;
import com.example.buuktu.utils.NavigationUtils;
import com.example.buuktu.views.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WorldkieView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorldkieView extends Fragment implements View.OnClickListener {
    ArrayList<Characterkie> characterkieArrayList = new ArrayList<>();
    ArrayList<StuffkieModel> stuffkieArrayList = new ArrayList<>();
    FirebaseFirestore db;
    ImageButton ib_worldkieView,ib_back,ib_save;
    ImageView iv_locked_worldkie;
    TextView tv_nameWorldkieView,tv_nameUserWorldkieView,tv_usernameWorldkieView,tv_creationDateWorldkieView,tv_lastUpdateWorldkieView,tv_characterkiesPreviewWorldkie,tv_stuffkiesPreviewWorldkie,tv_locked_worldkie;
    CardView cv_characterkiesPreviewWorldkie,cv_stuffkiesPreviewWorldkie;
    RecyclerView rc_characterkiesPrevieWorldkie,rc_stuffkiesPreviewWorldkie;
    FirebaseAuth firebaseAuth;
    String UID,UID_AUTHOR,lastPhotoId="",mode;
    UserkieModel userkieModel;
    FragmentManager fragmentManager;
    CharacterkiesUserPreviewAdapter characterkiesUserPreviewAdapter;
    StuffkiesUserPreviewAdapter stuffkiesUserPreviewAdapter;
    CollectionReference collectionUserkies,collectionWorldkies,collectionStuffkies,collectionCharacterkies;
    WorldkieModel worldkieModel;
    MainActivity mainActivity;
    public WorldkieView() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment WorldkieView.
     */
    // TODO: Rename and change types and number of parameters
    public static WorldkieView newInstance() {
        return new WorldkieView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mode = getArguments().getString("mode");
            UID = getArguments().getString("UID");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_worldkie_view, container, false);
        setVar();
        initComponents(view);
        setVisibility();
        worldkieModel = new WorldkieModel();
        switch (mode)
        {
            case "self":
                firebaseAuth = FirebaseAuth.getInstance();
                ib_worldkieView.setOnClickListener(this);
                break;
        }
        ib_worldkieView.setVisibility(mode.equals("self")?View.VISIBLE:View.INVISIBLE);
        ib_back.setVisibility(mode.equals("self")?View.GONE:View.VISIBLE);
        UID_AUTHOR = mode.equals("other")?getArguments().getString("UID_AUTHOR"):firebaseAuth.getUid();


        getProfilePhoto();
        db = FirebaseFirestore.getInstance();
        characterkiesUserPreviewAdapter = new CharacterkiesUserPreviewAdapter(characterkieArrayList, getContext());
        rc_characterkiesPrevieWorldkie.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rc_characterkiesPrevieWorldkie.setAdapter(characterkiesUserPreviewAdapter);
        characterkiesUserPreviewAdapter = new CharacterkiesUserPreviewAdapter(characterkieArrayList, getContext());
        rc_characterkiesPrevieWorldkie.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rc_characterkiesPrevieWorldkie.setAdapter(characterkiesUserPreviewAdapter);

        collectionWorldkies = db.collection("Worldkies");

        collectionUserkies = db.collection("Users");
        collectionStuffkies = db.collection("Stuffkies");
        collectionCharacterkies = db.collection("Characterkies");
        collectionWorldkies.document(UID).addSnapshotListener((documentSnapshot, e) -> {
            if (e != null) {
                Log.e("Error", e.getMessage());
                Toast.makeText(getContext(), "Error al escuchar cambios: " + e.getMessage(), LENGTH_LONG).show();
                return;
            }

            if (documentSnapshot != null) {
               worldkieModel = WorldkieModel.fromSnapshot(documentSnapshot);
                //  userkieModel = new UserkieModel(firebaseAuth.getUid(),documentSnapshot.getString("name"),R.drawable.thumb_custom,documentSnapshot.getString("username"),documentSnapshot.getBoolean("photo_default"),true);
                tv_nameWorldkieView.setText(worldkieModel.getName());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

                tv_creationDateWorldkieView.setText(simpleDateFormat.format(worldkieModel.getCreation_date().toDate()));
                        tv_lastUpdateWorldkieView.setText(simpleDateFormat.format(worldkieModel.getLast_update().toDate()));
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
                tv_nameUserWorldkieView.setText(userkieModel.getName());
                tv_usernameWorldkieView.setText(userkieModel.getUsername());
                if((!worldkieModel.isWorldkie_private() && mode.equals("other") && !userkieModel.isProfile_private()) || (worldkieModel.isWorldkie_private() && mode.equals("self"))){
                    collectionStuffkies.addSnapshotListener((queryDocumentSnapshots, e) -> {
                        if (e != null) {
                            Log.e("Error", e.getMessage());
                            Toast.makeText(getContext(), "Error al escuchar cambios: " + e.getMessage(), LENGTH_LONG).show();
                            return;
                        }

                        if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                            stuffkieArrayList.clear(); // Limpia la lista antes de agregar nuevos datos

                            boolean foundData = false; // Add a flag
                            for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                                if (doc.getString("UID_WORLDKIE").equals(UID)) {

                                    StuffkieModel stuffkieModel = new StuffkieModel(
                                            doc.getId(),
                                            doc.getString("name"),
                                            doc.getBoolean("stuffkie_private"),
                                            doc.getBoolean("photo_default")
                                    );
                                    Log.d("StuffkiesSearch", "Stuffkie encontrado: " + doc.getString("name"));

                                    stuffkieArrayList.add(stuffkieModel);
                                    foundData = true; // Set the flag to true if data is found
                                }
                            }
                            if (foundData) {
                                tv_stuffkiesPreviewWorldkie.setVisibility(View.VISIBLE);
                                cv_stuffkiesPreviewWorldkie.setVisibility(View.VISIBLE);
                                updateRecyclerViewStuffkies(stuffkieArrayList);

                            } else {
                                tv_stuffkiesPreviewWorldkie.setVisibility(View.GONE);
                                cv_stuffkiesPreviewWorldkie.setVisibility(View.GONE);
                                updateRecyclerViewStuffkies(new ArrayList<>());

                            }
                        } else {
                            tv_stuffkiesPreviewWorldkie.setVisibility(View.GONE);
                            cv_stuffkiesPreviewWorldkie.setVisibility(View.GONE);
                            updateRecyclerViewStuffkies(new ArrayList<>());

                        }
                    });
                    collectionCharacterkies.addSnapshotListener((queryDocumentSnapshots, ex) -> {
                        if (ex != null) {
                            Log.e("Error", ex.getMessage());
                            Toast.makeText(getContext(), "Error al escuchar cambios: " + ex.getMessage(), LENGTH_LONG).show();
                            return;
                        }

                        if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                            characterkieArrayList.clear(); // Limpia la lista antes de agregar nuevos datos
                            boolean foundData = false; // Add a flag

                            for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                                //if (documentSnapshot.getBoolean("photo_default")) {
                                if (doc.getString("UID_WORLDKIE").equals(UID)) {

                                    Characterkie characterkieModel = new Characterkie(
                                            doc.getId(),
                                            doc.getString("name")
                                    );
                                    Log.d("StuffkiesSearch", "Stuffkie encontrado: " + doc.getString("name"));

                                    characterkieArrayList.add(characterkieModel);
                                    foundData = true; // Set the flag to true if data is found
                                    updateRecyclerViewCharacterkies(characterkieArrayList);
                                }
                            }
                            if (foundData) {
                                tv_characterkiesPreviewWorldkie.setVisibility(View.VISIBLE);
                                cv_characterkiesPreviewWorldkie.setVisibility(View.VISIBLE);
                            } else {
                                tv_characterkiesPreviewWorldkie.setVisibility(View.GONE);
                                cv_characterkiesPreviewWorldkie.setVisibility(View.GONE);
                            }
                        } else {
                            tv_characterkiesPreviewWorldkie.setVisibility(View.GONE);
                            cv_characterkiesPreviewWorldkie.setVisibility(View.GONE);
                        }
                    });
                } else if(!worldkieModel.isWorldkie_private() && userkieModel.isProfile_private() && mode.equals("other")){
                    tv_locked_worldkie.setVisibility(View.VISIBLE);
                    iv_locked_worldkie.setVisibility(View.VISIBLE);
                    tv_characterkiesPreviewWorldkie.setVisibility(View.GONE);
                    cv_characterkiesPreviewWorldkie.setVisibility(View.GONE);
                    tv_stuffkiesPreviewWorldkie.setVisibility(View.GONE);
                    cv_stuffkiesPreviewWorldkie.setVisibility(View.GONE);
                }
                    else if(worldkieModel.isWorldkie_private() && mode.equals("other")){
                    tv_locked_worldkie.setVisibility(View.VISIBLE);
                    iv_locked_worldkie.setVisibility(View.VISIBLE);
                    tv_characterkiesPreviewWorldkie.setVisibility(View.GONE);
                    cv_characterkiesPreviewWorldkie.setVisibility(View.GONE);
                    tv_stuffkiesPreviewWorldkie.setVisibility(View.GONE);
                    cv_stuffkiesPreviewWorldkie.setVisibility(View.GONE);
                }
            }
        });


        getProfilePhoto();
        ib_back.setOnClickListener(this);
        return view;
    }
    private void initComponents(View view){
        ib_worldkieView = view.findViewById(R.id.ib_worldkieView);
        tv_locked_worldkie = view.findViewById(R.id.tv_locked_worldkie);
        iv_locked_worldkie = view.findViewById(R.id.iv_locked_worldkie);
        tv_nameWorldkieView = view.findViewById(R.id.tv_nameWorldkieView);
        tv_nameUserWorldkieView = view.findViewById(R.id.tv_nameUserWorldkieView);
        tv_usernameWorldkieView = view.findViewById(R.id.tv_usernameWorldkieView);
        tv_creationDateWorldkieView = view.findViewById(R.id.tv_creationDateWorldkieView);
        tv_lastUpdateWorldkieView = view.findViewById(R.id.tv_lastUpdateWorldkieView);
        cv_characterkiesPreviewWorldkie = view.findViewById(R.id.cv_characterkiesPreviewWorldkie);
        cv_stuffkiesPreviewWorldkie = view.findViewById(R.id.cv_stuffkiesPreviewWorldkie);
        rc_characterkiesPrevieWorldkie = view.findViewById(R.id.rc_characterkiesPrevieWorldkie);
        rc_stuffkiesPreviewWorldkie = view.findViewById(R.id.rc_stuffkiesPreviewWorldkie);
        tv_stuffkiesPreviewWorldkie = view.findViewById(R.id.tv_stuffkiesPreviewWorldkie);
        tv_characterkiesPreviewWorldkie = view.findViewById(R.id.tv_characterkiesPreviewWorldkie);
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
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }
    private void getProfilePhoto() {
        if (worldkieModel.isPhoto_default()) {
               /* FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                firebaseFirestore.collection("Worldkies").document(UID).addSnapshotListener((queryDocumentSnapshot, e) -> {
                       /* if (e != null) {
                            Log.e("Error", e.getMessage());
                            Toast.makeText(getContext(), "Error al escuchar cambios: " + e.getMessage(), LENGTH_LONG).show();
                            return;
                        }*/
            String id_photo = worldkieModel.getId_photo();
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
        }//);
        //}
        else {
            StorageReference userFolderRef = FirebaseStorage.getInstance("gs://buuk-tu-worldkies").getReference(UID);

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
                ;
            });
/*.addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al buscar imagen", Toast.LENGTH_SHORT).show();
                    Log.e("Storage", "Error listando archivos: " + e.getMessage());
                })*/
            ;


            // }
            //}
        }
    }
    private void updateRecyclerViewCharacterkies(ArrayList<Characterkie> characterkieArrayList) {
        characterkiesUserPreviewAdapter = new CharacterkiesUserPreviewAdapter(characterkieArrayList,getContext());
        rc_characterkiesPrevieWorldkie.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rc_characterkiesPrevieWorldkie.setAdapter(characterkiesUserPreviewAdapter);
    }
    private void updateRecyclerViewStuffkies(ArrayList<StuffkieModel> stuffkieArrayList) {
        stuffkiesUserPreviewAdapter = new StuffkiesUserPreviewAdapter(stuffkieArrayList,getContext());
        rc_stuffkiesPreviewWorldkie.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rc_stuffkiesPreviewWorldkie.setAdapter(stuffkiesUserPreviewAdapter);
    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.ib_back){
            NavigationUtils.goBack(fragmentManager,mainActivity);
        } else if (v.getId() == R.id.ib_worldkieView) {
            InfoFutureFunctionDialog infoFutureFunctionDialog = new InfoFutureFunctionDialog(mainActivity);
            infoFutureFunctionDialog.show();
        }
    }
}