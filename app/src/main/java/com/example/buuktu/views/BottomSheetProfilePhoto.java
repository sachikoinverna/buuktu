package com.example.buuktu.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.Nullable;

import com.example.buuktu.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetProfilePhoto extends BottomSheetDialogFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
    ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_options_images,
                container, false);

        ImageButton img_one = v.findViewById(R.id.ib_imgOne);
        ImageButton img_gal = v.findViewById(R.id.ib_gallery);
        ImageButton img_def = v.findViewById(R.id.ib_default);

        /*.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),
                        "Algorithm Shared", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        course_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),
                        "Course Shared", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });*/
        return v;
    }
}
