package com.example.buuktu.adapters;

import static android.widget.Toast.LENGTH_LONG;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buuktu.ProfileView;
import com.example.buuktu.R;
import com.example.buuktu.models.UserkieModel;
import com.example.buuktu.utils.DrawableUtils;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class UserkieSearchAdapter extends RecyclerView.Adapter<UserkieSearchAdapter.ViewHolder> implements View.OnClickListener{
    @Override
    public void onClick(View v) {

    }
    private ArrayList<UserkieModel> dataSet;
    private FragmentManager fragmentManager;

    private Context context;
    private Fragment menuWorldkie;
    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_userkie_photo_search,iv_userkie_private_search;
        MaterialCardView cv_userkie_search;
        TextView tv_userkie_name_search, tv_userkie_username_search;
        private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance("gs://buuk-tu-worldkies");
        private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        public ViewHolder(View view) {
            super(view);
            tv_userkie_username_search = view.findViewById(R.id.tv_userkie_username_search);
            tv_userkie_name_search =  view.findViewById(R.id.tv_userkie_name_search);
            iv_userkie_photo_search= view.findViewById(R.id.iv_userkie_photo_search);
            cv_userkie_search = view.findViewById(R.id.cv_userkie_search);
            iv_userkie_private_search = view.findViewById(R.id.iv_userkie_private_search);
        }

        public FirebaseStorage getFirebaseStorage() {
            return firebaseStorage;
        }
        public FirebaseFirestore getDb() {
            return firestore;
        }

        public TextView getTv_userkie_username_search() {
            return tv_userkie_username_search;
        }

        public TextView getTv_userkie_name_search() {
            return tv_userkie_name_search;
        }

        public MaterialCardView getCv_userkie_search() {
            return cv_userkie_search;
        }

        public ImageView getIv_userkie_photo_search() {
            return iv_userkie_photo_search;
        }
        public ImageView getIv_userkie_private_search() {
            return iv_userkie_private_search;
        }
    }
    //Constructor donde pasamos la lista de productos y el contexto
    public UserkieSearchAdapter(ArrayList<UserkieModel> dataSet, Context ctx, FragmentManager fragmentManager) {
        this.dataSet = dataSet;
        this.context = ctx;
        this.fragmentManager = fragmentManager;
    }
    //Se llama cada vez que se hace scroll en la pantalla y los elementos desaparecen y aparecen
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        //Creamos la vista de cada item a partir de nuestro layout
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.userkies_list_layout_search, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getTv_userkie_username_search().setText(dataSet.get(holder.getAdapterPosition()).getUsername());
        holder.getTv_userkie_name_search().setText(dataSet.get(holder.getAdapterPosition()).getName());
        if(!dataSet.get(holder.getAdapterPosition()).isProfile_private()){
            holder.getIv_userkie_private_search().setVisibility(View.INVISIBLE);
        }

        holder.getCv_userkie_search().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileView profileView = new ProfileView();
                Bundle bundle = new Bundle();
                bundle.putString("mode","other");
                bundle.putString("UID",dataSet.get(holder.getAdapterPosition()).getUID());
                profileView.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, profileView)
                        .addToBackStack(null)
                        .commit();
            }
        });
        ;
        if(dataSet.get(holder.getAdapterPosition()).isPhoto_default()) {
            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            firebaseFirestore.collection("Users").document(dataSet.get(holder.getAdapterPosition()).getUID()).addSnapshotListener((queryDocumentSnapshot, e) -> {
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
                    holder.getIv_userkie_photo_search().setImageDrawable(drawable);
                    DrawableUtils.personalizarImagenCircle(context, DrawableUtils.drawableToBitmap(drawable), holder.getIv_userkie_photo_search(), R.color.brownMaroon);
                }
            });
        } else {
                            StorageReference userFolderRef = FirebaseStorage.getInstance("gs://buuk-tu-users").getReference(dataSet.get(holder.getAdapterPosition()).getUID());

                            userFolderRef.listAll().addOnSuccessListener(listResult -> {
                                for (StorageReference item : listResult.getItems()) {
                                    if (item.getName().startsWith("profile")) {
                                        item.getBytes(5 * 1024 * 1024).addOnSuccessListener(bytes -> {
                                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                            Bitmap bitmapScaled = Bitmap.createScaledBitmap(bitmap, 80, 80, false);
                                            DrawableUtils.personalizarImagenCircle(context, bitmapScaled, holder.getIv_userkie_photo_search(), R.color.brownMaroon);
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
