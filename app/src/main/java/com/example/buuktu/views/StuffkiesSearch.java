package com.example.buuktu.views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.buuktu.R;
import com.example.buuktu.adapters.StuffkieSearchAdapter;
import com.example.buuktu.models.StuffkieModel;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;


public class StuffkiesSearch extends Fragment {

    private ArrayList<StuffkieModel> stuffkieModelArrayList= new ArrayList<>();
    private RecyclerView rc_stuffkies_search;
    private StuffkieSearchAdapter stuffkieSearchAdapter;
    private MainActivity mainActivity;
    public StuffkiesSearch() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stuffkies_search, container, false);
        initComponents(view);
        getStuffkies();
        setRecyclerView();
        return view;
    }
    private void initComponents(View view){
        mainActivity =(MainActivity) getActivity();
        rc_stuffkies_search = view.findViewById(R.id.rc_stuffkies_search);
    }
    private void getStuffkies() {
        mainActivity.getCollectionStuffkies()
                .whereNotEqualTo("UID_AUTHOR",mainActivity.getUID()).whereEqualTo("draft",false)
                .addSnapshotListener((snapshots, error) -> {
                    if (error != null) return;

                    stuffkieModelArrayList.clear();
                    if (snapshots != null) {
                        for (DocumentSnapshot doc : snapshots) {
                            stuffkieModelArrayList.add(StuffkieModel.fromSnapshot(doc));
                        }
                    }
                    stuffkieSearchAdapter.notifyDataSetChanged();
                });
    }
    private void setRecyclerView() {
        rc_stuffkies_search.setLayoutManager(new LinearLayoutManager(mainActivity));
        stuffkieSearchAdapter = new StuffkieSearchAdapter(stuffkieModelArrayList, mainActivity, getParentFragmentManager());
        rc_stuffkies_search.setAdapter(stuffkieSearchAdapter);
    }
}