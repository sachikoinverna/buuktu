package com.example.buuktu.views;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.buuktu.R;
import com.example.buuktu.controllers.CreateWorldkieController;
import com.example.buuktu.models.WorldkieModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;

public class CreateWorldkie extends AppCompatActivity {
    ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    Uri image;
    int flag = Intent.FLAG_GRANT_READ_URI_PERMISSION;
    int RESULT_CODE = 0;
    int REQUEST_CODE = 1;
    ImageButton bt_chooseImage;
    ImageButton bt_deleteImageRegister;
    ImageButton bt_cancel;
    ImageButton bt_ok;
    TextInputEditText et_nameWorldkieCreate;
    FirebaseStorage storage = FirebaseStorage.getInstance("gs://buuk-tu-worldkies");
    boolean create = getIntent().getBooleanExtra("create",true);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_worldkie);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        bt_cancel = findViewById(R.id.bt_cancel_addWordlkie);
        bt_ok = findViewById(R.id.bt_ok_addWordlkie);
        et_nameWorldkieCreate = findViewById(R.id.et_nameWorldkieCreate);
        bt_chooseImage = findViewById(R.id.ib_select_img_create_worldkie);
        bt_deleteImageRegister = findViewById(R.id.ib_delete_img_create_wordlkie);
        bt_deleteImageRegister.setVisibility(View.INVISIBLE);
      //  CreateWorldkieController createWorldkieController = new CreateWorldkieController(this);
     /*   bt_cancel.setOnClickListener(createWorldkieController);
        bt_ok.setOnClickListener(createWorldkieController);
        bt_deleteImageRegister.setOnClickListener(createWorldkieController);
*/
      /*  pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                    if (uri != null) {
                        //registerController.setUri(uri);
                        image = uri;
                        try {
                            //Bitmap image1 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                            ImageDecoder.Source image1 = ImageDecoder.createSource(this.getContentResolver(),uri);
                            Bitmap bitmap = ImageDecoder.decodeBitmap(image1);
                            Bitmap bitmap1 = Bitmap.createScaledBitmap(bitmap, 640, 640, false);
                            bt_chooseImage.setImageBitmap(bitmap1);
                            personalizarImagen(bitmap1);*/
                            /*StorageReference userRef = storage.getReference().child("ujlDPggHwenVJNQcUSqO");
                            userRef.child(image.getLastPathSegment()).putFile(image);*/
                         /*   bt_deleteImageRegister.setVisibility(View.VISIBLE);

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        Log.d("PhotoPicker", "Selected URI: " + uri);
                    } else {
                        Log.d("PhotoPicker", "No media selected");
                    }
                });*/
        if (create){
            createMode();
        }else {
           // editarMode();
        }
    }
    public void createMode(){
        getEt_nameWorldkieCreate().setText("");
       // putDefaultImage();
    }
   /* public void editarMode(WorldkieModel worldkieModel){
        create=false;
        createWorldkie.getEt_nameWorldkieCreate().setText(worldkieModel.getName());
    }*/
   /* private void putDefaultImage(){
        getIB_profile_photo().setImageResource(R.mipmap.default_icon);
        Bitmap bitmap = ((BitmapDrawable) createWorldkie.getIB_profile_photo().getDrawable()).getBitmap();
        personalizarImagen(bitmap);
        getBt_deleteImageRegister().setVisibility(View.INVISIBLE);
    }*/
    public TextInputEditText getEt_nameWorldkieCreate(){
        return et_nameWorldkieCreate;
    }
    public ImageButton getBt_deleteImageRegister(){
        return bt_deleteImageRegister;
    }
    public ImageButton getIB_profile_photo(){
        return bt_chooseImage;
    }
    public Uri getImage(){
        return image;
    }
    public void selectImage(View view){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_CODE);
                return; // Esperar hasta que el usuario otorgue permisos
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
                return; // Esperar hasta que el usuario otorgue permisos
            }
        }String mimeType = "image/gif";
        pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }
    public void personalizarImagen(Bitmap bitmap){
        //Canvas canvas = new Canvas(circularBitmap);
        //bt_chooseImage.setBor

        RoundedBitmapDrawable roundedDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        roundedDrawable.setCircular(true);
        roundedDrawable.setCornerRadius(bitmap.getHeight());
        bt_chooseImage.setImageDrawable(roundedDrawable);
        bt_chooseImage.setBackgroundColor(Color.TRANSPARENT);
        Drawable drawableBorder = getResources().getDrawable(R.drawable.border_register);
        drawableBorder.setTint(Color.RED);
        bt_chooseImage.setBackground(drawableBorder);
        //bt_chooseImage.set
    }
}