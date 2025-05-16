package com.example.buuktu.views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.buuktu.R;
import com.example.buuktu.adapters.SettingAdapter;
import com.example.buuktu.models.SettingModel;
import com.example.buuktu.utils.NavigationUtils;

import java.util.ArrayList;


public class AccountSettings extends Fragment implements View.OnClickListener {
    private SettingAdapter settingAdapter;
    private ArrayList<SettingModel> dataSet = new ArrayList<>();
    private RecyclerView rv_account_settings;
    private ImageButton backButton,ib_profile_superior;
    private MainActivity mainActivity;
    public AccountSettings() {
    }

    public static AccountSettings newInstance() {
        return new AccountSettings();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_settings, container, false);
        initComponents(view);
        setVisibility();
        setListeners();
        setRecyclerView();
        return view;
    }
    private void setVisibility(){
        backButton.setVisibility(View.VISIBLE);
        ib_profile_superior.setVisibility(View.VISIBLE);

    }
    private void setListeners(){
        backButton.setOnClickListener(this);

    }
    private void initComponents(View view){
        rv_account_settings = view.findViewById(R.id.rv_account_settings);
        mainActivity = (MainActivity) getActivity();
        backButton = mainActivity.getBackButton();
        ib_profile_superior = mainActivity.getIb_self_profile();
    }
    private void setRecyclerView(){
        dataSet.add(new SettingModel(mainActivity.getString(R.string.email),mainActivity.getFirebaseAuth().getCurrentUser().getEmail()));
        dataSet.add(new SettingModel(mainActivity.getString(R.string.user_password),"*******"));
        settingAdapter = new SettingAdapter(dataSet,mainActivity);
        rv_account_settings.setAdapter(settingAdapter);
        rv_account_settings.setLayoutManager(new LinearLayoutManager(mainActivity));
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.ib_back){
            NavigationUtils.goBack(requireActivity().getSupportFragmentManager(),mainActivity);
        }
    }
}