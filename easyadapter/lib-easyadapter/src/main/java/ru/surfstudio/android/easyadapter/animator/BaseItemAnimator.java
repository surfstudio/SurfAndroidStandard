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
package ru.surfstudio.android.easyadapter.animator;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import ru.surfstudio.android.easyadapter.AnimatableViewHolder;
import ru.surfstudio.android.easyadapter.holder.BaseViewHolder;

/**
 * ItemAnimator with support custom animations for ViewHolders, see {@link BaseViewHolder}
 */
public class BaseItemAnimator extends DefaultItemAnimator {

    @Override
    public final void onRemoveStarting(RecyclerView.ViewHolder item) {
        super.onRemoveStarting(item);
        if (!(item instanceof AnimatableViewHolder) || !((AnimatableViewHolder) item).animateRemove()) {
            onRemoveStartingInternal(item);
        }
    }

    @Override
    public final void onAddStarting(RecyclerView.ViewHolder item) {
        super.onAddStarting(item);
        if (!(item instanceof AnimatableViewHolder) || !((AnimatableViewHolder) item).animateInsert()) {
            onAddStartingInternal(item);
        }
    }

    @Override
    public final void onChangeStarting(RecyclerView.ViewHolder item, boolean oldItem) {
        super.onChangeStarting(item, oldItem);
        if (!(item instanceof AnimatableViewHolder) || !((AnimatableViewHolder) item).animateChange()) {
            onChangeStartingInternal(item);
        }
    }

    /**
     * Called when a remove animation is being started on the given ViewHolder.
     *
     * @param item the ViewHolder being animated
     *
     * @see #onRemoveStarting(RecyclerView.ViewHolder)}
     */
    protected void onRemoveStartingInternal(RecyclerView.ViewHolder item) {
        //empty
    }

    /**
     * Called when an add animation is being started on the given ViewHolder.
     *
     * @param item the ViewHolder being animated
     *
     * @see #onRemoveStarting(RecyclerView.ViewHolder)}
     */
    protected void onAddStartingInternal(RecyclerView.ViewHolder item) {
        //empty
    }

    /**
     * Called when a change animation is being started on the given ViewHolder.
     *
     * @param item the ViewHolder being animated
     *
     * @see #onRemoveStarting(RecyclerView.ViewHolder)}
     */
    protected void onChangeStartingInternal(RecyclerView.ViewHolder item) {
        //empty
    }
}
