package com.example.buuktu.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buuktu.R;
import com.example.buuktu.models.Characterkie;
import com.example.buuktu.utils.DrawableUtils;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;

public class CharacterkiesUserPreviewAdapter extends RecyclerView.Adapter<CharacterkiesUserPreviewAdapter.ViewHolder> implements View.OnClickListener {

    @Override
    public void onClick(View v) {

    }
    private ArrayList<Characterkie> dataSet;
    private ItemClickListener clicListener;

    private Context context;

    public interface ItemClickListener {
        public void onClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_characterkie_preview_worldkie,iv_characterkie_private_preview;
        private TextView tv_characterkie_preview_worldkie,tv_characterkie_preview_draft;
        //private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance("gs://buuk-tu-worldkies");
        //private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        public ViewHolder(View view) {
            super(view);
            iv_characterkie_preview_worldkie =  view.findViewById(R.id.iv_characterkie_preview_worldkie);
            tv_characterkie_preview_worldkie= view.findViewById(R.id.tv_characterkie_preview_worldkie);
            iv_characterkie_private_preview = view.findViewById(R.id.iv_characterkie_private_preview);
            tv_characterkie_preview_draft = view.findViewById(R.id.tv_characterkie_preview_draft);
        }

        public ImageView getIv_characterkie_preview_worldkie() {
            return iv_characterkie_preview_worldkie;
        }

        public TextView getTv_characterkie_preview_worldkie() {
            return tv_characterkie_preview_worldkie;
        }

        public TextView getTv_characterkie_preview_draft() {
            return tv_characterkie_preview_draft;
        }

        public ImageView getIv_characterkie_private_preview() {
            return iv_characterkie_private_preview;
        }
    }

    //Constructor donde pasamos la lista de productos y el contexto
    public CharacterkiesUserPreviewAdapter(ArrayList<Characterkie> dataSet, Context ctx) {
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
           View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.characterkie_list_layout_preview, viewGroup, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull CharacterkiesUserPreviewAdapter.ViewHolder holder, int position) {
        holder.getTv_characterkie_preview_worldkie().setText(dataSet.get(holder.getAdapterPosition()).getName());
        if(!dataSet.get(holder.getAdapterPosition()).isDraft()){
            holder.getTv_characterkie_preview_draft().setVisibility(View.INVISIBLE);
        }
        if(!dataSet.get(holder.getAdapterPosition())
                .isCharacterkie_private()){
            holder.getIv_characterkie_preview_worldkie().setVisibility(View.GONE);
        }
        if (dataSet.get(holder.getAdapterPosition()).isPhoto_default()) {
            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            firebaseFirestore.collection("Characterkies").document(dataSet.get(holder.getAdapterPosition()).getUID()).addSnapshotListener((queryDocumentSnapshot, e) -> {
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
                    holder.getIv_characterkie_preview_worldkie().setImageDrawable(drawable);
                    try {
                        DrawableUtils.personalizarImagenCuadradoButton(context,115/6,7,R.color.brownMaroon,drawable, holder.getIv_characterkie_preview_worldkie());
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
                            item.getBytes(5 * 1024 * 1024).addOnSuccessListener(bytes -> {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                Bitmap bitmapScaled = Bitmap.createScaledBitmap(bitmap, 80, 80, false);
                                DrawableUtils.personalizarImagenCircle(context, bitmapScaled, holder.getIv_characterkie_preview_worldkie(), R.color.brownMaroon);
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