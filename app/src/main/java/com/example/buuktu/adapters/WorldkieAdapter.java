package com.example.buuktu.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buuktu.R;
import com.example.buuktu.models.WorldkieModel;

import java.util.ArrayList;

public class WorldkieAdapter extends RecyclerView.Adapter<WorldkieAdapter.ViewHolder>{

    public interface ItemClickListener {
        public void onClick(View view, int position);
    }
        private ArrayList<WorldkieModel> dataSet;

    private ItemClickListener clicListener;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView tv_name_wordlkie;
        private ImageView iv_photo_wordlkie;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            tv_name_wordlkie = (TextView) view.findViewById(R.id.tv_name_wordlkie);
            iv_photo_wordlkie = (ImageView) view.findViewById(R.id.iv_photo_wordlkie);
        }

        //getters
        public TextView getTv_name_wordkie() {
            return tv_name_wordlkie;
        }

        public ImageView getIv_photo_wordlkie() {
            return iv_photo_wordlkie;
        }

        //Esto propaga el evento hacía fuera, así podemos capturarlo en el punto que queramos de
        //nuestra aplicación
        @Override
        public void onClick(View view) {
            clicListener.onClick(view, getAdapterPosition());
        }
    }

    //Constructor donde pasamos la lista de productos y el contexto
    public WorldkieAdapter(ArrayList<WorldkieModel> dataSet, Context ctx) {
        this.dataSet = dataSet;
        this.context = ctx;
    }

    //Este metodo se utiliza desde la actividad que captura el evento de clic de los items
    public void setOnClickListener(ItemClickListener clicListener){
        this.clicListener = clicListener;
    }

    //Se llama cada vez que se hace scroll en la pantalla y los elementos desaparecen y aparecen
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        //Creamos la vista de cada item a partir de nuestro layout
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.worldkie_list_layout, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorldkieAdapter.ViewHolder holder, int position) {
        holder.getTv_name_wordkie().setText(dataSet.get(position).getName());

        //De esra forma establacemos las imagenes de la lista
        String uri = "@drawable/" + dataSet.get(position).getPhoto();  // where myresource (without the extension) is the file
        int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());

        Drawable res =  context.getResources().getDrawable(imageResource);
        holder.getIv_photo_wordlkie().setImageDrawable(res);
    }


    // Devolvemos el numero de items de nuestro arraylist, lo invoca automaticamente el layout manager
    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}