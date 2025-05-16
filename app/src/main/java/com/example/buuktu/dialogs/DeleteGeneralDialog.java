package com.example.buuktu.dialogs;

import android.app.Dialog;
import android.os.Bundle;
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
import com.example.buuktu.utils.DrawableUtils;
import com.example.buuktu.utils.EfectsUtils;
import com.example.buuktu.views.MainActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DeleteGeneralDialog extends Dialog implements View.OnClickListener {
    final MainActivity context;
    final String mode;
    final String UID;
    ImageButton ib_accept_dialog, ib_close_dialog;
    TextView tv_title_del;
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

        customizeImage();
        setListeners();
        setDialogProperties();
    }

    private void customizeImage() {
        switch (mode) {
            case "notekie":
                iv_photo_del.setImageResource(R.mipmap.img_del_notes);
                break;
            case "stuffkie":
                iv_photo_del.setImageResource(R.mipmap.img_del_stuffkie);
                break;
            case "worldkie":
                iv_photo_del.setImageResource(R.mipmap.img_del_worldkie);
                break;
            case "characterkie":
                iv_photo_del.setImageResource(R.mipmap.img_del_characterkie);
                break;
            case "scenariokie":
                iv_photo_del.setImageResource(R.mipmap.img_del_scenariokie);
                break;
        }
        DrawableUtils.personalizarImagenCuadradoButton(context, 12, 3, R.color.purple1, iv_photo_del.getDrawable(), iv_photo_del);
    }

    private void setListeners() {
        ib_close_dialog.setOnClickListener(this);
        ib_accept_dialog.setOnClickListener(this);
    }

    private void initComponents() {
        ib_close_dialog = findViewById(R.id.ib_close_dialog);
        ib_accept_dialog = findViewById(R.id.ib_accept_dialog);
        iv_photo_del = findViewById(R.id.iv_info_dialog);
        tv_title_del = findViewById(R.id.tv_title_del);
        animationView = findViewById(R.id.anim_del);
    }

    private void prepareLoading() {
        tv_title_del.setVisibility(View.GONE);
        iv_photo_del.setVisibility(View.GONE);
        ib_close_dialog.setVisibility(View.GONE);
        ib_accept_dialog.setVisibility(View.GONE);
        animationView.setVisibility(View.VISIBLE);
    }

    private void deleteNotekie() {
        prepareLoading();
        context.getCollectionNotekies().document(UID).delete().addOnSuccessListener(unused -> failSuccess("success")).addOnFailureListener(e -> failSuccess("fail"));
    }

    private void deleteWorldkie() {
        prepareLoading();
        context.getCollectionWorldkies().document(UID).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                WorldkieModel worldkieModel = WorldkieModel.fromSnapshot(documentSnapshot);


                context.getCollectionWorldkies().document(UID).delete().addOnSuccessListener(unused -> {
                    if (!worldkieModel.isPhoto_default()) {
                        context.getFirebaseStorageWorldkies().getReference().child(UID).delete().addOnSuccessListener(unused1 -> failSuccess("success"));
                    } else {
                        failSuccess("success");

                    }
                }).addOnFailureListener(e -> failSuccess("fail"));

            }
        }).addOnFailureListener(e -> failSuccess("fail"));
    }

    private void deleteCharacterkie() {
        prepareLoading();
        context.getCollectionCharacterkies().document(UID).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                CharacterkieModel characterkieModel = CharacterkieModel.fromSnapshot(documentSnapshot);

                context.getCollectionCharacterkies().document(UID).delete().addOnSuccessListener(unused -> {
                    if (!characterkieModel.isPhoto_default()) {
                        context.getFirebaseStorageCharacterkies().getReference().child(UID).delete().addOnSuccessListener(unused1 -> failSuccess("success"));
                    } else {
                        failSuccess("success");

                    }
                }).addOnFailureListener(e -> failSuccess("fail"));

            }
        }).addOnFailureListener(e -> failSuccess("fail"));
    }

    private void deleteStuffkie() {
        prepareLoading();
        context.getCollectionStuffkies().document(UID).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                StuffkieModel stuffkieModel = StuffkieModel.fromSnapshot(documentSnapshot);
                context.getCollectionStuffkies().document(UID).delete().addOnSuccessListener(unused -> {
                    if (!stuffkieModel.isPhoto_default()) {
                        context.getFirebaseStorageStuffkies().getReference().child(UID).delete().addOnSuccessListener(unused1 -> failSuccess("success"));
                    } else {
                        failSuccess("success");

                    }
                }).addOnFailureListener(e -> failSuccess("fail"));

            }
        }).addOnFailureListener(e -> failSuccess("fail"));
    }

    private void deleteScenariokie() {
        prepareLoading();
        context.getCollectionScenariokies().document(UID).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                ScenariokieModel scenariokieModel = ScenariokieModel.fromSnapshot(documentSnapshot);

                context.getCollectionScenariokies().document(UID).delete().addOnSuccessListener(unused -> {
                    if (!scenariokieModel.isPhoto_default()) {
                        context.getFirebaseStorageScenariokies().getReference().child(UID).delete().addOnSuccessListener(unused1 -> failSuccess("success"));
                    } else {
                        failSuccess("success");

                    }
                }).addOnFailureListener(e -> failSuccess("fail"));

            }
        }).addOnFailureListener(e -> failSuccess("fail"));
    }

    private void failSuccess(String text) {
        EfectsUtils.setAnimationsDialog(text, animationView);
        delayedDismiss();
    }

    private void delayedDismiss() {
        Completable.timer(2, TimeUnit.SECONDS).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(this::dismiss);
    }

    private void setDialogProperties() {
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
