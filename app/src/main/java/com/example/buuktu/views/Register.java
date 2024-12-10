package com.example.buuktu.views;


import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_MEDIA_IMAGES;
import static android.Manifest.permission.READ_MEDIA_VIDEO;
import static android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.google.android.gms.common.util.CollectionUtils.listOf;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
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
import com.example.buuktu.controllers.RegisterController;
import com.example.buuktu.utils.CheckUtil;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;

public class Register extends AppCompatActivity {
    int flag = Intent.FLAG_GRANT_READ_URI_PERMISSION;
    int RESULT_CODE = 0;
    int REQUEST_CODE = 1;
    private FirebaseFirestore db;
    public TextInputEditText dp_birthday;
    public TextInputEditText et_nameRegister;
    public TextInputEditText et_surnameRegister;
    public TextInputEditText et_pronounsRegister;
    public TextInputEditText et_userRegister;
    public TextInputEditText et_emailRegister;
    public TextInputEditText et_passwordRepeat;
    public TextInputEditText et_password;
    public TextInputEditText et_telephoneRegister;
    public TextView tv_nameRegister;
    public TextView tv_surnameRegister;
    public TextView tv_emailRegister;
    public TextView tv_birthdayRegister;
    public TextView tv_passwordRegister;
    public TextView tv_passwordRepeatRegister;
    public TextView tv_pronounsRegister;
    public TextView tv_usernameRegister;
    public TextView tv_telephoneRegister;
    ImageButton bt_registerToLogin;
    Calendar calendar;
    int yearC;
    int monthC;
    int dayC;
    String errorMailFormat;
    String dateSelected;
    ImageButton bt_register;
    private FirebaseAuth auth;
    FirebaseFirestore dbFire;
    String UID;
    Uri image;
    RegisterController registerController;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                // Callback is invoked after the user selects a media item or closes the
                // photo picker.
                if (uri != null) {
                    //registerController.setUri(uri);
                    image = uri;
                    Log.d("PhotoPicker", "Selected URI: " + uri);
                } else {
                    Log.d("PhotoPicker", "No media selected");
                }
            });
    // Create a child reference
// imagesRef now points to "images"
    //StorageReference imagesRef = storageRef.child("images");

    // Child references can also take paths
// spaceRef now points to "images/space.jpg
// imagesRef still points to "images"
    //StorageReference spaceRef = storageRef.child("images/space.jpg");
    private ImageButton bt_chooseImage;
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
        auth = FirebaseAuth.getInstance();
        dp_birthday = findViewById(R.id.dp_birthday);
        et_nameRegister = findViewById(R.id.et_nameRegister);
        et_surnameRegister = findViewById(R.id.et_surnameRegister);
        et_pronounsRegister = findViewById(R.id.et_pronounsRegister);
        et_userRegister = findViewById(R.id.et_userRegister);
        et_emailRegister = findViewById(R.id.et_emailRegister);
        et_password = findViewById(R.id.et_password);
        et_passwordRepeat = findViewById(R.id.et_passwordRepeat);
        et_telephoneRegister = findViewById(R.id.et_telephoneRegister);
        tv_nameRegister = findViewById(R.id.tv_errorNameRegister);
        tv_surnameRegister = findViewById(R.id.tv_errorSurnameRegister);
        tv_emailRegister = findViewById(R.id.tv_errorEmailRegister);
        tv_birthdayRegister = findViewById(R.id.tv_birthdayError);
        tv_passwordRegister = findViewById(R.id.tv_errorPasswordRegister);
        tv_passwordRepeatRegister = findViewById(R.id.tv_errorPasswordRepeatRegister);
        tv_pronounsRegister = findViewById(R.id.tv_errorPronounsRegister);
        tv_usernameRegister = findViewById(R.id.tv_errorUsernameRegister);
        tv_telephoneRegister = findViewById(R.id.tv_errorUsernameRegister);
        bt_registerToLogin = findViewById(R.id.bt_registerToLogin);
        CheckUtil.setErrorMessage(null, tv_nameRegister);
        CheckUtil.setErrorMessage(null, tv_surnameRegister);
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
        personalizarImagen();



    }

    private void setListeners() {

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

    public TextInputEditText getEt_surnameRegister() {
        return et_surnameRegister;
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

    public TextView getTv_surnameRegister() {
        return tv_surnameRegister;
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
    public void selectImage(View view){
        /*if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_MEDIA_IMAGES) ==
                PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.
            performAction(...);
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(
                this, Manifest.permission.REQUESTED_PERMISSION)) {
            // In an educational UI, explain to the user why your app requires this
            // permission for a specific feature to behave as expected, and what
            // features are disabled if it's declined. In this UI, include a
            // "cancel" or "no thanks" button that lets the user continue
            // using your app without granting the permission.
            showInContextUI(...);
        } else {
            // You can directly ask for the permission.
            // The registered ActivityResultCallback gets the result of this request.
            requestPermissionLauncher.launch(
                    Manifest.permission.REQUESTED_PERMISSION);
        }*/
        if (
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                        (
                                ContextCompat.checkSelfPermission(this, READ_MEDIA_IMAGES) == PERMISSION_GRANTED ||
                                        ContextCompat.checkSelfPermission(this, READ_MEDIA_VIDEO) == PERMISSION_GRANTED
                        )
        ) {
            // Full access on Android 13 (API level 33) or higher
        } else if (
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE &&
                        ContextCompat.checkSelfPermission(this, READ_MEDIA_VISUAL_USER_SELECTED) == PERMISSION_GRANTED
        ) {
            String mimeType = "image/gif";
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(new ActivityResultContracts.PickVisualMedia.SingleMimeType(mimeType))
                    .build());
        }  else if (ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) == PERMISSION_GRANTED) {
            String mimeType = "image/gif";
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(new ActivityResultContracts.PickVisualMedia.SingleMimeType(mimeType))
                    .build());
        } else {
            // Access denied
        }
                String mimeType = "image/gif";
                pickMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(new ActivityResultContracts.PickVisualMedia.SingleMimeType(mimeType))
                        .build());


        //StorageReference userRef = storage.getReference().child("ujlDPggHwenVJNQcUSqO");
        //userRef.child(image.getLastPathSegment()).putFile(image);
    }
    public void personalizarImagen(){
        //Canvas canvas = new Canvas(circularBitmap);
        //bt_chooseImage.setBor
        Bitmap originalBitmap = ((BitmapDrawable) bt_chooseImage.getDrawable()).getBitmap();
        RoundedBitmapDrawable roundedDrawable = RoundedBitmapDrawableFactory.create(getResources(), originalBitmap);
        roundedDrawable.setCircular(true);
        roundedDrawable.setCornerRadius(originalBitmap.getHeight());
        bt_chooseImage.setImageDrawable(roundedDrawable);
        bt_chooseImage.setBackgroundColor(Color.TRANSPARENT);
        Drawable drawableBorder = getResources().getDrawable(R.drawable.border_register);
        drawableBorder.setTint(Color.RED);
        bt_chooseImage.setBackground(drawableBorder);
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
