package com.example.buuktu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.buuktu.adapters.CardInspoAdapter;
import com.example.buuktu.adapters.CardInspoDesafiosAdapter;
import com.example.buuktu.models.CardItem;
import com.example.buuktu.utils.NavigationUtils;
import com.example.buuktu.views.MainActivity;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InspoDesafios#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InspoDesafios extends Fragment implements View.OnClickListener {

    RecyclerView rc_buttons_inspo_desafio;
    private CardInspoDesafiosAdapter adapter;
    ImageButton ib_back,ib_profile_superior;
    FragmentManager fragmentManager;
    FragmentActivity activity;
    public InspoDesafios() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment InspoDesafios.
     */
    // TODO: Rename and change types and number of parameters
    public static InspoDesafios newInstance() {
        return new InspoDesafios();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inspo_desafios, container, false);
        rc_buttons_inspo_desafio = view.findViewById(R.id.rc_buttons_inspo_desafio);
        rc_buttons_inspo_desafio.setLayoutManager(new GridLayoutManager(getContext(), 2));
        MainActivity mainActivity = (MainActivity) getActivity();
        ib_back = mainActivity.getBackButton();
        ib_profile_superior = mainActivity.getIb_self_profile();
        ib_profile_superior.setVisibility(View.VISIBLE);
        ArrayList<CardItem> items = new ArrayList<>();
        items.add(new CardItem(R.drawable.twotone_abc_24,"Wordkie of the day"));
        items.add(new CardItem(R.drawable.twotone_pin_24,"Numberkie of the day"));
        ib_back.setVisibility(View.VISIBLE);
        fragmentManager = requireActivity().getSupportFragmentManager();
        activity = requireActivity();
        setListeners();
        updateRecyclerView(items);
        return view;
    }
    public void updateRecyclerView(ArrayList<CardItem> cardItems){
        adapter = new CardInspoDesafiosAdapter(getContext(),cardItems,getParentFragmentManager());
        rc_buttons_inspo_desafio.setAdapter(adapter);
        rc_buttons_inspo_desafio.setLayoutManager(new GridLayoutManager(getContext(),2));
    }
    private void setListeners(){
        ib_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.ib_back){
            NavigationUtils.goBack(fragmentManager,activity);
        }
    }
}