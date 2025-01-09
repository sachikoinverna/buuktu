package com.example.buuktu.controllers;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.buuktu.R;
import com.example.buuktu.models.UserModel;
import com.example.buuktu.models.WorldkieModel;
import com.example.buuktu.views.CreateWorldkie;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class CreateWorldkieController implements View.OnClickListener {
    private final CreateWorldkie createWorldkie;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage storage = FirebaseStorage.getInstance("gs://buuk-tu-worldkies");
    public CreateWorldkieController(CreateWorldkie createWorldkie) {
        this.createWorldkie = createWorldkie;
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.db = FirebaseFirestore.getInstance();
    }
    private void putDefaultImage(){
        createWorldkie.getIB_profile_photo().setImageResource(R.mipmap.default_icon);
        Bitmap bitmap = ((BitmapDrawable) createWorldkie.getIB_profile_photo().getDrawable()).getBitmap();
        createWorldkie.personalizarImagen(bitmap);
        createWorldkie.getBt_deleteImageRegister().setVisibility(View.INVISIBLE);
    }
    private void addDataToFirestore() {
        CollectionReference dbWorldkies = db.collection("Worldkies");
        WorldkieModel worldkieModel = new WorldkieModel(firebaseAuth.getUid(), createWorldkie.getEt_nameWorldkieCreate().getText().toString());
        dbWorldkies.add(worldkieModel).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                String uid = documentReference.getId();
                Toast.makeText(createWorldkie, "Your Course has been added to Firebase Firestore", Toast.LENGTH_SHORT).show();
                if (createWorldkie.getIB_profile_photo().getDrawable().equals(R.mipmap.default_icon)) {
                    // StorageReference userRef = storage.getReference().child("ajYrQVbzQAdW7mgjIF3fxNJsIjF3");
                    //register.getIB_profile_photo().setDrawingCacheEnabled(true);
                    //register.getIB_profile_photo().buildDrawingCache();
                    //Bitmap bitmap = ((BitmapDrawable) register.getIB_profile_photo().getDrawable()).getBitmap();
                    // ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    //bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    //byte[] data = baos.toByteArray();
                    //Uri file = Uri.fromFile(new File(String.valueOf(R.mipmap.default_icon)));
                    //userRef.putBytes(data);
                } else {
                    StorageReference userRef = storage.getReference().child(uid);
                    userRef.child(createWorldkie.getImage().getLastPathSegment()).putFile(createWorldkie.getImage());
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ib_delete_img_create_wordlkie){
            putDefaultImage();
        } else if (view.getId()==R.id.bt_ok_addWordlkie) {
            addDataToFirestore();
        } else if (view.getId()==R.id.bt_cancel_addWordlkie) {
            createWorldkie.finish();
        }
    }
}
