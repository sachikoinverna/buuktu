package com.example.buuktu.bottomsheet;

import static com.example.buuktu.R.color;
import static com.example.buuktu.R.mipmap;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.MenuWrapperICS;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.example.buuktu.R;
import com.example.buuktu.utils.DrawableUtils;
import com.example.buuktu.views.CreateCharacterkie;
import com.example.buuktu.views.CreateEditScenariokie;
import com.example.buuktu.views.CreateEditStuffkie;
import com.example.buuktu.views.CreateEditWorldkie;
import com.example.buuktu.views.MainActivity;
import com.example.buuktu.views.Register;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class BottomSheetProfilePhotoDefault extends BottomSheetDialogFragment implements View.OnClickListener {
    Context context;
    ImageButton ib_prf_imgOne, ib_prf_imgTwo, ib_prf_imgThree, ib_prf_imgFour, ib_prf_imgFive, ib_prf_imgSix, ib_prf_imgSeven, ib_prf_imgEight, ib_prf_imgNine, ib_prf_imgTen, ib_prf_imgEleven, ib_back_default_photo_profile;
    Register register;
    ArrayList<ImageButton> imageButtons;
    CreateEditWorldkie createEditWorldkie;
    CreateCharacterkie createCharacterkie;
    CreateEditStuffkie createEditStuffkie;
    CreateEditScenariokie createEditScenariokie;
    ArrayList<Integer> photosWorldkies ;
    ArrayList<Integer> photosUserkies ;
    ArrayList<Integer> photosStuffkies;
    ArrayList<Integer> photosCharacterkies ;
    ArrayList<Integer> photosScenariokies ;

    public BottomSheetProfilePhotoDefault() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
    ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_default_image_profile_photo,
                container, false);
        initComponents(v);

        context = getContext();
        fillArrays();
        setImages();
        setListeners();
        return v;
    }

    private void fillArrays() {
        if (register != null) {
            photosUserkies = new ArrayList<>();
            photosUserkies.add(R.mipmap.photoprofileone);
            photosUserkies.add(R.mipmap.photoprofiletwo);
            photosUserkies.add(mipmap.photoprofilethree);
            photosUserkies.add(mipmap.photoprofilefour);
            photosUserkies.add(mipmap.photoprofilefive);
            photosUserkies.add(mipmap.photoprofilesix);
            photosUserkies.add(mipmap.photoprofileseven);
            photosUserkies.add(mipmap.photoprofileeight);
            photosUserkies.add(mipmap.photoprofilenine);
            photosUserkies.add(mipmap.photoprofileten);
            photosUserkies.add(mipmap.photoprofileeleven);

        } else if (createEditWorldkie != null) {
            photosWorldkies = new ArrayList<>();
            photosWorldkies.add(R.mipmap.photoworldkieone);
            photosWorldkies.add(R.mipmap.photoworldkietwo);
            photosWorldkies.add(R.mipmap.photoworldkiethree);
            photosWorldkies.add(R.mipmap.photoworldkiefour);
            photosWorldkies.add(R.mipmap.photoworldkiefive);
            photosWorldkies.add(R.mipmap.photoworldkiesix);
            photosWorldkies.add(R.mipmap.photoworldkieseven);
            photosWorldkies.add(R.mipmap.photoworldkieeight);
            photosWorldkies.add(R.mipmap.photoworldkienine);
            photosWorldkies.add(R.mipmap.photoworldkieten);
            photosWorldkies.add(R.mipmap.photoworldkieeleven);

        } else if (createCharacterkie != null) {
            photosCharacterkies = new ArrayList<>();
            photosCharacterkies.add(R.mipmap.photocharacterkieone);
            photosCharacterkies.add(R.mipmap.photocharacterkietwo);
            photosCharacterkies.add(R.mipmap.photocharacterkiethree);
            photosCharacterkies.add(R.mipmap.photocharacterkiefour);
            photosCharacterkies.add(R.mipmap.photocharacterkiefive);
            photosCharacterkies.add(R.mipmap.photocharacterkiesix);
            photosCharacterkies.add(R.mipmap.photocharacterkieseven);
            photosCharacterkies.add(mipmap.photocharacterkieeight);
            photosCharacterkies.add(mipmap.photocharacterkienine);
            photosCharacterkies.add(mipmap.photocharacterkieten);
            photosCharacterkies.add(mipmap.photocharacterkieeleven);
        } else if (createEditStuffkie != null) {
            photosStuffkies = new ArrayList<>();
            photosStuffkies.add(mipmap.photostuffkieone);
            photosStuffkies.add(mipmap.photostuffkietwo);
            photosStuffkies.add(mipmap.photostuffkiethree);
            photosStuffkies.add(mipmap.photostuffkiefour);
            photosStuffkies.add(mipmap.photostuffkiefive);
            photosStuffkies.add(mipmap.photostuffkiesix);
            photosStuffkies.add(mipmap.photostuffkieseven);
            photosStuffkies.add(mipmap.photostuffkieeight);
            photosStuffkies.add(mipmap.photostuffkienine);
            photosStuffkies.add(mipmap.photostuffkieten);
            photosStuffkies.add(mipmap.photostuffkieeleven);
        } else if (createEditScenariokie != null) {
            photosScenariokies = new ArrayList<>();
            photosScenariokies.add(mipmap.photoscenariokieone);
            photosScenariokies.add(mipmap.photoscenariokietwo);
            photosScenariokies.add(mipmap.photoscenariokiethree);
            photosScenariokies.add(mipmap.photoscenariokiefour);
            photosScenariokies.add(mipmap.photoscenariokiefive);
            photosScenariokies.add(mipmap.photoscenariokiesix);
            photosScenariokies.add(mipmap.photoscenariokieseven);
            photosScenariokies.add(mipmap.photoscenariokieeight);
            photosScenariokies.add(mipmap.photoscenariokienine);
            photosScenariokies.add(mipmap.photoscenariokieten);
            photosScenariokies.add(mipmap.photoscenariokieeleven);
        }
        imageButtons.add(ib_prf_imgOne);
        imageButtons.add(ib_prf_imgTwo);
        imageButtons.add(ib_prf_imgThree);
        imageButtons.add(ib_prf_imgFour);
        imageButtons.add(ib_prf_imgFive);
        imageButtons.add(ib_prf_imgSix);
        imageButtons.add(ib_prf_imgSeven);
        imageButtons.add(ib_prf_imgEight);
        imageButtons.add(ib_prf_imgNine);
        imageButtons.add(ib_prf_imgTen);
        imageButtons.add(ib_prf_imgEleven);
    }

    private void setImages() {
        int imageIndex = 0;
        for (ImageButton imageButton : imageButtons) {
            if (register != null) {
                DrawableUtils.personalizarImagenCircleButton(context, DrawableUtils.drawableToBitmap(ContextCompat.getDrawable(context, photosUserkies.get(imageIndex))), imageButton, color.blue1);
            } else if (createCharacterkie != null) {
                DrawableUtils.personalizarImagenCircleButton(context, DrawableUtils.drawableToBitmap(ContextCompat.getDrawable(context, photosCharacterkies.get(imageIndex))), imageButton, color.greenWhatever);

                imageButton.setImageDrawable(ContextCompat.getDrawable(context, photosCharacterkies.get(imageIndex)));
            } else if (createEditScenariokie != null) {
                DrawableUtils.personalizarImagenCuadradoButton(context, 80 / 6, 7, color.greenWhatever, photosScenariokies.get(imageIndex), imageButton);

                imageButton.setImageDrawable(ContextCompat.getDrawable(context, photosScenariokies.get(imageIndex)));
            } else if (createEditStuffkie != null) {
                DrawableUtils.personalizarImagenCuadradoButton(context, 80 / 6, 7, color.greenWhatever, photosStuffkies.get(imageIndex), imageButton);

                imageButton.setImageDrawable(ContextCompat.getDrawable(context, photosStuffkies.get(imageIndex)));
            } else if (createEditWorldkie != null) {
                DrawableUtils.personalizarImagenCuadradoButton(context, 80 / 6, 7, color.greenWhatever, photosWorldkies.get(imageIndex), imageButton);
            }
            imageIndex++;
        }
    }

    private void initComponents(View v) {
        ib_prf_imgOne = v.findViewById(R.id.ib_prf_imgOne);
        ib_prf_imgTwo = v.findViewById(R.id.ib_prf_imgTwo);
        ib_prf_imgThree = v.findViewById(R.id.ib_prf_imgThree);
        ib_prf_imgFour = v.findViewById(R.id.ib_prf_imgFour);
        ib_prf_imgFive = v.findViewById(R.id.ib_prf_imgFive);
        ib_prf_imgSix = v.findViewById(R.id.ib_prf_imgSix);
        ib_prf_imgSeven = v.findViewById(R.id.ib_prf_imgSeven);
        ib_prf_imgEight = v.findViewById(R.id.ib_prf_imgEight);
        ib_prf_imgNine = v.findViewById(R.id.ib_prf_imgNine);
        ib_prf_imgTen = v.findViewById(R.id.ib_prf_imgTen);
        ib_prf_imgEleven = v.findViewById(R.id.ib_prf_imgEleven);
        ib_back_default_photo_profile = v.findViewById(R.id.ib_back_default_photo_profile);
        if (getActivity() instanceof MainActivity) {
            if (getParentFragment() instanceof CreateEditWorldkie) {
                imageButtons = new ArrayList<>();
                createEditWorldkie = (CreateEditWorldkie) getParentFragment();
            }
            else if (getParentFragment() instanceof CreateEditScenariokie)
            {
                imageButtons = new ArrayList<>();

                createEditScenariokie = (CreateEditScenariokie) getParentFragment();
            }
            else if (getParentFragment() instanceof CreateCharacterkie)
            {
                imageButtons = new ArrayList<>();

                createCharacterkie = (CreateCharacterkie) getParentFragment();
            }
            else if (getParentFragment() instanceof CreateEditStuffkie) {
                imageButtons = new ArrayList<>();
                createEditStuffkie = (CreateEditStuffkie) getParentFragment();
            }

        } else if (getActivity() instanceof Register) register = (Register) getActivity();
    }

    private void setListeners() {
        for (ImageButton imageButton : imageButtons) {
            imageButton.setOnClickListener(this);
        }
        ib_back_default_photo_profile.setOnClickListener(this);
    }

    private void setSelectedPhoto(ImageButton imageButton, int index) {
        if (register != null) {
            register.setSelectedProfilePhoto(imageButton.getDrawable());
            register.getIB_profile_photo().setTag(DrawableUtils.getMipmapName(context, photosUserkies.get(index)));
            register.setPhotoDefault();
        } else if (createEditWorldkie != null) {
            createEditWorldkie.setSelectedProfilePhoto(imageButton.getDrawable());
            createEditWorldkie.getIb_select_img_create_worldkie().setTag(DrawableUtils.getMipmapName(context, photosWorldkies.get(index)));
            createEditWorldkie.setPhotoDefault();

        } else if (createCharacterkie != null) {
            createCharacterkie.setSelectedProfilePhoto(imageButton.getDrawable());
            createCharacterkie.getIb_select_img_create_worldkie().setTag(DrawableUtils.getMipmapName(context, photosCharacterkies.get(index)));
            createCharacterkie.setPhotoDefault();

        } else if (createEditStuffkie != null) {
            createEditStuffkie.setSelectedProfilePhoto(imageButton.getDrawable());
            createEditStuffkie.getIb_select_img_create_stuffkie().setTag(DrawableUtils.getMipmapName(context, photosStuffkies.get(index)));
            createEditStuffkie.setPhotoDefault();


        } else if (createEditScenariokie != null) {
            createEditScenariokie.setSelectedProfilePhoto(imageButton.getDrawable());
            createEditScenariokie.getIb_select_img_create_scenariokie().setTag(DrawableUtils.getMipmapName(context, photosScenariokies.get(index)));
            createEditScenariokie.setPhotoDefault();

        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ib_prf_imgOne) setSelectedPhoto(ib_prf_imgOne, 0);
        else if (v.getId() == R.id.ib_prf_imgTwo) setSelectedPhoto(ib_prf_imgTwo, 1);
        else if (v.getId() == R.id.ib_prf_imgThree) setSelectedPhoto(ib_prf_imgThree, 2);
        else if (v.getId() == R.id.ib_prf_imgFour) setSelectedPhoto(ib_prf_imgFour, 3);
        else if (v.getId() == R.id.ib_prf_imgFive) setSelectedPhoto(ib_prf_imgFive, 4);

        else if (v.getId() == R.id.ib_prf_imgSix) setSelectedPhoto(ib_prf_imgSix, 5);

        else if (v.getId() == R.id.ib_prf_imgSeven) setSelectedPhoto(ib_prf_imgSeven, 6);

        else if (v.getId() == R.id.ib_prf_imgEight) setSelectedPhoto(ib_prf_imgEight, 7);

        else if (v.getId() == R.id.ib_prf_imgNine) setSelectedPhoto(ib_prf_imgNine, 8);

        else if (v.getId() == R.id.ib_prf_imgTen) setSelectedPhoto(ib_prf_imgTen, 9);
        else if (v.getId() == R.id.ib_prf_imgEleven) setSelectedPhoto(ib_prf_imgEleven, 10);

        else if (v.getId() == R.id.ib_back_default_photo_profile)
            goBackBottomSheetOptionsPhotoProfile();
    }

    public void goBackBottomSheetOptionsPhotoProfile() {
        FragmentManager fragmentManager = getParentFragment() != null
                ? getParentFragment().getChildFragmentManager()
                : requireActivity().getSupportFragmentManager();

        dismiss();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            BottomSheetProfilePhoto bottomSheet = new BottomSheetProfilePhoto();
            bottomSheet.show(fragmentManager, "BottomSheetProfilePhoto");
        }, 100);

    }
}
