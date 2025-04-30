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

import com.example.buuktu.adapters.WorldkieSearchAdapter;
import com.example.buuktu.models.WorldkieModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WorldkiesSearch#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorldkiesSearch extends Fragment {

    RecyclerView rc_worldkies_search;
    private FirebaseFirestore db;
    private ArrayList<WorldkieModel> worldkieModelArrayList;
    CollectionReference collectionWorldkies;
    WorldkieSearchAdapter worldkieSearchAdapter;
    FirebaseAuth firebaseAuth;
    String UID;
    public WorldkiesSearch() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment WorldkiesSearch.
     */
    // TODO: Rename and change types and number of parameters
    public static WorldkiesSearch newInstance() {
        return new WorldkiesSearch();
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
        View view = inflater.inflate(R.layout.fragment_worldkies_search, container, false);
        rc_worldkies_search = view.findViewById(R.id.rc_worldkies_search);
        db = FirebaseFirestore.getInstance();
        worldkieModelArrayList = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
        UID = firebaseAuth.getUid();
        collectionWorldkies = db.collection("Worldkies");
        worldkieSearchAdapter = new WorldkieSearchAdapter(worldkieModelArrayList, getContext(), getParentFragmentManager());
        rc_worldkies_search.setAdapter(worldkieSearchAdapter);
        rc_worldkies_search.setLayoutManager(new LinearLayoutManager(getContext()));
        collectionWorldkies.whereNotEqualTo("UID_AUTHOR",UID).whereEqualTo("draft",false).addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e != null) {
                Log.e("Error", e.getMessage());
                Toast.makeText(getContext(), "Error al escuchar cambios: " + e.getMessage(), LENGTH_LONG).show();
                return;
            }

            if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
               // worldkieModelArrayList.clear(); // Limpia la lista antes de agregar nuevos datos

                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                    DocumentSnapshot doc = dc.getDocument();
                        WorldkieModel worldkieModel = new WorldkieModel(
                                doc.getId(),
                                doc.getString("UID_AUTHOR"), doc.getTimestamp("creation_date"),
                                doc.getString("name"),
                                doc.getBoolean("photo_default"),doc.getBoolean("worldkie_private"),doc.getTimestamp("last_update")
                        );
                        switch (dc.getType()) {
                            case ADDED:
                                    safeAddToList(worldkieModelArrayList, dc.getNewIndex(), worldkieModel);
                                    worldkieSearchAdapter.notifyItemInserted(dc.getNewIndex());
                                break;

                            case MODIFIED:

                                    if (dc.getOldIndex() >= 0 && dc.getOldIndex() < worldkieModelArrayList.size()) {
                                        worldkieModelArrayList.remove(dc.getOldIndex());
                                        worldkieSearchAdapter.notifyItemRemoved(dc.getOldIndex());
                                }
                                break;

                            case REMOVED:
                                if (dc.getOldIndex() >= 0 && dc.getOldIndex() < worldkieModelArrayList.size()) {
                                    worldkieModelArrayList.remove(dc.getOldIndex());
                                    worldkieSearchAdapter.notifyItemRemoved(dc.getOldIndex());
                                }
                                break;
                        }
                    }
                    //worldkieModelArrayList.add(worldkieModel);
                    //updateRecyclerView(worldkieModelArrayList); // Actualiza después de cargar cada imagen

                }
        });
        return view;
    }
    private void safeAddToList(ArrayList<WorldkieModel> list, int index, WorldkieModel item) {
        if (index >= 0 && index <= list.size()) {
            list.add(index, item);
        } else {
            list.add(item); // Fallback: añade al final
        }
    }

    private void safeSetToList(ArrayList<WorldkieModel> list, int index, WorldkieModel item) {
        if (index >= 0 && index < list.size()) {
            list.set(index, item);
        }
    }
                private void updateRecyclerView (ArrayList < WorldkieModel > worldkieModelArrayList)
                {
                    WorldkieSearchAdapter worldkieSearchAdapter = new WorldkieSearchAdapter(worldkieModelArrayList, getContext(), getParentFragmentManager());
                    rc_worldkies_search.setAdapter(worldkieSearchAdapter);
                    rc_worldkies_search.setLayoutManager(new LinearLayoutManager(getContext()));
                }
            }