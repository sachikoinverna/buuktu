package com.example.buuktu.views;

import static android.widget.Toast.LENGTH_LONG;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.buuktu.R;
import com.example.buuktu.adapters.WorldkieAdapter;
import com.example.buuktu.controllers.HomeController;
import com.example.buuktu.models.UserModel;
import com.example.buuktu.models.WorldkieModel;
import com.example.buuktu.utils.BitmapUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment {
    private String UID;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance("gs://buuk-tu-worldkies");
    private ArrayList<WorldkieModel> worldkieModelArrayList = new ArrayList<>();
    private RecyclerView rc_worldkies;
    private FloatingActionButton fb_parent, fb_add;
    private boolean isAllFabsVisible;
    private HomeController homeController;
    public Home() {
        // Required empty public constructor
    }

    public static Home newInstance() {
        return new Home();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Inicializa las vistas
        fb_parent = view.findViewById(R.id.fb_parentWorldkies);
        fb_add = view.findViewById(R.id.fb_addWorldkie);
        fb_add.setVisibility(View.GONE);
        isAllFabsVisible = false;
        homeController = new HomeController(this);
        fb_add.setOnClickListener(homeController);
        fb_parent.setOnClickListener(homeController);

        rc_worldkies = view.findViewById(R.id.rc_worldkies);

        db = FirebaseFirestore.getInstance();
        UID = auth.getCurrentUser().getUid();

        Toast.makeText(getContext(), UID, Toast.LENGTH_SHORT).show();
        worldkieModelArrayList = new ArrayList<>();

        CollectionReference dbWorldkies = db.collection("Worldkies");
        /*dbWorldkies.whereEqualTo("UID_AUTHOR", UID)
                .orderBy("last_update")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        final int totalDocuments = queryDocumentSnapshots.size();

                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                            if (documentSnapshot.getBoolean("photo_default")) {
                                Drawable drawable = getResources().getDrawable(R.drawable.worldkie_default);
                                WorldkieModel worldkieModel = new WorldkieModel(
                                        documentSnapshot.getId(), documentSnapshot.getString("UID_AUTHOR"),
                                        documentSnapshot.getString("name"),
                                        drawable, true
                                );
                                worldkieModelArrayList.add(worldkieModel);
                                updateRecyclerView(worldkieModelArrayList); // Call after processing each document (default image)
                            } else {
                                StorageReference storageRef = storage.getReference().child(documentSnapshot.getId());
                                final long ONE_MEGABYTE = 1024 * 1024;

                                storageRef.getBytes(ONE_MEGABYTE)
                                        .addOnSuccessListener(bytes -> {
                                            Bitmap bitmap = BitmapUtils.convertCompressedByteArrayToBitmap(bytes);
                                            Drawable drawable = new BitmapDrawable(getResources(), bitmap);

                                            WorldkieModel worldkieModel = new WorldkieModel(
                                                    documentSnapshot.getId(),
                                                    documentSnapshot.getString("UID_AUTHOR"),
                                                    documentSnapshot.getString("name"),
                                                    drawable, false
                                            );
                                            worldkieModelArrayList.add(worldkieModel);
                                            updateRecyclerView(worldkieModelArrayList); // Call after processing each document (custom image)
                                        })
                                        .addOnFailureListener(exception ->
                                                Toast.makeText(getContext(), "Error al cargar imagen", Toast.LENGTH_SHORT).show()
                                        );
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Error", e.getMessage().toString());
                        Toast.makeText(getContext(), e.getMessage().toString(), LENGTH_LONG).show();
                    }
                });*/
        dbWorldkies.whereEqualTo("UID_AUTHOR", UID)
                .orderBy("last_update")
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        Log.e("Error", e.getMessage());
                        Toast.makeText(getContext(), "Error al escuchar cambios: " + e.getMessage(), LENGTH_LONG).show();
                        return;
                    }

                    if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                        worldkieModelArrayList.clear(); // Limpia la lista antes de agregar nuevos datos

                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                            if (documentSnapshot.getBoolean("photo_default")) {
                                Drawable drawable = getResources().getDrawable(R.drawable.worldkie_default);
                                WorldkieModel worldkieModel = new WorldkieModel(
                                        documentSnapshot.getId(),
                                        documentSnapshot.getString("UID_AUTHOR"),
                                        documentSnapshot.getString("name"),
                                        drawable,
                                        true,documentSnapshot.getBoolean("worldkie_private")
                                );
                                worldkieModelArrayList.add(worldkieModel);
                            } else {
                                StorageReference storageRef = storage.getReference().child(documentSnapshot.getId());
                                final long ONE_MEGABYTE = 1024 * 1024;

                                storageRef.getBytes(ONE_MEGABYTE)
                                        .addOnSuccessListener(bytes -> {
                                            Bitmap bitmap = BitmapUtils.convertCompressedByteArrayToBitmap(bytes);
                                            Drawable drawable = new BitmapDrawable(getResources(), bitmap);

                                            WorldkieModel worldkieModel = new WorldkieModel(
                                                    documentSnapshot.getId(),
                                                    documentSnapshot.getString("UID_AUTHOR"),
                                                    documentSnapshot.getString("name"),
                                                    drawable,
                                                    false,
                                                    documentSnapshot.getBoolean("worldkie_private")
                                            );
                                            worldkieModelArrayList.add(worldkieModel);
                                            updateRecyclerView(worldkieModelArrayList); // Actualiza después de cargar cada imagen
                                        })
                                        .addOnFailureListener(exception -> {
                                            Log.e("Error", "Error al cargar imagen: " + exception.getMessage());
                                        });
                            }
                        }

                        updateRecyclerView(worldkieModelArrayList); // Actualiza el RecyclerView después de procesar todos los documentos
                    } else {
                        // Si no hay documentos, limpia la lista y actualiza el RecyclerView
                        worldkieModelArrayList.clear();
                        updateRecyclerView(worldkieModelArrayList);
                    }
                });
        return view;
    }


    public FloatingActionButton getFb_parent() {
        return fb_parent;
    }

    public FloatingActionButton getFb_add() {
        return fb_add;
    }
    public boolean isAllFabsVisible(){
        return isAllFabsVisible;
    }

    public void setAllFabsVisible(boolean allFabsVisible) {
        isAllFabsVisible = allFabsVisible;
    }

    // Helper method to update the RecyclerView
        private void updateRecyclerView(ArrayList<WorldkieModel> worldkieModelArrayList) {
            WorldkieAdapter worldkieAdapter = new WorldkieAdapter(worldkieModelArrayList, getContext());
            rc_worldkies.setAdapter(worldkieAdapter);
            rc_worldkies.setLayoutManager(new LinearLayoutManager(getContext()));
        }

}