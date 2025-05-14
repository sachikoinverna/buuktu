package com.example.buuktu.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.buuktu.R;
import com.example.buuktu.utils.DrawableUtils;
import com.example.buuktu.utils.NavigationUtils;


public class WorldkieMenu extends Fragment implements View.OnClickListener {
    FragmentManager fragmentManager;
    MainActivity mainActivity;
    private Button tv_characterkiesAdd,textView5,textView8;
    ImageButton backButton,ib_profile_superior;
   String worldkie_id;
    Bundle bundle = new Bundle();
    ImageView iv_scenariokies_worldkie_menu,iv_characterkies_worldkie_menu,iv_stuffkies_worldkie_menu;
    public WorldkieMenu() {}

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
        View view =inflater.inflate(R.layout.fragment_worldkie_menu, container, false);
        mainActivity = (MainActivity) getActivity();

        initComponents(view);
        bundle.putString("worldkie_id",worldkie_id);
        setVisibility();
        setListeners();
        fragmentManager = requireActivity().getSupportFragmentManager();
        return view;
    }
    private void initComponents(View view){
        tv_characterkiesAdd = view.findViewById( R.id.tv_characterkies);
        textView5 = view.findViewById(R.id.textView5);
        textView8 = view.findViewById(R.id.textView8);
        iv_scenariokies_worldkie_menu = view.findViewById(R.id.iv_scenariokies_worldkie_menu);
        iv_stuffkies_worldkie_menu = view.findViewById(R.id.iv_stuffkies_worldkie_menu);
        iv_characterkies_worldkie_menu = view.findViewById(R.id.iv_characterkies_worldkie_menu);
        backButton = mainActivity.getBackButton();
        ib_profile_superior = mainActivity.getIb_self_profile();

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

        DrawableUtils.personalizarImagenCuadradoButton(mainActivity,50,10,R.color.white,DrawableUtils.drawableToBitmap(iv_characterkies_worldkie_menu.getDrawable()),iv_characterkies_worldkie_menu);
        DrawableUtils.personalizarImagenCuadradoButton(mainActivity,50,10,R.color.white,DrawableUtils.drawableToBitmap(iv_scenariokies_worldkie_menu.getDrawable()),iv_scenariokies_worldkie_menu);
        DrawableUtils.personalizarImagenCuadradoButton(mainActivity,50,10,R.color.white,DrawableUtils.drawableToBitmap(iv_stuffkies_worldkie_menu.getDrawable()),iv_stuffkies_worldkie_menu);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.ib_back){
            NavigationUtils.goBack(fragmentManager,mainActivity);
        } else if (v.getId()==R.id.tv_characterkies) {
            NavigationUtils.goNewFragmentWithBundle(bundle,fragmentManager,new Characterkies());
        } else if (v.getId()==R.id.textView5) {
            NavigationUtils.goNewFragmentWithBundle(bundle,fragmentManager,new Scenariokies());

        } else if (v.getId()==R.id.textView8) {
            NavigationUtils.goNewFragmentWithBundle(bundle,fragmentManager,new Stuffkies());

        }
    }
}