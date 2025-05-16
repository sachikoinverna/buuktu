package com.example.buuktu.views;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Switch;

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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
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
    TextInputLayout et_nameRegisterFilled,et_userRegisterFilled,dp_birthdayFilled ,et_pronounsRegisterFilled, et_emailRegisterFilled, et_telephoneRegisterFilled, et_passwordFilled ,et_passwordRepeatRegisterFilled;
    public TextInputEditText dp_birthday, et_nameRegister, et_pronounsRegister, et_userRegister, et_emailRegister, et_passwordRepeat, et_password, et_telephoneRegister;
    ImageButton bt_chooseImage;
    private Switch tb_privateAccountRegister;
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    Uri image;
    String email;
    String username;
    final FirebaseStorage storage = FirebaseStorage.getInstance("gs://buuk-tu-users");
    BottomSheetProfilePhoto bottomSheetProfilePhoto;
    Date birthday;
    CollectionReference collectionReferenceUsers;
    UserkieModel userkieModel;
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
        isUserLoggedIn();
        UIUtils.hideSystemUI(this);

        initComponents();
        setListeners();
        setFilters();

    }
    private void isUserLoggedIn(){
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }


    public void setImageUri(Uri image) {
        this.image = image;
    }


    private void initComponents() {
        userkieModel = new UserkieModel();
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
        bt_chooseImage.setTag(DrawableUtils.getMipmapName(this,R.mipmap.photoprofileone));
        collectionReferenceUsers = FirebaseFirestore.getInstance().collection("Users");
        setPhotoDefault();
        DrawableUtils.personalizarImagenCircleButton(this, DrawableUtils.drawableToBitmap(bt_chooseImage.getDrawable()), bt_chooseImage, R.color.blue1);


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

    //Se navega al registro.
    public void handlerGoLogin(View view) {
        // Lanza la actividad para que el usuario inicie sesión.
        startActivity(new Intent(this, Login.class));
        // Elimina la pantalla de registro de la pila de actividades.
        finish();

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
    public void setPhotoNoDefault(){
        userkieModel.setPhoto_default(false);
        userkieModel.setPhoto_id(null);
    }
    public void setPhotoDefault(){
        userkieModel.setPhoto_default(true);
        userkieModel.setPhoto_id(bt_chooseImage.getTag().toString());
    }
    public void addDataToFirestore(View view) {
        // Verifica que todos los datos ingresados sean válidos
        if (checkAllFields()) {
            //
            CreateEditGeneralDialog dialog = new CreateEditGeneralDialog(this);
            dialog.show();
            LottieAnimationView animationView = dialog.findViewById(R.id.anim_create_edit);
            Completable.timer(3, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                                username = et_userRegister.getText().toString();
                                email = et_emailRegister.getText().toString();
                                // Realiza una búsqueda en "Users" para el nombre de usuario especificado, limitando a un solo resultado
                                Task<QuerySnapshot> usernameTask = collectionReferenceUsers.whereEqualTo("username", username).limit(1).get();
                        // Realiza una búsqueda en "Users" para el email especificado, limitando a un solo resultado
                        Task<QuerySnapshot> emailTask = collectionReferenceUsers.whereEqualTo("email", email).limit(1).get();
                        //
                                Tasks.whenAllSuccess(usernameTask, emailTask).addOnSuccessListener(results -> {
                                    QuerySnapshot usernameSnapshot = (QuerySnapshot) results.get(0);
                                    QuerySnapshot emailSnapshot = (QuerySnapshot) results.get(1);
                                    //Comprueba si
                                    if (!usernameSnapshot.isEmpty()) {
                                        CheckUtil.setErrorMessage(getString(R.string.userErrorExists), et_userRegisterFilled);
                                    }
                                    if (!emailSnapshot.isEmpty()) {
                                        CheckUtil.setErrorMessage(getString(R.string.emailExists), et_emailRegisterFilled);
                                    }
                                    if (usernameSnapshot.isEmpty() && emailSnapshot.isEmpty()) {
                                        // Puedes continuar con el registro

                                        userkieModel.setEmail(email);
                                        userkieModel.setName(et_nameRegister.getText().toString());
                                        userkieModel.setPronouns(et_pronounsRegister.getText().toString());
                                        userkieModel.setProfile_private(tb_privateAccountRegister.isChecked());
                                        userkieModel.setUsername(et_userRegister.getText().toString());
                                        userkieModel.setBirthday(new Timestamp(birthday));
                                        PhoneNumberUtil photoNumberUtil = PhoneNumberUtil.getInstance();
                                        try {
                                            Phonenumber.PhoneNumber phoneNumber = photoNumberUtil.parse(et_telephoneRegister.getText().toString()
                                                    , "ES");
                                            userkieModel.setNumber(photoNumberUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL));

                                        } catch (NumberParseException e) {
                                            System.err.println("NumberParseException was thrown: " + e);
                                        }
                                        auth.createUserWithEmailAndPassword(email, et_password.getText().toString()).addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                // below method is use to add data to Firebase Firestore.
                                                collectionReferenceUsers.document(task.getResult().getUser().getUid()).set(userkieModel.toMap()).addOnSuccessListener(unused -> {
                                                    //Comprueba
                                                    if (!userkieModel.isPhoto_default()) {
                                                        storage.getReference().child(task.getResult().getUser().getUid()).child("profile" + DrawableUtils.getExtensionFromUri(getApplicationContext(), image)).putFile(image);

                                                    }
                                                    EfectsUtils.setAnimationsDialog("success",animationView);


                                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                                }).addOnFailureListener(e -> {
                                                    EfectsUtils.setAnimationsDialog("fail",animationView);

                                                    delayedDismiss(dialog);
                                                });
                                            }
                                        });
                                    }
                                });
                            }
                    );
        }
        }

    private void delayedDismiss(CreateEditGeneralDialog dialog) {
        Completable.timer(2, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(dialog::dismiss);
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

}
