package com.example.buuktu.views;

import static android.widget.Toast.LENGTH_LONG;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.buuktu.R;
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
    private ArrayList<WorldkieModel> worldkieModelArrayList;
    CollectionReference collectionWorldkies;
    WorldkieSearchAdapter worldkieSearchAdapter;
    MainActivity mainActivity;
    public WorldkiesSearch() {}

    public static WorldkiesSearch newInstance() {
        return new WorldkiesSearch();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_worldkies_search, container, false);
        rc_worldkies_search = view.findViewById(R.id.rc_worldkies_search);
        worldkieModelArrayList = new ArrayList<>();
        worldkieSearchAdapter = new WorldkieSearchAdapter(worldkieModelArrayList, getContext(), getParentFragmentManager());
        rc_worldkies_search.setAdapter(worldkieSearchAdapter);
        rc_worldkies_search.setLayoutManager(new LinearLayoutManager(getContext()));
        mainActivity = (MainActivity) requireActivity();
        mainActivity.getCollectionWorldkies().whereNotEqualTo("UID_AUTHOR",mainActivity.getUID()).whereEqualTo("draft",false).addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e != null) {
                Log.e("Error", e.getMessage());
                Toast.makeText(getContext(), "Error al escuchar cambios: " + e.getMessage(), LENGTH_LONG).show();
                return;
            }

            if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
               // worldkieModelArrayList.clear(); // Limpia la lista antes de agregar nuevos datos

                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                    DocumentSnapshot doc = dc.getDocument();
                        WorldkieModel worldkieModel = WorldkieModel.fromSnapshot(doc);
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