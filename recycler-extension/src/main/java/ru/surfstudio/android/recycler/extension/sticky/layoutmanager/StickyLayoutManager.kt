/*
    Copyright 2016 Brandon Gogetap

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
package ru.surfstudio.android.recycler.extension.sticky.layoutmanager

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import ru.surfstudio.android.recycler.extension.sticky.item.StickyFooter
import ru.surfstudio.android.recycler.extension.sticky.item.StickyHeader
import java.util.*

/**
 * Кастомный Layout Manager с поддержкой Sticky Items'ов.
 */
class StickyLayoutManager(
        context: Context,
        orientation: Int,
        reverseLayout: Boolean,
        headerHandler: StickyHeaderHandler,
        private val isVisibleFirstFooterAtLaunch: Boolean = false
) : LinearLayoutManager(context, orientation, reverseLayout) {

    private var positioner: StickyItemPositioner = StickyItemPositioner() //менеджер позиционирования Sticky Items
    private var headerHandler: StickyHeaderHandler? = null //поставщик данных для анализа и автоматической расстановки Sticky Items

    private val headerPositions = ArrayList<Int>() //позиции элементов Sticky Header
    private val footerPositions = ArrayList<Int>() //позиции элементов Sticky Footer

    private var viewRetriever: ViewRetriever.RecyclerViewRetriever = ViewRetriever.RecyclerViewRetriever() //поставщик ViewHolder из адаптера для закрепления в родительском контейнере RecyclerView
    private var headerElevation = StickyItemPositioner.ElevationMode.NO_ELEVATION //режим тени Sticky Header'а
    private var footerElevation = StickyItemPositioner.ElevationMode.NO_ELEVATION //режим тени Sticky Footer'а

    private var listener: StickyItemListener? = null

    private val visibleHeaders: Map<Int, View>
        get() {
            val visibleHeaders = LinkedHashMap<Int, View>()

            for (i in 0 until childCount) {
                val view = getChildAt(i)
                val dataPosition = getPosition(view)
                if (headerPositions.contains(dataPosition)) {
                    visibleHeaders[dataPosition] = view
                }
            }
            return visibleHeaders
        }

    private val visibleFooters: Map<Int, View>
        get() {
            val visibleFooters = LinkedHashMap<Int, View>()

            for (i in 0 until childCount) {
                val view = getChildAt(i)
                val dataPosition = getPosition(view)
                if (footerPositions.contains(dataPosition)) {
                    visibleFooters[dataPosition] = view
                }
            }
            return visibleFooters
        }

    constructor(
            context: Context,
            headerHandler: StickyHeaderHandler,
            isVisibleFirstFooterAtLaunch: Boolean
    ) : this(context, LinearLayoutManager.VERTICAL, false, headerHandler, isVisibleFirstFooterAtLaunch) {
        init(headerHandler)
    }

    init {
        init(headerHandler)
    }

    private fun init(stickyHeaderHandler: StickyHeaderHandler) {
        Preconditions.checkNotNull(stickyHeaderHandler, "StickyHeaderHandler == null")
        this.headerHandler = stickyHeaderHandler
    }

    /**
     * Register a callback to be invoked when a header is attached/re-bound or detached.
     *
     * @param listener The callback that will be invoked, or null to unset.
     */
    fun setStickyHeaderListener(listener: StickyItemListener?) {
        this.listener = listener
        positioner.setListener(listener)
    }

    /**
     * Enable or disable elevation for Sticky Headers.
     *
     *
     * If you want to specify a specific amount of elevation, use
     * [StickyLayoutManager.elevateHeaders]
     *
     * @param elevateHeaders Enable Sticky Header elevation. Default is false.
     */
    fun elevateHeaders(elevateHeaders: Boolean) {
        this.headerElevation = if (elevateHeaders)
            StickyItemPositioner.ElevationMode.DEFAULT_ELEVATION
        else
            StickyItemPositioner.ElevationMode.NO_ELEVATION
        elevateHeaders(headerElevation)
    }

    /**
     * Enable Sticky Header elevation with a specific amount.
     *
     * @param dp elevation in dp
     */
    fun elevateHeaders(dp: StickyItemPositioner.ElevationMode) {
        this.headerElevation = dp
        positioner.setElevateHeaders(dp)
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State) {
        super.onLayoutChildren(recycler, state)
        cacheHeaderPositions()
        runPositionerInit()
        Log.d("LOG", "1111 onLayoutChildren")
        resetStickyItemsPositioner()
    }

    override fun scrollVerticallyBy(dy: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        val scroll = super.scrollVerticallyBy(dy, recycler, state)
        if (Math.abs(scroll) > 0) {
            resetStickyItemsPositioner()
        }
        //Log.d("LOG", "1111 headerPositionToShow = ${findLastVisibleItemPosition()}")
        return scroll
    }

    override fun scrollHorizontallyBy(dx: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        val scroll = super.scrollHorizontallyBy(dx, recycler, state)
        if (Math.abs(scroll) > 0) {
            resetStickyItemsPositioner()
        }
        return scroll
    }

    override fun removeAndRecycleAllViews(recycler: RecyclerView.Recycler) {
        super.removeAndRecycleAllViews(recycler)
        positioner.clearHeader()
        positioner.clearFooter()
    }

    override fun onAttachedToWindow(recyclerView: RecyclerView?) {
        Preconditions.validateParentView(recyclerView)

        viewRetriever.recyclerView = recyclerView
        positioner.recyclerView = recyclerView

        positioner.setElevateHeaders(headerElevation)
        positioner.setListener(listener)
        if (headerPositions.size > 0) {
            // Layout has already happened and header positions are cached. Catch positioner up.
            positioner.setStickyPositions(headerPositions, footerPositions)
            runPositionerInit()
        }
        super.onAttachedToWindow(recyclerView)
    }

    private fun runPositionerInit() {
        positioner.reset(orientation)
        resetStickyItemsPositioner()
    }

    /**
     * Перерасчёт позиций закреплённых элементов списка.
     */
    private fun resetStickyItemsPositioner() {
        positioner.updateHeaderState(
                findFirstVisibleItemPosition(),
                visibleHeaders,
                viewRetriever,
                findFirstCompletelyVisibleItemPosition() == 0
        )
        positioner.updateFooterState(
                findLastVisibleItemPosition(),
                visibleFooters,
                viewRetriever,
                isVisibleFirstFooterAtLaunch
        )
    }

    private fun cacheHeaderPositions() {
        headerPositions.clear()
        val adapterData = headerHandler?.getAdapterData()
        if (adapterData == null) {
            positioner.setStickyPositions(headerPositions, footerPositions)
            return
        }

        for (i in adapterData.indices) {
            if (adapterData[i] is StickyHeader) {
                headerPositions.add(i)
            } else if (adapterData[i] is StickyFooter) {
                footerPositions.add(i)
            }
        }
        positioner.setStickyPositions(headerPositions, footerPositions)
    }
}