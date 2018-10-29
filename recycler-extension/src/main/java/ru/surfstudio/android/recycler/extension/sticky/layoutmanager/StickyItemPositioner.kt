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
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import androidx.annotation.Px
import androidx.annotation.VisibleForTesting
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.surfstudio.easyadapter.carousel.R

/**
 * Менеджер позиционирования Sticky-элементов.
 *
 * Отвечает за позиционирование Sticky Header'ов и Sticky Footer'ов.
 */
class StickyItemPositioner(
        var recyclerView: RecyclerView? = null
) {

    private val checkMargins: Boolean

    private var currentHeader: View? = null //текущий закреплённый Sticky Header
    private var currentFooter: View? = null //текущий закреплённый Sticky Footer

    @get:VisibleForTesting
    var lastHeaderBoundPosition = INVALID_POSITION
        private set
    var lastFooterBoundPosition = INVALID_POSITION
        private set

    private var headerPositions: List<Int>? = null
    private var footerPositions: List<Int>? = null

    private var orientation: Int = 0
    private var dirty: Boolean = false
    private var headerElevation = ElevationMode.NO_ELEVATION.dp
    private var cachedElevation = ElevationMode.NO_ELEVATION
    private var currentHeaderViewHolder: RecyclerView.ViewHolder? = null
    private var currentFooterViewHolder: RecyclerView.ViewHolder? = null
    private var headerListener: StickyHeaderListener? = null
    private var footerListener: StickyFooterListener? = null

    private val recyclerParent: ViewGroup
        get() = recyclerView?.parent as ViewGroup

    init {
        recyclerView?.viewTreeObserver?.addOnGlobalLayoutListener {
            val visibility = this@StickyItemPositioner.recyclerView?.visibility
            visibility?.let {
                if (currentHeader != null) {
                    currentHeader!!.visibility = it
                }
            }

        }
        checkMargins = recyclerViewHasPadding()
    }

    /**
     * Инициализация менеджера путём передачи позиций всех Sticky-элементов.
     *
     * @param headerPositions позиции всех Sticky Header'ов
     * @param footerPositions позиции всех Sticky Footer'ов
     */
    fun setStickyPositions(headerPositions: List<Int>, footerPositions: List<Int>) {
        this.headerPositions = headerPositions
        this.footerPositions = footerPositions
    }

    /**
     * Обновление состояния и отображения Sticky Header'а.
     *
     * @param firstVisiblePosition позиция первого видимого элемента списка
     */
    fun updateHeaderState(firstVisiblePosition: Int,
                          visibleHeaders: Map<Int, View>,
                          viewRetriever: ViewRetriever?,
                          atTop: Boolean) {
        //вычисляется позиция sticky header в общем списке item'ов
        val headerPositionToShow = if (atTop) {
            INVALID_POSITION
        } else {
            getHeaderPositionToShow(firstVisiblePosition, visibleHeaders[firstVisiblePosition])
        }
        val headerToCopy = visibleHeaders[headerPositionToShow]
        if (headerPositionToShow != lastHeaderBoundPosition) {
            if (headerPositionToShow == INVALID_POSITION || checkMargins && headerAwayFromEdge(headerToCopy)) { // We don't want to attach yet if header view is not at edge
                dirty = true
                safeDetachHeader()
                lastHeaderBoundPosition = INVALID_POSITION
            } else {
                lastHeaderBoundPosition = headerPositionToShow
                val viewHolder = viewRetriever?.getViewHolderForPosition(headerPositionToShow)
                attachHeader(viewHolder, headerPositionToShow)
            }
        } else if (checkMargins) {
            /*
              This could still be our firstVisiblePosition even if another view is visible above it.
              See `#getHeaderPositionToShow` for explanation.
             */
            if (headerAwayFromEdge(headerToCopy)) {
                detachHeader(lastHeaderBoundPosition)
                lastHeaderBoundPosition = INVALID_POSITION
            }
        }
        checkHeaderPositions(visibleHeaders)
        recyclerView?.post { checkElevation() }
    }

    fun updateFooterState(lastVisibleItemPosition: Int,
                          visibleFooters: Map<Int, View>,
                          viewRetriever: ViewRetriever?,
                          showFirstAtLaunch: Boolean
    ) {
        //вычисляется позиция sticky header в общем списке item'ов
        val footerPositionToShow = if (!showFirstAtLaunch) {
            INVALID_POSITION
        } else {
            getFooterPositionToShow(lastVisibleItemPosition, visibleFooters[lastVisibleItemPosition])
        }
        val footerToCopy = visibleFooters[footerPositionToShow]
        if (showFirstAtLaunch || footerPositionToShow != lastFooterBoundPosition) {
            if (footerPositionToShow == INVALID_POSITION || checkMargins && footerAwayFromEdge(footerToCopy)) { // We don't want to attach yet if header view is not at edge
                dirty = true
                safeDetachFooter()
                lastFooterBoundPosition = INVALID_POSITION
            } else {
                if (currentFooterViewHolder == null) {
                    lastFooterBoundPosition = footerPositionToShow
                    val viewHolder = viewRetriever?.getViewHolderForPosition(footerPositionToShow)
                    attachFooter(viewHolder, footerPositionToShow)
                }
            }
        } else if (checkMargins) {
            /*
              This could still be our lastVisibleItemPosition even if another view is visible above it.
              See `#getHeaderPositionToShow` for explanation.
             */
            if (footerAwayFromEdge(footerToCopy)) {
                detachFooter(lastFooterBoundPosition)
                lastFooterBoundPosition = INVALID_POSITION
            }
        }
        checkFooterPositions(visibleFooters)
        recyclerView?.post { checkElevation() }
    }

    // This checks visible headers and their positions to determine if the sticky header needs
    // to be offset. In reality, only the header following the sticky header is checked. Some
    // optimization may be possible here (not storing all visible headers in map).
    fun checkHeaderPositions(visibleHeaders: Map<Int, View>) {
        if (currentHeader == null) return
        // This can happen after configuration changes.
        if (currentHeader!!.height == 0) {
            waitForLayoutAndRetry(visibleHeaders)
            return
        }
        var reset = true
        for ((key, nextHeader) in visibleHeaders) {
            if (key <= lastHeaderBoundPosition) {
                continue
            }
            reset = offsetHeader(nextHeader) == -1f
            break
        }
        if (reset) resetTranslationHeader()
        currentHeader!!.visibility = View.VISIBLE
    }

    // This checks visible headers and their positions to determine if the sticky header needs
    // to be offset. In reality, only the header following the sticky header is checked. Some
    // optimization may be possible here (not storing all visible headers in map).
    fun checkFooterPositions(visibleFooters: Map<Int, View>) {
        if (currentFooter == null) return
        // This can happen after configuration changes.
        if (currentFooter!!.height == 0) {
            waitForLayoutAndRetry(visibleFooters)
            return
        }
        var reset = true
        for ((key, nextFooter) in visibleFooters) {
            if (key <= lastFooterBoundPosition) {
                continue
            }
            reset = offsetFooter(nextFooter) == -1f
            break
        }
        if (reset) resetTranslationFooter()
        currentFooter!!.visibility = View.VISIBLE
    }

    fun setElevateHeaders(dpElevation: ElevationMode) {
        if (dpElevation != ElevationMode.NO_ELEVATION) {
            // Context may not be available at this point, so caching the dp value to be converted
            // into pixels after first header is attached.
            cachedElevation = dpElevation
        } else {
            headerElevation = ElevationMode.NO_ELEVATION.dp
            cachedElevation = ElevationMode.NO_ELEVATION
        }
    }

    fun reset(orientation: Int) {
        this.orientation = orientation
        lastHeaderBoundPosition = INVALID_POSITION
        dirty = true
        safeDetachHeader()
        safeDetachFooter()
    }

    fun clearHeader() {
        detachHeader(lastHeaderBoundPosition)
    }

    fun clearFooter() {
        detachFooter(lastFooterBoundPosition)
    }

    fun setStickyHeaderListener(listener: StickyHeaderListener?) {
        this.headerListener = listener
    }

    fun setStickyFooterListener(listener: StickyFooterListener?) {
        this.footerListener = listener
    }

    private fun offsetHeader(nextHeader: View): Float {
        val shouldOffsetHeader = shouldOffsetHeader(nextHeader)
        var offset = -1f
        if (shouldOffsetHeader) {
            if (orientation == LinearLayoutManager.VERTICAL) {
                offset = -(currentHeader!!.height - nextHeader.y)
                currentHeader!!.translationY = offset
            } else {
                offset = -(currentHeader!!.width - nextHeader.x)
                currentHeader!!.translationX = offset
            }
        }
        return offset
    }

    private fun offsetFooter(nextFooter: View): Float {
        val shouldOffsetFooter = shouldOffsetFooter(nextFooter)
        var offset = -1f
        if (shouldOffsetFooter) {
            if (orientation == LinearLayoutManager.VERTICAL) {
                offset = -(currentFooter!!.height - nextFooter.y)
                currentFooter!!.translationY = offset
            } else {
                offset = -(currentFooter!!.width - nextFooter.x)
                currentFooter!!.translationX = offset
            }
        }
        return offset
    }


    private fun shouldOffsetHeader(nextHeader: View): Boolean {
        return if (orientation == LinearLayoutManager.VERTICAL) {
            nextHeader.y < currentHeader!!.height
        } else {
            nextHeader.x < currentHeader!!.width
        }
    }

    private fun shouldOffsetFooter(nextFooter: View): Boolean {
        return if (orientation == LinearLayoutManager.VERTICAL) {
            nextFooter.y < currentFooter!!.height
        } else {
            nextFooter.x < currentFooter!!.width
        }
    }

    private fun resetTranslationHeader() {
        if (orientation == LinearLayoutManager.VERTICAL) {
            currentHeader!!.translationY = 0f
        } else {
            currentHeader!!.translationX = 0f
        }
    }

    private fun resetTranslationFooter() {
        if (orientation == LinearLayoutManager.VERTICAL) {
            currentFooter!!.translationY = 0f
        } else {
            currentFooter!!.translationX = 0f
        }
    }

    /**
     * In case of padding, first visible position may not be accurate.
     *
     *
     * Example: RecyclerView has padding of 10dp. With clipToPadding set to false, a visible view
     * above the 10dp threshold will not be recognized as firstVisiblePosition by the LayoutManager.
     *
     *
     * To remedy this, we are checking if the firstVisiblePosition (according to the LayoutManager)
     * is a header (headerForPosition will not be null). If it is, we check its Y. If #getY is
     * greater than 0 then we know it is actually not the firstVisiblePosition, and return the
     * preceding header position (if available).
     */
    private fun getHeaderPositionToShow(firstVisiblePosition: Int, headerForPosition: View?): Int {
        var headerPositionToShow = INVALID_POSITION
        if (headerIsOffset(headerForPosition)) {
            val offsetHeaderIndex = headerPositions!!.indexOf(firstVisiblePosition)
            if (offsetHeaderIndex > 0) {
                return headerPositions!![offsetHeaderIndex - 1]
            }
        }
        for (headerPosition in headerPositions!!) {
            if (headerPosition <= firstVisiblePosition) {
                headerPositionToShow = headerPosition
            } else {
                break
            }
        }
        return headerPositionToShow
    }

    private fun getFooterPositionToShow(
            lastVisiblePosition: Int,
            footerForPosition: View?
    ): Int {
        var footerPositionToShow = INVALID_POSITION
        /*if (headerIsOffset(footerForPosition)) {
            val offsetHeaderIndex = footerPositions!!.indexOf(lastVisiblePosition)
            Log.d("LOG", "1111 offsetHeaderIndex = $offsetHeaderIndex")
            if (offsetHeaderIndex > 0) {
                Log.d("LOG", "1111 footerPosition = ${footerPositions!![offsetHeaderIndex - 1]}")
                return footerPositions!![offsetHeaderIndex - 1]
            }
        }*/
        for (footerPosition in footerPositions!!) {
            if (footerPosition >= lastVisiblePosition) {
                footerPositionToShow = footerPosition
                //нужен самый первый подходящий футер
                break
            } else {
                continue
            }
        }
        return footerPositionToShow
    }

    private fun headerIsOffset(headerForPosition: View?): Boolean {
        return if (headerForPosition != null) {
            if (orientation == LinearLayoutManager.VERTICAL)
                headerForPosition.y > 0
            else
                headerForPosition.x > 0
        } else false
    }

    @VisibleForTesting
    private fun attachHeader(viewHolder: RecyclerView.ViewHolder?, headerPosition: Int) {
        if (currentHeaderViewHolder === viewHolder) {
            callDetachHeader(lastHeaderBoundPosition)

            recyclerView?.adapter?.onBindViewHolder(currentHeaderViewHolder!!, headerPosition)
            currentHeaderViewHolder!!.itemView.requestLayout()
            checkTranslation()
            callAttachHeader(headerPosition)
            dirty = false
            return
        }
        detachHeader(lastHeaderBoundPosition)
        this.currentHeaderViewHolder = viewHolder

        recyclerView?.adapter?.onBindViewHolder(currentHeaderViewHolder!!, headerPosition)
        this.currentHeader = currentHeaderViewHolder!!.itemView
        callAttachHeader(headerPosition)
        resolveElevationSettings(currentHeader!!.context)
        // Set to Invisible until we position it in #checkHeaderPositions.
        currentHeader!!.visibility = View.INVISIBLE
        currentHeader!!.id = R.id.header_view
        recyclerParent.addView(currentHeader)
        if (checkMargins) {
            updateLayoutParams(currentHeader!!)
        }
        dirty = false
    }

    @VisibleForTesting
    private fun attachFooter(viewHolder: RecyclerView.ViewHolder?, footerPosition: Int) {
        if (currentFooterViewHolder === viewHolder) {
            callDetachFooter(lastFooterBoundPosition)
            recyclerView?.adapter?.onBindViewHolder(currentFooterViewHolder!!, footerPosition)
            currentFooterViewHolder!!.itemView.requestLayout()
            checkTranslation()
            callAttachFooter(footerPosition)
            dirty = false
            return
        }
        detachFooter(lastFooterBoundPosition)
        this.currentFooterViewHolder = viewHolder

        recyclerView?.adapter?.onBindViewHolder(currentFooterViewHolder!!, footerPosition)
        this.currentFooter = currentFooterViewHolder!!.itemView
        callAttachFooter(footerPosition)
        resolveElevationSettings(currentFooter!!.context)
        // Set to Invisible until we position it in #checkHeaderPositions.
        currentFooter!!.visibility = View.INVISIBLE
        currentFooter!!.id = R.id.footer_view

        val frameLp = currentFooter?.layoutParams as? FrameLayout.LayoutParams
        frameLp?.gravity = Gravity.BOTTOM
        val coordinatorLp = currentFooter?.layoutParams as? CoordinatorLayout.LayoutParams
        coordinatorLp?.gravity = Gravity.BOTTOM
        currentFooter?.layoutParams = frameLp ?: coordinatorLp
        recyclerParent.addView(currentFooter)
        if (checkMargins) {
            updateLayoutParams(currentFooter!!)
        }
        dirty = false
    }

    private fun currentDimension(): Int {
        if (currentHeader == null) {
            return 0
        }
        return if (orientation == LinearLayoutManager.VERTICAL) {
            currentHeader!!.height
        } else {
            currentHeader!!.width
        }
    }

    private fun headerHasTranslation(): Boolean {
        if (currentHeader == null) {
            return false
        }
        return if (orientation == LinearLayoutManager.VERTICAL) {
            currentHeader!!.translationY < 0
        } else {
            currentHeader!!.translationX < 0
        }
    }

    private fun updateTranslation(diff: Int) {
        if (currentHeader == null) {
            return
        }
        if (orientation == LinearLayoutManager.VERTICAL) {
            currentHeader!!.translationY = currentHeader!!.translationY + diff
        } else {
            currentHeader!!.translationX = currentHeader!!.translationX + diff
        }
    }

    /**
     * When a view is re-bound using the same view holder, the height may have changed. If the header has translation
     * applied, this could cause a flickering if the view's height has increased.
     */
    private fun checkTranslation() {
        val view = currentHeader ?: return
        view.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            var previous = currentDimension()

            override fun onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                } else {
                    view.viewTreeObserver.removeGlobalOnLayoutListener(this)
                }
                if (currentHeader == null) return

                val newDimen = currentDimension()
                if (headerHasTranslation() && previous != newDimen) {
                    updateTranslation(previous - newDimen)
                }
            }
        })
    }

    private fun checkElevation() {
        if (headerElevation != ElevationMode.NO_ELEVATION.dp && currentHeader != null) {
            if (orientation == LinearLayoutManager.VERTICAL &&
                    currentHeader!!.translationY == 0f || orientation == LinearLayoutManager.HORIZONTAL &&
                    currentHeader!!.translationX == 0f) {
                elevateHeader()
            } else {
                settleHeader()
            }
        }
    }

    private fun elevateHeader() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (currentHeader!!.tag != null) {
                // Already elevated, bail out
                return
            }
            currentHeader!!.tag = true
            currentHeader!!.animate().z(headerElevation)
        }
    }

    private fun settleHeader() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (currentHeader!!.tag != null) {
                currentHeader!!.tag = null
                currentHeader!!.animate().z(0f)
            }
        }
    }

    private fun detachHeader(position: Int) {
        if (currentHeader != null) {
            recyclerParent.removeView(currentHeader)
            callDetachHeader(position)
            currentHeader = null
            currentHeaderViewHolder = null
        }
    }

    private fun detachFooter(position: Int) {
        if (currentFooter != null) {
            recyclerParent.removeView(currentFooter)
            callDetachFooter(position)
            currentFooter = null
            currentFooterViewHolder = null
        }
    }

    private fun callAttachHeader(position: Int) {
        currentHeader?.let { headerListener?.headerAttached(it, position) }
    }

    private fun callDetachHeader(position: Int) {
        currentHeader?.let { headerListener?.headerDetached(it, position) }
    }

    private fun callAttachFooter(position: Int) {
        currentFooter?.let { footerListener?.footerAttached(it, position) }
    }

    private fun callDetachFooter(position: Int) {
        currentFooter?.let { footerListener?.footerDetached(it, position) }
    }

    /**
     * Adds margins to left/right (or top/bottom in horizontal orientation)
     *
     *
     * Top padding (or left padding in horizontal orientation) with clipToPadding = true is not
     * supported. If you need to offset the top (or left in horizontal orientation) and do not
     * want scrolling children to be visible, use margins.
     */
    private fun updateLayoutParams(currentHeader: View) {
        val params = currentHeader.layoutParams as ViewGroup.MarginLayoutParams
        matchMarginsToPadding(params)
    }

    private fun matchMarginsToPadding(layoutParams: ViewGroup.MarginLayoutParams) {
        @Px val leftMargin = if (orientation == LinearLayoutManager.VERTICAL)
            recyclerView?.paddingLeft
        else
            0
        @Px val topMargin = if (orientation == LinearLayoutManager.VERTICAL)
            0
        else
            recyclerView?.paddingTop
        @Px val rightMargin = if (orientation == LinearLayoutManager.VERTICAL)
            recyclerView?.paddingRight
        else
            0
        if (leftMargin != null && topMargin != null && rightMargin != null) {
            layoutParams.setMargins(leftMargin, topMargin, rightMargin, 0)
        }
    }

    /**
     * Определение признака отрыва Header'а от верхнего или левого края экрана в зависимости от
     * ориентации LayoutManager. Требуется для того, чтобы вовремя откреплять Sticky Header от
     * границы экрана при дальнейшем скролле списка.
     */
    private fun headerAwayFromEdge(headerView: View?): Boolean =
            if (headerView != null) {
                if (orientation == LinearLayoutManager.VERTICAL)
                    headerView.y > 0
                else
                    headerView.x > 0
            } else
                false

    /**
     * Определение признака отрыва Footer'а от нижнего или правого края экрана в зависимости от
     * ориентации LayoutManager. Требуется для того, чтобы вовремя откреплять Sticky Footer от
     * границы экрана при дальнейшем скролле списка.
     */
    private fun footerAwayFromEdge(footerView: View?): Boolean =
            if (footerView != null) {
                if (orientation == LinearLayoutManager.VERTICAL) {
                    footerView.y > 0
                } else
                    footerView.x > 0
            } else
                false

    private fun recyclerViewHasPadding(): Boolean {
        val paddingLeft = recyclerView?.paddingLeft ?: 0
        val paddingRight = recyclerView?.paddingRight ?: 0
        val paddingTop = recyclerView?.paddingTop ?: 0
        return (paddingLeft > 0
                || paddingRight > 0
                || paddingTop > 0)
    }

    /**
     *
     */
    private fun waitForLayoutAndRetry(visibleStickyItems: Map<Int, View>) {
        val headerView = currentHeader
        val footerView = currentFooter
        headerView?.viewTreeObserver?.addOnGlobalLayoutListener(
                object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            headerView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        } else {
                            headerView.viewTreeObserver.removeGlobalOnLayoutListener(this)
                        }
                        // If header was removed during layout
                        if (currentHeader == null) return
                        recyclerParent.requestLayout()
                        checkHeaderPositions(visibleStickyItems)
                    }
                })

        footerView?.viewTreeObserver?.addOnGlobalLayoutListener(
                object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            footerView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        } else {
                            footerView.viewTreeObserver.removeGlobalOnLayoutListener(this)
                        }
                        // If header was removed during layout
                        if (currentFooter == null) return
                        recyclerParent.requestLayout()
                        checkFooterPositions(visibleStickyItems)
                    }
                })
    }

    /**
     * Detaching while [StickyLayoutManager] is laying out children can cause an inconsistent
     * state in the child count variable in [android.widget.FrameLayout] layoutChildren method
     */
    private fun safeDetachHeader() {
        val cachedPosition = lastHeaderBoundPosition
        recyclerParent.post {
            if (dirty) {
                detachHeader(cachedPosition)
            }
        }
    }

    private fun safeDetachFooter() {
        val cachedPosition = lastFooterBoundPosition
        recyclerParent.post {
            if (dirty) {
                detachFooter(cachedPosition)
            }
        }
    }

    private fun resolveElevationSettings(context: Context) {
        if (cachedElevation != ElevationMode.NO_ELEVATION && headerElevation == ElevationMode.NO_ELEVATION.dp) {
            headerElevation = pxFromDp(context, cachedElevation.dp)
        }
    }

    private fun pxFromDp(context: Context, dp: Float): Float {
        val scale = context.resources.displayMetrics.density
        return dp * scale
    }

    companion object {
        private val INVALID_POSITION = -1
    }

    enum class ElevationMode(val dp: Float) {
        NO_ELEVATION(-1f),
        DEFAULT_ELEVATION(5f)
    }
}