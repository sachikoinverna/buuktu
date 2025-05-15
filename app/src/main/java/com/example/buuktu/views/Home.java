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
    private ArrayList<WorldkieModel> worldkieModelArrayList;
    private RecyclerView rc_worldkies;
    private FloatingActionButton fb_add;
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



        setRecyclerView();
        getWorldkies();
        return view;
    }
    private void getWorldkies() {
        mainActivity.getCollectionWorldkies().whereEqualTo("UID_AUTHOR", mainActivity.getUID())
                .orderBy("last_update", Query.Direction.DESCENDING)
                .addSnapshotListener((snapshots, error) -> {
                    if (error != null) return;

                    worldkieModelArrayList.clear();
                    if (snapshots != null) {
                        for (DocumentSnapshot doc : snapshots) {
                            worldkieModelArrayList.add(WorldkieModel.fromSnapshot(doc));
                        }
                    }
                    worldkieAdapter.notifyDataSetChanged();
                });
    }
    private void setRecyclerView() {
        rc_worldkies.setLayoutManager(new GridLayoutManager(mainActivity,2));
        worldkieAdapter = new WorldkieAdapter(worldkieModelArrayList, mainActivity, fragmentManager);
        rc_worldkies.setAdapter(worldkieAdapter);
    }
    private void initComponents(View view) {
        fb_add = view.findViewById(R.id.fb_addWorldkie);
        rc_worldkies = view.findViewById(R.id.rc_worldkies);
        mainActivity = (MainActivity) getActivity();
        fragmentManager = requireActivity().getSupportFragmentManager();
        backButton = mainActivity.getBackButton();
        ib_save = mainActivity.getIb_save();
        ib_profile_superior = mainActivity.getIb_self_profile();

    }
    private void setListeners() {
        fb_add.setOnClickListener(this);

    }
    private void setVisibility() {
    ib_save.setVisibility(View.GONE);
    backButton.setVisibility(View.GONE);
    ib_profile_superior.setVisibility(View.VISIBLE);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fb_addWorldkie) {
            NavigationUtils.goNewFragment(fragmentManager,new CreateEditWorldkie());
        }
    }
}