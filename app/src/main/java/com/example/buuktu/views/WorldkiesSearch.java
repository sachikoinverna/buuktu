package com.example.buuktu.views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.buuktu.R;
import com.example.buuktu.adapters.WorldkieSearchAdapter;
import com.example.buuktu.models.WorldkieModel;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class WorldkiesSearch extends Fragment {

    RecyclerView rc_worldkies_search;
    private ArrayList<WorldkieModel> worldkieModelArrayList= new ArrayList<>();
    WorldkieSearchAdapter worldkieSearchAdapter;
    MainActivity mainActivity;
    public WorldkiesSearch() {}

    public static WorldkiesSearch newInstance() {
        return new WorldkiesSearch();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_worldkies_search, container, false);
        initComponents(view);
        getWorldkies();
        setRecyclerView();
        return view;
    }
    private void initComponents(View view){
        rc_worldkies_search = view.findViewById(R.id.rc_worldkies_search);
        mainActivity = (MainActivity) requireActivity();
    }
    private void getWorldkies() {
        mainActivity.getCollectionWorldkies().whereNotEqualTo("UID_AUTHOR",mainActivity.getUID()).whereEqualTo("draft",false)
                .addSnapshotListener((snapshots, error) -> {
                    if (error != null) return;

                    worldkieModelArrayList.clear();
                    if (snapshots != null) {
                        for (DocumentSnapshot doc : snapshots) {
                            worldkieModelArrayList.add(WorldkieModel.fromSnapshot(doc));
                        }
                    }
                    worldkieSearchAdapter.notifyDataSetChanged();
                });
    }
    private void setRecyclerView() {
        rc_worldkies_search.setLayoutManager(new LinearLayoutManager(mainActivity));
            worldkieSearchAdapter = new WorldkieSearchAdapter(worldkieModelArrayList, mainActivity, getParentFragmentManager());
        rc_worldkies_search.setAdapter(worldkieSearchAdapter);
    }
            }