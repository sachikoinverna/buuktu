package com.example.buuktu.views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.airbnb.lottie.LottieAnimationView;
import com.example.buuktu.R;
import com.example.buuktu.dialogs.CreateEditGeneralDialog;
import com.example.buuktu.models.NotekieModel;
import com.example.buuktu.utils.EfectsUtils;
import com.example.buuktu.utils.NavigationUtils;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class Note extends Fragment implements View.OnClickListener {
    String note_id;
    EditText et_title_note,et_content_note;
    NotekieModel notekieModel;
    ImageButton ib_save,backButton,ib_profile_superior;
    FragmentManager fragmentManager;
    MainActivity mainActivity;
    LottieAnimationView animationView;
    CreateEditGeneralDialog dialog;

    public Note() {}

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
        View view = inflater.inflate(R.layout.fragment_note, container, false);
        initComponents(view);

        setNotekieModel();
        setListeners();
        return view;
    }
    private void setNotekieModel(){
        if(note_id!=null) {
            mainActivity.getCollectionNotekies().document(note_id).addSnapshotListener((queryDocumentSnapshot, e) -> {
                if (e != null) return;
                if (queryDocumentSnapshot != null) {
                    notekieModel = NotekieModel.fromSnapshot(queryDocumentSnapshot);
                    if (!notekieModel.getTitle().isEmpty()) {
                        et_title_note.setText(notekieModel.getTitle());
                    }
                    et_content_note.setText(notekieModel.getText());
                }
            });
        }
        else{
            notekieModel = new NotekieModel();
            notekieModel.setUID_USER(mainActivity.getUID());
            notekieModel.setTitle(mainActivity.getString(R.string.untitled));
            notekieModel.setText("");
            createMode();

        }
    }
    private void createMode(){
        et_title_note.setHint(notekieModel.getTitle());
        et_content_note.setText(notekieModel.getText());
    }
    private void initComponents(View view){
        mainActivity = (MainActivity) getActivity();
        ib_save = mainActivity.getIb_save();
        backButton = mainActivity.getBackButton();
        ib_profile_superior = mainActivity.getIb_self_profile();
        et_title_note = view.findViewById(R.id.et_title_note);
        et_content_note = view.findViewById(R.id.et_content_note);
        fragmentManager = mainActivity.getSupportFragmentManager();
        dialog = new CreateEditGeneralDialog(mainActivity);
        setInitVisibility();
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

        Completable.timer(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> mainActivity.getCollectionNotekies().add(notekieModel.toMap()).addOnCompleteListener(task -> {
                     if (task.isSuccessful()) {
                         note_id = task.getResult().getId();
                         successFail("success");
                     }
                 }).addOnFailureListener(e -> successFail("fail"))
                );
    }
private void delayedDismiss() {
    Completable.timer(3, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(() -> dialog.dismiss());
}
    private void editDataFirestore() {
        Completable.timer(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                            mainActivity.getCollectionNotekies().document(notekieModel.getUID()).update(notekieModel.toMap()).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    successFail("success");
                                }
                            }).addOnFailureListener(e -> successFail("fail"));
                        }
                );
    }
    private void save(){
        if ((!et_content_note.getText().toString().isEmpty())) {
            notekieModel.setTitle(et_title_note.getText().toString().isEmpty() ?"":et_title_note.getText().toString());
            notekieModel.setText(et_content_note.getText().toString());
            notekieModel.setLast_update(Timestamp.now());
            dialog.show();
            animationView = dialog.getAnimationView();
            if (note_id != null) {
                editDataFirestore();
            }else{
                addDataToFirestore();
            }
        }
    }
    private void successFail(String mode){
        EfectsUtils.setAnimationsDialog(mode, animationView);
        delayedDismiss();
    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.ib_back){
            NavigationUtils.goBack(fragmentManager,mainActivity);
        }else if (v.getId()==R.id.ib_save) {
                save();
        }
    }
}