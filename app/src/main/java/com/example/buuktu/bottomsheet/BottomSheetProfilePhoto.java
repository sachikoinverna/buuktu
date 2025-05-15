package com.example.buuktu.bottomsheet;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.buuktu.views.CreateCharacterkie;
import com.example.buuktu.views.CreateEditScenariokie;
import com.example.buuktu.views.CreateEditStuffkie;
import com.example.buuktu.R;
import com.example.buuktu.utils.DrawableUtils;
import com.example.buuktu.views.CreateEditWorldkie;
import com.example.buuktu.views.MainActivity;
import com.example.buuktu.views.Register;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetProfilePhoto extends BottomSheetDialogFragment implements View.OnClickListener{
    Context context;
    Button tv_choose_photo_default,tv_choose_photo_gallery;
    final int REQUEST_CODE = 1;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    Register register;
    CreateEditWorldkie createEditWorldkie;
    CreateCharacterkie createCharacterkie;
    CreateEditStuffkie createEditStuffkie;
    CreateEditScenariokie createEditScenariokie;
    public BottomSheetProfilePhoto (){
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
    ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottomsheet_choose_photo_origin,
                container, false);
    initComponents(v);

        initSelector();
        setListeners();


        return v;
    }
    private void setListeners(){
        tv_choose_photo_default.setOnClickListener(this);
        tv_choose_photo_gallery.setOnClickListener(this);

    }
    private void initSelector(){
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            Uri uri = result.getData().getData();
                            if(register!=null) {
                                register.setImageUri(uri);
                                DrawableUtils.personalizarImagenCircleButton(context, uri, register.getIB_profile_photo(), R.color.blue1);
                                    register.setPhotoNoDefault();
                                    register.setSource("device");
                                } else if (createEditWorldkie!=null) {
                                createEditWorldkie.setImageUri(uri);
                                DrawableUtils.personalizarImagenCuadradoButton(context,150/6,7,R.color.brownMaroon,uri, createEditWorldkie.getIb_select_img_create_worldkie());
                                    createEditWorldkie.setPhotoNoDefault();
                                } else if (createCharacterkie!=null) {
                                createCharacterkie.setImageUri(uri);
                                DrawableUtils.personalizarImagenCircleButton(context, uri, createCharacterkie.getIb_select_img_create_worldkie(), R.color.brownMaroon);
                                    createCharacterkie.setPhotoNoDefault();
                                }else if (createEditStuffkie!=null) {
                                createEditStuffkie.setImageUri(uri);
                                DrawableUtils.personalizarImagenCuadradoButton(context,150/6,7,R.color.brownMaroon,uri, createEditStuffkie.getIb_select_img_create_stuffkie());
                                createEditStuffkie.setPhotoNoDefault();
                                } else if(createEditScenariokie!=null){
                                createEditScenariokie.setImageUri(uri);

                                DrawableUtils.personalizarImagenCuadradoButton(context,150/6,7,R.color.brownMaroon,uri, createEditScenariokie.getIb_select_img_create_scenariokie());
                                    createEditScenariokie.setPhotoNoDefault();
                                }
                                dismiss();
                            }
                }
        );
    }
    public void selectImageGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_CODE);
                return;
            }
        } else {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
                return;
            }
        }

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        imagePickerLauncher.launch(intent);

    }

    private void initComponents(View v){
    tv_choose_photo_default = v.findViewById(R.id.tv_choose_photo_default);
    tv_choose_photo_gallery = v.findViewById(R.id.tv_choose_photo_gallery);
        context = getContext();
        if(getActivity() instanceof Register)register = (Register) getActivity();
        else if(getActivity() instanceof MainActivity) {
            if (getParentFragment() instanceof CreateEditWorldkie) createEditWorldkie = (CreateEditWorldkie) getParentFragment();
            else if (getParentFragment() instanceof CreateCharacterkie) createCharacterkie = (CreateCharacterkie) getParentFragment();
            else if (getParentFragment() instanceof CreateEditStuffkie) createEditStuffkie = (CreateEditStuffkie) getParentFragment();
            else if (getParentFragment() instanceof CreateEditScenariokie) createEditScenariokie = (CreateEditScenariokie) getParentFragment();

        }
}
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.tv_choose_photo_default){
            BottomSheetProfilePhotoDefault bottomSheetProfilePhotoDefault = new BottomSheetProfilePhotoDefault();
            bottomSheetProfilePhotoDefault.show(getParentFragmentManager(),"BottomSheetProfilePhotoDefault");
            dismiss();
        } else if (v.getId() == R.id.tv_choose_photo_gallery) {
            selectImageGallery();
        }
    }
}
