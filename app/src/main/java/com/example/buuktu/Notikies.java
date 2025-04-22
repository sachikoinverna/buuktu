package com.example.buuktu;

import static android.widget.Toast.LENGTH_LONG;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buuktu.adapters.NotikieListAdapter;
import com.example.buuktu.adapters.StuffkieSearchAdapter;
import com.example.buuktu.models.CardItem;
import com.example.buuktu.models.NotikieModel;
import com.example.buuktu.models.StuffkieModel;
import com.example.buuktu.views.MainActivity;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Notikies#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Notikies extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

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
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Notikies.
     */
    // TODO: Rename and change types and number of parameters
    public static Notikies newInstance(String param1, String param2) {
        Notikies fragment = new Notikies();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notikies, container, false);
        MainActivity mainActivity = (MainActivity) getActivity();
        ImageButton backButton = mainActivity.getBackButton();
        backButton.setVisibility(View.GONE);
        rc_notikies_list = view.findViewById(R.id.rc_notikies_list);
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
                    //if (documentSnapshot.getBoolean("photo_default")) {
                    //    if (!documentSnapshot.getId().equals(firebaseAuth.getUid())) {

                    Drawable drawable = getResources().getDrawable(R.drawable.thumb_custom);
                    NotikieModel notikieModel = new NotikieModel(
                            documentSnapshot.getString("message"),
                            documentSnapshot.getTimestamp("date"), // Usamos getTimestamp para obtener el campo como un Timestamp
                            Math.toIntExact(documentSnapshot.getLong("icon"))
                    );


                    Log.d("StuffkiesSearch", "Stuffkie encontrado: " + documentSnapshot.getString("name"));

                    notikieModelArrayList.add(notikieModel);
                    updateRecyclerView(notikieModelArrayList);
                }
            }
        });
        return view;
    }
    private void updateRecyclerView(ArrayList<NotikieModel> notikieModelArrayList) {
        NotikieListAdapter notikieListAdapter = new NotikieListAdapter(notikieModelArrayList, getContext(), getParentFragmentManager());
        rc_notikies_list.setAdapter(notikieListAdapter);
        rc_notikies_list.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}