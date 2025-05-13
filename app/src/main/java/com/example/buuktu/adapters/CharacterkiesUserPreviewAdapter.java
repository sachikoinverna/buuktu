package com.example.buuktu.adapters;

import android.content.Context;
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
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buuktu.CharacterkieView;
import com.example.buuktu.CreateCharacterkie;
import com.example.buuktu.R;
import com.example.buuktu.dialogs.DeleteGeneralDialog;
import com.example.buuktu.models.CharacterkieModel;
import com.example.buuktu.utils.DrawableUtils;
import com.example.buuktu.utils.EfectsUtils;
import com.example.buuktu.utils.NavigationUtils;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class CharacterkiesUserPreviewAdapter extends RecyclerView.Adapter<CharacterkiesUserPreviewAdapter.ViewHolder> {


    private final ArrayList<CharacterkieModel> dataSet;

    private final Context context;
    private final FragmentManager fragmentManager;
    private final String mode;
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageButton iv_characterkie_preview_worldkie;
        private final ImageView iv_characterkie_private_preview;
        private final TextView tv_characterkie_preview_worldkie;
        private final TextView tv_characterkie_preview_draft;
        final CardView cardView;
        private FirebaseStorage firebaseStorageCharacterkies = FirebaseStorage.getInstance("gs://buuk-tu-characterkies");
        //private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        private final FrameLayout fl_characterkie_photo_container_preview;

        public ViewHolder(View view) {
            super(view);
            iv_characterkie_preview_worldkie =  view.findViewById(R.id.iv_characterkie_preview_worldkie);
            tv_characterkie_preview_worldkie= view.findViewById(R.id.tv_characterkie_preview_worldkie);
            iv_characterkie_private_preview = view.findViewById(R.id.iv_characterkie_private_preview);
            tv_characterkie_preview_draft = view.findViewById(R.id.tv_characterkie_preview_draft);
            cardView = view.findViewById(R.id.cv_characterkie_preview);
            fl_characterkie_photo_container_preview = view.findViewById(R.id.fl_characterkie_photo_container_preview);
        }

        public FirebaseStorage getFirebaseStorageCharacterkies() {
            return firebaseStorageCharacterkies;
        }

        public FrameLayout getFl_characterkie_photo_container_preview() {
            return fl_characterkie_photo_container_preview;
        }

        public ImageButton getIv_characterkie_preview_worldkie() {
            return iv_characterkie_preview_worldkie;
        }

        public TextView getTv_characterkie_preview_worldkie() {
            return tv_characterkie_preview_worldkie;
        }

        public TextView getTv_characterkie_preview_draft() {
            return tv_characterkie_preview_draft;
        }

        public ImageView getIv_characterkie_private_preview() {
            return iv_characterkie_private_preview;
        }

        public CardView getCardView() {
            return cardView;
        }
    }

    //Constructor donde pasamos la lista de productos y el contexto
    public CharacterkiesUserPreviewAdapter(ArrayList<CharacterkieModel> dataSet, Context ctx,FragmentManager fragmentManager,String mode) {
        this.dataSet = dataSet;
        this.context = ctx;
        this.fragmentManager = fragmentManager;
        this.mode=mode;
    }

    
    //Se llama cada vez que se hace scroll en la pantalla y los elementos desaparecen y aparecen
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        //Creamos la vista de cada item a partir de nuestro layout
           View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.characterkie_list_layout_preview, viewGroup, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull CharacterkiesUserPreviewAdapter.ViewHolder holder, int position) {
        holder.getFl_characterkie_photo_container_preview().setVisibility(View.INVISIBLE);
        CharacterkieModel characterkieModel = dataSet.get(position);
        holder.getTv_characterkie_preview_worldkie().setText(characterkieModel.getName());
        if(!characterkieModel.isDraft()){
            holder.getTv_characterkie_preview_draft().setVisibility(View.INVISIBLE);
        }
        if(!characterkieModel
                .isCharacterkie_private()){
            holder.getIv_characterkie_preview_worldkie().setVisibility(View.GONE);
        }
        holder.getCardView().setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("mode","other");
            bundle.putString("UID",characterkieModel.getUID());
            bundle.putString("UID_AUTHOR",characterkieModel.getUID_AUTHOR());
            bundle.putString("UID_WORLDKIE",characterkieModel.getUID_WORLDKIE());
            NavigationUtils.goNewFragmentWithBundle(bundle,fragmentManager,new CharacterkieView());
        });
        holder.getCardView().setOnLongClickListener(v -> {
            View popupView = LayoutInflater.from(context).inflate(R.layout.menu_popup, null);
            PopupWindow popupWindow = new PopupWindow(popupView,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    true);

// Opcional: animación y sombra
            popupWindow.setElevation(8f);

// Mostrarlo anclado al CardView
            popupWindow.showAsDropDown(holder.getCardView(), 0, -50);

// ListenersBundle bundle = new Bundle();
//        bundle.putString("worldkie_id", worldkieModel.getUID());
            popupView.findViewById(R.id.bt_edit_item).setOnClickListener(view -> {
                Bundle bundle = new Bundle();
                bundle.putString("characterkie_id", characterkieModel.getUID());
                NavigationUtils.goNewFragmentWithBundle(bundle, fragmentManager, new CreateCharacterkie());
                popupWindow.dismiss();
            });

            popupView.findViewById(R.id.bt_del_item).setOnClickListener(view2 -> {
                DeleteGeneralDialog deleteGeneralDialog = new DeleteGeneralDialog(context, "characterkie", characterkieModel.getUID());
                deleteGeneralDialog.show();
                popupWindow.dismiss();
            });
            return true;
        });
        if (characterkieModel.isPhoto_default()) {
                String id_photo = characterkieModel.getPhoto_id();
                int resId = context.getResources().getIdentifier(id_photo, "mipmap", context.getPackageName());

                if (resId != 0) {

                    Drawable drawable = ContextCompat.getDrawable(context, resId);
                    //   holder.getIv_userkie_photo_search().set
                 //   DrawableUtils.personalizarImagenCircleButton(context, DrawableUtils.drawableToBitmap(drawable), holder.getIv_characterkie_preview_worldkie(), R.color.brownBrown);
                    holder.getIv_characterkie_preview_worldkie().setVisibility(View.VISIBLE);
                    holder.getFl_characterkie_photo_container_preview().setVisibility(View.VISIBLE);
                    EfectsUtils.startCircularReveal(drawable, holder.getIv_characterkie_preview_worldkie());
                }
        } else {
            StorageReference userFolderRef = holder.getFirebaseStorageCharacterkies().getReference(characterkieModel.getUID());

            userFolderRef.listAll().addOnSuccessListener(listResult -> {
                for (StorageReference item : listResult.getItems()) {
                    if (item.getName().startsWith("cover")) {
                            item.getBytes(5 * 1024 * 1024).addOnSuccessListener(bytes -> {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                Bitmap bitmapScaled = Bitmap.createScaledBitmap(bitmap, 80, 80, false);
                                DrawableUtils.personalizarImagenCircle(context, bitmapScaled, holder.getIv_characterkie_preview_worldkie(), R.color.brownMaroon);
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