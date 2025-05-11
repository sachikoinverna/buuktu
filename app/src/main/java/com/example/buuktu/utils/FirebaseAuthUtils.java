package com.example.buuktu.utils;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class FirebaseAuthUtils {
    public static final boolean changeEmail(String newEmail) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.updateEmail(newEmail)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "User email address updated.");
                    }
                });
        return true;
    }
    public static final boolean deleteAccount() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String UID = user.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference worldkies = db.collection("Worldkies");
        CollectionReference stuffkies = db.collection("Stuffkies");
        CollectionReference characterkies = db.collection("Characterkies");

        //);
// }
//}
        user.delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        worldkies.whereEqualTo("UID_AUTHOR", UID).addSnapshotListener((queryDocumentSnapshots, e) -> {
                            if (e != null) {
                                Log.e("Error", e.getMessage());
                                //Toast.makeText(getContext(), "Error al escuchar cambios: " + e.getMessage(), LENGTH_LONG).show();
                                return;
                            }

                            if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                                //  worldkieModelArrayList.clear(); // Limpia la lista antes de agregar nuevos datos
                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    worldkies.document(documentSnapshot.getId()).delete().addOnSuccessListener(unused -> {
                /* holder.getFirebaseStorage().getReference().child(dataSet.get(holder.getAdapterPosition()).getUID()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                     @Override
                     public void onSuccess(Void unused) {

                     }
                 }).addOnFailureListener(new OnFailureListener() {
                     @Override
                     public void onFailure(@NonNull Exception e) {

                     }
                 });*/
                                    });
                                }
                            }
                        });
                      /*  stuffkies.whereEqualTo("UID_AUTHOR", UID).addSnapshotListener((queryDocumentSnapshots, e) -> {
                            if (e != null) {
                                Log.e("Error", e.getMessage());
                                //Toast.makeText(getContext(), "Error al escuchar cambios: " + e.getMessage(), LENGTH_LONG).show();
                                return;
                            }

                            if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                                //  worldkieModelArrayList.clear(); // Limpia la lista antes de agregar nuevos datos
                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    stuffkies.document(documentSnapshot.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {*/
                    /* holder.getFirebaseStorage().getReference().child(dataSet.get(holder.getAdapterPosition()).getUID()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                         @Override
                         public void onSuccess(Void unused) {

                         }
                     }).addOnFailureListener(new OnFailureListener() {
                         @Override
                         public void onFailure(@NonNull Exception e) {

                         }
                     });*/
                                        }
                                    });
                                                   /*characterkies.whereEqualTo("UID_AUTHOR", UID).addSnapshotListener((queryDocumentSnapshots, e) -> {
                                                       if (e != null) {
                                                           Log.e("Error", e.getMessage());
                                                           //Toast.makeText(getContext(), "Error al escuchar cambios: " + e.getMessage(), LENGTH_LONG).show();
                                                           return;
                                                       }

                                                       if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                                                           for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                                               characterkies.document(documentSnapshot.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                   @Override
                                                                   public void onSuccess(Void unused) {*/
                                               /* holder.getFirebaseStorage().getReference().child(dataSet.get(holder.getAdapterPosition()).getUID()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {

                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {

                                                    }
                                                });*/
                                                                  // }
                                                             //  });
                                                           //}
                                                    //   }
                                                  // });
                                             //  }
                                          // }
                                       //});
                                  /*  for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                                        DocumentSnapshot doc = dc.getDocument();


                                        if (!doc.getBoolean("photo_default")) {
                                           // loadAndSetImage(doc.getId(), worldkieModel);
                                        }
                                    }*/
                            /*holder.getDb().collection("Worldkies").document(dataSet.get(holder.getAdapterPosition()).getUID()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    holder.getFirebaseStorage().getReference().child(dataSet.get(holder.getAdapterPosition()).getUID()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });*/
                     //   }
                   // });
                         //   Log.d(TAG, "User account deleted.");
        //                }
      //              }
    //            });
        return true;

    }

    public static final boolean signOut() {
        FirebaseAuth.getInstance().signOut();
        return true;
    }
    public static final boolean reauth(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

// Get auth credentials from the user for re-authentication. The example below shows
// email and password credentials but there are multiple possible providers,
// such as GoogleAuthProvider or FacebookAuthProvider.
        AuthCredential credential = EmailAuthProvider
                .getCredential("user@example.com", "password1234");

// Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(task -> Log.d(TAG, "User re-authenticated."));
        return true;
    }
    public static final boolean verifyEmail() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Email sent.");
                    }
                });
        return true;
    }
    public static final boolean correoContraseña(String email){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Email sent.");
                    }
                });
        return true;
    }
}
