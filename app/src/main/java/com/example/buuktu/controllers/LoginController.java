package com.example.buuktu.controllers;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.buuktu.R;
import com.example.buuktu.utils.CheckUtil;
import com.example.buuktu.views.Login;
import com.example.buuktu.views.MainActivity;
import com.example.buuktu.views.Register;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginController implements View.OnClickListener {
    private final Login login;
   private FirebaseFirestore db;
   private FirebaseAuth auth;
    public LoginController(Login login) {
        this.login = login;
       auth = FirebaseAuth.getInstance();
       db = FirebaseFirestore.getInstance();
       //handlerLogin();
    }

    private void handlerClearFields(){

    }
    private void handlerGoToRegister(){
        Intent intent = new Intent(login.getApplicationContext(), Register.class);
        login.startActivity(intent);
    }
    private void handlerLogin(){
        if(!CheckUtil.checkTextEmpty(login.getEt_emailLogin()) && !CheckUtil.checkTextEmpty(login.getEt_passwordLogin())) {
            auth.signInWithEmailAndPassword(login.getEt_emailLogin().getText().toString(),login.getEt_passwordLogin().getText().toString()).addOnSuccessListener(
                    new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Intent intent = new Intent(login.getApplicationContext(), MainActivity.class);
                            login.startActivity(intent);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    CollectionReference dbUsers = db.collection("Users");
                    dbUsers.whereEqualTo("username", login.getEt_emailLogin().getText().toString()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if(!queryDocumentSnapshots.isEmpty()){
                                for (int i = 0; i < queryDocumentSnapshots.size(); i++){
                                    auth.signInWithEmailAndPassword(queryDocumentSnapshots.getDocuments().get(i).getString("email"), login.getEt_passwordLogin().getText().toString())
                                            .addOnCompleteListener(login, new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (task.isSuccessful()) {
                                                        //Toast.makeText(login,"Login Successful",Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(login.getApplicationContext(), MainActivity.class);
                                                        login.startActivity(intent);

                                                    } else {
                                                        Toast.makeText(login,"Login Failed",Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                            } else if (queryDocumentSnapshots.isEmpty()) {
                                Toast.makeText(login,"Login Failed",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(login,"Login Failed",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.bt_loginToRegister) {
            handlerGoToRegister();
        } else if (view.getId()==R.id.ib_login) {
            handlerLogin();
        }
    }
}
