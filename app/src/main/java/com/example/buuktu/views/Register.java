package com.example.buuktu.views;


import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.buuktu.R;
import com.example.buuktu.controllers.RegisterController;
import com.example.buuktu.utils.CheckUtil;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class Register extends AppCompatActivity {
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
        RegisterController registerController = new RegisterController(this);
        et_nameRegister.setOnFocusChangeListener(registerController);
        et_password.setOnFocusChangeListener(registerController);
        et_passwordRepeat.setOnFocusChangeListener(registerController);
        bt_registerToLogin.setOnClickListener(registerController);
        dp_birthday.setOnClickListener(registerController);
        db = FirebaseFirestore.getInstance();
        bt_register = findViewById(R.id.bt_register);
/*// Launch the photo picker and let the user choose only images/videos of a
// specific MIME type, such as GIFs.
        String mimeType = "image/gif";
        pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(new PickVisualMedia.SingleMimeType(mimeType))
                .build());
    }*/
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
}
