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

import ru.surfstudio.android.easyadapter.EasyAdapter;
import ru.surfstudio.android.easyadapter.ItemList;
import ru.surfstudio.android.easyadapter.holder.DoubleBindableViewHolder;
import ru.surfstudio.android.easyadapter.item.DoubleBindableItem;

/**
 * Controller for RecyclerView's item with two blocks of data.
 * It is used with {@link EasyAdapter} and {@link ItemList}.
 *
 * @param <H>  type of ViewHolder
 * @param <T1> first data type
 * @param <T2> second data type
 * @see ru.surfstudio.android.easyadapter.item.DoubleBindableItem
 * @see BaseItemController
 */
public abstract class DoubleBindableItemController<T1, T2, H extends DoubleBindableViewHolder<T1, T2>>
        extends BaseItemController<H, DoubleBindableItem<T1, T2, H>> {

    /**
     * Bind item with two blocks of data to holder
     *
     * @param holder holder to retrieve item
     * @param item   item to bind
     */
    @Override
    public final void bind(H holder, DoubleBindableItem<T1, T2, H> item) {
        bind(holder, item.getFirstData(), item.getSecondData());
    }

    /**
     * Bind two blocks of data data to holder
     *
     * @param holder     holder to retrieve item
     * @param firstData  first data to bind
     * @param secondData second data to bind
     */
    public void bind(H holder, T1 firstData, T2 secondData) {
        holder.bind(firstData, secondData);
    }

    @Override
    public final Object getItemId(DoubleBindableItem<T1, T2, H> item) {
        return getItemId(item.getFirstData(), item.getSecondData());
    }

    @Override
    public final Object getItemHash(DoubleBindableItem<T1, T2, H> item) {
        return getItemHash(item.getFirstData(), item.getSecondData());
    }

    /**
     * Get the unique id for two blocks of data
     * Method is used for automatically call notify... methods, see {@link EasyAdapter}.
     *
     * @param firstData  first data
     * @param secondData second data
     * @return unique id retrieved from two blocks of data
     */
    protected abstract Object getItemId(T1 firstData, T2 secondData);


    /**
     * Get the data hashcode.
     * Method is used for automatically call notify... methods, see {@link EasyAdapter}
     *
     * @param firstData  first data
     * @param secondData second data
     * @return hashcode of data
     */
    protected Object getItemHash(T1 firstData, T2 secondData) {
        return firstData == null ? 0 : firstData.hashCode() +
                        (secondData == null ? 0 : secondData.hashCode());
    }
}
