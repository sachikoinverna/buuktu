package com.example.buuktu;

import android.animation.Animator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.DrawableRes;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.buuktu.adapters.RoundedBorderSquareTransformation;
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
    boolean isAllFabsVisible;
    ConstraintLayout constraintLayout;
    TextInputEditText et_nameStuffkieCreate;
    TextInputLayout et_nameStuffkieCreateFull;
    Switch tb_stuffkiePrivacity,tb_stuffkieDraft;
    public CreateEditStuffkie() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CreateEditStuffkie.
     */
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
        tb_stuffkieDraft.setVisibility(View.INVISIBLE);
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
        tb_stuffkiePrivacity = view.findViewById(R.id.tb_stuffkiePrivacity);
        tb_stuffkieDraft = view.findViewById(R.id.tb_stuffkieDraft);
        isAllFabsVisible = false;
        fb_add_field_createStuffkie.setVisibility(View.INVISIBLE);
    }

    private void setListeners(){
        ib_save.setOnClickListener(this);
        ib_back.setOnClickListener(this);
        ib_select_img_create_stuffkie.setOnClickListener(this);
        fb_add_field_createStuffkie.setOnClickListener(this);
        fb_more_createStuffkie.setOnClickListener(this);
        tb_stuffkiePrivacity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    tb_stuffkieDraft.setVisibility(View.VISIBLE);
                }else{
                    tb_stuffkieDraft.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
    public void setImageUri(Uri image){
        this.image=image;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
    private void putDefaultImage() throws IOException {
//        Drawable drawable = ContextCompat.getDrawable(context, R.mipmap.photostuffkieone);
       // DrawableUtils.personalizarImagenCuadradoButton(get);
        DrawableUtils.personalizarImagenCuadradoButton(context,150/7,7,R.color.brownMaroon,R.mipmap.photostuffkieone,ib_select_img_create_stuffkie);
    }
    public void setSelectedProfilePhoto(Drawable image){
      /*  Bitmap bitmap = Bitmap.createBitmap(
                image.getIntrinsicWidth(),
                image.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        image.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        image.draw(canvas);*/
        int cornerRadius = 150 / 6; // Ejemplo de radio
        int borderWidth = 7; // Ejemplo de grosor del borde
        int borderColor = getContext().getResources().getColor(R.color.brownMaroon, null); // Asegúrate de que el color sea correcto
//        Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.photoworldkieone);
        //ib_select_img_create_worldkie.setImageDrawable(image);
    //       DrawableUtils.
        RequestOptions requestOptions = new RequestOptions()
                // .override(150, 150)
                .centerCrop()
                .transform(new RoundedBorderSquareTransformation(cornerRadius,borderWidth,borderColor));

        Glide.with(getContext())
                .load(DrawableUtils.drawableToBitmap(image))
                .apply(requestOptions)
                .into(ib_select_img_create_stuffkie);
    }
    public void setSelectedProfilePhoto(@DrawableRes int imageResId){
        DrawableUtils.personalizarImagenCuadradoButton(getContext(),150/6,7,R.color.brownMaroon,imageResId,ib_select_img_create_stuffkie);

    }
    private void startCircularReveal(Drawable finalDrawable) {
        ib_select_img_create_stuffkie.setImageDrawable(finalDrawable);
        ib_select_img_create_stuffkie.setAlpha(1f);

        // Solo ejecutar la animación en dispositivos con API 21+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Obtener el centro del ImageButton
            int centerX = ib_select_img_create_stuffkie.getWidth() / 2;
            int centerY = ib_select_img_create_stuffkie.getHeight() / 2;

            // Calcular el radio final (el círculo más grande que puede caber dentro del ImageButton)
            float finalRadius = Math.max(ib_select_img_create_stuffkie.getWidth(), ib_select_img_create_stuffkie.getHeight());

            // Crear el Animator para la revelación circular
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(
                    ib_select_img_create_stuffkie, centerX, centerY, 0, finalRadius);
            circularReveal.setDuration(500); // Duración de la animación en milisegundos

            // Iniciar la animación
            circularReveal.start();
        }
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
            bottomSheetProfilePhoto.show(getChildFragmentManager(),"BottomSheetProfilePhoto");
        } else if (v.getId()==R.id.fb_more_createStuffkie) {
            if (isAllFabsVisible) {
                fb_add_field_createStuffkie.setVisibility(View.INVISIBLE);
                isAllFabsVisible = false;
            } else {
                fb_add_field_createStuffkie.setVisibility(View.INVISIBLE);
                isAllFabsVisible = true;
            }
        }
    }
}