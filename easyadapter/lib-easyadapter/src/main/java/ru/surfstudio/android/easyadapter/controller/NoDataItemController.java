/*
  Copyright (c) 2018-present, SurfStudio LLC, Maxim Tuev.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package ru.surfstudio.android.easyadapter.controller;

import androidx.recyclerview.widget.RecyclerView;

import ru.surfstudio.android.easyadapter.EasyAdapter;
import ru.surfstudio.android.easyadapter.ItemList;
import ru.surfstudio.android.easyadapter.item.BaseItem;
import ru.surfstudio.android.easyadapter.item.NoDataItem;

/**
 * Controller for RecyclerView's item without data {@link NoDataItem}.
 * It is used with {@link EasyAdapter} and {@link ItemList}.
 * <p>
 * There should be only one NoDataItemController in the list of items.
 * If you need to have more of them, you must override {@link #getItemId(NoDataItem)}
 * and write your custom logic of unique controller identification.
 *
 * @param <H> type of ViewHolder
 * @see BaseItemController
 */
public abstract class NoDataItemController<H extends RecyclerView.ViewHolder>
        extends BaseItemController<H, NoDataItem<H>> {

    @Override
    public String getItemId(NoDataItem<H> item) {
        return getTypeStringHashCode();
    }

    /**
     * Bind item to holder. Empty, because item simply contains no data.
     *
     * @param holder holder to retrieve item
     * @param item   item to bind
     */
    @Override
    public void bind(H holder, NoDataItem<H> item) {
        //empty
    }

    /**
     * Method always returns the same value because item has no data
     *
     * @param item noDataItem
     * @return hashCode
     * @see BaseItemController#getItemHash(BaseItem)
     */
    @Override
    public String getItemHash(NoDataItem<H> item) {
        return "0";
    }
}
