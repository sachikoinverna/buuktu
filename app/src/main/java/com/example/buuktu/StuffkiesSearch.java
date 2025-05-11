package com.example.buuktu;

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

import com.example.buuktu.adapters.StuffkieSearchAdapter;
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
    private final ArrayList<StuffkieModel> filteredDataSet = new ArrayList<>();

    private ArrayList<StuffkieModel> stuffkieModelArrayList;
    CollectionReference collectionStuffkies;
    private FirebaseFirestore db;
    FirebaseAuth firebaseAuth;
    String UID;
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
        initComponents(view);
        db = FirebaseFirestore.getInstance();
        stuffkieModelArrayList = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
        UID = firebaseAuth.getUid();
        collectionStuffkies = db.collection("Stuffkies");
        stuffkieSearchAdapter = new StuffkieSearchAdapter(stuffkieModelArrayList, getContext(), getParentFragmentManager());
        rc_stuffkies_search.setAdapter(stuffkieSearchAdapter);
        rc_stuffkies_search.setLayoutManager(new LinearLayoutManager(getContext()));
        collectionStuffkies.whereNotEqualTo("UID_AUTHOR",UID).whereEqualTo("draft",false).addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e != null) {
                Log.e("Error", e.getMessage());
                Toast.makeText(getContext(), "Error al escuchar cambios: " + e.getMessage(), LENGTH_LONG).show();
                return;
            }

            if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                //stuffkieModelArrayList.clear(); // Limpia la lista antes de agregar nuevos datos

                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                    DocumentSnapshot doc = dc.getDocument();                    //if (documentSnapshot.getBoolean("photo_default")) {

                        StuffkieModel stuffkieModel = StuffkieModel.fromSnapshot(doc);
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
                    }
                }
        });
        return view;
    }
    private void initComponents(View view){
        rc_stuffkies_search = view.findViewById(R.id.rc_stuffkies_search);

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