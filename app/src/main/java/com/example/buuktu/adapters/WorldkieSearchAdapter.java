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

import com.example.buuktu.R;
import com.example.buuktu.views.WorldkieView;
import com.example.buuktu.models.WorldkieModel;
import com.example.buuktu.utils.DrawableUtils;
import com.example.buuktu.utils.EfectsUtils;
import com.example.buuktu.utils.NavigationUtils;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class WorldkieSearchAdapter extends RecyclerView.Adapter<WorldkieSearchAdapter.ViewHolder>{
    private final ArrayList<WorldkieModel> dataSet;
    private final FragmentManager fragmentManager;

    private final Context context;
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView iv_worldkie_photo_search;
        private final ImageView iv_worldkie_private_search;
        final ImageButton ib_show_more_details_worldkie_search;
        final MaterialCardView cv_worldkie_search;
        final TextView tv_worldkie_name_search;
        final TextView tv_worldkie_username_search;
        final TextView tv_date_creation_search_worldkie_title;
        final TextView tv_date_creation_search_worldkie;
        final TextView tv_date_last_update_search_worldkie_title;
        final TextView tv_date_last_update_search_worldkie;
        private final FirebaseStorage firebaseStorageWorldkies= FirebaseStorage.getInstance("gs://buuk-tu-worldkies");
        private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        private boolean moreDetailsShowed;
        final CollectionReference collectionUserkies;
        private final FirebaseFirestore db;
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

        public FirebaseStorage getFirebaseStorageWorldkies() {
            return firebaseStorageWorldkies;
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
        holder.getTv_worldkie_name_search().setText(worldkieModel.getName());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        holder.getTv_date_last_update_search_worldkie().setText(simpleDateFormat.format(worldkieModel.getLast_update().toDate()));
        holder.getTv_date_creation_search_worldkie().setText(simpleDateFormat.format(worldkieModel.getCreation_date().toDate()));
        holder.getIv_worldkie_private_search().setVisibility(worldkieModel.isWorldkie_private()?View.VISIBLE:View.INVISIBLE);

        holder.getCv_worldkie_search().setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("mode","other");
            bundle.putString("UID",UID);
            bundle.putString("UID_AUTHOR",UID_AUTHOR);
            NavigationUtils.goNewFragmentWithBundle(bundle,fragmentManager,new WorldkieView());
        });
        holder.getIb_show_more_details_worldkie_search().setOnClickListener(v -> {
                holder.getTv_date_last_update_search_worldkie_title().setVisibility(holder.moreDetailsShowed? View.GONE:View.VISIBLE);
                holder.getTv_date_last_update_search_worldkie().setVisibility(holder.moreDetailsShowed? View.GONE:View.VISIBLE);
                holder.getTv_date_creation_search_worldkie_title().setVisibility(holder.moreDetailsShowed? View.GONE:View.VISIBLE);
                holder.getTv_date_creation_search_worldkie().setVisibility(holder.moreDetailsShowed? View.GONE:View.VISIBLE);
                holder.getIb_show_more_details_worldkie_search().setImageResource(holder.moreDetailsShowed? R.drawable.twotone_arrow_drop_down_circle_24:R.drawable.twotone_keyboard_arrow_up_24);
            holder.moreDetailsShowed = !holder.moreDetailsShowed;
        });
        if (worldkieModel.isPhoto_default()) {
                String id_photo = worldkieModel.getId_photo();
                int resId = context.getResources().getIdentifier(id_photo, "mipmap", context.getPackageName());

                if (resId != 0) {
                    Drawable drawable = ContextCompat.getDrawable(context, resId);
                    holder.getIv_worldkie_photo_search().setImageDrawable(drawable);
                    try {
                        DrawableUtils.personalizarImagenCuadradoButton(context,115/6,7,R.color.brownMaroon,drawable, holder.getIv_worldkie_photo_search());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    holder.getIv_worldkie_photo_search().setVisibility(View.VISIBLE);
                    EfectsUtils.startCircularReveal(drawable,holder.getIv_worldkie_photo_search());
                }
        } else {
            holder.firebaseStorageWorldkies.getReference(UID).listAll().addOnSuccessListener(listResult -> {
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
            });
        }
    }



    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
