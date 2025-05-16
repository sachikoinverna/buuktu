package com.example.buuktu.views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.buuktu.R;
import com.example.buuktu.adapters.ScenariokieSearchAdapter;
import com.example.buuktu.models.ScenariokieModel;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;


public class ScenariokiesSearch extends Fragment {
    private ArrayList<ScenariokieModel> scenariokieModelArrayList = new ArrayList<>();
    private RecyclerView rc_scenariokies_search;
    private ScenariokieSearchAdapter scenariokieSearchAdapter;
    private MainActivity mainActivity;
    public ScenariokiesSearch() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scenariokies_search, container, false);
        initComponents(view);
        getScenariokies();
        setRecyclerView();
        return view;
    }
    private void initComponents(View view){
        mainActivity =(MainActivity) getActivity();
        rc_scenariokies_search = view.findViewById(R.id.rc_scenariokies_search);
    }
    private void getScenariokies() {
        mainActivity.getCollectionScenariokies()
                .whereNotEqualTo("UID_AUTHOR",mainActivity.getUID()).whereEqualTo("draft",false)
                .addSnapshotListener((snapshots, error) -> {
                    if (error != null) return;

                    scenariokieModelArrayList.clear();
                    if (snapshots != null) {
                        for (DocumentSnapshot doc : snapshots) {
                            scenariokieModelArrayList.add(ScenariokieModel.fromSnapshot(doc));
                        }
                    }
                    scenariokieSearchAdapter.notifyDataSetChanged();
                });
    }
    private void setRecyclerView() {
        rc_scenariokies_search.setLayoutManager(new LinearLayoutManager(mainActivity));
        scenariokieSearchAdapter = new ScenariokieSearchAdapter(scenariokieModelArrayList, mainActivity, getParentFragmentManager());
        rc_scenariokies_search.setAdapter(scenariokieSearchAdapter);
    }
}