package com.example.buuktu.bottomsheet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.example.buuktu.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetChooseGender extends BottomSheetDialogFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
    ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_default_image_profile_photo,
                container, false);
        return v;
    }
}
