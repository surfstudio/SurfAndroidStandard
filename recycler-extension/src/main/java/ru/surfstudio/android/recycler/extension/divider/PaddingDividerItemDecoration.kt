package ru.surfstudio.android.recycler.extension.divider

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Настраиваемый DividerItemDecoration
 *
 * Позволяет задать padding слева и справа для вертикального списка и
 * сверху и снизу для горизонтального.
 * Также позволяет задать headerCount и footerCount.
 */
class PaddingDividerItemDecoration @JvmOverloads constructor(
        context: Context,
        orientation: Int,
        @DrawableRes resId: Int,
        var leftPaddingPx: Int = 0,
        var rightPaddingPx: Int = 0,
        var topPaddingPx: Int = 0,
        var bottomPaddingPx: Int = 0,
        headerCount: Int = 0,
        footerCount: Int = 0,
        val drawBeforeFirst: Boolean = false
) : RecyclerView.ItemDecoration() {

    companion object {
        const val HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL
        const val VERTICAL_LIST = LinearLayoutManager.VERTICAL
    }

    private val dividerDrawable: Drawable? = ContextCompat.getDrawable(context, resId)

    private var orientation: Int = 0
    private var headerCount: Int = 0
    private var footerCount: Int = 0

    init {
        setOrientation(orientation)
        setHeaderCount(headerCount)
        setFooterCount(footerCount)
    }

    fun setOrientation(orientation: Int) {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
            throw IllegalArgumentException("invalid orientation")
        }
        this.orientation = orientation
    }

    fun setHeaderCount(headerCount: Int) {
        if (headerCount < 0) {
            throw IllegalArgumentException("wrong header count")
        }
        this.headerCount = headerCount
    }

    fun setFooterCount(footerCount: Int) {
        if (footerCount < 0) {
            throw IllegalArgumentException("wrong footer count")
        }
        this.footerCount = footerCount
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (orientation == VERTICAL_LIST) {
            drawVertical(c, parent)
        } else {
            drawHorizontal(c, parent)
        }
    }

    private fun drawVertical(c: Canvas, parent: RecyclerView) {
        val left = parent.paddingLeft + leftPaddingPx
        val right = parent.width - parent.paddingRight - rightPaddingPx

        val childCount = parent.childCount - footerCount
        val startPosition = if (headerCount == 0) {
            if (drawBeforeFirst) {
                0
            } else {
                1
            }
        } else {
            headerCount
        }
        for (i in startPosition until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin
            val bottom = top + dividerDrawable!!.intrinsicHeight
            dividerDrawable.setBounds(left, top, right, bottom)
            dividerDrawable.draw(c)
        }
    }

    private fun drawHorizontal(c: Canvas, parent: RecyclerView) {
        val top = parent.paddingTop + topPaddingPx
        val bottom = parent.height - parent.paddingBottom - bottomPaddingPx

        val childCount = parent.childCount
        val startPosition = if (drawBeforeFirst) {
            0
        } else {
            1
        }
        for (i in startPosition until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val left = child.right + params.rightMargin
            val right = left + dividerDrawable!!.intrinsicHeight
            dividerDrawable.setBounds(left, top, right, bottom)
            dividerDrawable.draw(c)
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (orientation == VERTICAL_LIST) {
            outRect.set(0, 0, 0, dividerDrawable!!.intrinsicHeight)
        } else {
            outRect.set(0, 0, dividerDrawable!!.intrinsicWidth, 0)
        }
    }
}