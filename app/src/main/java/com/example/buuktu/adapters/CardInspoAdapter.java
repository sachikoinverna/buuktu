package com.example.buuktu.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buuktu.R;
import com.example.buuktu.models.FieldItem;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class CardInspoAdapter extends RecyclerView.Adapter<CardInspoAdapter.ViewHolder> {

    // Modelo de datos, por ejemplo:
    private List<FieldItem> fieldItems;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(FieldItem fieldItem, int position);
    }

    public CardInspoAdapter(Context context, List<FieldItem> fieldItems, OnItemClickListener listener) {
        this.context = context;
        this.fieldItems = fieldItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el layout del item que contiene un CardView
        View view = LayoutInflater.from(context).inflate(R.layout.card_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FieldItem fieldItem = fieldItems.get(position);
        holder.cardView.setOnClickListener(v  -> {
            if(listener != null) {
                listener.onItemClick(fieldItem, position);
            }
        });
        holder.bind(fieldItem, listener);
    }

    @Override
    public int getItemCount() {
        return fieldItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardView;
        ImageView icon;
        TextView text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Asegúrate de que los IDs coincidan con los de tu layout XML (item_card.xml)
            cardView = itemView.findViewById(R.id.cv_bottomsheet_choose_components);
            icon = itemView.findViewById(R.id.iv_card_item_icon);
            text = itemView.findViewById(R.id.tv_card_item_name);
        }

        public void bind(final FieldItem item, final OnItemClickListener listener) {
            // Configurar los datos en el CardView
            icon.setImageResource(item.getIcon());
            text.setText(item.getName());
         //   cardView.setOnClickListener(v -> listener.onItemClick(item));
        }
    }
}

