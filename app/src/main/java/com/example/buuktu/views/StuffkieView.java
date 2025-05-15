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
import com.example.buuktu.models.StuffkieModel;
import com.example.buuktu.models.UserkieModel;
import com.example.buuktu.models.WorldkieModel;
import com.example.buuktu.utils.DrawableUtils;
import com.example.buuktu.utils.NavigationUtils;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;


public class StuffkieView extends Fragment implements View.OnClickListener {
    UserkieModel userkieModel;
    String UID,mode,UID_WORLDKIE,UID_AUTHOR;
    StuffkieModel stuffkieModel;
    WorldkieModel worldkieModel;
    MainActivity mainActivity;
    TextView tv_locked_stuffkie,tv_nameUserStuffkieView,tv_nameStuffkieView,tv_usernameStuffkieView,tv_nameWorldkieViewStuffkie;
    ImageButton ib_save,ib_back,ib_stuffkieView;
    ImageView iv_locked_stuffkie;
    FragmentManager fragmentManager;
    public StuffkieView() {
    }

    public static StuffkieView newInstance() {
        return new StuffkieView();
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
        View view = inflater.inflate(R.layout.fragment_stuffkie_view, container, false);
        initComponents(view);
        setVisibility();
        setListeners();
        UID_AUTHOR = mode.equals("other") ? getArguments().getString("UID_AUTHOR") : mainActivity.getUID();
        getStuffkie();
        return view;
    }
    private void getStuffkie(){
        mainActivity.getCollectionUsers().document(UID_AUTHOR).addSnapshotListener((documentSnapshot, e) -> {
            if (e != null) return;

            if (documentSnapshot != null) {
                userkieModel = UserkieModel.fromSnapshot(documentSnapshot);
                tv_nameUserStuffkieView.setText(userkieModel.getName());
                tv_usernameStuffkieView.setText(userkieModel.getUsername());

                mainActivity.getCollectionStuffkies().document(UID).addSnapshotListener((queryDocumentSnapshots, ex) -> {
                    if (ex != null) return;

                    stuffkieModel = StuffkieModel.fromSnapshot(queryDocumentSnapshots);
                    tv_nameStuffkieView.setText(stuffkieModel.getName());
                    getProfilePhoto();
                });
                mainActivity.getCollectionWorldkies().document(UID_WORLDKIE).addSnapshotListener((queryDocumentSnapshots2, ex) -> {
                    if (ex != null) return;
                    worldkieModel = WorldkieModel.fromSnapshot(queryDocumentSnapshots2);
                    tv_nameWorldkieViewStuffkie.setText(worldkieModel.getName());
                });
                iv_locked_stuffkie.setBackgroundResource(((!userkieModel.isProfile_private() && mode.equals("other")) || (mode.equals("self") || (!worldkieModel.isWorldkie_private()&& mode.equals("other")) || (!stuffkieModel.isStuffkie_private()&&mode.equals("other")))  ? R.drawable.twotone_lock_24:R.drawable.twotone_build_circle_24));
                tv_locked_stuffkie.setText(((!userkieModel.isProfile_private() && mode.equals("other")) || (mode.equals("self")|| (!worldkieModel.isWorldkie_private()&& mode.equals("other")) || (!stuffkieModel.isStuffkie_private()&&mode.equals("other"))) ? mainActivity.getString(R.string.wait_new_info):mainActivity.getString(R.string.private_stuffkie)));
            }
        });
    }
    private void setListeners(){
        ib_back.setOnClickListener(this);
    }
    private void getProfilePhoto() {
        if (stuffkieModel.isPhoto_default()) {
            int resId = mainActivity.getResources().getIdentifier(stuffkieModel.getPhoto_id(), "mipmap", mainActivity.getPackageName());

            if (resId != 0) {
                Drawable drawable = ContextCompat.getDrawable(mainActivity, resId);
                ib_stuffkieView.setImageDrawable(drawable);
                try {
                    DrawableUtils.personalizarImagenCuadradoButton(mainActivity, 115 / 6, 7, R.color.brownMaroon, drawable, ib_stuffkieView);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        } else {
            mainActivity.getFirebaseStorageCharacterkies().getReference(UID).listAll().addOnSuccessListener(listResult -> {
                for (StorageReference item : listResult.getItems()) {
                    if (item.getName().startsWith("cover")) {
                        item.getDownloadUrl().addOnSuccessListener(uri -> {
                            // try {
                            DrawableUtils.personalizarImagenCuadradoButton(mainActivity, 115 / 7, 7, R.color.greenWhatever, uri, ib_stuffkieView);
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
        tv_nameWorldkieViewStuffkie = view.findViewById(R.id.tv_nameWorldkieViewStuffkie);
        tv_nameUserStuffkieView = view.findViewById(R.id.tv_nameUserStuffkieView);
        ib_stuffkieView = view.findViewById(R.id.ib_stuffkieView);
        iv_locked_stuffkie = view.findViewById(R.id.iv_locked_stuffkie);
        tv_locked_stuffkie = view.findViewById(R.id.tv_locked_stuffkie);
        tv_nameStuffkieView = view.findViewById(R.id.tv_nameStuffkieView);
        tv_usernameStuffkieView = view.findViewById(R.id.tv_usernameStuffkieView);
        mainActivity = (MainActivity)getActivity();
        ib_save = mainActivity.getIb_save();
        ib_back = mainActivity.getBackButton();
        fragmentManager = mainActivity.getSupportFragmentManager();
    }
    private void setVisibility(){
        ib_save.setVisibility(View.GONE);
        ib_back.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.ib_back){
            NavigationUtils.goBack(fragmentManager,mainActivity);
        }
    }
}