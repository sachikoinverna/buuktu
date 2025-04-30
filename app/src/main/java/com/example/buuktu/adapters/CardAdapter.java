package com.example.buuktu.adapters;

import android.content.Context;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private final List<FieldItem> items = new ArrayList<>();
    private final Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(FieldItem fieldItem, int position);
    }

    public CardAdapter(Context context, List<FieldItem> initialItems, OnItemClickListener listener) {
        this.context = context;
        this.listener = listener;
        setItems(initialItems);
    }

    public void setItems(List<FieldItem> newItems) {
        items.clear();
        if (newItems != null) items.addAll(newItems);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        if (position >= 0 && position < items.size()) {
            items.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, items.size() - position);
        } else {
            Log.e("CardAdapter", "removeItem(): posición inválida " + position);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.card_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FieldItem item = items.get(position);
        holder.bind(item, context);
        holder.cardView.setOnClickListener(v -> {
            if (listener != null) {
                int pos = holder.getAdapterPosition(); // getAdapterPosition() compatible
                if (pos != RecyclerView.NO_POSITION) {
                    listener.onItemClick(item, pos);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardView;
        ImageView icon;
        TextView text;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cv_bottomsheet_choose_components);
            icon = itemView.findViewById(R.id.iv_card_item_icon);
            text = itemView.findViewById(R.id.tv_card_item_name);
        }

        void bind(FieldItem item, Context context) {
            String name = item.getName();
            String iconName = item.getIcon();
            if (iconName != null && !iconName.isEmpty()) {
                int resId = context.getResources().getIdentifier(
                        iconName, "drawable", context.getPackageName());
                if (resId != 0) icon.setImageResource(resId);
                else icon.setImageResource(R.drawable.twotone_email_24);
            } else {
                icon.setImageResource(R.drawable.twotone_email_24);
            }
            text.setText(name != null ? name : "");
        }
    }
}