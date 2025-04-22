package com.example.buuktu.views;

import static android.widget.Toast.LENGTH_LONG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buuktu.R;
import com.example.buuktu.adapters.WorldkieAdapter;
import com.example.buuktu.models.WorldkieModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment implements View.OnClickListener {
    private String UID;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore db;
    private FirebaseStorage storage = FirebaseStorage.getInstance("gs://buuk-tu-worldkies");
    private ArrayList<WorldkieModel> worldkieModelArrayList;
    private RecyclerView rc_worldkies;
    private FloatingActionButton fb_parent, fb_add;
    private boolean isAllFabsVisible;
    private WorldkieAdapter worldkieAdapter;
    CollectionReference dbWorldkies;
    ImageButton ib_save,ib_profile_superior;
    FragmentManager fragmentManager;

    //private ListenerRegistration firestoreListener;
    public Home() {
        // Required empty public constructor
    }

    public static Home newInstance() {
        return new Home();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        MainActivity mainActivity = (MainActivity) getActivity();
        ImageButton backButton = mainActivity.getBackButton();
        backButton.setVisibility(View.GONE);
        ib_save = mainActivity.getIb_save();
        ib_save.setVisibility(View.GONE);
        ib_profile_superior = mainActivity.getIb_self_profile();
        ib_profile_superior.setVisibility(View.VISIBLE);
        initComponents(view);
        fb_add.setVisibility(View.GONE);
        isAllFabsVisible = false;
        setListeners();
        db = FirebaseFirestore.getInstance();
        UID = auth.getCurrentUser().getUid();
        rc_worldkies.setLayoutManager(new LinearLayoutManager(getContext()));
        worldkieAdapter = new WorldkieAdapter(worldkieModelArrayList, getContext(), getParentFragmentManager());
        rc_worldkies.setAdapter(worldkieAdapter);
        dbWorldkies = db.collection("Worldkies");
        fragmentManager = requireActivity().getSupportFragmentManager();

        dbWorldkies.whereEqualTo("UID_AUTHOR", UID)
                .orderBy("last_update", Query.Direction.DESCENDING)
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        Log.e("Error", e.getMessage());
                        Toast.makeText(getContext(), "Error al escuchar cambios: " + e.getMessage(), LENGTH_LONG).show();
                        return;
                    }

                    if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                      //  worldkieModelArrayList.clear(); // Limpia la lista antes de agregar nuevos datos

                        for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                            DocumentSnapshot doc = dc.getDocument();
                            WorldkieModel worldkieModel = createWorldkieModelFromDocument(doc); // Crea el modelo inicial
                            switch (dc.getType()) {
                                case ADDED:
                                    safeAddToList(worldkieModelArrayList, dc.getNewIndex(), worldkieModel);
                                    worldkieAdapter.notifyItemInserted(dc.getNewIndex());
                                    break;
                                case MODIFIED:
                                    safeSetToList(worldkieModelArrayList, dc.getOldIndex(), worldkieModel);
                                    worldkieAdapter.notifyItemChanged(dc.getOldIndex());
                                    break;
                                case REMOVED:
                                    safeRemoveFromList(worldkieModelArrayList, dc.getOldIndex());
                                    worldkieAdapter.notifyItemRemoved(dc.getOldIndex());
                                    break;
                            }

                            if (doc.getBoolean("photo_default")) {
                            } else {
                                //loadAndSetImage(doc.getId(), worldkieModel);
                            }
                        }

                    } else {
                        // Si no hay documentos, limpia la lista y actualiza el RecyclerView
                       // worldkieModelArrayList.clear();
                       // updateRecyclerView(worldkieModelArrayList);
                    }
                });
        return view;
    }
    private void setListeners(){
        fb_add.setOnClickListener(this);
        fb_parent.setOnClickListener(this);
    }
    public void initComponents(View view){
        fb_parent = view.findViewById(R.id.fb_parentWorldkies);
        fb_add = view.findViewById(R.id.fb_addWorldkie);
        rc_worldkies = view.findViewById(R.id.rc_worldkies);
        worldkieModelArrayList = new ArrayList<>();
    }
    private void navigateToNextFragment() {
        CreateEditWorldkie createEditWorldkie = new CreateEditWorldkie();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, createEditWorldkie) // El contenedor donde se muestra el fragmento
                .addToBackStack(null) // Añade la transacción a la pila para que se pueda volver atrás
                .commit();
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
    private void safeRemoveFromList(ArrayList<WorldkieModel> list, int index) {
        if (index >= 0 && index < list.size()) {
            list.remove(index);
        }
    }
    private WorldkieModel createWorldkieModelFromDocument(DocumentSnapshot doc) {
            /*public WorldkieModel(String UID, String UID_AUTHOR,int drawable, String name, Date
        creation_date,boolean photo_default,Date last_update, boolean worldkie_private) {
*/
      //  if (doc.getBoolean("photo_default")) {
            //Drawable drawable = getResources().getDrawable(R.drawable.worldkie_default);
            return new WorldkieModel(
                    doc.getId(),
                    doc.getString("UID_AUTHOR"), R.drawable.twotone_lightbulb_24,
                    doc.getString("name"),
                    doc.getTimestamp("creation_date").toDate(),
                    doc.getBoolean("photo_default"),
                    doc.getTimestamp("last_update").toDate(),
                    doc.getBoolean("worldkie_private"));
          //  );
      //  } /*else {
          /*  return new WorldkieModel(
                    doc.getId(),
                    doc.getString("UID_AUTHOR"),
                    doc.getString("name"),R.mipmap.img_del_stuffkie,
                    false,
                    doc.getBoolean("worldkie_private")
            );*/
       //}*/
        //return null;
    }

    // Helper method to update the RecyclerView
        private void updateRecyclerView(ArrayList<WorldkieModel> worldkieModelArrayList) {
            WorldkieAdapter worldkieAdapter = new WorldkieAdapter(worldkieModelArrayList, getContext(), getParentFragmentManager());
            rc_worldkies.setAdapter(worldkieAdapter);
            rc_worldkies.setLayoutManager(new LinearLayoutManager(getContext()));
        }
    @Override
    public void onStop() {
        super.onStop();
       /* if (firestoreListener != null) {
            firestoreListener.remove();
        }*/
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.fb_parentWorldkies){
                if (!isAllFabsVisible) {
                    fb_add.setVisibility(View.VISIBLE);
                    isAllFabsVisible = true;
                } else {
                    fb_add.setVisibility(View.GONE);
                    isAllFabsVisible = false;
                }
            } else if (v.getId()==R.id.fb_addWorldkie) {
            navigateToNextFragment();
        }
    }
}