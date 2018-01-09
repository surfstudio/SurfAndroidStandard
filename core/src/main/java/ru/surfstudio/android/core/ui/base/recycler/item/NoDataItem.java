package ru.surfstudio.android.core.ui.base.recycler.item;


import android.support.v7.widget.RecyclerView;

import ru.surfstudio.android.core.ui.base.recycler.controller.NoDataItemController;

public final class NoDataItem<H extends RecyclerView.ViewHolder>
        extends BaseItem<H> {

    public NoDataItem(NoDataItemController<H> itemController) {
        super(itemController);
    }

}
