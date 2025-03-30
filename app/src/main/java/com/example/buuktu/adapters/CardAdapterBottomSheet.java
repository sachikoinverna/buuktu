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
import com.example.buuktu.models.CardItem;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class CardAdapterBottomSheet extends RecyclerView.Adapter<CardAdapterBottomSheet.ViewHolder> {

    // Modelo de datos, por ejemplo:
    private List<CardItem> cardItems;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(CardItem item);
    }

    public CardAdapterBottomSheet(Context context, List<CardItem> cardItems, OnItemClickListener listener) {
        this.context = context;
        this.cardItems = cardItems;
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
        CardItem item = cardItems.get(position);
        holder.bind(item, listener);
    }

    @Override
    public int getItemCount() {
        return cardItems.size();
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

        public void bind(final CardItem item, final OnItemClickListener listener) {
            // Configurar los datos en el CardView
            icon.setImageResource(item.getIconResId());
            text.setText(item.getText());
            cardView.setOnClickListener(v -> listener.onItemClick(item));
        }
    }
}

