package com.example.buuktu;

import static android.widget.Toast.LENGTH_LONG;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.buuktu.models.NotekieModel;
import com.example.buuktu.utils.NavigationUtils;
import com.example.buuktu.views.MainActivity;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

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
    NotekieModel notekieModel;
    ImageButton ib_save,backButton,ib_profile_superior;
    FirebaseAuth auth;
    String UID_USER;
    Map<String, Object> notekieData;
    Timestamp timestampNow;
    FragmentManager fragmentManager;
    MainActivity mainActivity;
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
        initComponents(view);


        collectionNotekies = db.collection("Notekies");
        notekieModel = new NotekieModel();
        if(note_id!=null) {
            collectionNotekies.document(note_id).addSnapshotListener((queryDocumentSnapshot, e) -> {
                if (e != null) {
                    Log.e("Error", e.getMessage());
                    Toast.makeText(getContext(), "Error al escuchar cambios: " + e.getMessage(), LENGTH_LONG).show();
                    return;
                }
                if (queryDocumentSnapshot.exists()) {
                    NotekieModel note = NotekieModel.fromSnapshot(queryDocumentSnapshot);
                    String title = note.getTitle();
                    if (!title.isEmpty()) {
                        et_title_note.setText(notekieModel.getTitle());
                    } else {
                        et_title_note.setHint(notekieModel.getTitle());
                    }
                    et_content_note.setText(notekieModel.getContent());
                }
                });
        }
        else{
            notekieModel.setTitle("");
            notekieModel.setContent("");
        }
        setListeners();
        return view;
    }
    private void initComponents(View view){
        mainActivity = (MainActivity) getActivity();
        ib_save = mainActivity.getIb_save();
        backButton = mainActivity.getBackButton();
        ib_profile_superior = mainActivity.getIb_self_profile();
        et_title_note = view.findViewById(R.id.et_title_note);
        et_content_note = view.findViewById(R.id.et_content_note);
        fragmentManager = mainActivity.getSupportFragmentManager();
        setInitVisibility();
        initVar();
    }
    private void initVar(){
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        UID_USER = auth.getUid();
        notekieData = new HashMap<>();
    }
    private void setListeners(){
        backButton.setOnClickListener(this);
        ib_save.setOnClickListener(this);
    }
    private void setInitVisibility(){
        ib_save.setVisibility(View.VISIBLE);
        backButton.setVisibility(View.VISIBLE);
        ib_profile_superior.setVisibility(View.VISIBLE);
    }
    private void addDataToFirestore() {
        collectionNotekies.add(notekieData).addOnSuccessListener(documentReference -> note_id = documentReference.getId()).addOnFailureListener(e -> {
        });
    }
    private void editDataFirestore() {
        collectionNotekies.document(note_id).update(notekieData).addOnSuccessListener(unused -> {

        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.ib_back){
            NavigationUtils.goBack(fragmentManager,mainActivity);
        }else if (v.getId()==R.id.ib_save) {
                if ((!et_content_note.getText().equals(notekieModel.getContent()) || !et_title_note.getText().equals(notekieModel.getTitle())) && (!et_title_note.getText().equals("") || !et_content_note.getText().equals(""))) {
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