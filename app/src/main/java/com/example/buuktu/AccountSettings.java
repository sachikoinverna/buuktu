package com.example.buuktu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.buuktu.adapters.SettingAdapter;
import com.example.buuktu.models.SettingModel;
import com.example.buuktu.views.MainActivity;
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
    ImageButton backButton;
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
        MainActivity mainActivity = (MainActivity) getActivity();
        backButton = mainActivity.getBackButton();
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackToPreviousFragment();
            }
        });

        dataSet.add(new SettingModel("Correo electronico",firebaseAuth.getCurrentUser().getEmail()));
        dataSet.add(new SettingModel("Password","*******"));
        updateRecyclerView(dataSet);
        return view;
    }
    private void initComponents(View view){
        rv_account_settings = view.findViewById(R.id.rv_account_settings);
    }
    private void goBackToPreviousFragment() {
        // Verifica si hay un fragmento en la pila de retroceso
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

        if (fragmentManager.getBackStackEntryCount() > 0) {
            // Si hay fragmentos en la pila de retroceso, navega hacia atrás
            fragmentManager.popBackStack(); // Retrocede al fragmento anterior
        } else {
            // Si no hay fragmentos en la pila, puede que quieras cerrar la actividad o hacer alguna otra acción
            // Por ejemplo, cerrar la actividad:
            requireActivity().onBackPressed(); // Realiza el retroceso por defecto (salir de la actividad)
        }
    }
    private void updateRecyclerView(ArrayList<SettingModel> settingModels){
        settingAdapter = new SettingAdapter(settingModels,getContext(),UID);
        rv_account_settings.setAdapter(settingAdapter);
        rv_account_settings.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}