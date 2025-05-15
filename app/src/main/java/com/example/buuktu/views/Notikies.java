package com.example.buuktu.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buuktu.R;
import com.example.buuktu.adapters.NotikieListAdapter;
import com.example.buuktu.models.NotikieModel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;


public class Notikies extends Fragment {
    MainActivity mainActivity;
    ImageButton backButton,ib_profile_superior;
    RecyclerView rc_notikies_list;
   NotikieListAdapter notikieListAdapter;
   ArrayList<NotikieModel> notikieModelArrayList = new ArrayList<>();

    public Notikies() {
    }


    public static Notikies newInstance() {
        return new Notikies();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notikies, container, false);
        initComponents(view);
        setVisibility();
        getNotekies();
        setRecyclerView();

        return view;
    }
   private void getNotekies() {
        mainActivity.getNotikiesCollection()
                .whereEqualTo("UID_USER", mainActivity.getUID()).orderBy("date", Query.Direction.DESCENDING)
                .addSnapshotListener((snapshots, error) -> {
                    if (error != null) return;

                    notikieModelArrayList.clear();
                    if (snapshots != null) {
                        for (DocumentSnapshot doc : snapshots) {
                            notikieModelArrayList.add(NotikieModel.fromSnapshot(doc));
                        }
                    }
                    notikieListAdapter.notifyDataSetChanged();
                });
    }
    private void setRecyclerView() {
        notikieListAdapter = new NotikieListAdapter(notikieModelArrayList, mainActivity);
        rc_notikies_list.setAdapter(notikieListAdapter);
        rc_notikies_list.setLayoutManager(new LinearLayoutManager(mainActivity));
    }
    private void initComponents(View view){
        mainActivity = (MainActivity) getActivity();
        backButton = mainActivity.getBackButton();
        ib_profile_superior = mainActivity.getIb_self_profile();
        rc_notikies_list = view.findViewById(R.id.rc_notikies_list);
    }
    private void setVisibility(){
        backButton.setVisibility(View.GONE);
        ib_profile_superior.setVisibility(View.VISIBLE);

    }
}