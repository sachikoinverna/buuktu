package com.example.buuktu.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buuktu.CharacterkiesSearch;
import com.example.buuktu.R;
import com.example.buuktu.models.Characterkie;
import com.example.buuktu.models.UserkieModel;
import com.example.buuktu.utils.DrawableUtils;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class CharacterkieSearchAdapter extends RecyclerView.Adapter<CharacterkieSearchAdapter.ViewHolder> implements View.OnClickListener{
    @Override
    public void onClick(View v) {

    }
    private ArrayList<Characterkie> dataSet;
    private FragmentManager fragmentManager;

    private Context context;
    private Fragment menuWorldkie;
    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_characterkie_photo_search,iv_characterkie_private_search;
        MaterialCardView cv_characterkie_search;
        TextView tv_characterkie_name_search, tv_characterkie_username_search;
        private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance("gs://buuk-tu-worldkies");
        private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        public ViewHolder(View view) {
            super(view);
            tv_characterkie_username_search = view.findViewById(R.id.tv_characterkie_username_search);
            tv_characterkie_name_search =  view.findViewById(R.id.tv_characterkie_name_search);
            iv_characterkie_photo_search= view.findViewById(R.id.iv_characterkie_photo_search);
            cv_characterkie_search = view.findViewById(R.id.cv_characterkie_search);
            iv_characterkie_private_search = view.findViewById(R.id.iv_characterkie_private_search);
        }

        public FirebaseStorage getFirebaseStorage() {
            return firebaseStorage;
        }
        public FirebaseFirestore getDb() {
            return firestore;
        }

        public TextView getTv_characterkie_username_search() {
            return tv_characterkie_username_search;
        }

        public TextView getTv_characterkie_name_search() {
            return tv_characterkie_name_search;
        }

        public MaterialCardView getCv_characterkie_search() {
            return cv_characterkie_search;
        }

        public ImageView getIv_characterkie_photo_search() {
            return iv_characterkie_photo_search;
        }
        public ImageView getIv_characterkie_private_search() {
            return iv_characterkie_private_search;
        }
    }
    //Constructor donde pasamos la lista de productos y el contexto
    public CharacterkieSearchAdapter(ArrayList<Characterkie> dataSet, Context ctx, FragmentManager fragmentManager) {
        this.dataSet = dataSet;
        this.context = ctx;
        this.fragmentManager = fragmentManager;
    }
    //Se llama cada vez que se hace scroll en la pantalla y los elementos desaparecen y aparecen
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        //Creamos la vista de cada item a partir de nuestro layout
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.characterkies_list_layout_search, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       // holder.getTv_characterkie_username_search().setText(dataSet.get(holder.getAdapterPosition()).getUsername());
        holder.getTv_characterkie_name_search().setText(dataSet.get(holder.getAdapterPosition()).getName());
        /*if(dataSet.get(holder.getAdapterPosition()).is()){
            holder.getIv_characterkie_private_search().setImageAlpha(R.drawable.twotone_lock_24);
        }else{
            holder.getIv_characterkie_private_search().setImageAlpha(R.drawable.twotone_lock_open_24);
        }*/

        holder.getCv_characterkie_search().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        ;
        DrawableUtils.personalizarImagenCircle(context,DrawableUtils.drawableToBitmap(holder.iv_characterkie_photo_search.getDrawable()),holder.getIv_characterkie_photo_search(),R.color.brownMaroon);

        //De esra forma establacemos las imagenes de la lista
        //String uri = "@drawable/" + dataSet.get(position).getPhoto();  // where myresource (without the extension) is the file
        //int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());
        //Drawable res =  context.getResources().getDrawable(imageResource);
        //  holder.getIv_photo_wordlkie().setImageDrawable(dataSet.get(holder.getAdapterPosition()).getPhoto());
        //  Bitmap bitmap = DrawableUtils.drawableToBitmap(dataSet.get(holder.getAdapterPosition()).getPhoto());
        //  int colorRGB = Color.rgb(139, 111, 71);
        //8B6F47
        //  Color color = Color.valueOf(colorRGB);

        // DrawableUtils.personalizarImagenCuadrado(context,bitmap,holder.getIv_photo_wordlkie(),color);
    }


    // Devolvemos el numero de items de nuestro arraylist, lo invoca automaticamente el layout manager
    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
