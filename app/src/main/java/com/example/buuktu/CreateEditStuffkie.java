package com.example.buuktu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.buuktu.bottomsheet.BottomSheetChooseComponents;
import com.example.buuktu.bottomsheet.BottomSheetProfilePhoto;
import com.example.buuktu.utils.DrawableUtils;
import com.example.buuktu.utils.NavigationUtils;
import com.example.buuktu.views.MainActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateEditStuffkie#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateEditStuffkie extends Fragment implements View.OnClickListener{
    ImageButton ib_select_img_create_stuffkie,ib_back,ib_save;
    FloatingActionButton fb_add_field_createStuffkie,fb_more_createStuffkie;
    Uri image;
    BottomSheetProfilePhoto bottomSheetProfilePhoto;
    String source;
    FragmentManager fragmentManager;
    FragmentActivity activity;
    Context context;
    ConstraintLayout constraintLayout;
    TextInputEditText et_nameStuffkieCreate;
    TextInputLayout et_nameStuffkieCreateFull;
    public CreateEditStuffkie() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CreateEditStuffkie.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateEditStuffkie newInstance() {
        return new CreateEditStuffkie();
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
        View view = inflater.inflate(R.layout.fragment_create_edit_stuffkie, container, false);
        MainActivity mainActivity = (MainActivity) getActivity();
        ib_back = mainActivity.getBackButton();
        ib_back.setVisibility(View.VISIBLE);
        ib_save = mainActivity.getIb_save();
        ib_save.setVisibility(View.VISIBLE);
        initComponents(view);
        fragmentManager = requireActivity().getSupportFragmentManager();
        activity = requireActivity();
        context = getContext();
        setListeners();
        source = "app";
        bottomSheetProfilePhoto = new BottomSheetProfilePhoto();
        try {
            putDefaultImage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return view;
    }
    private void initComponents(View view){
        fb_add_field_createStuffkie = view.findViewById(R.id.fb_add_field_createStuffkie);
        ib_select_img_create_stuffkie = view.findViewById(R.id.ib_select_img_create_stuffkie);
        fb_more_createStuffkie = view.findViewById(R.id.fb_more_createStuffkie);
        et_nameStuffkieCreateFull = view.findViewById(R.id.et_nameStuffkieCreateFull);
        et_nameStuffkieCreate = view.findViewById(R.id.et_nameStuffkieCreate);
        constraintLayout = view.findViewById(R.id.constraint_create_stuffkie);
    }
    private void setListeners(){
        ib_save.setOnClickListener(this);
        ib_back.setOnClickListener(this);
        fb_add_field_createStuffkie.setOnClickListener(this);
        fb_more_createStuffkie.setOnClickListener(this);
    }
    public void setImageUri(Uri image){
        this.image=image;
    }
    public void setSource(String source) {
        this.source = source;
    }
    private void putDefaultImage() throws IOException {
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.photostuffkieone);
        DrawableUtils.personalizarImagenCircleButton(getContext(),DrawableUtils.drawableToBitmap(drawable),ib_select_img_create_stuffkie,R.color.brownMaroon);
    }
    public void selectImage(){
        bottomSheetProfilePhoto.show(getChildFragmentManager(),"BottomSheetProfilePhoto");
    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.ib_save){
                /*if(worldkie_id == null){
                    addDataToFirestore();
                }else{
                    editDataFirestore();
                }*/
            }else if(v.getId()==R.id.ib_back){
            NavigationUtils.goBack(fragmentManager,activity);
        }else if (v.getId()==R.id.fb_add_field_createStuffkie) {
        //    BottomSheetChooseComponents bottomSheetFragment = new BottomSheetChooseComponents(context, constraintLayout, this, fieldsNotAdded);
         //   bottomSheetFragment.show(getChildFragmentManager(), bottomSheetFragment.getTag());
        } else if (v.getId()==R.id.ib_select_img_create_stuffkie) {
            selectImage();
        }
    }
}