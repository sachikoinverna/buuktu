package com.example.buuktu.adapters;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buuktu.views.NotekieDiffCallback;
import com.example.buuktu.R;
import com.example.buuktu.dialogs.DeleteGeneralDialog;
import com.example.buuktu.models.NotekieModel;
import com.example.buuktu.views.MainActivity;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private final List<NotekieModel> notekieModels;
    private final MainActivity context;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(NotekieModel item);
    }

    public NoteAdapter(MainActivity context, List<NotekieModel> notekieModels, OnItemClickListener listener) {
        this.context = context;
        this.notekieModels = notekieModels;
        this.listener = listener;
    }

    public MainActivity getContext() {
        return context;
    }

    @NonNull
    @Override
    public NoteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.note_item, parent, false);
        return new NoteAdapter.ViewHolder(view,context);
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
        final FirebaseFirestore db;
        final CollectionReference collectionNotekies;
        MainActivity context;

        public ViewHolder(@NonNull View itemView, MainActivity context) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cv_note_item);
            title = itemView.findViewById(R.id.tv_title_note_item);
            content = itemView.findViewById(R.id.tv_content_note_item);
            db = FirebaseFirestore.getInstance();
            collectionNotekies = db.collection("Notekies");
            this.context=context;
        }

        @SuppressLint("ResourceAsColor")
        public void bind(final NotekieModel item, final OnItemClickListener listener) {
            content.setText(item.getText());
            if (!item.getTitle().isEmpty()) {
                title.setText(item.getTitle());
            } else {
                title.setText("("+context.getString(R.string.untitled)+")");
                title.setTextColor(R.color.greenWhatever); // Cambia si prefieres un color directo
                Typeface cursiva = Typeface.createFromAsset(itemView.getContext().getAssets(), "Alegreya-Italic.ttf");
                title.setTypeface(cursiva);
            }

            cardView.setOnClickListener(v -> listener.onItemClick(item));
            cardView.setOnLongClickListener(v -> {
                View popupView = LayoutInflater.from(itemView.getContext()).inflate(R.layout.menu_popup, null);
                PopupWindow popupWindow = new PopupWindow(popupView,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        true);

// Opcional: animación y sombra
                popupWindow.setElevation(8f);

// Mostrarlo anclado al CardView
                popupWindow.showAsDropDown(cardView, 0, -50);

// Listeners
                popupView.findViewById(R.id.bt_edit_item).setVisibility(View.GONE);

                popupView.findViewById(R.id.bt_del_item).setOnClickListener(view2 -> {
                    DeleteGeneralDialog dialog = new DeleteGeneralDialog(
                            context, "notekie", item.getUID());
                    dialog.show();
                    popupWindow.dismiss();
                });
                return true;
            });
        }


    }
}
