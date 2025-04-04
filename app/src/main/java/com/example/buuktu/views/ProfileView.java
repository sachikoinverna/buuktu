package com.example.buuktu.views;

import static android.widget.Toast.LENGTH_LONG;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buuktu.R;
import com.example.buuktu.StuffkiesUserPreviewAdapter;
import com.example.buuktu.adapters.CharacterkiesUserPreviewAdapter;
import com.example.buuktu.adapters.StuffkieSearchAdapter;
import com.example.buuktu.adapters.WorldkiesUserPreviewAdapter;
import com.example.buuktu.models.Characterkie;
import com.example.buuktu.models.StuffkieModel;
import com.example.buuktu.models.UserkieModel;
import com.example.buuktu.models.WorldkieModel;
import com.example.buuktu.utils.DrawableUtils;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ProfileView extends AppCompatActivity {
 private ImageButton ib_profileView;
 private TextView tv_usernameProfileView,tv_nameProfileView;
 ScrollView sv_pa;
 MaterialCardView cv_characterkiesPreviewUserSelf,cv_stuffkiesPreviewUserSelf,cv_worldkiesPreviewUserSelf;
 ArrayList<Characterkie> characterkieArrayList;
    ArrayList<StuffkieModel> stuffkieArrayList;
    ArrayList<WorldkieModel> worldkieArrayList;
FirebaseFirestore db;
RecyclerView rc_characterkiePreviewUserSelf,rc_stuffkiePreviewUserSelf,rc_worldkiePreviewUserSelf;
 CharacterkiesUserPreviewAdapter characterkiesUserPreviewAdapter;
 StuffkiesUserPreviewAdapter stuffkiesUserPreviewAdapter;
 WorldkiesUserPreviewAdapter worldkiesUserPreviewAdapter;
 CollectionReference collectionUserkies;
 CollectionReference collectionWorldkies;
 CollectionReference collectionStuffkies;
    CollectionReference collectionCharacterkies;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        rc_worldkiePreviewUserSelf = findViewById(R.id.rc_worldkiePreviewUserSelf);
        rc_stuffkiePreviewUserSelf = findViewById(R.id.rc_stuffkiePreviewUserSelf);
        rc_characterkiePreviewUserSelf = findViewById(R.id.rc_characterkiePreviewUserSelf);
        cv_worldkiesPreviewUserSelf = findViewById(R.id.cv_worldkiesPreviewUserSelf);
        cv_stuffkiesPreviewUserSelf = findViewById(R.id.cv_stuffkiesPreviewUserSelf);
        cv_characterkiesPreviewUserSelf = findViewById(R.id.cv_characterkiesPreviewUserSelf);
        ib_profileView = findViewById(R.id.ib_profileView);
        tv_usernameProfileView = findViewById(R.id.tv_usernameProfileView);
        tv_nameProfileView = findViewById(R.id.tv_nameProfileView);
        /*Bitmap bitmap = BitmapUtils.drawableToBitmap(getDrawable(R.drawable.worldkie_default));
        Bitmap bitmap1 = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
        ib_profileView.setImageBitmap(bitmap1);
        personalizarImagen(bitmap1);*/
        DrawableUtils.personalizarImagenCircleButton(this, DrawableUtils.drawableToBitmap(ib_profileView.getDrawable()), ib_profileView, R.color.brownBrown);
        ib_profileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        characterkieArrayList = new ArrayList<>();
        characterkieArrayList.add(new Characterkie("232532", "lALA"));
        characterkieArrayList.add(new Characterkie("232532", "lALSA"));
        characterkieArrayList.add(new Characterkie("232532", "lALSFA"));
        characterkieArrayList.add(new Characterkie("232532", "lAFLSA"));
        characterkieArrayList.add(new Characterkie("232532", "lASLAF"));
        characterkiesUserPreviewAdapter = new CharacterkiesUserPreviewAdapter(characterkieArrayList, this);
        rc_characterkiePreviewUserSelf.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rc_characterkiePreviewUserSelf.setAdapter(characterkiesUserPreviewAdapter);
        worldkieArrayList = new ArrayList<>();
        characterkieArrayList.add(new Characterkie("232532", "lALA"));
        characterkiesUserPreviewAdapter = new CharacterkiesUserPreviewAdapter(characterkieArrayList, this);
        rc_characterkiePreviewUserSelf.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rc_characterkiePreviewUserSelf.setAdapter(characterkiesUserPreviewAdapter);

        stuffkieArrayList = new ArrayList<>();
        characterkieArrayList.add(new Characterkie("232532", "lALA"));

        collectionUserkies = db.collection("Users");
        collectionStuffkies = db.collection("Stuffkies");
        collectionCharacterkies = db.collection("Characterkies");
        collectionWorldkies = db.collection("Worldkies");
        collectionUserkies.document(firebaseAuth.getUid()).addSnapshotListener((documentSnapshot, e) -> {
            if (e != null) {
                Log.e("Error", e.getMessage());
                Toast.makeText(this, "Error al escuchar cambios: " + e.getMessage(), LENGTH_LONG).show();
                return;
            }

            if (documentSnapshot != null) {
                UserkieModel userkieModel = new UserkieModel(firebaseAuth.getUid(),documentSnapshot.getString("name"),R.drawable.add_button,documentSnapshot.getString("username"),true,true);
                tv_nameProfileView.setText(userkieModel.getName());
                tv_usernameProfileView.setText(userkieModel.getUsername());
            }
        });
        collectionStuffkies.addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e != null) {
                Log.e("Error", e.getMessage());
                Toast.makeText(this, "Error al escuchar cambios: " + e.getMessage(), LENGTH_LONG).show();
                return;
            }

            if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                stuffkieArrayList.clear(); // Limpia la lista antes de agregar nuevos datos

                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                    //if (documentSnapshot.getBoolean("photo_default")) {
                    if (!documentSnapshot.getId().equals(firebaseAuth.getUid())) {

                        Drawable drawable = getResources().getDrawable(R.drawable.worldkie_default);
                        StuffkieModel stuffkieModel = new StuffkieModel(
                                documentSnapshot.getId(),
                                documentSnapshot.getString("name"),
                                Boolean.TRUE.equals(documentSnapshot.getBoolean("stuffkie_private")),
                                R.drawable.cloudlogin
                        );
                        Log.d("StuffkiesSearch", "Stuffkie encontrado: " + documentSnapshot.getString("name"));

                        stuffkieArrayList.add(stuffkieModel);
                        updateRecyclerViewStuffkies(stuffkieArrayList);
                    }
                }
            }
        });
        collectionWorldkies.addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e != null) {
                Log.e("Error", e.getMessage());
                Toast.makeText(this, "Error al escuchar cambios: " + e.getMessage(), LENGTH_LONG).show();
                return;
            }

            if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                worldkieArrayList.clear(); // Limpia la lista antes de agregar nuevos datos

                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                    //if (documentSnapshot.getBoolean("photo_default")) {
                    if (!documentSnapshot.getId().equals(firebaseAuth.getUid())) {

                        Drawable drawable = getResources().getDrawable(R.drawable.worldkie_default);
                        WorldkieModel worldkieModel = new WorldkieModel(
                                documentSnapshot.getId(),
                                documentSnapshot.getString("UID_AUTHOR"),
                                R.drawable.cloudlogin,
                                documentSnapshot.getString("name"), documentSnapshot.getDate("creation_date"),
                                true, documentSnapshot.getDate("last_update"), documentSnapshot.getBoolean("worldkie_private")
                        );
                    Log.d("StuffkiesSearch", "Stuffkie encontrado: " + documentSnapshot.getString("name"));

                    worldkieArrayList.add(worldkieModel);
                    updateRecyclerViewWorldkies(worldkieArrayList);
                    }
                }
            }
        });
        collectionCharacterkies.addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e != null) {
                Log.e("Error", e.getMessage());
                Toast.makeText(this, "Error al escuchar cambios: " + e.getMessage(), LENGTH_LONG).show();
                return;
            }

            if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                characterkieArrayList.clear(); // Limpia la lista antes de agregar nuevos datos

                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                    //if (documentSnapshot.getBoolean("photo_default")) {
                    if (!documentSnapshot.getId().equals(firebaseAuth.getUid())) {

                    Drawable drawable = getResources().getDrawable(R.drawable.worldkie_default);
                    Characterkie characterkieModel = new Characterkie(
                            documentSnapshot.getId(),
                            documentSnapshot.getString("name")
                    );
                    Log.d("StuffkiesSearch", "Stuffkie encontrado: " + documentSnapshot.getString("name"));

                    characterkieArrayList.add(characterkieModel);
                    updateRecyclerViewCharacterkies(characterkieArrayList);
                    }
                }
            }
        });
        }
    private void updateRecyclerViewStuffkies(ArrayList<StuffkieModel> stuffkieArrayList) {
                    stuffkiesUserPreviewAdapter = new StuffkiesUserPreviewAdapter(stuffkieArrayList,this);
                    rc_stuffkiePreviewUserSelf.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                    rc_stuffkiePreviewUserSelf.setAdapter(stuffkiesUserPreviewAdapter);
    }
    private void updateRecyclerViewWorldkies(ArrayList<WorldkieModel> worldkieArrayList) {
        worldkiesUserPreviewAdapter = new WorldkiesUserPreviewAdapter(worldkieArrayList,this);
        rc_worldkiePreviewUserSelf.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rc_worldkiePreviewUserSelf.setAdapter(worldkiesUserPreviewAdapter);
    }
    private void updateRecyclerViewCharacterkies(ArrayList<Characterkie> characterkieArrayList) {
        characterkiesUserPreviewAdapter = new CharacterkiesUserPreviewAdapter(characterkieArrayList,this);
        rc_characterkiePreviewUserSelf.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rc_characterkiePreviewUserSelf.setAdapter(characterkiesUserPreviewAdapter);
    }
}