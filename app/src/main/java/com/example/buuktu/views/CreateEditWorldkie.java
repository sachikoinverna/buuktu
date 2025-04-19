package com.example.buuktu.views;

import static android.widget.Toast.LENGTH_LONG;

import android.animation.Animator;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.buuktu.DataBinderMapperImpl;
import com.example.buuktu.R;
import com.example.buuktu.adapters.RoundedBorderTransformation;
import com.example.buuktu.bottomsheet.BottomSheetProfilePhoto;
import com.example.buuktu.dialogs.CreateEditGeneralDialog;
import com.example.buuktu.dialogs.DeleteNotekieDialog;
import com.example.buuktu.models.WorldkieModel;
import com.example.buuktu.utils.BitmapUtils;
import com.example.buuktu.utils.DrawableUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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
public class CreateEditWorldkie extends Fragment {
    private FirebaseFirestore db;
    CollectionReference collectionWorldkie;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage storage = FirebaseStorage.getInstance("gs://buuk-tu-worldkies");
    private String worldkie_id;
    private WorldkieModel worldkieModel;
    private ProgressDialog barraProgreso;
    TextInputLayout et_nameWorldkieCreateFull;
    TextInputEditText et_nameWorldkieCreate;
    Switch tb_worldkiePrivacity,tb_worldkieDraft;
    ImageButton ib_save,ib_select_img_create_worldkie,ib_back;
    CreateEditGeneralDialog dialog;
    Uri image;
    BottomSheetProfilePhoto bottomSheetProfilePhoto;
    String source;
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
        ib_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(worldkie_id == null){
                    addDataToFirestore();
                }else{
                    editDataFirestore();
                }
            }
        });
        ib_back.setVisibility(View.VISIBLE);
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackToPreviousFragment();
            }
        });
        initComponents(view);
        dialog = new CreateEditGeneralDialog(getContext());
        source = "app";
        bottomSheetProfilePhoto = new BottomSheetProfilePhoto();

        db = FirebaseFirestore.getInstance();
        collectionWorldkie = db.collection("Worldkies");
        worldkieModel = new WorldkieModel();
        if(worldkie_id == null){
            createMode();
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
                    if(worldkie_private) {
                        boolean draft = queryDocumentSnapshot.getBoolean("draft");
                        worldkieModel.setDraft(draft);
                    }
                    boolean photo_default = queryDocumentSnapshot.getBoolean("photo_default");
                    worldkieModel.setPhoto_default(photo_default);
                       getImage();
                    /*if (!title.isEmpty()) {
                        noteItem.setTitle(title);
                        et_title_note.setText(noteItem.getTitle());
                    } else {
                        noteItem.setTitle("(Sin titulo)");
                        et_title_note.setHint(noteItem.getTitle());
                    }*/

                //    noteItem.setContent(queryDocumentSnapshot.getString("text"));
                //    et_content_note.setText(noteItem.getContent());
                }
            });

            editMode(worldkieModel);
        }
        ib_select_img_create_worldkie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
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
    //    DrawableUtils.personalizarImagenCuadradoButton(getContext(),DrawableUtils.drawableToBitmap(ib_select_img_create_worldkie.getDrawable()),ib_select_img_create_worldkie,R.color.greenWhatever);

        return view;
    }
    private void goBackToPreviousFragment() {
        // Verifica si hay un fragmento en la pila de retroceso
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

        if (fragmentManager.getBackStackEntryCount() > 0) {
            // Si hay fragmentos en la pila de retroceso, navega hacia atrás
            fragmentManager.popBackStack(); // Retrocede al fragmento anterior
        } else {
            // Si no hay fragmentos en la pila, puede que quieras cerrar la actividad o hacer alguna otra acción
            // Por ejemplo, cerrar la actividad:
            requireActivity().onBackPressed(); // Realiza el retroceso por defecto (salir de la actividad)
        }
    }
    private void getImage(){
        if(worldkieModel.isPhoto_default()){
        collectionWorldkie.document(worldkie_id).addSnapshotListener((queryDocumentSnapshot, e) -> {
                       /* if (e != null) {
                            Log.e("Error", e.getMessage());
                            Toast.makeText(getContext(), "Error al escuchar cambios: " + e.getMessage(), LENGTH_LONG).show();
                            return;
                        }*/
            //boolean photo_default = queryDocumentSnapshot.getBoolean("photo_default");
            String id_photo = queryDocumentSnapshot.getString("photo_id");
            int resId = getContext().getResources().getIdentifier(id_photo, "mipmap", getContext().getPackageName());

            if (resId != 0) {
                Drawable drawable = ContextCompat.getDrawable(getContext(), resId);
                ib_select_img_create_worldkie.setImageDrawable(drawable);
                DrawableUtils.personalizarImagenCuadrado(getContext(), DrawableUtils.drawableToBitmap(drawable), ib_select_img_create_worldkie, R.color.brownMaroon);
            }
        });
    } else {
            ib_select_img_create_worldkie.setVisibility(View.INVISIBLE);
        StorageReference userFolderRef = FirebaseStorage.getInstance("gs://buuk-tu-worldkies").getReference(worldkie_id);

        userFolderRef.listAll().addOnSuccessListener(listResult -> {
            for (StorageReference item : listResult.getItems()) {
                if (item.getName().startsWith("cover")) {
                    item.getDownloadUrl().addOnSuccessListener(uri -> {
                        int cornerRadius = 150 / 6; // Ejemplo de radio
                        int borderWidth = 7; // Ejemplo de grosor del borde
                        int borderColor = getContext().getResources().getColor(R.color.brownMaroon, null); // Asegúrate de que el color sea correcto

                        RequestOptions requestOptions = new RequestOptions()
                                //.override(150, 150)
                                .centerCrop()
                                .transform(new RoundedBorderTransformation(cornerRadius,borderWidth,borderColor));

                        Glide.with(getContext())
                                .load(uri)
                                .apply(requestOptions)
                                .into(ib_select_img_create_worldkie);
                        ib_select_img_create_worldkie.setVisibility(View.VISIBLE);
                        startCircularReveal(ib_select_img_create_worldkie.getDrawable());
                        // Para el borde con Glide, necesitarías una transformación personalizada más compleja
                        // o dibujar el borde alrededor del ImageView en su contenedor.
                    });
                }
            }
            ;
        });
    }
    }
    public void createMode(){
        et_nameWorldkieCreate.setText("");
        tb_worldkiePrivacity.setChecked(false);
        tb_worldkieDraft.setVisibility(View.GONE);
        putDefaultImage();
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

        RequestOptions requestOptions = new RequestOptions()
               // .override(150, 150)
                .centerCrop()
                .transform(new RoundedBorderTransformation(cornerRadius,borderWidth,borderColor));

        Glide.with(getContext())
                .load(DrawableUtils.drawableToBitmap(image))
                .apply(requestOptions)
                .into(ib_select_img_create_worldkie);
    }
    public void setSelectedProfilePhoto(@DrawableRes int imageResId){
        int cornerRadius = 150 / 6;
        int borderWidth = 7;
        int borderColor = ContextCompat.getColor(getContext(), R.color.brownMaroon);

        RequestOptions requestOptions = new RequestOptions()
               //.override(150, 150)
                .centerCrop()
                .transform(new RoundedBorderTransformation(cornerRadius, borderWidth, borderColor));

        Glide.with(getContext())
                .load(imageResId) // 👍 Esto sí pasa por la transformación
                .apply(requestOptions)
                .into(ib_select_img_create_worldkie);
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

    private void putDefaultImage(){
        int cornerRadius = 150 / 7; // Ejemplo de radio
        int borderWidth = 2; // Ejemplo de grosor del borde
        int borderColor = getContext().getResources().getColor(R.color.brownMaroon, null); // Asegúrate de que el color sea correcto
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.photoworldkieone);
        ib_select_img_create_worldkie.setImageDrawable(drawable);

        RequestOptions requestOptions = new RequestOptions()
                .override(150, 150)
                .centerCrop()
                .transform(new RoundedBorderTransformation(cornerRadius,borderWidth,borderColor));

        Glide.with(getContext())
                .load(drawable)
                .apply(requestOptions)
                .into(ib_select_img_create_worldkie);
    }
    private void startCircularReveal(Drawable finalDrawable) {
        ib_select_img_create_worldkie.setImageDrawable(finalDrawable);
        ib_select_img_create_worldkie.setAlpha(1f);

        // Solo ejecutar la animación en dispositivos con API 21+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Obtener el centro del ImageButton
            int centerX = ib_select_img_create_worldkie.getWidth() / 2;
            int centerY = ib_select_img_create_worldkie.getHeight() / 2;

            // Calcular el radio final (el círculo más grande que puede caber dentro del ImageButton)
            float finalRadius = Math.max(ib_select_img_create_worldkie.getWidth(), ib_select_img_create_worldkie.getHeight());

            // Crear el Animator para la revelación circular
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(
                    ib_select_img_create_worldkie, centerX, centerY, 0, finalRadius);
            circularReveal.setDuration(500); // Duración de la animación en milisegundos

            // Iniciar la animación
            circularReveal.start();
        }
    }
    private void addDataToFirestore() {
   //     mostrarBarraProgreso();
        Date creation_date = new Date();
        Map<String, Object> worldkieData = new HashMap<>();
        worldkieData.put("UID_AUTHOR", firebaseAuth.getUid());
        worldkieData.put("name", et_nameWorldkieCreate.getText().toString()); // Corrección clave
        worldkieData.put("creation_date", creation_date);
        worldkieData.put("last_update", creation_date);
        worldkieData.put("photo_default", ib_select_img_create_worldkie.getDrawable().equals(R.drawable.worldkie_default));
        worldkieData.put("worldkie_private", tb_worldkiePrivacity.isChecked());
        barraProgreso.incrementProgressBy(25);
        db.collection("Worldkies").add(worldkieData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                barraProgreso.incrementProgressBy(25);
                Toast.makeText(getContext(), "Your Course has been added to Firebase Firestore", Toast.LENGTH_SHORT).show();
              /*  if (!ib_select_img_create_worldkie.getDrawable().equals(R.drawable.worldkie_default)) {
                    StorageReference userRef = storage.getReference().child(documentReference.getId());
                    Drawable drawable = ib_select_img_create_worldkie.getDrawable();
                    Bitmap bitmap = null;

                    if (drawable instanceof BitmapDrawable) {
                        bitmap = ((BitmapDrawable) drawable).getBitmap();
                    } else if (drawable instanceof RoundedBitmapDrawable) {
                        bitmap = ((RoundedBitmapDrawable) drawable).getBitmap();
                    }// createWorldkie.getIB_profile_photo().setNam
                    userRef.child(documentReference.getId()+createWorldkie.getImage().getLastPathSegment());
                    UploadTask uploadTask = userRef.putFile(createWorldkie.getImage());
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(createWorldkie, "Subida exitosa",Toast.LENGTH_SHORT).show();*/
                           /* createWorldkie.getParentActivityIntent().
                            Intent intent = getIntent(createWorldkie,);
                            createWorldkie.finish();
                            finish();
                            startActivity(intent);*/
                         /*   barraProgreso.incrementProgressBy(50);
                            barraProgreso.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(),"Subida fallida",Toast.LENGTH_SHORT).show();
                        }
                    });
                }*/
                CreateEditGeneralDialog dialog = new CreateEditGeneralDialog(getContext());
                TextView tv_text = dialog.findViewById(R.id.tv_text_create_edit);
                tv_text.setText("Creando worldkie...");
                LottieAnimationView animationView = dialog.findViewById(R.id.anim_del_notekie);
                animationView.setAnimation(R.raw.reading_anim);
                animationView.playAnimation();

               /* collectionNotekies.document(item.getUID()).delete()
                        .addOnSuccessListener(unused -> {
                            animationView.setAnimation(R.raw.success_anim);
                            animationView.playAnimation();
                            Completable.timer(5, TimeUnit.SECONDS)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(() -> {
                                        animationView.setVisibility(View.GONE);
                                        dialog.dismiss();
                                    });
                        })
                        .addOnFailureListener(e -> {
                            animationView.setAnimation(R.raw.fail_anim);
                            animationView.playAnimation();
                            Completable.timer(5, TimeUnit.SECONDS)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(() -> {
                                        animationView.setVisibility(View.GONE);
                                        dialog.dismiss();
                                    });
                        });*/

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
               /* Log.e("Error", e.getMessage().toString());
                Toast.makeText(createWorldkie, e.getMessage().toString(), Toast.LENGTH_LONG).show();*/
            }
        });
    }
    private void editDataFirestore() {
        CollectionReference dbWorldkies = db.collection("Worldkies");
        Date last_update = new Date();
        Map<String, Object> worldkieData = new HashMap<>();
        if (!et_nameWorldkieCreate.equals(worldkieModel.getName())) {
            worldkieData.put("name", et_nameWorldkieCreate.getText().toString());
        }
        worldkieData.put("last_update", last_update);
        //boolean isDefaultImage = (boolean) createWorldkie.getIB_profile_photo().getTag(R.drawable.worldkie_default);
        if (worldkieModel.isPhoto_default() != ib_select_img_create_worldkie.getDrawable().equals(R.drawable.worldkie_default)) {
            worldkieData.put("photo_default", ib_select_img_create_worldkie.getDrawable().equals(R.drawable.worldkie_default));
        }
        if(worldkieModel.isWorldkie_private() != tb_worldkiePrivacity.isChecked()){
            worldkieData.put("worldkie_private", tb_worldkiePrivacity.isChecked());
        }
        dbWorldkies.document(worldkieModel.getUID()).update(worldkieData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                if (worldkieModel.getPhoto()!=ib_select_img_create_worldkie.getDrawable() && !worldkieModel.isPhoto_default()) {
                    storage.getReference().child(worldkieModel.getUID()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            subirNuevaImagen();
                        }
                    });
                } else if (worldkieModel.getPhoto()!=ib_select_img_create_worldkie.getDrawable() && worldkieModel.isPhoto_default()) {
                    subirNuevaImagen();
                }
            }
        });
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


}