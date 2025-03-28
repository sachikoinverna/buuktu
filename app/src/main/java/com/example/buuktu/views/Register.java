package com.example.buuktu.views;


import static com.google.android.gms.common.util.CollectionUtils.listOf;

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
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

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
import com.example.buuktu.bottomsheet.BottomSheetProfilePhotoDefault;
import com.example.buuktu.controllers.RegisterController;
import com.example.buuktu.utils.CheckUtil;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.Calendar;

public class Register extends AppCompatActivity {
    int flag = Intent.FLAG_GRANT_READ_URI_PERMISSION;
    int RESULT_CODE = 0;
    int REQUEST_CODE = 1;
    private FirebaseFirestore db;
    public TextInputEditText dp_birthday,et_nameRegister,et_pronounsRegister,et_userRegister,et_emailRegister,et_passwordRepeat,et_password,et_telephoneRegister;
    public TextView tv_nameRegister,tv_surnameRegister,tv_emailRegister,tv_birthdayRegister,tv_passwordRegister,tv_passwordRepeatRegister,tv_pronounsRegister,tv_usernameRegister,tv_telephoneRegister;
    ImageButton bt_registerToLogin;
    ImageButton bt_chooseImage;
    ImageButton bt_deleteImageRegister;
    ImageButton img_one,img_def,img_gal;
    private Switch tb_privateAccountRegister;
    ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    Calendar calendar;
    int yearC,monthC,dayC;
    String errorMailFormat;
    String dateSelected;
    ImageButton bt_register;
    private FirebaseAuth auth;
    FirebaseFirestore dbFire;
    String UID;
    Uri image;
    RegisterController registerController;
    FirebaseStorage storage = FirebaseStorage.getInstance("gs://buuk-tu-users");

    // Create a child reference
// imagesRef now points to "images"
    //StorageReference imagesRef = storageRef.child("images");

