package com.example.buuktu.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buuktu.R;
import com.example.buuktu.models.StuffkieModel;
import com.example.buuktu.utils.DrawableUtils;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;

public class StuffkiesUserPreviewAdapter extends RecyclerView.Adapter<StuffkiesUserPreviewAdapter.ViewHolder> implements View.OnClickListener {

    @Override
    public void onClick(View v) {

    }
    private ArrayList<StuffkieModel> dataSet;
    private ItemClickListener clicListener;

    private Context context;

    public interface ItemClickListener {
        public void onClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_stuffkie_preview_worldkie,iv_stuffkie_private_preview;
        private TextView tv_stuffkie_preview_worldkie,tv_stuffkie_preview_draft;
        CardView cv_stuffkie_preview;
        //private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance("gs://buuk-tu-worldkies");
        //private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        public ViewHolder(View view) {
            super(view);
            iv_stuffkie_preview_worldkie =  view.findViewById(R.id.iv_stuffkie_preview_worldkie);
            tv_stuffkie_preview_worldkie= view.findViewById(R.id.tv_stuffkie_preview_worldkie);
            iv_stuffkie_private_preview = view.findViewById(R.id.iv_stuffkie_private_preview);
            tv_stuffkie_preview_draft = view.findViewById(R.id.tv_stuffkie_preview_draft);
            cv_stuffkie_preview = view.findViewById(R.id.cv_stuffkie_preview);
        }

        public ImageView getIv_stuffkie_private_preview() {
            return iv_stuffkie_private_preview;
        }

        public TextView getTv_stuffkie_preview_draft() {
            return tv_stuffkie_preview_draft;
        }

        public ImageView getIv_stuffkie_preview_worldkie() {
            return iv_stuffkie_preview_worldkie;
        }

        public TextView getTv_stuffkie_preview_worldkie() {
            return tv_stuffkie_preview_worldkie;
        }

        public CardView getCv_stuffkie_preview() {
            return cv_stuffkie_preview;
        }
    }

    //Constructor donde pasamos la lista de productos y el contexto
    public StuffkiesUserPreviewAdapter(ArrayList<StuffkieModel> dataSet, Context ctx) {
        this.dataSet = dataSet;
        this.context = ctx;
    }
    public void setOnClickListener(ItemClickListener clicListener){
        this.clicListener = clicListener;
    }

    
    //Se llama cada vez que se hace scroll en la pantalla y los elementos desaparecen y aparecen
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        //Creamos la vista de cada item a partir de nuestro layout
           View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.stuffkie_list_layout_preview, viewGroup, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull StuffkiesUserPreviewAdapter.ViewHolder holder, int position) {
        holder.getTv_stuffkie_preview_worldkie().setText(dataSet.get(holder.getAdapterPosition()).getName());
        if(!dataSet.get(holder.getAdapterPosition()).isBorrador()){
            holder.getTv_stuffkie_preview_draft().setVisibility(View.INVISIBLE);
        }
        if(!dataSet.get(holder.getAdapterPosition()).isStuffkie_private()){
            holder.getIv_stuffkie_private_preview().setVisibility(View.GONE);
        }
        if (dataSet.get(holder.getAdapterPosition()).isPhoto_default()) {
            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            firebaseFirestore.collection("Stuffkies").document(dataSet.get(holder.getAdapterPosition()).getUID()).addSnapshotListener((queryDocumentSnapshot, e) -> {
                       /* if (e != null) {
                            Log.e("Error", e.getMessage());
                            Toast.makeText(getContext(), "Error al escuchar cambios: " + e.getMessage(), LENGTH_LONG).show();
                            return;
                        }*/
                //boolean photo_default = queryDocumentSnapshot.getBoolean("photo_default");
                String id_photo = queryDocumentSnapshot.getString("photo_id");
                int resId = context.getResources().getIdentifier(id_photo, "mipmap", context.getPackageName());

                if (resId != 0) {
                    Drawable drawable = ContextCompat.getDrawable(context, resId);
                    holder.getIv_stuffkie_preview_worldkie().setImageDrawable(drawable);
                    try {
                        DrawableUtils.personalizarImagenCuadradoButton(context,115/6,7,R.color.brownMaroon,drawable, holder.getIv_stuffkie_preview_worldkie());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
        } else {
            StorageReference userFolderRef = FirebaseStorage.getInstance("gs://buuk-tu-stuffkies").getReference(dataSet.get(holder.getAdapterPosition()).getUID());

            userFolderRef.listAll().addOnSuccessListener(listResult -> {
                for (StorageReference item : listResult.getItems()) {
                    if (item.getName().startsWith("cover")) {
                        item.getDownloadUrl().addOnSuccessListener(uri -> {
                            try {
                                DrawableUtils.personalizarImagenCuadradoButton(context,115/7,7, R.color.greenWhatever,uri,holder.getIv_stuffkie_preview_worldkie());
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