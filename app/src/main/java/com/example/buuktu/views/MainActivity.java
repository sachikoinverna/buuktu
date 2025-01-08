package com.example.buuktu.views;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buuktu.R;
import com.example.buuktu.adapters.WorldkieAdapter;
import com.example.buuktu.models.WorldkieModel;
import com.example.buuktu.utils.BitmapUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
String UID;
FirebaseAuth auth = FirebaseAuth.getInstance();

ArrayList<WorldkieModel> worldkieModelArrayList;
private FirebaseFirestore db;
FirebaseStorage storage = FirebaseStorage.getInstance("gs://buuk-tu-worldkies");
RecyclerView rc_worldkies;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        rc_worldkies = findViewById(R.id.rc_worldkies);
        db = FirebaseFirestore.getInstance();
        UID = auth.getCurrentUser().getUid();
        Toast.makeText(this, UID, Toast.LENGTH_SHORT).show();
        worldkieModelArrayList = new ArrayList<>();
        CollectionReference dbWorldkies = db.collection("Worldkies");
        dbWorldkies.whereEqualTo("UID_AUTHOR",UID).orderBy("last_update").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()){
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                        StorageReference storageRef = storage.getReference().child(documentSnapshot.getString("UID"));
                        final long ONE_MEGABYTE = 1024 * 1024;
                        storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                WorldkieModel worldkieModel = new WorldkieModel(documentSnapshot.getId(),documentSnapshot.getString("name"), BitmapUtils.convertCompressedByteArrayToBitmap(bytes));
                                worldkieModelArrayList.add(worldkieModel);
                            }
                        }).addOnFailureListener(new OnFailureListener() {@Override
                            public void onFailure(@NonNull Exception exception) {
                                Toast.makeText(MainActivity.this, "Error al cargar imagen", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
  } else if (queryDocumentSnapshots.isEmpty()) {
                    
              }
            }
        });
        WorldkieAdapter worldkieAdapter = new WorldkieAdapter(worldkieModelArrayList, this);
        rc_worldkies.setAdapter(worldkieAdapter);
        rc_worldkies.setLayoutManager(new LinearLayoutManager(this));

    }
}