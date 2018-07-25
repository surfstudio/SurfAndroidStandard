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
 */package ru.surfstudio.android.recycler.extension.sticky.layoutmanager

import android.content.Context
import android.os.Build
import android.support.annotation.Px
import android.support.annotation.VisibleForTesting
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import ru.surfstudio.easyadapter.carousel.R


internal class StickyHeaderPositioner(private val recyclerView: RecyclerView) {
    private val checkMargins: Boolean

    private var currentHeader: View? = null
    @get:VisibleForTesting
    var lastBoundPosition = INVALID_POSITION
        private set
    private var headerPositions: List<Int>? = null
    private var orientation: Int = 0
    private var dirty: Boolean = false
    private var headerElevation = NO_ELEVATION.toFloat()
    private var cachedElevation = NO_ELEVATION
    private var currentViewHolder: RecyclerView.ViewHolder? = null
    private var listener: StickyHeaderListener? = null

    private val recyclerParent: ViewGroup
        get() = recyclerView.parent as ViewGroup

    init {
        recyclerView.viewTreeObserver.addOnGlobalLayoutListener {
            val visibility = this@StickyHeaderPositioner.recyclerView.visibility
            if (currentHeader != null) {
                currentHeader!!.visibility = visibility
            }
        }
        checkMargins = recyclerViewHasPadding()
    }

    fun setHeaderPositions(headerPositions: List<Int>) {
        this.headerPositions = headerPositions
    }

