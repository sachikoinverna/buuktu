package com.example.buuktu.adapters;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buuktu.R;
import com.example.buuktu.views.StuffkieView;
import com.example.buuktu.models.StuffkieModel;
import com.example.buuktu.utils.DrawableUtils;
import com.example.buuktu.utils.EfectsUtils;
import com.example.buuktu.utils.NavigationUtils;
import com.example.buuktu.views.MainActivity;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class StuffkieSearchAdapter extends RecyclerView.Adapter<StuffkieSearchAdapter.ViewHolder>{

    private final ArrayList<StuffkieModel> dataSet;
    private final FragmentManager fragmentManager;
private final String mode;
    private final MainActivity context;
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView iv_stuffkie_photo_search;
        private final ImageView iv_stuffkie_private_search;
        final MaterialCardView cv_stuffkie_search;
        final TextView tv_stuffkie_name_search;
        final TextView tv_stuffkie_username_search;

        public ViewHolder(View view) {
            super(view);
            tv_stuffkie_username_search = view.findViewById(R.id.tv_stuffkie_username_search);
            tv_stuffkie_name_search =  view.findViewById(R.id.tv_stuffkie_name_search);
            iv_stuffkie_photo_search= view.findViewById(R.id.iv_stuffkie_photo_search);
            cv_stuffkie_search = view.findViewById(R.id.cv_stuffkie_search);
            iv_stuffkie_private_search = view.findViewById(R.id.iv_stuffkie_private_search);
        }

        public TextView getTv_stuffkie_username_search() {
            return tv_stuffkie_username_search;
        }

        public TextView getTv_stuffkie_name_search() {
            return tv_stuffkie_name_search;
        }

        public MaterialCardView getCv_stuffkie_search() {
            return cv_stuffkie_search;
        }

        public ImageView getIv_stuffkie_photo_search() {
            return iv_stuffkie_photo_search;
        }
        public ImageView getIv_stuffkie_private_search() {
            return iv_stuffkie_private_search;
        }
    }
    public StuffkieSearchAdapter(ArrayList<StuffkieModel> dataSet, MainActivity ctx, FragmentManager fragmentManager,String mode) {
        this.dataSet = dataSet;
        this.context = ctx;
        this.fragmentManager = fragmentManager;
        this.mode=mode;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.stuffkies_list_layout_search, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StuffkieModel stuffkieModel = dataSet.get(position);
        holder.getIv_stuffkie_photo_search().setVisibility(View.INVISIBLE);
        context.getCollectionUsers().document(stuffkieModel.getAUTHOR_UID()).addSnapshotListener((documentSnapshot, e) -> {
            if (e != null) {
                return;
            }
            if (documentSnapshot != null) {
                holder.getTv_stuffkie_username_search().setText(documentSnapshot.getString("username"));
            }
        });
            holder.getTv_stuffkie_name_search().setText(stuffkieModel.getName());
        if(!stuffkieModel.isStuffkie_private()){
            holder.getIv_stuffkie_private_search().setVisibility(View.INVISIBLE);
        }

        holder.getCv_stuffkie_search().setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("mode",mode);
            bundle.putString("UID",stuffkieModel.getUID());
            bundle.putString("UID_AUTHOR", stuffkieModel.getAUTHOR_UID());
            bundle.putString("UID_WORLDKIE",stuffkieModel.getWORDLKIE_UID());
            NavigationUtils.goNewFragmentWithBundle(bundle,fragmentManager,new StuffkieView());

        });

        if (stuffkieModel.isPhoto_default()) {
                String id_photo = stuffkieModel.getPhoto_id();
                int resId = context.getResources().getIdentifier(id_photo, "mipmap", context.getPackageName());

                if (resId != 0) {
                    Drawable drawable = ContextCompat.getDrawable(context, resId);
                    holder.getIv_stuffkie_photo_search().setImageDrawable(drawable);
                        DrawableUtils.personalizarImagenCuadradoButton(context,115/6,7,R.color.brownMaroon,drawable, holder.getIv_stuffkie_photo_search());
                    holder.getIv_stuffkie_photo_search().setVisibility(View.VISIBLE);
                    EfectsUtils.startCircularReveal(drawable,holder.getIv_stuffkie_photo_search());
                }
        } else {
            context.getFirebaseStorageStuffkies().getReference(stuffkieModel.getUID()).listAll().addOnSuccessListener(listResult -> {
                for (StorageReference item : listResult.getItems()) {
                    if (item.getName().startsWith("cover")) {
                        item.getDownloadUrl().addOnSuccessListener(uri -> {
                                DrawableUtils.personalizarImagenCuadradoButton(context,115/7,7, R.color.greenWhatever,uri,holder.getIv_stuffkie_photo_search());
                                holder.getIv_stuffkie_photo_search().setVisibility(View.VISIBLE);
                                EfectsUtils.startCircularReveal(context,uri,holder.getIv_stuffkie_photo_search());
                        });
                    }
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
