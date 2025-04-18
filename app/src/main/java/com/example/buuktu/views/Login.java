package com.example.buuktu.views;

import static android.widget.Toast.makeText;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.buuktu.R;
import com.example.buuktu.utils.CheckUtil;
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
    private TextInputEditText editTextEmailLogin, editTextPasswordLogin;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    TextView tv_loginButton, tv_loginToRegisterButton;
    String email,username,password;
    CollectionReference dbUsers;

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
        initComponents();

    }

    private void initComponents() {
        tv_loginButton = findViewById(R.id.tv_loginButton);
        tv_loginToRegisterButton = findViewById(R.id.tv_loginToRegisterButton);
        editTextEmailLogin = findViewById(R.id.et_emailLogin);
        editTextPasswordLogin = findViewById(R.id.et_passwordLogin);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        dbUsers = db.collection("Users");
    }

    public void handlerGoToRegister(View view) {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

    public void handlerLogin(View view) {
        if (!CheckUtil.checkTextEmpty(editTextEmailLogin) && !CheckUtil.checkTextEmpty(editTextPasswordLogin)) {
            password = editTextPasswordLogin.getText().toString();
            if(editTextEmailLogin.getText().toString().contains("@")){
                email = editTextEmailLogin.getText().toString();
                logWithEmail();
            }else{
                username = editTextEmailLogin.getText().toString();
                logWithUsername();
            }
        }
    }
    private void logWithUsername(){
        dbUsers.whereEqualTo("username", username).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
                        email = queryDocumentSnapshots.getDocuments().get(i).getString("email");
                        logWithEmail();
                    }
                } else if (queryDocumentSnapshots.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void logWithEmail(){
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(
                new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {

            }
        });
    }
}