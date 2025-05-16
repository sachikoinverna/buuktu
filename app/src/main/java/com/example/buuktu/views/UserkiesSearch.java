package com.example.buuktu.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buuktu.R;
import com.example.buuktu.adapters.UserkieSearchAdapter;
import com.example.buuktu.models.UserkieModel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;

import java.util.ArrayList;

public class UserkiesSearch extends Fragment {

    private RecyclerView rc_userkies_search;
    private ArrayList<UserkieModel> userkieModelArrayList= new ArrayList<>();
    private UserkieSearchAdapter userkieSearchAdapter;
    MainActivity mainActivity;
    public UserkiesSearch() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_userkies_search, container, false);
        initComponents(view);
        getUserkies();
        setRecyclerView();
        return view;
    }
    private void initComponents(View view){
        mainActivity = (MainActivity)getActivity();
        rc_userkies_search = view.findViewById(R.id.rc_userkies_search);
    }
    private void getUserkies() {
        mainActivity.getCollectionUsers().whereNotEqualTo(FieldPath.documentId(), mainActivity.getUID()).addSnapshotListener((snapshots, error) -> {
                    if (error != null) return;

            userkieModelArrayList.clear();
                    if (snapshots != null) {
                        for (DocumentSnapshot doc : snapshots) {
                            userkieModelArrayList.add(UserkieModel.fromSnapshot(doc));
                        }
                    }
            userkieSearchAdapter.notifyDataSetChanged();
                });
    }
    private void setRecyclerView() {
        rc_userkies_search.setLayoutManager(new LinearLayoutManager(mainActivity));
        userkieSearchAdapter = new UserkieSearchAdapter(userkieModelArrayList, mainActivity, getParentFragmentManager());
        rc_userkies_search.setAdapter(userkieSearchAdapter);
    }

}
