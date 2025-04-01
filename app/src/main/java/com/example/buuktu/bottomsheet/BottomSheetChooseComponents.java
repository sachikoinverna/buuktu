package com.example.buuktu.bottomsheet;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buuktu.R;
import com.example.buuktu.adapters.CardAdapter;
import com.example.buuktu.listeners.OnFieldDeletedListener;
import com.example.buuktu.models.CardItem;
import com.example.buuktu.models.FieldItem;
import com.example.buuktu.utils.ComponentsUtils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class BottomSheetChooseComponents extends BottomSheetDialogFragment {
    private RecyclerView recyclerView;
    private CardAdapter adapter;
    private Context context;
    private ConstraintLayout constraintLayout;
    private OnFieldDeletedListener listener; // Agregar el listener
    List<FieldItem> fieldItems;
    public BottomSheetChooseComponents(Context context, ConstraintLayout constraintLayout, OnFieldDeletedListener listener, List<FieldItem> fieldItems) {
        this.context = context;
        this.constraintLayout = constraintLayout;
        this.listener = listener; // Inicializar el listener
        this.fieldItems = fieldItems; // Inicializar la lista de items
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottomsheet_choose_components, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewBottomSheetChooseComponents);

        // Configurar RecyclerView con GridLayoutManager (2 columnas)
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        // Configurar el adaptador
        if (fieldItems == null) {
            fieldItems = new ArrayList<>(); // Evita que sea nulo
        }
        adapter = new CardAdapter(getContext(), fieldItems, new CardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(FieldItem fieldItem, int position) {
                if (listener != null) {
                    listener.onFieldDeleted(fieldItem); // Notifica a CreateCharacterkie
                }
                fieldItems.remove(position);
                adapter.notifyItemRemoved(position);
            }
        });
        recyclerView.setAdapter(adapter);

        return view;
    }
      //  Button bt_add_pronouns_characterkie_create = v.findViewById(R.id.bt_add_pronouns_characterkie_create);
       // Button bt_add_status_characterkie_create = v.findViewById(R.id.bt_add_status_characterkie_create);
      //  Button img_def = v.findViewById(R.id.ib_default);

       /* if (img_one != null) {
            img_one.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Imagen seleccionada", Toast.LENGTH_SHORT).show();
                }
            });
        }*/

      /*  if (bt_add_pronouns_characterkie_create != null) {
            bt_add_pronouns_characterkie_create.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Galeria seleccionada", Toast.LENGTH_SHORT).show();
                    //selectImageGallery();
                }
            });
        }

        if (bt_add_status_characterkie_create != null) {
            bt_add_status_characterkie_create.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Imagenes default seleccionada", Toast.LENGTH_SHORT).show();
                    //dialog2.show();
                }
            });
        }
        return v;
    }*/

}
