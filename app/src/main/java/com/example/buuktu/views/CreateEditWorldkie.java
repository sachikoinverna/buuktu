package com.example.buuktu.views;

import static android.widget.Toast.LENGTH_LONG;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.airbnb.lottie.LottieAnimationView;
import com.example.buuktu.DataBinderMapperImpl;
import com.example.buuktu.R;
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

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
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
    ImageButton bt_saveWorldkie,ib_select_img_create_worldkie,ib_back;
    CreateEditGeneralDialog dialog;
    public CreateEditWorldkie() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateEditWorldkie.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateEditWorldkie newInstance(String param1, String param2) {
        CreateEditWorldkie fragment = new CreateEditWorldkie();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            worldkie_id = getArguments().getString("worldkie_id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_edit_worldkie, container, false);
        MainActivity mainActivity = (MainActivity) getActivity();
        ib_back = mainActivity.getBackButton();
        ib_back.setVisibility(View.VISIBLE);
        initComponents(view);
        dialog = new CreateEditGeneralDialog(getContext());
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
                    boolean photo_default = queryDocumentSnapshot.getBoolean("creation_date");
                    worldkieModel.setPhoto_default(photo_default);
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
        bt_saveWorldkie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(worldkie_id == null){
                    addDataToFirestore();
                }else{
                    editDataFirestore();
                }
            }
        });
        DrawableUtils.personalizarImagenCuadradoButton(getContext(),DrawableUtils.drawableToBitmap(ib_select_img_create_worldkie.getDrawable()),ib_select_img_create_worldkie,R.color.greenWhatever);

        return view;
    }
    public void createMode(){
        et_nameWorldkieCreate.setText("");
        tb_worldkiePrivacity.setChecked(false);
        tb_worldkieDraft.setVisibility(View.GONE);
        putDefaultImage();
    }
    public void editMode(WorldkieModel worldkieModel){
        et_nameWorldkieCreate.setText(worldkieModel.getName());
        tb_worldkiePrivacity.setChecked(worldkieModel.isWorldkie_private());

        if(!worldkieModel.isWorldkie_private()){
            tb_worldkieDraft.setVisibility(View.GONE);
        }
        obtenerImagen();
    }

    private void obtenerImagen(){
        if (worldkieModel.isPhoto_default()) {
            putDefaultImage();
        } else {
            StorageReference storageRef = storage.getReference().child(worldkieModel.getUID());
            final long ONE_MEGABYTE = 1024 * 1024;
            storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
                Bitmap bitmap = BitmapUtils.convertCompressedByteArrayToBitmap(bytes);
                Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                ib_select_img_create_worldkie.setImageDrawable(drawable);
                worldkieModel.setPhoto(drawable);
            }).addOnFailureListener(exception ->
                    Toast.makeText(getContext(), "Error al cargar imagen", Toast.LENGTH_SHORT).show()
            );
        }
    }
    private void putDefaultImage(){
        ib_select_img_create_worldkie.setImageResource(R.drawable.worldkie_default);
        DrawableUtils.personalizarImagenCuadradoButton(getContext(),DrawableUtils.drawableToBitmap(ib_select_img_create_worldkie.getDrawable()),ib_select_img_create_worldkie,R.color.greenWhatever);
    }
    private void addDataToFirestore() {
        mostrarBarraProgreso();
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
    private void mostrarBarraProgreso(){
       /* barraProgreso = new ProgressDialog(createWorldkie);
        barraProgreso.setTitle("Buscando...");
        barraProgreso.setMessage("Progreso...");
        barraProgreso.setProgressStyle(barraProgreso.STYLE_HORIZONTAL);
        barraProgreso.setProgress(0);
        barraProgreso.setMax(10);
        barraProgreso.show();*/

    }
    private void initComponents(View view){
        et_nameWorldkieCreateFull = view.findViewById(R.id.et_nameWorldkieCreateFull);
        et_nameWorldkieCreate = view.findViewById(R.id.et_nameWorldkieCreate);
        tb_worldkiePrivacity = view.findViewById(R.id.tb_worldkiePrivacity);
        tb_worldkieDraft = view.findViewById(R.id.tb_worldkieDraft);
        bt_saveWorldkie = view.findViewById(R.id.bt_saveWorldkie);
        ib_select_img_create_worldkie = view.findViewById(R.id.ib_select_img_create_worldkie);
    }


}