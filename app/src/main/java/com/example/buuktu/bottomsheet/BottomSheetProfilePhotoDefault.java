package com.example.buuktu.bottomsheet;

import static com.example.buuktu.R.*;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.FileObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.example.buuktu.R;
import com.example.buuktu.utils.DrawableUtils;
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
    public BottomSheetProfilePhotoDefault(){
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
    ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_default_image_profile_photo,
                container, false);
        initComponents(v);
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
            register.setSelectedProfilePhoto(ib_prf_imgOne.getDrawable());
        } else if (v.getId() == R.id.ib_prf_imgTwo) {
            setSelectedImage(ib_prf_imgTwo.getDrawable(),ib_prf_imgTwo);
            register.setSelectedProfilePhoto(ib_prf_imgTwo.getDrawable());

        } else if (v.getId() == R.id.ib_prf_imgThree) {
            register.setSelectedProfilePhoto(ib_prf_imgThree.getDrawable());

        } else if (v.getId() == R.id.ib_prf_imgFour) {
            register.setSelectedProfilePhoto(ib_prf_imgFour.getDrawable());

        } else if (v.getId() == R.id.ib_prf_imgFive) {
            register.setSelectedProfilePhoto(ib_prf_imgFive.getDrawable());

        } else if (v.getId() == R.id.ib_prf_imgSix) {
            register.setSelectedProfilePhoto(ib_prf_imgSix.getDrawable());

        } else if (v.getId() == R.id.ib_prf_imgSeven) {
            register.setSelectedProfilePhoto(ib_prf_imgSeven.getDrawable());


        } else if (v.getId() == R.id.ib_prf_imgEight) {
            register.setSelectedProfilePhoto(ib_prf_imgEight.getDrawable());


        } else if (v.getId() == R.id.ib_prf_imgNine) {
            register.setSelectedProfilePhoto(ib_prf_imgNine.getDrawable());


        } else if (v.getId() == R.id.ib_prf_imgTen) {
            register.setSelectedProfilePhoto(ib_prf_imgTen.getDrawable());


        } else if (v.getId() == R.id.ib_prf_imgEleven)
        {
            register.setSelectedProfilePhoto(ib_prf_imgEleven.getDrawable());

        } else if(v.getId() == R.id.ib_back_default_photo_profile){
            goBackBottomSheetOptionsPhotoProfile();
        }
    }
    private void setSelectedImage(Drawable drawable,ImageButton imageButton){
        if(!drawable.equals(register.getSelectedProfilePhoto())){
            for (ImageButton button: imageButtons){
                if(button.getDrawable().equals(drawable)){
                    DrawableUtils.personalizarImagenCircleButton(context,DrawableUtils.drawableToBitmap(button.getDrawable()),button, R.color.greenWhatever,true);
                }
                else{
                    DrawableUtils.personalizarImagenCircleButton(context,DrawableUtils.drawableToBitmap(button.getDrawable()),button, R.color.redError,false);

                }
            }
            register.setSource("app");
        DrawableUtils.personalizarImagenCircleButton(context,DrawableUtils.drawableToBitmap(drawable),imageButton, R.color.brownMaroon,false);
        }
    }
    private void setImageBottomSheetOpen(){
        for(ImageButton button:imageButtons){
            if(button.getDrawable().equals(register.getSelectedProfilePhoto())){
                DrawableUtils.personalizarImagenCircleButton(context,DrawableUtils.drawableToBitmap(button.getDrawable()),button, color.greenWhatever,true);

            }else{
                DrawableUtils.personalizarImagenCircleButton(context,DrawableUtils.drawableToBitmap(button.getDrawable()),button, color.greenWhatever,false);

            }
        }
    }
    public void goBackBottomSheetOptionsPhotoProfile(){
        BottomSheetProfilePhoto bottomSheetProfilePhoto = new BottomSheetProfilePhoto();
        bottomSheetProfilePhoto.show(getParentFragmentManager(),"BottomSheetProfilePhoto");
        dismiss();
    }

}
