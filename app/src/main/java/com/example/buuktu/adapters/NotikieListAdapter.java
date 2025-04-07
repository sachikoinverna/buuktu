package com.example.buuktu.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buuktu.R;
import com.example.buuktu.models.Characterkie;
import com.example.buuktu.models.NotikieModel;
import com.example.buuktu.utils.DrawableUtils;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class NotikieListAdapter extends RecyclerView.Adapter<NotikieListAdapter.ViewHolder> implements View.OnClickListener{
    @Override
    public void onClick(View v) {

    }
    private ArrayList<NotikieModel> dataSet;
    private FragmentManager fragmentManager;

    private Context context;
    private Fragment menuWorldkie;
    public class ViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cv_notikie_list_layout;
        TextView tv_text_notikie_list_layout,tv_date_notikie_list_layout;
        ImageView iv_icon_notikie_list_layout;
      //  private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance("gs://buuk-tu-worldkies");
        private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        public ViewHolder(View view) {
            super(view);
            iv_icon_notikie_list_layout = view.findViewById(R.id.iv_icon_notikie_list_layout);
            cv_notikie_list_layout = view.findViewById(R.id.cv_notikie_list_layout);
            tv_date_notikie_list_layout = view.findViewById(R.id.tv_date_notikie_list_layout);
            tv_text_notikie_list_layout = view.findViewById(R.id.tv_text_notikie_list_layout);
        }

      //  public FirebaseStorage getFirebaseStorage() {
      //      return firebaseStorage;
      //  }
        public FirebaseFirestore getDb() {
            return firestore;
        }

        public MaterialCardView getCv_notikie_list_layout() {
            return cv_notikie_list_layout;
        }

        public TextView getTv_text_notikie_list_layout() {
            return tv_text_notikie_list_layout;
        }

        public TextView getTv_date_notikie_list_layout() {
            return tv_date_notikie_list_layout;
        }

        public ImageView getIv_icon_notikie_list_layout() {
            return iv_icon_notikie_list_layout;
        }
    }
    //Constructor donde pasamos la lista de productos y el contexto
    public NotikieListAdapter(ArrayList<NotikieModel> dataSet, Context ctx, FragmentManager fragmentManager) {
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
        holder.getTv_text_notikie_list_layout().setText(dataSet.get(holder.getAdapterPosition()).getMessage());
        holder.getIv_icon_notikie_list_layout()
                .setImageAlpha(dataSet.get(holder.getAdapterPosition()).getIcon());
        /*if(dataSet.get(holder.getAdapterPosition()).is()){
            holder.getIv_characterkie_private_search().setImageAlpha(R.drawable.twotone_lock_24);
        }else{
            holder.getIv_characterkie_private_search().setImageAlpha(R.drawable.twotone_lock_open_24);
        }*/

        holder.getCv_notikie_list_layout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        ;
        DrawableUtils.personalizarImagenCircle(context,DrawableUtils.drawableToBitmap(holder.iv_icon_notikie_list_layout.getDrawable()),holder.getIv_icon_notikie_list_layout(),R.color.brownMaroon);

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
