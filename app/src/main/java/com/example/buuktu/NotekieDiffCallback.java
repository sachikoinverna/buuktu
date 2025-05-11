package com.example.buuktu;

import androidx.recyclerview.widget.DiffUtil;

import com.example.buuktu.models.NotekieModel;

import java.util.List;

public class NotekieDiffCallback extends DiffUtil.Callback {
    private final List<NotekieModel> oldList;
    private final List<NotekieModel> newList;

    public NotekieDiffCallback(List<NotekieModel> oldList, List<NotekieModel> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getUID().equals(newList.get(newItemPosition).getUID());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }
}
