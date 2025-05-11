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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Inspo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Inspo extends Fragment {
    MainActivity mainActivity;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inspo, container, false);
        initComponents(view);
        return view;

    }
    private void initComponents(View view){
        rc_buttons_inspo = view.findViewById(R.id.rc_buttons_inspo);
        rc_buttons_inspo.setLayoutManager(new GridLayoutManager(mainActivity, 2));
        mainActivity = (MainActivity) getActivity();
        ib_back = mainActivity.getBackButton();
        ib_profile_superior = mainActivity.getIb_self_profile();
        ib_save = mainActivity.getIb_save();
        ArrayList<CardItem> items = new ArrayList<>();
        items.add(new CardItem(R.drawable.twotone_sticky_note_2_24, "Notekies"));
        items.add(new CardItem(R.drawable.twotone_military_tech_24, "Desafios"));
        updateRecyclerView(items);
        setInitVisibility();
    }
    private void setInitVisibility(){
        ib_save.setVisibility(View.GONE);
        ib_back.setVisibility(View.GONE);
        ib_profile_superior.setVisibility(View.VISIBLE);
    }
public void updateRecyclerView(ArrayList<CardItem> cardItems){
    adapter = new CardInspoAdapter(mainActivity,cardItems,getParentFragmentManager());
    rc_buttons_inspo.setAdapter(adapter);
    rc_buttons_inspo.setLayoutManager(new GridLayoutManager(mainActivity,2));
}
}