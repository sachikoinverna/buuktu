package com.example.buuktu;

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

import com.example.buuktu.models.Characterkie;
import com.example.buuktu.models.StuffkieModel;
import com.example.buuktu.utils.DrawableUtils;

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
        private ImageView iv_stuffkie_preview_worldkie;
        private TextView tv_stuffkie_preview_worldkie;
        //private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance("gs://buuk-tu-worldkies");
        //private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        public ViewHolder(View view) {
            super(view);
            iv_stuffkie_preview_worldkie =  view.findViewById(R.id.iv_stuffkie_preview_worldkie);
            tv_stuffkie_preview_worldkie= view.findViewById(R.id.tv_stuffkie_preview_worldkie);
        }

        public ImageView getIv_stuffkie_preview_worldkie() {
            return iv_stuffkie_preview_worldkie;
        }

        public TextView getTv_stuffkie_preview_worldkie() {
            return tv_stuffkie_preview_worldkie;
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
        // holder.getIv_characterkie_preview_worldkie().setImageDrawable(dataSet.get(holder.getAdapterPosition()).getPhoto());
        Drawable drawable = context.getDrawable(R.drawable.cloudlogin);
      //  Drawable drawable = dataSet.get(holder.getAdapterPosition()).getPhoto();

        Bitmap bitmap = DrawableUtils.drawableToBitmap(drawable);
        int colorInt = ContextCompat.getColor(context, R.color.redError);
        Color color = Color.valueOf(colorInt);

        DrawableUtils.personalizarImagenCircle(context,bitmap,holder.getIv_stuffkie_preview_worldkie(),R.color.brownBrown);
    }

    // Devolvemos el numero de items de nuestro arraylist, lo invoca automaticamente el layout manager
    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}