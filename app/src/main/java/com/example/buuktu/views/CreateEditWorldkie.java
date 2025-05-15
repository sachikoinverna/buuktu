package com.example.buuktu.views;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Switch;

import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.buuktu.R;
import com.example.buuktu.utils.CheckUtil;
import com.example.buuktu.utils.RoundedBorderSquareTransformation;
import com.example.buuktu.bottomsheet.BottomSheetProfilePhoto;
import com.example.buuktu.dialogs.CreateEditGeneralDialog;
import com.example.buuktu.models.WorldkieModel;
import com.example.buuktu.utils.DrawableUtils;
import com.example.buuktu.utils.EfectsUtils;
import com.example.buuktu.utils.NavigationUtils;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class CreateEditWorldkie extends Fragment implements View.OnClickListener {
    MainActivity mainActivity;
    private WorldkieModel worldkieModel;
    TextInputLayout et_nameWorldkieCreateFull;
    TextInputEditText et_nameWorldkieCreate;
    Switch tb_worldkiePrivacity,tb_worldkieDraft;
    ImageButton ib_save,ib_select_img_create_worldkie,ib_back,ib_profile_superior;
    CreateEditGeneralDialog dialog;
    Uri image;
    BottomSheetProfilePhoto bottomSheetProfilePhoto;
    FragmentManager fragmentManager;
    String worldkie_id;
    LottieAnimationView animationView;
    public CreateEditWorldkie() {
    }

    public static CreateEditWorldkie newInstance() {
        return new CreateEditWorldkie();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
                this.worldkie_id = getArguments().getString("worldkie_id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_edit_worldkie, container, false);
        mainActivity = (MainActivity) getActivity();
        fragmentManager = requireActivity().getSupportFragmentManager();
        initComponents(view);
        setVisibility();
        dialog = new CreateEditGeneralDialog(mainActivity);

        bottomSheetProfilePhoto = new BottomSheetProfilePhoto();

        worldkieModel = new WorldkieModel();
        setWorldkieModel();
        setListeners();
        return view;
    }
    public void setWorldkieModel(){
        if(worldkie_id == null){
            try {
                createMode();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else{
            mainActivity.getCollectionWorldkies().document(worldkie_id).addSnapshotListener((queryDocumentSnapshot, e) -> {

                if (e != null) {
                    return;
                }
                if (queryDocumentSnapshot!=null) {
                    worldkieModel = WorldkieModel.fromSnapshot(queryDocumentSnapshot);
                    editMode();
                }
            });

        }
    }
    public void editMode(){
        et_nameWorldkieCreate.setText(worldkieModel.getName());
        tb_worldkiePrivacity.setChecked(worldkieModel.isWorldkie_private());
        tb_worldkieDraft.setVisibility(worldkieModel.isWorldkie_private()?View.VISIBLE:View.GONE);
        tb_worldkieDraft.setChecked(worldkieModel.isDraft());
        getImage();
    }
    private void setVisibility(){
        ib_save.setVisibility(View.VISIBLE);
        ib_profile_superior.setVisibility(View.VISIBLE);
        ib_back.setVisibility(View.VISIBLE);
        ib_select_img_create_worldkie.setVisibility(View.INVISIBLE);
    }
    private void setListeners() {
        ib_select_img_create_worldkie.setOnClickListener(this);
        ib_back.setOnClickListener(this);
        ib_save.setOnClickListener(this);
        tb_worldkiePrivacity.setOnCheckedChangeListener((buttonView, isChecked) -> {
            tb_worldkieDraft.setVisibility(
                    isChecked ? View.VISIBLE : View.GONE);
            worldkieModel.setWorldkie_private(isChecked);
        });
        tb_worldkieDraft.setOnCheckedChangeListener((buttonView, isChecked) -> worldkieModel.setDraft(isChecked));
    }
    private void getImage(){
        if(worldkieModel.isPhoto_default()){
            int resId = mainActivity.getResources().getIdentifier(worldkieModel.getId_photo(), "mipmap", mainActivity.getPackageName());

            if (resId != 0) {
                Drawable drawable = ContextCompat.getDrawable(mainActivity, resId);
                ib_select_img_create_worldkie.setImageDrawable(drawable);
                ib_select_img_create_worldkie.setTag(DrawableUtils.getMipmapName(mainActivity,resId));

                try {
                    DrawableUtils.personalizarImagenCuadradoButton(mainActivity,150/7,7,R.color.brownMaroon,drawable, ib_select_img_create_worldkie);
                    ib_select_img_create_worldkie.setVisibility(View.VISIBLE);
                    EfectsUtils.startCircularReveal(drawable,ib_select_img_create_worldkie);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
    } else {
        mainActivity.getFirebaseStorageWorldkies().getReference(worldkie_id).listAll().addOnSuccessListener(listResult -> {
            for (StorageReference item : listResult.getItems()) {
                if (item.getName().startsWith("cover")) {
                    item.getDownloadUrl().addOnSuccessListener(uri -> {

                        DrawableUtils.personalizarImagenCuadradoButton(getContext(),150/6,7,R.color.brownMaroon,uri,ib_select_img_create_worldkie);
                        ib_select_img_create_worldkie.setVisibility(View.VISIBLE);
                        EfectsUtils.startCircularReveal(ib_select_img_create_worldkie.getDrawable(),ib_select_img_create_worldkie);
                    });
                break;
                }
            }
        });
    }
    }
    public void createMode() throws IOException {
        et_nameWorldkieCreate.setText("");
        tb_worldkiePrivacity.setChecked(false);
        tb_worldkieDraft.setVisibility(View.GONE);
        putDefaultImage();
        worldkieModel.setUID_AUTHOR(mainActivity.getUID());
        worldkieModel.setPhoto_default(true);
        worldkieModel.setWorldkie_private(false);
        ib_select_img_create_worldkie.setTag(DrawableUtils.getMipmapName(mainActivity,R.mipmap.photoworldkieone));
        worldkieModel.setId_photo(ib_select_img_create_worldkie.getTag().toString());
    }
    public void setSelectedProfilePhoto(Drawable image){

        int cornerRadius = 150 / 6;
        int borderWidth = 7;
        int borderColor = getContext().getResources().getColor(R.color.brownMaroon, null);


        RequestOptions requestOptions = new RequestOptions()
                .centerCrop()
                .transform(new RoundedBorderSquareTransformation(cornerRadius,borderWidth,borderColor));

        Glide.with(getContext())
                .load(DrawableUtils.drawableToBitmap(image))
                .apply(requestOptions)
                .into(ib_select_img_create_worldkie);

    }
    public void setSelectedProfilePhoto(@DrawableRes int imageResId){
        DrawableUtils.personalizarImagenCuadradoButton(getContext(),150/6,7,R.color.brownMaroon,imageResId,ib_select_img_create_worldkie);

    }

    public void selectImage(){
        bottomSheetProfilePhoto.show(getChildFragmentManager(),"BottomSheetProfilePhoto");
    }
    public ImageButton getIb_select_img_create_worldkie() {
        return ib_select_img_create_worldkie;
    }


    public void setPhotoNoDefault(){
        worldkieModel.setPhoto_default(false);
        worldkieModel.setId_photo(null);
    }
    public void setPhotoDefault(){
        worldkieModel.setPhoto_default(true);
        worldkieModel.setId_photo(ib_select_img_create_worldkie.getTag().toString());
    }
    public void setImageUri(Uri image){
        this.image=image;
    }

    private void putDefaultImage() throws IOException {
        Drawable drawable = ContextCompat.getDrawable(mainActivity, R.mipmap.photoworldkieone);
        DrawableUtils.personalizarImagenCuadradoButton(mainActivity,115/6,7,R.color.brownMaroon,drawable, ib_select_img_create_worldkie);
        ib_select_img_create_worldkie.setVisibility(View.VISIBLE);

    }

  private void delayedDismiss() {
      Completable.timer(2, TimeUnit.SECONDS)
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(() -> dialog.dismiss());
  }
    private void addDataToFirestore() {
            Completable.timer(3, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                                Task<DocumentReference> addTask = mainActivity.getCollectionWorldkies().add(worldkieModel.toMap());

                                addTask.addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        worldkie_id = addTask.getResult().getId();

                                        success();

                                    }
                                }).addOnFailureListener(e -> fail());
                            }
                    );

    }
    private void save(){
        if(CheckUtil.handlerCheckName(mainActivity,et_nameWorldkieCreate,et_nameWorldkieCreateFull)) {
            worldkieModel.setName(et_nameWorldkieCreate.getText().toString());
            worldkieModel.setCreation_date(worldkie_id==null?new Timestamp(Instant.now()):null);
            worldkieModel.setLast_update(new Timestamp(Instant.now()));
            dialog.show();
            animationView = dialog.getAnimationView();
            if(worldkie_id == null){
                addDataToFirestore();
            }else{
                editDataFirestore();
            }
        }
    }
    private void editDataFirestore() {
            Completable.timer(3, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> mainActivity.getCollectionWorldkies().document(worldkie_id).update(worldkieModel.toMap()).addOnSuccessListener(unused -> success()).addOnFailureListener(e -> fail())
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
            if (!worldkieModel.isPhoto_default()) {
                mainActivity.getFirebaseStorageWorldkies().getReference().child(worldkie_id).child("cover" + DrawableUtils.getExtensionFromUri(getContext(), image)).putFile(image);

            }
        }

    private void initComponents(View view){
        mainActivity = (MainActivity) getActivity();
        ib_back = mainActivity.getBackButton();
        ib_save = mainActivity.getIb_save();
        ib_profile_superior = mainActivity.getIb_self_profile();
        et_nameWorldkieCreateFull = view.findViewById(R.id.et_nameWorldkieCreateFull);
        et_nameWorldkieCreate = view.findViewById(R.id.et_nameWorldkieCreate);
        tb_worldkiePrivacity = view.findViewById(R.id.tb_worldkiePrivacity);
        tb_worldkieDraft = view.findViewById(R.id.tb_worldkieDraft);
        ib_select_img_create_worldkie = view.findViewById(R.id.ib_select_img_create_worldkie);
    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.ib_select_img_create_worldkie){
            selectImage();
        } else if (v.getId()==R.id.ib_save) {
                   save();
        }
        // Comprueba si se ha presionado el botón de retroceso.
        else if (v.getId()==R.id.ib_back) {
            NavigationUtils.goBack(fragmentManager,mainActivity);
        } else if(v.getId()==R.id.ib_save){
                    if(worldkie_id == null){
                        addDataToFirestore();
                    }else{
                        editDataFirestore();
                    }
                }
        }
    }
