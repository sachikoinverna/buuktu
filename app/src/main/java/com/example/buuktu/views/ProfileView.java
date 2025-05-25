package com.example.buuktu.views;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.buuktu.R;
import com.example.buuktu.adapters.CharacterkiesUserPreviewAdapter;
import com.example.buuktu.adapters.ScenariokiesUserPreviewAdapter;
import com.example.buuktu.adapters.StuffkiesUserPreviewAdapter;
import com.example.buuktu.adapters.WorldkiesUserPreviewAdapter;
import com.example.buuktu.models.CharacterkieModel;
import com.example.buuktu.models.ScenariokieModel;
import com.example.buuktu.models.StuffkieModel;
import com.example.buuktu.models.UserkieModel;
import com.example.buuktu.models.WorldkieModel;
import com.example.buuktu.utils.DrawableUtils;
import com.example.buuktu.utils.EfectsUtils;
import com.example.buuktu.utils.NavigationUtils;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class ProfileView extends Fragment implements View.OnClickListener {
    String mode;
    private ImageButton ib_profileView, ib_save, ib_back, ib_profile_superior;
    ImageView iv_locked_profile;
    private TextView tv_usernameProfileView, tv_nameProfileView, tv_worldkiesPreviewUserkie, tv_stuffkiesPreviewUserkie, tv_characterkiesPreviewUserkie, tv_locked_profile, tv_scenariokiesPreviewUserkie;
    MaterialCardView cv_characterkiesPreviewUserkie, cv_stuffkiesPreviewUserkie, cv_worldkiesPreviewUserkie, cv_scenariokiesPreviewUserkie;
    ArrayList<CharacterkieModel> characterkieArrayList=new ArrayList<>();
    ArrayList<StuffkieModel> stuffkieArrayList=new ArrayList<>();
    ArrayList<WorldkieModel> worldkieArrayList=new ArrayList<>();
    ArrayList<ScenariokieModel> scenariokieModelArrayList = new ArrayList<>();
    RecyclerView rc_characterkiePreviewUserkie, rc_stuffkiePreviewUserkie, rc_worldkiePreviewUserkie, rc_scenariokiePreviewUserkie;
    CharacterkiesUserPreviewAdapter characterkiesUserPreviewAdapter;
    StuffkiesUserPreviewAdapter stuffkiesUserPreviewAdapter;
    WorldkiesUserPreviewAdapter worldkiesUserPreviewAdapter;
    ScenariokiesUserPreviewAdapter scenariokiesUserPreviewAdapter;
    String UID;
    UserkieModel userkieModel;
    FragmentManager fragmentManager;
    MainActivity mainActivity;

    public ProfileView() {
    }

    public static ProfileView newInstance() {
        return new ProfileView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mode = getArguments().getString("mode");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_view, container, false);

        initComponents(view);
        setListeners();
        getData();


        return view;

    }

    private void getData() {
        UID = mode.equals("other") ? getArguments().getString("UID") : mainActivity.getUID();
        getUser();

    }

    private void getUser() {
        mainActivity.getCollectionUsers().document(UID).addSnapshotListener((documentSnapshot, e) -> {
            if (e != null) return;
            if (documentSnapshot != null) {
                userkieModel = UserkieModel.fromSnapshot(documentSnapshot);
                getProfilePhoto();
                tv_nameProfileView.setText(userkieModel.getName());
                tv_usernameProfileView.setText(userkieModel.getUsername());
                if ((userkieModel.isProfile_private() && mode.equals("other"))) {
                    hideShowSection(tv_characterkiesPreviewUserkie, cv_characterkiesPreviewUserkie, false);
                    hideShowSection(tv_worldkiesPreviewUserkie, cv_worldkiesPreviewUserkie, false);
                    hideShowSection(tv_stuffkiesPreviewUserkie, cv_stuffkiesPreviewUserkie, false);
                    hideShowSection(tv_scenariokiesPreviewUserkie, cv_scenariokiesPreviewUserkie, false);

                } else {
                    getStuffkies();
                    getWorldkies();
                    getScenariokies();
                    getCharacterkies();
                }
                iv_locked_profile.setVisibility(userkieModel.isProfile_private()&& !mode.equals("self")?View.VISIBLE:View.GONE);
                tv_locked_profile.setVisibility(userkieModel.isProfile_private()&&!mode.equals("self")?View.VISIBLE:View.GONE);
            }
        });
    }
    private void getStuffkies() {
        Query queryStuffkies = mainActivity.getCollectionStuffkies().whereEqualTo("AUTHOR_UID", UID);
        addDraftQuery(queryStuffkies).addSnapshotListener((queryDocumentSnapshots, ex) -> {
            if (ex != null) return;

            if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                stuffkieArrayList.clear();
                for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                    stuffkieArrayList.add(StuffkieModel.fromSnapshot(doc));
                }
                hideShowSection(tv_stuffkiesPreviewUserkie, cv_stuffkiesPreviewUserkie, true);

                setRecyclerViewStuffkies();
            } else {
                hideShowSection(tv_stuffkiesPreviewUserkie, cv_stuffkiesPreviewUserkie, false);
                setRecyclerViewStuffkies();

            }
        });
    }
    private void getWorldkies(){
            Query queryWorldkies = mainActivity.getCollectionWorldkies().whereEqualTo("UID_AUTHOR",UID);
        addDraftQuery(queryWorldkies).addSnapshotListener((queryDocumentSnapshots, ex) -> {
                if (ex != null) return;


                if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                    worldkieArrayList.clear(); // Limpia la lista antes de agregar nuevos datos

                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                        worldkieArrayList.add(WorldkieModel.fromSnapshot(doc));

                    }
                    hideShowSection(tv_worldkiesPreviewUserkie,cv_worldkiesPreviewUserkie,true);

                    setRecyclerViewWorldkies();
                }else {
                    hideShowSection(tv_worldkiesPreviewUserkie,cv_worldkiesPreviewUserkie,false);

                    setRecyclerViewWorldkies();
                }
            });
        }
    private void getCharacterkies(){
            Query queryCharacterkies = mainActivity.getCollectionCharacterkies().whereEqualTo("UID_AUTHOR",UID);

        addDraftQuery(queryCharacterkies).addSnapshotListener((queryDocumentSnapshots, ex) -> {
                if (ex != null) {
                    return;
                }

                if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                    characterkieArrayList.clear(); // Limpia la lista antes de agregar nuevos datos

                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {


                        characterkieArrayList.add(CharacterkieModel.fromSnapshot(doc));

                    }
                    hideShowSection(tv_characterkiesPreviewUserkie,cv_characterkiesPreviewUserkie,true);

                    setRecyclerViewCharacterkies();
                }else {
                    hideShowSection(tv_characterkiesPreviewUserkie,cv_characterkiesPreviewUserkie,false);
                    setRecyclerViewCharacterkies();

                }
            });
        }
    private Query addDraftQuery(Query query){
        if (mode.equals("other")) {
            return query.whereEqualTo("draft", false);
        }
        return query;
    }
    private void getScenariokies(){
            Query queryScenariokies = mainActivity.getCollectionScenariokies().whereEqualTo("AUTHOR_UID",UID);
            addDraftQuery(queryScenariokies).addSnapshotListener((queryDocumentSnapshots, ex) -> {
                if (ex != null) {
                    return;
                }

                if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                    scenariokieModelArrayList.clear(); // Limpia la lista antes de agregar nuevos datos

                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                        scenariokieModelArrayList.add(ScenariokieModel.fromSnapshot(doc));

                    }
                    hideShowSection(tv_scenariokiesPreviewUserkie,cv_scenariokiesPreviewUserkie,true);

                    setRecyclerViewScenariokies();
                }else {
                    hideShowSection(tv_scenariokiesPreviewUserkie,cv_scenariokiesPreviewUserkie,false);
                    setRecyclerViewScenariokies();

                }
            });
    }
    private void hideShowSection(TextView textView,MaterialCardView materialCardView,boolean visible){
        textView.setVisibility(visible?View.VISIBLE:View.GONE);
        materialCardView.setVisibility(visible?View.VISIBLE:View.GONE);
    }
    private void initComponents(View view){
        mainActivity = (MainActivity) getActivity();
        rc_worldkiePreviewUserkie = view.findViewById(R.id.rc_worldkiePreviewUserkie);
        rc_stuffkiePreviewUserkie = view.findViewById(R.id.rc_stuffkiePreviewUserkie);
        rc_characterkiePreviewUserkie = view.findViewById(R.id.rc_characterkiePreviewUserkie);
        rc_scenariokiePreviewUserkie = view.findViewById(R.id.rc_scenariokiePreviewUserkie);
        cv_worldkiesPreviewUserkie = view.findViewById(R.id.cv_worldkiesPreviewUserkie);
        cv_stuffkiesPreviewUserkie = view.findViewById(R.id.cv_stuffkiesPreviewUserkie);
        cv_characterkiesPreviewUserkie = view.findViewById(R.id.cv_characterkiesPreviewUserkie);
        ib_profileView = view.findViewById(R.id.ib_profileView);
        tv_usernameProfileView = view.findViewById(R.id.tv_usernameProfileView);
        tv_nameProfileView = view.findViewById(R.id.tv_nameProfileView);
        tv_characterkiesPreviewUserkie = view.findViewById(R.id.tv_characterkiesPreviewUserkie);
        tv_stuffkiesPreviewUserkie = view.findViewById(R.id.tv_stuffkiesPreviewUserkie);
        tv_worldkiesPreviewUserkie = view.findViewById(R.id.tv_worldkiesPreviewUserkie);
        iv_locked_profile =view.findViewById(R.id.iv_locked_profile);
        tv_locked_profile = view.findViewById(R.id.tv_locked_profile);
        tv_scenariokiesPreviewUserkie = view.findViewById(R.id.tv_scenariokiesPreviewUserkie);
        cv_scenariokiesPreviewUserkie = view.findViewById(R.id.cv_scenariokiesPreviewUserkie);

        ib_save = mainActivity.getIb_save();
        ib_back = mainActivity.getBackButton();
        ib_profile_superior = mainActivity.getIb_self_profile();
        fragmentManager = mainActivity.getSupportFragmentManager();
        setVisibility();
    }
    private void setVisibility(){
        ib_save.setVisibility(View.GONE);
        ib_back.setVisibility(View.VISIBLE);
        ib_profile_superior.setVisibility(mode.equals("self")?View.INVISIBLE:View.VISIBLE);
    }
    private void setListeners(){
                ib_back.setOnClickListener(this);
    }
    private void getProfilePhoto(){
        ib_profileView.setVisibility(View.INVISIBLE);

            if(userkieModel.isPhoto_default()) {
                int resId = getResources().getIdentifier(userkieModel.getPhoto_id(), "mipmap", getContext().getPackageName());

                if (resId != 0) {
                    Drawable drawable = ContextCompat.getDrawable(mainActivity, resId);
                    ib_profileView.setImageDrawable(drawable);
                    DrawableUtils.personalizarImagenCircleButton(mainActivity, DrawableUtils.drawableToBitmap(drawable), ib_profileView, R.color.brownMaroon);
                    ib_profileView.setVisibility(View.VISIBLE);
                    EfectsUtils.startCircularReveal(ib_profileView.getDrawable(),ib_profileView);
                }

            } else {
                mainActivity.getFirebaseStorageUsers().getReference(UID).listAll().addOnSuccessListener(listResult -> {
                    for (StorageReference item : listResult.getItems()) {
                        if (item.getName().startsWith("profile")) {
                            item.getDownloadUrl().addOnSuccessListener(uri -> {
                                DrawableUtils.personalizarImagenCircleButton(mainActivity, uri, ib_profileView, R.color.brownMaroon);
                                ib_profileView.setVisibility(View.VISIBLE);
                                EfectsUtils.startCircularReveal(ib_profileView.getDrawable(),ib_profileView);
                            });
                            break;
                        }
                    }
                });
            }
            ib_profileView.setVisibility(View.VISIBLE);
            EfectsUtils.startCircularReveal(ib_profileView.getDrawable(),ib_profileView);
    }
    private void setRecyclerViewStuffkies() {
        stuffkiesUserPreviewAdapter = new StuffkiesUserPreviewAdapter(stuffkieArrayList,mainActivity,fragmentManager,mode);
        rc_stuffkiePreviewUserkie.setLayoutManager(new LinearLayoutManager(mainActivity, LinearLayoutManager.HORIZONTAL, false));
        rc_stuffkiePreviewUserkie.setAdapter(stuffkiesUserPreviewAdapter);
    }
    private void setRecyclerViewWorldkies() {
        worldkiesUserPreviewAdapter = new WorldkiesUserPreviewAdapter(worldkieArrayList,mainActivity,fragmentManager,mode);
        rc_worldkiePreviewUserkie.setLayoutManager(new LinearLayoutManager(mainActivity, LinearLayoutManager.HORIZONTAL, false));
        rc_worldkiePreviewUserkie.setAdapter(worldkiesUserPreviewAdapter);
    }
    private void setRecyclerViewCharacterkies() {
        characterkiesUserPreviewAdapter = new CharacterkiesUserPreviewAdapter(characterkieArrayList,mainActivity,fragmentManager,mode);
        rc_characterkiePreviewUserkie.setLayoutManager(new LinearLayoutManager(mainActivity, LinearLayoutManager.HORIZONTAL, false));
        rc_characterkiePreviewUserkie.setAdapter(characterkiesUserPreviewAdapter);
    }
    private void setRecyclerViewScenariokies() {
        scenariokiesUserPreviewAdapter = new ScenariokiesUserPreviewAdapter(scenariokieModelArrayList,mainActivity,fragmentManager,mode);
        rc_scenariokiePreviewUserkie.setLayoutManager(new LinearLayoutManager(mainActivity, LinearLayoutManager.HORIZONTAL, false));
        rc_scenariokiePreviewUserkie.setAdapter(scenariokiesUserPreviewAdapter);
    }
    @Override
    public void onClick(View v) {
        // Comprueba si se ha presionado el botón de retroceso.
        if(v.getId()==R.id.ib_back){
            NavigationUtils.goBack(fragmentManager,mainActivity);
        }
    }
}