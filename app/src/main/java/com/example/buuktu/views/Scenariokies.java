package com.example.buuktu.views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.buuktu.CreateEditScenariokie;
import com.example.buuktu.R;
import com.example.buuktu.adapters.CharacterkiesUserPreviewAdapter;
import com.example.buuktu.adapters.ScenariokiesUserPreviewAdapter;
import com.example.buuktu.models.CharacterkieModel;
import com.example.buuktu.models.ScenariokieModel;
import com.example.buuktu.utils.NavigationUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Scenariokies#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Scenariokies extends Fragment implements View.OnClickListener {
    private String UID;
    private FirebaseFirestore db;
    private ArrayList<ScenariokieModel> scenariokieModels;
    private RecyclerView rc_scenariokies;
    private FloatingActionButton fb_parent, fb_add;
    private boolean isAllFabsVisible;
    private ScenariokiesUserPreviewAdapter scenariokiesUserPreviewAdapter;
    private CollectionReference dbScenariokies;
    private ImageButton ib_save, ib_profile_superior, backButton;
    private FragmentManager fragmentManager;
    private MainActivity mainActivity;
    public Scenariokies() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Scenariokies.
     */
    // TODO: Rename and change types and number of parameters
    public static Scenariokies newInstance() {
        return new Scenariokies();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scenariokies, container, false);
        db = FirebaseFirestore.getInstance();
        scenariokieModels = new ArrayList<>();
        dbScenariokies = db.collection("Scenariokies");
        initComponents(view);
        setVisibility();
        setListeners();
        UID = mainActivity.getUID();

        // Configura RecyclerView
        rc_scenariokies.setLayoutManager(new GridLayoutManager(mainActivity,2));
    //    characterkiesUserPreviewAdapter = new CharacterkiesUserPreviewAdapter(scenariokieModels, mainActivity, fragmentManager,"self");
      //  rc_scenariokies.setAdapter(characterkiesUserPreviewAdapter);

        // Escucha cambios en Firestore
        listenToWorldkies();
        return view;
    }
    private void initComponents(View view) {
        fb_parent = view.findViewById(R.id.fb_parentScenariokies);
        fb_add = view.findViewById(R.id.fb_addScenariokie);
        rc_scenariokies = view.findViewById(R.id.rc_scenariokies);
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
        dbScenariokies.whereEqualTo("UID_WORLDKIE", UID)
                .addSnapshotListener((querySnapshots, e) -> {
                    if (e != null) {
                        Log.e("Firestore", "Error al escuchar cambios: " + e.getMessage());
                        Toast.makeText(getContext(), "Error Firestore: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (querySnapshots != null && !querySnapshots.isEmpty()) {
                        Log.d("Firestore", "Documentos recibidos: " + querySnapshots.size());
                        scenariokieModels.clear();

                        for (DocumentSnapshot doc : querySnapshots.getDocuments()) {
                            Log.d("Firestore", "Doc ID: " + doc.getId() + ", Data: " + doc.getData());
                            ScenariokieModel scenariokieModel = ScenariokieModel.fromSnapshot(doc);
                            scenariokieModels.add(scenariokieModel);
                        }

                        scenariokiesUserPreviewAdapter.notifyDataSetChanged();
                    } else {
                        Log.d("Firestore", "No se encontraron documentos para UID: " + UID);
                        scenariokieModels.clear();
                        scenariokiesUserPreviewAdapter.notifyDataSetChanged();
                    }
                });
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fb_parentScenariokies) {
            isAllFabsVisible = !isAllFabsVisible;
            fb_add.setVisibility(isAllFabsVisible ? View.VISIBLE : View.GONE);
        } else if (v.getId() == R.id.fb_addScenariokie) {
            NavigationUtils.goNewFragment(fragmentManager,new CreateEditScenariokie());
        }
    }
}