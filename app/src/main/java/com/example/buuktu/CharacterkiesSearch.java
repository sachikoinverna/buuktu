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

import com.example.buuktu.adapters.CharacterkieSearchAdapter;
import com.example.buuktu.models.CharacterkieModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CharacterkiesSearch#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CharacterkiesSearch extends Fragment {
    private ArrayList<CharacterkieModel> characterkieModelArrayList;
    CollectionReference collectionCharacterkies;
    private FirebaseFirestore db;
    FirebaseAuth firebaseAuth;
    String UID;
    RecyclerView rc_characterkies_search;
    CharacterkieSearchAdapter characterkieSearchAdapter;
    public CharacterkiesSearch() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
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
        firebaseAuth = FirebaseAuth.getInstance();
        UID = firebaseAuth.getUid();
        characterkieModelArrayList = new ArrayList<>();
        collectionCharacterkies = db.collection("Characterkies");
        characterkieSearchAdapter = new CharacterkieSearchAdapter(characterkieModelArrayList, getContext(), getParentFragmentManager());
        rc_characterkies_search.setAdapter(characterkieSearchAdapter);
        collectionCharacterkies.whereNotEqualTo("UID_AUTHOR",UID).whereEqualTo("draft",false).addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e != null) {
                Log.e("Error", e.getMessage());
                Toast.makeText(getContext(), "Error al escuchar cambios: " + e.getMessage(), LENGTH_LONG).show();
                return;
            }

            if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                //stuffkieModelArrayList.clear(); // Limpia la lista antes de agregar nuevos datos

                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                    DocumentSnapshot doc = dc.getDocument();
                    CharacterkieModel characterkie = CharacterkieModel.fromSnapshot(doc);
                        Log.d("StuffkiesSearch", "Stuffkie encontrado: " + doc.getString("name"));
                        switch (dc.getType()) {
                            case ADDED:
                                safeAddToList(characterkieModelArrayList, dc.getNewIndex(), characterkie);
                                characterkieSearchAdapter.notifyItemInserted(dc.getNewIndex());
                                break;

                            case MODIFIED:
                                safeSetToList(characterkieModelArrayList, dc.getOldIndex(), characterkie);
                                characterkieSearchAdapter.notifyItemChanged(dc.getOldIndex());
                                break;

                            case REMOVED:
                                if (dc.getOldIndex() >= 0 && dc.getOldIndex() < characterkieModelArrayList.size()) {
                                    characterkieModelArrayList.remove(dc.getOldIndex());
                                    characterkieSearchAdapter.notifyItemRemoved(dc.getOldIndex());
                                }
                                break;
                        }
                    }
                }

        });
        return view;
    }
    private void safeAddToList(ArrayList<CharacterkieModel> list, int index, CharacterkieModel item) {
        if (index >= 0 && index <= list.size()) {
            list.add(index, item);
        } else {
            list.add(item); // Fallback: añade al final
        }
    }

    private void safeSetToList(ArrayList<CharacterkieModel> list, int index, CharacterkieModel item) {
        if (index >= 0 && index < list.size()) {
            list.set(index, item);
        }
    }
    private void updateRecyclerView(ArrayList<CharacterkieModel> characterkieArrayList) {
        CharacterkieSearchAdapter characterkieSearchAdapter = new CharacterkieSearchAdapter(characterkieArrayList, getContext(), getParentFragmentManager());
        rc_characterkies_search.setAdapter(characterkieSearchAdapter);
        rc_characterkies_search.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}