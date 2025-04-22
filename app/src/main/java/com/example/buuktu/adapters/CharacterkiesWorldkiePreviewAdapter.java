package com.example.buuktu.adapters;

import android.content.Context;
import android.graphics.Bitmap;
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

import java.util.ArrayList;

public class CharacterkiesWorldkiePreviewAdapter extends RecyclerView.Adapter<CharacterkiesWorldkiePreviewAdapter.ViewHolder> implements View.OnClickListener {

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
        private ImageView iv_characterkie_preview_worldkie;
        private TextView tv_characterkie_preview_worldkie;
        //private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance("gs://buuk-tu-worldkies");
        //private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        
        public ViewHolder(View view) {
            super(view);
            iv_characterkie_preview_worldkie =  view.findViewById(R.id.iv_characterkie_preview_worldkie);
            tv_characterkie_preview_worldkie= view.findViewById(R.id.tv_characterkie_preview_worldkie);
        }

        public ImageView getIv_characterkie_preview_worldkie() {
            return iv_characterkie_preview_worldkie;
        }

        public TextView getTv_characterkie_preview_worldkie() {
            return tv_characterkie_preview_worldkie;
        }
    }

    //Constructor donde pasamos la lista de productos y el contexto
    public CharacterkiesWorldkiePreviewAdapter(ArrayList<Characterkie> dataSet, Context ctx) {
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
    public void onBindViewHolder(@NonNull CharacterkiesWorldkiePreviewAdapter.ViewHolder holder, int position) {
        holder.getTv_characterkie_preview_worldkie().setText(dataSet.get(holder.getAdapterPosition()).getName());
        // holder.getIv_characterkie_preview_worldkie().setImageDrawable(dataSet.get(holder.getAdapterPosition()).getPhoto());
        Drawable drawable = context.getDrawable(R.drawable.thumb_custom);
      //  Drawable drawable = dataSet.get(holder.getAdapterPosition()).getPhoto();

        Bitmap bitmap = DrawableUtils.drawableToBitmap(drawable);
        DrawableUtils.personalizarImagenCircle(context,bitmap,holder.getIv_characterkie_preview_worldkie(),R.color.brownBrown);
    }

    // Devolvemos el numero de items de nuestro arraylist, lo invoca automaticamente el layout manager
    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}