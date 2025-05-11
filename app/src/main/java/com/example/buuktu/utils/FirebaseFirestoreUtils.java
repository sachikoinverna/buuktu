package com.example.buuktu.utils;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseFirestoreUtils {
    static FirebaseFirestore db = FirebaseFirestore.getInstance();;

    static CollectionReference collectionUserkies = db.collection("Users");
    static CollectionReference collectionStuffkies = db.collection("Stuffkies");
    CollectionReference collectionCharacterkies = db.collection("Characterkies");
   /* public static UserkieModel getUserkieModel(Context context,String UID,String mode) {
        UserkieModel userkieModel;
    }*/

   /* public static ArrayList<UserkieModel> getUsers(){

    }
    public static ArrayList<UserkieModel> getUsers(){

    }
    public static ArrayList<StuffkieModel> getStuffkiesExcludeSelf(String uid){

    }
    public static ArrayList<StuffkieModel> getStuffkies(){
     /*   collectionStuffkies.addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e != null) {
                Log.e("Error", e.getMessage());
                Toast.makeText(getContext(), "Error al escuchar cambios: " + e.getMessage(), LENGTH_LONG).show();
                return;
            }

            if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                stuffkieModelArrayList.clear(); // Limpia la lista antes de agregar nuevos datos

                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                    //if (documentSnapshot.getBoolean("photo_default")) {
                    //    if (!documentSnapshot.getId().equals(firebaseAuth.getUid())) {

                    Drawable drawable = getResources().getDrawable(R.drawable.worldkie_default);
                    StuffkieModel stuffkieModel = new StuffkieModel(
                            documentSnapshot.getId(),
                            documentSnapshot.getString("name"),
                            Boolean.TRUE.equals(documentSnapshot.getBoolean("stuffkie_private")),
                            R.drawable.cloudlogin
                    );
                    Log.d("StuffkiesSearch", "Stuffkie encontrado: " + documentSnapshot.getString("name"));

                    stuffkieModelArrayList.add(stuffkieModel);
    }*/
}
