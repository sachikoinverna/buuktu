package com.example.buuktu.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buuktu.Notes;
import com.example.buuktu.R;
import com.example.buuktu.dialogs.PeriodNumbersDialog;
import com.example.buuktu.dialogs.PeriodWordsDialog;
import com.example.buuktu.models.CardItem;
import com.example.buuktu.views.Inspo;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class CardInspoDesafiosAdapter extends RecyclerView.Adapter<CardInspoDesafiosAdapter.ViewHolder> implements View.OnClickListener {

    // Modelo de datos, por ejemplo:
    private List<CardItem> dataSet;
    private Context context;
    FragmentManager fragmentManager;
    @Override
    public void onClick(View v) {

    }


    public CardInspoDesafiosAdapter(Context context, List<CardItem> dataSet, FragmentManager fragmentManager) {
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
    public void onBindViewHolder(@NonNull CardInspoDesafiosAdapter.ViewHolder holder, int position) {
        String text = dataSet.get(position).getText();
        holder.getIcon().setImageResource(dataSet.get(position).getIconResId());
        holder.getText().setText(text);
        holder.getCardView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(text.equals("Wordkie of the day")){
                    PeriodWordsDialog periodWordsDialog = new PeriodWordsDialog(v.getContext());
                    periodWordsDialog.show();
                } else if (text.equals("Numberkie of the day")) {
                    PeriodNumbersDialog periodNumbersDialog = new PeriodNumbersDialog(v.getContext());
                    periodNumbersDialog.show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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

