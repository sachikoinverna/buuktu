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

import com.example.buuktu.adapters.UserkieSearchAdapter;
import com.example.buuktu.models.UserkieModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserkiesSearch#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserkiesSearch extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    RecyclerView rc_userkies_search;
    private FirebaseFirestore db;
    FirebaseAuth firebaseAuth;
    private ArrayList<UserkieModel> userkieModelArrayList;
    CollectionReference collectionUserkies;
    UserkieSearchAdapter userkieSearchAdapter;
    static SearchView searchView;
    public UserkiesSearch(SearchView searchView) {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserkiesSearch.
     */
    // TODO: Rename and change types and number of parameters
    public static UserkiesSearch newInstance(String param1, String param2) {
        UserkiesSearch fragment = new UserkiesSearch(searchView);
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_userkies_search, container, false);
        rc_userkies_search = view.findViewById(R.id.rc_userkies_search);
        db = FirebaseFirestore.getInstance();
        userkieModelArrayList = new ArrayList<>();
        collectionUserkies = db.collection("Users");
        userkieSearchAdapter = new UserkieSearchAdapter(userkieModelArrayList, getContext(), getParentFragmentManager());
        rc_userkies_search.setAdapter(userkieSearchAdapter);
        rc_userkies_search.setLayoutManager(new LinearLayoutManager(getContext()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                collectionUserkies.addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        Log.e("Error", e.getMessage());
                        Toast.makeText(getContext(), "Error al escuchar cambios: " + e.getMessage(), LENGTH_LONG).show();
                        return;
                    }

                    if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                        userkieModelArrayList.clear(); // Limpia la lista antes de agregar nuevos datos

                        //for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                        for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                            DocumentSnapshot doc = dc.getDocument();                    //if (docum
                            //if (documentSnapshot.getBoolean("photo_default")) {
                           // if(newText)
                            if (!doc.getId().equals(firebaseAuth.getUid())) {

                                Drawable drawable = getResources().getDrawable(R.drawable.worldkie_default);
                                UserkieModel userkieModel = new UserkieModel(
                                        doc.getId(),
                                        doc.getString("name"),
                                        R.drawable.cloudlogin,
                                        doc.getString("username"),
                                        true, doc.getBoolean("private")
                                );
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
                                });
                            }
                return false;
            };
        };
                        }}
                                          });
        collectionUserkies.addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e != null) {
                Log.e("Error", e.getMessage());
                Toast.makeText(getContext(), "Error al escuchar cambios: " + e.getMessage(), LENGTH_LONG).show();
                return;
            }

            if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                userkieModelArrayList.clear(); // Limpia la lista antes de agregar nuevos datos

                //for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                    DocumentSnapshot doc = dc.getDocument();                    //if (docum
                    //if (documentSnapshot.getBoolean("photo_default")) {
                    if (!doc.getId().equals(firebaseAuth.getUid())) {

                        Drawable drawable = getResources().getDrawable(R.drawable.worldkie_default);
                        UserkieModel userkieModel = new UserkieModel(
                                doc.getId(),
                                doc.getString("name"),
                                R.drawable.cloudlogin,
                                doc.getString("username"),
                                true, doc.getBoolean("private")
                        );
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

                    //  userkieModelArrayList.add(userkieModel);
                    // updateRecyclerView(userkieModelArrayList);
                }// Actualiza después de cargar cada imagen
                //  } else {
                      /*  StorageReference storageRef = storage.getReference().child(documentSnapshot.getId());
                        final long ONE_MEGABYTE = 1024 * 1024;

                        storageRef.getBytes(ONE_MEGABYTE)
                                .addOnSuccessListener(bytes -> {*/
                                 /*   Bitmap bitmap = BitmapUtils.convertCompressedByteArrayToBitmap(bytes);
                                    Drawable drawable = new BitmapDrawable(getResources(), bitmap);

                                    WorldkieModel worldkieModel = new WorldkieModel(
                                            documentSnapshot.getId(),
                                            documentSnapshot.getString("name"),
                                            R.
                                            documentSnapshot.getString("username"),
                                            drawable,
                                            false,
                                            documentSnapshot.getBoolean("worldkie_private")
                                    );*/
                //     worldkieModelArrayList.add(worldkieModel);
                //    updateRecyclerView(worldkieModelArrayList); // Actualiza después de cargar cada imagen
                              /*  })
                                .addOnFailureListener(exception -> {
                                    Log.e("Error", "Error al cargar imagen: " + exception.getMessage());
                                });*/
            }

               // updateRecyclerView(worldkieModelArrayList); // Actualiza el RecyclerView después de procesar todos los documentos
            }); /*else {
                // Si no hay documentos, limpia la lista y actualiza el RecyclerView
                worldkieModelArrayList.clear();
                updateRecyclerView(worldkieModelArrayList);
            }*/
        //});
        return view;
    }
    private void safeAddToList(ArrayList<UserkieModel> list, int index, UserkieModel item) {
        if (index >= 0 && index <= list.size()) {
            list.add(index, item);
        } else {
            list.add(item); // Fallback: añade al final
        }
    }

    private void safeSetToList(ArrayList<UserkieModel> list, int index, UserkieModel item) {
        if (index >= 0 && index < list.size()) {
            list.set(index, item);
        }
    }
    private void updateRecyclerView(ArrayList<UserkieModel> userkieModelArrayList) {
        UserkieSearchAdapter userkieSearchAdapter = new UserkieSearchAdapter(userkieModelArrayList, getContext(), getParentFragmentManager());
        rc_userkies_search.setAdapter(userkieSearchAdapter);
        rc_userkies_search.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}