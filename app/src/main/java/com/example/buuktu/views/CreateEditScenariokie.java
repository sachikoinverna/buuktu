package com.example.buuktu.views;

import static android.widget.Toast.LENGTH_LONG;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.buuktu.R;
import com.example.buuktu.dialogs.CreateEditGeneralDialog;
import com.example.buuktu.models.ScenariokieModel;
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


public class CreateEditScenariokie extends Fragment implements View.OnClickListener {
    ImageButton ib_select_img_create_scenariokie,ib_back,ib_save;
    Uri image;
    BottomSheetProfilePhoto bottomSheetProfilePhoto;
    String worldkie_id,scenariokie_id;
    FragmentManager fragmentManager;
    Switch tb_scenariokiePrivacity,tb_scenariokieDraft;
    TextInputEditText et_nameScenariokieCreate;
    TextInputLayout et_nameScenariokieCreateFull;
    MainActivity mainActivity;
    CreateEditGeneralDialog dialog;
    LottieAnimationView animationView;
    ScenariokieModel scenariokieModel;

    public CreateEditScenariokie() {
    }

    public static CreateEditScenariokie newInstance() {
        return new CreateEditScenariokie();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if(getArguments().containsKey("worldkie_id")) {
                this.worldkie_id = getArguments().getString("worldkie_id");
            }
            if(getArguments().containsKey("scenariokie_id")) {
                this.scenariokie_id = getArguments().getString("scenariokie_id");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_edit_scenariokie, container, false);


        initComponents(view);
        setVisibility();
        dialog = new CreateEditGeneralDialog(mainActivity);
        scenariokieModel = new ScenariokieModel();
        fragmentManager = requireActivity().getSupportFragmentManager();
        bottomSheetProfilePhoto = new BottomSheetProfilePhoto();
        tb_scenariokieDraft.setVisibility(View.INVISIBLE);
        ib_select_img_create_scenariokie = view.findViewById(R.id.ib_select_img_create_scenariokie);
        setListeners();
        return view;
    }
    private void setVisibility(){
        ib_back.setVisibility(View.VISIBLE);
        ib_save.setVisibility(View.VISIBLE);
    }
    public void setScenariokieModel() {
        if (scenariokie_id == null) {
            createMode();
            try {
                putDefaultImage();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            mainActivity.getCollectionScenariokies().document(scenariokie_id).addSnapshotListener((queryDocumentSnapshot, e) -> {
                if (e != null) {
                    Log.e("Error", e.getMessage());
                    Toast.makeText(getContext(), "Error al escuchar cambios: " + e.getMessage(), LENGTH_LONG).show();
                    return;
                }
                if (queryDocumentSnapshot != null) {

                    scenariokieModel = ScenariokieModel.fromSnapshot(queryDocumentSnapshot);
                    et_nameScenariokieCreate.setText(scenariokieModel.getName());
                    tb_scenariokiePrivacity.setChecked(scenariokieModel.isScenariokie_private());
                    tb_scenariokieDraft.setVisibility(scenariokieModel.isScenariokie_private() ? View.VISIBLE : View.GONE);
                    tb_scenariokieDraft.setChecked(scenariokieModel.isDraft());
                    getImage();
                }
            });
        }
    }
    private void createMode(){
        et_nameScenariokieCreate.setText("");
        tb_scenariokiePrivacity.setChecked(false);
        tb_scenariokieDraft.setVisibility(View.GONE);
        scenariokieModel.setAUTHOR_UID(mainActivity.getUID());
        scenariokieModel.setWORDLKIE_UID(worldkie_id);
        scenariokieModel.setPhoto_default(true);
        scenariokieModel.setScenariokie_private(false);
        ib_select_img_create_scenariokie.setVisibility(View.VISIBLE);
        ib_select_img_create_scenariokie.setTag(DrawableUtils.getMipmapName(mainActivity,R.mipmap.photoscenariokieone));
        scenariokieModel.setPhoto_id(ib_select_img_create_scenariokie.getTag().toString());
    }
    private void initComponents(View view){
        tb_scenariokiePrivacity = view.findViewById(R.id.tb_scenariokiePrivacity);
        tb_scenariokieDraft = view.findViewById(R.id.tb_scenariokieDraft);
        et_nameScenariokieCreateFull = view.findViewById(R.id.et_nameScenariokieCreateFull);
        et_nameScenariokieCreate = view.findViewById(R.id.et_nameScenariokieCreate);
        mainActivity = (MainActivity) getActivity();
        ib_back = mainActivity.getBackButton();
        ib_save = mainActivity.getIb_save();

    }
    public Drawable getSelectedProfilePhoto()
    {
        return ib_select_img_create_scenariokie.getDrawable();
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
                .into(ib_select_img_create_scenariokie);
    }
    private void putDefaultImage() throws IOException {
        DrawableUtils.personalizarImagenCuadradoButton(mainActivity,150/7,7,R.color.brownMaroon,R.mipmap.photoscenariokieone,ib_select_img_create_scenariokie);
    }

    public void setImageUri(Uri image){
        this.image=image;
    }
    public void selectImage(){
        bottomSheetProfilePhoto.show(getChildFragmentManager(),"BottomSheetProfilePhoto");
    }
    public ImageButton getIb_select_img_create_scenariokie() {
        return ib_select_img_create_scenariokie;
    }
    public void setPhotoNoDefault(){
        scenariokieModel.setPhoto_default(false);
        scenariokieModel.setPhoto_id(null);
    }
    public void setPhotoDefault(){
        scenariokieModel.setPhoto_default(true);
        scenariokieModel.setPhoto_id(ib_select_img_create_scenariokie.getTag().toString());
    }
    private void setListeners(){
        ib_select_img_create_scenariokie.setOnClickListener(this);
        ib_back.setOnClickListener(this);
        ib_save.setOnClickListener(this);
        tb_scenariokiePrivacity.setOnCheckedChangeListener((buttonView, isChecked) -> tb_scenariokieDraft.setVisibility(isChecked?View.VISIBLE:View.INVISIBLE));
        tb_scenariokieDraft.setOnCheckedChangeListener((buttonView, isChecked) -> {

        });
    }
    private void addDataToFirestore(){
            Completable.timer(3, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                                Task<DocumentReference> addTask = mainActivity.getCollectionScenariokies().add(scenariokieModel.toMap());

                                addTask.addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        scenariokie_id = addTask.getResult().getId();
                                        success();

                                    }
                                }).addOnFailureListener(e -> fail());
                            }
                    );
    }
    private void uploadNewImage(){
        if (!scenariokieModel.isPhoto_default()) {
            mainActivity.getFirebaseStorageScenariokies().getReference().child(scenariokie_id).child("cover" + DrawableUtils.getExtensionFromUri(mainActivity, image)).putFile(image);
        }
    }
    private void getImage(){
        if(scenariokieModel.isPhoto_default()){
            int resId = mainActivity.getResources().getIdentifier(scenariokieModel.getPhoto_id(), "mipmap", mainActivity.getPackageName());

            if (resId != 0) {
                Drawable drawable = ContextCompat.getDrawable(mainActivity, resId);
                ib_select_img_create_scenariokie.setImageDrawable(drawable);
                ib_select_img_create_scenariokie.setTag(DrawableUtils.getMipmapName(mainActivity,resId));

                try {
                    DrawableUtils.personalizarImagenCuadradoButton(mainActivity,150/7,7,R.color.brownMaroon,drawable, ib_select_img_create_scenariokie);
                    ib_select_img_create_scenariokie.setVisibility(View.VISIBLE);
                    EfectsUtils.startCircularReveal(drawable,ib_select_img_create_scenariokie);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        } else {
           mainActivity.getFirebaseStorageScenariokies().getReference(scenariokie_id).listAll().addOnSuccessListener(listResult -> {
                for (StorageReference item : listResult.getItems()) {
                    if (item.getName().startsWith("cover")) {
                        item.getDownloadUrl().addOnSuccessListener(uri -> {

                            DrawableUtils.personalizarImagenCuadradoButton(getContext(),150/6,7,R.color.brownMaroon,uri,ib_select_img_create_scenariokie);
                            ib_select_img_create_scenariokie.setVisibility(View.VISIBLE);
                            EfectsUtils.startCircularReveal(ib_select_img_create_scenariokie.getDrawable(),ib_select_img_create_scenariokie);
                        });
                        break;
                    }
                }
            });
        }
    }
    private void editDataFirestore() {
            Completable.timer(3, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> mainActivity.getCollectionScenariokies().document(scenariokie_id).update(scenariokieModel.toMap()).addOnSuccessListener(unused -> success()).addOnFailureListener(e -> fail())
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
    private void delayedDismiss() {
        Completable.timer(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> dialog.dismiss());
    }
    private void save(){
        if (CheckUtil.handlerCheckName(mainActivity, et_nameScenariokieCreate, et_nameScenariokieCreateFull)) {
            dialog.show();
            animationView = dialog.getAnimationView();
            scenariokieModel.setName(et_nameScenariokieCreate.getText().toString());
            if (scenariokie_id == null) {
                addDataToFirestore();
            } else {
                editDataFirestore();
            }
        }
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ib_select_img_create_scenariokie) {
            selectImage();

        } else if (v.getId() == R.id.ib_back) {
            NavigationUtils.goBack(fragmentManager,mainActivity);
        } else if (v.getId()==R.id.ib_save) {
            save();
        }
    }
}