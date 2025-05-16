package com.example.buuktu.adapters;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buuktu.utils.NavigationUtils;
import com.example.buuktu.views.Note;
import com.example.buuktu.R;
import com.example.buuktu.dialogs.DeleteGeneralDialog;
import com.example.buuktu.models.NotekieModel;
import com.example.buuktu.views.MainActivity;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private final List<NotekieModel> notekieModels;
    private final MainActivity context;
    public NoteAdapter(MainActivity context, List<NotekieModel> notekieModels) {
        this.context = context;
        this.notekieModels = notekieModels;
    }

    public MainActivity getContext() {
        return context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.note_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.ViewHolder holder, int position) {
        NotekieModel item = notekieModels.get(position);
        holder.content.setText(item.getText());
        if (!item.getTitle().isEmpty()) {
            holder.title.setText(item.getTitle());
        } else {
            holder.title.setText("("+context.getString(R.string.untitled)+")");
            holder.title.setTextColor(context.getColor(R.color.greenWhatever)); // Cambia si prefieres un color directo
            Typeface cursiva = Typeface.createFromAsset(context.getAssets(), "Alegreya-Italic.ttf");
            holder.title.setTypeface(cursiva);
        }

        holder.cardView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("note_id", item.getUID());
            NavigationUtils.goNewFragmentWithBundle(bundle,context.getSupportFragmentManager(),new Note());
        });
        holder.cardView.setOnLongClickListener(v -> {
            View popupView = LayoutInflater.from(context).inflate(R.layout.menu_popup, null);
            PopupWindow popupWindow = new PopupWindow(popupView,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    true);

// Mostrarlo anclado al CardView
            popupWindow.showAsDropDown(holder.cardView, 0, -50);

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

    @Override
    public int getItemCount() {
        return notekieModels.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        final MaterialCardView cardView;
        final TextView content;
        final TextView title;
        MainActivity context;

        private MaterialCardView getCardView() {
            return cardView;
        }

        private TextView getContent() {
            return content;
        }

        private TextView getTitle() {
            return title;
        }

        private MainActivity getContext() {
            return context;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cv_note_item);
            title = itemView.findViewById(R.id.tv_title_note_item);
            content = itemView.findViewById(R.id.tv_content_note_item);
        }


    }
}
