package com.example.buuktu;

import static android.widget.Toast.LENGTH_LONG;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.buuktu.adapters.CharacterkieSearchAdapter;
import com.example.buuktu.models.Characterkie;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CharacterkiesSearch#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CharacterkiesSearch extends Fragment {
    private ArrayList<Characterkie> characterkieModelArrayList;
    CollectionReference collectionCharacterkies;
    private FirebaseFirestore db;
    FirebaseAuth firebaseAuth;
    RecyclerView rc_characterkies_search;
    static SearchView searchView;
    public CharacterkiesSearch() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CharacterkiesSearch.
     */
    // TODO: Rename and change types and number of parameters
    public static CharacterkiesSearch newInstance(String param1, String param2) {
        return new CharacterkiesSearch();
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
        View view = inflater.inflate(R.layout.fragment_characterkies_search, container, false);
        rc_characterkies_search = view.findViewById(R.id.rc_characterkies_search);
        db = FirebaseFirestore.getInstance();
        characterkieModelArrayList = new ArrayList<>();
        collectionCharacterkies = db.collection("Characterkies");
        collectionCharacterkies.addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e != null) {
                Log.e("Error", e.getMessage());
                Toast.makeText(getContext(), "Error al escuchar cambios: " + e.getMessage(), LENGTH_LONG).show();
                return;
            }

            if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                characterkieModelArrayList.clear(); // Limpia la lista antes de agregar nuevos datos

                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                    //if (documentSnapshot.getBoolean("photo_default")) {
                    //    if (!documentSnapshot.getId().equals(firebaseAuth.getUid())) {

                    Drawable drawable = getResources().getDrawable(R.drawable.worldkie_default);
                    /*StuffkieModel stuffkieModel = new StuffkieModel(
                            documentSnapshot.getId(),
                            documentSnapshot.getString("name"),
                            Boolean.TRUE.equals(documentSnapshot.getBoolean("stuffkie_private")),
                            R.drawable.cloudlogin
                    );*/
                    Characterkie characterkie = new Characterkie(documentSnapshot.getId(),documentSnapshot.getString("name"));
                    Log.d("StuffkiesSearch", "Stuffkie encontrado: " + documentSnapshot.getString("name"));

                    characterkieModelArrayList.add(characterkie);
                    updateRecyclerView(characterkieModelArrayList);
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
    private void updateRecyclerView(ArrayList<Characterkie> characterkieArrayList) {
        CharacterkieSearchAdapter characterkieSearchAdapter = new CharacterkieSearchAdapter(characterkieArrayList, getContext(), getParentFragmentManager());
        rc_characterkies_search.setAdapter(characterkieSearchAdapter);
        rc_characterkies_search.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}