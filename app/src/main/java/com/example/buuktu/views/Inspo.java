package com.example.buuktu.views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.buuktu.R;
import com.example.buuktu.adapters.CardInspoAdapter;
import com.example.buuktu.models.CardItem;

import java.util.ArrayList;


public class Inspo extends Fragment {
    private MainActivity mainActivity;
    private RecyclerView rc_buttons_inspo;
    private CardInspoAdapter adapter;
    private ImageButton ib_back,ib_save,ib_profile_superior;
    private ArrayList<CardItem> items = new ArrayList<>();
    public Inspo() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inspo, container, false);
        initComponents(view);
        setVisibility();
        setRecyclerView();
        return view;

    }
    private void initComponents(View view){
        rc_buttons_inspo = view.findViewById(R.id.rc_buttons_inspo);
        mainActivity = (MainActivity) getActivity();
        ib_back = mainActivity.getBackButton();
        ib_profile_superior = mainActivity.getIb_self_profile();
        ib_save = mainActivity.getIb_save();

    }
    private void setRecyclerView(){
        items.clear();
        items.add(new CardItem(R.drawable.twotone_sticky_note_2_24, mainActivity.getResources().getString(R.string.notekies)));
        items.add(new CardItem(R.drawable.twotone_military_tech_24, mainActivity.getResources().getString(R.string.challenges)));
        adapter = new CardInspoAdapter(mainActivity,items,getParentFragmentManager());
        rc_buttons_inspo.setAdapter(adapter);
        rc_buttons_inspo.setLayoutManager(new GridLayoutManager(mainActivity,2));
    }
    private void setVisibility(){
        ib_save.setVisibility(View.GONE);
        ib_back.setVisibility(View.GONE);
        ib_profile_superior.setVisibility(View.VISIBLE);
    }
}