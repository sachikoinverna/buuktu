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

import com.example.buuktu.adapters.SettingsAdapter;
import com.example.buuktu.adapters.StuffkieSearchAdapter;
import com.example.buuktu.models.SettingModel;
import com.example.buuktu.models.StuffkieModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StuffkiesSearch#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StuffkiesSearch extends Fragment {
    private ArrayList<StuffkieModel> filteredDataSet = new ArrayList<StuffkieModel>();

    private ArrayList<StuffkieModel> stuffkieModelArrayList;
    CollectionReference collectionStuffkies;
    private FirebaseFirestore db;
    FirebaseAuth firebaseAuth;
    RecyclerView rc_stuffkies_search;
    StuffkieSearchAdapter stuffkieSearchAdapter;
    public StuffkiesSearch() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment StuffkiesSearch.
     */
    // TODO: Rename and change types and number of parameters
    public static StuffkiesSearch newInstance() {
        return new StuffkiesSearch();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stuffkies_search, container, false);
        rc_stuffkies_search = view.findViewById(R.id.rc_stuffkies_search);
        db = FirebaseFirestore.getInstance();
        stuffkieModelArrayList = new ArrayList<>();
        collectionStuffkies = db.collection("Stuffkies");
        stuffkieSearchAdapter = new StuffkieSearchAdapter(stuffkieModelArrayList, getContext(), getParentFragmentManager());
        rc_stuffkies_search.setAdapter(stuffkieSearchAdapter);
        rc_stuffkies_search.setLayoutManager(new LinearLayoutManager(getContext()));
        collectionStuffkies.addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e != null) {
                Log.e("Error", e.getMessage());
                Toast.makeText(getContext(), "Error al escuchar cambios: " + e.getMessage(), LENGTH_LONG).show();
                return;
            }

            if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                //stuffkieModelArrayList.clear(); // Limpia la lista antes de agregar nuevos datos

                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                    DocumentSnapshot doc = dc.getDocument();                    //if (documentSnapshot.getBoolean("photo_default")) {
                //    if (!documentSnapshot.getId().equals(firebaseAuth.getUid())) {

                        Drawable drawable = getResources().getDrawable(R.drawable.worldkie_default);
                        StuffkieModel stuffkieModel = new StuffkieModel(
                                doc.getId(),
                                doc.getString("name"),
                                Boolean.TRUE.equals(doc.getBoolean("stuffkie_private")),
                                R.drawable.cloudlogin
                        );
                    switch (dc.getType()) {
                        case ADDED:
                            safeAddToList(stuffkieModelArrayList, dc.getNewIndex(), stuffkieModel);
                            stuffkieSearchAdapter.notifyItemInserted(dc.getNewIndex());
                            break;

                        case MODIFIED:
                            safeSetToList(stuffkieModelArrayList, dc.getOldIndex(), stuffkieModel);
                            stuffkieSearchAdapter.notifyItemChanged(dc.getOldIndex());
                            break;

                        case REMOVED:
                            if (dc.getOldIndex() >= 0 && dc.getOldIndex() < stuffkieModelArrayList.size()) {
                                stuffkieModelArrayList.remove(dc.getOldIndex());
                                stuffkieSearchAdapter.notifyItemRemoved(dc.getOldIndex());
                            }
                            break;
                    }
                  //  stuffkieModelArrayList.add(stuffkieModel);
                  //      updateRecyclerView(stuffkieModelArrayList);
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
       /* searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterList(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });*/
        return view;
    }
    private void filterList(String query) {
        filteredDataSet.clear();

        // Filtrar los elementos de la lista
        if (query.isEmpty()) {
            // Si el campo de búsqueda está vacío, muestra todos los elementos
            filteredDataSet.addAll(stuffkieModelArrayList);
        } else {
            // Filtrar los elementos que contienen el texto de la búsqueda
            for (StuffkieModel item : stuffkieModelArrayList) {
                if (item.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredDataSet.add(item);

                }
            }
        }
        updateRecyclerView(filteredDataSet);
        // Notificar al adaptador que los datos han cambiado
        stuffkieSearchAdapter.notifyDataSetChanged();
    }
    private void updateRecyclerView(ArrayList<StuffkieModel> settingModels){
        stuffkieSearchAdapter = new StuffkieSearchAdapter(settingModels,getContext(),getParentFragmentManager());
        rc_stuffkies_search.setAdapter(stuffkieSearchAdapter);
        rc_stuffkies_search.setLayoutManager(new LinearLayoutManager(getContext()));
    }
    private void safeAddToList(ArrayList<StuffkieModel> list, int index, StuffkieModel item) {
        if (index >= 0 && index <= list.size()) {
            list.add(index, item);
        } else {
            list.add(item); // Fallback: añade al final
        }
    }

    private void safeSetToList(ArrayList<StuffkieModel> list, int index, StuffkieModel item) {
        if (index >= 0 && index < list.size()) {
            list.set(index, item);
        }
    }
}