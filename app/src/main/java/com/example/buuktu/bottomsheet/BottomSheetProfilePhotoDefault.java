package com.example.buuktu.bottomsheet;

import static com.example.buuktu.R.*;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.NinePatch;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.FileObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.example.buuktu.CreateCharacterkie;
import com.example.buuktu.CreateEditScenariokie;
import com.example.buuktu.CreateEditStuffkie;
import com.example.buuktu.R;
import com.example.buuktu.utils.DrawableUtils;
import com.example.buuktu.views.CreateEditWorldkie;
import com.example.buuktu.views.MainActivity;
import com.example.buuktu.views.Register;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.transition.FadeThroughProvider;

import java.util.ArrayList;

public class BottomSheetProfilePhotoDefault extends BottomSheetDialogFragment  implements View.OnClickListener{
    Context context;
    ImageButton ib_prf_imgOne,ib_prf_imgTwo,ib_prf_imgThree,ib_prf_imgFour,ib_prf_imgFive,ib_prf_imgSix ,ib_prf_imgSeven ,ib_prf_imgEight ,ib_prf_imgNine ,ib_prf_imgTen ,ib_prf_imgEleven;
    Bitmap selectedBitmap;
    Register register;
    ArrayList<ImageButton> imageButtons;
    CreateEditWorldkie createEditWorldkie;
    CreateCharacterkie createCharacterkie;
    CreateEditStuffkie createEditStuffkie;
    CreateEditScenariokie createEditScenariokie;

