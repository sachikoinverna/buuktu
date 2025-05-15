package com.example.buuktu.adapters;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.buuktu.R;
import com.example.buuktu.dialogs.DeleteGeneralDialog;
import com.example.buuktu.models.WorldkieModel;
import com.example.buuktu.utils.DrawableUtils;
import com.example.buuktu.utils.EfectsUtils;
import com.example.buuktu.utils.NavigationUtils;
import com.example.buuktu.views.CreateEditWorldkie;
import com.example.buuktu.views.MainActivity;
import com.example.buuktu.views.WorldkieMenu;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class WorldkieAdapter extends RecyclerView.Adapter<WorldkieAdapter.ViewHolder> {
    private final ArrayList<WorldkieModel> dataSet;
    private final FragmentManager fragmentManager;
    private final MainActivity context;

    public WorldkieAdapter(ArrayList<WorldkieModel> dataSet, MainActivity ctx, FragmentManager fragmentManager) {
        this.dataSet = dataSet;
        this.context = ctx;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.worldkie_list_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
        Glide.with(context).clear(holder.getIv_photo_wordlkie()); // Limpia cualquier carga anterior de Glide
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WorldkieModel worldkieModel = dataSet.get(position);
        holder.getIv_photo_wordlkie().setVisibility(View.INVISIBLE);
        holder.getTv_name_wordkie().setText(worldkieModel.getName());
        Bundle bundle = new Bundle();
        bundle.putString("worldkie_id", worldkieModel.getUID());
        holder.getCard_view_worldkie_list_layout().setOnClickListener(v -> NavigationUtils.goNewFragmentWithBundle(bundle, fragmentManager, new WorldkieMenu()));
        holder.getCard_view_worldkie_list_layout().setOnLongClickListener(v -> {
            View popupView = LayoutInflater.from(context).inflate(R.layout.menu_popup, null);
            PopupWindow popupWindow = new PopupWindow(popupView,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    true);

            popupWindow.showAsDropDown(holder.getCard_view_worldkie_list_layout(), 0, -50);

            popupView.findViewById(R.id.bt_edit_item).setOnClickListener(view -> {
                NavigationUtils.goNewFragmentWithBundle(bundle, fragmentManager, new CreateEditWorldkie());
                popupWindow.dismiss();
            });

            popupView.findViewById(R.id.bt_del_item).setOnClickListener(view2 -> {
                DeleteGeneralDialog deleteGeneralDialog = new DeleteGeneralDialog(context, "worldkie", worldkieModel.getUID());
                deleteGeneralDialog.show();
                popupWindow.dismiss();
            });
            return true;
        });

        if (worldkieModel.isPhoto_default()) {
            int resId = context.getResources().getIdentifier(worldkieModel.getId_photo(), "mipmap", context.getPackageName());

            if (resId != 0) {
                Drawable drawable = ContextCompat.getDrawable(context, resId);
                holder.getIv_photo_wordlkie().setImageDrawable(drawable);
                    DrawableUtils.personalizarImagenCuadradoButton(context, 115 / 6, 7, R.color.white, drawable, holder.getIv_photo_wordlkie());
                holder.getIv_photo_wordlkie().setVisibility(View.VISIBLE);

                EfectsUtils.startCircularReveal(drawable, holder.getIv_photo_wordlkie());

            }
            holder.getIv_photo_wordlkie().setVisibility(View.VISIBLE);
        } else {
            context.getFirebaseStorageWorldkies().getReference(worldkieModel.getUID()).listAll().addOnSuccessListener(listResult -> {
                for (StorageReference item : listResult.getItems()) {
                    if (item.getName().startsWith("cover")) {
                        item.getDownloadUrl().addOnSuccessListener(uri -> {
                            DrawableUtils.personalizarImagenCuadradoImageView(context, 150 / 6, 7, R.color.white, uri, holder.getIv_photo_wordlkie());
                            holder.getIv_photo_wordlkie().setVisibility(View.VISIBLE);
                            EfectsUtils.startCircularReveal(holder.getIv_photo_wordlkie().getDrawable(), holder.getIv_photo_wordlkie());

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

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_name_wordlkie;
        private final ImageView iv_photo_wordlkie;
        MaterialCardView card_view_worldkie_list_layout;
        public ViewHolder(View view) {
            super(view);
            tv_name_wordlkie = view.findViewById(R.id.tv_name_setting_profile);
            iv_photo_wordlkie = view.findViewById(R.id.iv_photo_setting_profile);

            card_view_worldkie_list_layout = view.findViewById(R.id.card_view_worldkie_list_layout);
        }

        public MaterialCardView getCard_view_worldkie_list_layout() {
            return card_view_worldkie_list_layout;
        }


        public TextView getTv_name_wordkie() {
            return tv_name_wordlkie;
        }

        public ImageView getIv_photo_wordlkie() {
            return iv_photo_wordlkie;
        }


    }

}
