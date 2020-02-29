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

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView


object Decorator {

    /**
     * Const use when decor must draw for every ViewHolder
     */
    const val EACH_VIEW = -1

    /**
     * Builder for [androidx.recyclerview.widget.RecyclerView.ItemDecoration]
     */
    class Builder {

        /**
         * Scope of underlay decors for ViewHolder
         */
        private var underlayViewHolderScope: MutableList<DecorDrawer<ViewHolderDecor>> =
                mutableListOf()

        /**
         * Scope of overlay decors for ViewHolder
         */
        private var overlayViewHolderScope: MutableList<DecorDrawer<ViewHolderDecor>> =
                mutableListOf()

        /**
         * Scope of underlay decors for RecyclerView
         */
        private var underlayRecyclerScope: MutableList<RecyclerViewDecor> =
                mutableListOf()

        /**
         * Scope of overlay decors for RecyclerView
         */
        private var overlayRecyclerScope: MutableList<RecyclerViewDecor> =
                mutableListOf()

        /**
         * Scope of offsets decors for ViewHolder
         */
        private var offsetsScope: MutableList<DecorDrawer<OffsetDecor>> =
                mutableListOf()

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
        fun build(): MasterDecorator {
            require(
                    offsetsScope.groupingBy { it.viewItemType }.eachCount().all { it.value == 1 }
            ) { "Any ViewHolder can have only a single OffsetDrawer" }

            return MasterDecorator(
                    DecorsBridge(
                            underlayViewHolderScope,
                            underlayRecyclerScope,
                            overlayViewHolderScope,
                            overlayRecyclerScope,
                            offsetsScope
                    )
            )
        }
    }

    /**
     * Interface for implementation own OffsetDrawer
     */
    interface OffsetDecor {
        fun getItemOffsets(outRect: Rect, view: View, recyclerView: RecyclerView, state: RecyclerView.State)
    }

    /**
     * Interface for implementation own RecyclerViewDecor
     */
    interface RecyclerViewDecor {
        fun draw(canvas: Canvas, recyclerView: RecyclerView, state: RecyclerView.State)
    }

    /**
     * Interface for implementation own ViewHolderDecor
     */
    interface ViewHolderDecor {
        fun draw(canvas: Canvas, view: View, recyclerView: RecyclerView, state: RecyclerView.State)
    }
}