    public BottomSheetProfilePhotoDefault(){
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
    ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_default_image_profile_photo,
                container, false);
        initComponents(v);
        if(getActivity() instanceof MainActivity){
            if(getParentFragment() instanceof CreateEditWorldkie){
                createEditWorldkie = (CreateEditWorldkie) getParentFragment();
                Log.d("BottomSheet", "createEditWorldkie detectado");

            } else if (getParentFragment() instanceof CreateEditScenariokie) {
                createEditScenariokie = (CreateEditScenariokie) getParentFragment();
            }else if (getParentFragment() instanceof CreateCharacterkie) {
                createCharacterkie = (CreateCharacterkie) getParentFragment();
            }else if (getParentFragment() instanceof CreateEditStuffkie) {
                createEditStuffkie = (CreateEditStuffkie) getParentFragment();
            }
        }
        imageButtons = new ArrayList<>();
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
        if (getActivity() instanceof Register) {
            register = (Register) getActivity();
        }
        context = getContext();
        if(register !=null) {
            ib_prf_imgOne.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.photoprofileone));
            ib_prf_imgTwo.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.photoprofiletwo));
            ib_prf_imgThree.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.photoprofilethree));
            ib_prf_imgFour.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.photoprofilefour));
            ib_prf_imgFive.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.photoprofilefive));
            ib_prf_imgSix.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.photoprofilesix));
            ib_prf_imgSeven.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.photoprofileseven));
            ib_prf_imgEight.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.photoprofileeight));
            ib_prf_imgNine.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.photoprofilenine));
            ib_prf_imgTen.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.photoprofileten));
            ib_prf_imgEleven.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.photoprofileeleven));
        }
        if(createEditWorldkie!=null){
            ib_prf_imgOne.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.photoworldkieone));
            ib_prf_imgTwo.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.photoworldkietwo));
            ib_prf_imgThree.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.photoworldkiethree));
            ib_prf_imgFour.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.photoworldkiefour));
            ib_prf_imgFive.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.photoworldkiefive));
            ib_prf_imgSix.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.photoworldkiesix));
            ib_prf_imgSeven.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.photoworldkieseven));
            ib_prf_imgEight.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.photoworldkieeight));
            ib_prf_imgNine.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.photoworldkienine));
            ib_prf_imgTen.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.photoworldkieten));
            ib_prf_imgEleven.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.photoworldkieeleven));
        }
        if(createCharacterkie!=null){
            ib_prf_imgOne.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.photocharacterkieone));
            ib_prf_imgTwo.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.photocharacterkietwo));
            ib_prf_imgThree.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.photocharacterkiethree));
            ib_prf_imgFour.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.photocharacterkiefour));
            ib_prf_imgFive.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.photocharacterkiefive));
            ib_prf_imgSix.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.photocharacterkiesix));
            ib_prf_imgSeven.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.photocharacterkieseven));
            ib_prf_imgEight.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.photoworldkieeight));
            ib_prf_imgNine.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.photoworldkienine));
            ib_prf_imgTen.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.photoworldkieten));
            ib_prf_imgEleven.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.photoworldkieeleven));
        }
        if(createEditStuffkie!=null){
            ib_prf_imgOne.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.photocharacterkieone));
            ib_prf_imgTwo.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.photocharacterkietwo));
            ib_prf_imgThree.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.photocharacterkiethree));
            ib_prf_imgFour.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.photocharacterkiefour));
            ib_prf_imgFive.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.photocharacterkiefive));
            ib_prf_imgSix.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.photocharacterkiesix));
            ib_prf_imgSeven.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.photocharacterkieseven));
            ib_prf_imgEight.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.photoworldkieeight));
            ib_prf_imgNine.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.photoworldkienine));
            ib_prf_imgTen.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.photoworldkieten));
            ib_prf_imgEleven.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.photoworldkieeleven));
        }
        if(createEditScenariokie!=null){
            ib_prf_imgOne.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.photocharacterkieone));
            ib_prf_imgTwo.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.photocharacterkietwo));
            ib_prf_imgThree.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.photocharacterkiethree));
            ib_prf_imgFour.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.photocharacterkiefour));
            ib_prf_imgFive.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.photocharacterkiefive));
            ib_prf_imgSix.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.photocharacterkiesix));
            ib_prf_imgSeven.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.photocharacterkieseven));
            ib_prf_imgEight.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.photoworldkieeight));
            ib_prf_imgNine.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.photoworldkienine));
            ib_prf_imgTen.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.photoworldkieten));
            ib_prf_imgEleven.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.photoworldkieeleven));
        }
        setListeners();
        setImageBottomSheetOpen();
        return v;
    }
    private void initComponents(View v){
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

    }
    private void setListeners(){
        for(ImageButton imageButton: imageButtons){
            imageButton.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ib_prf_imgOne) {
            if(register != null) {
                register.setSelectedProfilePhoto(ib_prf_imgOne.getDrawable());
            } else if (createEditWorldkie !=null) {
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.photoworldkieone);
                createEditWorldkie.setSelectedProfilePhoto(R.mipmap.photoworldkieone);
            } else if (createCharacterkie !=null) {
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.photoworldkieone);
                createCharacterkie.setSelectedProfilePhoto(R.mipmap.photoworldkieone);
            } else if (createEditStuffkie !=null) {
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.photoworldkieone);
                //createEditStuffkie.setSelectedProfilePhoto(R.mipmap.photoworldkieone);
            }else if (createEditScenariokie !=null) {
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.photoworldkieone);
                //createEditStuffkie.setSelectedProfilePhoto(R.mipmap.photoworldkieone);
            }
        } else if (v.getId() == R.id.ib_prf_imgTwo) {
            if(register != null) {
                register.setSelectedProfilePhoto(ib_prf_imgTwo.getDrawable());
            } else if (createEditWorldkie !=null) {
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.photoworldkietwo);
                createEditWorldkie.setSelectedProfilePhoto(R.mipmap.photoworldkietwo);
            }else if (createCharacterkie !=null) {
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.photoworldkieone);
                createCharacterkie.setSelectedProfilePhoto(R.mipmap.photoworldkieone);
            }else if (createEditStuffkie !=null) {
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.photoworldkieone);
                //createEditStuffkie.setSelectedProfilePhoto(R.mipmap.photoworldkieone);
            }else if (createEditScenariokie !=null) {
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.photoworldkieone);
                //createEditStuffkie.setSelectedProfilePhoto(R.mipmap.photoworldkieone);
            }

        } else if (v.getId() == R.id.ib_prf_imgThree) {
            if(register != null) {
                register.setSelectedProfilePhoto(ib_prf_imgThree.getDrawable());
            } else if (createEditWorldkie !=null) {
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.photoworldkiethree);
                createEditWorldkie.setSelectedProfilePhoto(R.mipmap.photoworldkiethree);
            }else if (createCharacterkie !=null) {
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.photoworldkieone);
                createCharacterkie.setSelectedProfilePhoto(R.mipmap.photoworldkieone);
            }else if (createEditStuffkie !=null) {
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.photoworldkieone);
                //createEditStuffkie.setSelectedProfilePhoto(R.mipmap.photoworldkieone);
            }else if (createEditScenariokie !=null) {
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.photoworldkieone);
                //createEditStuffkie.setSelectedProfilePhoto(R.mipmap.photoworldkieone);
            }
        } else if (v.getId() == R.id.ib_prf_imgFour) {
            if(register != null) {
                register.setSelectedProfilePhoto(ib_prf_imgFour.getDrawable());
            } else if (createEditWorldkie !=null) {
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.photoworldkiefour);
                createEditWorldkie.setSelectedProfilePhoto(R.mipmap.photoworldkiefour);
            }else if (createCharacterkie !=null) {
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.photoworldkieone);
                createCharacterkie.setSelectedProfilePhoto(R.mipmap.photoworldkieone);
            }else if (createEditStuffkie !=null) {
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.photoworldkieone);
                //createEditStuffkie.setSelectedProfilePhoto(R.mipmap.photoworldkieone);
            }else if (createEditScenariokie !=null) {
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.photoworldkieone);
                //createEditStuffkie.setSelectedProfilePhoto(R.mipmap.photoworldkieone);
            }
        } else if (v.getId() == R.id.ib_prf_imgFive) {
            if(register != null) {
                register.setSelectedProfilePhoto(ib_prf_imgFive.getDrawable());
            } else if (createEditWorldkie !=null) {
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.photoworldkiefive);

                createEditWorldkie.setSelectedProfilePhoto(R.mipmap.photoworldkiefive);
            }else if (createCharacterkie !=null) {
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.photoworldkieone);
                createCharacterkie.setSelectedProfilePhoto(R.mipmap.photoworldkieone);
            }else if (createEditStuffkie !=null) {
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.photoworldkieone);
                //createEditStuffkie.setSelectedProfilePhoto(R.mipmap.photoworldkieone);
            }else if (createEditScenariokie !=null) {
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.photoworldkieone);
                //createEditStuffkie.setSelectedProfilePhoto(R.mipmap.photoworldkieone);
            }
        } else if (v.getId() == R.id.ib_prf_imgSix) {
            if(register != null) {
                register.setSelectedProfilePhoto(ib_prf_imgSix.getDrawable());
            } else if (createEditWorldkie !=null) {
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.photoworldkiesix);
                createEditWorldkie.setSelectedProfilePhoto(R.mipmap.photoworldkiesix);
            }else if (createCharacterkie !=null) {
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.photoworldkieone);
                createCharacterkie.setSelectedProfilePhoto(R.mipmap.photoworldkieone);
            }else if (createEditStuffkie !=null) {
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.photoworldkieone);
                //createEditStuffkie.setSelectedProfilePhoto(R.mipmap.photoworldkieone);
            }else if (createEditScenariokie !=null) {
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.photoworldkieone);
                //createEditStuffkie.setSelectedProfilePhoto(R.mipmap.photoworldkieone);
            }
        } else if (v.getId() == R.id.ib_prf_imgSeven) {
            if(register != null) {
                register.setSelectedProfilePhoto(ib_prf_imgSeven.getDrawable());
            } else if (createEditWorldkie !=null) {
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.photoworldkieseven);
                createEditWorldkie.setSelectedProfilePhoto(R.mipmap.photoworldkieseven);
            }else if (createCharacterkie !=null) {
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.photoworldkieone);
                createCharacterkie.setSelectedProfilePhoto(R.mipmap.photoworldkieone);
            }else if (createEditStuffkie !=null) {
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.photoworldkieone);
                //createEditStuffkie.setSelectedProfilePhoto(R.mipmap.photoworldkieone);
            }else if (createEditScenariokie !=null) {
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.photoworldkieone);
                //createEditStuffkie.setSelectedProfilePhoto(R.mipmap.photoworldkieone);
            }

        } else if (v.getId() == R.id.ib_prf_imgEight) {
            if(register != null) {
                register.setSelectedProfilePhoto(ib_prf_imgEight.getDrawable());
            } else if (createEditWorldkie !=null) {
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.photoworldkieeight);
                createEditWorldkie.setSelectedProfilePhoto(R.mipmap.photoworldkieeight);
            }else if (createCharacterkie !=null) {
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.photoworldkieone);
                createCharacterkie.setSelectedProfilePhoto(R.mipmap.photoworldkieone);
            }
            else if (createEditStuffkie !=null) {
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.photoworldkieone);
                //createEditStuffkie.setSelectedProfilePhoto(R.mipmap.photoworldkieone);
            }else if (createEditScenariokie !=null) {
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.photoworldkieone);
                //createEditStuffkie.setSelectedProfilePhoto(R.mipmap.photoworldkieone);
            }
        } else if (v.getId() == R.id.ib_prf_imgNine) {
            if(register != null) {
                register.setSelectedProfilePhoto(ib_prf_imgNine.getDrawable());
            } else if (createEditWorldkie !=null) {
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.photoworldkienine);

                createEditWorldkie.setSelectedProfilePhoto(R.mipmap.photoworldkienine);
            }else if (createCharacterkie !=null) {
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.photoworldkieone);
                createCharacterkie.setSelectedProfilePhoto(R.mipmap.photoworldkieone);
            }
            else if (createEditStuffkie !=null) {
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.photoworldkieone);
                //createEditStuffkie.setSelectedProfilePhoto(R.mipmap.photoworldkieone);
            }else if (createEditScenariokie !=null) {
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.photoworldkieone);
                //createEditStuffkie.setSelectedProfilePhoto(R.mipmap.photoworldkieone);
            }
        } else if (v.getId() == R.id.ib_prf_imgTen) {
            if(register != null) {
                register.setSelectedProfilePhoto(ib_prf_imgTen.getDrawable());
            } else if (createEditWorldkie !=null) {
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.photoworldkieten);
                createEditWorldkie.setSelectedProfilePhoto(R.mipmap.photoworldkieten);
            }else if (createCharacterkie !=null) {
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.photoworldkieone);
                createCharacterkie.setSelectedProfilePhoto(R.mipmap.photoworldkieone);
            }
            else if (createEditStuffkie !=null) {
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.photoworldkieone);
                //createEditStuffkie.setSelectedProfilePhoto(R.mipmap.photoworldkieone);
            }else if (createEditScenariokie !=null) {
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.photoworldkieone);
                //createEditStuffkie.setSelectedProfilePhoto(R.mipmap.photoworldkieone);
            }
        } else if (v.getId() == R.id.ib_prf_imgEleven)
        {
            if(register != null) {
                register.setSelectedProfilePhoto(ib_prf_imgEleven.getDrawable());
            } else if (createEditWorldkie !=null) {
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.photoworldkieeleven);
                createEditWorldkie.setSelectedProfilePhoto(R.mipmap.photoworldkieeleven);
            }else if (createCharacterkie !=null) {
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.photoworldkieone);
                createCharacterkie.setSelectedProfilePhoto(R.mipmap.photoworldkieone);
            }else if (createEditStuffkie !=null) {
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.photoworldkieone);
                //createEditStuffkie.setSelectedProfilePhoto(R.mipmap.photoworldkieone);
            }else if (createEditScenariokie !=null) {
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.photoworldkieone);
                //createEditStuffkie.setSelectedProfilePhoto(R.mipmap.photoworldkieone);
            }
        } else if(v.getId() == R.id.ib_back_default_photo_profile){
            goBackBottomSheetOptionsPhotoProfile();
        }
    }
    private void setSelectedImage(Drawable drawable,ImageButton imageButton){
        if(register!=null) {
            if (!drawable.equals(register.getSelectedProfilePhoto())) {
                for (ImageButton button : imageButtons) {
                    if (button.getDrawable().equals(drawable)) {
                        DrawableUtils.personalizarImagenCircleButton(context, DrawableUtils.drawableToBitmap(button.getDrawable()), button, R.color.greenWhatever, true);
                    } else {
                        DrawableUtils.personalizarImagenCircleButton(context, DrawableUtils.drawableToBitmap(button.getDrawable()), button, R.color.redError, false);

                    }
                }
                register.setSource("app");
                DrawableUtils.personalizarImagenCircleButton(context, DrawableUtils.drawableToBitmap(drawable), imageButton, R.color.brownMaroon, false);
            }
        } else if (createEditWorldkie != null) {
            if (!drawable.equals(createEditWorldkie.getSelectedProfilePhoto())) {
                for (ImageButton button : imageButtons) {
                    if (button.getDrawable().equals(drawable)) {
                        DrawableUtils.personalizarImagenCircleButton(context, DrawableUtils.drawableToBitmap(button.getDrawable()), button, R.color.greenWhatever, true);
                    } else {
                        DrawableUtils.personalizarImagenCircleButton(context, DrawableUtils.drawableToBitmap(button.getDrawable()), button, R.color.redError, false);

                    }
                }
                createEditWorldkie.setSource("app");
                DrawableUtils.personalizarImagenCircleButton(context, DrawableUtils.drawableToBitmap(drawable), imageButton, R.color.brownMaroon, false);
            }

        }else if (createCharacterkie != null) {
            if (!drawable.equals(createCharacterkie.getSelectedProfilePhoto())) {
                for (ImageButton button : imageButtons) {
                    if (button.getDrawable().equals(drawable)) {
                        DrawableUtils.personalizarImagenCircleButton(context, DrawableUtils.drawableToBitmap(button.getDrawable()), button, R.color.greenWhatever, true);
                    } else {
                        DrawableUtils.personalizarImagenCircleButton(context, DrawableUtils.drawableToBitmap(button.getDrawable()), button, R.color.redError, false);

                    }
                }
                createCharacterkie.setSource("app");
                DrawableUtils.personalizarImagenCircleButton(context, DrawableUtils.drawableToBitmap(drawable), imageButton, R.color.brownMaroon, false);
            }

        }else if (createEditStuffkie != null) {
            //if (!drawable.equals(createEditStuffkie.getSelectedProfilePhoto())) {
                for (ImageButton button : imageButtons) {
                    if (button.getDrawable().equals(drawable)) {
                  //      DrawableUtils.personalizarImagenCircleButton(context, DrawableUtils.drawableToBitmap(button.getDrawable()), button, R.color.greenWhatever, true);
                    } else {
                    //    DrawableUtils.personalizarImagenCircleButton(context, DrawableUtils.drawableToBitmap(button.getDrawable()), button, R.color.redError, false);

                    }
               // }
                    createEditStuffkie.setSource("app");
                //DrawableUtils.personalizarImagenCircleButton(context, DrawableUtils.drawableToBitmap(drawable), imageButton, R.color.brownMaroon, false);
            }

        }else if (createEditScenariokie != null) {
            //if (!drawable.equals(createEditStuffkie.getSelectedProfilePhoto())) {
            for (ImageButton button : imageButtons) {
                if (button.getDrawable().equals(drawable)) {
                    //      DrawableUtils.personalizarImagenCircleButton(context, DrawableUtils.drawableToBitmap(button.getDrawable()), button, R.color.greenWhatever, true);
                } else {
                    //    DrawableUtils.personalizarImagenCircleButton(context, DrawableUtils.drawableToBitmap(button.getDrawable()), button, R.color.redError, false);

                }
                // }
                //createEditScenariokie.setSource("app");
                //DrawableUtils.personalizarImagenCircleButton(context, DrawableUtils.drawableToBitmap(drawable), imageButton, R.color.brownMaroon, false);
            }

        }
    }
    private void setImageBottomSheetOpen(){
       if(register !=null) {
           for (ImageButton button : imageButtons) {
               if (button.getDrawable().equals(register.getSelectedProfilePhoto())) {
                   DrawableUtils.personalizarImagenCircleButton(context, DrawableUtils.drawableToBitmap(button.getDrawable()), button, color.greenWhatever, true);

               } else {
                   DrawableUtils.personalizarImagenCircleButton(context, DrawableUtils.drawableToBitmap(button.getDrawable()), button, color.greenWhatever, false);

               }
           }
       } else if (createEditWorldkie!=null) {
           for (ImageButton button : imageButtons) {
               if (button.getDrawable().equals(createEditWorldkie.getSelectedProfilePhoto())) {

                   DrawableUtils.personalizarImagenCircleButton(context, DrawableUtils.drawableToBitmap(button.getDrawable()), button, color.greenWhatever, true);

               } else {
                   DrawableUtils.personalizarImagenCircleButton(context, DrawableUtils.drawableToBitmap(button.getDrawable()), button, color.greenWhatever, false);

               }
           }
       }else if (createCharacterkie!=null) {
           for (ImageButton button : imageButtons) {
               if (button.getDrawable().equals(createCharacterkie.getSelectedProfilePhoto())) {

                   DrawableUtils.personalizarImagenCircleButton(context, DrawableUtils.drawableToBitmap(button.getDrawable()), button, color.greenWhatever, true);

               } else {
                   DrawableUtils.personalizarImagenCircleButton(context, DrawableUtils.drawableToBitmap(button.getDrawable()), button, color.greenWhatever, false);

               }
           }
       }else if (createEditStuffkie!=null) {
           for (ImageButton button : imageButtons) {
               /*if (button.getDrawable().equals(createEditStuffkie.getSelectedProfilePhoto())) {

                   DrawableUtils.personalizarImagenCircleButton(context, DrawableUtils.drawableToBitmap(button.getDrawable()), button, color.greenWhatever, true);

               } else {
                   DrawableUtils.personalizarImagenCircleButton(context, DrawableUtils.drawableToBitmap(button.getDrawable()), button, color.greenWhatever, false);

               }*/
           }
       }

    }
    public void goBackBottomSheetOptionsPhotoProfile(){
        if(register!=null) {
            BottomSheetProfilePhoto bottomSheetProfilePhoto = new BottomSheetProfilePhoto();
            bottomSheetProfilePhoto.show(getParentFragmentManager(), "BottomSheetProfilePhoto");
        } else if (createEditWorldkie !=null || createCharacterkie !=null || createEditStuffkie !=null) {
            Log.d("BottomSheet", "createEditWorldkie detectado");
            BottomSheetProfilePhoto bottomSheetProfilePhoto = new BottomSheetProfilePhoto();
            bottomSheetProfilePhoto.show(getChildFragmentManager(), "BottomSheetProfilePhoto");
        }
        dismiss();
    }

}
