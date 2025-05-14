package com.example.buuktu.utils;

import com.google.firebase.auth.FirebaseAuth;

public class FirebaseAuthUtils {

    public static final void signOut() {
        FirebaseAuth.getInstance().signOut();
    }
}
