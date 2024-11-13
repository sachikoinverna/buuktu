package com.example.buuktu.views;

import android.os.Bundle;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.buuktu.R;
import com.example.buuktu.controllers.LoginController;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    //private DatabaseReference mDatabase;
    private TextInputEditText editTextEmailLogin;
    private TextInputEditText editTextPasswordLogin;
    private FirebaseAuth auth;
    //FirebaseDatabase database = FirebaseDatabase.getInstance();
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
        auth = FirebaseAuth.getInstance();
        editTextEmailLogin = findViewById(R.id.et_emailLogin);
        editTextPasswordLogin = findViewById(R.id.et_passwordLoginFilled);

        //WORK WORK WORK WORK WORK WORK WORK WORK WORK
        /*auth.createUserWithEmailAndPassword("chikoritaxserperior@gmail.com", "123456")
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Login.this, "Signup Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Login.this, Register.class));
                        } else {
                            switch (task.getException().getMessage()) {
                                case "auth/email-already-in-use":
                                    Toast.makeText(Login.this,"Ya existe una cuenta con el correo electronico",Toast.LENGTH_LONG).show();
                                break;
                                default:
                                    break;
                            }
                            Toast.makeText(Login.this, "Signup Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });*/

        /*mDatabase = database.getReference("https://buuk-tu-default-rtdb.europe-west1.firebasedatabase.app/");
        mDatabase.child("users").child("user").child("sachikoinverna@gmail.com").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                }
            }
        });*/
        /*ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Post post = dataSnapshot.getValue(Post.class);
                // ..
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mPostReference.addValueEventListener(postListener);*/
        LoginController loginController = new LoginController(this);
        /*Typeface birchLeaf = Typeface.createFromAsset(this.getAssets(), "font/birchleaf.ttf");
        TextView textView = findViewById(R.id.textView);
        textView.setTypeface(birchLeaf);*/
    }
    public TextInputEditText getEt_emailLogin() {
        return editTextEmailLogin;
    }
    public TextInputEditText getEt_passwordLogin() {
        return editTextPasswordLogin;

    }
}