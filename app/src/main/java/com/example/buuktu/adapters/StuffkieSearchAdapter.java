package com.example.buuktu.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buuktu.R;
import com.example.buuktu.models.StuffkieModel;
import com.example.buuktu.utils.DrawableUtils;
import com.example.buuktu.utils.EfectsUtils;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;

public class StuffkieSearchAdapter extends RecyclerView.Adapter<StuffkieSearchAdapter.ViewHolder>{

    private ArrayList<StuffkieModel> dataSet;
    private FragmentManager fragmentManager;

    private Context context;
    private Fragment menuWorldkie;
    public class ViewHolder extends RecyclerView.ViewHolder {
        String lastPhotoId="",lastName="";
        private ImageView iv_stuffkie_photo_search,iv_stuffkie_private_search;
        MaterialCardView cv_stuffkie_search;
        TextView tv_stuffkie_name_search, tv_stuffkie_username_search;
        private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance("gs://buuk-tu-worldkies");
        private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        public ViewHolder(View view) {
            super(view);
            tv_stuffkie_username_search = view.findViewById(R.id.tv_stuffkie_username_search);
            tv_stuffkie_name_search =  view.findViewById(R.id.tv_stuffkie_name_search);
            iv_stuffkie_photo_search= view.findViewById(R.id.iv_stuffkie_photo_search);
            cv_stuffkie_search = view.findViewById(R.id.cv_stuffkie_search);
            iv_stuffkie_private_search = view.findViewById(R.id.iv_stuffkie_private_search);
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

        public FirebaseStorage getFirebaseStorage() {
            return firebaseStorage;
        }
        public FirebaseFirestore getDb() {
            return firestore;
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
    //Constructor donde pasamos la lista de productos y el contexto
    public StuffkieSearchAdapter(ArrayList<StuffkieModel> dataSet, Context ctx, FragmentManager fragmentManager) {
        this.dataSet = dataSet;
        this.context = ctx;
        this.fragmentManager = fragmentManager;
    }
    //Se llama cada vez que se hace scroll en la pantalla y los elementos desaparecen y aparecen
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        //Creamos la vista de cada item a partir de nuestro layout
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.stuffkies_list_layout_search, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getIv_stuffkie_photo_search().setVisibility(View.INVISIBLE);
       // holder.getTv_stuffkie_username_search().setText(dataSet.get(holder.getAdapterPosition()).get());
        String name =dataSet.get(holder.getAdapterPosition()).getName();
        if(!holder.getLastName().equals(name)) {
            holder.getTv_stuffkie_name_search().setText(name);
            holder.setLastName(name);
        }
        if(!dataSet.get(holder.getAdapterPosition()).isStuffkie_private()){
            holder.getIv_stuffkie_private_search().setVisibility(View.INVISIBLE);
        }

        holder.getCv_stuffkie_search().setOnClickListener(v -> {

        });
        ;

        if (dataSet.get(holder.getAdapterPosition()).isPhoto_default()) {
            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            firebaseFirestore.collection("Stuffkies").document(dataSet.get(holder.getAdapterPosition()).getUID()).addSnapshotListener((queryDocumentSnapshot, e) -> {
                String id_photo = queryDocumentSnapshot.getString("photo_id");
                int resId = context.getResources().getIdentifier(id_photo, "mipmap", context.getPackageName());

                if (resId != 0 && (holder.getLastPhotoId().equals(id_photo))) {
                    Drawable drawable = ContextCompat.getDrawable(context, resId);
                    holder.getIv_stuffkie_photo_search().setImageDrawable(drawable);
                    try {
                        DrawableUtils.personalizarImagenCuadradoButton(context,115/6,7,R.color.brownMaroon,drawable, holder.getIv_stuffkie_photo_search());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    holder.setLastPhotoId(id_photo);
                    holder.getIv_stuffkie_photo_search().setVisibility(View.VISIBLE);
                    EfectsUtils.startCircularReveal(drawable,holder.getIv_stuffkie_photo_search());
                }
            });
        } else {
            StorageReference userFolderRef = FirebaseStorage.getInstance("gs://buuk-tu-stuffkies").getReference(dataSet.get(holder.getAdapterPosition()).getUID());

            userFolderRef.listAll().addOnSuccessListener(listResult -> {
                for (StorageReference item : listResult.getItems()) {
                    if (item.getName().startsWith("cover")) {
                        item.getDownloadUrl().addOnSuccessListener(uri -> {
                            try {
                                DrawableUtils.personalizarImagenCuadradoButton(context,115/7,7, R.color.greenWhatever,uri,holder.getIv_stuffkie_photo_search(),R.mipmap.photostuffkieone);
                                holder.getIv_stuffkie_photo_search().setVisibility(View.VISIBLE);
                                EfectsUtils.startCircularReveal(context,uri,holder.getIv_stuffkie_photo_search());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    }
                }
                ;
            });
        }
    }


    // Devolvemos el numero de items de nuestro arraylist, lo invoca automaticamente el layout manager
    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
