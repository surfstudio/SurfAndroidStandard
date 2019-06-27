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
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder;
import ru.surfstudio.android.easyadapter.item.BindableItem;

/**
 * Controller for RecyclerView's item.
 * It is used with {@link EasyAdapter} and {@link ItemList}.
 *
 * @param <H> type of ViewHolder
 * @param <T> type of data
 * @see BindableItem
 * @see BaseItemController
 */
public abstract class BindableItemController<T, H extends BindableViewHolder<T>>
        extends BaseItemController<H, BindableItem<T, H>> {


    /**
     * Bind item with data to holder
     *
     * @param holder holder to retrieve item
     * @param item   item to bind
     */
    @Override
    public final void bind(H holder, BindableItem<T, H> item) {
        bind(holder, item.getData());
    }

    /**
     * Bind data to holder
     *
     * @param holder holder to retrieve item
     * @param data   data to bind
     */
    public void bind(H holder, T data) {
        holder.bind(data);
    }

    @Override
    public final String getItemId(BindableItem<T, H> item) {
        return getItemId(item.getData());
    }

    @Override
    public final String getItemHash(BindableItem<T, H> item) {
        return getItemHash(item.getData());
    }

    /**
     * Get the unique id for data.
     * Method is used for automatically call notify... methods, see {@link EasyAdapter}.
     *
     * @param data data
     * @return unique id retrieved from data
     */
    protected abstract String getItemId(T data);

    /**
     * Get the data hashcode.
     * Method is used for automatically call notify... methods, see {@link EasyAdapter}
     *
     * @param data data
     * @return hashcode of the data
     */
    protected String getItemHash(T data) {
        return String.valueOf(data == null ? 0 : data.hashCode());
    }
}
