package com.example.buuktu;

import static android.widget.Toast.LENGTH_LONG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.graphics.drawable.Drawable;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.Toast;

import com.example.buuktu.adapters.UserkieSearchAdapter;
import com.example.buuktu.models.UserkieModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Currency;
import java.util.UUID;

public class UserkiesSearch extends Fragment {

    private RecyclerView rc_userkies_search;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private ArrayList<UserkieModel> userkieModelArrayList;
    private UserkieSearchAdapter userkieSearchAdapter;
    private String UID;
    public UserkiesSearch() {
        // Constructor vacío obligatorio
    }

    public static UserkiesSearch newInstance() {
        return new UserkiesSearch();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userkieModelArrayList = new ArrayList<>();
        userkieSearchAdapter = new UserkieSearchAdapter(userkieModelArrayList, getContext(), getParentFragmentManager());
        UID = firebaseAuth.getUid();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_userkies_search, container, false);

        rc_userkies_search = view.findViewById(R.id.rc_userkies_search);
        rc_userkies_search.setLayoutManager(new LinearLayoutManager(getContext()));
        rc_userkies_search.setAdapter(userkieSearchAdapter);

        loadUserkies();

        return view;
    }

    private void loadUserkies() {
        CollectionReference collectionUserkies = db.collection("Users");

        collectionUserkies.whereNotEqualTo(FieldPath.documentId(), UID).addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e != null) {
                Log.e("Firestore Error", e.getMessage());
                Toast.makeText(getContext(), "Error al cargar usuarios", LENGTH_LONG).show();
                return;
            }

            for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                DocumentSnapshot doc = dc.getDocument();
                 UserkieModel userkieModel;
                    if(doc.getBoolean("photo_default")) {
                        userkieModel = new UserkieModel(doc.getId(),doc.getString("name"), doc.getString("username"), doc.getBoolean("profile_private"), doc.getBoolean("photo_default"),doc.getString("photo_id"));
                    }else{
                        userkieModel = new UserkieModel(doc.getId(),doc.getString("name"), doc.getString("username"), doc.getBoolean("profile_private"), doc.getBoolean("photo_default"));

                    }
                    switch (dc.getType()) {
                        case ADDED:
                            safeAddToList(userkieModelArrayList, dc.getNewIndex(), userkieModel);
                            userkieSearchAdapter.notifyItemInserted(dc.getNewIndex());
                            break;
                        case MODIFIED:
                            safeSetToList(userkieModelArrayList, dc.getOldIndex(), userkieModel);
                            userkieSearchAdapter.notifyItemChanged(dc.getOldIndex());
                            break;
                        case REMOVED:
                            if (dc.getOldIndex() >= 0 && dc.getOldIndex() < userkieModelArrayList.size()) {
                                userkieModelArrayList.remove(dc.getOldIndex());
                                userkieSearchAdapter.notifyItemRemoved(dc.getOldIndex());
                            }
                            break;
                    }
            }
        });
    }

    private void safeAddToList(ArrayList<UserkieModel> list, int index, UserkieModel item) {
        if (index >= 0 && index <= list.size()) {
            list.add(index, item);
        } else {
            list.add(item); // Añade al final si hay error en el índice
        }
    }

    private void safeSetToList(ArrayList<UserkieModel> list, int index, UserkieModel item) {
        if (index >= 0 && index < list.size()) {
            list.set(index, item);
        }
    }
}
