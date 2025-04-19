package com.example.buuktu;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.buuktu.bottomsheet.BottomSheetProfilePhoto;
import com.example.buuktu.views.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateEditStuffkie#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateEditStuffkie extends Fragment {
    ImageButton ib_select_img_create_characterkie,ib_back,ib_save;

    Uri image;
    BottomSheetProfilePhoto bottomSheetProfilePhoto;
    String source;
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
        return view;
    }
    public void setImageUri(Uri image){
        this.image=image;
    }
    public void setSource(String source) {
        this.source = source;
    }
}