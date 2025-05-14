package com.example.buuktu.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buuktu.views.ProfileView;
import com.example.buuktu.R;
import com.example.buuktu.models.UserkieModel;
import com.example.buuktu.utils.DrawableUtils;
import com.example.buuktu.utils.EfectsUtils;
import com.example.buuktu.utils.NavigationUtils;
import com.example.buuktu.views.MainActivity;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class UserkieSearchAdapter extends RecyclerView.Adapter<UserkieSearchAdapter.ViewHolder> {

    private final ArrayList<UserkieModel> dataSet;
    private final FragmentManager fragmentManager;

    private final MainActivity context;
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView iv_userkie_private_search;
        final ImageButton iv_userkie_photo_search;
        final MaterialCardView cv_userkie_search;
        final TextView tv_userkie_name_search, tv_userkie_username_search;
        FrameLayout fl_userkie_photo_container;
        public ViewHolder(View view) {
            super(view);
            tv_userkie_username_search = view.findViewById(R.id.tv_userkie_username_search);
            tv_userkie_name_search =  view.findViewById(R.id.tv_userkie_name_search);
            iv_userkie_photo_search= view.findViewById(R.id.iv_userkie_photo_search);
            cv_userkie_search = view.findViewById(R.id.cv_userkie_search);
            iv_userkie_private_search = view.findViewById(R.id.iv_userkie_private_search);
            fl_userkie_photo_container = view.findViewById(R.id.fl_userkie_photo_container);
        }


        public TextView getTv_userkie_username_search() {
            return tv_userkie_username_search;
        }

        public TextView getTv_userkie_name_search() {
            return tv_userkie_name_search;
        }

        public MaterialCardView getCv_userkie_search() {
            return cv_userkie_search;
        }

        public ImageButton getIv_userkie_photo_search() {
            return iv_userkie_photo_search;
        }
        public ImageView getIv_userkie_private_search() {
            return iv_userkie_private_search;
        }

        public FrameLayout getFl_userkie_photo_container() {
            return fl_userkie_photo_container;
        }
    }
    //Constructor donde pasamos la lista de productos y el contexto
    public UserkieSearchAdapter(ArrayList<UserkieModel> dataSet, MainActivity ctx, FragmentManager fragmentManager) {
        this.dataSet = dataSet;
        this.context = ctx;
        this.fragmentManager = fragmentManager;
    }
    //Se llama cada vez que se hace scroll en la pantalla y los elementos desaparecen y aparecen
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        //Creamos la vista de cada item a partir de nuestro layout
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.userkies_list_layout_search, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserkieModel userkieModel = dataSet.get(position);
        holder.getFl_userkie_photo_container().setVisibility(View.INVISIBLE);
            holder.getTv_userkie_username_search().setText(userkieModel.getUsername());
            holder.getTv_userkie_name_search().setText(userkieModel.getName());
        if(!userkieModel.isProfile_private()){
            holder.getIv_userkie_private_search().setVisibility(View.INVISIBLE);
        }

        holder.getCv_userkie_search().setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("mode","other");
            bundle.putString("UID",userkieModel.getUID());
            NavigationUtils.goNewFragmentWithBundle(bundle,fragmentManager,new ProfileView());
        });
        if (userkieModel.isPhoto_default()) {
                String id_photo = userkieModel.getPhoto_id();
                int resId = context.getResources().getIdentifier(id_photo, "mipmap", context.getPackageName());

                if (resId != 0) {
                    Drawable drawable = ContextCompat.getDrawable(context, resId);
                 //   holder.getIv_userkie_photo_search().set
                    DrawableUtils.personalizarImagenCircleButton(context, DrawableUtils.drawableToBitmap(drawable), holder.getIv_userkie_photo_search(), R.color.brownBrown);
                    holder.getFl_userkie_photo_container().setVisibility(View.VISIBLE);
                    EfectsUtils.startCircularReveal(drawable, holder.getIv_userkie_photo_search());
                }
        } else {
            StorageReference userFolderRef = context.getFirebaseStorageUsers().getReference(userkieModel.getUID());

            userFolderRef.listAll().addOnSuccessListener(listResult -> {
                for (StorageReference item : listResult.getItems()) {
                    if (item.getName().startsWith("profile")) {
                        item.getBytes(5 * 1024 * 1024).addOnSuccessListener(bytes -> {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            // Pass the original bitmap to DrawableUtils
                            DrawableUtils.personalizarImagenCircleButton(context, bitmap, holder.getIv_userkie_photo_search(), R.color.brownBrown);
                            Drawable drawable = new BitmapDrawable(context.getResources(), bitmap);
                            EfectsUtils.startCircularReveal(drawable, holder.getIv_userkie_photo_search());
                            holder.getFl_userkie_photo_container().setVisibility(View.VISIBLE);

                        });
                        break;
                    }
                }
            });
        }
        }


    // Devolvemos el numero de items de nuestro arraylist, lo invoca automaticamente el layout manager
    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
