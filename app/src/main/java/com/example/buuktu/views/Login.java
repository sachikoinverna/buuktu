package com.example.buuktu.views;

import static android.widget.Toast.makeText;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.buuktu.R;
import com.example.buuktu.controllers.LoginController;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class Login extends AppCompatActivity {
    private TextInputEditText editTextEmailLogin,editTextPasswordLogin;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    ImageButton ib_login;
    ImageButton bt_loginToRegister;
    TextView tv_loginButton,tv_loginToRegisterButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        tv_loginButton = findViewById(R.id.tv_loginButton);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        tv_loginToRegisterButton = findViewById(R.id.tv_loginToRegisterButton);
        editTextEmailLogin = findViewById(R.id.et_emailLogin);
        editTextPasswordLogin = findViewById(R.id.et_passwordLogin);
        LoginController loginController = new LoginController(this);
        bt_loginToRegister.setOnClickListener(loginController);
        /*Typeface birchLeaf = Typeface.createFromAsset(this.getAssets(), "font/birchleaf.ttf");
        TextView textView = findViewById(R.id.textView);
        textView.setTypeface(birchLeaf);*/
        ib_login.setOnClickListener(loginController);
    }
    public TextInputEditText getEt_emailLogin() {
        return editTextEmailLogin;
    }
    public TextInputEditText getEt_passwordLogin() {
        return editTextPasswordLogin;

    }
}