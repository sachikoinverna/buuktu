package com.example.buuktu.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.buuktu.R;
import com.example.buuktu.utils.UIUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {
    private TextInputEditText editTextEmailLogin, editTextPasswordLogin;

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
        //Oculta la barra de estado.
        UIUtils.hideSystemUI(this);
        //Inicializa los componentes.
        initComponents();

    }

    private void initComponents() {
        editTextEmailLogin = findViewById(R.id.et_emailLogin);
        editTextPasswordLogin = findViewById(R.id.et_passwordLogin);
    }

    //Se navega al registro.
    public void handlerGoToRegister(View view) {
        // Lanza la actividad para que el usuario se registre.
        startActivity(new Intent(this,Register.class));
        // Elimina la pantalla de login de la pila de actividades.
        finish();
    }

    public void handlerLogin(View view) {
        // Comprueba si los campos de Email/Nombre de usuario y Contraseña no están vacíos.
        if (!editTextEmailLogin.getText().toString().isEmpty() && !editTextPasswordLogin.getText().toString().isEmpty()) {
        // Ningún campo está vacío, así que se verifica si se está usando un email para acceder.
            if(Patterns.EMAIL_ADDRESS.matcher(editTextEmailLogin.getText().toString()).matches()){
                // Al usar un email, se llama al método de inicio de sesión correspondiente.
                logWithEmail(editTextEmailLogin.getText().toString());
            }else{
                // Al usar un nombre de usuario, se llama al método de inicio de sesión correspondiente.
                logWithUsername(editTextEmailLogin.getText().toString());
            }
        }
    }
    private void logWithUsername(String username){
        // Realiza una búsqueda en la colección "Users" para el nombre de usuario especificado, limitando a un solo resultado.
        FirebaseFirestore.getInstance().collection("Users").whereEqualTo("username", username).limit(1).get().addOnSuccessListener(queryDocumentSnapshots -> {
            // Comprueba si se ha obtenido algún resultado.
            if (!queryDocumentSnapshots.isEmpty()) {
            // Se obtuvo un resultado: se llama al método de inicio de sesión con el email del primer documento.
                logWithEmail(queryDocumentSnapshots.getDocuments().get(0).getString("email"));
            } else if (queryDocumentSnapshots.isEmpty()) {
                // No se ha obtenido ningún resultado, se indica que el login ha fallado.
                Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void logWithEmail(String email){
        // Intenta autenticar al usuario en FirebaseAuth con email y contraseña.
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, editTextPasswordLogin.getText().toString()).addOnSuccessListener(
                authResult -> {
                    // El inicio de sesión fue exitoso, por lo que se lanza la actividad principal de la aplicación.
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    // Elimina la pantalla de login de la pila de actividades.
                    finish();
                }).addOnFailureListener(e -> {
                // El inicio de sesión falló, se muestra un mensaje de error.
            Toast.makeText(getApplicationContext(), "Login Failed 2", Toast.LENGTH_SHORT).show();
                });
    }
}