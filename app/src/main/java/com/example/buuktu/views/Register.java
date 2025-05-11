package com.example.buuktu.views;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
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
import com.example.buuktu.utils.EfectsUtils;
import com.example.buuktu.utils.UIUtils;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class Register extends AppCompatActivity implements View.OnFocusChangeListener {
    Calendar calendar;
    int yearC, monthC, dayC;
    private FirebaseFirestore db;
    TextInputLayout et_nameRegisterFilled,et_userRegisterFilled,dp_birthdayFilled ,et_pronounsRegisterFilled, et_emailRegisterFilled, et_telephoneRegisterFilled, et_passwordFilled ,et_passwordRepeatRegisterFilled;

    public TextInputEditText dp_birthday, et_nameRegister, et_pronounsRegister, et_userRegister, et_emailRegister, et_passwordRepeat, et_password, et_telephoneRegister;
    public Button tv_registerButton, tv_registerToLoginButton;
    ImageButton bt_chooseImage,imageButtonActualBottomSheet;
    private Switch tb_privateAccountRegister;
    private FirebaseAuth auth;
    Uri image;
    String email, username, password,source, photo_id, number, pronouns, name;

    Boolean privateAccount;
    final FirebaseStorage storage = FirebaseStorage.getInstance("gs://buuk-tu-users");
    BottomSheetProfilePhoto bottomSheetProfilePhoto;
    boolean photo_default;
    Date birthday;
    CollectionReference collectionReferenceUsers;
    InputFilter[] filters;
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
        setFilters();
        db = FirebaseFirestore.getInstance();
        collectionReferenceUsers = db.collection("Users");
        if (auth.getCurrentUser() != null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        DrawableUtils.personalizarImagenCircleButton(this, DrawableUtils.drawableToBitmap(bt_chooseImage.getDrawable()), bt_chooseImage, R.color.brownBrown);
    }

    public void setClean() {
        CheckUtil.setErrorMessage(null, et_nameRegisterFilled);
        CheckUtil.setErrorMessage(null, et_emailRegisterFilled);
        CheckUtil.setErrorMessage(null, dp_birthdayFilled);
        CheckUtil.setErrorMessage(null, et_passwordFilled);
        CheckUtil.setErrorMessage(null, et_passwordRepeatRegisterFilled);
        CheckUtil.setErrorMessage(null, et_pronounsRegisterFilled);
        CheckUtil.setErrorMessage(null, et_userRegisterFilled);
        CheckUtil.setErrorMessage(null, et_telephoneRegisterFilled);
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
        et_pronounsRegisterFilled = findViewById(R.id.et_pronounsRegisterFilled);
        et_emailRegisterFilled = findViewById(R.id.et_emailRegisterFilled);
        et_telephoneRegisterFilled = findViewById(R.id.et_telephoneRegisterFilled);
        et_passwordFilled = findViewById(R.id.et_passwordFilled);
        et_passwordRepeatRegisterFilled = findViewById(R.id.et_passwordRepeatRegisterFilled);
        tv_registerButton = findViewById(R.id.tv_registerButton);
        tv_registerToLoginButton = findViewById(R.id.tv_registerToLoginButton);
        bt_chooseImage = findViewById(R.id.bt_chooseImageRegister);
        tb_privateAccountRegister = findViewById(R.id.tb_privateAccountRegister);
        et_nameRegisterFilled = findViewById(R.id.et_nameRegisterFilled);
        et_userRegisterFilled = findViewById(R.id.et_userRegisterFilled);
        dp_birthdayFilled = findViewById(R.id.dp_birthdayFilled);
        calendar = Calendar.getInstance();
        yearC = calendar.get(Calendar.YEAR);
        monthC = calendar.get(Calendar.MONTH);
        dayC = calendar.get(Calendar.DAY_OF_MONTH);
        bottomSheetProfilePhoto = new BottomSheetProfilePhoto();
        source = "app";
        bt_chooseImage.setTag(DrawableUtils.getMipmapName(this,R.mipmap.photoprofileone));
        auth = FirebaseAuth.getInstance();


    }
    private void setFilters(){
        InputFilter filter = (source, start, end, dest, dstart, dend) -> {
            for (int i = start; i < end; i++) {
                if (Character.isSpaceChar(source.charAt(i))) {
                    return "";
                }
            }
            return null;

        };
        et_password.setFilters(new InputFilter[]{filter,new InputFilter.LengthFilter(50)});
        et_passwordRepeat.setFilters(new InputFilter[]{filter,new InputFilter.LengthFilter(50)});
        et_telephoneRegister.setFilters(new InputFilter[]{filter,new InputFilter.LengthFilter(9)});
        et_userRegister.setFilters(new InputFilter[]{filter,new InputFilter.LengthFilter(20)});
        et_emailRegister.setFilters(new InputFilter[]{filter,new InputFilter.LengthFilter(50)});
        et_pronounsRegister.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
        et_emailRegister.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
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
                CheckUtil.handlerCheckName(this,et_nameRegister,et_nameRegisterFilled);
            } else if (field == R.id.dp_birthday) {
                CheckUtil.handlerCheckBirthdayDate(this,dp_birthday,dp_birthdayFilled);
            } else if (field == R.id.et_pronounsRegister) {
                CheckUtil.handlerCheckPronouns(this,et_pronounsRegister,et_pronounsRegisterFilled);
            } else if(field == R.id.et_emailRegister){
                CheckUtil.checkEmail(this,et_emailRegister,et_emailRegisterFilled);
            }else if(field == R.id.et_userRegister){
                CheckUtil.handlerCheckUsernameEmpty(this,et_userRegister,et_userRegisterFilled);
            }
            else if (field == R.id.et_password) {
                CheckUtil.handlerCheckPassword(this,et_password,et_passwordFilled);
            } else if (field == R.id.et_passwordRepeat) {
                CheckUtil.handlerCheckPasswordRepeat(this,et_passwordRepeat,et_password,et_passwordRepeatRegisterFilled);
            } else if (field == R.id.et_telephoneRegister) {
                CheckUtil.handlerCheckTelephone(this,et_telephoneRegister,et_telephoneRegisterFilled);
            }
        } else {
            if (field == R.id.et_nameRegister) {
                CheckUtil.setErrorMessage(null, et_nameRegisterFilled);
            } else if (field == R.id.dp_birthday) {
                CheckUtil.setErrorMessage(null, dp_birthdayFilled);
            } else if (field == R.id.et_pronounsRegister) {
                CheckUtil.setErrorMessage(null, et_pronounsRegisterFilled);
            }else if (field == R.id.et_password) {
                CheckUtil.setErrorMessage(null, et_passwordFilled);
            } else if (field == R.id.et_passwordRepeat) {
                CheckUtil.setErrorMessage(null, et_passwordFilled);
            } else if (field == R.id.et_telephoneRegister) {
                CheckUtil.setErrorMessage(null, et_telephoneRegisterFilled);
            }
            else if(field == R.id.et_emailRegister){
                CheckUtil.setErrorMessage(null, et_emailRegisterFilled);
            }else if(field == R.id.et_userRegister){
                CheckUtil.setErrorMessage(null, et_userRegisterFilled);
            }
        }
    }

    public void handlerGoToRegister(View view) {
        clearFields();
        startActivity(new Intent(this, Login.class));
    }

    private boolean checkAllFields() {
        boolean[] allValid = {true}; // usamos un array para modificarlo dentro de lambda

        if (!CheckUtil.handlerCheckName(this, et_nameRegister, et_nameRegisterFilled)) {
            allValid[0] = false;
        }
        if (!CheckUtil.handlerCheckBirthdayDate(this, dp_birthday, dp_birthdayFilled)) {
            allValid[0] = false;
        }
        if (!CheckUtil.handlerCheckPassword(this, et_password, et_passwordFilled)) {
            allValid[0] = false;
        }
        if (!CheckUtil.handlerCheckPasswordRepeat(this, et_passwordRepeat, et_password, et_passwordRepeatRegisterFilled)) {
            allValid[0] = false;
        }
        if (!CheckUtil.handlerCheckPronouns(this, et_pronounsRegister, et_pronounsRegisterFilled)) {
            allValid[0] = false;
        }
        if(!CheckUtil.handlerCheckTelephone(this,et_telephoneRegister,et_telephoneRegisterFilled)){
            allValid[0] = false;
        }
        if(!CheckUtil.checkEmail(this,et_emailRegister,et_emailRegisterFilled)){
            allValid[0] = false;
        }
        if(!CheckUtil.handlerCheckUsernameEmpty(this,et_userRegister,et_userRegisterFilled)){
            allValid[0] = false;
        }
        return allValid[0];

    }
    public void addDataToFirestore(View view) {
        if (checkAllFields()) {
            CreateEditGeneralDialog dialog = new CreateEditGeneralDialog(this);
            dialog.show();


            LottieAnimationView animationView = dialog.findViewById(R.id.anim_create_edit);
            animationView.setAnimation(R.raw.reading_anim);
            animationView.playAnimation();
            Completable.timer(3, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                                username = et_userRegister.getText().toString();
                                email = et_emailRegister.getText().toString();

                                Task<QuerySnapshot> usernameTask = collectionReferenceUsers.whereEqualTo("username", username).limit(1).get();
                                Task<QuerySnapshot> emailTask = collectionReferenceUsers.whereEqualTo("email", email).limit(1).get();
                                Tasks.whenAllSuccess(usernameTask, emailTask).addOnSuccessListener(results -> {
                                    QuerySnapshot usernameSnapshot = (QuerySnapshot) results.get(0);
                                    QuerySnapshot emailSnapshot = (QuerySnapshot) results.get(1);
                                    if (!usernameSnapshot.isEmpty()) {
                                        CheckUtil.setErrorMessage("Nombre de usuario existente", et_userRegisterFilled);
                                    }
                                    if (!emailSnapshot.isEmpty()) {
                                        CheckUtil.setErrorMessage("Email existente", et_emailRegisterFilled);
                                    }
                                    if (usernameSnapshot.isEmpty() && emailSnapshot.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                                        // Puedes continuar con el registro


                                        email = et_emailRegister.getText().toString();
                                        password = et_password.getText().toString();
                                        privateAccount = tb_privateAccountRegister.isChecked();
                                        username = et_userRegister.getText().toString();
                                        pronouns = et_pronounsRegister.getText().toString();
                                        name = et_nameRegister.getText().toString();
                                        number = et_telephoneRegister.getText().toString();
                                        PhoneNumberUtil photoNumberUtil = PhoneNumberUtil.getInstance();
                                        try {
                                            Phonenumber.PhoneNumber phoneNumber = photoNumberUtil.parse(number
                                                    , "ES");
                                            number = photoNumberUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);

                                        } catch (NumberParseException e) {
                                            System.err.println("NumberParseException was thrown: " + e.toString());
                                        }
                                        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {


                                                Toast.makeText(getApplicationContext(), "Signup Successful", Toast.LENGTH_SHORT).show();
                                                CollectionReference dbUsers = db.collection("Users");
                                                UserkieModel userkieModel;
                                                if (source.equals("app")) {
                                                    photo_id = bt_chooseImage.getTag().toString();
                                                    userkieModel = new UserkieModel(photo_id, privateAccount, true, email, number, username, new Timestamp(birthday), pronouns, name);
                                                } else {
                                                    userkieModel = new UserkieModel(name, pronouns, new Timestamp(birthday), username, number, email, false, privateAccount);

                                                }
                                                // below method is use to add data to Firebase Firestore.
                                                DocumentReference documentRef = dbUsers.document(task.getResult().getUser().getUid());

                                                //.document(uid)
                                                documentRef.set(userkieModel).addOnSuccessListener(unused -> {

                                                    if (source.equals("device")) {
                                                        StorageReference userRef = storage.getReference().child(task.getResult().getUser().getUid());
                                                        userRef.child("profile" + DrawableUtils.getExtensionFromUri(getApplicationContext(), image)).putFile(image);

                                                    }
                                                    EfectsUtils.setAnimationsDialog("success",animationView);

                                                    Completable.timer(3, TimeUnit.SECONDS)
                                                            .subscribeOn(Schedulers.io())
                                                            .observeOn(AndroidSchedulers.mainThread())
                                                            .subscribe(dialog::dismiss);
                                                    clearFields();
                                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                                }).addOnFailureListener(e -> {
                                                    EfectsUtils.setAnimationsDialog("fail",animationView);

                                                    Completable.timer(3, TimeUnit.SECONDS)
                                                            .subscribeOn(Schedulers.io())
                                                            .observeOn(AndroidSchedulers.mainThread())
                                                            .subscribe(dialog::dismiss);
                                                });
                                            }
                                        });
                                    }
                                });
                            }
                    );
        }
        }


    public void showDatePickerDialog(View view) {
        DatePickerDialog date = new DatePickerDialog(this, (datePicker, year, month, day) -> {
            dp_birthday.setText(day + "/" + (month + 1) + "/" + year);
            dp_birthdayFilled.setHintEnabled(true); // este es el TextInputLayout
            dayC = day;
            monthC = month;
            yearC = year;
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day); // Configura el calendario con la fecha seleccionada
            birthday = calendar.getTime();
        }, yearC, monthC, dayC);
        date.show();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        UIUtils.onWindowFocusChanged(this, hasFocus);
    }
    private void clearFields(){
            et_nameRegister.setText("");
            et_emailRegister.setText("");
            et_userRegister.setText("");
            et_pronounsRegister.setText("");
            et_password.setText("");
            et_passwordRepeat.setText("");
            dp_birthday.setText("");
            et_telephoneRegister.setText("");
            tb_privateAccountRegister.setChecked(false);
    }
}
