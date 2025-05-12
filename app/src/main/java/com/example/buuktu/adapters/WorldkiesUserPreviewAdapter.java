package com.example.buuktu.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buuktu.R;
import com.example.buuktu.WorldkieView;
import com.example.buuktu.models.WorldkieModel;
import com.example.buuktu.utils.DrawableUtils;
import com.example.buuktu.utils.EfectsUtils;
import com.example.buuktu.utils.NavigationUtils;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;

public class WorldkiesUserPreviewAdapter extends RecyclerView.Adapter<WorldkiesUserPreviewAdapter.ViewHolder>{


    private final ArrayList<WorldkieModel> dataSet;
    private final FragmentManager fragmentManager;

    private final Context context;


    public class ViewHolder extends RecyclerView.ViewHolder {
        String lastPhotoId="";
        private final ImageView iv_worldkie_preview_worldkie;
        private final ImageView iv_stuffkie_private_preview;
        private final TextView tv_worldkie_preview_worldkie;
        private final TextView tv_worldkie_preview_draft;
        final CardView cv_worldkie_preview;
        //private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance("gs://buuk-tu-worldkies");
        //private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

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

        public String getLastPhotoId() {
            return lastPhotoId;
        }

        public void setLastPhotoId(String lastPhotoId) {
            this.lastPhotoId = lastPhotoId;
        }
    }

    //Constructor donde pasamos la lista de productos y el contexto
    public WorldkiesUserPreviewAdapter(ArrayList<WorldkieModel> dataSet, Context ctx, FragmentManager fragmentManager) {
        this.dataSet = dataSet;
        this.context = ctx;
        this.fragmentManager = fragmentManager;
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
            holder.getIv_worldkie_preview_worldkie().setVisibility(View.INVISIBLE);
        }
        holder.getCv_worldkie_preview().setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("mode","other");
            bundle.putString("UID",UID);
            bundle.putString("UID_AUTHOR",UID_AUTHOR);
            NavigationUtils.goNewFragmentWithBundle(bundle,fragmentManager,new WorldkieView());
        });
        if (worldkieModel.isPhoto_default()) {
             String id_photo = worldkieModel.getId_photo();
                int resId = context.getResources().getIdentifier(id_photo, "mipmap", context.getPackageName());

                if (resId != 0 && (!holder.getLastPhotoId().equals(id_photo))) {
                    Drawable drawable = ContextCompat.getDrawable(context, resId);
                    holder.getIv_worldkie_preview_worldkie().setImageDrawable(drawable);
                    try {
                        DrawableUtils.personalizarImagenCuadradoButton(context,115/6,7,R.color.brownMaroon,drawable, holder.getIv_worldkie_preview_worldkie());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    holder.setLastPhotoId(id_photo);
                    holder.getIv_worldkie_preview_worldkie().setVisibility(View.VISIBLE);
                    EfectsUtils.startCircularReveal(drawable,holder.getIv_worldkie_preview_worldkie());
                }
        } else {
            StorageReference userFolderRef = FirebaseStorage.getInstance("gs://buuk-tu-worldkies").getReference(UID);

            userFolderRef.listAll().addOnSuccessListener(listResult -> {
                for (StorageReference item : listResult.getItems()) {
                    if (item.getName().startsWith("cover")) {
                        item.getDownloadUrl().addOnSuccessListener(uri -> {
                            try {
                                DrawableUtils.personalizarImagenCuadradoButton(context,100/6,7, R.color.greenWhatever,uri,holder.getIv_worldkie_preview_worldkie(),R.mipmap.photoprofileone);
                                holder.getIv_worldkie_preview_worldkie().setVisibility(View.VISIBLE);
                                EfectsUtils.startCircularReveal(context,uri,holder.getIv_worldkie_preview_worldkie());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
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