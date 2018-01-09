package ru.surfstudio.android.core.ui.base.recycler;


import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ru.surfstudio.android.core.ui.base.recycler.controller.BaseItemController;
import ru.surfstudio.android.core.ui.base.recycler.controller.BindableItemController;
import ru.surfstudio.android.core.ui.base.recycler.item.BaseItem;

public class EasyAdapter extends RecyclerView.Adapter {

    protected List<BaseItem> items = new ArrayList<>();
    private List<ItemInfo> lastItemsInfo = new ArrayList<>();
    private SparseArray<BaseItemController> supportedItemControllers = new SparseArray<>();
    private boolean autoNotifyOnSetItemsEnabled = true;

    public EasyAdapter() {
        setHasStableIds(false);
    }

    public void setAutoNotifyOnSetItemsEnabled(boolean enableAutoNotifyOnSetItems) {
        this.autoNotifyOnSetItemsEnabled = enableAutoNotifyOnSetItems;
    }

    @Override
    public final int getItemViewType(int position) {
        return items.get(position).getItemController().viewType();
    }

    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return supportedItemControllers.get(viewType).createViewHolder(parent);
    }

    @Override
    public final void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BaseItem item = items.get(position);
        item.getItemController().bind(holder, item);
    }

    @Override
    public final int getItemCount() {
        return items.size();
    }

    public final <T> void setData(Collection<T> data, BindableItemController<T, ? extends RecyclerView.ViewHolder> itemController){
        setItems(ItemList.create(data, itemController));
    }

    public void setItems(ItemList items) {
        this.items.clear();
        this.items.addAll(items);
        updateSupportedItemControllers(this.items);
        if(autoNotifyOnSetItemsEnabled) {
            autoNotify();
        }
    }

    private void updateSupportedItemControllers(List<BaseItem> items) {
        supportedItemControllers.clear();
        for(BaseItem item : items){
            BaseItemController itemController = item.getItemController();
            supportedItemControllers.put(itemController.viewType(), itemController);
        }
    }

    @Override
    public final long getItemId(int position){
        BaseItem item = items.get(position);
        return item.getItemController().getItemId(item);
    }

    public final long getItemHash(int position){
        BaseItem item = items.get(position);
        return item.getItemController().getItemHash(item);
    }

    public void autoNotify() {
        final List<ItemInfo> newItemInfo = extractItemInfo();
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(
                new AutoNotifyDiffCallback(lastItemsInfo, newItemInfo));
        diffResult.dispatchUpdatesTo(this);
        lastItemsInfo = newItemInfo;
    }

    private List<ItemInfo> extractItemInfo() {
        int itemCount = getItemCount();
        List<ItemInfo> currentItemsInfo = new ArrayList<>(itemCount);
        for(int i = 0; i < itemCount; i++){
            currentItemsInfo.add(new ItemInfo(
                    getItemId(i),
                    getItemHash(i)));
        }
        return currentItemsInfo;
    }

    private class ItemInfo {
        private long id;
        private long hash;

        ItemInfo(long id, long hash) {
            this.id = id;
            this.hash = hash;
        }

        long getId() {
            return id;
        }

        long getHash() {
            return hash;
        }
    }

    private class AutoNotifyDiffCallback extends DiffUtil.Callback {

        private final List<ItemInfo> lastItemsInfo;
        private final List<ItemInfo> newItemsInfo;

        AutoNotifyDiffCallback(List<ItemInfo> lastItemsInfo, List<ItemInfo> newItemsInfo) {
            this.lastItemsInfo = lastItemsInfo;
            this.newItemsInfo = newItemsInfo;
        }

        @Override
        public int getOldListSize() {
            return lastItemsInfo.size();
        }

        @Override
        public int getNewListSize() {
            return newItemsInfo.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return lastItemsInfo.get(oldItemPosition).getId() ==
                    newItemsInfo.get(newItemPosition).getId();
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return lastItemsInfo.get(oldItemPosition).getHash() ==
                    newItemsInfo.get(newItemPosition).getHash();
        }
    }
}
