package com.example.buuktu.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buuktu.R;
import com.example.buuktu.controllers.HomeController;
import com.example.buuktu.controllers.WorldkieAdapterController;
import com.example.buuktu.models.WorldkieModel;

import java.util.ArrayList;

public class WorldkieAdapter extends RecyclerView.Adapter<WorldkieAdapter.ViewHolder> implements View.OnClickListener {

    @Override
    public void onClick(View v) {

    }
    private ArrayList<WorldkieModel> dataSet;

    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_name_wordlkie;
        private ImageView iv_photo_wordlkie;
        private ImageButton ib_enterToAWorldkie ;
        private ImageButton ib_select_img_create_worldkie;
        private ImageButton ib_deleteAWorldkie;
        public ViewHolder(View view) {
            super(view);
            tv_name_wordlkie = view.findViewById(R.id.tv_name_wordlkie);
            iv_photo_wordlkie =  view.findViewById(R.id.iv_photo_wordlkie);
            ib_enterToAWorldkie= view.findViewById(R.id.ib_enterToAWorldkie);
            ib_select_img_create_worldkie = view.findViewById(R.id.ib_select_img_create_worldkie);
            ib_deleteAWorldkie = view.findViewById(R.id.ib_deleteAWorldkie);
        }

        public ImageButton getIb_enterToAWorldkie() {
            return ib_enterToAWorldkie;
        }

        public ImageButton getIb_select_img_create_worldkie() {
            return ib_select_img_create_worldkie;
        }

        public ImageButton getIb_deleteAWorldkie() {
            return ib_deleteAWorldkie;
        }

        //getters
        public TextView getTv_name_wordkie() {
            return tv_name_wordlkie;
        }

        public ImageView getIv_photo_wordlkie() {
            return iv_photo_wordlkie;
        }

    }

    //Constructor donde pasamos la lista de productos y el contexto
    public WorldkieAdapter(ArrayList<WorldkieModel> dataSet, Context ctx) {
        this.dataSet = dataSet;
        this.context = ctx;
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
            //String uri = "@drawable/" + dataSet.get(position).getPhoto();  // where myresource (without the extension) is the file
            //int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());
            //Drawable res =  context.getResources().getDrawable(imageResource);
            holder.getIv_photo_wordlkie().setImageDrawable(dataSet.get(position).getPhoto());
    }


    // Devolvemos el numero de items de nuestro arraylist, lo invoca automaticamente el layout manager
    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}