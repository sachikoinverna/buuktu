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
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buuktu.R;
import com.example.buuktu.views.WorldkieView;
import com.example.buuktu.dialogs.DeleteGeneralDialog;
import com.example.buuktu.models.WorldkieModel;
import com.example.buuktu.utils.DrawableUtils;
import com.example.buuktu.utils.EfectsUtils;
import com.example.buuktu.utils.NavigationUtils;
import com.example.buuktu.views.CreateEditWorldkie;
import com.example.buuktu.views.MainActivity;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class WorldkiesUserPreviewAdapter extends RecyclerView.Adapter<WorldkiesUserPreviewAdapter.ViewHolder>{


    private final ArrayList<WorldkieModel> dataSet;
    private final FragmentManager fragmentManager;

    private final MainActivity context;
    String mode;


    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView iv_worldkie_preview_worldkie;
        private final ImageView iv_stuffkie_private_preview;
        private final TextView tv_worldkie_preview_worldkie;
        private final TextView tv_worldkie_preview_draft;
        final CardView cv_worldkie_preview;
        public ViewHolder(View view) {
            super(view);
            iv_worldkie_preview_worldkie =  view.findViewById(R.id.iv_worldkie_preview_worldkie);
            tv_worldkie_preview_worldkie= view.findViewById(R.id.tv_worldkie_preview_worldkie);
            cv_worldkie_preview = view.findViewById(R.id.cv_worldkie_preview);
            iv_stuffkie_private_preview = view.findViewById(R.id.iv_stuffkie_private_preview);
            tv_worldkie_preview_draft = view.findViewById(R.id.tv_worldkie_preview_draft);
        }


        public ImageView getIv_worldkie_preview_worldkie() {
            return iv_worldkie_preview_worldkie;
        }

        public TextView getTv_worldkie_preview_worldkie() {
            return tv_worldkie_preview_worldkie;
        }

        public CardView getCv_worldkie_preview() {
            return cv_worldkie_preview;
        }

        public TextView getTv_worldkie_preview_draft() {
            return tv_worldkie_preview_draft;
        }

        public ImageView getIv_stuffkie_private_preview() {
            return iv_stuffkie_private_preview;
        }

    }

    //Constructor donde pasamos la lista de productos y el contexto
    public WorldkiesUserPreviewAdapter(ArrayList<WorldkieModel> dataSet, MainActivity ctx, FragmentManager fragmentManager, String mode) {
        this.dataSet = dataSet;
        this.context = ctx;
        this.fragmentManager = fragmentManager;
        this.mode=mode;
    }


    
    //Se llama cada vez que se hace scroll en la pantalla y los elementos desaparecen y aparecen
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        //Creamos la vista de cada item a partir de nuestro layout
           View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.worldkie_list_layout_preview, viewGroup, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull WorldkiesUserPreviewAdapter.ViewHolder holder, int position) {
        WorldkieModel worldkieModel = dataSet.get(position);
        String UID = worldkieModel.getUID();
        String UID_AUTHOR = worldkieModel.getUID_AUTHOR();
        holder.getIv_stuffkie_private_preview().setVisibility(View.INVISIBLE);
        holder.getTv_worldkie_preview_worldkie().setText(worldkieModel.getName());
        if(!worldkieModel.isWorldkie_private()) {
            holder.getIv_stuffkie_private_preview().setVisibility(View.INVISIBLE);
        }
        if(!worldkieModel.isDraft()) {
            holder.getTv_worldkie_preview_draft().setVisibility(View.INVISIBLE);
        }

        holder.getCv_worldkie_preview().setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("mode",mode);
            bundle.putString("UID",UID);
            bundle.putString("UID_AUTHOR",UID_AUTHOR);
            NavigationUtils.goNewFragmentWithBundle(bundle,fragmentManager,new WorldkieView());
        });
        if(mode.equals("self")) {

            holder.getCv_worldkie_preview().setOnLongClickListener(v -> {
                View popupView = LayoutInflater.from(context).inflate(R.layout.menu_popup, null);
                PopupWindow popupWindow = new PopupWindow(popupView,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        true);

                popupWindow.showAsDropDown(holder.getCv_worldkie_preview(), 0, -50);

                Bundle bundle = new Bundle();
                bundle.putString("worldkie_id", worldkieModel.getUID());
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
        }
        if (worldkieModel.isPhoto_default()) {
                int resId = context.getResources().getIdentifier(worldkieModel.getId_photo(), "mipmap", context.getPackageName());

                if (resId != 0) {
                    Drawable drawable = ContextCompat.getDrawable(context, resId);
                    holder.getIv_worldkie_preview_worldkie().setImageDrawable(drawable);
                        DrawableUtils.personalizarImagenCuadradoButton(context,115/6,7,R.color.brownMaroon,drawable, holder.getIv_worldkie_preview_worldkie());

                    holder.getIv_worldkie_preview_worldkie().setVisibility(View.VISIBLE);
                    EfectsUtils.startCircularReveal(drawable,holder.getIv_worldkie_preview_worldkie());
                }
        } else {
            context.getFirebaseStorageWorldkies().getReference(UID).listAll().addOnSuccessListener(listResult -> {
                for (StorageReference item : listResult.getItems()) {
                    if (item.getName().startsWith("cover")) {
                        item.getDownloadUrl().addOnSuccessListener(uri -> {
                                DrawableUtils.personalizarImagenCuadradoButton(context,100/6,7, R.color.greenWhatever,uri,holder.getIv_worldkie_preview_worldkie());
                                holder.getIv_worldkie_preview_worldkie().setVisibility(View.VISIBLE);
                            EfectsUtils.startCircularReveal(holder.getIv_worldkie_preview_worldkie().getDrawable(), holder.getIv_worldkie_preview_worldkie());
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