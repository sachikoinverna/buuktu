package com.example.buuktu.views;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buuktu.CreateCharacterkie;
import com.example.buuktu.R;
import com.example.buuktu.adapters.CharacterkiesUserPreviewAdapter;
import com.example.buuktu.adapters.StuffkiesUserPreviewAdapter;
import com.example.buuktu.models.CharacterkieModel;
import com.example.buuktu.models.StuffkieModel;
import com.example.buuktu.models.WorldkieModel;
import com.example.buuktu.utils.NavigationUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class Characterkies extends Fragment implements View.OnClickListener {
    private String UID;
    private FirebaseFirestore db;
    private ArrayList<CharacterkieModel> characterkieModels;
    private RecyclerView rc_characterkies;
    private FloatingActionButton fb_parent, fb_add;
    private boolean isAllFabsVisible;
    private CharacterkiesUserPreviewAdapter characterkiesUserPreviewAdapter;
    private CollectionReference dbCharacterkies;
    private ImageButton ib_save, ib_profile_superior, backButton;
    private FragmentManager fragmentManager;
    private MainActivity mainActivity;
    String worldkie_id;
    public Characterkies() {}

    public static Characterkies newInstance() {
        return new Characterkies();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments().containsKey("worldkie_id")) {
            this.worldkie_id = getArguments().getString("worldkie_id");
        }
        db = FirebaseFirestore.getInstance();
        characterkieModels = new ArrayList<>();
        dbCharacterkies = db.collection("Characterkies");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_characterkies, container, false);


        // Inicializa vistas y listeners
        initComponents(view);
        setVisibility();
        setListeners();
        UID = mainActivity.getUID();

        // Configura RecyclerView
        rc_characterkies.setLayoutManager(new GridLayoutManager(mainActivity,2));
        characterkiesUserPreviewAdapter = new CharacterkiesUserPreviewAdapter(characterkieModels, mainActivity, fragmentManager,"self");
        rc_characterkies.setAdapter(characterkiesUserPreviewAdapter);

        // Escucha cambios en Firestore
        listenToWorldkies();

        return view;
    }

    private void initComponents(View view) {
        fb_parent = view.findViewById(R.id.fb_parentCharacterkies);
        fb_add = view.findViewById(R.id.fb_addCharacterkie);
        rc_characterkies = view.findViewById(R.id.rc_characterkies);
        mainActivity = (MainActivity) getActivity();
        fragmentManager = requireActivity().getSupportFragmentManager();

        // Referencias desde MainActivity
        backButton = mainActivity.getBackButton();
        ib_save = mainActivity.getIb_save();
        ib_profile_superior = mainActivity.getIb_self_profile();
        isAllFabsVisible = false;

    }
    private void setListeners() {
        fb_parent.setOnClickListener(this);
        fb_add.setOnClickListener(this);

    }
    private void setVisibility() {
        fb_add.setVisibility(View.GONE);
        ib_save.setVisibility(View.GONE);
        backButton.setVisibility(View.VISIBLE);
        ib_profile_superior.setVisibility(View.VISIBLE);
    }

    private void listenToWorldkies() {
        Log.d("Firestore", "Iniciando listener para UID: " + worldkie_id);
        dbCharacterkies.whereEqualTo("UID_WORLDKIE", worldkie_id)
                .addSnapshotListener((querySnapshots, e) -> {
                    if (e != null) {
                        Log.e("Firestore", "Error al escuchar cambios: " + e.getMessage());
                        Toast.makeText(getContext(), "Error Firestore: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (querySnapshots != null && !querySnapshots.isEmpty()) {
                        Log.d("Firestore", "Documentos recibidos: " + querySnapshots.size());
                        characterkieModels.clear();

                        for (DocumentSnapshot doc : querySnapshots.getDocuments()) {
                            Log.d("Firestore", "Doc ID: " + doc.getId() + ", Data: " + doc.getData());
                            CharacterkieModel characterkieModel = CharacterkieModel.fromSnapshot(doc);
                            characterkieModels.add(characterkieModel);
                        }

                        characterkiesUserPreviewAdapter.notifyDataSetChanged();
                    } else {
                        Log.d("Firestore", "No se encontraron documentos para UID: " + UID);
                        characterkieModels.clear();
                        characterkiesUserPreviewAdapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fb_parentCharacterkies) {
            isAllFabsVisible = !isAllFabsVisible;
            fb_add.setVisibility(isAllFabsVisible ? View.VISIBLE : View.GONE);
        } else if (v.getId() == R.id.fb_addCharacterkie) {
            Bundle bundle = new Bundle();
            bundle.putString("worldkie_id",worldkie_id);
            NavigationUtils.goNewFragmentWithBundle(bundle,fragmentManager,new CreateCharacterkie());
        }
    }
}