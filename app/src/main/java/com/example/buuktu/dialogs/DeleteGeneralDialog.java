package com.example.buuktu.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.airbnb.lottie.LottieAnimationView;
import com.example.buuktu.R;
import com.example.buuktu.models.CharacterkieModel;
import com.example.buuktu.models.ScenariokieModel;
import com.example.buuktu.models.StuffkieModel;
import com.example.buuktu.models.WorldkieModel;
import com.example.buuktu.utils.EfectsUtils;
import com.example.buuktu.views.MainActivity;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DeleteGeneralDialog extends Dialog implements View.OnClickListener {
    ImageButton ib_accept_dialog, ib_close_dialog;
    final Context context;
    final String mode;
    final String UID;
    TextView tv_title_del, tv_text_del;
    ImageView iv_photo_del;
    FirebaseFirestore db;
    CollectionReference collectionNotekies,collectionWorldkies,collectionCharacterkies,collectionScenariokies,collectionStuffkies;
    LottieAnimationView animationView;
    private FirebaseStorage firebaseStorageWorldkie=FirebaseStorage.getInstance("gs://buuk-tu-worldkies"),firebaseStorageStuffkie=FirebaseStorage.getInstance("gs://buuk-tu-stuffkies"),firebaseStorageCharacterkie=FirebaseStorage.getInstance("gs://buuk-tu-characterkies"),firebaseStorageScenariokies=FirebaseStorage.getInstance("gs://buuk-tu-scenariokies");
    WorldkieModel worldkieModel;
    public DeleteGeneralDialog(@NonNull Context context, String mode, String UID) {
        super(context);
        this.context = context;
        this.mode = mode;
        this.UID = UID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* if (context instanceof Fragment)
        {
        }*/
        setContentView(R.layout.delete_general_dialog);
        initComponents();
        db = FirebaseFirestore.getInstance();
        animationView = findViewById(R.id.anim_del);
        //tv_text_del = findViewById(R.id.tv);
        switch (mode) {
            case "notekie":
                iv_photo_del.setImageResource(R.mipmap.img_del_notes);
                tv_text_del.setText("¿Deseas eliminar la nota? No podras recuperarla.");
                collectionNotekies = db.collection("Notekies");
                break;
            case "stuffkie":
                iv_photo_del.setImageResource(R.mipmap.img_del_stuffkie);
                tv_text_del.setText("¿Deseas eliminar el stuffkie? No podras recuperarla.");
                collectionStuffkies = db.collection("Stuffkies");
                break;
            case "worldkie":
                iv_photo_del.setImageResource(R.mipmap.img_del_worldkie);
                tv_text_del.setText("¿Deseas eliminar el worldkie? No podras recuperarla.");
                collectionWorldkies = db.collection("Worldkies");
                break;
            case "characterkie":
                iv_photo_del.setImageResource(R.mipmap.img_del_characterkie);
                tv_text_del.setText("¿Deseas eliminar el characterkie? No podras recuperarla.");
                collectionCharacterkies = db.collection("Characterkies");
                break;
            case "scenariokie":
                collectionScenariokies = db.collection("Scenariokies");
                break;
        }
        //tv_text_del = findViewById(R.id.tv);
        ib_close_dialog.setOnClickListener(this);
        ib_accept_dialog.setOnClickListener(this);
        setCanceledOnTouchOutside(false);
        setCancelable(false);

        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }
    private void initComponents(){
        ib_close_dialog = findViewById(R.id.ib_close_dialog);
        ib_accept_dialog = findViewById(R.id.ib_accept_dialog);
        iv_photo_del = findViewById(R.id.iv_photo_del);
        tv_text_del = findViewById(R.id.tv_text_del);
        tv_title_del = findViewById(R.id.tv_title_del);
    }
    private void prepareLoading(){
        tv_title_del.setVisibility(View.GONE);
        tv_text_del.setVisibility(View.GONE);
        iv_photo_del.setVisibility(View.GONE);
        ib_close_dialog.setVisibility(View.GONE);
        ib_accept_dialog.setVisibility(View.GONE);
        animationView.setVisibility(View.VISIBLE);
       // EfectsUtils.setAnimationsDialog("start",animationView);

    }
    private void deleteNotekie() {
        prepareLoading();
        collectionNotekies.document(UID).delete()
                .addOnSuccessListener(unused -> {
                    EfectsUtils.setAnimationsDialog("success", animationView);
                    delayedDismiss();
                })
                .addOnFailureListener(e -> {
                    EfectsUtils.setAnimationsDialog("fail", animationView);
                    delayedDismiss();
                });
    }
    private void deleteWorldkie() {
        prepareLoading();
        Log.d("DeleteWorldkie", "Iniciando proceso de eliminación para UID: " + UID);
        collectionWorldkies.document(UID).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        WorldkieModel worldkieModel = WorldkieModel.fromSnapshot(documentSnapshot);
                        Log.d("DeleteWorldkie", "Documento Worldkie encontrado: " + worldkieModel.toString());
                        boolean isDefault = worldkieModel.isPhoto_default();
                        Log.d("DeleteWorldkie", "¿La foto es default? " + isDefault);

                        collectionWorldkies.document(UID).delete()
                                .addOnSuccessListener(unused -> {
                                    Log.d("DeleteWorldkie", "Eliminación del documento Firestore exitosa.");
                                    if (!isDefault && firebaseStorageWorldkie != null) {
                                        Log.d("DeleteWorldkie", "Intentando eliminar la foto del Storage.");
                                        firebaseStorageWorldkie.getReference().child(UID).delete()
                                                .addOnSuccessListener(unused1 -> {
                                                    Log.d("DeleteWorldkie", "Eliminación de la foto del Storage exitosa.");
                                                    EfectsUtils.setAnimationsDialog("success", animationView);
                                                    delayedDismiss();
                                                })
                                                .addOnFailureListener(ex -> {
                                                    Log.e("DeleteWorldkie", "Error al eliminar la foto del Storage.", ex);
                                                    EfectsUtils.setAnimationsDialog("fail", animationView);
                                                    delayedDismiss();
                                                });
                                    } else {
                                        Log.d("DeleteWorldkie", "No se necesita eliminar la foto del Storage o no hay instancia.");
                                        EfectsUtils.setAnimationsDialog("success", animationView);
                                        delayedDismiss();
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("DeleteWorldkie", "Error al eliminar el documento Firestore.", e);
                                    EfectsUtils.setAnimationsDialog("fail", animationView);
                                    delayedDismiss();
                                });

                    } else {
                        Log.w("DeleteWorldkie", "El documento Worldkie con UID " + UID + " no existe.");
                        EfectsUtils.setAnimationsDialog("fail", animationView);
                        delayedDismiss();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("DeleteWorldkie", "Error al obtener el documento Worldkie.", e);
                    EfectsUtils.setAnimationsDialog("fail", animationView);
                    delayedDismiss();
                });
    }
    private void deleteCharacterkie() {
        prepareLoading();
        Log.d("DeleteWorldkie", "Iniciando proceso de eliminación para UID: " + UID);
        collectionCharacterkies.document(UID).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        CharacterkieModel characterkieModel = CharacterkieModel.fromSnapshot(documentSnapshot);
                        Log.d("DeleteWorldkie", "Documento Worldkie encontrado: " + characterkieModel.toString());
                        boolean isDefault = characterkieModel.isPhoto_default();
                        Log.d("DeleteWorldkie", "¿La foto es default? " + isDefault);

                        collectionCharacterkies.document(UID).delete()
                                .addOnSuccessListener(unused -> {
                                    Log.d("DeleteWorldkie", "Eliminación del documento Firestore exitosa.");
                                    if (!isDefault && firebaseStorageCharacterkie != null) {
                                        Log.d("DeleteWorldkie", "Intentando eliminar la foto del Storage.");
                                        firebaseStorageCharacterkie.getReference().child(UID).delete()
                                                .addOnSuccessListener(unused1 -> {
                                                    Log.d("DeleteWorldkie", "Eliminación de la foto del Storage exitosa.");
                                                    EfectsUtils.setAnimationsDialog("success", animationView);
                                                    delayedDismiss();
                                                })
                                                .addOnFailureListener(ex -> {
                                                    Log.e("DeleteWorldkie", "Error al eliminar la foto del Storage.", ex);
                                                    EfectsUtils.setAnimationsDialog("fail", animationView);
                                                    delayedDismiss();
                                                });
                                    } else {
                                        Log.d("DeleteWorldkie", "No se necesita eliminar la foto del Storage o no hay instancia.");
                                        EfectsUtils.setAnimationsDialog("success", animationView);
                                        delayedDismiss();
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("DeleteWorldkie", "Error al eliminar el documento Firestore.", e);
                                    EfectsUtils.setAnimationsDialog("fail", animationView);
                                    delayedDismiss();
                                });

                    } else {
                        Log.w("DeleteWorldkie", "El documento Worldkie con UID " + UID + " no existe.");
                        EfectsUtils.setAnimationsDialog("fail", animationView);
                        delayedDismiss();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("DeleteWorldkie", "Error al obtener el documento Worldkie.", e);
                    EfectsUtils.setAnimationsDialog("fail", animationView);
                    delayedDismiss();
                });
    }
    private void deleteStuffkie() {
        prepareLoading();
        Log.d("DeleteWorldkie", "Iniciando proceso de eliminación para UID: " + UID);
        collectionStuffkies.document(UID).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        StuffkieModel stuffkieModel = StuffkieModel.fromSnapshot(documentSnapshot);
                        Log.d("DeleteWorldkie", "Documento Worldkie encontrado: " + stuffkieModel.toString());
                        boolean isDefault = stuffkieModel.isPhoto_default();
                        Log.d("DeleteWorldkie", "¿La foto es default? " + isDefault);

                        collectionStuffkies.document(UID).delete()
                                .addOnSuccessListener(unused -> {
                                    Log.d("DeleteWorldkie", "Eliminación del documento Firestore exitosa.");
                                    if (!isDefault && firebaseStorageStuffkie != null) {
                                        Log.d("DeleteWorldkie", "Intentando eliminar la foto del Storage.");
                                        firebaseStorageStuffkie.getReference().child(UID).delete()
                                                .addOnSuccessListener(unused1 -> {
                                                    Log.d("DeleteWorldkie", "Eliminación de la foto del Storage exitosa.");
                                                    EfectsUtils.setAnimationsDialog("success", animationView);
                                                    delayedDismiss();
                                                })
                                                .addOnFailureListener(ex -> {
                                                    Log.e("DeleteWorldkie", "Error al eliminar la foto del Storage.", ex);
                                                    EfectsUtils.setAnimationsDialog("fail", animationView);
                                                    delayedDismiss();
                                                });
                                    } else {
                                        Log.d("DeleteWorldkie", "No se necesita eliminar la foto del Storage o no hay instancia.");
                                        EfectsUtils.setAnimationsDialog("success", animationView);
                                        delayedDismiss();
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("DeleteWorldkie", "Error al eliminar el documento Firestore.", e);
                                    EfectsUtils.setAnimationsDialog("fail", animationView);
                                    delayedDismiss();
                                });

                    } else {
                        Log.w("DeleteWorldkie", "El documento Worldkie con UID " + UID + " no existe.");
                        EfectsUtils.setAnimationsDialog("fail", animationView);
                        delayedDismiss();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("DeleteWorldkie", "Error al obtener el documento Worldkie.", e);
                    EfectsUtils.setAnimationsDialog("fail", animationView);
                    delayedDismiss();
                });
    }
    private void deleteScenariokie() {
        prepareLoading();
        collectionScenariokies.document(UID).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        ScenariokieModel scenariokieModel = ScenariokieModel.fromSnapshot(documentSnapshot);
                        Log.d("DeleteWorldkie", "Documento Worldkie encontrado: " + scenariokieModel.toString());
                        boolean isDefault = scenariokieModel.isPhoto_default();
                        Log.d("DeleteWorldkie", "¿La foto es default? " + isDefault);

                        collectionScenariokies.document(UID).delete()
                                .addOnSuccessListener(unused -> {
                                    Log.d("DeleteWorldkie", "Eliminación del documento Firestore exitosa.");
                                    if (!isDefault && firebaseStorageScenariokies != null) {
                                        Log.d("DeleteWorldkie", "Intentando eliminar la foto del Storage.");
                                        firebaseStorageStuffkie.getReference().child(UID).delete()
                                                .addOnSuccessListener(unused1 -> {
                                                    Log.d("DeleteWorldkie", "Eliminación de la foto del Storage exitosa.");
                                                    EfectsUtils.setAnimationsDialog("success", animationView);
                                                    delayedDismiss();
                                                })
                                                .addOnFailureListener(ex -> {
                                                    Log.e("DeleteWorldkie", "Error al eliminar la foto del Storage.", ex);
                                                    EfectsUtils.setAnimationsDialog("fail", animationView);
                                                    delayedDismiss();
                                                });
                                    } else {
                                        Log.d("DeleteWorldkie", "No se necesita eliminar la foto del Storage o no hay instancia.");
                                        EfectsUtils.setAnimationsDialog("success", animationView);
                                        delayedDismiss();
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("DeleteWorldkie", "Error al eliminar el documento Firestore.", e);
                                    EfectsUtils.setAnimationsDialog("fail", animationView);
                                    delayedDismiss();
                                });

                    } else {
                        Log.w("DeleteWorldkie", "El documento Worldkie con UID " + UID + " no existe.");
                        EfectsUtils.setAnimationsDialog("fail", animationView);
                        delayedDismiss();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("DeleteWorldkie", "Error al obtener el documento Worldkie.", e);
                    EfectsUtils.setAnimationsDialog("fail", animationView);
                    delayedDismiss();
                });
    }
    private void delayedDismiss() {
        Completable.timer(2, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::dismiss);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ib_close_dialog) {
            dismiss();
        } else if (v.getId() == R.id.ib_accept_dialog) {
            if (mode.equals("notekie")) {
                deleteNotekie();
            }else if (mode.equals("worldkie")){
                deleteWorldkie();
            } else if(mode.equals("characterkie")){
                deleteCharacterkie();
            } else if (mode.equals("stuffkie")) {
                deleteStuffkie();
            } else if (mode.equals("scenariokie")) {
                deleteScenariokie();
            }
        }
    }
}
