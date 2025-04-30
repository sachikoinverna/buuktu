package com.example.buuktu.bottomsheet;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buuktu.R;
import com.example.buuktu.adapters.CardAdapter;
import com.example.buuktu.listeners.OnFieldDeletedListener;
import com.example.buuktu.models.FieldItem;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class BottomSheetChooseComponents extends BottomSheetDialogFragment {
    private CardAdapter adapter;
    private OnFieldDeletedListener listener;
    private List<FieldItem> fieldItems;

    public BottomSheetChooseComponents(Context context,
                                       ConstraintLayout constraintLayout,
                                       OnFieldDeletedListener listener,
                                       List<FieldItem> fieldItems) {
        this.listener = listener;
        this.fieldItems = fieldItems != null
                ? new ArrayList<>(fieldItems)
                : new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.bottomsheet_choose_components,
                container,
                false);

        RecyclerView rv = view.findViewById(
                R.id.recyclerViewBottomSheetChooseComponents);
        rv.setLayoutManager(new GridLayoutManager(getContext(), 2));

        adapter = new CardAdapter(getContext(), fieldItems,
                (fieldItem, position) -> {
                    Log.d("BottomSheet", "Eliminando posición: " + position);
                    if (listener != null) listener.onFieldDeleted(fieldItem);
                    adapter.removeItem(position);
                }
        );
        rv.setAdapter(adapter);
        return view;
    }
}
