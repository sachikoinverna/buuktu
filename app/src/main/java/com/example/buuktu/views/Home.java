package com.example.buuktu.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buuktu.R;
import com.example.buuktu.adapters.WorldkieAdapter;
import com.example.buuktu.models.WorldkieModel;
import com.example.buuktu.utils.NavigationUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;


public class Home extends Fragment implements View.OnClickListener {
    private String UID;
    private ArrayList<WorldkieModel> worldkieModelArrayList;
    private RecyclerView rc_worldkies;
    private FloatingActionButton fb_add;
    private boolean isAllFabsVisible;
    private WorldkieAdapter worldkieAdapter;
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

        worldkieModelArrayList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        // Inicializa vistas y listeners
        initComponents(view);
        setVisibility();
        setListeners();
        UID = mainActivity.getUID();

        // Configura RecyclerView
        rc_worldkies.setLayoutManager(new GridLayoutManager(mainActivity,2));
        worldkieAdapter = new WorldkieAdapter(worldkieModelArrayList, mainActivity, fragmentManager);
        rc_worldkies.setAdapter(worldkieAdapter);

        // Escucha cambios en Firestore
        listenToWorldkies();

        return view;
    }

    private void initComponents(View view) {
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
        fb_add.setOnClickListener(this);

    }
    private void setVisibility() {
    fb_add.setVisibility(View.GONE);
    ib_save.setVisibility(View.GONE);
    backButton.setVisibility(View.GONE);
    ib_profile_superior.setVisibility(View.VISIBLE);
    }

    private void listenToWorldkies() {
        mainActivity.getCollectionWorldkies().whereEqualTo("uid_AUTHOR", UID)
                .orderBy("last_update", Query.Direction.DESCENDING)
                .addSnapshotListener((querySnapshots, e) -> {
                    if (e != null) {
                        return;
                    }

                    if (querySnapshots != null && !querySnapshots.isEmpty()) {
                        worldkieModelArrayList.clear();

                        for (DocumentSnapshot doc : querySnapshots.getDocuments()) {
                            WorldkieModel worldkieModel = WorldkieModel.fromSnapshot(doc);
                            worldkieModelArrayList.add(worldkieModel);
                        }

                        worldkieAdapter.notifyDataSetChanged();
                    } else {
                        worldkieModelArrayList.clear();
                        worldkieAdapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fb_addWorldkie) {
            NavigationUtils.goNewFragment(fragmentManager,new CreateEditWorldkie());
        }
    }
}