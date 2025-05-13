package com.example.buuktu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.buuktu.models.CharacterkieModel;
import com.example.buuktu.models.StuffkieModel;
import com.example.buuktu.models.UserkieModel;
import com.example.buuktu.models.WorldkieModel;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StuffkieView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StuffkieView extends Fragment {
    FirebaseFirestore db;
    CollectionReference collectionStuffkies,collectionUserkies,collectionWorldkies;
    UserkieModel userkieModel;
    StuffkieModel stuffkieModel;
    public StuffkieView() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment StuffkieView.
     */
    // TODO: Rename and change types and number of parameters
    public static StuffkieView newInstance() {
        return new StuffkieView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stuffkie_view, container, false);
        collectionUserkies = db.collection("Users");
        collectionStuffkies = db.collection("Stuffkies");
        collectionWorldkies = db.collection("Worldkies");
       /* collectionUserkies.document(UID).addSnapshotListener((documentSnapshot, e) -> {
            if (e != null) {

                return;
            }

            if (documentSnapshot != null) {
                userkieModel = UserkieModel.fromSnapshot(documentSnapshot);
                getProfilePhoto();
                tv_nameProfileView.setText(userkieModel.getName());
                tv_usernameProfileView.setText(userkieModel.getUsername());
                if ((!userkieModel.isProfile_private() && mode.equals("other")) || (mode.equals("self"))){
                    collectionStuffkies.whereEqualTo("UID_AUTHOR",UID).addSnapshotListener((queryDocumentSnapshots, ex) -> {
                        if (ex != null) {
                            return;
                        }

                        if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                            stuffkieArrayList.clear(); // Limpia la lista antes de agregar nuevos datos

                            boolean foundData = false; // Add a flag

                            for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                                StuffkieModel stuffkieModel = StuffkieModel.fromSnapshot(doc);
                                stuffkieArrayList.add(stuffkieModel);
                                foundData = true; // Set the flag to true if data is found

                            }
                            if (foundData) {
                                tv_stuffkiesPreviewUserkie.setVisibility(View.VISIBLE);
                                cv_stuffkiesPreviewUserkie.setVisibility(View.VISIBLE);
                                updateRecyclerViewStuffkies(stuffkieArrayList);

                            }
                        } else {
                            tv_stuffkiesPreviewUserkie.setVisibility(View.GONE);
                            cv_stuffkiesPreviewUserkie.setVisibility(View.GONE);
                            updateRecyclerViewStuffkies(new ArrayList<>());

                        }
                    });
                    collectionWorldkies.whereEqualTo("UID_AUTHOR", UID).whereNotEqualTo("draft", true).addSnapshotListener((queryDocumentSnapshots, ex) -> {
                        if (ex != null) {
                            return;
                        }

                        if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                            worldkieArrayList.clear(); // Limpia la lista antes de agregar nuevos datos
                            boolean foundData = false; // Add a flag

                            for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                                worldkieArrayList.add(WorldkieModel.fromSnapshot(doc));
                                foundData = true; // Set the flag to true if data is found
                            }
                            if (foundData) {
                                tv_worldkiesPreviewUserkie.setVisibility(View.VISIBLE);
                                cv_worldkiesPreviewUserkie.setVisibility(View.VISIBLE);
                                updateRecyclerViewWorldkies(worldkieArrayList);
                            } else {
                                tv_worldkiesPreviewUserkie.setVisibility(View.GONE);
                                cv_worldkiesPreviewUserkie.setVisibility(View.GONE);
                                updateRecyclerViewWorldkies(new ArrayList<>());
                            }
                        } else {
                            tv_worldkiesPreviewUserkie.setVisibility(View.GONE);
                            cv_worldkiesPreviewUserkie.setVisibility(View.GONE);
                            updateRecyclerViewWorldkies(new ArrayList<>());
                        }
                    });

                    collectionCharacterkies.whereEqualTo("UID_AUTHOR", UID).whereNotEqualTo("draft", true).addSnapshotListener((queryDocumentSnapshots, ex) -> {
                        if (ex != null) {
                            return;
                        }

                        if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                            characterkieArrayList.clear(); // Limpia la lista antes de agregar nuevos datos
                            boolean foundData = false; // Add a flag

                            for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                                //if (documentSnapshot.getBoolean("photo_default")) {

                                CharacterkieModel characterkieModel = CharacterkieModel.fromSnapshot(documentSnapshot);

                                characterkieArrayList.add(characterkieModel);
                                foundData = true; // Set the flag to true if data is found
                            }
                            if (foundData) {
                                tv_characterkiesPreviewUserkie.setVisibility(View.VISIBLE);
                                cv_characterkiesPreviewUserkie.setVisibility(View.VISIBLE);
                                updateRecyclerViewCharacterkies(characterkieArrayList);
                            } else {
                                tv_characterkiesPreviewUserkie.setVisibility(View.GONE);
                                cv_characterkiesPreviewUserkie.setVisibility(View.GONE);
                                updateRecyclerViewCharacterkies(new ArrayList<>());

                            }
                        }
                    });
                }
                else{
                    tv_characterkiesPreviewUserkie.setVisibility(View.GONE);
                    cv_characterkiesPreviewUserkie.setVisibility(View.GONE);
                    tv_worldkiesPreviewUserkie.setVisibility(View.GONE);
                    cv_worldkiesPreviewUserkie.setVisibility(View.GONE);
                    tv_stuffkiesPreviewUserkie.setVisibility(View.GONE);
                    cv_stuffkiesPreviewUserkie.setVisibility(View.GONE);
                    iv_locked_profile.setVisibility(View.VISIBLE);
                    tv_locked_profile.setVisibility(View.VISIBLE);
                }
            }
        });*/
        return view;
    }
}