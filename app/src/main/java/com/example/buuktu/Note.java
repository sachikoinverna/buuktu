package com.example.buuktu;

import static android.widget.Toast.LENGTH_LONG;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.buuktu.models.NoteItem;
import com.example.buuktu.utils.NavigationUtils;
import com.example.buuktu.views.MainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.sql.Time;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Note#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Note extends Fragment implements View.OnClickListener {

    String note_id;
    EditText et_title_note,et_content_note;
    FirebaseFirestore db;
    CollectionReference collectionNotekies;
    NoteItem noteItem;
    ImageButton ib_save_note,backButton;
    FirebaseAuth auth;
    String UID_USER;
    Map<String, Object> notekieData;
    Timestamp timestampNow;
    ImageButton ib_save;
    FragmentManager fragmentManager;
    FragmentActivity activity;
    public Note() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Note.
     */
    // TODO: Rename and change types and number of parameters
    public static Note newInstance() {
        return new Note();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            note_id = getArguments().getString("note_id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_note, container, false);
        MainActivity mainActivity = (MainActivity) getActivity();
        ib_save = mainActivity.getIb_save();
        ib_save.setVisibility(View.VISIBLE);
        backButton = mainActivity.getBackButton();
        backButton.setVisibility(View.VISIBLE);
        et_title_note = view.findViewById(R.id.et_title_note);
        et_content_note = view.findViewById(R.id.et_content_note);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        UID_USER = auth.getUid();
        notekieData = new HashMap<>();

        collectionNotekies = db.collection("Notekies");
        noteItem = new NoteItem();
        if(note_id!=null) {
            collectionNotekies.document(note_id).addSnapshotListener((queryDocumentSnapshot, e) -> {
                if (e != null) {
                    Log.e("Error", e.getMessage());
                    Toast.makeText(getContext(), "Error al escuchar cambios: " + e.getMessage(), LENGTH_LONG).show();
                    return;
                }
                if (queryDocumentSnapshot.exists()) {
                    String title = queryDocumentSnapshot.getString("title");
                    if (!title.isEmpty()) {
                        noteItem.setTitle(title);
                        et_title_note.setText(noteItem.getTitle());
                    } else {
                        noteItem.setTitle("(Sin titulo)");
                        et_title_note.setHint(noteItem.getTitle());
                    }
                    noteItem.setContent(queryDocumentSnapshot.getString("text"));
                    et_content_note.setText(noteItem.getContent());
                }
                });
        }
        else{
            noteItem.setTitle("");
            noteItem.setContent("");
        }
        ib_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((!et_content_note.getText().equals(noteItem.getContent()) || !et_title_note.getText().equals(noteItem.getTitle())) && (!et_title_note.getText().equals("") || !et_content_note.getText().equals(""))) {
                    timestampNow = Timestamp.now();
                    if(et_title_note.getText().equals("")){
                        notekieData.put("title","");
                    }else{
                        notekieData.put("title", et_title_note.getText().toString());
                    }
                    notekieData.put("text", et_content_note.getText().toString()); // Corrección clave
                    notekieData.put("last_update", timestampNow);
                    if (note_id != null) {
                        editDataFirestore();
                    }else{
                        notekieData.put("UID_USER", UID_USER);
                        addDataToFirestore();

                    }
                }
            }
        });
        fragmentManager = requireActivity().getSupportFragmentManager();
        activity = requireActivity();
        setListeners();
        return view;
    }

    private void setListeners(){
        backButton.setOnClickListener(this);
        ib_save.setOnClickListener(this);
    }
    private void addDataToFirestore() {
       // mostrarBarraProgreso();
    ///    barraProgreso.incrementProgressBy(25);
        collectionNotekies.add(notekieData).addOnSuccessListener(new   OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                note_id = documentReference.getId();
                //barraProgreso.incrementProgressBy(25);
              //  Toast.makeText(createWorldkie, "Your Course has been added to Firebase Firestore", Toast.LENGTH_SHORT).show()
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
               /* Log.e("Error", e.getMessage().toString());
                Toast.makeText(createWorldkie, e.getMessage().toString(), Toast.LENGTH_LONG).show();*/
            }
        });
    }
    private void editDataFirestore() {
        collectionNotekies.document(note_id).update(notekieData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.ib_back){
            NavigationUtils.goBack(fragmentManager,activity);
        }else if (v.getId()==R.id.ib_save) {
                if ((!et_content_note.getText().equals(noteItem.getContent()) || !et_title_note.getText().equals(noteItem.getTitle())) && (!et_title_note.getText().equals("") || !et_content_note.getText().equals(""))) {
                    timestampNow = Timestamp.now();
                    if(et_title_note.getText().equals("")){
                        notekieData.put("title","");
                    }else{
                        notekieData.put("title", et_title_note.getText().toString());
                    }
                    notekieData.put("text", et_content_note.getText().toString()); // Corrección clave
                    notekieData.put("last_update", timestampNow);
                    if (note_id != null) {
                        editDataFirestore();
                    }else{
                        notekieData.put("UID_USER", UID_USER);
                        addDataToFirestore();

                    }
                }
            }
    }
}