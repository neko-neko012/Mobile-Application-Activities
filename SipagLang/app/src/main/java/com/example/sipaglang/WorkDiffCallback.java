package com.example.sipaglang;

import androidx.recyclerview.widget.DiffUtil;
import java.util.List;

public class WorkDiffCallback extends DiffUtil.Callback {
    private final List<WorkEntity> oldList;
    private final List<WorkEntity> newList;

    public WorkDiffCallback(List<WorkEntity> oldList, List<WorkEntity> newList) {
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
        return oldList.get(oldItemPosition).getId() == newList.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }
}

