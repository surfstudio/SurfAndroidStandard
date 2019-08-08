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
import android.view.ViewGroup;

import ru.surfstudio.android.easyadapter.EasyAdapter;
import ru.surfstudio.android.easyadapter.ItemList;
import ru.surfstudio.android.easyadapter.item.BaseItem;

/**
 * Base Controller for RecyclerView's item. It is used with {@link EasyAdapter} and {@link ItemList}.
 * <p>
 * Controller is responsible for creating uniqueness checking interface for the data
 * and to bind the data to a ViewHolder.
 *
 * @param <H> type of ViewHolder
 * @param <I> type of Item
 */
public abstract class BaseItemController<H extends RecyclerView.ViewHolder, I extends BaseItem> {

    public static final long NO_ID = RecyclerView.NO_ID;

    /**
     * Bind base item to holder
     *
     * @param holder holder to retrieve item
     * @param item   item to bind
     */
    public abstract void bind(H holder, I item);

    /**
     * Create holder inside parent
     *
     * @param parent parent, to which holder will attach
     */
    public abstract H createViewHolder(ViewGroup parent);

    public int viewType() {
        return getClass().getCanonicalName().hashCode();
    }

    /**
     * Get the unique id for item.
     * Method is used for automatically call notify... methods, see {@link EasyAdapter}.
     *
     * @param item item
     * @return unique id retrieved from item
     */
    public String getItemId(I item) {
        return String.valueOf(NO_ID);
    }

    /**
     * Get the item hashcode.
     * Method is used for automatically call notify... methods, see {@link EasyAdapter}
     *
     * @param item item
     * @return hashcode of the item
     */
    public abstract String getItemHash(I item);
}
