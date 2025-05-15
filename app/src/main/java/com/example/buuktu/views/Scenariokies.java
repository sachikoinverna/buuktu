package com.example.buuktu.views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.buuktu.R;
import com.example.buuktu.adapters.ScenariokiesUserPreviewAdapter;
import com.example.buuktu.models.ScenariokieModel;
import com.example.buuktu.utils.NavigationUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;


public class Scenariokies extends Fragment implements View.OnClickListener {
    private String worldkie_id;
    private ArrayList<ScenariokieModel> scenariokieModels = new ArrayList<>();
    private RecyclerView rc_scenariokies;
    private FloatingActionButton fb_add;
    private ScenariokiesUserPreviewAdapter scenariokiesUserPreviewAdapter;
    private ImageButton ib_save, ib_profile_superior, backButton;
    private FragmentManager fragmentManager;
    private MainActivity mainActivity;
    Bundle bundle = new Bundle();
    public Scenariokies() {
    }

    public static Scenariokies newInstance() {
        return new Scenariokies();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if(getArguments().containsKey("worldkie_id")) {
                this.worldkie_id = getArguments().getString("worldkie_id");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scenariokies, container, false);
        initComponents(view);
        setVisibility();
        setListeners();
        getScenariokies();
        setRecyclerView();
        return view;
    }

    private void getScenariokies() {
        mainActivity.getCollectionScenariokies()
                .whereEqualTo("WORDLKIE_UID", worldkie_id)
                .addSnapshotListener((snapshots, error) -> {
                    if (error != null) return;

                    scenariokieModels.clear();
                    if (snapshots != null) {
                        for (DocumentSnapshot doc : snapshots) {
                            scenariokieModels.add(ScenariokieModel.fromSnapshot(doc));
                        }
                    }
                    scenariokiesUserPreviewAdapter.notifyDataSetChanged();
                });
    }
    private void setRecyclerView() {
        rc_scenariokies.setLayoutManager(new GridLayoutManager(mainActivity,2));
        scenariokiesUserPreviewAdapter = new ScenariokiesUserPreviewAdapter(scenariokieModels, mainActivity, fragmentManager,"self");
        rc_scenariokies.setAdapter(scenariokiesUserPreviewAdapter);
    }
    private void initComponents(View view) {
        fb_add = view.findViewById(R.id.fb_addScenariokie);
        rc_scenariokies = view.findViewById(R.id.rc_scenariokies);
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
        backButton.setVisibility(View.VISIBLE);
        ib_profile_superior.setVisibility(View.VISIBLE);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fb_addScenariokie) {
            bundle.putString("worldkie_id",worldkie_id);
            NavigationUtils.goNewFragmentWithBundle(bundle,fragmentManager,new CreateEditScenariokie());
        }
    }
}