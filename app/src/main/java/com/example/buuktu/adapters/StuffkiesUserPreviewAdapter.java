package com.example.buuktu.adapters;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buuktu.views.CreateEditStuffkie;
import com.example.buuktu.R;
import com.example.buuktu.views.StuffkieView;
import com.example.buuktu.dialogs.DeleteGeneralDialog;
import com.example.buuktu.models.StuffkieModel;
import com.example.buuktu.utils.DrawableUtils;
import com.example.buuktu.utils.EfectsUtils;
import com.example.buuktu.utils.NavigationUtils;
import com.example.buuktu.views.MainActivity;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;

public class StuffkiesUserPreviewAdapter extends RecyclerView.Adapter<StuffkiesUserPreviewAdapter.ViewHolder>{

    private final ArrayList<StuffkieModel> dataSet;
    private final MainActivity context;
    private final FragmentManager fragmentManager;
    private final String mode;
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView iv_stuffkie_preview_worldkie;
        private final ImageView iv_stuffkie_private_preview;
        private final TextView tv_stuffkie_preview_worldkie;
        private final TextView tv_stuffkie_preview_draft;
        final CardView cv_stuffkie_preview;
        private final FirebaseStorage firebaseStorageStuffkies = FirebaseStorage.getInstance("gs://buuk-tu-stuffkies");
        //private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference collectionUserkies;
        public ViewHolder(View view) {
            super(view);
            iv_stuffkie_preview_worldkie =  view.findViewById(R.id.iv_stuffkie_preview_worldkie);
            tv_stuffkie_preview_worldkie= view.findViewById(R.id.tv_stuffkie_preview_worldkie);
            iv_stuffkie_private_preview = view.findViewById(R.id.iv_stuffkie_private_preview);
            tv_stuffkie_preview_draft = view.findViewById(R.id.tv_stuffkie_preview_draft);
            cv_stuffkie_preview = view.findViewById(R.id.cv_stuffkie_preview);
           // FirebaseStorage.getInstance("gs://buuk-tu-stuffkies");
        }

        public ImageView getIv_stuffkie_private_preview() {
            return iv_stuffkie_private_preview;
        }

        public TextView getTv_stuffkie_preview_draft() {
            return tv_stuffkie_preview_draft;
        }

        public ImageView getIv_stuffkie_preview_worldkie() {
            return iv_stuffkie_preview_worldkie;
        }

        public TextView getTv_stuffkie_preview_worldkie() {
            return tv_stuffkie_preview_worldkie;
        }

        public CardView getCv_stuffkie_preview() {
            return cv_stuffkie_preview;
        }

        public FirebaseStorage getFirebaseStorageStuffkies() {
            return firebaseStorageStuffkies;
        }

    }

    //Constructor donde pasamos la lista de productos y el contexto
    public StuffkiesUserPreviewAdapter(ArrayList<StuffkieModel> dataSet, MainActivity ctx, FragmentManager fragmentManager, String mode) {
        this.dataSet = dataSet;
        this.context = ctx;
        this.fragmentManager = fragmentManager;
        this.mode = mode;
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
        StuffkieModel stuffkieModel = dataSet.get(position);
        holder.getIv_stuffkie_preview_worldkie().setVisibility(View.INVISIBLE);
        holder.getTv_stuffkie_preview_worldkie().setText(stuffkieModel.getName());
        if(!stuffkieModel.isDraft()){
            holder.getTv_stuffkie_preview_draft().setVisibility(View.INVISIBLE);
        }
        if(!stuffkieModel.isStuffkie_private()){
            holder.getIv_stuffkie_private_preview().setVisibility(View.INVISIBLE);
        }

        holder.getCv_stuffkie_preview().setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("mode",mode);
            bundle.putString("UID",stuffkieModel.getUID());
            bundle.putString("UID_AUTHOR",stuffkieModel.getAUTHOR_UID());
            bundle.putString("UID_WORLDKIE",stuffkieModel.getWORDLKIE_UID());
            NavigationUtils.goNewFragmentWithBundle(bundle,fragmentManager,new StuffkieView());
        });
        if(mode.equals("self")) {
            holder.getCv_stuffkie_preview().setOnLongClickListener(v -> {
                View popupView = LayoutInflater.from(context).inflate(R.layout.menu_popup, null);
                PopupWindow popupWindow = new PopupWindow(popupView,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        true);

// Opcional: animación y sombra
                popupWindow.setElevation(8f);

// Mostrarlo anclado al CardView
                popupWindow.showAsDropDown(holder.getCv_stuffkie_preview(), 0, -50);

// ListenersBundle bundle = new Bundle();
//        bundle.putString("worldkie_id", worldkieModel.getUID());
                popupView.findViewById(R.id.bt_edit_item).setOnClickListener(view -> {
                    Bundle bundle = new Bundle();
                    bundle.putString("stuffkie_id", stuffkieModel.getUID());
                    NavigationUtils.goNewFragmentWithBundle(bundle, fragmentManager, new CreateEditStuffkie());
                    popupWindow.dismiss();
                });

                popupView.findViewById(R.id.bt_del_item).setOnClickListener(view2 -> {
                    DeleteGeneralDialog deleteGeneralDialog = new DeleteGeneralDialog(context, "stuffkie", stuffkieModel.getUID());
                    deleteGeneralDialog.show();
                    popupWindow.dismiss();
                });
                return true;
            });
        }
        if (stuffkieModel.isPhoto_default()) {
                String id_photo = stuffkieModel.getPhoto_id();
                int resId = context.getResources().getIdentifier(id_photo, "mipmap", context.getPackageName());

                if (resId != 0) {
                    Drawable drawable = ContextCompat.getDrawable(context, resId);
                    holder.getIv_stuffkie_preview_worldkie().setImageDrawable(drawable);
                    try {
                        DrawableUtils.personalizarImagenCuadradoButton(context,115/6,7,R.color.brownMaroon,drawable, holder.getIv_stuffkie_preview_worldkie());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    holder.getIv_stuffkie_preview_worldkie().setVisibility(View.VISIBLE);
                    EfectsUtils.startCircularReveal(drawable,holder.getIv_stuffkie_preview_worldkie());

                }
        } else {
            StorageReference userFolderRef = context.getFirebaseStorageStuffkies().getReference(stuffkieModel.getUID());

            userFolderRef.listAll().addOnSuccessListener(listResult -> {
                for (StorageReference item : listResult.getItems()) {
                    if (item.getName().startsWith("cover")) {
                        item.getDownloadUrl().addOnSuccessListener(uri -> {
                            try {
                                DrawableUtils.personalizarImagenCuadradoButton(context,115/7,7, R.color.greenWhatever,uri,holder.getIv_stuffkie_preview_worldkie(),R.mipmap.photostuffkieone);
                                holder.getIv_stuffkie_preview_worldkie().setVisibility(View.VISIBLE);
                                EfectsUtils.startCircularReveal(context,uri,holder.getIv_stuffkie_preview_worldkie());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });

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