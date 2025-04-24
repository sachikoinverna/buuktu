package com.example.buuktu.views;


import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.example.buuktu.R;
import com.example.buuktu.bottomsheet.BottomSheetProfilePhoto;
import com.example.buuktu.dialogs.CreateEditGeneralDialog;
import com.example.buuktu.models.UserkieModel;
import com.example.buuktu.utils.CheckUtil;
import com.example.buuktu.utils.DrawableUtils;
import com.example.buuktu.utils.UIUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class Register extends AppCompatActivity implements View.OnFocusChangeListener {
    int flag = Intent.FLAG_GRANT_READ_URI_PERMISSION;
    int RESULT_CODE = 0;
    int REQUEST_CODE = 1;
    Calendar calendar;
    int yearC, monthC, dayC;
    private FirebaseFirestore db;
    public TextInputEditText dp_birthday, et_nameRegister, et_pronounsRegister, et_userRegister, et_emailRegister, et_passwordRepeat, et_password, et_telephoneRegister;
    public TextView tv_nameRegister, tv_emailRegister, tv_birthdayRegister, tv_passwordRegister, tv_passwordRepeatRegister, tv_pronounsRegister, tv_usernameRegister, tv_telephoneRegister;
    ImageButton bt_chooseImage;
    ImageButton img_one, img_def, img_gal;
    private Switch tb_privateAccountRegister;
    String errorMailFormat;
    String dateSelected;
    ImageButton bt_register;
    private FirebaseAuth auth;
    FirebaseFirestore dbFire;
    String UID;
    Uri image;
    String extension;
    String email, username, password;
    Boolean privateAccount;
    FirebaseStorage storage = FirebaseStorage.getInstance("gs://buuk-tu-users");
    TextView tv_registerButton, tv_registerToLoginButton;
    BottomSheetProfilePhoto bottomSheetProfilePhoto;
    ImageButton imageButtonActualBottomSheet;
    String source, photo_id, number, pronouns, name;
    boolean photo_default;
    Date birthday;
    TextInputLayout dp_birthdayFilled;

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
        UIUtils.hideSystemUI(this);

        initComponents();
        setClean();
        setListeners();

        db = FirebaseFirestore.getInstance();
        if (auth.getCurrentUser() != null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        DrawableUtils.personalizarImagenCircleButton(this, DrawableUtils.drawableToBitmap(bt_chooseImage.getDrawable()), bt_chooseImage, R.color.brownBrown);
    }

    private void setClean() {
        CheckUtil.setErrorMessage(null, tv_nameRegister);
        CheckUtil.setErrorMessage(null, tv_emailRegister);
        CheckUtil.setErrorMessage(null, tv_birthdayRegister);
        CheckUtil.setErrorMessage(null, tv_passwordRegister);
        CheckUtil.setErrorMessage(null, tv_passwordRepeatRegister);
        CheckUtil.setErrorMessage(null, tv_pronounsRegister);
        CheckUtil.setErrorMessage(null, tv_usernameRegister);
        CheckUtil.setErrorMessage(null, tv_telephoneRegister);
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSource() {
        return source;
    }

    public ImageButton getImageButtonActualBottomSheet() {
        return imageButtonActualBottomSheet;
    }

    public Uri getImageUri() {
        return image;
    }

    public void setImageUri(Uri image) {
        this.image = image;
    }

    public void setImageButtonActualBottomSheet(int id) {

    }

    private void initComponents() {
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
        tv_passwordRegister = findViewById(R.id.tv_errorPasswordRegister);
        tv_passwordRepeatRegister = findViewById(R.id.tv_errorPasswordRepeatRegister);
        tv_pronounsRegister = findViewById(R.id.tv_errorPronounsRegister);
        tv_usernameRegister = findViewById(R.id.tv_errorUsernameRegister);
        tv_telephoneRegister = findViewById(R.id.tv_errorNumberRegister);
        tv_registerButton = findViewById(R.id.tv_registerButton);
        tv_registerToLoginButton = findViewById(R.id.tv_registerToLoginButton);
        bt_chooseImage = findViewById(R.id.bt_chooseImageRegister);
        tb_privateAccountRegister = findViewById(R.id.tb_privateAccountRegister);
        dp_birthdayFilled = findViewById(R.id.dp_birthdayFilled);
        calendar = Calendar.getInstance();
        yearC = calendar.get(Calendar.YEAR);
        monthC = calendar.get(Calendar.MONTH);
        dayC = calendar.get(Calendar.DAY_OF_MONTH);
        bottomSheetProfilePhoto = new BottomSheetProfilePhoto();
        source = "app";
        auth = FirebaseAuth.getInstance();

    }

    private void setListeners() {
        et_nameRegister.setOnFocusChangeListener(this);
        et_password.setOnFocusChangeListener(this);
        et_passwordRepeat.setOnFocusChangeListener(this);
        et_userRegister.setOnFocusChangeListener(this);
        et_emailRegister.setOnFocusChangeListener(this);
        et_telephoneRegister.setOnFocusChangeListener(this);
    }

    public void setSelectedProfilePhoto(Drawable image) {
        bt_chooseImage.setImageDrawable(image);
    }

    public Drawable getSelectedProfilePhoto() {
        return bt_chooseImage.getDrawable();
    }

    public ImageButton getBt_chooseImage() {
        return bt_chooseImage;
    }

    public ImageButton getIB_profile_photo() {
        return bt_chooseImage;
    }

    public void selectImage(View view) {
        bottomSheetProfilePhoto.show(getSupportFragmentManager(), "BottomSheetProfilePhoto");
    }

    @Override
    public void onFocusChange(View v, boolean b) {
        final int field = v.getId();
        if (!b){
            if (field == R.id.et_nameRegister) {
                CheckUtil.handlerCheckName(this,et_nameRegister,tv_nameRegister);
            } else if (field == R.id.dp_birthday) {
                CheckUtil.handlerCheckBirthdayDate(this,dp_birthday,tv_birthdayRegister);
            } else if (field == R.id.et_userRegister) {
                CheckUtil.handlerCheckUser(this,et_userRegister,tv_usernameRegister);
            } else if (field == R.id.et_pronounsRegister) {
                CheckUtil.handlerCheckPronouns(this,et_pronounsRegister,tv_pronounsRegister);
            } else if (field == R.id.et_emailRegister) {
                CheckUtil.handlerCheckEmail(this,et_emailRegister,tv_emailRegister);
            } else if (field == R.id.et_password) {
                CheckUtil.handlerCheckPassword(this,et_password,tv_passwordRegister);
            } else if (field == R.id.et_passwordRepeat) {
                CheckUtil.handlerCheckPasswordRepeat(this,et_passwordRepeat,et_password,tv_passwordRepeatRegister);
            } else if (field == R.id.et_telephoneRegister) {
                CheckUtil.handlerCheckTelephone(this,et_telephoneRegister,tv_telephoneRegister);
            }
        } else {
            if (field == R.id.et_nameRegister) {
                CheckUtil.setErrorMessage(null, et_nameRegister);
            } else if (field == R.id.dp_birthday) {
                CheckUtil.setErrorMessage(null, tv_birthdayRegister);
            } else if (field == R.id.et_userRegister) {
                CheckUtil.setErrorMessage(null, tv_usernameRegister);
            } else if (field == R.id.et_pronounsRegister) {
                CheckUtil.setErrorMessage(null, tv_pronounsRegister);
            } else if (field == R.id.et_emailRegister) {
                CheckUtil.setErrorMessage(null, tv_emailRegister);
            } else if (field == R.id.et_password) {
                CheckUtil.setErrorMessage(null, tv_passwordRegister);
            } else if (field == R.id.et_passwordRepeat) {
                CheckUtil.setErrorMessage(null, tv_passwordRepeatRegister);
            } else if (field == R.id.et_telephoneRegister) {
                CheckUtil.setErrorMessage(null, tv_telephoneRegister);
            }
        }
    }

    public void handlerGoToRegister(View view) {
        startActivity(new Intent(this, Login.class));
    }

    private boolean checkAllFields() {
        boolean[] allValid = {true}; // usamos un array para modificarlo dentro de lambda

        if (!CheckUtil.handlerCheckName(this, et_nameRegister, tv_nameRegister)) {
            allValid[0] = false;
        }
        if (!CheckUtil.handlerCheckBirthdayDate(this, dp_birthday, tv_birthdayRegister)) {
            allValid[0] = false;
        }
        if (!CheckUtil.handlerCheckUser(this, et_userRegister, tv_usernameRegister)){
            allValid[0] = false;
        }
        if (!CheckUtil.handlerCheckEmail(this, et_emailRegister, tv_emailRegister)) {
            allValid[0] = false;
        }

        if (!CheckUtil.handlerCheckPassword(this, et_password, tv_passwordRegister)) {
            allValid[0] = false;
        }
        if (!CheckUtil.handlerCheckPasswordRepeat(this, et_passwordRepeat, et_password, tv_passwordRepeatRegister)) {
            allValid[0] = false;
        }
        if (!CheckUtil.handlerCheckPronouns(this, et_pronounsRegister, tv_pronounsRegister)) {
            allValid[0] = false;
        }
        return allValid[0];

    }

    public void addDataToFirestore(View view) {

        if (checkAllFields()) {
            CreateEditGeneralDialog dialog = new CreateEditGeneralDialog(getApplicationContext(), "Hola");

            TextView tv_title = dialog.findViewById(R.id.tv_text_create_edit);

            LottieAnimationView animationView = dialog.findViewById(R.id.anim_create_edit);
            animationView.setAnimation(R.raw.reading_anim);
            animationView.playAnimation();
            dialog.show();
            photo_id = bt_chooseImage.getTag().toString();
            email = et_emailRegister.getText().toString();
            password = et_password.getText().toString();
            privateAccount = tb_privateAccountRegister.isChecked();
            username = et_userRegister.getText().toString();
            pronouns = et_pronounsRegister.getText().toString();
            name = et_nameRegister.getText().toString();
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {


                        Toast.makeText(getApplicationContext(), "Signup Successful", Toast.LENGTH_SHORT).show();
                        CollectionReference dbUsers = db.collection("Users");
                        UserkieModel userkieModel;
                        if (source.equals("app")) {

                            userkieModel = new UserkieModel(photo_id, privateAccount, true, email, number, username, new Timestamp(birthday), pronouns, name);
                        } else {
                            userkieModel = new UserkieModel(name, pronouns, new Timestamp(birthday), username, number, email, false, privateAccount);

                        }
                        // below method is use to add data to Firebase Firestore.
                        DocumentReference documentRef = dbUsers.document(task.getResult().getUser().getUid());

                        //.document(uid)
                        documentRef.set(userkieModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getApplicationContext(), "Your Course has been added to Firebase Firestore", Toast.LENGTH_SHORT).show();

                                if (source.equals("device")) {
                                    StorageReference userRef = storage.getReference().child(task.getResult().getUser().getUid());
                                    userRef.child("profile" + getExtensionFromUri(image)).putFile(image);
                                    tv_title.setText("Creado");
                                    animationView.setAnimation(R.raw.success_anim);
                                    animationView.playAnimation();
                                    Completable.timer(5, TimeUnit.SECONDS)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(() -> {
                                                animationView.setVisibility(View.GONE);
                                                dialog.dismiss();
                                            });
                                }
                            }

                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                animationView.setAnimation(R.raw.fail_anim);
                                tv_title.setText("No creado");
                                animationView.playAnimation();
                                Completable.timer(5, TimeUnit.SECONDS)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(() -> {
                                            animationView.setVisibility(View.GONE);
                                            dialog.dismiss();
                                        });
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
    }

    private String getExtensionFromUri(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String type = cr.getType(uri);
        return "." + mime.getExtensionFromMimeType(type);
    }

    public void showDatePickerDialog(View view) {
        DatePickerDialog date = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                dp_birthday.setText("" + day + "/" + (month + 1) + "/" + year);
                dp_birthdayFilled.setHintEnabled(true); // este es el TextInputLayout
                dayC = day;
                monthC = month;
                yearC = year;
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day); // Configura el calendario con la fecha seleccionada
                birthday = calendar.getTime();
            }
        }, yearC, monthC, dayC);
        date.show();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        UIUtils.onWindowFocusChanged(this, hasFocus);
    }

}
