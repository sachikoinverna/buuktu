package com.example.buuktu;

import static android.widget.Toast.LENGTH_LONG;

import android.graphics.drawable.Drawable;
import android.health.connect.datatypes.Vo2MaxRecord;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.buuktu.adapters.StuffkieSearchAdapter;
import com.example.buuktu.adapters.UserkieSearchAdapter;
import com.example.buuktu.models.StuffkieModel;
import com.example.buuktu.models.UserkieModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StuffkiesSearch#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StuffkiesSearch extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<StuffkieModel> stuffkieModelArrayList;
    CollectionReference collectionStuffkies;
    private FirebaseFirestore db;
    FirebaseAuth firebaseAuth;
    RecyclerView rc_stuffkies_search;

    public StuffkiesSearch() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StuffkiesSearch.
     */
    // TODO: Rename and change types and number of parameters
    public static StuffkiesSearch newInstance(String param1, String param2) {
        StuffkiesSearch fragment = new StuffkiesSearch();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stuffkies_search, container, false);
        rc_stuffkies_search = view.findViewById(R.id.rc_stuffkies_search);
        db = FirebaseFirestore.getInstance();
        stuffkieModelArrayList = new ArrayList<>();
        collectionStuffkies = db.collection("Stuffkies");
        collectionStuffkies.addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e != null) {
                Log.e("Error", e.getMessage());
                Toast.makeText(getContext(), "Error al escuchar cambios: " + e.getMessage(), LENGTH_LONG).show();
                return;
            }

            if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                stuffkieModelArrayList.clear(); // Limpia la lista antes de agregar nuevos datos

                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                    //if (documentSnapshot.getBoolean("photo_default")) {
                //    if (!documentSnapshot.getId().equals(firebaseAuth.getUid())) {

                        Drawable drawable = getResources().getDrawable(R.drawable.worldkie_default);
                        StuffkieModel stuffkieModel = new StuffkieModel(
                                documentSnapshot.getId(),
                                documentSnapshot.getString("name"),
                                Boolean.TRUE.equals(documentSnapshot.getBoolean("stuffkie_private")),
                                R.drawable.cloudlogin
                        );
                    Log.d("StuffkiesSearch", "Stuffkie encontrado: " + documentSnapshot.getString("name"));

                    stuffkieModelArrayList.add(stuffkieModel);
                        updateRecyclerView(stuffkieModelArrayList);
                    //}// Actualiza después de cargar cada imagen
                    //  } else {
                      /*  StorageReference storageRef = storage.getReference().child(documentSnapshot.getId());
                        final long ONE_MEGABYTE = 1024 * 1024;

                        storageRef.getBytes(ONE_MEGABYTE)
                                .addOnSuccessListener(bytes -> {*/
                                 /*   Bitmap bitmap = BitmapUtils.convertCompressedByteArrayToBitmap(bytes);
                                    Drawable drawable = new BitmapDrawable(getResources(), bitmap);

                                    WorldkieModel worldkieModel = new WorldkieModel(
                                            documentSnapshot.getId(),
                                            documentSnapshot.getString("name"),
                                            R.
                                            documentSnapshot.getString("username"),
                                            drawable,
                                            false,
                                            documentSnapshot.getBoolean("worldkie_private")
                                    );*/
                    //     worldkieModelArrayList.add(worldkieModel);
                    //    updateRecyclerView(worldkieModelArrayList); // Actualiza después de cargar cada imagen
                              /*  })
                                .addOnFailureListener(exception -> {
                                    Log.e("Error", "Error al cargar imagen: " + exception.getMessage());
                                });*/
                }
            }

            // updateRecyclerView(worldkieModelArrayList); // Actualiza el RecyclerView después de procesar todos los documentos
        }); /*else {
                // Si no hay documentos, limpia la lista y actualiza el RecyclerView
                worldkieModelArrayList.clear();
                updateRecyclerView(worldkieModelArrayList);
            }*/
        //});
        return view;
    }
    private void updateRecyclerView(ArrayList<StuffkieModel> stuffkieModelArrayList) {
        StuffkieSearchAdapter stuffkieSearchAdapter = new StuffkieSearchAdapter(stuffkieModelArrayList, getContext(), getParentFragmentManager());
        rc_stuffkies_search.setAdapter(stuffkieSearchAdapter);
        rc_stuffkies_search.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}