    // Child references can also take paths
// spaceRef now points to "images/space.jpg
// imagesRef still points to "images"
    //StorageReference spaceRef = storageRef.child("images/space.jpg");
    //String connectionString = "mongodb+srv://chikorita:<db_password>@cluster0.zphspah.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        bt_chooseImage = findViewById(R.id.bt_chooseImageRegister);
        Bitmap originalBitmap = ((BitmapDrawable) bt_chooseImage.getDrawable()).getBitmap();
        personalizarImagen(originalBitmap);
        tb_privateAccountRegister = findViewById(R.id.tb_privateAccountRegister);
        pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                    // Callback is invoked after the user selects a media item or closes the
                    // photo picker.
                    if (uri != null) {
                        //registerController.setUri(uri);
                        image = uri;
                        try {
                            //Bitmap image1 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                            ImageDecoder.Source image1 = ImageDecoder.createSource(this.getContentResolver(),uri);
                            Bitmap bitmap = ImageDecoder.decodeBitmap(image1);
                            Bitmap bitmap1 = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
                            bt_chooseImage.setImageBitmap(bitmap1);
                            personalizarImagen(bitmap1);
                            StorageReference userRef = storage.getReference().child("ujlDPggHwenVJNQcUSqO");
                            userRef.child(image.getLastPathSegment()).putFile(image);
                            bt_deleteImageRegister.setVisibility(View.VISIBLE);

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        Log.d("PhotoPicker", "Selected URI: " + uri);
                    } else {
                        Log.d("PhotoPicker", "No media selected");
                    }
                });
        bt_deleteImageRegister = findViewById(R.id.bt_deleteImageRegister);
        auth = FirebaseAuth.getInstance();
        dp_birthday = findViewById(R.id.dp_birthday);
        et_nameRegister = findViewById(R.id.et_nameRegister);
        et_pronounsRegister = findViewById(R.id.et_pronounsRegister);
        et_userRegister = findViewById(R.id.et_userRegister);
        et_emailRegister = findViewById(R.id.et_emailRegister);
        et_password = findViewById(R.id.et_password);
        et_passwordRepeat = findViewById(R.id.et_passwordRepeat);
        et_telephoneRegister = findViewById(R.id.et_telephoneRegister);
        tv_nameRegister = findViewById(R.id.tv_errorNameRegister);
        tv_emailRegister = findViewById(R.id.tv_errorEmailRegister);
        tv_birthdayRegister = findViewById(R.id.tv_birthdayError);
        tv_passwordRegister = findViewById(R.id.et_password);
        tv_passwordRepeatRegister = findViewById(R.id.tv_errorPasswordRepeatRegister);
        tv_pronounsRegister = findViewById(R.id.tv_errorPronounsRegister);
        tv_usernameRegister = findViewById(R.id.tv_errorUsernameRegister);
        tv_telephoneRegister = findViewById(R.id.tv_errorNumberRegister);
        bt_registerToLogin = findViewById(R.id.bt_registerToLogin);
        CheckUtil.setErrorMessage(null, tv_nameRegister);
         CheckUtil.setErrorMessage(null, tv_emailRegister);
        CheckUtil.setErrorMessage(null, tv_birthdayRegister);
        CheckUtil.setErrorMessage(null, tv_passwordRegister);
        CheckUtil.setErrorMessage(null, tv_passwordRepeatRegister);
        CheckUtil.setErrorMessage(null, tv_birthdayRegister);
        CheckUtil.setErrorMessage("", tv_pronounsRegister);
        CheckUtil.setErrorMessage(null, tv_usernameRegister);
        CheckUtil.setErrorMessage(null, tv_telephoneRegister);
        calendar = Calendar.getInstance();
        yearC = calendar.get(Calendar.YEAR);
        monthC = calendar.get(Calendar.MONTH);
        dayC = calendar.get(Calendar.DAY_OF_MONTH);
        registerController = new RegisterController(this);
        et_nameRegister.setOnFocusChangeListener(registerController);
        et_password.setOnFocusChangeListener(registerController);
        et_passwordRepeat.setOnFocusChangeListener(registerController);
        bt_registerToLogin.setOnClickListener(registerController);
        dp_birthday.setOnClickListener(registerController);
        db = FirebaseFirestore.getInstance();
        bt_register = findViewById(R.id.bt_register);
        bt_deleteImageRegister.setOnClickListener(registerController);
        if (auth.getCurrentUser()!=null){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
    public ImageButton getBt_deleteImageRegister(){
        return bt_deleteImageRegister;
    }
    private void setListeners() {

    }

    public Switch getTb_privateAccountRegister() {
        return tb_privateAccountRegister;
    }

    public ImageButton getBt_chooseImage() {
        return bt_chooseImage;
    }
    public TextInputEditText getDp_birthday() {
        return dp_birthday;
    }

    public TextInputEditText getEt_nameRegister() {
        return et_nameRegister;
    }

    public TextInputEditText getEt_passwordRegister() {
        return et_password;
    }

    public TextInputEditText getEt_passwordRepeat() {
        return et_passwordRepeat;
    }

    public TextInputEditText getEt_pronounsRegister() {
        return et_pronounsRegister;
    }

    public TextInputEditText getEt_userRegister() {
        return et_userRegister;
    }

    public TextInputEditText getEt_emailRegister() {
        return et_emailRegister;
    }

    public TextInputEditText getEt_telephoneRegister() {
        return et_telephoneRegister;
    }

    public TextView getTv_nameRegister() {
        return tv_nameRegister;
    }

    public TextView getTv_emailRegister() {
        return tv_emailRegister;
    }

    public TextView getTv_birthdayRegister() {
        return tv_birthdayRegister;
    }

    public TextView getTv_passwordRegister() {
        return tv_passwordRegister;
    }

    public TextView getTv_passwordRepeatRegister() {
        return tv_passwordRepeatRegister;
    }

    public TextView getTv_pronounsRegister() {
        return tv_pronounsRegister;
    }

    public TextView getTv_usernameRegister() {
        return tv_usernameRegister;
    }

    public TextView getTv_telephoneRegister() {
        return tv_telephoneRegister;
    }
    public ImageButton getIB_profile_photo(){
        return bt_chooseImage;
    }
    public Uri getImage(){
        return image;
    }
    public void selectImage(View view){/*
        BottomSheetProfilePhoto bottomSheetProfilePhoto = new BottomSheetProfilePhoto();
        bottomSheetProfilePhoto.show(getSupportFragmentManager(),"BottomSheetProfilePhoto");*/
        BottomSheetProfilePhotoDefault bottomSheetProfilePhotoDefault = new BottomSheetProfilePhotoDefault();
        bottomSheetProfilePhotoDefault.show(getSupportFragmentManager(),"BottomSheetProfilePhotoDefault");
       /* Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_options_images);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
        }

        Dialog dialog2 = new Dialog(this);
        dialog2.setContentView(R.layout.dialog_options_images);
        dialog2.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog2.setCancelable(false);
        dialog2.getWindow().getAttributes().windowAnimations = R.style.animation;
        */
        /*img_one = dialog.findViewById(R.id.ib_imgOne);
        */
        /*img_gal = dialog.findViewById(R.id.ib_gallery);
        img_def = dialog.findViewById(R.id.ib_default);

        if (img_one != null) {
            img_one.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(Register.this, "Imagen seleccionada", Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (img_gal != null) {
            img_gal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectImageGallery();
                }
            });
        }

        if (img_def != null) {
            img_def.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog2.show();
                }
            });
        }
        dialog.show();*/

    }
    public void selectImageGallery(){
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
        bt_chooseImage.setPadding(15, 15, 15, 15); // Añadir padding para el borde visible
        bt_chooseImage.setScaleType(ImageView.ScaleType.CENTER_CROP); // Ajusta la imagen para que quede dentro del borde
        //bt_chooseImage.set
    }
    /*@Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                }  else {
                    // Explain to the user that the feature is unavailable because
                    // the feature requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                }
                return;
        }*/
        // Other 'case' lines to check for other
        // permissions this app might request.
//    }
}
//}
