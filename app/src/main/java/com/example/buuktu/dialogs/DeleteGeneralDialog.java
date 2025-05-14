package com.example.buuktu.dialogs;

import android.app.Dialog;
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

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DeleteGeneralDialog extends Dialog implements View.OnClickListener {
    ImageButton ib_accept_dialog, ib_close_dialog;
    final MainActivity context;
    final String mode;
    final String UID;
    TextView tv_title_del, tv_text_del;
    ImageView iv_photo_del;
    LottieAnimationView animationView;
    public DeleteGeneralDialog(@NonNull MainActivity context, String mode, String UID) {
        super(context);
        this.context = context;
        this.mode = mode;
        this.UID = UID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_general_dialog);
        initComponents();
        switch (mode) {
            case "notekie":
                iv_photo_del.setImageResource(R.mipmap.img_del_notes);
                tv_text_del.setText(context.getString(R.string.del_notekie));
                break;
            case "stuffkie":
                iv_photo_del.setImageResource(R.mipmap.img_del_stuffkie);
                tv_text_del.setText(context.getString(R.string.del_stuffkie));
                break;
            case "worldkie":
                iv_photo_del.setImageResource(R.mipmap.img_del_worldkie);
                tv_text_del.setText(context.getString(R.string.del_worldkie));
                break;
            case "characterkie":
                iv_photo_del.setImageResource(R.mipmap.img_del_characterkie);
                tv_text_del.setText(context.getString(R.string.del_characterkie));
                break;
            case "scenariokie":
                iv_photo_del.setImageResource(R.mipmap.img_del_scenariokie);
                tv_text_del.setText("¿Deseas eliminar el scenariokie? No podras recuperarla.");
                break;
        }
        setListeners();
        setDialogProperties();
    }
    private void setListeners(){
        ib_close_dialog.setOnClickListener(this);
        ib_accept_dialog.setOnClickListener(this);
    }
    private void initComponents(){
        ib_close_dialog = findViewById(R.id.ib_close_dialog);
        ib_accept_dialog = findViewById(R.id.ib_accept_dialog);
        iv_photo_del = findViewById(R.id.iv_photo_del);
        tv_text_del = findViewById(R.id.tv_text_del);
        tv_title_del = findViewById(R.id.tv_title_del);
        animationView = findViewById(R.id.anim_del);
    }
    private void prepareLoading(){
        tv_title_del.setVisibility(View.GONE);
        tv_text_del.setVisibility(View.GONE);
        iv_photo_del.setVisibility(View.GONE);
        ib_close_dialog.setVisibility(View.GONE);
        ib_accept_dialog.setVisibility(View.GONE);
        animationView.setVisibility(View.VISIBLE);
    }
    private void deleteNotekie() {
        prepareLoading();
        context.getCollectionNotekies().document(UID).delete()
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
        context.getCollectionWorldkies().document(UID).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        WorldkieModel worldkieModel = WorldkieModel.fromSnapshot(documentSnapshot);


                        context.getCollectionWorldkies().document(UID).delete()
                                .addOnSuccessListener(unused -> {
                                    if (!worldkieModel.isPhoto_default()) {
                                        Log.d("DeleteWorldkie", "Intentando eliminar la foto del Storage.");
                                        context.getFirebaseStorageWorldkies().getReference().child(UID).delete()
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
        context.getCollectionCharacterkies().document(UID).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        CharacterkieModel characterkieModel = CharacterkieModel.fromSnapshot(documentSnapshot);

                        context.getCollectionCharacterkies().document(UID).delete()
                                .addOnSuccessListener(unused -> {
                                    Log.d("DeleteWorldkie", "Eliminación del documento Firestore exitosa.");
                                    if (!characterkieModel.isPhoto_default()) {
                                        Log.d("DeleteWorldkie", "Intentando eliminar la foto del Storage.");
                                        context.getFirebaseStorageCharacterkies().getReference().child(UID).delete()
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
        context.getCollectionStuffkies().document(UID).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        StuffkieModel stuffkieModel = StuffkieModel.fromSnapshot(documentSnapshot);
                        context.getCollectionStuffkies().document(UID).delete()
                                .addOnSuccessListener(unused -> {
                                    Log.d("DeleteWorldkie", "Eliminación del documento Firestore exitosa.");
                                    if (!stuffkieModel.isPhoto_default()) {
                                        Log.d("DeleteWorldkie", "Intentando eliminar la foto del Storage.");
                                        context.getFirebaseStorageStuffkies().getReference().child(UID).delete()
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
        context.getCollectionScenariokies().document(UID).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        ScenariokieModel scenariokieModel = ScenariokieModel.fromSnapshot(documentSnapshot);

                        context.getCollectionScenariokies().document(UID).delete()
                                .addOnSuccessListener(unused -> {
                                    Log.d("DeleteWorldkie", "Eliminación del documento Firestore exitosa.");
                                    if (!scenariokieModel.isPhoto_default()) {
                                        Log.d("DeleteWorldkie", "Intentando eliminar la foto del Storage.");
                                        context.getFirebaseStorageScenariokies().getReference().child(UID).delete()
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
    private void setDialogProperties(){
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ib_close_dialog) {
            dismiss();
        } else if (v.getId() == R.id.ib_accept_dialog) {
            switch (mode) {
                case "notekie":
                    deleteNotekie();
                    break;
                case "worldkie":
                    deleteWorldkie();
                    break;
                case "characterkie":
                    deleteCharacterkie();
                    break;
                case "stuffkie":
                    deleteStuffkie();
                    break;
                case "scenariokie":
                    deleteScenariokie();
                    break;
            }
        }
    }
}
