package ru.surfstudio.android.core.ui.base.recycler.controller;


import ru.surfstudio.android.core.ui.base.recycler.holder.BindableViewHolder;
import ru.surfstudio.android.core.ui.base.recycler.item.BindableItem;

public abstract class BindableItemController<T, H extends BindableViewHolder<T>>
        extends BaseItemController<H, BindableItem<T, H>> {

    @Override
    public final void bind(H holder, BindableItem<T, H> item) {
        bind(holder, item.getData());
    }

    public void bind(H holder, T data) {
        holder.bind(data);
    }

    @Override
    public final long getItemId(BindableItem<T, H> item) {
        return getItemId(item.getData());
    }

    @Override
    public final long getItemHash(BindableItem<T, H> item) {
        return getItemHash(item.getData());
    }

    public long getItemId(T data){
        return NO_ID;
    }

    public long getItemHash(T data){
        return data.hashCode();
    }
}
