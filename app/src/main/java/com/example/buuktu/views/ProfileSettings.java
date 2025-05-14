package com.example.buuktu.views;

import static android.widget.Toast.LENGTH_LONG;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.buuktu.R;
import com.example.buuktu.adapters.SettingAdapter;
import com.example.buuktu.models.SettingModel;
import com.example.buuktu.models.UserkieModel;
import com.example.buuktu.utils.NavigationUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

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
    FirebaseFirestore db;
    CollectionReference userkies;
    DocumentReference userkie;
    String UID;
    FirebaseAuth firebaseAuth;
    UserkieModel userkieModel;
    ImageButton backButton,ib_save,ib_profile_superior;
    FragmentManager fragmentManager;
    private final CompoundButton.OnCheckedChangeListener switchListener = (buttonView, isChecked) -> {
        if(!lastValueProfilePrivate.equals(isChecked)) {
            Map<String, Object> worldkieData = new HashMap<>();
            worldkieData.put("private", isChecked);
            userkie.update(worldkieData);
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

       setVar();

        initComponents(view);
        setListeners();
        setVisibility();
        // 🔁 Listener para cambios en Firestore
        userkie.addSnapshotListener((documentSnapshot, e) -> {
            if (e != null) {
                Log.e("Error", e.getMessage());
                Toast.makeText(getContext(), "Error al escuchar cambios: " + e.getMessage(), LENGTH_LONG).show();
                return;
            }

            if (documentSnapshot != null) {
                dataSet.clear();

                    userkieModel = UserkieModel.fromSnapshot(documentSnapshot);

                // 🧠 Evitar bucle al cambiar el estado desde código
                tb_profile_private_settings.setOnCheckedChangeListener(null);
                tb_profile_private_settings.setChecked(userkieModel.isProfile_private());
                tb_profile_private_settings.setOnCheckedChangeListener(switchListener);

                dataSet.add(new SettingModel(mainActivity.getResources().getString(R.string.name), userkieModel.getName()));
                dataSet.add(new SettingModel(mainActivity.getResources().getString(R.string.pronouns), userkieModel.getPronouns()));
                updateRecyclerView();
            }

            // Inicialmente le asignamos el listener
            tb_profile_private_settings.setOnCheckedChangeListener(switchListener);
        });
        return view;
    }
    private void setVisibility(){
        backButton.setVisibility(View.VISIBLE);
        ib_profile_superior.setVisibility(View.VISIBLE);
        ib_save.setVisibility(View.GONE);

    }
    private void setVar(){
        db = FirebaseFirestore.getInstance();
        userkies = db.collection("Users");
        firebaseAuth = FirebaseAuth.getInstance();
        UID = firebaseAuth.getUid();
        userkie = userkies.document(UID);
    }
    private void setListeners(){
        backButton.setOnClickListener(this);
    }
    private void initComponents(View view) {
        mainActivity = (MainActivity) getActivity();
        tb_profile_private_settings = view.findViewById(R.id.tb_profile_private_settings);
        rv_settings_profile = view.findViewById(R.id.rv_settings_profile);
        rv_settings_profile.setLayoutManager(new LinearLayoutManager(mainActivity));
        settingAdapter = new SettingAdapter(dataSet, mainActivity, UID);
        rv_settings_profile.setAdapter(settingAdapter);
        backButton = mainActivity.getBackButton();
        ib_profile_superior = mainActivity.getIb_self_profile();
        ib_save = mainActivity.getIb_save();
        fragmentManager = mainActivity.getSupportFragmentManager();
    }
    private void updateRecyclerView() {
        settingAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        // Comprueba si se ha presionado el botón de retroceso.
        if(v.getId()==R.id.ib_back){
            NavigationUtils.goBack(fragmentManager,mainActivity);
        }
    }
}