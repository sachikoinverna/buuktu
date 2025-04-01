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
import com.example.buuktu.models.NoteItem;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    // Modelo de datos, por ejemplo:
    private List<NoteItem> noteItems;
    private Context context;
    private NoteAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(NoteItem item);
    }

    public NoteAdapter(Context context, List<NoteItem> noteItems, NoteAdapter.OnItemClickListener listener) {
        this.context = context;
        this.noteItems = noteItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NoteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el layout del item que contiene un CardView
        View view = LayoutInflater.from(context).inflate(R.layout.note_item, parent, false);
        return new NoteAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.ViewHolder holder, int position) {
        NoteItem item = noteItems.get(position);
        holder.bind(item, listener);
    }

    @Override
    public int getItemCount() {
        return noteItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardView;
        TextView content;
        TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Asegúrate de que los IDs coincidan con los de tu layout XML (item_card.xml)
            cardView = itemView.findViewById(R.id.cv_note_item);
            title = itemView.findViewById(R.id.tv_title_note_item);
            content = itemView.findViewById(R.id.tv_content_note_item);
        }

        public void bind(final NoteItem item, final NoteAdapter.OnItemClickListener listener) {
            // Configurar los datos en el CardView
            content.setText(item.getContent());
            title.setText(item.getTitle());
            cardView.setOnClickListener(v -> listener.onItemClick(item));
        }
    }
}