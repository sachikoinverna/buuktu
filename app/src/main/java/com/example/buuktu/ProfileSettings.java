package com.example.buuktu;

import static android.widget.Toast.LENGTH_LONG;

import static androidx.browser.customtabs.CustomTabsClient.getPackageName;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.buuktu.adapters.SettingAdapter;
import com.example.buuktu.dialogs.InfoFutureFunctionDialog;
import com.example.buuktu.models.SettingModel;
import com.example.buuktu.models.UserkieModel;
import com.example.buuktu.utils.DrawableUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileSettings#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileSettings extends Fragment {
    private SettingAdapter settingAdapter;
    private RecyclerView rv_settings_profile;
    private ArrayList<SettingModel> dataSet = new ArrayList<SettingModel>();
    Switch tb_profile_private_settings;
    FirebaseFirestore db;
    CollectionReference userkies;
    DocumentReference userkie;
    String UID;
    FirebaseAuth firebaseAuth;
    UserkieModel userkieModel;
    private final CompoundButton.OnCheckedChangeListener switchListener = (buttonView, isChecked) -> {
        Map<String, Object> worldkieData = new HashMap<>();
        worldkieData.put("private", isChecked);
        userkie.update(worldkieData);
    };

    public ProfileSettings() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProfileSettings.
     */
    public static ProfileSettings newInstance() {
        return new ProfileSettings();
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
        View view = inflater.inflate(R.layout.fragment_profile_settings, container, false);

        db = FirebaseFirestore.getInstance();
        userkies = db.collection("Users");
        firebaseAuth = FirebaseAuth.getInstance();
        UID = firebaseAuth.getUid();
        userkie = userkies.document(UID);

        initComponents(view);

        // 🔁 Listener para cambios en Firestore
        userkie.addSnapshotListener((documentSnapshot, e) -> {
            if (e != null) {
                Log.e("Error", e.getMessage());
                Toast.makeText(getContext(), "Error al escuchar cambios: " + e.getMessage(), LENGTH_LONG).show();
                return;
            }

            if (documentSnapshot != null) {
                dataSet.clear();

                boolean isPrivate = documentSnapshot.getBoolean("private") != null && documentSnapshot.getBoolean("private");

                userkieModel = new UserkieModel(
                        UID,
                        documentSnapshot.getString("name"),
                        R.drawable.add_button,
                        documentSnapshot.getString("username"),
                        documentSnapshot.getBoolean("photo_default"),
                        isPrivate
                );

                // 🧠 Evitar bucle al cambiar el estado desde código
                tb_profile_private_settings.setOnCheckedChangeListener(null);
                tb_profile_private_settings.setChecked(isPrivate);
                tb_profile_private_settings.setOnCheckedChangeListener(switchListener);

                dataSet.add(new SettingModel("Nombre", userkieModel.getName()));
                dataSet.add(new SettingModel("Pronombres", "She/her"));
                updateRecyclerView();
            }
            ;

            // Inicialmente le asignamos el listener
            tb_profile_private_settings.setOnCheckedChangeListener(switchListener);
        });
        return view;
    }

    private void initComponents(View view) {
        tb_profile_private_settings = view.findViewById(R.id.tb_profile_private_settings);
        rv_settings_profile = view.findViewById(R.id.rv_settings_profile);
        rv_settings_profile.setLayoutManager(new LinearLayoutManager(getContext()));
        settingAdapter = new SettingAdapter(dataSet, getContext(), UID);
        rv_settings_profile.setAdapter(settingAdapter);
    }
    private void updateRecyclerView() {
        settingAdapter.notifyDataSetChanged();
    }
}