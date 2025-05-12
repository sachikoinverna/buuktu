package com.example.buuktu.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.example.buuktu.views.WorldkieMenu;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;

public class WorldkieAdapter extends RecyclerView.Adapter<WorldkieAdapter.ViewHolder> {
    private final ArrayList<WorldkieModel> dataSet;
    private final FragmentManager fragmentManager;
    private final Context context;

    //Constructor donde pasamos la lista de productos y el contexto
    public WorldkieAdapter(ArrayList<WorldkieModel> dataSet, Context ctx, FragmentManager fragmentManager) {
        this.dataSet = dataSet;
        this.context = ctx;
        this.fragmentManager = fragmentManager;
    }

    //Se llama cada vez que se hace scroll en la pantalla y los elementos desaparecen y aparecen
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        //Creamos la vista de cada item a partir de nuestro layout
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.worldkie_list_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
        Glide.with(context).clear(holder.getIv_photo_wordlkie()); // Limpia cualquier carga anterior de Glide
    }

    @Override
    public void onBindViewHolder(@NonNull WorldkieAdapter.ViewHolder holder, int position) {
        WorldkieModel worldkieModel = dataSet.get(position);
        holder.getIv_photo_wordlkie().setVisibility(View.INVISIBLE);
        holder.getTv_name_wordkie().setText(worldkieModel.getName());
        Bundle bundle = new Bundle();
        bundle.putString("worldkie_id", worldkieModel.getUID());
        holder.getIb_enterToAWorldkie().setOnClickListener(v -> {
            NavigationUtils.goNewFragmentWithBundle(bundle, fragmentManager, new WorldkieMenu());

        });
        holder.getIb_editAWorldkie().setOnClickListener(v -> {

            NavigationUtils.goNewFragmentWithBundle(bundle, fragmentManager, new CreateEditWorldkie());

        });
        holder.getIb_deleteAWorldkie().setOnClickListener(v -> {
            DeleteGeneralDialog deleteGeneralDialog = new DeleteGeneralDialog(context, "worldkie", worldkieModel.getUID());
            deleteGeneralDialog.show();
        });
        if (worldkieModel.isPhoto_default()) {
            String id_photo = worldkieModel.getId_photo();
            int resId = context.getResources().getIdentifier(id_photo, "mipmap", context.getPackageName());

            if (resId != 0 && (!holder.getLastPhotoId().equals(id_photo))) {
                Drawable drawable = ContextCompat.getDrawable(context, resId);
                holder.getIv_photo_wordlkie().setImageDrawable(drawable);
                try {
                    DrawableUtils.personalizarImagenCuadradoButton(context, 115 / 6, 7, R.color.brownMaroon, drawable, holder.getIv_photo_wordlkie());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                holder.setLastPhotoId(id_photo);
                holder.getIv_photo_wordlkie().setVisibility(View.VISIBLE);

                EfectsUtils.startCircularReveal(drawable, holder.getIv_photo_wordlkie());

            }
            holder.getIv_photo_wordlkie().setVisibility(View.VISIBLE);
        } else {
            StorageReference userFolderRef = FirebaseStorage.getInstance("gs://buuk-tu-worldkies").getReference(dataSet.get(holder.getAdapterPosition()).getUID());

            userFolderRef.listAll().addOnSuccessListener(listResult -> {
                for (StorageReference item : listResult.getItems()) {
                    if (item.getName().startsWith("cover")) {
                        item.getDownloadUrl().addOnSuccessListener(uri -> {
                            DrawableUtils.personalizarImagenCuadradoImageView(context, 150 / 6, 7, R.color.brownMaroon, uri, holder.getIv_photo_wordlkie());
                            holder.getIv_photo_wordlkie().setVisibility(View.VISIBLE);
                            EfectsUtils.startCircularReveal(context, uri, holder.getIv_photo_wordlkie());
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_name_wordlkie;
        private final ImageView iv_photo_wordlkie;
        private final ImageButton ib_enterToAWorldkie;
        private final ImageButton ib_editAWorldkie;
        private final ImageButton ib_deleteAWorldkie;
        private final FirebaseStorage firebaseStorage = FirebaseStorage.getInstance("gs://buuk-tu-worldkies");
        private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference collectionWorldkies = firestore.collection("Worldkies");
        private String lastPhotoId = "", lastName = "";

        public ViewHolder(View view) {
            super(view);
            tv_name_wordlkie = view.findViewById(R.id.tv_name_setting_profile);
            iv_photo_wordlkie = view.findViewById(R.id.iv_photo_setting_profile);
            ib_enterToAWorldkie = view.findViewById(R.id.ib_enterToAWorldkie);
            ib_editAWorldkie = view.findViewById(R.id.ib_editAWorldkie);
            ib_deleteAWorldkie = view.findViewById(R.id.ib_deleteAWorldkie);
        }

        public FirebaseStorage getFirebaseStorage() {
            return firebaseStorage;
        }

        public FirebaseFirestore getDb() {
            return firestore;
        }

        public ImageButton getIb_enterToAWorldkie() {
            return ib_enterToAWorldkie;
        }

        public ImageButton getIb_editAWorldkie() {
            return ib_editAWorldkie;
        }

        public ImageButton getIb_deleteAWorldkie() {
            return ib_deleteAWorldkie;
        }

        public CollectionReference getCollectionWorldkies() {
            return collectionWorldkies;
        }

        public void setCollectionWorldkies(CollectionReference collectionWorldkies) {
            this.collectionWorldkies = collectionWorldkies;
        }

        //getters
        public TextView getTv_name_wordkie() {
            return tv_name_wordlkie;
        }

        public ImageView getIv_photo_wordlkie() {
            return iv_photo_wordlkie;
        }

        public String getLastPhotoId() {
            return lastPhotoId;
        }

        public void setLastPhotoId(String lastPhotoId) {
            this.lastPhotoId = lastPhotoId;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }
    }

}
