package com.example.buuktu.views;

import static android.widget.Toast.LENGTH_LONG;

import android.content.Context;
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
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.buuktu.R;
import com.example.buuktu.utils.RoundedBorderSquareTransformation;
import com.example.buuktu.bottomsheet.BottomSheetProfilePhoto;
import com.example.buuktu.dialogs.CreateEditGeneralDialog;
import com.example.buuktu.models.WorldkieModel;
import com.example.buuktu.utils.DrawableUtils;
import com.example.buuktu.utils.EfectsUtils;
import com.example.buuktu.utils.NavigationUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
    Context context;
    String UID, worldkie_id, source,name;
    boolean privacity, draft;

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
        MainActivity mainActivity = (MainActivity) getActivity();
        ib_back = mainActivity.getBackButton();
        ib_save = mainActivity.getIb_save();
        ib_save.setVisibility(View.VISIBLE);
        ib_profile_superior = mainActivity.getIb_self_profile();
        ib_profile_superior.setVisibility(View.VISIBLE);
        context = getContext();
        ib_save.setOnClickListener(this);

        fragmentManager = requireActivity().getSupportFragmentManager();
        activity = requireActivity();
        ib_back.setVisibility(View.VISIBLE);
        firebaseAuth = FirebaseAuth.getInstance();
        UID = firebaseAuth.getUid();
        initComponents(view);
        ib_select_img_create_worldkie.setVisibility(View.INVISIBLE);
        dialog = new CreateEditGeneralDialog(getContext());
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
                if (queryDocumentSnapshot.exists()) {
                    String name = queryDocumentSnapshot.getString("name");
                    worldkieModel.setName(name);
                    boolean worldkie_private = queryDocumentSnapshot.getBoolean("worldkie_private");
                    worldkieModel.setWorldkie_private(worldkie_private);
                    tb_worldkiePrivacity.setChecked(worldkie_private);
                    if(worldkie_private) {
                        boolean draft = queryDocumentSnapshot.getBoolean("draft");
                        worldkieModel.setDraft(draft);
                        tb_worldkieDraft.setChecked(draft);
                        tb_worldkieDraft.setVisibility(View.VISIBLE);
                    }
                    boolean photo_default = queryDocumentSnapshot.getBoolean("photo_default");
                    worldkieModel.setPhoto_default(photo_default);
                    et_nameWorldkieCreate.setText(name);
                       getImage();
                }
            });

            editMode(worldkieModel);
        }
        setListeners();
    //    DrawableUtils.personalizarImagenCuadradoButton(getContext(),DrawableUtils.drawableToBitmap(ib_select_img_create_worldkie.getDrawable()),ib_select_img_create_worldkie,R.color.greenWhatever);

        return view;
    }
    private void setListeners(){
        ib_select_img_create_worldkie.setOnClickListener(this);
        ib_back.setOnClickListener(this);
    ib_save.setOnClickListener(this);
        tb_worldkiePrivacity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    tb_worldkieDraft.setVisibility(View.VISIBLE);
                }else{
                    tb_worldkieDraft.setVisibility(View.GONE);
                }
            }
        });
    }
    private void getImage(){
        if(worldkieModel.isPhoto_default()){
        collectionWorldkie.document(worldkie_id).addSnapshotListener((queryDocumentSnapshot, e) -> {
                       /* if (e != null) {
                            Log.e("Error", e.getMessage());
                            Toast.makeText(getContext(), "Error al escuchar cambios: " + e.getMessage(), LENGTH_LONG).show();
                            return;
                        }*/
            String id_photo = queryDocumentSnapshot.getString("id_photo");
            int resId = getContext().getResources().getIdentifier(id_photo, "mipmap", getContext().getPackageName());

            if (resId != 0) {
                Drawable drawable = ContextCompat.getDrawable(getContext(), resId);
                ib_select_img_create_worldkie.setImageDrawable(drawable);
                source = "app";
                ib_select_img_create_worldkie.setTag(DrawableUtils.getMipmapName(context,resId));

                try {
                    DrawableUtils.personalizarImagenCuadradoButton(getContext(),150/7,7,R.color.brownMaroon,drawable, ib_select_img_create_worldkie);
                    ib_select_img_create_worldkie.setVisibility(View.VISIBLE);
                    EfectsUtils.startCircularReveal(drawable,ib_select_img_create_worldkie);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
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
            ;
        });
    }
    }
    public void createMode() throws IOException {
        et_nameWorldkieCreate.setText("");
        tb_worldkiePrivacity.setChecked(false);
        tb_worldkieDraft.setVisibility(View.GONE);
        putDefaultImage();
        source = "app";
        ib_select_img_create_worldkie.setTag(DrawableUtils.getMipmapName(context,R.mipmap.photoworldkieone));

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
    public void setImageUri(Uri image){
        this.image=image;
    }

    private void putDefaultImage() throws IOException {
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.photoworldkieone);
        DrawableUtils.personalizarImagenCuadradoButton(getContext(),115/6,7,R.color.brownMaroon,drawable, ib_select_img_create_worldkie);
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
        //if(CheckUtil.handlerCheckName(context,et_nameWorldkieCreate,))
        if(!et_nameWorldkieCreate.getText().toString().equals("")) {
            dialog.show();
            LottieAnimationView animationView = dialog.findViewById(R.id.anim_create_edit);
            animationView.setAnimation(R.raw.reading_anim);
            animationView.playAnimation();
            Completable.timer(3, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                        getValues();
                        if (source.equals("device")) {
                                    if (!privacity) {
                                        worldkieModel = new WorldkieModel(UID, name, false, privacity, draft);
                                    } else {
                                        worldkieModel = new WorldkieModel(UID, name, false, privacity);
                                    }
                                } else {
                                    if (!privacity) {
                                        worldkieModel = new WorldkieModel(UID, name, true, privacity, draft, ib_select_img_create_worldkie.getTag().toString());
                                    } else {
                                        worldkieModel = new WorldkieModel(UID, name, true, privacity, ib_select_img_create_worldkie.getTag().toString());
                                    }
                                }
                                Task<DocumentReference> addTask = db.collection("Worldkies").add(worldkieModel);

                                addTask.addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                        if (task.isSuccessful()) {

                                            if (source.equals("device")) {
                                                StorageReference userRef = storage.getReference().child(task.getResult().getId());
                                                userRef.child("profile" + DrawableUtils.getExtensionFromUri(getContext(), image)).putFile(image);

                                            }
                                            animationView.setAnimation(R.raw.success_anim);
                                            animationView.playAnimation();
                                            Completable.timer(2, TimeUnit.SECONDS)
                                                    .subscribeOn(Schedulers.io())
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .subscribe(() -> {
                                                        dialog.dismiss();
                                                        NavigationUtils.goBack(fragmentManager, activity);
                                                    });

                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        animationView.setAnimation(R.raw.fail_anim);
                                        animationView.playAnimation();
                                        Completable.timer(3, TimeUnit.SECONDS)
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(() -> {
                                                    animationView.setVisibility(View.GONE);
                                                    dialog.dismiss();
                                                });
                                    }
                                });
                            }
                    );
        }
    }
    private void getValues(){
        name = et_nameWorldkieCreate.getText().toString();
        privacity = tb_worldkiePrivacity.isChecked();
        if(privacity) {
            draft = tb_worldkieDraft.isChecked();

        }
    }

    private void editDataFirestore() {
        //if(CheckUtil.handlerCheckName(context,et_nameWorldkieCreate,))
        if(!et_nameWorldkieCreate.getText().toString().equals("")) {
            dialog.show();
            LottieAnimationView animationView = dialog.findViewById(R.id.anim_create_edit);
            animationView.setAnimation(R.raw.reading_anim);
            animationView.playAnimation();
            Completable.timer(3, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                                getValues();
                                CollectionReference dbWorldkies = db.collection("Worldkies");
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
                                dbWorldkies.document(worldkie_id).update(worldkieData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        if (source.equals("device")) {
                                            StorageReference userRef = storage.getReference().child(worldkie_id);
                                            userRef.child("profile" + DrawableUtils.getExtensionFromUri(getContext(), image)).putFile(image);

                                        }
                                        animationView.setAnimation(R.raw.success_anim);
                                        animationView.playAnimation();
                                        Completable.timer(3, TimeUnit.SECONDS)
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(() -> {
                                                    animationView.setVisibility(View.GONE);
                                                    dialog.dismiss();
                                                    NavigationUtils.goBack(fragmentManager, activity);
                                                });

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        animationView.setAnimation(R.raw.fail_anim);
                                        animationView.playAnimation();
                                        Completable.timer(5, TimeUnit.SECONDS)
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(() -> {
                                                    animationView.setVisibility(View.GONE);
                                                    dialog.dismiss();
                                                });
                                    }
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
