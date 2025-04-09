package com.example.buuktu.adapters;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buuktu.R;
import com.example.buuktu.models.CardItem;
import com.example.buuktu.models.NoteItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    // Modelo de datos, por ejemplo:
    private List<NoteItem> noteItems;
    private OnItemClickListener listener;
    private FragmentManager fragmentManager;

    private Context context;
  //  private Fragment menuWorldkie;
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
        // Inflar el layout del item que contiene un CardView
        View view = LayoutInflater.from(context).inflate(R.layout.note_item, parent, false);
        return new NoteAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.ViewHolder holder, int position) {
        NoteItem item = noteItems.get(position);
        holder.cardView.setOnClickListener(v  -> {
            if(listener != null) {
                listener.onItemClick(item);
            }
        });
        holder.bind(item, listener);
    }

    @Override
    public int getItemCount() {
        return noteItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardView;
        TextView content,title;
        ImageButton ib_option_note_item,ib_delete_note_item;
        FirebaseFirestore db;
        CollectionReference collectionNotekies;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Asegúrate de que los IDs coincidan con los de tu layout XML (item_card.xml)
            cardView = itemView.findViewById(R.id.cv_note_item);
            title = itemView.findViewById(R.id.tv_title_note_item);
            content = itemView.findViewById(R.id.tv_content_note_item);
            ib_option_note_item = itemView.findViewById(R.id.ib_option_note_item);
            ib_delete_note_item = itemView.findViewById(R.id.ib_delete_note_item);
            db = FirebaseFirestore.getInstance();
            collectionNotekies = db.collection("Notekies");
        }

        public void bind(final NoteItem item, final NoteAdapter.OnItemClickListener listener) {
            // Configurar los datos en el CardView
            content.setText(item.getContent());
            if(!item.getTitle().isEmpty()) {
                title.setText(item.getTitle());
            }else{
                title.setText("(Sin titulo)");
                title.setTextColor(R.color.greenWhatever);
                AssetManager assetManager = itemView.getContext().getAssets();

                Typeface cursiva = Typeface.createFromAsset(assetManager, "Alegreya-Italic.ttf");
                title.setTypeface(cursiva);
            }
            cardView.setOnClickListener(v -> listener.onItemClick(item));
            ib_option_note_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // context.g
                }
            });
            ib_delete_note_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                           collectionNotekies.document(item.getUID()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {

                               @Override
                               public void onSuccess(Void unused) {

                               }
                           }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                     //   }
                   // });
                }
            });
        }
    }
}