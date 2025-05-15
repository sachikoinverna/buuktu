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
import com.example.buuktu.adapters.CharacterkiesUserPreviewAdapter;
import com.example.buuktu.models.CharacterkieModel;
import com.example.buuktu.utils.NavigationUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class Characterkies extends Fragment implements View.OnClickListener {
    private ArrayList<CharacterkieModel> characterkieModels = new ArrayList<>();
    private RecyclerView rc_characterkies;
    private FloatingActionButton fb_add;
    private CharacterkiesUserPreviewAdapter characterkiesUserPreviewAdapter;
    private ImageButton ib_save, ib_profile_superior, backButton;
    private FragmentManager fragmentManager;
    private MainActivity mainActivity;
    private String worldkie_id;
    Bundle bundle = new Bundle();

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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_characterkies, container, false);
        initComponents(view);
        setVisibility();
        setListeners();
        getCharacterkies();
        setRecyclerView();


        return view;
    }

    private void initComponents(View view) {
        fb_add = view.findViewById(R.id.fb_addCharacterkie);
        rc_characterkies = view.findViewById(R.id.rc_characterkies);
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

    private void getCharacterkies() {
        mainActivity.getCollectionCharacterkies()
                .whereEqualTo("UID_WORLDKIE", worldkie_id)
                .addSnapshotListener((snapshots, error) -> {
                    if (error != null) return;

                    characterkieModels.clear();
                    if (snapshots != null) {
                        for (DocumentSnapshot doc : snapshots) {
                            characterkieModels.add(CharacterkieModel.fromSnapshot(doc));
                        }
                    }
                    characterkiesUserPreviewAdapter.notifyDataSetChanged();
                });
    }
    private void setRecyclerView() {
        rc_characterkies.setLayoutManager(new GridLayoutManager(mainActivity, 2));
        characterkiesUserPreviewAdapter = new CharacterkiesUserPreviewAdapter(characterkieModels, mainActivity, fragmentManager, "self");
        rc_characterkies.setAdapter(characterkiesUserPreviewAdapter);
    }
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.ib_back){
            NavigationUtils.goBack(fragmentManager,mainActivity);
        } else if (v.getId() == R.id.fb_addCharacterkie) {
            bundle.putString("worldkie_id",worldkie_id);
            NavigationUtils.goNewFragmentWithBundle(bundle,fragmentManager,new CreateCharacterkie());
        }
    }
}