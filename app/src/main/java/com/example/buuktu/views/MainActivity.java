package com.example.buuktu.views;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.buuktu.R;
import com.example.buuktu.models.WorldkieModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
String UID;
FirebaseAuth auth = FirebaseAuth.getInstance();
ArrayList<WorldkieModel> worldkieModelArrayList;
private FirebaseFirestore db;

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
        db = FirebaseFirestore.getInstance();
        UID = auth.getCurrentUser().getUid();
        Toast.makeText(this, UID, Toast.LENGTH_SHORT).show();
        worldkieModelArrayList = new ArrayList<>();
        CollectionReference dbWorldkies = db.collection("Worldkies");
        dbWorldkies.orderBy("UID").startAt(UID).endAt(UID + '~').get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()){
                    for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
                        // Create a storage reference from our app
                       // StorageReference storageRef = storage.getReference();

// Create a reference with an initial file path and name
                        //StorageReference pathReference = storageRef.child("images/stars.jpg");

// Create a reference to a file from a Google Cloud Storage URI
                       // StorageReference gsReference = storage.getReferenceFromUrl("gs://bucket/images/stars.jpg");

// Create a reference from an HTTPS URL
// Note that in the URL, characters are URL escaped!
                       // StorageReference httpsReference = storage.getReferenceFromUrl("https://firebasestorage.googleapis.com/b/bucket/o/images%20stars.jpg");
                       // StorageReference islandRef = storageRef.child("images/island.jpg");

                        //final long ONE_MEGABYTE = 1024 * 1024;
                        //islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                          //  @Override
                            //public void onSuccess(byte[] bytes) {
                              //  DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(i);
                                //WorldkieModel worldkieModel = new WorldkieModel(documentSnapshot.getString(""}
                        //}).addOnFailureListener(new OnFailureListener() {
                          //  @Override
                            //public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                            }
                        //});
                    }
  } //else if (queryDocumentSnapshots.isEmpty()) {
                    
            //    }
            //}
        });
    }
}