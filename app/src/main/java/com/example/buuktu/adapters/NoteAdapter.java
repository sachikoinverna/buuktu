package com.example.buuktu.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buuktu.NotekieDiffCallback;
import com.example.buuktu.R;
import com.example.buuktu.dialogs.DeleteGeneralDialog;
import com.example.buuktu.models.NotekieModel;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private final List<NotekieModel> notekieModels;
    private final Context context;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(NotekieModel item);
    }

    public NoteAdapter(Context context, List<NotekieModel> notekieModels, OnItemClickListener listener) {
        this.context = context;
        this.notekieModels = notekieModels;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NoteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.note_item, parent, false);
        return new NoteAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.ViewHolder holder, int position) {
        NotekieModel item = notekieModels.get(position);
        holder.bind(item, listener);
    }

    @Override
    public int getItemCount() {
        return notekieModels.size();
    }

    public void updateList(List<NotekieModel> newItems) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new NotekieDiffCallback(this.notekieModels, newItems));
        this.notekieModels.clear();
        this.notekieModels.addAll(newItems);
        diffResult.dispatchUpdatesTo(this);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final MaterialCardView cardView;
        final TextView content;
        final TextView title;
        final ImageButton ib_option_note_item;
        final ImageButton ib_delete_note_item;
        final FirebaseFirestore db;
        final CollectionReference collectionNotekies;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cv_note_item);
            title = itemView.findViewById(R.id.tv_title_note_item);
            content = itemView.findViewById(R.id.tv_content_note_item);
            ib_option_note_item = itemView.findViewById(R.id.ib_option_note_item);
            ib_delete_note_item = itemView.findViewById(R.id.ib_delete_note_item);
            db = FirebaseFirestore.getInstance();
            collectionNotekies = db.collection("Notekies");
        }

        @SuppressLint("ResourceAsColor")
        public void bind(final NotekieModel item, final OnItemClickListener listener) {
            content.setText(item.getContent());
            if (!item.getTitle().isEmpty()) {
                title.setText(item.getTitle());
            } else {
                title.setText("(Sin título)");
                title.setTextColor(R.color.greenWhatever); // Cambia si prefieres un color directo
                Typeface cursiva = Typeface.createFromAsset(itemView.getContext().getAssets(), "Alegreya-Italic.ttf");
                title.setTypeface(cursiva);
            }

            cardView.setOnClickListener(v -> listener.onItemClick(item));


            ib_delete_note_item.setOnClickListener(v -> {
                DeleteGeneralDialog dialog = new DeleteGeneralDialog(
                        v.getContext(),"notekie", item.getUID());
                dialog.show();
            });
        }


    }
}
