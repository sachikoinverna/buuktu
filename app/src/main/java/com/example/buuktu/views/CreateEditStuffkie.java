package com.example.buuktu.views;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Switch;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.buuktu.R;
import com.example.buuktu.dialogs.CreateEditGeneralDialog;
import com.example.buuktu.models.StuffkieModel;
import com.example.buuktu.utils.CheckUtil;
import com.example.buuktu.utils.EfectsUtils;
import com.example.buuktu.utils.RoundedBorderSquareTransformation;
import com.example.buuktu.bottomsheet.BottomSheetProfilePhoto;
import com.example.buuktu.utils.DrawableUtils;
import com.example.buuktu.utils.NavigationUtils;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class CreateEditStuffkie extends Fragment implements View.OnClickListener{
    ImageButton ib_select_img_create_stuffkie,ib_back,ib_save;
    Uri image;
    BottomSheetProfilePhoto bottomSheetProfilePhoto;
    String stuffkie_id,worldkie_id;
    FragmentManager fragmentManager;
    TextInputEditText et_nameStuffkieCreate;
    TextInputLayout et_nameStuffkieCreateFull;
    Switch tb_stuffkiePrivacity,tb_stuffkieDraft;
    MainActivity mainActivity;
    CreateEditGeneralDialog dialog;
    LottieAnimationView animationView;
    StuffkieModel stuffkieModel;
    public CreateEditStuffkie() {
    }


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
        View view = inflater.inflate(R.layout.fragment_create_edit_stuffkie, container, false);

        initComponents(view);
        setVisibility();
        setListeners();

        setStuffkieModel();
        return view;
    }
    private void setStuffkieModel(){
        if(stuffkie_id == null){
            try {
                stuffkieModel = new StuffkieModel();
                createMode();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else{
            mainActivity.getCollectionStuffkies().document(stuffkie_id).addSnapshotListener((queryDocumentSnapshot, e) -> {
                if (e != null) {
                    return;
                }
                if (queryDocumentSnapshot!=null) {
                    stuffkieModel = StuffkieModel.fromSnapshot(queryDocumentSnapshot);
                    editMode();
                }
            });

        }
    }
    public void editMode(){
        et_nameStuffkieCreate.setText(stuffkieModel.getName());
        tb_stuffkiePrivacity.setChecked(stuffkieModel.isStuffkie_private());
        tb_stuffkieDraft.setVisibility(stuffkieModel.isStuffkie_private()?View.VISIBLE:View.GONE);
        tb_stuffkieDraft.setChecked(stuffkieModel.isDraft());
        getImage();
    }
    private void getImage(){
        if(stuffkieModel.isPhoto_default()){
            int resId = mainActivity.getResources().getIdentifier(stuffkieModel.getPhoto_id(), "mipmap", mainActivity.getPackageName());

            if (resId != 0) {
                Drawable drawable = ContextCompat.getDrawable(mainActivity, resId);
                ib_select_img_create_stuffkie.setImageDrawable(drawable);
                ib_select_img_create_stuffkie.setTag(DrawableUtils.getMipmapName(mainActivity,resId));
                 DrawableUtils.personalizarImagenCuadradoButton(mainActivity,150/7,7,R.color.brownMaroon,drawable, ib_select_img_create_stuffkie);
                    ib_select_img_create_stuffkie.setVisibility(View.VISIBLE);
                    EfectsUtils.startCircularReveal(drawable,ib_select_img_create_stuffkie);
            }
        } else {
            mainActivity.getFirebaseStorageStuffkies().getReference(stuffkie_id).listAll().addOnSuccessListener(listResult -> {
                for (StorageReference item : listResult.getItems()) {
                    if (item.getName().startsWith("cover")) {
                        item.getDownloadUrl().addOnSuccessListener(uri -> {

                            DrawableUtils.personalizarImagenCuadradoButton(getContext(),150/6,7,R.color.brownMaroon,uri,ib_select_img_create_stuffkie);
                            ib_select_img_create_stuffkie.setVisibility(View.VISIBLE);
                            EfectsUtils.startCircularReveal(ib_select_img_create_stuffkie.getDrawable(),ib_select_img_create_stuffkie);
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
        bottomSheetProfilePhoto = new BottomSheetProfilePhoto();
        dialog = new CreateEditGeneralDialog(mainActivity);
    }
    public void createMode() throws IOException {
        et_nameStuffkieCreate.setText("");
        tb_stuffkiePrivacity.setChecked(false);
        tb_stuffkieDraft.setVisibility(View.GONE);
        putDefaultImage();
        stuffkieModel.setAUTHOR_UID(mainActivity.getUID());
        stuffkieModel.setWORDLKIE_UID(worldkie_id);
        stuffkieModel.setPhoto_default(true);
        stuffkieModel.setStuffkie_private(false);
        ib_select_img_create_stuffkie.setTag(DrawableUtils.getMipmapName(mainActivity,R.mipmap.photostuffkieone));
        stuffkieModel.setPhoto_id(ib_select_img_create_stuffkie.getTag().toString());
    }

    private void addDataToFirestore(){

            Completable.timer(3, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                                Task<DocumentReference> addTask = mainActivity.getCollectionStuffkies().add(stuffkieModel.toMap());

                                addTask.addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        stuffkie_id = addTask.getResult().getId();
                                        success();

                                    }
                                }).addOnFailureListener(e -> fail());
                            }
                    );
    }
    private void editDataFirestore() {
                Completable.timer(3, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() ->  mainActivity.getCollectionStuffkies().document(stuffkie_id).update(stuffkieModel.toMap()).addOnSuccessListener(unused -> success()).addOnFailureListener(e -> fail())
                        );
    }
    private void success(){
        uploadNewImage();
        EfectsUtils.setAnimationsDialog("success", animationView);
        delayedDismiss();
    }
    private void fail(){
        EfectsUtils.setAnimationsDialog("fail", animationView);
        delayedDismiss();
    }
private void uploadNewImage(){
    if (!stuffkieModel.isPhoto_default()) {
        mainActivity.getFirebaseStorageStuffkies().getReference().child(stuffkie_id).child("cover" + DrawableUtils.getExtensionFromUri(mainActivity, image)).putFile(image);

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

    private void putDefaultImage() throws IOException {
        DrawableUtils.personalizarImagenCuadradoButton(mainActivity,150/7,7,R.color.brownMaroon,R.mipmap.photostuffkieone,ib_select_img_create_stuffkie);
    }
    public void setSelectedProfilePhoto(Drawable image){
        int cornerRadius = 150 / 6; // Ejemplo de radio
        int borderWidth = 7; // Ejemplo de grosor del borde
        int borderColor = getContext().getResources().getColor(R.color.brownMaroon, null); // Asegúrate de que el color sea correcto

        RequestOptions requestOptions = new RequestOptions()
                .centerCrop()
                .transform(new RoundedBorderSquareTransformation(cornerRadius,borderWidth,borderColor));

        Glide.with(getContext())
                .load(DrawableUtils.drawableToBitmap(image))
                .apply(requestOptions)
                .into(ib_select_img_create_stuffkie);
    }

    public ImageButton getIb_select_img_create_stuffkie() {
        return ib_select_img_create_stuffkie;
    }

    private void delayedDismiss() {
        Completable.timer(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> dialog.dismiss());
    }
    private void save(){
        if(CheckUtil.handlerCheckName(mainActivity,et_nameStuffkieCreate,et_nameStuffkieCreateFull)) {
            dialog.show();
            animationView = dialog.getAnimationView();
            stuffkieModel.setName(et_nameStuffkieCreate.getText().toString());
            if (stuffkie_id == null) {
                addDataToFirestore();
            }else{
                editDataFirestore();
            }
        }
    }
    private void selectImage (){
        bottomSheetProfilePhoto.show(getChildFragmentManager(),"BottomSheetProfilePhoto");

    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.ib_save){
            save();
        }
        else if(v.getId()==R.id.ib_back){
            NavigationUtils.goBack(fragmentManager,mainActivity);
        }else if (v.getId()==R.id.ib_select_img_create_stuffkie) {
        selectImage();
        }
    }
}