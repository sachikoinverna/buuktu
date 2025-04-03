package com.example.buuktu.adapters;

import static android.widget.Toast.LENGTH_LONG;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buuktu.R;
import com.example.buuktu.models.UserkieModel;
import com.example.buuktu.models.WorldkieModel;
import com.example.buuktu.utils.DrawableUtils;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class WorldkieSearchAdapter extends RecyclerView.Adapter<WorldkieSearchAdapter.ViewHolder> implements View.OnClickListener{
    @Override
    public void onClick(View v) {

    }
    private ArrayList<WorldkieModel> dataSet;
    private FragmentManager fragmentManager;

    private Context context;
    private Fragment menuWorldkie;
    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_worldkie_photo_search,iv_worldkie_private_search;
        ImageButton ib_show_more_details_worldkie_search;
        MaterialCardView cv_worldkie_search;
        TextView tv_worldkie_name_search, tv_worldkie_username_search,tv_date_creation_search_worldkie_title,tv_date_creation_search_worldkie,tv_date_last_update_search_worldkie_title,tv_date_last_update_search_worldkie;
        private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance("gs://buuk-tu-worldkies");
        private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        private boolean moreDetailsShowed;
        CollectionReference collectionUserkies;
        private FirebaseFirestore db;
        public ViewHolder(View view) {
            super(view);
            ib_show_more_details_worldkie_search = view.findViewById(R.id.ib_show_more_details_worldkie_search);
            tv_worldkie_username_search = view.findViewById(R.id.tv_worldkie_username_search);
            tv_worldkie_name_search =  view.findViewById(R.id.tv_worldkie_name_search);
            tv_date_creation_search_worldkie_title = view.findViewById(R.id.tv_date_creation_search_worldkie_title);
            tv_date_creation_search_worldkie = view.findViewById(R.id.tv_date_creation_search_worldkie);
            tv_date_last_update_search_worldkie_title = view.findViewById(R.id.tv_date_last_update_search_worldkie_title);
            tv_date_last_update_search_worldkie = view.findViewById(R.id.tv_date_last_update_search_worldkie);
            iv_worldkie_photo_search= view.findViewById(R.id.iv_worldkie_photo_search);
            cv_worldkie_search = view.findViewById(R.id.cv_worldkie_search);
            iv_worldkie_private_search = view.findViewById(R.id.iv_worldkie_private_search);
            db = FirebaseFirestore.getInstance();
            collectionUserkies = db.collection("Users");

            moreDetailsShowed = false;
        }

        public FirebaseStorage getFirebaseStorage() {
            return firebaseStorage;
        }
        public FirebaseFirestore getDb() {
            return firestore;
        }

        public TextView getTv_worldkie_username_search() {
            return tv_worldkie_username_search;
        }

        public TextView getTv_worldkie_name_search() {
            return tv_worldkie_name_search;
        }

        public MaterialCardView getCv_worldkie_search() {
            return cv_worldkie_search;
        }

        public ImageView getIv_worldkie_photo_search() {
            return iv_worldkie_photo_search;
        }
        public ImageView getIv_worldkie_private_search() {
            return iv_worldkie_private_search;
        }

        public TextView getTv_date_last_update_search_worldkie() {
            return tv_date_last_update_search_worldkie;
        }

        public TextView getTv_date_last_update_search_worldkie_title() {
            return tv_date_last_update_search_worldkie_title;
        }

        public TextView getTv_date_creation_search_worldkie() {
            return tv_date_creation_search_worldkie;
        }

        public TextView getTv_date_creation_search_worldkie_title() {
            return tv_date_creation_search_worldkie_title;
        }

        public ImageButton getIb_show_more_details_worldkie_search() {
            return ib_show_more_details_worldkie_search;
        }

        public CollectionReference getCollectionUserkies() {
            return collectionUserkies;
        }
    }
    //Constructor donde pasamos la lista de productos y el contexto
    public WorldkieSearchAdapter(ArrayList<WorldkieModel> dataSet, Context ctx, FragmentManager fragmentManager) {
        this.dataSet = dataSet;
        this.context = ctx;
        this.fragmentManager = fragmentManager;
    }
    //Se llama cada vez que se hace scroll en la pantalla y los elementos desaparecen y aparecen
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        //Creamos la vista de cada item a partir de nuestro layout
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.worldkies_list_layout_search, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    holder.getCollectionUserkies().document(dataSet.get(holder.getAdapterPosition()).getUID_AUTHOR()).addSnapshotListener((documentSnapshot, e) -> {
                if (e != null) {
                    Log.e("Error", e.getMessage());
                    Toast.makeText(context, "Error al escuchar cambios: " + e.getMessage(), LENGTH_LONG).show();
                    return;
                }

                if (documentSnapshot != null) {
                    holder.getTv_worldkie_username_search().setText(documentSnapshot.getString("username"));
                }
            });
        holder.getTv_date_last_update_search_worldkie_title().setVisibility(View.GONE);
        holder.getTv_date_last_update_search_worldkie().setVisibility(View.GONE);
        holder.getTv_date_creation_search_worldkie_title().setVisibility(View.GONE);
        holder.getTv_date_creation_search_worldkie().setVisibility(View.GONE);

        holder.getTv_worldkie_name_search().setText(dataSet.get(holder.getAdapterPosition()).getName());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        holder.getTv_date_last_update_search_worldkie().setText(simpleDateFormat.format(dataSet.get(holder.getAdapterPosition()).getLast_update()));
        holder.getTv_date_creation_search_worldkie().setText(simpleDateFormat.format( dataSet.get(holder.getAdapterPosition()).getCreation_date()));

        if(dataSet.get(holder.getAdapterPosition()).isWorldkie_private()){
            holder.getIv_worldkie_private_search().setImageAlpha(R.drawable.twotone_lock_24);
        }else{
            holder.getIv_worldkie_private_search().setImageAlpha(R.drawable.twotone_lock_open_24);
        }

        holder.getCv_worldkie_search().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.getIb_show_more_details_worldkie_search().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.moreDetailsShowed){
                    holder.getTv_date_last_update_search_worldkie_title().setVisibility(View.GONE);
                    holder.getTv_date_last_update_search_worldkie().setVisibility(View.GONE);
                    holder.getTv_date_creation_search_worldkie_title().setVisibility(View.GONE);
                    holder.getTv_date_creation_search_worldkie().setVisibility(View.GONE);
                    holder.moreDetailsShowed = false;
                    holder.getIb_show_more_details_worldkie_search().setImageResource(R.drawable.twotone_arrow_drop_down_circle_24);
                }else{
                    holder.getTv_date_last_update_search_worldkie_title().setVisibility(View.VISIBLE);
                    holder.getTv_date_last_update_search_worldkie().setVisibility(View.VISIBLE);
                    holder.getTv_date_creation_search_worldkie_title().setVisibility(View.VISIBLE);
                    holder.getTv_date_creation_search_worldkie().setVisibility(View.VISIBLE);
                    holder.moreDetailsShowed = true;
                    holder.getIb_show_more_details_worldkie_search().setImageResource(R.drawable.twotone_keyboard_arrow_up_24);
                }
            }
        });
        ;
        Color color = Color.valueOf(context.getColor(R.color.brownBrown));
        DrawableUtils.personalizarImagenCuadrado(context,DrawableUtils.drawableToBitmap(holder.getIv_worldkie_photo_search().getDrawable()),holder.getIv_worldkie_photo_search(),R.color.brownMaroon);
        /*holder.getIb_enterToAWorldkie().setOnClickListener(new View.OnClickListener() {
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
        });*/
      /*  holder.getIb_editAWorldkie().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), CreateWorldkie.class);
                intent.putExtra("create",false);
                //  intent.putExtra("worldkie",new WorldkieModel(dataSet.get(holder.getAdapterPosition()).getUID(),dataSet.get(holder.getAdapterPosition()).getName(),dataSet.get(holder.getAdapterPosition()).isPhoto_default(),dataSet.get(holder.getAdapterPosition()).isWorldkie_private()));
                holder.itemView.getContext().startActivity(intent);
            }
        });*/
       /* holder.getIb_deleteAWorldkie().setOnClickListener(new View.OnClickListener() {
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
        });*/
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
