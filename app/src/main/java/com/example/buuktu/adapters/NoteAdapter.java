package com.example.buuktu.adapters;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.graphics.YuvImage;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.buuktu.NotekieDiffCallback;
import com.example.buuktu.R;
import com.example.buuktu.dialogs.DeleteNotekieDialog;
import com.example.buuktu.models.NoteItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private List<NoteItem> noteItems;
    private final Context context;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(NoteItem item);
    }

    public NoteAdapter(Context context, List<NoteItem> noteItems, OnItemClickListener listener) {
        this.context = context;
        this.noteItems = noteItems;
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
        NoteItem item = noteItems.get(position);
        holder.bind(item, listener);
    }

    @Override
    public int getItemCount() {
        return noteItems.size();
    }

    public void updateList(List<NoteItem> newItems) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new NotekieDiffCallback(this.noteItems, newItems));
        this.noteItems.clear();
        this.noteItems.addAll(newItems);
        diffResult.dispatchUpdatesTo(this);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardView;
        TextView content, title;
        ImageButton ib_option_note_item, ib_delete_note_item;
        FirebaseFirestore db;
        CollectionReference collectionNotekies;

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

        public void bind(final NoteItem item, final OnItemClickListener listener) {
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

            ib_option_note_item.setOnClickListener(v -> {
                // Acción para opciones del ítem (editar, compartir, etc.)
            });

            ib_delete_note_item.setOnClickListener(v -> {
                DeleteNotekieDialog dialog = new DeleteNotekieDialog(itemView.getContext());
                dialog.setOnDialogClickListener(new DeleteNotekieDialog.OnDialogDelClickListener() {
                    @Override
                    public void onAccept() {
                        deleteNoteItem(item, dialog);
                    }

                    @Override
                    public void onCancel() {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            });
        }

        private void deleteNoteItem(NoteItem item, DeleteNotekieDialog dialog) {
            TextView tv_title = dialog.findViewById(R.id.tv_lvl_title_del);
            tv_title.setText("Cuidado");
            TextView tv_text = dialog.findViewById(R.id.tv_lvl_text_del);
            tv_text.setText("Cuidado");

            ImageView iv_photo = dialog.findViewById(R.id.iv_photo_del);
            iv_photo.setImageAlpha(R.mipmap.img_del_characterkie);
            ImageButton ib_close = dialog.findViewById(R.id.ib_close_dialog);
            ImageButton ib_accept = dialog.findViewById(R.id.ib_accept_dialog);
            LottieAnimationView animationView = dialog.findViewById(R.id.anim_del_notekie);

            tv_title.setVisibility(View.GONE);
            tv_text.setVisibility(View.GONE);
            iv_photo.setVisibility(View.GONE);
            ib_close.setVisibility(View.GONE);
            ib_accept.setVisibility(View.GONE);
            animationView.setVisibility(View.VISIBLE);
            animationView.setAnimation(R.raw.reading_anim);
            animationView.playAnimation();

            collectionNotekies.document(item.getUID()).delete()
                    .addOnSuccessListener(unused -> {
                        animationView.setAnimation(R.raw.success_anim);
                        animationView.playAnimation();
                        Completable.timer(5, TimeUnit.SECONDS)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(() -> {
                                    animationView.setVisibility(View.GONE);
                                    dialog.dismiss();
                                });
                    })
                    .addOnFailureListener(e -> {
                        animationView.setAnimation(R.raw.fail_anim);
                        animationView.playAnimation();
                        Completable.timer(5, TimeUnit.SECONDS)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(() -> {
                                    animationView.setVisibility(View.GONE);
                                    dialog.dismiss();
                                });
                    });
        }
    }
}
