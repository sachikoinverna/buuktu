package com.example.buuktu.adapters;

import static android.widget.Toast.LENGTH_LONG;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buuktu.ProfileView;
import com.example.buuktu.R;
import com.example.buuktu.WorldkieView;
import com.example.buuktu.models.UserkieModel;
import com.example.buuktu.models.WorldkieModel;
import com.example.buuktu.utils.DrawableUtils;
import com.example.buuktu.utils.EfectsUtils;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class WorldkieSearchAdapter extends RecyclerView.Adapter<WorldkieSearchAdapter.ViewHolder> implements View.OnClickListener{
    @Override
    public void onClick(View v) {

    }
    private ArrayList<WorldkieModel> dataSet;
    private FragmentManager fragmentManager;

    private Context context;
    private Fragment menuWorldkie;
    public class ViewHolder extends RecyclerView.ViewHolder {
        String lastPhotoId="",lastName="";
        private ImageView iv_worldkie_photo_search,iv_worldkie_private_search;
        ImageButton ib_show_more_details_worldkie_search;
        MaterialCardView cv_worldkie_search;
        TextView tv_worldkie_name_search, tv_worldkie_username_search,tv_date_creation_search_worldkie_title,tv_date_creation_search_worldkie,tv_date_last_update_search_worldkie_title,tv_date_last_update_search_worldkie;
        private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance("gs://buuk-tu-worldkies");
        private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        private boolean moreDetailsShowed;
        CollectionReference collectionUserkies;
        private FirebaseFirestore db;
        public ViewHolder(View view) {
            super(view);
            ib_show_more_details_worldkie_search = view.findViewById(R.id.ib_show_more_details_worldkie_search);
            tv_worldkie_username_search = view.findViewById(R.id.tv_worldkie_username_search);
            tv_worldkie_name_search =  view.findViewById(R.id.tv_worldkie_name_search);
            tv_date_creation_search_worldkie_title = view.findViewById(R.id.tv_date_creation_search_worldkie_title);
            tv_date_creation_search_worldkie = view.findViewById(R.id.tv_date_creation_search_worldkie);
            tv_date_last_update_search_worldkie_title = view.findViewById(R.id.tv_date_last_update_search_worldkie_title);
            tv_date_last_update_search_worldkie = view.findViewById(R.id.tv_date_last_update_search_worldkie);
            iv_worldkie_photo_search= view.findViewById(R.id.iv_worldkie_photo_search);
            cv_worldkie_search = view.findViewById(R.id.cv_worldkie_search);
            iv_worldkie_private_search = view.findViewById(R.id.iv_worldkie_private_search);
            db = FirebaseFirestore.getInstance();
            collectionUserkies = db.collection("Users");

            moreDetailsShowed = false;
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

        public TextView getTv_worldkie_username_search() {
            return tv_worldkie_username_search;
        }

        public TextView getTv_worldkie_name_search() {
            return tv_worldkie_name_search;
        }

        public MaterialCardView getCv_worldkie_search() {
            return cv_worldkie_search;
        }

        public ImageView getIv_worldkie_photo_search() {
            return iv_worldkie_photo_search;
        }
        public ImageView getIv_worldkie_private_search() {
            return iv_worldkie_private_search;
        }

        public TextView getTv_date_last_update_search_worldkie() {
            return tv_date_last_update_search_worldkie;
        }

        public TextView getTv_date_last_update_search_worldkie_title() {
            return tv_date_last_update_search_worldkie_title;
        }

        public TextView getTv_date_creation_search_worldkie() {
            return tv_date_creation_search_worldkie;
        }

        public TextView getTv_date_creation_search_worldkie_title() {
            return tv_date_creation_search_worldkie_title;
        }

        public ImageButton getIb_show_more_details_worldkie_search() {
            return ib_show_more_details_worldkie_search;
        }

        public String getLastPhotoId() {
            return lastPhotoId;
        }

        public void setLastPhotoId(String lastPhotoId) {
            this.lastPhotoId = lastPhotoId;
        }

        public CollectionReference getCollectionUserkies() {
            return collectionUserkies;
        }
    }
    //Constructor donde pasamos la lista de productos y el contexto
    public WorldkieSearchAdapter(ArrayList<WorldkieModel> dataSet, Context ctx, FragmentManager fragmentManager) {
        this.dataSet = dataSet;
        this.context = ctx;
        this.fragmentManager = fragmentManager;
    }
    //Se llama cada vez que se hace scroll en la pantalla y los elementos desaparecen y aparecen
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        //Creamos la vista de cada item a partir de nuestro layout
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.worldkies_list_layout_search, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WorldkieModel worldkieModel = dataSet.get(position);
        String UID_AUTHOR = worldkieModel.getUID_AUTHOR();
        String UID = worldkieModel.getUID();
        holder.getIv_worldkie_photo_search().setVisibility(View.INVISIBLE);
        holder.getCollectionUserkies().document(UID_AUTHOR).addSnapshotListener((documentSnapshot, e) -> {
            if (e != null) {
                Log.e("Error", e.getMessage());
                Toast.makeText(context, "Error al escuchar cambios: " + e.getMessage(), LENGTH_LONG).show();
                return;
            }

            if (documentSnapshot != null) {
                holder.getTv_worldkie_username_search().setText(documentSnapshot.getString("username"));
            }
        });
        holder.getTv_date_last_update_search_worldkie_title().setVisibility(View.GONE);
        holder.getTv_date_last_update_search_worldkie().setVisibility(View.GONE);
        holder.getTv_date_creation_search_worldkie_title().setVisibility(View.GONE);
        holder.getTv_date_creation_search_worldkie().setVisibility(View.GONE);
        String name = dataSet.get(holder.getAdapterPosition()).getName();

        if (!holder.getLastName().equals(name)){
            holder.getTv_worldkie_name_search().setText(name);
            holder.setLastName(name);
    }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        holder.getTv_date_last_update_search_worldkie().setText(simpleDateFormat.format(worldkieModel.getLast_update().toDate()));
        holder.getTv_date_creation_search_worldkie().setText(simpleDateFormat.format(worldkieModel.getCreation_date().toDate()));

        if (worldkieModel.isWorldkie_private()) {
            holder.getIv_worldkie_private_search().setVisibility(View.VISIBLE);
        } else {
            holder.getIv_worldkie_private_search().setVisibility(View.INVISIBLE);
        }

        holder.getCv_worldkie_search().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WorldkieView worldkieView = new WorldkieView();
                Bundle bundle = new Bundle();
                bundle.putString("mode","other");
                bundle.putString("UID",UID);
                bundle.putString("UID_AUTHOR",UID_AUTHOR);
                worldkieView.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, worldkieView)
                        .addToBackStack(null)
                        .commit();
            }
        });
        holder.getIb_show_more_details_worldkie_search().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.moreDetailsShowed) {
                    holder.getTv_date_last_update_search_worldkie_title().setVisibility(View.GONE);
                    holder.getTv_date_last_update_search_worldkie().setVisibility(View.GONE);
                    holder.getTv_date_creation_search_worldkie_title().setVisibility(View.GONE);
                    holder.getTv_date_creation_search_worldkie().setVisibility(View.GONE);
                    holder.moreDetailsShowed = false;
                    holder.getIb_show_more_details_worldkie_search().setImageResource(R.drawable.twotone_arrow_drop_down_circle_24);
                } else {
                    holder.getTv_date_last_update_search_worldkie_title().setVisibility(View.VISIBLE);
                    holder.getTv_date_last_update_search_worldkie().setVisibility(View.VISIBLE);
                    holder.getTv_date_creation_search_worldkie_title().setVisibility(View.VISIBLE);
                    holder.getTv_date_creation_search_worldkie().setVisibility(View.VISIBLE);
                    holder.moreDetailsShowed = true;
                    holder.getIb_show_more_details_worldkie_search().setImageResource(R.drawable.twotone_keyboard_arrow_up_24);
                }
            }
        });
        ;
        if (worldkieModel.isPhoto_default()) {
            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            firebaseFirestore.collection("Worldkies").document(UID).addSnapshotListener((queryDocumentSnapshot, e) -> {
                String id_photo = queryDocumentSnapshot.getString("id_photo");
                int resId = context.getResources().getIdentifier(id_photo, "mipmap", context.getPackageName());

                if (resId != 0 && (!holder.getLastPhotoId().equals(id_photo))) {
                    Drawable drawable = ContextCompat.getDrawable(context, resId);
                    holder.getIv_worldkie_photo_search().setImageDrawable(drawable);
                    try {
                        DrawableUtils.personalizarImagenCuadradoButton(context,115/6,7,R.color.brownMaroon,drawable, holder.getIv_worldkie_photo_search());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    holder.setLastPhotoId(id_photo);
                    holder.getIv_worldkie_photo_search().setVisibility(View.VISIBLE);
                    EfectsUtils.startCircularReveal(drawable,holder.getIv_worldkie_photo_search());
                }
            });
        } else {
            StorageReference userFolderRef = FirebaseStorage.getInstance("gs://buuk-tu-worldkies").getReference(UID);

            userFolderRef.listAll().addOnSuccessListener(listResult -> {
                for (StorageReference item : listResult.getItems()) {
                    if (item.getName().startsWith("cover")) {
                        item.getDownloadUrl().addOnSuccessListener(uri -> {
                            try {
                                DrawableUtils.personalizarImagenCuadradoButton(context,115/7,7, R.color.greenWhatever,uri,holder.getIv_worldkie_photo_search(),R.mipmap.photoworldkieone);
                                holder.getIv_worldkie_photo_search().setVisibility(View.VISIBLE);
                                EfectsUtils.startCircularReveal(context,uri,holder.getIv_worldkie_photo_search());
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
