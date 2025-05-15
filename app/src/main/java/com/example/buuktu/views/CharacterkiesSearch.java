package com.example.buuktu.views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.buuktu.R;
import com.example.buuktu.adapters.CharacterkieSearchAdapter;
import com.example.buuktu.models.CharacterkieModel;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;


public class CharacterkiesSearch extends Fragment {
    private ArrayList<CharacterkieModel> characterkieModelArrayList= new ArrayList<>();
    private RecyclerView rc_characterkies_search;
    private CharacterkieSearchAdapter characterkieSearchAdapter;
    private MainActivity mainActivity;
    public CharacterkiesSearch() {
    }


    public static CharacterkiesSearch newInstance() {
        return new CharacterkiesSearch();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_characterkies_search, container, false);

        initComponents(view);
        setRecyclerView();
        getCharacterkies();
        return view;
    }
    private void initComponents(View view){
        rc_characterkies_search = view.findViewById(R.id.rc_characterkies_search);
        mainActivity = (MainActivity) getActivity();
    }
    private void getCharacterkies() {
        mainActivity.getCollectionCharacterkies()
                .whereNotEqualTo("UID_AUTHOR",mainActivity.getUID()).whereEqualTo("draft",false)
                .addSnapshotListener((snapshots, error) -> {
                    if (error != null) return;

                    characterkieModelArrayList.clear();
                    if (snapshots != null) {
                        for (DocumentSnapshot doc : snapshots) {
                            characterkieModelArrayList.add(CharacterkieModel.fromSnapshot(doc));
                        }
                    }
                    characterkieSearchAdapter.notifyDataSetChanged();
                });
    }
    private void setRecyclerView() {
        rc_characterkies_search.setLayoutManager(new LinearLayoutManager(mainActivity));
        characterkieSearchAdapter = new CharacterkieSearchAdapter(characterkieModelArrayList, mainActivity, getParentFragmentManager());
        rc_characterkies_search.setAdapter(characterkieSearchAdapter);
    }
}