package com.example.buuktu.controllers;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;

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
import com.google.firebase.storage.UploadTask;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CreateWorldkieController implements View.OnClickListener {
    private final CreateWorldkie createWorldkie;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage storage = FirebaseStorage.getInstance("gs://buuk-tu-worldkies");
    public CreateWorldkieController(CreateWorldkie createWorldkie) {
        this.createWorldkie = createWorldkie;
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.db = FirebaseFirestore.getInstance();
        createWorldkie.getIB_profile_photo().setTag(R.drawable.worldkie_default, true);
    }
    private void putDefaultImage(){
        createWorldkie.getIB_profile_photo().setImageResource(R.mipmap.default_icon);
        Bitmap bitmap = ((BitmapDrawable) createWorldkie.getIB_profile_photo().getDrawable()).getBitmap();
        createWorldkie.personalizarImagen(bitmap);
        createWorldkie.getBt_deleteImageRegister().setVisibility(View.INVISIBLE);
    }
    private void addDataToFirestore() {
        CollectionReference dbWorldkies = db.collection("Worldkies");
        Date creation_date = new Date();
        Map<String, Object> worldkieData = new HashMap<>();
        worldkieData.put("UID_AUTHOR", firebaseAuth.getUid());
        worldkieData.put("name", createWorldkie.getEt_nameWorldkieCreate().getText().toString()); // Corrección clave
        worldkieData.put("creation_date", creation_date);
        worldkieData.put("last_update", creation_date);
        boolean isDefaultImage = (boolean) createWorldkie.getIB_profile_photo().getTag(R.drawable.worldkie_default);
        worldkieData.put("photo_default", isDefaultImage);
        dbWorldkies.add(worldkieData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
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
                    Drawable drawable = createWorldkie.getIB_profile_photo().getDrawable();
                    Bitmap bitmap = null;

                    if (drawable instanceof BitmapDrawable) {
                        bitmap = ((BitmapDrawable) drawable).getBitmap();
                    } else if (drawable instanceof RoundedBitmapDrawable) {
                        bitmap = ((RoundedBitmapDrawable) drawable).getBitmap();
                    }// createWorldkie.getIB_profile_photo().setNam
                    userRef.child(uid+createWorldkie.getImage().getLastPathSegment());
                    UploadTask uploadTask = userRef.putFile(createWorldkie.getImage());
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(createWorldkie, "Subida exitosa",Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(createWorldkie,"Subida fallida",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
               /* Log.e("Error", e.getMessage().toString());
                Toast.makeText(createWorldkie, e.getMessage().toString(), Toast.LENGTH_LONG).show();*/
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
