package com.example.buuktu.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buuktu.InspoDesafios;
import com.example.buuktu.Notes;
import com.example.buuktu.R;
import com.example.buuktu.models.CardItem;
import com.example.buuktu.utils.NavigationUtils;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class CardInspoAdapter extends RecyclerView.Adapter<CardInspoAdapter.ViewHolder> {

    // Modelo de datos, por ejemplo:
    private final List<CardItem> dataSet;
    private final Context context;
    final FragmentManager fragmentManager;



    public CardInspoAdapter(Context context, List<CardItem> dataSet, FragmentManager fragmentManager) {
        this.context = context;
        this.dataSet = dataSet;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el layout del item que contiene un CardView
        View view = LayoutInflater.from(context).inflate(R.layout.card_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardInspoAdapter.ViewHolder holder, int position) {
        CardItem cardItem = dataSet.get(position);
        String text = cardItem.getText();
        holder.getIcon().setImageResource(cardItem.getIconResId());
        holder.getText().setText(text);

        holder.getCardView().setOnClickListener(v -> {
            if(text.equals(context.getResources().getString(R.string.notekies))){
                NavigationUtils.goNewFragment(fragmentManager,new Notes());
            } else if (text.equals(context.getResources().getString(R.string.challenges))) {
                NavigationUtils.goNewFragment(fragmentManager,new InspoDesafios());
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final MaterialCardView cardView;
        final ImageView icon;
        final TextView text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Asegúrate de que los IDs coincidan con los de tu layout XML (item_card.xml)
            cardView = itemView.findViewById(R.id.cv_bottomsheet_choose_components);
            icon = itemView.findViewById(R.id.iv_card_item_icon);
            text = itemView.findViewById(R.id.tv_card_item_name);
        }

        public MaterialCardView getCardView() {
            return cardView;
        }

        public ImageView getIcon() {
            return icon;
        }

        public TextView getText() {
            return text;
        }
    }
}

