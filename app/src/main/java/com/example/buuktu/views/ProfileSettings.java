package com.example.buuktu.views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;

import com.example.buuktu.R;
import com.example.buuktu.adapters.SettingAdapter;
import com.example.buuktu.models.SettingModel;
import com.example.buuktu.models.UserkieModel;
import com.example.buuktu.utils.NavigationUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfileSettings extends Fragment implements View.OnClickListener {
    private SettingAdapter settingAdapter;
    MainActivity mainActivity;
    private RecyclerView rv_settings_profile;
    private final ArrayList<SettingModel> dataSet = new ArrayList<>();
    Boolean lastValueProfilePrivate=false;
    Switch tb_profile_private_settings;

    UserkieModel userkieModel;
    ImageButton backButton,ib_save,ib_profile_superior;
    FragmentManager fragmentManager;
    private final CompoundButton.OnCheckedChangeListener switchListener = (buttonView, isChecked) -> {
        if(!lastValueProfilePrivate.equals(isChecked)) {
            Map<String, Object> worldkieData = new HashMap<>();
            worldkieData.put("private", isChecked);
            mainActivity.getCollectionUsers().document(mainActivity.getUID()).update(worldkieData);
            lastValueProfilePrivate=isChecked;
        }
    };

    public ProfileSettings() {
    }

    public static ProfileSettings newInstance() {
        return new ProfileSettings();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_settings, container, false);
        initComponents(view);
        setListeners();
        setVisibility();
        getUserkie();
        setRecyclerView();
        return view;
    }
    private void getUserkie() {
        mainActivity.getCollectionUsers().document(mainActivity.getUID()).addSnapshotListener((documentSnapshot, e) -> {
            if (e != null) return;
            if (documentSnapshot != null) {
                dataSet.clear();
                userkieModel = UserkieModel.fromSnapshot(documentSnapshot);
                tb_profile_private_settings.setOnCheckedChangeListener(null);
                tb_profile_private_settings.setChecked(userkieModel.isProfile_private());
                tb_profile_private_settings.setOnCheckedChangeListener(switchListener);
                dataSet.add(new SettingModel(mainActivity.getResources().getString(R.string.name), userkieModel.getName()));
                dataSet.add(new SettingModel(mainActivity.getResources().getString(R.string.pronouns), userkieModel.getPronouns()));

            }
            settingAdapter.notifyDataSetChanged();
        });
    }
    private void setRecyclerView() {
        rv_settings_profile.setLayoutManager(new LinearLayoutManager(mainActivity));
        settingAdapter = new SettingAdapter(dataSet, mainActivity);
        rv_settings_profile.setAdapter(settingAdapter);
    }
    private void setVisibility(){
        backButton.setVisibility(View.VISIBLE);
        ib_profile_superior.setVisibility(View.VISIBLE);
        ib_save.setVisibility(View.GONE);

    }
    private void setListeners(){
        tb_profile_private_settings.setOnCheckedChangeListener(switchListener);
        backButton.setOnClickListener(this);
    }
    private void initComponents(View view) {
        mainActivity = (MainActivity) getActivity();
        tb_profile_private_settings = view.findViewById(R.id.tb_profile_private_settings);
        rv_settings_profile = view.findViewById(R.id.rv_settings_profile);
        backButton = mainActivity.getBackButton();
        ib_profile_superior = mainActivity.getIb_self_profile();
        ib_save = mainActivity.getIb_save();
        fragmentManager = mainActivity.getSupportFragmentManager();
    }


    @Override
    public void onClick(View v) {
        // Comprueba si se ha presionado el botón de retroceso.
        if(v.getId()==R.id.ib_back){
            NavigationUtils.goBack(fragmentManager,mainActivity);
        }
    }
}