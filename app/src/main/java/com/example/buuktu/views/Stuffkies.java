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
import com.example.buuktu.adapters.StuffkiesUserPreviewAdapter;
import com.example.buuktu.models.StuffkieModel;
import com.example.buuktu.utils.NavigationUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class Stuffkies extends Fragment implements View.OnClickListener {
    private ArrayList<StuffkieModel> stuffkieModels=new ArrayList<>();
    private RecyclerView rc_worldkies;
    private FloatingActionButton fb_parent, fb_add;
    private boolean isAllFabsVisible;
    private StuffkiesUserPreviewAdapter stuffkiesUserPreviewAdapter;
    private ImageButton ib_save, ib_profile_superior, backButton;
    private FragmentManager fragmentManager;
    private MainActivity mainActivity;
    Bundle bundle = new Bundle();
    String worldkie_id;
    public Stuffkies() {}

    public static Stuffkies newInstance() {
        return new Stuffkies();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.worldkie_id=getArguments().getString("worldkie_id");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stuffkies, container, false);


        initComponents(view);
        setVisibility();
        setListeners();
        bundle.putString("worldkie_id",worldkie_id);
        getStuffkies();
        setRecyclerView();
        return view;
    }

    private void getStuffkies() {
        mainActivity.getCollectionStuffkies()
                .whereEqualTo("WORDLKIE_UID", worldkie_id)
                .addSnapshotListener((snapshots, error) -> {
                    if (error != null) return;

                    stuffkieModels.clear();
                    if (snapshots != null) {
                        for (DocumentSnapshot doc : snapshots) {
                            stuffkieModels.add(StuffkieModel.fromSnapshot(doc));
                        }
                    }
                    stuffkiesUserPreviewAdapter.notifyDataSetChanged();
                });
    }
    private void setRecyclerView() {
        rc_worldkies.setLayoutManager(new GridLayoutManager(mainActivity,2));
        stuffkiesUserPreviewAdapter = new StuffkiesUserPreviewAdapter(stuffkieModels, mainActivity, fragmentManager,"self");
        rc_worldkies.setAdapter(stuffkiesUserPreviewAdapter);
    }

    private void initComponents(View view) {
        fb_add = view.findViewById(R.id.fb_addStuffkie);
        rc_worldkies = view.findViewById(R.id.rc_stuffkies);
        mainActivity = (MainActivity) getActivity();
        fragmentManager = requireActivity().getSupportFragmentManager();
        backButton = mainActivity.getBackButton();
        ib_save = mainActivity.getIb_save();
        ib_profile_superior = mainActivity.getIb_self_profile();
        isAllFabsVisible = false;

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
        if (v.getId() == R.id.fb_addStuffkie) {
            NavigationUtils.goNewFragmentWithBundle(bundle,fragmentManager,new CreateEditStuffkie());
        }
    }
}