    fun updateHeaderState(firstVisiblePosition: Int, visibleHeaders: Map<Int, View>,
                          viewRetriever: ViewRetriever?, atTop: Boolean) {
        //вычисляется позиция sticky header в общем списке item'ов
        val headerPositionToShow = if (atTop) {
            INVALID_POSITION
        } else {
            getHeaderPositionToShow(firstVisiblePosition, visibleHeaders[firstVisiblePosition])
        }
        Log.d("LOG", "1111 headerPositionToShow = $headerPositionToShow")
        val headerToCopy = visibleHeaders[headerPositionToShow]
        if (headerPositionToShow != lastBoundPosition) {
            if (headerPositionToShow == INVALID_POSITION || checkMargins && headerAwayFromEdge(headerToCopy)) { // We don't want to attach yet if header view is not at edge
                dirty = true
                safeDetachHeader()
                lastBoundPosition = INVALID_POSITION
            } else {
                lastBoundPosition = headerPositionToShow
                val viewHolder = viewRetriever?.getViewHolderForPosition(headerPositionToShow)
                attachHeader(viewHolder, headerPositionToShow)
            }
        } else if (checkMargins) {
            /*
              This could still be our firstVisiblePosition even if another view is visible above it.
              See `#getHeaderPositionToShow` for explanation.
             */
            if (headerAwayFromEdge(headerToCopy)) {
                detachHeader(lastBoundPosition)
                lastBoundPosition = INVALID_POSITION
            }
        }
        checkHeaderPositions(visibleHeaders)
        recyclerView.post { checkElevation() }
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
            if (key <= lastBoundPosition) {
                continue
            }
            reset = offsetHeader(nextHeader) == -1f
            break
        }
        if (reset) resetTranslation()
        currentHeader!!.visibility = View.VISIBLE
    }

    fun setElevateHeaders(dpElevation: Int) {
        if (dpElevation != NO_ELEVATION) {
            // Context may not be available at this point, so caching the dp value to be converted
            // into pixels after first header is attached.
            cachedElevation = dpElevation
        } else {
            headerElevation = NO_ELEVATION.toFloat()
            cachedElevation = NO_ELEVATION
        }
    }

    fun reset(orientation: Int) {
        this.orientation = orientation
        lastBoundPosition = INVALID_POSITION
        dirty = true
        safeDetachHeader()
    }

    fun clearHeader() {
        detachHeader(lastBoundPosition)
    }

    fun setListener(listener: StickyHeaderListener?) {
        this.listener = listener
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

    private fun shouldOffsetHeader(nextHeader: View): Boolean {
        return if (orientation == LinearLayoutManager.VERTICAL) {
            nextHeader.y < currentHeader!!.height
        } else {
            nextHeader.x < currentHeader!!.width
        }
    }

    private fun resetTranslation() {
        if (orientation == LinearLayoutManager.VERTICAL) {
            currentHeader!!.translationY = 0f
        } else {
            currentHeader!!.translationX = 0f
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
        if (currentViewHolder === viewHolder) {
            callDetach(lastBoundPosition)

            recyclerView.adapter.onBindViewHolder(currentViewHolder!!, headerPosition)
            currentViewHolder!!.itemView.requestLayout()
            checkTranslation()
            callAttach(headerPosition)
            dirty = false
            return
        }
        detachHeader(lastBoundPosition)
        this.currentViewHolder = viewHolder

        recyclerView.adapter.onBindViewHolder(currentViewHolder!!, headerPosition)
        this.currentHeader = currentViewHolder!!.itemView
        callAttach(headerPosition)
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
            internal var previous = currentDimension()

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
        if (headerElevation != NO_ELEVATION.toFloat() && currentHeader != null) {
            if (orientation == LinearLayoutManager.VERTICAL && currentHeader!!.translationY == 0f || orientation == LinearLayoutManager.HORIZONTAL && currentHeader!!.translationX == 0f) {
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
            callDetach(position)
            currentHeader = null
            currentViewHolder = null
        }
    }

    private fun callAttach(position: Int) {
        if (listener != null) {
            listener!!.headerAttached(currentHeader!!, position)
        }
    }

    private fun callDetach(position: Int) {
        if (listener != null) {
            listener!!.headerDetached(currentHeader!!, position)
        }
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
            recyclerView.paddingLeft
        else
            0
        @Px val topMargin = if (orientation == LinearLayoutManager.VERTICAL)
            0
        else
            recyclerView.paddingTop
        @Px val rightMargin = if (orientation == LinearLayoutManager.VERTICAL)
            recyclerView.paddingRight
        else
            0
        layoutParams.setMargins(leftMargin, topMargin, rightMargin, 0)
    }

    private fun headerAwayFromEdge(headerToCopy: View?): Boolean {
        return if (headerToCopy != null) {
            if (orientation == LinearLayoutManager.VERTICAL)
                headerToCopy.y > 0
            else
                headerToCopy.x > 0
        } else false
    }

    private fun recyclerViewHasPadding(): Boolean {
        return (recyclerView.paddingLeft > 0
                || recyclerView.paddingRight > 0
                || recyclerView.paddingTop > 0)
    }

    private fun waitForLayoutAndRetry(visibleHeaders: Map<Int, View>) {
        val view = currentHeader ?: return
        view.viewTreeObserver.addOnGlobalLayoutListener(
                object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        } else {

                            view.viewTreeObserver.removeGlobalOnLayoutListener(this)
                        }
                        // If header was removed during layout
                        if (currentHeader == null) return
                        recyclerParent.requestLayout()
                        checkHeaderPositions(visibleHeaders)
                    }
                })
    }

    /**
     * Detaching while [StickyLayoutManager] is laying out children can cause an inconsistent
     * state in the child count variable in [android.widget.FrameLayout] layoutChildren method
     */
    private fun safeDetachHeader() {
        val cachedPosition = lastBoundPosition
        recyclerParent.post {
            if (dirty) {
                detachHeader(cachedPosition)
            }
        }
    }

    private fun resolveElevationSettings(context: Context) {
        if (cachedElevation != NO_ELEVATION && headerElevation == NO_ELEVATION.toFloat()) {
            headerElevation = pxFromDp(context, cachedElevation)
        }
    }

    private fun pxFromDp(context: Context, dp: Int): Float {
        val scale = context.resources.displayMetrics.density
        return dp * scale
    }

    companion object {

        val NO_ELEVATION = -1
        val DEFAULT_ELEVATION = 5

        private val INVALID_POSITION = -1
    }
}