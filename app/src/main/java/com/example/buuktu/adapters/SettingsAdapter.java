package com.example.buuktu.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buuktu.AccountSettings;
import com.example.buuktu.ProfileSettings;
import com.example.buuktu.R;
import com.example.buuktu.models.SettingModel;

import java.util.ArrayList;

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.ViewHolder> implements View.OnClickListener {

    @Override
    public void onClick(View v) {

    }
    private ArrayList<SettingModel> dataSet;
    private Context context;
    FragmentManager fragmentManager;
    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_photo_setting;
        private TextView tv_name_setting;
        private CardView card_view_setting_list_one;
        //private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance("gs://buuk-tu-worldkies");
        //private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        public ViewHolder(View view) {
            super(view);
            iv_photo_setting =  view.findViewById(R.id.iv_photo_setting_profile);
            tv_name_setting= view.findViewById(R.id.tv_name_setting_profile);
            card_view_setting_list_one = view.findViewById(R.id.card_view_setting_list_profile);
        }

        public TextView getTv_name_setting() {
            return tv_name_setting;
        }

        public ImageView getIv_photo_setting() {
            return iv_photo_setting;
        }
        public CardView getCard_view_setting_list_one() {
            return card_view_setting_list_one;
        }
    }

    //Constructor donde pasamos la lista de productos y el contexto
    public SettingsAdapter(ArrayList<SettingModel> dataSet, Context ctx,FragmentManager fragmentManager) {
        this.dataSet = dataSet;
        this.context = ctx;
        this.fragmentManager=fragmentManager;
    }


    //Se llama cada vez que se hace scroll en la pantalla y los elementos desaparecen y aparecen
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        //Creamos la vista de cada item a partir de nuestro layout
           View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.settings_list_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SettingsAdapter.ViewHolder holder, int position) {
        String name = dataSet.get(holder.getAdapterPosition()).getName();
            holder.getTv_name_setting().setText(name);
         holder.getIv_photo_setting().setImageDrawable(dataSet.get(holder.getAdapterPosition()).getDrawable());
         holder.getCard_view_setting_list_one().setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if (name.equals("Perfil")) {
                     ProfileSettings profileSettings = new ProfileSettings();
                     fragmentManager.beginTransaction().replace(R.id.fragment_container, profileSettings) .addToBackStack(null) // Permite regresar atrás con el botón de retroceso
                             .commit();
                 } else if (name.equals("Cuenta")) {
                     AccountSettings accountSettings = new AccountSettings();
                     fragmentManager.beginTransaction().replace(R.id.fragment_container, accountSettings) .addToBackStack(null) // Permite regresar atrás con el botón de retroceso
                             .commit();
                     // Toast.makeText(context, "Cuenta", Toast.LENGTH_SHORT).show();

                 }
             }
         });
    }


    // Devolvemos el numero de items de nuestro arraylist, lo invoca automaticamente el layout manager
    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}