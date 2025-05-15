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
import com.example.buuktu.dialogs.PeriodNumbersDialog;
import com.example.buuktu.dialogs.PeriodWordsDialog;
import com.example.buuktu.models.CardItem;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class CardInspoDesafiosAdapter extends RecyclerView.Adapter<CardInspoDesafiosAdapter.ViewHolder> {

    private final List<CardItem> dataSet;
    private final Context context;


    public CardInspoDesafiosAdapter(Context context, List<CardItem> dataSet) {
        this.context = context;
        this.dataSet = dataSet;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardInspoDesafiosAdapter.ViewHolder holder, int position) {
        CardItem cardItem = dataSet.get(position);
        holder.getIcon().setImageResource(cardItem.getIconResId());

        holder.getText().setText(cardItem.getText());
        holder.getCardView().setOnClickListener(v -> {
            if(cardItem.getText().equals("Wordkie of the day")){
                PeriodWordsDialog periodWordsDialog = new PeriodWordsDialog(v.getContext());
                periodWordsDialog.show();
            } else if (cardItem.getText().equals("Numberkie of the day")) {
                PeriodNumbersDialog periodNumbersDialog = new PeriodNumbersDialog(v.getContext());
                periodNumbersDialog.show();
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

