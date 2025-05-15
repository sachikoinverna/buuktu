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
import com.example.buuktu.adapters.CardInspoDesafiosAdapter;
import com.example.buuktu.models.CardItem;
import com.example.buuktu.utils.NavigationUtils;

import java.util.ArrayList;

public class InspoDesafios extends Fragment implements View.OnClickListener {

    private RecyclerView rc_buttons_inspo_desafio;
    private CardInspoDesafiosAdapter adapter;
    private ImageButton ib_back,ib_profile_superior;
    private MainActivity mainActivity;
    private ArrayList<CardItem> items = new ArrayList<>();

    public InspoDesafios() {}

    public static InspoDesafios newInstance() {
        return new InspoDesafios();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inspo_desafios, container, false);
        initComponents(view);
        setVisibility();
        setListeners();
        setRecyclerView();
        return view;
    }
    private void initComponents(View view){
        rc_buttons_inspo_desafio = view.findViewById(R.id.rc_buttons_inspo_desafio);
        mainActivity = (MainActivity) getActivity();
        ib_back = mainActivity.getBackButton();
        ib_profile_superior = mainActivity.getIb_self_profile();
    }
    private void setRecyclerView(){
        items.add(new CardItem(R.drawable.twotone_abc_24,mainActivity.getResources().getString(R.string.wordkie_of_the_day)));
        items.add(new CardItem(R.drawable.twotone_pin_24,mainActivity.getResources().getString(R.string.numberkie_of_the_day)));
        adapter = new CardInspoDesafiosAdapter(mainActivity,items);
        rc_buttons_inspo_desafio.setAdapter(adapter);
        rc_buttons_inspo_desafio.setLayoutManager(new GridLayoutManager(mainActivity,2));
    }
    private void setVisibility(){
        ib_profile_superior.setVisibility(View.VISIBLE);
        ib_back.setVisibility(View.VISIBLE);
    }

    private void setListeners(){
        ib_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // Comprueba si se ha presionado el botón de retroceso.
        if(v.getId()==R.id.ib_back){
            NavigationUtils.goBack(getParentFragmentManager(),mainActivity);
        }
    }
}