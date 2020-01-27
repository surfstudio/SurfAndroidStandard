/*
  Copyright (c) 2018-present, SurfStudio LLC. Oleg Zhilo

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

package ru.surfstudio.android.recycler.decorator

import ru.surfstudio.android.recycler.decorator.base.OffsetDecor
import ru.surfstudio.android.recycler.decorator.base.RecyclerViewDecor
import ru.surfstudio.android.recycler.decorator.base.ViewHolderDecor
import ru.surfstudio.android.recycler.decorator.draw.DecorDrawer
import ru.surfstudio.android.recycler.decorator.draw.Decorator
import ru.surfstudio.android.recycler.decorator.draw.DecorsBridge
import ru.surfstudio.android.recycler.decorator.draw.ScopeBuilder

/**
 * Builder for [androidx.recyclerview.widget.RecyclerView.ItemDecoration]
 */
class Builder {

    /**
     * Scope of underlay decors for ViewHolder
     */
    private var underlayViewHolderScope: ScopeBuilder<DecorDrawer<ViewHolderDecor>> =
        ScopeBuilder()

    /**
     * Scope of overlay decors for ViewHolder
     */
    private var overlayViewHolderScope: ScopeBuilder<DecorDrawer<ViewHolderDecor>> =
        ScopeBuilder()

    /**
     * Scope of underlay decors for RecyclerView
     */
    private var underlayRecyclerScope: ScopeBuilder<RecyclerViewDecor> =
        ScopeBuilder()

    /**
     * Scope of overlay decors for RecyclerView
     */
    private var overlayRecyclerScope: ScopeBuilder<RecyclerViewDecor> =
        ScopeBuilder()

    /**
     * Scope of offsets decors for ViewHolder
     */
    private var offsetsScope: ScopeBuilder<DecorDrawer<OffsetDecor>> =
        ScopeBuilder()

    /**
     * Add ViewHolder decor for special ViewHolder to underlay
     * @param pair Pair of ViewHolder's ViewType and [ru.surfstudio.android.recycler.decorator.base.ViewHolderDecor]
     */
    fun underlay(pair: Pair<Int, ViewHolderDecor>): Builder {
        return apply {
            underlayViewHolderScope.add(DecorDrawer(pair.first, pair.second))
        }
    }

    /**
     * Add ViewHolder decor, for each ViewHolder underlay
     * @param decor [ru.surfstudio.android.recycler.decorator.base.ViewHolderDecor]
     */
    fun underlay(decor: ViewHolderDecor): Builder {
        return apply {
            underlayViewHolderScope.add(DecorDrawer(EACH_VIEW, decor))
        }
    }

    /**
     * Add RecyclerView decor underlay
     * @param decor [ru.surfstudio.android.recycler.decorator.base.RecyclerViewDecor]
     */
    fun underlay(decor: RecyclerViewDecor): Builder {
        return apply {
            underlayRecyclerScope.add(decor)
        }
    }

    /**
     * Add RecyclerView decor overlay
     * @param decor [ru.surfstudio.android.recycler.decorator.base.RecyclerViewDecor]
     */
    fun overlay(decor: RecyclerViewDecor): Builder {
        return apply {
            overlayRecyclerScope.add(decor)
        }
    }

    /**
     * Add ViewHolder decor for special ViewHolder to overlay
     * @param pair Pair of ViewHolder's ViewType and [ru.surfstudio.android.recycler.decorator.base.ViewHolderDecor]
     */
    fun overlay(pair: Pair<Int, ViewHolderDecor>): Builder {
        return apply {
            overlayViewHolderScope.add(DecorDrawer(pair.first, pair.second))
        }
    }

    /**
     * Add ViewHolder decor, for each ViewHolder to overlay
     * @param decor [ru.surfstudio.android.recycler.decorator.base.ViewHolderDecor]
     */
    fun overlay(decor: ViewHolderDecor): Builder {
        return apply {
            overlayViewHolderScope.add(DecorDrawer(EACH_VIEW, decor))
        }
    }

    /**
     * Add offset decor for special ViewHolder
     * @param pair Pair of ViewHolder's ViewType and [ru.surfstudio.android.recycler.decorator.base.OffsetDecor]
     */
    fun offset(pair: Pair<Int, OffsetDecor>): Builder {
        return apply {
            offsetsScope.add(DecorDrawer(pair.first, pair.second))
        }
    }

    /**
     * Add offset for each ViewHolder
     * @param decor [ru.surfstudio.android.recycler.decorator.base.OffsetDecor]
     */
    fun offset(decor: OffsetDecor): Builder {
        return apply {
            offsetsScope.add(DecorDrawer(EACH_VIEW, decor))
        }
    }

    /**
     * Builds the main decorator
     */
    fun build(): Decorator {
        val underlay = underlayViewHolderScope.build()
        val underlayRecycler = underlayRecyclerScope.build()
        val overlay = overlayViewHolderScope.build()
        val overlayRecycler = overlayRecyclerScope.build()
        val offsets = offsetsScope.build()

        require(
            offsets.groupingBy { it.viewItemType }.eachCount().all { it.value == 1 }
        ) { "Any ViewHolder can have only a single OffsetDrawer" }

        return Decorator(
                DecorsBridge(underlay, underlayRecycler, overlay, overlayRecycler, offsets)
        )
    }
}