package ru.surfstudio.android.core.ui.base.recycler.controller;


import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import ru.surfstudio.android.core.ui.base.recycler.item.BaseItem;

public abstract class BaseItemController<H extends RecyclerView.ViewHolder, I extends BaseItem> {
    public static final long NO_ID = RecyclerView.NO_ID;

    public abstract void bind(H holder, I item);

    public abstract H createViewHolder(ViewGroup parent);

    public int viewType() {
        return getClass().getCanonicalName().hashCode();
    }

    public long getItemId(I item){
        return NO_ID;
    }

    public abstract long getItemHash(I item);
}
