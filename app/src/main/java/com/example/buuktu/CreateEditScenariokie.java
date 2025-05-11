package com.example.buuktu;

import android.animation.Animator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.DrawableRes;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.buuktu.dialogs.CreateEditGeneralDialog;
import com.example.buuktu.models.ScenariokieModel;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateEditScenariokie#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateEditScenariokie extends Fragment implements View.OnClickListener {
    ImageButton ib_select_img_create_scenariokie,ib_back,ib_save;
    Uri image;
    BottomSheetProfilePhoto bottomSheetProfilePhoto;
    String source,worldkie_id,scenariokie_id;
    FragmentManager fragmentManager;
    FragmentActivity activity;
    Switch tb_scenariokiePrivacity,tb_scenariokieDraft;
    Context context;
    TextInputEditText et_nameScenariokieCreate;
    TextInputLayout et_nameScenariokieCreateFull;
    MainActivity mainActivity;
    CreateEditGeneralDialog dialog;
    LottieAnimationView animationView;
    ScenariokieModel scenariokieModel;
    public CreateEditScenariokie() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CreateEditScenariokie.
     */
    // TODO: Rename and change types and number of parameters
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_edit_scenariokie, container, false);
        mainActivity = (MainActivity) getActivity();
        ib_back = mainActivity.getBackButton();
        ib_back.setVisibility(View.VISIBLE);
        ib_save = mainActivity.getIb_save();
        ib_save.setVisibility(View.VISIBLE);
        context = getContext();
        initComponents(view);
        dialog = new CreateEditGeneralDialog(mainActivity);
        animationView = dialog.getAnimationView();
        scenariokieModel = new ScenariokieModel();
        fragmentManager = requireActivity().getSupportFragmentManager();
        activity = requireActivity();
        source = "app";
        bottomSheetProfilePhoto = new BottomSheetProfilePhoto();
        tb_scenariokieDraft.setVisibility(View.INVISIBLE);
        ib_select_img_create_scenariokie = view.findViewById(R.id.ib_select_img_create_scenariokie);
        setListeners();
        try {
            putDefaultImage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return view;
    }
    private void initComponents(View view){
        tb_scenariokiePrivacity = view.findViewById(R.id.tb_scenariokiePrivacity);
        tb_scenariokieDraft = view.findViewById(R.id.tb_scenariokieDraft);
        et_nameScenariokieCreateFull = view.findViewById(R.id.et_nameScenariokieCreateFull);
        et_nameScenariokieCreate = view.findViewById(R.id.et_nameScenariokieCreate);
    }
    public Drawable getSelectedProfilePhoto()
    {
        return ib_select_img_create_scenariokie.getDrawable();
    }
    public void setSource(String source) {
        this.source = source;
    }
    public void setSelectedProfilePhoto(@DrawableRes int imageResId){
        int cornerRadius = 150 / 6;
        int borderWidth = 7;
        int borderColor = ContextCompat.getColor(getContext(), R.color.brownMaroon);

        RequestOptions requestOptions = new RequestOptions()
                //.override(150, 150)
                .centerCrop()
                .transform(new RoundedBorderSquareTransformation(cornerRadius, borderWidth, borderColor));

        Glide.with(getContext())
                .load(imageResId) // 👍 Esto sí pasa por la transformación
                .apply(requestOptions)
                .into(ib_select_img_create_scenariokie);
    }
    private void putDefaultImage() throws IOException {
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.photoscenariokieone);
        DrawableUtils.personalizarImagenCircleButton(getContext(),DrawableUtils.drawableToBitmap(drawable),ib_select_img_create_scenariokie,R.color.brownMaroon);
    }
    public void setSelectedProfilePhoto(Drawable image){
        ib_select_img_create_scenariokie.setImageDrawable(image);
    }

    public String getSource() {
        return source;
    }
    public void setImageUri(Uri image){
        this.image=image;
    }
    public void selectImage(){
        bottomSheetProfilePhoto.show(getChildFragmentManager(),"BottomSheetProfilePhoto");
    }
    public ImageButton getIb_select_img_create_worldkie() {
        return ib_select_img_create_scenariokie;
    }
    private void startCircularReveal(Drawable finalDrawable) {
        ib_select_img_create_scenariokie.setImageDrawable(finalDrawable);
        ib_select_img_create_scenariokie.setAlpha(1f);

        // Solo ejecutar la animación en dispositivos con API 21+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Obtener el centro del ImageButton
            int centerX = ib_select_img_create_scenariokie.getWidth() / 2;
            int centerY = ib_select_img_create_scenariokie.getHeight() / 2;

            // Calcular el radio final (el círculo más grande que puede caber dentro del ImageButton)
            float finalRadius = Math.max(ib_select_img_create_scenariokie.getWidth(), ib_select_img_create_scenariokie.getHeight());

            // Crear el Animator para la revelación circular
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(
                    ib_select_img_create_scenariokie, centerX, centerY, 0, finalRadius);
            circularReveal.setDuration(500); // Duración de la animación en milisegundos

            // Iniciar la animación
            circularReveal.start();
        }
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
       if(CheckUtil.handlerCheckName(mainActivity,et_nameScenariokieCreate,et_nameScenariokieCreateFull)) {
           dialog.show();
           EfectsUtils.setAnimationsDialog("start", animationView);
         /*   Completable.timer(3, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                                Task<DocumentReference> addTask = characterkieCollection.add(characterkie.toMap());

                                addTask.addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {

                                        if (!characterkie.isPhoto_default()) {
                                            StorageReference userRef = storage.getReference().child(task.getResult().getId());
                                            userRef.child("profile" + DrawableUtils.getExtensionFromUri(getContext(), image)).putFile(image);

                                        }
                                        EfectsUtils.setAnimationsDialog("success",animationView);
                                        Completable.timer(2, TimeUnit.SECONDS)
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(() -> {
                                                    dialog.dismiss();
                                                    NavigationUtils.goBack(fragmentManager, mainActivity);
                                                });

                                    }
                                }).addOnFailureListener(e -> {
                                    EfectsUtils.setAnimationsDialog("fail",animationView);
                                    Completable.timer(3, TimeUnit.SECONDS)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(() -> dialog.dismiss());
                                });
                            }
                    );
        }*/
       }
    }

    private void editDataFirestore() {
        if (CheckUtil.handlerCheckName(mainActivity, et_nameScenariokieCreate, et_nameScenariokieCreateFull)) {
            dialog.show();
            EfectsUtils.setAnimationsDialog("start", animationView);

          /*  Completable.timer(3, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> scenariokieCollection.document(characterkie_id).update(characterkie.toMap()).addOnSuccessListener(unused -> {
                                if (!characterkie.isPhoto_default()) {
                                    StorageReference userRef = storage.getReference().child(worldkie_id);
                                    userRef.child("profile" + DrawableUtils.getExtensionFromUri(getContext(), image)).putFile(image);

                                }
                                EfectsUtils.setAnimationsDialog("success",animationView);

                                Completable.timer(3, TimeUnit.SECONDS)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(() -> {
                                            dialog.dismiss();
                                            NavigationUtils.goBack(fragmentManager,mainActivity);
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
                            })
                    );
        }*/
        }
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ib_select_img_create_scenariokie) {
            selectImage();

        } else if (v.getId() == R.id.ib_back) {
            NavigationUtils.goBack(fragmentManager,activity);
        } else if (v.getId()==R.id.ib_save) {

                if(worldkie_id == null){
                    addDataToFirestore();
                }else{
                    editDataFirestore();
                }
        }
    }
}