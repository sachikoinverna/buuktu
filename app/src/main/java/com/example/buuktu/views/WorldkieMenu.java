package com.example.buuktu.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.buuktu.CreateCharacterkie;
import com.example.buuktu.CreateEditScenariokie;
import com.example.buuktu.CreateEditStuffkie;
import com.example.buuktu.R;
import com.example.buuktu.StuffkieView;
import com.example.buuktu.utils.NavigationUtils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WorldkieMenu#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorldkieMenu extends Fragment implements View.OnClickListener {
    FragmentManager fragmentManager;
    MainActivity mainActivity;
    FragmentActivity activity;
    private TextView tv_characterkiesAdd,textView5,textView8;
    private Fragment createCharacterkie,createScenariokie,createStuffkie;
    ImageButton backButton,ib_profile_superior;
   String worldkie_id;
    Bundle bundle = new Bundle();
    public WorldkieMenu() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment WorldkieMenu.
     */
    // TODO: Rename and change types and number of parameters
    public static WorldkieMenu newInstance() {
        return new WorldkieMenu();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.worldkie_id = getArguments().getString("worldkie_id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_worldkie_menu, container, false);
        mainActivity = (MainActivity) getActivity();
        backButton = mainActivity.getBackButton();
        ib_profile_superior = mainActivity.getIb_self_profile();
        initComponents(view);
        bundle.putString("worldkie_id",worldkie_id);
        setVisibility();
        setListeners();
        fragmentManager = requireActivity().getSupportFragmentManager();
        activity = requireActivity();        
        return view;
    }
    private void initComponents(View view){
        tv_characterkiesAdd = view.findViewById( R.id.tv_characterkies);
        textView5 = view.findViewById(R.id.textView5);
        textView8 = view.findViewById(R.id.textView8);
    }
    private void setListeners(){
        backButton.setOnClickListener(this);
        tv_characterkiesAdd.setOnClickListener(this);
        textView5.setOnClickListener(this);
        textView8.setOnClickListener(this);
    }
    private void setVisibility(){
        backButton.setVisibility(View.VISIBLE);
        ib_profile_superior.setVisibility(View.VISIBLE);

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.ib_back){
            NavigationUtils.goBack(fragmentManager,activity);
        } else if (v.getId()==R.id.tv_characterkies) {
            NavigationUtils.goNewFragmentWithBundle(bundle,fragmentManager,new Characterkies());
        } else if (v.getId()==R.id.textView5) {
            NavigationUtils.goNewFragmentWithBundle(bundle,fragmentManager,new CreateEditScenariokie());

        } else if (v.getId()==R.id.textView8) {
            NavigationUtils.goNewFragmentWithBundle(bundle,fragmentManager,new Stuffkies());

        }
    }
}