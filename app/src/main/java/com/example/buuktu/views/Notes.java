package com.example.buuktu.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.buuktu.R;
import com.example.buuktu.adapters.NoteAdapter;
import com.example.buuktu.models.NotekieModel;
import com.example.buuktu.utils.NavigationUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class Notes extends Fragment implements View.OnClickListener {

    private RecyclerView recyclerView;
    private NoteAdapter noteAdapter;
    private ArrayList<NotekieModel> items = new ArrayList<>();
    ImageButton ib_save,backButton,ib_profile_superior;
    private FloatingActionButton fbAddNote;
    FragmentManager fragmentManager;
    MainActivity mainActivity;
    public Notes() {}

    public static Notes newInstance() {
        return new Notes();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        initComponents(view);


        getNotekies();
        setRecyclerView();

        setListeners();
        // Escucha en Firestore

        return view;
    }
    private void initComponents(View view){
        mainActivity = (MainActivity) getActivity();
        backButton = mainActivity.getBackButton();
        ib_save = mainActivity.getIb_save();
        ib_profile_superior = mainActivity.getIb_self_profile();
        recyclerView = view.findViewById(R.id.rc_all_notes_adapter);
        fbAddNote = view.findViewById(R.id.fb_add_note_list_notes);
        fragmentManager = mainActivity.getSupportFragmentManager();
        setVisibility();
    }
    private void setVisibility(){
        backButton.setVisibility(View.VISIBLE);
        ib_save.setVisibility(View.GONE);
        ib_profile_superior.setVisibility(View.VISIBLE);
    }
    private void setListeners(){
        backButton.setOnClickListener(this);
        fbAddNote.setOnClickListener(this);
    }


    private void getNotekies() {
        mainActivity.getCollectionNotekies()
                .whereEqualTo("UID_USER", mainActivity.getUID())
                .orderBy("last_update", Query.Direction.DESCENDING)
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) return;
                    items.clear();

                            if (queryDocumentSnapshots != null) {
                                for (DocumentSnapshot doc : queryDocumentSnapshots) {
                                    items.add(NotekieModel.fromSnapshot(doc));
                                }
                            }
                            noteAdapter.notifyDataSetChanged();
                });
    }

    private void setRecyclerView() {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        noteAdapter = new NoteAdapter(mainActivity, items, item -> {
            Bundle bundle = new Bundle();
            bundle.putString("note_id", item.getUID());
            NavigationUtils.goNewFragmentWithBundle(bundle,fragmentManager,new Note());

        });
        recyclerView.setAdapter(noteAdapter);
    }
    @Override
    public void onClick(View v) {
        // Comprueba si se ha presionado el botón de retroceso.
        if(v.getId()==R.id.ib_back){
            NavigationUtils.goBack(fragmentManager,mainActivity);
        } else if (v.getId()==R.id.fb_add_note_list_notes) {
            NavigationUtils.goNewFragment(fragmentManager,new Note());
        }
    }
}
