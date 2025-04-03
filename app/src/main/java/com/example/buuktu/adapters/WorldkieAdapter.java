package com.example.buuktu.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buuktu.R;
import com.example.buuktu.models.WorldkieModel;
import com.example.buuktu.utils.DrawableUtils;
import com.example.buuktu.views.CreateWorldkie;
import com.example.buuktu.views.Worldkie;
import com.example.buuktu.views.WorldkieMenu;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class WorldkieAdapter extends RecyclerView.Adapter<WorldkieAdapter.ViewHolder> implements View.OnClickListener {

    @Override
    public void onClick(View v) {

    }
    private ArrayList<WorldkieModel> dataSet;
    private FragmentManager fragmentManager;

    private Context context;
    private Fragment menuWorldkie;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_name_wordlkie;
        private ImageView iv_photo_wordlkie;
        private ImageButton ib_enterToAWorldkie ;
        private ImageButton ib_editAWorldkie;
        private ImageButton ib_deleteAWorldkie;
        private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance("gs://buuk-tu-worldkies");
        private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        public ViewHolder(View view) {
            super(view);
            tv_name_wordlkie = view.findViewById(R.id.tv_name_setting);
            iv_photo_wordlkie =  view.findViewById(R.id.iv_photo_setting);
            ib_enterToAWorldkie= view.findViewById(R.id.ib_enterToAWorldkie);
            ib_editAWorldkie = view.findViewById(R.id.ib_editAWorldkie);
            ib_deleteAWorldkie = view.findViewById(R.id.ib_deleteAWorldkie);
        }

        public FirebaseStorage getFirebaseStorage() {
            return firebaseStorage;
        }
        public FirebaseFirestore getDb() {
            return firestore;
        }

        public ImageButton getIb_enterToAWorldkie() {
            return ib_enterToAWorldkie;
        }

        public ImageButton getIb_editAWorldkie() {
            return ib_editAWorldkie;
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
    public WorldkieAdapter(ArrayList<WorldkieModel> dataSet, Context ctx, FragmentManager fragmentManager) {
        this.dataSet = dataSet;
        this.context = ctx;
        this.fragmentManager = fragmentManager;
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
            holder.getTv_name_wordkie().setText(dataSet.get(holder.getAdapterPosition()).getName());
            holder.getIb_enterToAWorldkie().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    menuWorldkie = new WorldkieMenu();
                    Bundle bundle = new Bundle();
                    bundle.putString("worlkie_id",dataSet.get(holder.getAdapterPosition()).getUID());
                    menuWorldkie.setArguments(bundle);
                    fragmentManager.beginTransaction().replace(R.id.fragment_container, menuWorldkie) .addToBackStack(null) // Permite regresar atrás con el botón de retroceso
                            .commit();
                    //Intent intent = new Intent(holder.itemView.getContext(), Worldkie.class);
                //   holder.
                    // Intent intent = new Intent(holder.itemView.getContext(), WorldkieMenu.class);
                   // holder.itemView.getContext().startActivity(intent);
                }
            });
            holder.getIb_editAWorldkie().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(holder.itemView.getContext(), CreateWorldkie.class);
                    intent.putExtra("create",false);
                    intent.putExtra("worldkie",new WorldkieModel(dataSet.get(holder.getAdapterPosition()).getUID(),dataSet.get(holder.getAdapterPosition()).getName(),dataSet.get(holder.getAdapterPosition()).isPhoto_default(),dataSet.get(holder.getAdapterPosition()).isWorldkie_private()));
                    holder.itemView.getContext().startActivity(intent);
                }
            });
            holder.getIb_deleteAWorldkie().setOnClickListener(new View.OnClickListener() {
                                                                  @Override
                                                                  public void onClick(View v) {
                                                                      holder.getDb().collection("Worldkies").document(dataSet.get(holder.getAdapterPosition()).getUID()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                          @Override
                                                                          public void onSuccess(Void unused) {
                                                                                holder.getFirebaseStorage().getReference().child(dataSet.get(holder.getAdapterPosition()).getUID()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                    @Override
                                                                                    public void onSuccess(Void unused) {

                                                                                    }
                                                                                }).addOnFailureListener(new OnFailureListener() {
                                                                                    @Override
                                                                                    public void onFailure(@NonNull Exception e) {

                                                                                    }
                                                                                });
                                                                          }
                                                                      }).addOnFailureListener(new OnFailureListener() {
                                                                          @Override
                                                                          public void onFailure(@NonNull Exception e) {

                                                                          }
                                                                      });
                                                                  }
                                                              });
            //De esra forma establacemos las imagenes de la lista
            //String uri = "@drawable/" + dataSet.get(position).getPhoto();  // where myresource (without the extension) is the file
            //int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());
            //Drawable res =  context.getResources().getDrawable(imageResource);
            holder.getIv_photo_wordlkie().setImageDrawable(dataSet.get(holder.getAdapterPosition()).getPhoto());
        Bitmap bitmap = DrawableUtils.drawableToBitmap(dataSet.get(holder.getAdapterPosition()).getPhoto());
        int colorRGB = Color.rgb(139, 111, 71);
        //8B6F47
        Color color = Color.valueOf(colorRGB);

        DrawableUtils.personalizarImagenCuadrado(context,bitmap,holder.getIv_photo_wordlkie(),R.color.brownMaroon);
    }


    // Devolvemos el numero de items de nuestro arraylist, lo invoca automaticamente el layout manager
    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}