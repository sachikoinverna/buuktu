package com.example.buuktu.views;

import static android.provider.MediaStore.Images.Media.getBitmap;

import static com.example.buuktu.R.*;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import com.example.buuktu.utils.BitmapUtils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetProfilePhotoDefault extends BottomSheetDialogFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
    ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_default_image_profile_photo,
                container, false);

        ImageButton ib_prf_imgOne = v.findViewById(R.id.ib_prf_imgOne);
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.photoprofileone);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        personalizarImagen(bitmap,ib_prf_imgOne);
        ImageButton ib_prf_imgTwo = v.findViewById(R.id.ib_prf_imgTwo);
        Drawable drawable2 = ContextCompat.getDrawable(getContext(), R.mipmap.photoprofiletwo);
        Bitmap bitmap2 = ((BitmapDrawable) drawable2).getBitmap();
        personalizarImagen(bitmap2,ib_prf_imgTwo);

        ImageButton ib_prf_imgThree = v.findViewById(R.id.ib_prf_imgThree);
        Drawable drawable3 = ContextCompat.getDrawable(getContext(), R.mipmap.photoprofilethree);
        Bitmap bitmap3 = ((BitmapDrawable) drawable3).getBitmap();
        personalizarImagen(bitmap3,ib_prf_imgThree);

        ImageButton ib_prf_imgFour = v.findViewById(R.id.ib_prf_imgFour);
        Drawable drawable4 = ContextCompat.getDrawable(getContext(), R.mipmap.photoprofilefour);
        Bitmap bitmap4 = ((BitmapDrawable) drawable4).getBitmap();
        personalizarImagen(bitmap4,ib_prf_imgFour);

        ImageButton ib_prf_imgFive = v.findViewById(R.id.ib_prf_imgFive);
        Drawable drawable5 = ContextCompat.getDrawable(getContext(), R.mipmap.photoprofilefive);
        Bitmap bitmap5 = ((BitmapDrawable) drawable5).getBitmap();
        personalizarImagen(bitmap5,ib_prf_imgFive);

        ImageButton ib_prf_imgSix = v.findViewById(R.id.ib_prf_imgSix);
        Drawable drawable6 = ContextCompat.getDrawable(getContext(), R.mipmap.photoprofilesix);
        Bitmap bitmap6 = ((BitmapDrawable) drawable6).getBitmap();
        personalizarImagen(bitmap6,ib_prf_imgSix);

        ImageButton ib_prf_imgSeven = v.findViewById(R.id.ib_prf_imgSeven);
        Drawable drawable7 = ContextCompat.getDrawable(getContext(), mipmap.photoprofileseven);
        Bitmap bitmap7 = ((BitmapDrawable) drawable7).getBitmap();
        personalizarImagen(bitmap7,ib_prf_imgSeven);

        ImageButton ib_prf_imgEight = v.findViewById(R.id.ib_prf_imgEight);
        Drawable drawable8 = ContextCompat.getDrawable(getContext(), R.mipmap.photoprofileeight);
        Bitmap bitmap8 = ((BitmapDrawable) drawable8).getBitmap();
        personalizarImagen(bitmap8,ib_prf_imgEight);

        ImageButton ib_prf_imgNine = v.findViewById(R.id.ib_prf_imgNine);
        Drawable drawable9 = ContextCompat.getDrawable(getContext(), mipmap.photoprofilenine);
        Bitmap bitmap9 = ((BitmapDrawable) drawable9).getBitmap();
        personalizarImagen(bitmap9,ib_prf_imgNine);

        ImageButton ib_prf_imgTen = v.findViewById(R.id.ib_prf_imgTen);
        Drawable drawable10 = ContextCompat.getDrawable(getContext(), R.mipmap.photoprofileten);
        Bitmap bitmap10 = ((BitmapDrawable) drawable10).getBitmap();
        personalizarImagen(bitmap10,ib_prf_imgTen);

        ImageButton ib_prf_imgEleven = v.findViewById(R.id.ib_prf_imgEleven);
        Drawable drawable11 = ContextCompat.getDrawable(getContext(), R.mipmap.photoprofileeleven);
        Bitmap bitmap11 = ((BitmapDrawable) drawable11).getBitmap();
        personalizarImagen(bitmap11,ib_prf_imgEleven);
       /* if (img_one != null) {
            img_one.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Imagen seleccionada", Toast.LENGTH_SHORT).show();
                }
            });
        }*/
        return v;
    }
    public void personalizarImagen(Bitmap bitmap, ImageButton imageButton){
        //Canvas canvas = new Canvas(circularBitmap);
        //bt_chooseImage.setBor

        RoundedBitmapDrawable roundedDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        roundedDrawable.setCircular(true);
        roundedDrawable.setCornerRadius(bitmap.getHeight());
        imageButton.setImageDrawable(roundedDrawable);
        imageButton.setBackgroundColor(Color.TRANSPARENT);
        Drawable drawableBorder = getResources().getDrawable(R.drawable.border_register);
        drawableBorder.setTint(Color.RED);
        imageButton.setBackground(drawableBorder);
        imageButton.setPadding(15, 15, 15, 15); // Añadir padding para el borde visible
        imageButton.setScaleType(ImageView.ScaleType.CENTER_CROP); // Ajusta la imagen para que quede dentro del borde
        //bt_chooseImage.set
    }
}
