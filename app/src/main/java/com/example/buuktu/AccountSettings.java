package com.example.buuktu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.buuktu.adapters.SettingAdapter;
import com.example.buuktu.models.SettingModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountSettings#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountSettings extends Fragment {
    SettingAdapter settingAdapter;
    private RecyclerView rv_settings_profile;
    private ArrayList<SettingModel> dataSet = new ArrayList<SettingModel>();
    String UID;
    FirebaseAuth firebaseAuth;
    RecyclerView rv_account_settings;
    public AccountSettings() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AccountSettings.
     */
    public static AccountSettings newInstance() {
        return new AccountSettings();
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
        View view = inflater.inflate(R.layout.fragment_account_settings, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        initComponents(view);
        UID = firebaseAuth.getUid();
        firebaseAuth.getCurrentUser().getEmail();
        dataSet.add(new SettingModel("Correo electronico",firebaseAuth.getCurrentUser().getEmail()));
        dataSet.add(new SettingModel("Password","*******"));
        updateRecyclerView(dataSet);
        return view;
    }
    private void initComponents(View view){
        rv_account_settings = view.findViewById(R.id.rv_account_settings);
    }
    private void updateRecyclerView(ArrayList<SettingModel> settingModels){
        settingAdapter = new SettingAdapter(settingModels,getContext(),UID);
        rv_account_settings.setAdapter(settingAdapter);
        rv_account_settings.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}