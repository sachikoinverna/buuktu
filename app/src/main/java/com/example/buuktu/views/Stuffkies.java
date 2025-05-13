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

import com.example.buuktu.CreateEditStuffkie;
import com.example.buuktu.R;
import com.example.buuktu.adapters.StuffkiesUserPreviewAdapter;
import com.example.buuktu.adapters.WorldkieAdapter;
import com.example.buuktu.models.StuffkieModel;
import com.example.buuktu.models.WorldkieModel;
import com.example.buuktu.utils.NavigationUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class Stuffkies extends Fragment implements View.OnClickListener {
    private String UID;
    private FirebaseFirestore db;
    private ArrayList<StuffkieModel> stuffkieModels;
    private RecyclerView rc_worldkies;
    private FloatingActionButton fb_parent, fb_add;
    private boolean isAllFabsVisible;
    private StuffkiesUserPreviewAdapter stuffkiesUserPreviewAdapter;
    private CollectionReference dbStuffkies;
    private ImageButton ib_save, ib_profile_superior, backButton;
    private FragmentManager fragmentManager;
    private MainActivity mainActivity;
    public Stuffkies() {}

    public static Stuffkies newInstance() {
        return new Stuffkies();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        stuffkieModels = new ArrayList<>();
        dbStuffkies = db.collection("Stuffkies");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stuffkies, container, false);


        // Inicializa vistas y listeners
        initComponents(view);
        setVisibility();
        setListeners();
        UID = mainActivity.getUID();

        // Configura RecyclerView
        rc_worldkies.setLayoutManager(new GridLayoutManager(mainActivity,2));
        stuffkiesUserPreviewAdapter = new StuffkiesUserPreviewAdapter(stuffkieModels, mainActivity, fragmentManager,"self");
        rc_worldkies.setAdapter(stuffkiesUserPreviewAdapter);

        // Escucha cambios en Firestore
        listenToWorldkies();

        return view;
    }

    private void initComponents(View view) {
        fb_parent = view.findViewById(R.id.fb_parentStuffkies);
        fb_add = view.findViewById(R.id.fb_addStuffkie);
        rc_worldkies = view.findViewById(R.id.rc_stuffkies);
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
        Log.d("Firestore", "Iniciando listener para UID: " + UID);
        dbStuffkies.whereEqualTo("UID_WORLDKIE", UID)
                .addSnapshotListener((querySnapshots, e) -> {
                    if (e != null) {
                        Log.e("Firestore", "Error al escuchar cambios: " + e.getMessage());
                        Toast.makeText(getContext(), "Error Firestore: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (querySnapshots != null && !querySnapshots.isEmpty()) {
                        Log.d("Firestore", "Documentos recibidos: " + querySnapshots.size());
                        stuffkieModels.clear();

                        for (DocumentSnapshot doc : querySnapshots.getDocuments()) {
                            Log.d("Firestore", "Doc ID: " + doc.getId() + ", Data: " + doc.getData());
                            StuffkieModel stuffkieModel = StuffkieModel.fromSnapshot(doc);
                            stuffkieModels.add(stuffkieModel);
                        }

                        stuffkiesUserPreviewAdapter.notifyDataSetChanged();
                    } else {
                        Log.d("Firestore", "No se encontraron documentos para UID: " + UID);
                        stuffkieModels.clear();
                        stuffkiesUserPreviewAdapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fb_parentStuffkies) {
            isAllFabsVisible = !isAllFabsVisible;
            fb_add.setVisibility(isAllFabsVisible ? View.VISIBLE : View.GONE);
        } else if (v.getId() == R.id.fb_addStuffkie) {
            NavigationUtils.goNewFragment(fragmentManager,new CreateEditStuffkie());
        }
    }
}