package com.example.buuktu.views;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.buuktu.R;
import com.example.buuktu.adapters.WorldkieAdapter;
import com.example.buuktu.models.WorldkieModel;
import com.example.buuktu.utils.BitmapUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment {
    String UID;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    ArrayList<WorldkieModel> worldkieModelArrayList;
    private FirebaseFirestore db;
    FirebaseStorage storage = FirebaseStorage.getInstance("gs://buuk-tu-worldkies");
    RecyclerView rc_worldkies;;
    ImageButton ib_addWorldkie;;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home.
     */
    // TODO: Rename and change types and number of parameters
    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        // Inicializa las vistas dentro de onViewCreated
        rc_worldkies = view.findViewById(R.id.rc_worldkies);
        ib_addWorldkie = view.findViewById(R.id.ib_addWorldkie);
        db = FirebaseFirestore.getInstance();
        UID = auth.getCurrentUser().getUid();
        Toast.makeText(getContext(), UID, Toast.LENGTH_SHORT).show();
        worldkieModelArrayList = new ArrayList<>();
        CollectionReference dbWorldkies = db.collection("Worldkies");
        dbWorldkies.whereEqualTo("UID_AUTHOR", UID)
                .orderBy("last_update")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            // Usamos un contador para asegurar que el RecyclerView se actualice solo después de cargar todas las imágenes
                            final int totalDocuments = queryDocumentSnapshots.size();
                            final int[] loadedDocuments = {0}; // Usamos un array para mantener la referencia en el bloque lambda

                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                                StorageReference storageRef = storage.getReference().child(documentSnapshot.getString("UID"));
                                final long ONE_MEGABYTE = 1024 * 1024;

                                storageRef.getBytes(ONE_MEGABYTE)
                                        .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                            @Override
                                            public void onSuccess(byte[] bytes) {
                                                // Convierte el byte array a bitmap y lo asigna
                                                Bitmap bitmap = BitmapUtils.convertCompressedByteArrayToBitmap(bytes);
                                                Drawable drawable = new BitmapDrawable(getResources(), bitmap);

                                                // Crear un nuevo WorldkieModel con los datos y agregarlo a la lista
                                                WorldkieModel worldkieModel = new WorldkieModel(
                                                        documentSnapshot.getId(),
                                                        documentSnapshot.getString("name"),
                                                        drawable
                                                );
                                                worldkieModelArrayList.add(worldkieModel);

                                                // Aumentar el contador de documentos cargados
                                                loadedDocuments[0]++;

                                                // Verificar si todos los documentos se han cargado
                                                if (loadedDocuments[0] == totalDocuments) {
                                                    // Configurar el adaptador y el RecyclerView solo después de que todos los datos estén listos
                                                    WorldkieAdapter worldkieAdapter = new WorldkieAdapter(worldkieModelArrayList, getContext());
                                                    rc_worldkies.setAdapter(worldkieAdapter);
                                                    rc_worldkies.setLayoutManager(new LinearLayoutManager(getContext()));
                                                }
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception exception) {
                                                Toast.makeText(getContext(), "Error al cargar imagen", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        } else {
                            // Manejo en caso de que no haya datos
                            Toast.makeText(getContext(), "No se encontraron elementos", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        WorldkieAdapter worldkieAdapter = new WorldkieAdapter(worldkieModelArrayList, getContext());
        rc_worldkies.setAdapter(worldkieAdapter);
        rc_worldkies.setLayoutManager(new LinearLayoutManager(getContext()));
        ib_addWorldkie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return view;
    }
}