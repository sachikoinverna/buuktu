package com.example.buuktu.bottomsheet;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.buuktu.CreateCharacterkie;
import com.example.buuktu.CreateEditScenariokie;
import com.example.buuktu.CreateEditStuffkie;
import com.example.buuktu.R;
import com.example.buuktu.utils.DrawableUtils;
import com.example.buuktu.views.CreateEditWorldkie;
import com.example.buuktu.views.MainActivity;
import com.example.buuktu.views.Register;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.Objects;

public class BottomSheetProfilePhoto extends BottomSheetDialogFragment implements View.OnClickListener{
    Context context;
    TextView tv_choose_photo_default,tv_choose_photo_gallery;
    int flag = Intent.FLAG_GRANT_READ_URI_PERMISSION;
    int RESULT_CODE = 0;
    int REQUEST_CODE = 1;
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
    context = getContext();
    if(getActivity() instanceof Register){
        register = (Register) getActivity();
    }
    if(getActivity() instanceof MainActivity){
        if(getParentFragment() instanceof CreateEditWorldkie){
            createEditWorldkie = (CreateEditWorldkie) getParentFragment();
        } else if(getParentFragment() instanceof CreateCharacterkie){
            createCharacterkie = (CreateCharacterkie) getParentFragment();
        } else if (getParentFragment() instanceof CreateEditStuffkie) {
            createEditStuffkie = (CreateEditStuffkie) getParentFragment();
        }
    }
        initSelector();


        tv_choose_photo_default.setOnClickListener(this);
        tv_choose_photo_gallery.setOnClickListener(this);
        return v;
    }
    private void initSelector(){
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        try {
                            Uri uri = result.getData().getData();
                            if(register!=null) {
                                register.setImageUri(uri);
                            } else if (createEditWorldkie!=null) {
                                createEditWorldkie.setImageUri(uri);
                            } else if (createCharacterkie!=null) {
                                createCharacterkie.setImageUri(uri);
                            } else if (createEditStuffkie!=null) {
                                createEditStuffkie.setImageUri(uri);
                            }
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                ImageDecoder.Source source = ImageDecoder.createSource(context.getContentResolver(), uri);
                                Drawable drawable = ImageDecoder.decodeDrawable(source);
                                drawable.setBounds(0, 0, 100, 100);
                                if(register!=null) {
                                    register.setImageUri(uri);
                                } else if (createEditWorldkie!=null) {
                                    createEditWorldkie.setImageUri(uri);
                                }else if (createEditStuffkie!=null) {
                                    createEditStuffkie.setImageUri(uri);
                                }

                                if(register!=null) {
                                    register.getIB_profile_photo().setImageDrawable(drawable);
                                    DrawableUtils.personalizarImagenCircleButton(context, DrawableUtils.drawableToBitmap(drawable), register.getIB_profile_photo(), R.color.brownMaroon);
                                    register.setSource("device");
                                } else if (createEditWorldkie!=null) {
                                    createEditWorldkie.getIb_select_img_create_worldkie().setImageDrawable(drawable);
                                    DrawableUtils.personalizarImagenCircleButton(context, DrawableUtils.drawableToBitmap(drawable), createEditWorldkie.getIb_select_img_create_worldkie(), R.color.brownMaroon);
                                    createEditWorldkie.setSource("device");
                                } else if (createCharacterkie!=null) {
                                    createCharacterkie.getIb_select_img_create_worldkie().setImageDrawable(drawable);
                                    DrawableUtils.personalizarImagenCircleButton(context, DrawableUtils.drawableToBitmap(drawable), createCharacterkie.getIb_select_img_create_worldkie(), R.color.brownMaroon);
                                    createCharacterkie.setSource("device");
                                }else if (createEditStuffkie!=null) {
                                   // createEditStuffkie.getIb_select_img_create_worldkie().setImageDrawable(drawable);
                                   // DrawableUtils.personalizarImagenCircleButton(context, DrawableUtils.drawableToBitmap(drawable), createCharacterkie.getIb_select_img_create_worldkie(), R.color.brownMaroon);
                                    createEditStuffkie.setSource("device");
                                }
                                dismiss();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
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
