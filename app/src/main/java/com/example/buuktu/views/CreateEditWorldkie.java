package com.example.buuktu.views;

import static android.widget.Toast.LENGTH_LONG;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateEditWorldkie#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateEditWorldkie extends Fragment implements View.OnClickListener {
    MainActivity mainActivity;
    private FirebaseFirestore db;
    CollectionReference collectionWorldkie;
    private FirebaseAuth firebaseAuth;
    private final FirebaseStorage storage = FirebaseStorage.getInstance("gs://buuk-tu-worldkies");
    private WorldkieModel worldkieModel;
    TextInputLayout et_nameWorldkieCreateFull;
    TextInputEditText et_nameWorldkieCreate;
    Switch tb_worldkiePrivacity,tb_worldkieDraft;
    ImageButton ib_save,ib_select_img_create_worldkie,ib_back,ib_profile_superior;
    CreateEditGeneralDialog dialog;
    Uri image;
    BottomSheetProfilePhoto bottomSheetProfilePhoto;
    FragmentManager fragmentManager;
    FragmentActivity activity;
    String UID, worldkie_id, source,name;
    LottieAnimationView animationView;
    public CreateEditWorldkie() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CreateEditWorldkie.
     */
    // TODO: Rename and change types and number of parameters
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_edit_worldkie, container, false);
        mainActivity = (MainActivity) getActivity();
        ib_back = mainActivity.getBackButton();
        ib_save = mainActivity.getIb_save();
        ib_profile_superior = mainActivity.getIb_self_profile();
        ib_save.setOnClickListener(this);

        fragmentManager = requireActivity().getSupportFragmentManager();
        activity = requireActivity();
        firebaseAuth = FirebaseAuth.getInstance();
        UID = firebaseAuth.getUid();
        initComponents(view);
        setVisibility();
        dialog = new CreateEditGeneralDialog(mainActivity);
         animationView = dialog.findViewById(R.id.anim_create_edit);
        bottomSheetProfilePhoto = new BottomSheetProfilePhoto();

        db = FirebaseFirestore.getInstance();
        collectionWorldkie = db.collection("Worldkies");
        worldkieModel = new WorldkieModel();
        if(worldkie_id == null){
            try {
                createMode();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else{
            collectionWorldkie.document(worldkie_id).addSnapshotListener((queryDocumentSnapshot, e) -> {
                if (e != null) {
                    Log.e("Error", e.getMessage());
                    Toast.makeText(getContext(), "Error al escuchar cambios: " + e.getMessage(), LENGTH_LONG).show();
                    return;
                }
                if (queryDocumentSnapshot!=null) {
                    worldkieModel = WorldkieModel.fromSnapshot(queryDocumentSnapshot);
                    et_nameWorldkieCreate.setText(worldkieModel.getName());
                    tb_worldkiePrivacity.setChecked(worldkieModel.isWorldkie_private());
                    tb_worldkieDraft.setVisibility(worldkieModel.isWorldkie_private()?View.VISIBLE:View.GONE);
                    tb_worldkieDraft.setChecked(worldkieModel.isDraft());
                    getImage();
                }
            });

            editMode(worldkieModel);
        }
        setListeners();
        return view;
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
                source = "app";
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
        StorageReference userFolderRef = FirebaseStorage.getInstance("gs://buuk-tu-worldkies").getReference(worldkie_id);

        userFolderRef.listAll().addOnSuccessListener(listResult -> {
            for (StorageReference item : listResult.getItems()) {
                if (item.getName().startsWith("cover")) {
                    item.getDownloadUrl().addOnSuccessListener(uri -> {

                        DrawableUtils.personalizarImagenCuadradoButton(getContext(),150/6,7,R.color.brownMaroon,uri,ib_select_img_create_worldkie);
                        ib_select_img_create_worldkie.setVisibility(View.VISIBLE);
                        EfectsUtils.startCircularReveal(ib_select_img_create_worldkie.getDrawable(),ib_select_img_create_worldkie);
                        source = "device";
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
        source = "app";
        worldkieModel.setUID_AUTHOR(UID);
        worldkieModel.setPhoto_default(true);
        worldkieModel.setWorldkie_private(false);
        ib_select_img_create_worldkie.setTag(DrawableUtils.getMipmapName(mainActivity,R.mipmap.photoworldkieone));
        worldkieModel.setId_photo(ib_select_img_create_worldkie.getTag().toString());
    }
    public void setSelectedProfilePhoto(Drawable image){

        int cornerRadius = 150 / 6; // Ejemplo de radio
        int borderWidth = 7; // Ejemplo de grosor del borde
        int borderColor = getContext().getResources().getColor(R.color.brownMaroon, null); // Asegúrate de que el color sea correcto
//        Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.photoworldkieone);
        //ib_select_img_create_worldkie.setImageDrawable(image);

        RequestOptions requestOptions = new RequestOptions()
               // .override(150, 150)
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

    public String getSource() {
        return source;
    }

    public Drawable getSelectedProfilePhoto()
    {
        return ib_select_img_create_worldkie.getDrawable();
    }
    public void setSource(String source) {
        this.source = source;
    }
    public void selectImage(){
        bottomSheetProfilePhoto.show(getChildFragmentManager(),"BottomSheetProfilePhoto");
    }
    public ImageButton getIb_select_img_create_worldkie() {
        return ib_select_img_create_worldkie;
    }

    public void editMode(WorldkieModel worldkieModel){
        et_nameWorldkieCreate.setText(worldkieModel.getName());
        tb_worldkiePrivacity.setChecked(worldkieModel.isWorldkie_private());

        if(!worldkieModel.isWorldkie_private()){
            tb_worldkieDraft.setVisibility(View.GONE);
        }
        //obtenerImagen();
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
  /*  public void setClean() {
        CheckUtil.setErrorMessage(null, tv_nameRegister);
        CheckUtil.setErrorMessage(null, tv_emailRegister);
        CheckUtil.setErrorMessage(null, tv_birthdayRegister);
        CheckUtil.setErrorMessage(null, tv_passwordRegister);
        CheckUtil.setErrorMessage(null, tv_passwordRepeatRegister);
        CheckUtil.setErrorMessage(null, tv_pronounsRegister);
        CheckUtil.setErrorMessage(null, tv_usernameRegister);
        CheckUtil.setErrorMessage(null, tv_telephoneRegister);
    }*/
    private void addDataToFirestore() {
        if(CheckUtil.handlerCheckName(mainActivity,et_nameWorldkieCreate,et_nameWorldkieCreateFull)) {
            worldkieModel.setCreation_date(new Timestamp(Instant.now()));
            worldkieModel.setLast_update(new Timestamp(Instant.now()));
            worldkieModel.setPhoto_default(true);
            dialog.show();
            EfectsUtils.setAnimationsDialog("start",animationView);
            Completable.timer(3, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                        getValues();
                                Task<DocumentReference> addTask = collectionWorldkie.add(worldkieModel);

                                addTask.addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {

                                        if (!worldkieModel.isPhoto_default()) {
                                            StorageReference userRef = storage.getReference().child(task.getResult().getId());
                                            userRef.child("profile" + DrawableUtils.getExtensionFromUri(getContext(), image)).putFile(image);

                                        }
                                        EfectsUtils.setAnimationsDialog("success",animationView);
                                        Completable.timer(2, TimeUnit.SECONDS)
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(() -> {
                                                    dialog.dismiss();
                                                    NavigationUtils.goBack(fragmentManager, activity);
                                                });

                                    }
                                }).addOnFailureListener(e -> {
                                    EfectsUtils.setAnimationsDialog("fail",animationView);
                                    Completable.timer(3, TimeUnit.SECONDS)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(() -> {
                                                animationView.setVisibility(View.GONE);
                                                dialog.dismiss();
                                            });
                                });
                            }
                    );
        }
    }
    private void getValues(){
        worldkieModel.setName(et_nameWorldkieCreate.getText().toString());
    }
    private void editDataFirestore() {
        //if(CheckUtil.handlerCheckName(context,et_nameWorldkieCreate,))
        if(!et_nameWorldkieCreate.getText().toString().isEmpty()) {
            dialog.show();
            EfectsUtils.setAnimationsDialog("start",animationView);
            Completable.timer(3, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                                getValues();
                                Date last_update = new Date();
                                Map<String, Object> worldkieData = new HashMap<>();
                                if (!name.equals(worldkieModel.getName())) {
                                    worldkieData.put("name", name);
                                }
                                worldkieData.put("last_update", last_update);
                                //boolean isDefaultImage = (boolean) createWorldkie.getIB_profile_photo().getTag(R.drawable.worldkie_default);
                                if (source.equals("app") && !ib_select_img_create_worldkie.getTag().toString().equals(worldkieModel.getId_photo())) {
                                    worldkieData.put("id_photo", ib_select_img_create_worldkie.getTag().toString());
                                }
                                if (worldkieModel.isPhoto_default() && source.equals("device")) {
                                    worldkieData.put("photo_default", false);

                                } else if (!worldkieModel.isPhoto_default() && source.equals("app")) {
                                    worldkieData.put("photo_default", true);

                                }
                                if (worldkieModel.isWorldkie_private() != tb_worldkiePrivacity.isChecked()) {
                                    worldkieData.put("worldkie_private", tb_worldkiePrivacity.isChecked());
                                }
                                if (worldkieModel.isDraft() != tb_worldkieDraft.isChecked()) {
                                    worldkieData.put("draft", tb_worldkieDraft.isChecked());
                                }
                                worldkieModel.setCreation_date(null);
                                worldkieModel.setLast_update(new Timestamp(Instant.now()));
                                collectionWorldkie.document(worldkie_id).update(worldkieData).addOnSuccessListener(unused -> {
                                    if (source.equals("device")) {
                                        StorageReference userRef = storage.getReference().child(worldkie_id);
                                        userRef.child("profile" + DrawableUtils.getExtensionFromUri(getContext(), image)).putFile(image);

                                    }
                                    EfectsUtils.setAnimationsDialog("success",animationView);
                                    Completable.timer(3, TimeUnit.SECONDS)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(() -> {
                                                animationView.setVisibility(View.GONE);
                                                dialog.dismiss();
                                                NavigationUtils.goBack(fragmentManager, activity);
                                            });

                                }).addOnFailureListener(e -> {
                                    EfectsUtils.setAnimationsDialog("fail",animationView);
                                    Completable.timer(5, TimeUnit.SECONDS)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(() -> {
                                                animationView.setVisibility(View.GONE);
                                                dialog.dismiss();
                                            });
                                });
                            }
                    );
        }
    }
    private void subirNuevaImagen() {
      /*  Drawable drawable = ib_select_img_create_worldkie.getDrawable();
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            bitmap = ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof RoundedBitmapDrawable) {
            bitmap = ((RoundedBitmapDrawable) drawable).getBitmap();
        }

        // Subir la nueva imagen
        StorageReference worldkieRef = storage.getReference().child(worldkieModel.getUID() + createWorldkie.getImage().getLastPathSegment());
        UploadTask uploadTask = worldkieRef.putFile(createWorldkie.getImage());
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(createWorldkie, "Subida exitosa", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(createWorldkie, "Error al subir la imagen", Toast.LENGTH_SHORT).show();
            }
        });*/
    }
    private void initComponents(View view){
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
                    if(worldkie_id == null){
                        addDataToFirestore();
                    }else{
                        editDataFirestore();
                    }
        } else if (v.getId()==R.id.ib_back) {
            NavigationUtils.goBack(fragmentManager,activity);
        } else if(v.getId()==R.id.ib_save){
                    if(worldkie_id == null){
                        addDataToFirestore();
                    }else{
                        editDataFirestore();
                    }
                }
        }
    }
