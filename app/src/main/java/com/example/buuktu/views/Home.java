package com.example.buuktu.views;

import static android.widget.Toast.LENGTH_LONG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buuktu.R;
import com.example.buuktu.adapters.WorldkieAdapter;
import com.example.buuktu.models.WorldkieModel;
import com.example.buuktu.utils.NavigationUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment implements View.OnClickListener {
    private String UID;
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore db;
    private ArrayList<WorldkieModel> worldkieModelArrayList;
    private RecyclerView rc_worldkies;
    private FloatingActionButton fb_parent, fb_add;
    private boolean isAllFabsVisible;
    private WorldkieAdapter worldkieAdapter;
    private CollectionReference dbWorldkies;
    private ImageButton ib_save, ib_profile_superior, backButton;
    private FragmentManager fragmentManager;
    private MainActivity mainActivity;

    public Home() {}

    public static Home newInstance() {
        return new Home();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        UID = auth.getCurrentUser().getUid();
        worldkieModelArrayList = new ArrayList<>();
        dbWorldkies = db.collection("Worldkies");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        // Inicializa vistas y listeners
        initComponents(view);
        setVisibility();
        setListeners();

        // Configura RecyclerView
        rc_worldkies.setLayoutManager(new LinearLayoutManager(mainActivity));
        worldkieAdapter = new WorldkieAdapter(worldkieModelArrayList, mainActivity, getParentFragmentManager());
        rc_worldkies.setAdapter(worldkieAdapter);

        // Escucha cambios en Firestore
        listenToWorldkies();

        return view;
    }

    private void initComponents(View view) {
        fb_parent = view.findViewById(R.id.fb_parentWorldkies);
        fb_add = view.findViewById(R.id.fb_addWorldkie);
        rc_worldkies = view.findViewById(R.id.rc_worldkies);
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
    backButton.setVisibility(View.GONE);
    ib_profile_superior.setVisibility(View.VISIBLE);
    }

    private void listenToWorldkies() {
        Log.d("Firestore", "Iniciando listener para UID: " + UID);
        dbWorldkies.whereEqualTo("uid_AUTHOR", UID)
                .orderBy("last_update", Query.Direction.DESCENDING)
                .addSnapshotListener((querySnapshots, e) -> {
                    if (e != null) {
                        Log.e("Firestore", "Error al escuchar cambios: " + e.getMessage());
                        Toast.makeText(getContext(), "Error Firestore: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (querySnapshots != null && !querySnapshots.isEmpty()) {
                        Log.d("Firestore", "Documentos recibidos: " + querySnapshots.size());
                        worldkieModelArrayList.clear();

                        for (DocumentSnapshot doc : querySnapshots.getDocuments()) {
                            Log.d("Firestore", "Doc ID: " + doc.getId() + ", Data: " + doc.getData());
                            WorldkieModel worldkieModel = WorldkieModel.fromSnapshot(doc);
                            worldkieModelArrayList.add(worldkieModel);
                        }

                        worldkieAdapter.notifyDataSetChanged();
                    } else {
                        Log.d("Firestore", "No se encontraron documentos para UID: " + UID);
                        worldkieModelArrayList.clear();
                        worldkieAdapter.notifyDataSetChanged();
                    }
                });
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fb_parentWorldkies) {
            isAllFabsVisible = !isAllFabsVisible;
            fb_add.setVisibility(isAllFabsVisible ? View.VISIBLE : View.GONE);
        } else if (v.getId() == R.id.fb_addWorldkie) {
            NavigationUtils.goNewFragment(fragmentManager,new CreateEditWorldkie());
        }
    }
}