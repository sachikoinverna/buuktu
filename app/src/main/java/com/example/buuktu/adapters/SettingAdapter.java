package com.example.buuktu.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buuktu.R;
import com.example.buuktu.dialogs.CreateEditGeneralDialog;
import com.example.buuktu.dialogs.EditNamePronounsUserDialog;
import com.example.buuktu.dialogs.EditPasswordUserDialog;
import com.example.buuktu.models.SettingModel;
import com.example.buuktu.views.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;

public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.ViewHolder> {

    private final ArrayList<SettingModel> dataSet;
    private final Context context;
    final DocumentReference documentReference;
    final FirebaseAuth firebaseAuth;
    final String UID;
    EditNamePronounsUserDialog editNamePronounsUserDialog;
    final CreateEditGeneralDialog dialogCreateEdit;
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_name_setting_profile;
        private final TextView tv_value_setting_profile;
        private final CardView card_view_setting_list_profile;
        public ViewHolder(View view) {
            super(view);
            tv_value_setting_profile =  view.findViewById(R.id.tv_value_setting_profile);
            card_view_setting_list_profile= view.findViewById(R.id.card_view_setting_list_profile);
            tv_name_setting_profile = view.findViewById(R.id.tv_name_setting_profile);
        }

        public CardView getCard_view_setting_list_profile() {
            return card_view_setting_list_profile;
        }

        public TextView getTv_value_setting_profile() {
            return tv_value_setting_profile;
        }

        public TextView getTv_name_setting_profile() {
            return tv_name_setting_profile;
        }
    }

    public Context getContext() {
        return context;
    }

    //Constructor donde pasamos la lista de productos y el contexto
    public SettingAdapter(ArrayList<SettingModel> dataSet, MainActivity ctx, String UID) {
        this.dataSet = dataSet;
        this.context = ctx;
        firebaseAuth = FirebaseAuth.getInstance();
        this.UID = UID;
        documentReference = ctx.getCollectionUsers().document(UID);
        dialogCreateEdit = new CreateEditGeneralDialog(context);
    }


    //Se llama cada vez que se hace scroll en la pantalla y los elementos desaparecen y aparecen
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        //Creamos la vista de cada item a partir de nuestro layout
           View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.settings_profile_list_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SettingAdapter.ViewHolder holder, int position) {
        SettingModel settingModel = dataSet.get(position);
        String name = settingModel.getName();
        String value = settingModel.getValue();
        if (!name.equals(context.getResources().getString(R.string.user_password))) {
            holder.getTv_value_setting_profile().setText(value);
        } else {
            holder.getTv_value_setting_profile().setText("••••••");
        }

        holder.getTv_name_setting_profile().setText(name);
         holder.getCard_view_setting_list_profile().setOnClickListener(v -> {
             if (name.equals(context.getResources().getString(R.string.name)) || name.equals(context.getResources().getString(R.string.pronouns)) || name.equals(context.getResources().getString(R.string.email))) {
                 editNamePronounsUserDialog = new EditNamePronounsUserDialog(v.getContext(), name, value, documentReference);
                 editNamePronounsUserDialog.show();
             } else if (name.equals(context.getResources().getString(R.string.user_password))) {

             EditPasswordUserDialog editPasswordUserDialog = new EditPasswordUserDialog(context);
                 editPasswordUserDialog.show();
             }
         });
    }

    // Devolvemos el numero de items de nuestro arraylist, lo invoca automaticamente el layout manager
    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}