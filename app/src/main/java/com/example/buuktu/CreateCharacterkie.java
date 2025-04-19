package com.example.buuktu;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.buuktu.adapters.RoundedBorderTransformation;
import com.example.buuktu.bottomsheet.BottomSheetProfilePhoto;
import com.example.buuktu.views.MainActivity;
import com.example.buuktu.views.WorldkieMenu;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateCharacterkie#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateCharacterkie extends Fragment {
    private FragmentManager fragmentManager;
    private Fragment createCharacterkie;
    ImageButton ib_select_img_create_characterkie,ib_back,ib_save;
    Uri image;
    BottomSheetProfilePhoto bottomSheetProfilePhoto;
    String source;
    public CreateCharacterkie() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CreateCharacterkie.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateCharacterkie newInstance() {
        return new CreateCharacterkie();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_create_characterkie, container, false);
        MainActivity mainActivity = (MainActivity) getActivity();
        ib_back = mainActivity.getBackButton();
        ib_back.setVisibility(View.VISIBLE);
        ib_save = mainActivity.getIb_save();
        ib_save.setVisibility(View.VISIBLE);
        ib_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if(worldkie_id == null){
                    addDataToFirestore();
                }else{
                    editDataFirestore();
                }*/
            }
        });
        source = "app";
        bottomSheetProfilePhoto = new BottomSheetProfilePhoto();
        ib_select_img_create_characterkie = view.findViewById(R.id.ib_select_img_create_characterkie);
        ib_select_img_create_characterkie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        return view;
    }
    public Drawable getSelectedProfilePhoto()
    {
        return ib_select_img_create_characterkie.getDrawable();
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
                .transform(new RoundedBorderTransformation(cornerRadius, borderWidth, borderColor));

        Glide.with(getContext())
                .load(imageResId) // 👍 Esto sí pasa por la transformación
                .apply(requestOptions)
                .into(ib_select_img_create_characterkie);
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
        return ib_select_img_create_characterkie;
    }
}