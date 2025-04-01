package com.example.buuktu.views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.buuktu.R;
import com.example.buuktu.adapters.CardAdapter;
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

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView rc_buttons_inspo;
    private CardAdapter adapter;
    public Inspo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Inspo.
     */
    // TODO: Rename and change types and number of parameters
    public static Inspo newInstance(String param1, String param2) {
        Inspo fragment = new Inspo();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inspo, container, false);
        rc_buttons_inspo = view.findViewById(R.id.rc_buttons_inspo);
        rc_buttons_inspo.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // Simular datos
        List<CardItem> items = new ArrayList<>();
        items.add(new CardItem(R.drawable.sharp_emoji_nature_24, "Elemento 1"));
        items.add(new CardItem(R.drawable.twotone_message_24, "Elemento 2"));
        items.add(new CardItem(R.drawable.twotone_catching_pokemon_24, "Elemento 3"));
        items.add(new CardItem(R.drawable.twotone_delete_sweep_24, "Elemento 4"));

        // Configurar el adaptador
       /* adapter = new CardAdapter(getContext(), items -> {
            // Manejar clic en el CardView
            // Por ejemplo, agregar el campo al layout principal
            // o realizar otra acción
        });*/
        rc_buttons_inspo.setAdapter(adapter);
        return view;
    }
}