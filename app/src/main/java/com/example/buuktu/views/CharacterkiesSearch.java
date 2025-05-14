package com.example.buuktu.views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.buuktu.R;
import com.example.buuktu.adapters.CharacterkieSearchAdapter;
import com.example.buuktu.models.CharacterkieModel;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;


public class CharacterkiesSearch extends Fragment {
    private ArrayList<CharacterkieModel> characterkieModelArrayList;
    private String UID;
    private RecyclerView rc_characterkies_search;
    private CharacterkieSearchAdapter characterkieSearchAdapter;
    private MainActivity mainActivity;
    public CharacterkiesSearch() {
    }


    public static CharacterkiesSearch newInstance() {
        return new CharacterkiesSearch();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_characterkies_search, container, false);
        rc_characterkies_search = view.findViewById(R.id.rc_characterkies_search);
        mainActivity = (MainActivity) getActivity();
        UID = mainActivity.getUID();
        characterkieModelArrayList = new ArrayList<>();

        characterkieSearchAdapter = new CharacterkieSearchAdapter(characterkieModelArrayList, mainActivity, getParentFragmentManager());
        rc_characterkies_search.setAdapter(characterkieSearchAdapter);
        mainActivity.getCollectionCharacterkies().whereNotEqualTo("UID_AUTHOR",UID).whereEqualTo("draft",false).addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e != null) {
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
        CharacterkieSearchAdapter characterkieSearchAdapter = new CharacterkieSearchAdapter(characterkieArrayList, mainActivity, getParentFragmentManager());
        rc_characterkies_search.setAdapter(characterkieSearchAdapter);
        rc_characterkies_search.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}