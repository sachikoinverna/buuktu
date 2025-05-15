package com.example.buuktu.views;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.buuktu.R;
import com.example.buuktu.models.ScenariokieModel;
import com.example.buuktu.models.UserkieModel;
import com.example.buuktu.models.WorldkieModel;
import com.example.buuktu.utils.DrawableUtils;
import com.example.buuktu.utils.NavigationUtils;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;


public class Scenariokie extends Fragment implements View.OnClickListener {

    UserkieModel userkieModel;
    String UID,mode,UID_WORLDKIE,UID_AUTHOR;
    ScenariokieModel scenariokieModel;
    WorldkieModel worldkieModel;
    MainActivity mainActivity;
    TextView tv_locked_scenariokie, tv_nameUserScenariokieView, tv_nameScenariokieView, tv_usernameScenariokieView, tv_nameWorldkieViewScenariokie;
    ImageButton ib_save,ib_back, ib_scenariokieView;
    ImageView iv_locked_scenariokie;
    FragmentManager fragmentManager;

    public Scenariokie() {
    }

    public static Scenariokie newInstance() {
        return new Scenariokie();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mode = getArguments().getString("mode");
            UID = getArguments().getString("UID");
            UID_WORLDKIE = getArguments().getString("UID_WORLDKIE");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scenariokie, container, false);
        initComponents(view);
        setVisibility();
        setListeners();

        getScenariokie();


        return view;
    }
    private void getScenariokie(){
        mainActivity.getCollectionUsers().document(UID_AUTHOR).addSnapshotListener((documentSnapshot, e) -> {
            if (e != null) return;


            if (documentSnapshot != null) {
                userkieModel = UserkieModel.fromSnapshot(documentSnapshot);
                tv_nameUserScenariokieView.setText(userkieModel.getName());
                tv_usernameScenariokieView.setText(userkieModel.getUsername());

                mainActivity.getCollectionScenariokies().document(UID).addSnapshotListener((queryDocumentSnapshots, ex) -> {
                    if (ex != null) return;
                    scenariokieModel = scenariokieModel.fromSnapshot(queryDocumentSnapshots);
                    tv_nameScenariokieView.setText(scenariokieModel.getName());
                    getProfilePhoto();
                });
                mainActivity.getCollectionWorldkies().document(UID_WORLDKIE).addSnapshotListener((queryDocumentSnapshots2, ex) -> {
                    if (ex != null) return;
                    worldkieModel = WorldkieModel.fromSnapshot(queryDocumentSnapshots2);
                    tv_nameWorldkieViewScenariokie.setText(worldkieModel.getName());
                });
                iv_locked_scenariokie.setBackgroundResource(((!userkieModel.isProfile_private() && mode.equals("other")) || (mode.equals("self") || (!worldkieModel.isWorldkie_private()&& mode.equals("other")) || (!scenariokieModel.isScenariokie_private()&&mode.equals("other")))  ? R.drawable.twotone_lock_24:R.drawable.twotone_build_circle_24));
                tv_locked_scenariokie.setText(((!userkieModel.isProfile_private() && mode.equals("other")) || (mode.equals("self")|| (!worldkieModel.isWorldkie_private()&& mode.equals("other")) || (!scenariokieModel.isScenariokie_private()&&mode.equals("other"))) ? mainActivity.getString(R.string.wait_new_info):mainActivity.getString(R.string.private_stuffkie)));
            }
        });
    }
    private void setListeners(){
        ib_back.setOnClickListener(this);
    }
    private void getProfilePhoto() {
        if (scenariokieModel.isPhoto_default()) {
            String id_photo = scenariokieModel.getPhoto_id();
            int resId = mainActivity.getResources().getIdentifier(id_photo, "mipmap", mainActivity.getPackageName());

            if (resId != 0) {
                Drawable drawable = ContextCompat.getDrawable(mainActivity, resId);
                ib_scenariokieView.setImageDrawable(drawable);
                try {
                    DrawableUtils.personalizarImagenCuadradoButton(mainActivity, 115 / 6, 7, R.color.brownMaroon, drawable, ib_scenariokieView);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        } else {
            mainActivity.getFirebaseStorageScenariokies().getReference(UID).listAll().addOnSuccessListener(listResult -> {
                for (StorageReference item : listResult.getItems()) {
                    if (item.getName().startsWith("cover")) {
                        item.getDownloadUrl().addOnSuccessListener(uri -> {
                            // try {
                            DrawableUtils.personalizarImagenCuadradoButton(mainActivity, 115 / 7, 7, R.color.greenWhatever, uri, ib_scenariokieView);
                            //} catch (IOException e) {
                            //    throw new RuntimeException(e);
                            // }
                        });
                    }
                }
            }).addOnFailureListener(e -> Log.e("Storage", "Error listando archivos: " + e.getMessage()));


        }
    }
    private void initComponents(View view){
        tv_nameWorldkieViewScenariokie = view.findViewById(R.id.tv_nameWorldkieViewScenariokie);
        tv_nameUserScenariokieView = view.findViewById(R.id.tv_nameUserScenariokieView);
        ib_scenariokieView = view.findViewById(R.id.ib_scenariokieView);
        iv_locked_scenariokie = view.findViewById(R.id.iv_locked_scenariokie);
        tv_locked_scenariokie = view.findViewById(R.id.tv_locked_scenariokie);
        tv_nameScenariokieView = view.findViewById(R.id.tv_nameScenariokieView);
        tv_usernameScenariokieView = view.findViewById(R.id.tv_usernameScenariokieView);
        mainActivity = (MainActivity)getActivity();
        ib_save = mainActivity.getIb_save();
        ib_back = mainActivity.getBackButton();
        fragmentManager = mainActivity.getSupportFragmentManager();
        UID_AUTHOR = mode.equals("other") ? getArguments().getString("UID_AUTHOR") : mainActivity.getUID();
    }
    private void setVisibility(){
        ib_save.setVisibility(View.GONE);
        ib_back.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        // Comprueba si se ha presionado el botón de retroceso.
        if(v.getId()==R.id.ib_back){
            NavigationUtils.goBack(fragmentManager,mainActivity);
        }
    }
}