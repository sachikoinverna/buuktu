package com.example.buuktu.views;

import android.os.Bundle;

import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.buuktu.R;
import com.example.buuktu.adapters.CardAdapter;
import com.example.buuktu.adapters.CardInspoAdapter;
import com.example.buuktu.models.CardItem;
import com.example.buuktu.utils.ComponentsUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Inspo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Inspo extends Fragment {

    RecyclerView rc_buttons_inspo;
    private CardInspoAdapter adapter;
    ImageButton ib_back,ib_save,ib_profile_superior;
    public Inspo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Inspo.
     */
    // TODO: Rename and change types and number of parameters
    public static Inspo newInstance() {
        return new Inspo();
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
        View view = inflater.inflate(R.layout.fragment_inspo, container, false);
        rc_buttons_inspo = view.findViewById(R.id.rc_buttons_inspo);
        rc_buttons_inspo.setLayoutManager(new GridLayoutManager(getContext(), 2));
        MainActivity mainActivity = (MainActivity) getActivity();
        ib_back = mainActivity.getBackButton();
        ib_profile_superior = mainActivity.getIb_self_profile();
        ib_profile_superior.setVisibility(View.VISIBLE);
        ib_save = mainActivity.getIb_save();
        ib_save.setVisibility(View.GONE);
        ArrayList<CardItem> items = new ArrayList<>();
        items.add(new CardItem(R.drawable.twotone_sticky_note_2_24, "Notekies"));
        items.add(new CardItem(R.drawable.twotone_military_tech_24, "Desafios"));
        ib_back.setVisibility(View.GONE);

       updateRecyclerView(items);
        return view;

    }
public void updateRecyclerView(ArrayList<CardItem> cardItems){
    adapter = new CardInspoAdapter(getContext(),cardItems,getParentFragmentManager());
    rc_buttons_inspo.setAdapter(adapter);
    rc_buttons_inspo.setLayoutManager(new GridLayoutManager(getContext(),2));
}
}