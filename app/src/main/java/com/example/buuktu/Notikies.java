package com.example.buuktu;

import static android.widget.Toast.LENGTH_LONG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buuktu.adapters.NotikieListAdapter;
import com.example.buuktu.models.NotikieModel;
import com.example.buuktu.views.MainActivity;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Notikies#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Notikies extends Fragment {
    MainActivity mainActivity;
    ImageButton backButton,ib_profile_superior;
    RecyclerView rc_notikies_list;
   NotikieListAdapter notikieListAdapter;
   FirebaseFirestore db;
   CollectionReference notikiesCollection;
   ArrayList<NotikieModel> notikieModelArrayList;
    public Notikies() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Notikies.
     */
    public static Notikies newInstance() {
        return new Notikies();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notikies, container, false);
        initComponents(view);
        setVisibility();
        db = FirebaseFirestore.getInstance();
        notikiesCollection = db.collection("Notikies");
        notikieModelArrayList = new ArrayList<>();
        notikiesCollection.orderBy("date", Query.Direction.DESCENDING).addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e != null) {
                Log.e("Error", e.getMessage());
                Toast.makeText(getContext(), "Error al escuchar cambios: " + e.getMessage(), LENGTH_LONG).show();
                return;
            }

            if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                notikieModelArrayList.clear(); // Limpia la lista antes de agregar nuevos datos

                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                    notikieModelArrayList.add(NotikieModel.fromSnapshot(documentSnapshot));
                    updateRecyclerView(notikieModelArrayList);
                }
            }
        });
        return view;
    }
    private void initComponents(View view){
        mainActivity = (MainActivity) getActivity();
        backButton = mainActivity.getBackButton();
        ib_profile_superior = mainActivity.getIb_self_profile();
        rc_notikies_list = view.findViewById(R.id.rc_notikies_list);
    }
    private void setVisibility(){
        backButton.setVisibility(View.GONE);
        ib_profile_superior.setVisibility(View.VISIBLE);

    }
    private void updateRecyclerView(ArrayList<NotikieModel> notikieModelArrayList) {
        NotikieListAdapter notikieListAdapter = new NotikieListAdapter(notikieModelArrayList, mainActivity);
        rc_notikies_list.setAdapter(notikieListAdapter);
        rc_notikies_list.setLayoutManager(new LinearLayoutManager(mainActivity));
    }
}