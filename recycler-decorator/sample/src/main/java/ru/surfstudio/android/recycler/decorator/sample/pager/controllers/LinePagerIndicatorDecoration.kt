package ru.surfstudio.android.recycler.decorator.sample.pager.controllers

import android.graphics.Canvas
import android.graphics.Paint
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.surfstudio.android.recycler.decorator.Decorator
import ru.surfstudio.android.recycler.decorator.sample.toPx

class LinePagerIndicatorDecoration : Decorator.RecyclerViewDecor {

    private val colorActive = -0x1
    private val colorInactive = 0x66FFFFFF

    /**
     * Height attachTo the space the indicator takes up at the bottom attachTo the view.
     */
    private val indicatorHeight = 16.toPx

    /**
     * Indicator stroke width.
     */
    private val indicatorStrokeWidth = 2.toPx

    /**
     * Indicator width.
     */
    private val indicatorItemLength = 16.toPx
    /**
     * Padding between indicators.
     */
    private val indicatorItemPadding = 4.toPx

    /**
     * Some more natural animation interpolation
     */
    private val interpolator = AccelerateDecelerateInterpolator()

    private val paint = Paint()

    init {
        paint.strokeCap = Paint.Cap.ROUND
        paint.strokeWidth = indicatorStrokeWidth.toFloat()
        paint.style = Paint.Style.STROKE
        paint.isAntiAlias = true
    }

    private fun drawInactiveIndicators(c: Canvas, indicatorStartX: Float, indicatorPosY: Float, itemCount: Int) {
        paint.color = colorInactive

        // width attachTo item indicator including padding
        val itemWidth = indicatorItemLength + indicatorItemPadding

        var start = indicatorStartX
        for (i in 0 until itemCount) {
            // draw the line for every item
            c.drawLine(start, indicatorPosY, start + indicatorItemLength, indicatorPosY, paint)
            start += itemWidth
        }
    }

    private fun drawHighlights(
        c: Canvas, indicatorStartX: Float, indicatorPosY: Float,
        highlightPosition: Int, progress: Float, itemCount: Int
    ) {
        paint.color = colorActive

        // width attachTo item indicator including padding
        val itemWidth = indicatorItemLength + indicatorItemPadding

        if (progress == 0f) {
            // no swipe, draw a normal indicator
            val highlightStart = indicatorStartX + itemWidth * highlightPosition
            c.drawLine(
                highlightStart, indicatorPosY,
                highlightStart + indicatorItemLength, indicatorPosY, paint
            )
        } else {
            var highlightStart = indicatorStartX + itemWidth * highlightPosition
            // calculate partial highlight
            val partialLength = indicatorItemLength * progress

            // draw the cut off highlight
            c.drawLine(
                highlightStart + partialLength, indicatorPosY,
                highlightStart + indicatorItemLength, indicatorPosY, paint
            )

            // draw the highlight overlapping to the next item as well
            if (highlightPosition < itemCount - 1) {
                highlightStart += itemWidth
                c.drawLine(
                    highlightStart, indicatorPosY,
                    highlightStart + partialLength, indicatorPosY, paint
                )
            }
        }
    }

    override fun draw(
        canvas: Canvas,
        recyclerView: RecyclerView,
        state: RecyclerView.State
    ) {
        val itemCount = recyclerView.adapter!!.itemCount

        // center horizontally, calculate width and subtract half from center
        val totalLength = indicatorItemLength * itemCount
        val paddingBetweenItems = Math.max(0, itemCount - 1) * indicatorItemPadding
        val indicatorTotalWidth = totalLength + paddingBetweenItems
        val indicatorStartX = (recyclerView.width - indicatorTotalWidth) / 2f

        // center vertically in the allotted space
        val indicatorPosY = recyclerView.height - indicatorHeight / 2f

        drawInactiveIndicators(canvas, indicatorStartX, indicatorPosY, itemCount)


        // find active page (which should be highlighted)
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
        val activePosition = layoutManager!!.findFirstVisibleItemPosition()
        if (activePosition == RecyclerView.NO_POSITION) {
            return
        }

        // find offset attachTo active page (if the user is scrolling)
        val activeChild = layoutManager.findViewByPosition(activePosition)
        val left = activeChild!!.left
        val width = activeChild.width

        // on swipe the active item will be positioned from [-width, 0]
        // interpolate offset for smooth animation
        val progress = interpolator.getInterpolation(left * -1 / width.toFloat())

        drawHighlights(canvas, indicatorStartX, indicatorPosY, activePosition, progress, itemCount)
    }
}
