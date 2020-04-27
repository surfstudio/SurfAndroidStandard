/*
 * Copyright (c) 2019-present, SurfStudio LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.surfstudio.android.mvp.binding.rx.sample.easyadapter.ui.screen.pagination

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import ru.surfstudio.android.core.mvp.binding.sample.R
import ru.surfstudio.android.pagination.PaginationState
import ru.surfstudio.android.pagination.EasyPaginationAdapter

/**
 * Origin <a href="http://google.com">https://github.com/MaksTuev/EasyAdapter/tree/master/sample/src/main/java/ru/surfstudio/easyadapter/sample</a>
 *
 * Класс адаптера с поддержкой пагинации на основе EasyAdapter
 */
open class PaginationableAdapter(onShowMoreListener: () -> Unit) : EasyPaginationAdapter() {
    protected var paginationFooterItemController: BasePaginationFooterController<*>? = null

    init {
        setOnShowMoreListener(onShowMoreListener)
    }

    override fun getPaginationFooterController(): BasePaginationFooterController<*> {
        if (paginationFooterItemController == null)
            paginationFooterItemController = PaginationFooterItemController()
        return paginationFooterItemController!!
    }

    protected class PaginationFooterItemController : BasePaginationFooterController<PaginationFooterItemController.Holder>() {

        override fun createViewHolder(parent: ViewGroup, listener: OnShowMoreListener): Holder {
            return Holder(parent, listener)
        }

        inner class Holder(
                parent: ViewGroup,
                listener: OnShowMoreListener
        ) : BasePaginationFooterHolder(parent, R.layout.pagination_footer_layout) {

            private val loadingIndicator: View = View(parent.context)
            private val showMoreTv: TextView = itemView.findViewById(R.id.pagination_footer_text)

            init {
                showMoreTv.setOnClickListener { listener.onShowMore() }
                loadingIndicator.visibility = GONE
                showMoreTv.visibility = GONE
            }

            override fun bind(state: PaginationState) {

                //для пагинации на StaggeredGrid
                if (itemView.layoutParams is StaggeredGridLayoutManager.LayoutParams) {
                    itemView.updateLayoutParams<StaggeredGridLayoutManager.LayoutParams> { isFullSpan = true }
                }

                when (state) {
                    PaginationState.READY -> {
                        loadingIndicator.visibility = VISIBLE
                        showMoreTv.visibility = GONE
                    }
                    PaginationState.COMPLETE -> {
                        loadingIndicator.visibility = GONE
                        showMoreTv.visibility = GONE
                    }
                    PaginationState.ERROR -> {
                        loadingIndicator.visibility = GONE
                        showMoreTv.visibility = VISIBLE
                    }
                    else -> throw IllegalArgumentException("unsupported state: $state")
                }
            }
        }
    }
}
