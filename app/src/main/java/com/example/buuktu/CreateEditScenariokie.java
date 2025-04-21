package com.example.buuktu;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.buuktu.adapters.RoundedBorderSquareTransformation;
import com.example.buuktu.bottomsheet.BottomSheetProfilePhoto;
import com.example.buuktu.utils.DrawableUtils;
import com.example.buuktu.utils.NavigationUtils;
import com.example.buuktu.views.MainActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateEditScenariokie#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateEditScenariokie extends Fragment implements View.OnClickListener {
    ImageButton ib_select_img_create_scenariokie,ib_back,ib_save;
    Uri image;
    BottomSheetProfilePhoto bottomSheetProfilePhoto;
    String source;
    FragmentManager fragmentManager;
    FragmentActivity activity;
    Switch tb_scenariokiePrivacity,tb_scenariokieDraft;
    FloatingActionButton fb_add_field_createScenariokie;
    public CreateEditScenariokie() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CreateEditScenariokie.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateEditScenariokie newInstance() {
        return new CreateEditScenariokie();
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
        View view = inflater.inflate(R.layout.fragment_create_edit_scenariokie, container, false);
        MainActivity mainActivity = (MainActivity) getActivity();
        ib_back = mainActivity.getBackButton();
        ib_back.setVisibility(View.VISIBLE);
        ib_save = mainActivity.getIb_save();
        ib_save.setVisibility(View.VISIBLE);
        initComponents(view);
        fragmentManager = requireActivity().getSupportFragmentManager();
        activity = requireActivity();
        source = "app";
        bottomSheetProfilePhoto = new BottomSheetProfilePhoto();
        ib_select_img_create_scenariokie = view.findViewById(R.id.ib_select_img_create_scenariokie);
        setListeners();
        try {
            putDefaultImage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return view;
    }
    private void initComponents(View view){
        fb_add_field_createScenariokie = view.findViewById(R.id.fb_add_field_createScenariokie);
        tb_scenariokiePrivacity = view.findViewById(R.id.tb_scenariokiePrivacity);
        tb_scenariokieDraft = view.findViewById(R.id.tb_scenariokieDraft);
    }
    public Drawable getSelectedProfilePhoto()
    {
        return ib_select_img_create_scenariokie.getDrawable();
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
                .transform(new RoundedBorderSquareTransformation(cornerRadius, borderWidth, borderColor));

        Glide.with(getContext())
                .load(imageResId) // 👍 Esto sí pasa por la transformación
                .apply(requestOptions)
                .into(ib_select_img_create_scenariokie);
    }
    private void putDefaultImage() throws IOException {
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.photoscenariokieone);
        DrawableUtils.personalizarImagenCircleButton(getContext(),DrawableUtils.drawableToBitmap(drawable),ib_select_img_create_scenariokie,R.color.brownMaroon);
    }
    public void setSelectedProfilePhoto(Drawable image){
        ib_select_img_create_scenariokie.setImageDrawable(image);
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
        return ib_select_img_create_scenariokie;
    }
    private void setListeners(){
        ib_select_img_create_scenariokie.setOnClickListener(this);
        ib_back.setOnClickListener(this);
        ib_save.setOnClickListener(this);
        tb_scenariokiePrivacity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    tb_scenariokieDraft.setVisibility(View.VISIBLE);
                }else{
                    tb_scenariokieDraft.setVisibility(View.GONE);
                }
            }
        });
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ib_select_img_create_scenariokie) {
            selectImage();

        } else if (v.getId() == R.id.ib_back) {
            NavigationUtils.goBack(fragmentManager,activity);
          //  goBackToPreviousFragment();

        } else if (v.getId()==R.id.ib_save) {

                /*if(worldkie_id == null){
                    addDataToFirestore();
                }else{
                    editDataFirestore();
                }*/
        }
    }
}