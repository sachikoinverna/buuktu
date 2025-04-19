package com.example.buuktu.views;


import static java.time.LocalDate.parse;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.buuktu.R;
import com.example.buuktu.bottomsheet.BottomSheetProfilePhoto;
import com.example.buuktu.models.UserkieModel;
import com.example.buuktu.utils.BitmapUtils;
import com.example.buuktu.utils.CheckUtil;
import com.example.buuktu.utils.DrawableUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Register extends AppCompatActivity implements View.OnFocusChangeListener {
    int flag = Intent.FLAG_GRANT_READ_URI_PERMISSION;
    int RESULT_CODE = 0;
    int REQUEST_CODE = 1;
    Calendar calendar;
    int yearC,monthC,dayC;
    private FirebaseFirestore db;
    public TextInputEditText dp_birthday,et_nameRegister,et_pronounsRegister,et_userRegister,et_emailRegister,et_passwordRepeat,et_password,et_telephoneRegister;
    public TextView tv_nameRegister,tv_surnameRegister,tv_emailRegister,tv_birthdayRegister,tv_passwordRegister,tv_passwordRepeatRegister,tv_pronounsRegister,tv_usernameRegister,tv_telephoneRegister;
    ImageButton bt_chooseImage;
    ImageButton img_one,img_def,img_gal;
    private Switch tb_privateAccountRegister;
    ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    String errorMailFormat;
    String dateSelected;
    ImageButton bt_register;
    private FirebaseAuth auth;
    FirebaseFirestore dbFire;
    String UID;
    Uri image;
    String email,username,password;
    Boolean privateAccount;
    FirebaseStorage storage = FirebaseStorage.getInstance("gs://buuk-tu-users");
    TextView tv_registerButton,tv_registerToLoginButton;
    BottomSheetProfilePhoto bottomSheetProfilePhoto;
    ImageButton imageButtonActualBottomSheet;
    String source;
    //ArrayList<int>
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
        source = "app";
        bt_chooseImage = findViewById(R.id.bt_chooseImageRegister);
        tb_privateAccountRegister = findViewById(R.id.tb_privateAccountRegister);
        auth = FirebaseAuth.getInstance();
        initComponents();
        CheckUtil.setErrorMessage(null, tv_nameRegister);
         CheckUtil.setErrorMessage(null, tv_emailRegister);
        CheckUtil.setErrorMessage(null, tv_birthdayRegister);
//        CheckUtil.setErrorMessage(null, tv_passwordRegister);
 //       CheckUtil.setErrorMessage(null, tv_passwordRepeatRegister);
        CheckUtil.setErrorMessage(null, tv_birthdayRegister);
        CheckUtil.setErrorMessage("", tv_pronounsRegister);
        CheckUtil.setErrorMessage(null, tv_usernameRegister);
        CheckUtil.setErrorMessage(null, tv_telephoneRegister);
        et_nameRegister.setOnFocusChangeListener(this);
        et_password.setOnFocusChangeListener(this);
        et_passwordRepeat.setOnFocusChangeListener(this);
        db = FirebaseFirestore.getInstance();
        if (auth.getCurrentUser()!=null){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        /*auth.signInWithEmailAndPassword("chikoritaxserperior@gmail.com","135sEt754asdtpm*").addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });*/


        DrawableUtils.personalizarImagenCircleButton(this,DrawableUtils.drawableToBitmap(bt_chooseImage.getDrawable()),bt_chooseImage,R.color.brownBrown);
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSource() {
        return source;
    }

    public ImageButton getImageButtonActualBottomSheet(){
        return imageButtonActualBottomSheet;
    }
    public Uri getImageUri(){
        return image;
    }
    public void setImageUri(Uri image){
        this.image=image;
    }
    public void setImageButtonActualBottomSheet(int id){

            }
    private void initComponents(){
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
        tv_passwordRepeatRegister = findViewById(R.id.tv_errorPasswordRepeatRegister);
        tv_pronounsRegister = findViewById(R.id.tv_errorPronounsRegister);
        tv_usernameRegister = findViewById(R.id.tv_errorUsernameRegister);
        tv_telephoneRegister = findViewById(R.id.tv_errorNumberRegister);
        tv_registerButton = findViewById(R.id.tv_registerButton);
        tv_registerToLoginButton = findViewById(R.id.tv_registerToLoginButton);
        calendar = Calendar.getInstance();
        yearC = calendar.get(Calendar.YEAR);
        monthC = calendar.get(Calendar.MONTH);
        dayC = calendar.get(Calendar.DAY_OF_MONTH);
        bottomSheetProfilePhoto = new BottomSheetProfilePhoto();
    }
    private void setListeners() {

    }
    public void setSelectedProfilePhoto(Drawable image){
        bt_chooseImage.setImageDrawable(image);
    }
public Drawable getSelectedProfilePhoto()
{
    return bt_chooseImage.getDrawable();
}
    public ImageButton getBt_chooseImage() {
        return bt_chooseImage;
    }

    public ImageButton getIB_profile_photo(){
        return bt_chooseImage;
    }
    public void selectImage(View view){
       bottomSheetProfilePhoto.show(getSupportFragmentManager(),"BottomSheetProfilePhoto");
    }

    @Override
    public void onFocusChange(View v, boolean b) {
        final int field = v.getId();
        if (field == R.id.et_nameRegister) {
            if (!b) {
                handlerCheckName();
            } else if (b) {
                CheckUtil.setErrorMessage(null, et_nameRegister);
            }
        } else if (field == R.id.dp_birthday) {
            if (!b) {
                handlerCheckBirthdayDate();
            } else if (b) {
                CheckUtil.setErrorMessage(null, tv_birthdayRegister);
            }
        } else if (field == R.id.et_userRegister) {
            if (!b) {
                handlerCheckUser();
            } else if (b) {
                CheckUtil.setErrorMessage(null, tv_usernameRegister);
            }
        } else if (field == R.id.et_pronounsRegister) {
            if (!b) {
                handlerCheckPronouns();
            } else if (b) {
                CheckUtil.setErrorMessage(null, tv_pronounsRegister);
            }
        } else if (field == R.id.et_emailRegister) {
            if (!b) {
                handlerCheckEmail();
            } else if (b) {
                CheckUtil.setErrorMessage(null, tv_emailRegister);
            }
        } else if (field == R.id.et_password) {
            if (!b) {
                handlerCheckPassword();
            } else if (b) {
                CheckUtil.setErrorMessage(null, tv_passwordRegister);
            }
        } else if (field == R.id.et_passwordRepeat) {
            if (!b) {
                handlerCheckPasswordRepeat();
            } else if (b) {
                CheckUtil.setErrorMessage(null, tv_passwordRepeatRegister);
            }
        } else if (field == R.id.et_telephoneRegister) {
            if (!b) {
                handlerCheckTelephone();
            } else if (b) {
                CheckUtil.setErrorMessage(null, tv_telephoneRegister);
            }
        }
    }
    private boolean handlerCheckName(){
        if(CheckUtil.checkTextEmpty(et_nameRegister)){
            CheckUtil.setErrorMessage(getString(R.string.nameErrorEmpty),tv_nameRegister);
            return false;
        }else if (!CheckUtil.checkNumbers(et_nameRegister.getText().toString())){
            CheckUtil.setErrorMessage(getString(R.string.numberErrorTextField),tv_nameRegister);
            return false;
        }
        return true;
    }
    private boolean handlerCheckPronouns(){
        if(CheckUtil.checkTextEmpty(et_pronounsRegister)){
            CheckUtil.setErrorMessage(getString(R.string.pronounsErrorEmpty),tv_pronounsRegister);
            return false;
        }
        return true;
    }
    private boolean handlerCheckPassword(){
        if (CheckUtil.checkTextEmpty(et_password)){
            CheckUtil.setErrorMessage(getString(R.string.passwordErrorEmpty),tv_passwordRegister);
        } else if (et_password.getText().toString().length()<8) {
            CheckUtil.setErrorMessage(String.valueOf((R.string.passwordTooShort)),tv_passwordRegister);
            return false;
        }else if(!CheckUtil.checkSpecialCharacter(et_password.getText().toString())){
            CheckUtil.setErrorMessage(getString(R.string.passwordErrorSpecialChar),tv_passwordRegister);
            return false;
        } else if (!CheckUtil.checkUppercase(et_password.getText().toString())){
            CheckUtil.setErrorMessage(getString(R.string.passwordErrorUppercase),tv_passwordRegister);
            return false;
        }
        CheckUtil.setErrorMessage(null,tv_passwordRegister);
        return true;
    }
    public boolean handlerCheckPasswordRepeat(){
        if (!et_passwordRepeat.equals(et_password)) {
            CheckUtil.setErrorMessage(getString(R.string.passwordErrorRepeat),tv_passwordRepeatRegister);
        }
        CheckUtil.setErrorMessage(null,tv_passwordRepeatRegister);
        return true;
    }
    private boolean handlerCheckTelephone(){
        if(CheckUtil.checkTextEmpty(et_telephoneRegister)){
            CheckUtil.setErrorMessage(getString(R.string.telephoneErrorEmpty),tv_telephoneRegister);
            return false;
        }
        return true;
    }
    private boolean handlerCheckEmail(){
        if(CheckUtil.checkTextEmpty(et_emailRegister)){
            CheckUtil.setErrorMessage(getString(R.string.emailErrorEmpty),tv_emailRegister);
            return false;
        }
        if (!CheckUtil.checkEmailStructure(et_emailRegister.getText().toString())){
            CheckUtil.setErrorMessage(getString(R.string.emailErrorFormat),tv_emailRegister);
            return false;
        }
        return true;
    }
    public boolean handlerCheckUser(){
        if(CheckUtil.checkTextEmpty(et_userRegister)){
            CheckUtil.setErrorMessage(getString(R.string.userErrorEmpty),tv_usernameRegister);
            return false;
        } else if (CheckUtil.checkExistentUsername(et_userRegister.getText().toString())) {
            CheckUtil.setErrorMessage(getString(R.string.userErrorExists),tv_usernameRegister);
            return false;
        }
        return true;
    }
    private boolean handlerCheckBirthdayDate(){
        if(CheckUtil.checkTextEmpty(dp_birthday)){
            CheckUtil.setErrorMessage(getString(R.string.birthdayErrorEmpty),dp_birthday);
            return false;
        }
        return true;
    }
    public void handlerGoToRegister(View view){
        Intent intent = new Intent(this,Login.class);
        startActivity(intent);
    }
    private boolean checkAllFields(){
        if(handlerCheckName() && handlerCheckBirthdayDate() && handlerCheckUser() && handlerCheckEmail() && handlerCheckPassword()&& handlerCheckPasswordRepeat()&& handlerCheckPronouns()){
            return true;
        }
        return false;
    }
    public void addDataToFirestore(View view) {
        email = et_emailRegister.getText().toString();
        password = et_password.getText().toString();
        privateAccount = tb_privateAccountRegister.isChecked();
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    Toast.makeText(getApplicationContext(), "Signup Successful", Toast.LENGTH_SHORT).show();
                    CollectionReference dbUsers = db.collection("Users");
                    LocalDate localDate = parse("2000-01-01");
                    ZoneId zoneId = ZoneId.systemDefault(); // Or specify a specific zone
                    Instant instant = localDate.atStartOfDay(zoneId).toInstant();
                    //UserkieModel user = new UserkieModel(register.getEt_emailRegister().getText().toString(), task.getResult().getUser().getUid(), register.getEt_nameRegister().getText().toString(), register.getEt_surnameRegister().getText().toString(), register.getEt_pronounsRegister().getText().toString(), Date.from(instant),register.getEt_userRegister().getText().toString(), register.getEt_telephoneRegister().getText().toString());
                    UserkieModel user;
                    if(source.equals("app")) {
                        user = new UserkieModel(email, task.getResult().getUser().getUid(), et_nameRegister.getText().toString(), et_pronounsRegister.getText().toString(), Date.from(instant), et_userRegister.getText().toString(), et_telephoneRegister.getText().toString(), BitmapUtils.drawableToBitmap(getBt_chooseImage().getDrawable()).equals(R.mipmap.default_icon), privateAccount);
                    }else{
                        user = new UserkieModel(email, task.getResult().getUser().getUid(), et_nameRegister.getText().toString(), et_pronounsRegister.getText().toString(), Date.from(instant), et_userRegister.getText().toString(), et_telephoneRegister.getText().toString(), BitmapUtils.drawableToBitmap(getBt_chooseImage().getDrawable()).equals(R.mipmap.default_icon), privateAccount);

                    }
                    // below method is use to add data to Firebase Firestore.
                    DocumentReference documentRef = dbUsers.document(task.getResult().getUser().getUid());

                    //.document(uid)
                    documentRef.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(), "Your Course has been added to Firebase Firestore", Toast.LENGTH_SHORT).show();


                            if(getIB_profile_photo().getDrawable().equals(R.mipmap.default_icon)){
                                // StorageReference userRef = storage.getReference().child("ajYrQVbzQAdW7mgjIF3fxNJsIjF3");
                                //register.getIB_profile_photo().setDrawingCacheEnabled(true);
                                //register.getIB_profile_photo().buildDrawingCache();
                                //Bitmap bitmap = ((BitmapDrawable) register.getIB_profile_photo().getDrawable()).getBitmap();
                                // ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                //bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                //byte[] data = baos.toByteArray();
                                //Uri file = Uri.fromFile(new File(String.valueOf(R.mipmap.default_icon)));
                                //userRef.putBytes(data);
                            }else {
                                StorageReference userRef = storage.getReference().child(task.getResult().getUser().getUid());
                              //  userRef.child(register.getImage().getLastPathSegment()).putFile(register.getImage());
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // this method is called when the data addition process is failed.
                            // displaying a toast message when data addition is failed.
                            //Toast.makeText(register, "Fail to add course \n" + e, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    switch (task.getException().getMessage()) {
                        case "auth/email-already-in-use":
                            //Toast.makeText(register, "Ya existe una cuenta con el correo electronico", Toast.LENGTH_LONG).show();
                            break;
                        default:
                            break;
                    }
                   // Toast.makeText(register, "Signup Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void showDatePickerDialog(View view)
    {
        DatePickerDialog date = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                dp_birthday.setText(""+day+"/"+(month+1)+"/"+year);
                dayC= day;
                monthC= month;
                yearC= year;
            }
        },yearC,monthC,dayC);
        date.show();
    }

}
