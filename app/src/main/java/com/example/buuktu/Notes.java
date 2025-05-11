package com.example.buuktu;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.example.buuktu.adapters.NoteAdapter;
import com.example.buuktu.models.NotekieModel;
import com.example.buuktu.utils.NavigationUtils;
import com.example.buuktu.views.MainActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class Notes extends Fragment implements View.OnClickListener {

    private RecyclerView recyclerView;
    private NoteAdapter noteAdapter;
    private ArrayList<NotekieModel> items;
    private FirebaseFirestore db;
    private CollectionReference collectionNotekies;
    private String UID;
    ImageButton ib_save,backButton,ib_profile_superior;
    private FloatingActionButton fbMoreOptions, fbAddNote;
    private boolean isAllFabsVisible = false;
    FragmentManager fragmentManager;
    MainActivity mainActivity;
    public Notes() {}

    public static Notes newInstance(String param1, String param2) {
        return new Notes();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        initComponents(view);


        db = FirebaseFirestore.getInstance();
        UID = FirebaseAuth.getInstance().getUid();
        collectionNotekies = db.collection("Notekies");
        items = new ArrayList<>();

        // Layout manager
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        // Adaptador
        noteAdapter = new NoteAdapter(requireContext(), items, item -> {
            Bundle bundle = new Bundle();
            bundle.putString("note_id", item.getUID());
            NavigationUtils.goNewFragmentWithBundle(bundle,fragmentManager,new Note());

        });
        recyclerView.setAdapter(noteAdapter);

        // FAB lógica

        setListeners();
        // Escucha en Firestore
        setupFirestoreListener();

        return view;
    }
    private void initComponents(View view){
        mainActivity = (MainActivity) getActivity();
        backButton = mainActivity.getBackButton();
        ib_save = mainActivity.getIb_save();
        ib_profile_superior = mainActivity.getIb_self_profile();
        // Inicialización
        recyclerView = view.findViewById(R.id.rc_all_notes_adapter);
        fbAddNote = view.findViewById(R.id.fb_add_note_list_notes);
        fbMoreOptions = view.findViewById(R.id.fb_more_options_list_notes);
        fragmentManager = mainActivity.getSupportFragmentManager();
    }
    private void setVisibility(){
        backButton.setVisibility(View.VISIBLE);
        ib_save.setVisibility(View.GONE);
        ib_profile_superior.setVisibility(View.VISIBLE);
        fbAddNote.setVisibility(View.GONE);
        isAllFabsVisible = false;
    }
    private void setListeners(){
        backButton.setOnClickListener(this);
        fbMoreOptions.setOnClickListener(v -> toggleFabs());
        fbAddNote.setOnClickListener(this);
    }
    private void toggleFabs() {
        fbAddNote.setVisibility(isAllFabsVisible?View.GONE:View.VISIBLE);
        isAllFabsVisible = !isAllFabsVisible;
    }

    private void setupFirestoreListener() {
        collectionNotekies
                .whereEqualTo("UID_USER", UID)
                .orderBy("last_update", Query.Direction.DESCENDING)
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        Log.e("FirestoreError", e.getMessage());
                        Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                        for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                            DocumentSnapshot doc = dc.getDocument();
                            NotekieModel note = NotekieModel.fromSnapshot(doc);
                            switch (dc.getType()) {
                                case ADDED:
                                    items.add(dc.getNewIndex(), note);
                                    noteAdapter.notifyItemInserted(dc.getNewIndex());
                                    break;
                                case MODIFIED:
                                    items.set(dc.getOldIndex(), note);
                                    noteAdapter.notifyItemChanged(dc.getOldIndex());
                                    break;
                                case REMOVED:
                                    int indexToRemove = -1;
                                    for (int i = 0; i < items.size(); i++) {
                                        if (items.get(i).getUID().equals(doc.getId())) {
                                            indexToRemove = i;
                                            break;
                                        }
                                    }
                                    if (indexToRemove != -1) {
                                        items.remove(indexToRemove);
                                        noteAdapter.notifyItemRemoved(indexToRemove);
                                    }
                                    break;
                            }

                            Log.d("FirestoreNotes", "Note actualizada: " + note.getTitle());
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.ib_back){
            NavigationUtils.goBack(fragmentManager,mainActivity);
        } else if (v.getId()==R.id.fb_add_note_list_notes) {
            NavigationUtils.goNewFragment(fragmentManager,new Note());
        }
    }
}
