package com.example.buuktu.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.airbnb.lottie.LottieAnimationView;
import com.example.buuktu.R;
import com.example.buuktu.utils.EfectsUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DeleteGeneralDialog extends Dialog implements View.OnClickListener {
    ImageButton ib_accept_dialog, ib_close_dialog;
    Context context;
    String mode, UID;
    TextView tv_title_del, tv_text_del;
    ImageView iv_photo_del;
    FirebaseFirestore db;
    CollectionReference collectionNotekies,collectionWorldkies;
    LottieAnimationView animationView;
    private FirebaseStorage firebaseStorageWorldkie;

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
        ib_close_dialog = findViewById(R.id.ib_close_dialog);
        ib_accept_dialog = findViewById(R.id.ib_accept_dialog);
        iv_photo_del = findViewById(R.id.iv_photo_del);
        tv_text_del = findViewById(R.id.tv_text_del);
        db = FirebaseFirestore.getInstance();
        animationView = findViewById(R.id.anim_del);
        //tv_text_del = findViewById(R.id.tv);
        tv_title_del = findViewById(R.id.tv_title_del);
        tv_title_del.setText("Cuidado");
        switch (mode) {
            case "notekie":
                iv_photo_del.setImageResource(R.mipmap.img_del_notes);
                tv_text_del.setText("¿Deseas eliminar la nota? No podras recuperarla.");
                collectionNotekies = db.collection("Notekies");
                break;
            case "stuffkie":
                iv_photo_del.setImageResource(R.mipmap.img_del_stuffkie);
                tv_text_del.setText("¿Deseas eliminar el stuffkie? No podras recuperarla.");
                break;
            case "worldkie":
                iv_photo_del.setImageResource(R.mipmap.img_del_worldkie);
                tv_text_del.setText("¿Deseas eliminar el worldkie? No podras recuperarla.");
                collectionWorldkies = db.collection("Worldkies");
                firebaseStorageWorldkie = FirebaseStorage.getInstance("gs://buuk-tu-worldkies");
                break;
            case "characterkie":
                iv_photo_del.setImageResource(R.mipmap.img_del_characterkie);
                tv_text_del.setText("¿Deseas eliminar el characterkie? No podras recuperarla.");
                break;
        }
        //tv_text_del = findViewById(R.id.tv);
        ib_close_dialog.setOnClickListener(this);
        ib_accept_dialog.setOnClickListener(this);
        setCanceledOnTouchOutside(false);
        setCancelable(false);

        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    private void deleteNotekie() {

        tv_title_del.setVisibility(View.GONE);
        tv_text_del.setVisibility(View.GONE);
        iv_photo_del.setVisibility(View.GONE);
        ib_close_dialog.setVisibility(View.GONE);
        ib_accept_dialog.setVisibility(View.GONE);
        animationView.setVisibility(View.VISIBLE);
        animationView.setAnimation(R.raw.reading_anim);
        animationView.playAnimation();
        collectionNotekies.document(UID).delete()
                .addOnSuccessListener(unused -> {
                    EfectsUtils.setAnimationsDialog("success", animationView);

                    Completable.timer(5, TimeUnit.SECONDS)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(this::dismiss);
                })
                .addOnFailureListener(e -> {
                    EfectsUtils.setAnimationsDialog("fail", animationView);

                    Completable.timer(5, TimeUnit.SECONDS)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(this::dismiss);
                });
    }
    private void deleteWorldkie(){

        tv_title_del.setVisibility(View.GONE);
        tv_text_del.setVisibility(View.GONE);
        iv_photo_del.setVisibility(View.GONE);
        ib_close_dialog.setVisibility(View.GONE);
        ib_accept_dialog.setVisibility(View.GONE);
        animationView.setVisibility(View.VISIBLE);
            animationView.setVisibility(View.VISIBLE);
            animationView.setAnimation(R.raw.reading_anim);
            animationView.playAnimation();
            collectionWorldkies.document(UID).delete().addOnSuccessListener(unused -> firebaseStorageWorldkie.getReference().child(UID).delete().addOnSuccessListener(unused1 -> {
                animationView.setAnimation(R.raw.success_anim);
                animationView.playAnimation();
                Completable.timer(5, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            dismiss();
                        });
            }).addOnFailureListener(e -> {
                animationView.setAnimation(R.raw.fail_anim);
                animationView.playAnimation();
                Completable.timer(5, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            dismiss();
                        });
            })).addOnFailureListener(e -> {

            });
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
            }
        }
    }
}
