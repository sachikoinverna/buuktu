package com.example.buuktu;

import static android.widget.Toast.LENGTH_LONG;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.buuktu.adapters.NoteAdapter;
import com.example.buuktu.adapters.NotikieListAdapter;
import com.example.buuktu.listeners.OnDialogDelClickListener;
import com.example.buuktu.models.CardItem;
import com.example.buuktu.models.NoteItem;
import com.example.buuktu.models.NotikieModel;
import com.example.buuktu.views.CreateWorldkie;
import com.example.buuktu.views.WorldkieMenu;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Notes#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Notes extends Fragment implements OnDialogDelClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView rc_all_notes_adapter;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    NoteAdapter noteAdapter;
    FirebaseFirestore db;
    CollectionReference collectioNotekies;
    String UID;
    ArrayList<NoteItem> items;
    FloatingActionButton fb_more_options_list_notes,fb_add_note_list_notes;
    boolean isAllFabsVisible;
    public Notes() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Notes.
     */
    // TODO: Rename and change types and number of parameters
    public static Notes newInstance(String param1, String param2) {
        Notes fragment = new Notes();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_notes, container, false);
        db = FirebaseFirestore.getInstance();
        rc_all_notes_adapter = view.findViewById(R.id.rc_all_notes_adapter);
        rc_all_notes_adapter.setHasFixedSize(true);
        staggeredGridLayoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        rc_all_notes_adapter.setLayoutManager(staggeredGridLayoutManager);
        fb_add_note_list_notes = view.findViewById(R.id.fb_add_note_list_notes);
        fb_more_options_list_notes = view.findViewById(R.id.fb_more_options_list_notes);
        fb_add_note_list_notes.setVisibility(View.GONE);
        isAllFabsVisible = false;
        fb_more_options_list_notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isAllFabsVisible()) {
                    fb_add_note_list_notes.setVisibility(View.VISIBLE);
                    setAllFabsVisible(true);
                } else {
                    fb_add_note_list_notes.setVisibility(View.GONE);
                    setAllFabsVisible(false);
                }
            }
        });
        fb_add_note_list_notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Note note = new Note();
              //  Bundle bundle = new Bundle();
              //  bundle.putString("note_id",item.getUID());
              //  note.setArguments(bundle);
                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment_container, note) .addToBackStack(null) // Permite regresar atrás con el botón de retroceso
                        .commit();
            }
        });
        collectioNotekies = db.collection("Notekies");
        items = new ArrayList<>();
        UID = FirebaseAuth.getInstance().getUid();
        collectioNotekies.whereEqualTo("UID_USER", UID).orderBy("last_update", Query.Direction.DESCENDING).addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e != null) {
                Log.e("Error", e.getMessage());
                Toast.makeText(getContext(), "Error al escuchar cambios: " + e.getMessage(), LENGTH_LONG).show();
                return;
            }

            if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
               // items.clear(); // Limpia la lista antes de agregar nuevos datos

                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                    DocumentSnapshot doc = dc.getDocument();

                    //if (documentSnapshot.getBoolean("photo_default")) {
                    //    if (!documentSnapshot.getId().equals(firebaseAuth.getUid())) {
                    String title = doc.getString("title");
                    if(title.isEmpty()){
                        title = "(Sin titulo)";
                    }
                    NoteItem notekieModel = new NoteItem(
                            doc.getString("text"), doc.getString("title"),UID
                            ,doc.getTimestamp("last_update"),doc.getId()
                    );
                    switch (dc.getType()) {
                        case ADDED:
                            items.add(dc.getNewIndex(), notekieModel);
                            noteAdapter.notifyItemInserted(dc.getNewIndex());
                            break;

                        case MODIFIED:
                            items.set(dc.getOldIndex(), notekieModel);
                            noteAdapter.notifyItemChanged(dc.getOldIndex());
                            break;

                        case REMOVED:
                            items.remove(dc.getOldIndex());
                            noteAdapter.notifyItemRemoved(dc.getOldIndex());
                            break;
                    }

                    Log.d("StuffkiesSearch", "Stuffkie encontrado: " + doc.getString("name"));

                  //  items.add(notekieModel);
                 //   noteAdapter.notifyDataSetChanged();  // Actualiza vista

//                    updateRecyclerView(items);
                }
            }
        });
        rc_all_notes_adapter.setLayoutManager(new GridLayoutManager(getContext(), 2));
        noteAdapter = new NoteAdapter(getContext(), items, new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(NoteItem item) {
                Note note = new Note();
                Bundle bundle = new Bundle();
                bundle.putString("note_id",item.getUID());
                note.setArguments(bundle);
                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment_container, note) .addToBackStack(null) // Permite regresar atrás con el botón de retroceso
                        .commit();
                //Intent intent = new Intent(holder.itemView.getContext(), Worldkie.class);
                //   holder.
                // Intent intent = new Intent(holder.itemView.getContext(), WorldkieMenu.class);
                // holder.itemView.getContext().startActivity(intent);
            }
        });
        rc_all_notes_adapter.setAdapter(noteAdapter);
        return view;
    }
    public FloatingActionButton getFb_parent() {
        return fb_more_options_list_notes;
    }

    public FloatingActionButton getFb_add() {
        return fb_add_note_list_notes;
    }
    public boolean isAllFabsVisible(){
        return isAllFabsVisible;
    }

    public void setAllFabsVisible(boolean allFabsVisible) {
        isAllFabsVisible = allFabsVisible;
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.note_preview_menu, menu);
    }
    private void updateRecyclerView(ArrayList<NoteItem> noteItemArrayList) {
       // NoteAdapter notekieListAdapter = new NoteAdapter(getContext(),this, );
       // rc_all_notes_adapter.setAdapter(notekieModelArrayList);
        //rc_all_notes_adapter.setLayoutManager(new LinearLayoutManager(getContext()));
    }


    @Override
    public void onAccept() {

    }

    @Override
    public void onCancel() {

    }
}