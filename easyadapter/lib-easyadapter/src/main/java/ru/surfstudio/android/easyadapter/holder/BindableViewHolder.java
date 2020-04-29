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
package ru.surfstudio.android.easyadapter.holder;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.RecyclerView;

import ru.surfstudio.android.easyadapter.AnimatableViewHolder;
import ru.surfstudio.android.easyadapter.controller.BindableItemController;

/**
 * {@link androidx.recyclerview.widget.RecyclerView.ViewHolder} with binding and displaying data support.
 * <p>
 * To use with {@link BindableItemController}
 * <p>
 * This holder also has some convenient features, see {@link BaseViewHolder}
 *
 * @param <T> data type
 */
public abstract class BindableViewHolder<T> extends BaseViewHolder implements AnimatableViewHolder {

    public BindableViewHolder(ViewGroup parent, @LayoutRes int layoutRes) {
        super(parent, layoutRes);
    }

    public BindableViewHolder(View itemView) {
        super(itemView);
    }

    /**
     * Display data in ViewHolder
     * This method will be executed on each call to {@link androidx.recyclerview.widget.RecyclerView.Adapter#onBindViewHolder(RecyclerView.ViewHolder, int)}
     *
     * @param data data to display
     */
    public abstract void bind(T data);
}
