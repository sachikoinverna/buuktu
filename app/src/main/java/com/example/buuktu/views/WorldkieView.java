package com.example.buuktu.views;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
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
import com.example.buuktu.models.CharacterkieModel;
import com.example.buuktu.models.ScenariokieModel;
import com.example.buuktu.models.StuffkieModel;
import com.example.buuktu.models.UserkieModel;
import com.example.buuktu.models.WorldkieModel;
import com.example.buuktu.utils.DrawableUtils;
import com.example.buuktu.utils.NavigationUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class WorldkieView extends Fragment implements View.OnClickListener {
    final ArrayList<CharacterkieModel> characterkieArrayList = new ArrayList<>();
    final ArrayList<StuffkieModel> stuffkieArrayList = new ArrayList<>();
    ArrayList<ScenariokieModel> scenariokieModelArrayList = new ArrayList<>();
    ImageButton ib_worldkieView,ib_back,ib_save;
    ImageView iv_locked_worldkie;
    TextView tv_nameWorldkieView,tv_nameUserWorldkieView,tv_usernameWorldkieView,tv_creationDateWorldkieView,tv_lastUpdateWorldkieView,tv_characterkiesPreviewWorldkie,tv_stuffkiesPreviewWorldkie,tv_locked_worldkie,tv_scenariokiesPreviewWorldkie;
    CardView cv_characterkiesPreviewWorldkie,cv_stuffkiesPreviewWorldkie,cv_scenariokiesPreviewWorldkie;
    RecyclerView rc_characterkiesPrevieWorldkie,rc_stuffkiesPreviewWorldkie,rc_scenariokiesPreviewWorldkie;
    FirebaseAuth firebaseAuth;
    String UID,UID_AUTHOR,mode;
    UserkieModel userkieModel;
    FragmentManager fragmentManager;
    CharacterkiesUserPreviewAdapter characterkiesUserPreviewAdapter;
    StuffkiesUserPreviewAdapter stuffkiesUserPreviewAdapter;
    WorldkieModel worldkieModel;
    MainActivity mainActivity;
    ScenariokiesUserPreviewAdapter scenariokiesUserPreviewAdapter;

    public WorldkieView() {}

    public static WorldkieView newInstance() {
        return new WorldkieView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mode = getArguments().getString("mode");
            UID = getArguments().getString("UID");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_worldkie_view, container, false);
        setVar();
        initComponents(view);
        setVisibility();
        ib_worldkieView.setVisibility(mode.equals("self")?View.VISIBLE:View.INVISIBLE);
        ib_back.setVisibility(mode.equals("self")?View.GONE:View.VISIBLE);
        UID_AUTHOR = mode.equals("other")?getArguments().getString("UID_AUTHOR"):firebaseAuth.getUid();


        getData();
        setListeners();
        return view;
    }
    private void getData(){
        getWorldkie();
        getAuthor();
        if((worldkieModel.isWorldkie_private()||userkieModel.isProfile_private()) && mode.equals("other")){
            tv_locked_worldkie.setVisibility(View.VISIBLE);
            iv_locked_worldkie.setVisibility(View.VISIBLE);
            tv_characterkiesPreviewWorldkie.setVisibility(View.GONE);
            cv_characterkiesPreviewWorldkie.setVisibility(View.GONE);
            tv_stuffkiesPreviewWorldkie.setVisibility(View.GONE);
            cv_stuffkiesPreviewWorldkie.setVisibility(View.GONE);
            tv_scenariokiesPreviewWorldkie.setVisibility(View.GONE);
            cv_scenariokiesPreviewWorldkie.setVisibility(View.GONE);
        }else{
            getCharacterkies();
            getStuffkies();
            getScenariokies();
        }
    }
    private void addDraftQuery(Query query){
        if (mode.equals("other")){
            query.whereNotEqualTo("draft", false);
        }
    }

    private void getScenariokies(){
        Query queryScenariokie = mainActivity.getCollectionScenariokies().whereEqualTo("WORDLKIE_UID", UID);
        addDraftQuery(queryScenariokie);
        queryScenariokie.addSnapshotListener((queryDocumentSnapshots, ex) -> {
            if (ex != null) return;

            if (queryDocumentSnapshots != null) {
                scenariokieModelArrayList.clear(); // Limpia la lista antes de agregar nuevos datos

                for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                    scenariokieModelArrayList.add(ScenariokieModel.fromSnapshot(doc));
                    setRecyclerViewScenariokies();
                }
            }else{
                tv_scenariokiesPreviewWorldkie.setVisibility(View.GONE);
                cv_scenariokiesPreviewWorldkie.setVisibility(View.GONE);
            }
        });
    }
    private void getWorldkie(){
        mainActivity.getCollectionWorldkies().document(UID).addSnapshotListener((documentSnapshot, e) -> {
            if (e != null) return;

            if (documentSnapshot != null) {
                worldkieModel = WorldkieModel.fromSnapshot(documentSnapshot);
                tv_nameWorldkieView.setText(worldkieModel.getName());
                tv_creationDateWorldkieView.setText(new SimpleDateFormat("dd/MM/yyyy").format(worldkieModel.getCreation_date().toDate()));
                tv_lastUpdateWorldkieView.setText(new SimpleDateFormat("dd/MM/yyyy").format(worldkieModel.getLast_update().toDate()));

            }
        });
    }
    private void getAuthor(){
        mainActivity.getCollectionUsers().document(UID_AUTHOR).addSnapshotListener((document, e) -> {
            if (e != null) return;

            if (document != null) {
                userkieModel = UserkieModel.fromSnapshot(document);
                tv_nameUserWorldkieView.setText(userkieModel.getName());
                tv_usernameWorldkieView.setText(userkieModel.getUsername());
            }
        });
    }
    private void getCharacterkies(){
        Query queryCharacterkie = mainActivity.getCollectionWorldkies().whereEqualTo("UID_WORLDKIE", UID);
        if (mode.equals("other")) {
            queryCharacterkie = queryCharacterkie.whereNotEqualTo("draft", false);
        }
        queryCharacterkie.addSnapshotListener((queryDocumentSnapshots, ex) -> {
            if (ex != null) return;

            if (queryDocumentSnapshots != null) {
                characterkieArrayList.clear();

                for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {

                    characterkieArrayList.add(CharacterkieModel.fromSnapshot(doc));
                    setRecyclerViewCharacterkies();
                }
            }else{
                tv_characterkiesPreviewWorldkie.setVisibility(View.GONE);
                cv_characterkiesPreviewWorldkie.setVisibility(View.GONE);
            }
        }
        );
    }


    private void getStuffkies() {
        Query query = mainActivity.getCollectionStuffkies().whereEqualTo("UID_WORLDKIE", UID);

        if (mode.equals("other") ){
            query = query.whereNotEqualTo("draft", false);
        }
        query.addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e != null) return;

            if (queryDocumentSnapshots != null) {
                stuffkieArrayList.clear();

                for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {


                    stuffkieArrayList.add(StuffkieModel.fromSnapshot(doc));
                    setRecyclerViewStuffkies();
                }
            }else{

                tv_stuffkiesPreviewWorldkie.setVisibility(View.GONE);
                cv_stuffkiesPreviewWorldkie.setVisibility(View.GONE);
            }
        });
    }
    private void setListeners(){
        ib_back.setOnClickListener(this);
        ib_worldkieView.setOnClickListener(this);
    }
    private void initComponents(View view){
        tv_scenariokiesPreviewWorldkie = view.findViewById(R.id.tv_scenariokiesPreviewWorldkie);
        cv_scenariokiesPreviewWorldkie = view.findViewById(R.id.cv_scenariokiesPreviewWorldkie);
        rc_scenariokiesPreviewWorldkie = view.findViewById(R.id.rc_scenariokiesPreviewWorldkie);
        ib_worldkieView = view.findViewById(R.id.ib_characterkieView);
        tv_locked_worldkie = view.findViewById(R.id.tv_locked_characterkie);
        iv_locked_worldkie = view.findViewById(R.id.iv_locked_characterkie);
        tv_nameWorldkieView = view.findViewById(R.id.tv_nameCharacterkieView);
        tv_nameUserWorldkieView = view.findViewById(R.id.tv_nameUserCharacterkieView);
        tv_usernameWorldkieView = view.findViewById(R.id.tv_usernameCharacterkieView);
        tv_creationDateWorldkieView = view.findViewById(R.id.tv_nameWorldkieViewCharacterkie);
        tv_lastUpdateWorldkieView = view.findViewById(R.id.tv_lastUpdateWorldkieView);
        cv_characterkiesPreviewWorldkie = view.findViewById(R.id.cv_characterkiesPreviewWorldkie);
        cv_stuffkiesPreviewWorldkie = view.findViewById(R.id.cv_stuffkiesPreviewWorldkie);
        rc_characterkiesPrevieWorldkie = view.findViewById(R.id.rc_characterkiesPrevieWorldkie);
        rc_stuffkiesPreviewWorldkie = view.findViewById(R.id.rc_stuffkiesPreviewWorldkie);
        tv_stuffkiesPreviewWorldkie = view.findViewById(R.id.tv_stuffkiesPreviewWorldkie);
        tv_characterkiesPreviewWorldkie = view.findViewById(R.id.tv_birthdayViewCharacterkie);
        mainActivity = (MainActivity)getActivity();
        ib_save = mainActivity.getIb_save();
        ib_back = mainActivity.getBackButton();
        fragmentManager = mainActivity.getSupportFragmentManager();
    }
    private void setVisibility(){
        ib_save.setVisibility(View.GONE);
        ib_back.setVisibility(View.VISIBLE);
    }
    private void setVar(){
        firebaseAuth = FirebaseAuth.getInstance();
    }
    private void getProfilePhoto() {
        if (worldkieModel.isPhoto_default()) {
            String id_photo = worldkieModel.getId_photo();
            int resId = mainActivity.getResources().getIdentifier(id_photo, "mipmap", mainActivity.getPackageName());

            if (resId != 0) {
                Drawable drawable = ContextCompat.getDrawable(mainActivity, resId);
                ib_worldkieView.setImageDrawable(drawable);
                try {
                    DrawableUtils.personalizarImagenCuadradoButton(mainActivity, 115 / 6, 7, R.color.brownMaroon, drawable, ib_worldkieView);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        } else {
            mainActivity.getFirebaseStorageWorldkies().getReference(UID).listAll().addOnSuccessListener(listResult -> {
                for (StorageReference item : listResult.getItems()) {
                    if (item.getName().startsWith("cover")) {
                        item.getDownloadUrl().addOnSuccessListener(uri -> {
                            // try {
                            DrawableUtils.personalizarImagenCuadradoButton(mainActivity, 115 / 7, 7, R.color.greenWhatever, uri, ib_worldkieView);
                            //} catch (IOException e) {
                            //    throw new RuntimeException(e);
                            // }
                        });
                    }
                }
            });
        }
    }
    private void setRecyclerViewCharacterkies() {
        characterkiesUserPreviewAdapter = new CharacterkiesUserPreviewAdapter(characterkieArrayList,mainActivity,fragmentManager,"self");
        rc_characterkiesPrevieWorldkie.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rc_characterkiesPrevieWorldkie.setAdapter(characterkiesUserPreviewAdapter);
    }
    private void setRecyclerViewStuffkies() {
        stuffkiesUserPreviewAdapter = new StuffkiesUserPreviewAdapter(stuffkieArrayList,mainActivity,fragmentManager,"self");
        rc_stuffkiesPreviewWorldkie.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rc_stuffkiesPreviewWorldkie.setAdapter(stuffkiesUserPreviewAdapter);
    }
    private void setRecyclerViewScenariokies() {
        scenariokiesUserPreviewAdapter = new ScenariokiesUserPreviewAdapter(scenariokieModelArrayList,mainActivity,fragmentManager,"self");
        rc_scenariokiesPreviewWorldkie.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rc_scenariokiesPreviewWorldkie.setAdapter(scenariokiesUserPreviewAdapter);
    }
    @Override
    public void onClick(View v) {
        // Comprueba si se ha presionado el botón de retroceso.
        if(v.getId()==R.id.ib_back){
            NavigationUtils.goBack(fragmentManager,mainActivity);
        } else if (v.getId() == R.id.ib_characterkieView) {
            mainActivity.showInfoDialog("future_function");
        }
    }
}