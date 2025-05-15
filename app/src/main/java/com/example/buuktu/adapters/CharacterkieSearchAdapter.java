package com.example.buuktu.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buuktu.views.CharacterkieView;
import com.example.buuktu.R;
import com.example.buuktu.models.CharacterkieModel;
import com.example.buuktu.utils.DrawableUtils;
import com.example.buuktu.utils.EfectsUtils;
import com.example.buuktu.utils.NavigationUtils;
import com.example.buuktu.views.MainActivity;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class CharacterkieSearchAdapter extends RecyclerView.Adapter<CharacterkieSearchAdapter.ViewHolder>{

    private final ArrayList<CharacterkieModel> dataSet;
    private final FragmentManager fragmentManager;

    private final MainActivity context;
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageButton iv_characterkie_photo_search;
        private final ImageView iv_characterkie_private_search;
        final MaterialCardView cv_characterkie_search;
        final TextView tv_characterkie_name_search;
        final TextView tv_characterkie_username_search;
        FrameLayout fl_characterkie_photo_container;
        public ViewHolder(View view) {
            super(view);
            tv_characterkie_username_search = view.findViewById(R.id.tv_characterkie_username_search);
            tv_characterkie_name_search =  view.findViewById(R.id.tv_characterkie_name_search);
            iv_characterkie_photo_search= view.findViewById(R.id.iv_characterkie_photo_search);
            cv_characterkie_search = view.findViewById(R.id.cv_characterkie_search);
            iv_characterkie_private_search = view.findViewById(R.id.iv_characterkie_private_search);
            fl_characterkie_photo_container = view.findViewById(R.id.fl_characterkie_photo_container);
        }

        public TextView getTv_characterkie_username_search() {
            return tv_characterkie_username_search;
        }

        public FrameLayout getFl_characterkie_photo_container() {
            return fl_characterkie_photo_container;
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
    public CharacterkieSearchAdapter(ArrayList<CharacterkieModel> dataSet, MainActivity ctx, FragmentManager fragmentManager) {
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
        holder.getFl_characterkie_photo_container().setVisibility(View.INVISIBLE);
        CharacterkieModel characterkie = dataSet.get(position);

        // holder.getTv_characterkie_username_search().setText(dataSet.get(holder.getAdapterPosition()).getUsername());
        holder.getTv_characterkie_name_search().setText(characterkie.getName());
        /*if(dataSet.get(holder.getAdapterPosition()).is()){
            holder.getIv_characterkie_private_search().setImageAlpha(R.drawable.twotone_lock_24);
        }else{
            holder.getIv_characterkie_private_search().setImageAlpha(R.drawable.twotone_lock_open_24);
        }*/

        holder.getCv_characterkie_search().setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            NavigationUtils.goNewFragmentWithBundle(bundle,fragmentManager,new CharacterkieView());

        });
        if (characterkie.isPhoto_default()) {
            String id_photo = characterkie.getPhoto_id();
            int resId = context.getResources().getIdentifier(id_photo, "mipmap", context.getPackageName());

            if (resId != 0) {
                Drawable drawable = ContextCompat.getDrawable(context, resId);
                //   holder.getIv_userkie_photo_search().set
                //DrawableUtils.personalizarImagenCuadradoButton(context, DrawableUtils.drawableToBitmap(drawable), holder.getIv_characterkie_photo_search(), R.color.brownBrown);
                holder.getFl_characterkie_photo_container().setVisibility(View.VISIBLE);
                EfectsUtils.startCircularReveal(drawable, holder.getIv_characterkie_photo_search());
            }
        } else {
            context.getFirebaseStorageCharacterkies().getReference(characterkie.getUID()).listAll().addOnSuccessListener(listResult -> {
                for (StorageReference item : listResult.getItems()) {
                    if (item.getName().startsWith("cover")) {
                        item.getBytes(5 * 1024 * 1024).addOnSuccessListener(bytes -> {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            Bitmap bitmapScaled = Bitmap.createScaledBitmap(bitmap, 80, 80, false);
                            DrawableUtils.personalizarImagenCircle(context, bitmapScaled, holder.getIv_characterkie_photo_search(), R.color.brownMaroon);
                        });
                        break;
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
