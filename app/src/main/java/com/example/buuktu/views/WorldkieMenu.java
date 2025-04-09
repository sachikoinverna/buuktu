package com.example.buuktu.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.buuktu.CreateCharacterkie;
import com.example.buuktu.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WorldkieMenu#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorldkieMenu extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView tv_characterkiesAdd;
    private FragmentManager fragmentManager;
    private Fragment createCharacterkie;
    public WorldkieMenu() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WorldkieMenu.
     */
    // TODO: Rename and change types and number of parameters
    public static WorldkieMenu newInstance(String param1, String param2) {
        WorldkieMenu fragment = new WorldkieMenu();
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
        View view =inflater.inflate(R.layout.fragment_worldkie_menu, container, false);
        MainActivity mainActivity = (MainActivity) getActivity();
        ImageButton backButton = mainActivity.getBackButton();
        backButton.setVisibility(View.VISIBLE);
        tv_characterkiesAdd = view.findViewById( R.id.tv_characterkies);
        fragmentManager = getParentFragmentManager();
        tv_characterkiesAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createCharacterkie = new CreateCharacterkie();
                // Bundle bundle = new Bundle();
                //bundle.putString("worlkie_id",dataSet.get(holder.getAdapterPosition()).getUID());
                //createCharacterkie.setArguments(bundle);

                fragmentManager.beginTransaction().replace(R.id.fragment_container, createCharacterkie) .addToBackStack(null) // Permite regresar atrás con el botón de retroceso
                        .commit();
            }
        });
        return view;
    }
}