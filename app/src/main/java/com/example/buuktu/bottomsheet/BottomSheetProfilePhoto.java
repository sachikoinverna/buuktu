package com.example.buuktu.bottomsheet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.buuktu.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetProfilePhoto extends BottomSheetDialogFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
    ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_options_images,
                container, false);

       // ImageButton img_one = v.findViewById(R.id.ib_imgOne);
        ImageButton img_gal = v.findViewById(R.id.ib_gallery);
        ImageButton img_def = v.findViewById(R.id.ib_default);

       /* if (img_one != null) {
            img_one.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Imagen seleccionada", Toast.LENGTH_SHORT).show();
                }
            });
        }*/

        if (img_gal != null) {
            img_gal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Galeria seleccionada", Toast.LENGTH_SHORT).show();
                    //selectImageGallery();
                }
            });
        }

        if (img_def != null) {
            img_def.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Imagenes default seleccionada", Toast.LENGTH_SHORT).show();
                    //dialog2.show();
                }
            });
        }
        return v;
    }
}
