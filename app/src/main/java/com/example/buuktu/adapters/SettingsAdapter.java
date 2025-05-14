package com.example.buuktu.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buuktu.views.AccountSettings;
import com.example.buuktu.views.ProfileSettings;
import com.example.buuktu.R;
import com.example.buuktu.models.SettingModel;
import com.example.buuktu.utils.NavigationUtils;

import java.util.ArrayList;

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.ViewHolder>{

    private final ArrayList<SettingModel> dataSet;
    private final Context context;
    final FragmentManager fragmentManager;
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView iv_photo_setting;
        private final TextView tv_name_setting;
        private final CardView card_view_setting_list_one;
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
        SettingModel settingModel = dataSet.get(position);
        String name = settingModel.getName();
            holder.getTv_name_setting().setText(name);
        //holder.getIv_photo_setting().setBackgroundColor(R.color.white);
        //holder.getIv_photo_setting().setBackgroundColor(R.color.white);
        holder.getIv_photo_setting().setImageDrawable(settingModel.getDrawable());
         holder.getCard_view_setting_list_one().setOnClickListener(v -> {
             if (name.equals(context.getResources().getString(R.string.profile))) {
                 NavigationUtils.goNewFragment(fragmentManager,new ProfileSettings());

             } else if (name.equals(context.getResources().getString(R.string.account))) {
                 NavigationUtils.goNewFragment(fragmentManager,new AccountSettings());

             }
         });
    }


    // Devolvemos el numero de items de nuestro arraylist, lo invoca automaticamente el layout manager
    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}