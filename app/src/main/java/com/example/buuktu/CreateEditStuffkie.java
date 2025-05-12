package com.example.buuktu;

import static android.widget.Toast.LENGTH_LONG;

import android.animation.Animator;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.DrawableRes;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.buuktu.dialogs.CreateEditGeneralDialog;
import com.example.buuktu.models.StuffkieModel;
import com.example.buuktu.utils.CheckUtil;
import com.example.buuktu.utils.EfectsUtils;
import com.example.buuktu.utils.RoundedBorderSquareTransformation;
import com.example.buuktu.bottomsheet.BottomSheetProfilePhoto;
import com.example.buuktu.utils.DrawableUtils;
import com.example.buuktu.utils.NavigationUtils;
import com.example.buuktu.views.MainActivity;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateEditStuffkie#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateEditStuffkie extends Fragment implements View.OnClickListener{
    ImageButton ib_select_img_create_stuffkie,ib_back,ib_save;
    Uri image;
    BottomSheetProfilePhoto bottomSheetProfilePhoto;
    String source,stuffkie_id,UID,worldkie_id;
    FragmentManager fragmentManager;
    TextInputEditText et_nameStuffkieCreate;
    TextInputLayout et_nameStuffkieCreateFull;
    Switch tb_stuffkiePrivacity,tb_stuffkieDraft;
    MainActivity mainActivity;
    CreateEditGeneralDialog dialog;
    FirebaseStorage storage;
    LottieAnimationView animationView;
    StuffkieModel stuffkieModel;
    CollectionReference collectionStuffkie;
    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;
    public CreateEditStuffkie() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CreateEditStuffkie.
     */
    public static CreateEditStuffkie newInstance() {
        return new CreateEditStuffkie();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (getArguments().containsKey("stuffkie_id")) {
                this.stuffkie_id = getArguments().getString("stuffkie_id");
            }
            if (getArguments().containsKey("worldkie_id")) {
                this.worldkie_id = getArguments().getString("worldkie_id");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_edit_stuffkie, container, false);

        initComponents(view);
        setVisibility();
        setListeners();
        source = "app";
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance("gs://buuk-tu-stuffkies");
        UID = firebaseAuth.getUid();
        assert UID != null;
        Log.d("HOLA",UID);
        collectionStuffkie = db.collection("Stuffkies");
        bottomSheetProfilePhoto = new BottomSheetProfilePhoto();
        dialog = new CreateEditGeneralDialog(mainActivity);
        try {
            putDefaultImage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(stuffkie_id == null){
            try {
                stuffkieModel = new StuffkieModel();
                createMode();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else{
            collectionStuffkie.document(stuffkie_id).addSnapshotListener((queryDocumentSnapshot, e) -> {
                if (e != null) {
                    Log.e("Error", e.getMessage());
                    Toast.makeText(getContext(), "Error al escuchar cambios: " + e.getMessage(), LENGTH_LONG).show();
                    return;
                }
                if (queryDocumentSnapshot!=null) {
                    stuffkieModel = StuffkieModel.fromSnapshot(queryDocumentSnapshot);
                    et_nameStuffkieCreate.setText(stuffkieModel.getName());
                    tb_stuffkiePrivacity.setChecked(stuffkieModel.isStuffkie_private());
                    tb_stuffkieDraft.setVisibility(stuffkieModel.isStuffkie_private()?View.VISIBLE:View.GONE);
                    tb_stuffkieDraft.setChecked(stuffkieModel.isDraft());
                    getImage();
                }
            });

            editMode(stuffkieModel);
        }
        return view;
    }
    private void getImage(){
        if(stuffkieModel.isPhoto_default()){
            int resId = mainActivity.getResources().getIdentifier(stuffkieModel.getPhoto_id(), "mipmap", mainActivity.getPackageName());

            if (resId != 0) {
                Drawable drawable = ContextCompat.getDrawable(mainActivity, resId);
                ib_select_img_create_stuffkie.setImageDrawable(drawable);
                source = "app";
                ib_select_img_create_stuffkie.setTag(DrawableUtils.getMipmapName(mainActivity,resId));

                try {
                    DrawableUtils.personalizarImagenCuadradoButton(mainActivity,150/7,7,R.color.brownMaroon,drawable, ib_select_img_create_stuffkie);
                    ib_select_img_create_stuffkie.setVisibility(View.VISIBLE);
                    EfectsUtils.startCircularReveal(drawable,ib_select_img_create_stuffkie);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        } else {
            StorageReference userFolderRef = FirebaseStorage.getInstance("gs://buuk-tu-worldkies").getReference(stuffkie_id);

            userFolderRef.listAll().addOnSuccessListener(listResult -> {
                for (StorageReference item : listResult.getItems()) {
                    if (item.getName().startsWith("cover")) {
                        item.getDownloadUrl().addOnSuccessListener(uri -> {

                            DrawableUtils.personalizarImagenCuadradoButton(getContext(),150/6,7,R.color.brownMaroon,uri,ib_select_img_create_stuffkie);
                            ib_select_img_create_stuffkie.setVisibility(View.VISIBLE);
                            EfectsUtils.startCircularReveal(ib_select_img_create_stuffkie.getDrawable(),ib_select_img_create_stuffkie);
                            source = "device";
                        });
                        break;
                    }
                }
            });
        }
    }
    private void setVisibility(){
        ib_save.setVisibility(View.VISIBLE);
        ib_back.setVisibility(View.VISIBLE);
        tb_stuffkieDraft.setVisibility(View.INVISIBLE);

    }
    public void setPhotoNoDefault(){
        stuffkieModel.setPhoto_default(false);
        stuffkieModel.setPhoto_id(null);
    }
    public void setPhotoDefault(){
        stuffkieModel.setPhoto_default(true);
        stuffkieModel.setPhoto_id(ib_select_img_create_stuffkie.getTag().toString());
    }
    private void initComponents(View view){
        ib_select_img_create_stuffkie = view.findViewById(R.id.ib_select_img_create_stuffkie);
        et_nameStuffkieCreateFull = view.findViewById(R.id.et_nameStuffkieCreateFull);
        et_nameStuffkieCreate = view.findViewById(R.id.et_nameStuffkieCreate);
        tb_stuffkiePrivacity = view.findViewById(R.id.tb_stuffkiePrivacity);
        tb_stuffkieDraft = view.findViewById(R.id.tb_stuffkieDraft);
        mainActivity = (MainActivity) getActivity();
        ib_back = mainActivity.getBackButton();
        ib_save = mainActivity.getIb_save();
        fragmentManager = mainActivity.getSupportFragmentManager();
    }
    public void createMode() throws IOException {
        et_nameStuffkieCreate.setText("");
        tb_stuffkiePrivacity.setChecked(false);
        tb_stuffkieDraft.setVisibility(View.GONE);
        putDefaultImage();
        source = "app";
        stuffkieModel.setAUTHOR_UID(UID);
        stuffkieModel.setWORDLKIE_UID(worldkie_id);
        stuffkieModel.setPhoto_default(true);
        stuffkieModel.setStuffkie_private(false);
        ib_select_img_create_stuffkie.setTag(DrawableUtils.getMipmapName(mainActivity,R.mipmap.photostuffkieone));
        stuffkieModel.setPhoto_id(ib_select_img_create_stuffkie.getTag().toString());
    }
    public void editMode(StuffkieModel stuffkieModel){
        et_nameStuffkieCreate.setText(stuffkieModel.getName());
        tb_stuffkiePrivacity.setChecked(stuffkieModel.isStuffkie_private());

        if(!stuffkieModel.isStuffkie_private()){
            tb_stuffkieDraft.setVisibility(View.GONE);
        }
        //obtenerImagen();
    }
    private void addDataToFirestore(){
        if(CheckUtil.handlerCheckName(mainActivity,et_nameStuffkieCreate,et_nameStuffkieCreateFull)){
            dialog.show();
            animationView = dialog.getAnimationView();
            stuffkieModel.setName(et_nameStuffkieCreate.getText().toString());
            Completable.timer(3, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                                Task<DocumentReference> addTask = collectionStuffkie.add(stuffkieModel.toMap());

                                addTask.addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {

                                        if (!stuffkieModel.isPhoto_default()) {
                                            StorageReference userRef = storage.getReference().child(task.getResult().getId());
                                            userRef.child("profile" + DrawableUtils.getExtensionFromUri(getContext(), image)).putFile(image);

                                        }
                                        EfectsUtils.setAnimationsDialog("success",animationView);
                                        delayedDismiss();

                                    }
                                }).addOnFailureListener(e -> {
                                    EfectsUtils.setAnimationsDialog("fail",animationView);
                                    delayedDismiss();
                                });
                            }
                    );
        }
    }
    private void editDataFirestore() {
        if (CheckUtil.handlerCheckName(mainActivity, et_nameStuffkieCreate, et_nameStuffkieCreateFull)) {
            if (!stuffkieModel.getName().equals(et_nameStuffkieCreate.getText().toString())) {
                stuffkieModel.setName(et_nameStuffkieCreate.getText().toString());
            }
                dialog.show();
                animationView = dialog.getAnimationView();
                Completable.timer(3, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() ->  collectionStuffkie.document(stuffkie_id).update(stuffkieModel.toMap()).addOnSuccessListener(unused -> {
                                    if (!stuffkieModel.isPhoto_default()) {
                                        StorageReference userRef = storage.getReference().child(stuffkie_id);
                                        userRef.child("profile" + DrawableUtils.getExtensionFromUri(getContext(), image)).putFile(image);

                                    }
                                    EfectsUtils.setAnimationsDialog("success", animationView);
                                    delayedDismiss();

                                }).addOnFailureListener(e -> {
                                    EfectsUtils.setAnimationsDialog("fail", animationView);
                                    delayedDismiss();
                                })
                        );
            }
    }

    private void setListeners(){
        ib_save.setOnClickListener(this);
        ib_back.setOnClickListener(this);
        ib_select_img_create_stuffkie.setOnClickListener(this);
        tb_stuffkiePrivacity.setOnCheckedChangeListener((buttonView, isChecked) -> {
            tb_stuffkieDraft.setVisibility(isChecked?View.VISIBLE:View.INVISIBLE);
            stuffkieModel.setStuffkie_private(isChecked);
        });
        tb_stuffkieDraft.setOnCheckedChangeListener((buttonView, isChecked) -> stuffkieModel.setDraft(isChecked));
    }
    public void setImageUri(Uri image){
        this.image=image;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
    private void putDefaultImage() throws IOException {
//        Drawable drawable = ContextCompat.getDrawable(context, R.mipmap.photostuffkieone);
       // DrawableUtils.personalizarImagenCuadradoButton(get);
        DrawableUtils.personalizarImagenCuadradoButton(mainActivity,150/7,7,R.color.brownMaroon,R.mipmap.photostuffkieone,ib_select_img_create_stuffkie);
    }
    public void setSelectedProfilePhoto(Drawable image){
      /*  Bitmap bitmap = Bitmap.createBitmap(
                image.getIntrinsicWidth(),
                image.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        image.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        image.draw(canvas);*/
        int cornerRadius = 150 / 6; // Ejemplo de radio
        int borderWidth = 7; // Ejemplo de grosor del borde
        int borderColor = getContext().getResources().getColor(R.color.brownMaroon, null); // Asegúrate de que el color sea correcto
//        Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.photoworldkieone);
        //ib_select_img_create_worldkie.setImageDrawable(image);
    //       DrawableUtils.
        RequestOptions requestOptions = new RequestOptions()
                // .override(150, 150)
                .centerCrop()
                .transform(new RoundedBorderSquareTransformation(cornerRadius,borderWidth,borderColor));

        Glide.with(getContext())
                .load(DrawableUtils.drawableToBitmap(image))
                .apply(requestOptions)
                .into(ib_select_img_create_stuffkie);
    }
    public void setSelectedProfilePhoto(@DrawableRes int imageResId){
        DrawableUtils.personalizarImagenCuadradoButton(getContext(),150/6,7,R.color.brownMaroon,imageResId,ib_select_img_create_stuffkie);

    }
    private void delayedDismiss() {
        Completable.timer(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    dialog.dismiss();
                });
    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.ib_save){
                if(stuffkie_id == null){
                    addDataToFirestore();
                }/*else{
                    editDataFirestore();
                }*/
            }else if(v.getId()==R.id.ib_back){
            NavigationUtils.goBack(fragmentManager,mainActivity);
        }else if (v.getId()==R.id.ib_select_img_create_stuffkie) {
            bottomSheetProfilePhoto.show(getChildFragmentManager(),"BottomSheetProfilePhoto");
        }
    }
}