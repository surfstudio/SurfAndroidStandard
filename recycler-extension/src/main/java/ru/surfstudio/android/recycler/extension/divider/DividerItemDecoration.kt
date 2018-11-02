package ru.surfstudio.android.recycler.extension.divider

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout

/**
 * Класс для создания разделителей у RecyclerView
 *
 * @param context контекст
 * @param resId id drawable-ресурса, который будет использоваться для отрисовки разделителей
 * @param orientation ориентация списка
 * @param headerCount количество верхних видимых элементов списка, между которыми не будет разделителей
 * @param footerCount количество нижних видимых элементов списка, между которыми не будет разделителей
 * @param leftPaddingPx padding слева между разделителями и RecyclerView (для вертикальных списков)
 * @param rightPaddingPx padding справа между разделителями и RecyclerView (для вертикальных списков)
 * @param topPaddingPx padding сверху между разделятелями и RecyclerView (для горизонтальных списков)
 * @param bottomPaddingPx padding снизу между разделятелями и RecyclerView (для горизонтальных списков)
 * @param drawBeforeFirst отрисовывать ли разделитель перед первым элементом списка
 * @param drawAfterLast отрисовывать ли разделитель после последнего элемента списка
 */
@SuppressLint("DuplicateDivider")
class DividerItemDecoration @JvmOverloads constructor(
        context: Context,
        @DrawableRes resId: Int,
        orientation: Int,
        headerCount: Int = 0,
        footerCount: Int = 0,
        var leftPaddingPx: Int = 0,
        var rightPaddingPx: Int = 0,
        var topPaddingPx: Int = 0,
        var bottomPaddingPx: Int = 0,
        var drawBeforeFirst: Boolean = false,
        var drawAfterLast: Boolean = false
) : RecyclerView.ItemDecoration() {

    companion object {
        const val VERTICAL_LIST = LinearLayout.VERTICAL
        const val HORIZONTAL_LIST = LinearLayout.HORIZONTAL
    }

    private lateinit var dividerDrawable: Drawable

    private var orientation: Int = VERTICAL_LIST

    private var headerCount: Int = 0
    private var footerCount: Int = 0

    init {
        setDividerDrawable(context, resId)
        setOrientation(orientation)
        setHeaderCount(headerCount)
        setFooterCount(footerCount)
    }

    fun setDividerDrawable(context: Context, @DrawableRes resId: Int) {
        val dividerDrawable = ContextCompat.getDrawable(context, resId)
                ?: throw IllegalArgumentException("drawable cannot be null")
        this.dividerDrawable = dividerDrawable
    }

    fun setOrientation(orientation: Int) {
        if (orientation != VERTICAL_LIST && orientation != HORIZONTAL_LIST) {
            throw IllegalArgumentException("invalid orientation")
        }
        this.orientation = orientation
    }

    fun setHeaderCount(headerCount: Int) {
        if (headerCount < 0) {
            throw IllegalArgumentException("header count must be greater than 0")
        }
        this.headerCount = headerCount
    }

    fun setFooterCount(footerCount: Int) {
        if (footerCount < 0) {
            throw IllegalArgumentException("footer count must be greater than zero")
        }
        this.footerCount = footerCount
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (orientation == VERTICAL_LIST) {
            drawVertical(canvas, parent)
        } else {
            drawHorizontal(canvas, parent)
        }
    }

    fun drawVertical(canvas: Canvas, parent: RecyclerView) {
        val startPosition = getStartPositionForDrawing(parent)
        val finishPosition = getFinishPositionForDrawing(parent)

        for (i in startPosition until finishPosition) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams

            val left = parent.paddingLeft + leftPaddingPx
            val top = child.bottom + params.bottomMargin
            val right = parent.width - parent.paddingRight - rightPaddingPx
            val bottom = top + dividerDrawable.intrinsicHeight

            dividerDrawable.setBounds(left, top, right, bottom)
            dividerDrawable.draw(canvas)
        }
    }

    fun drawHorizontal(canvas: Canvas, parent: RecyclerView) {
        val startPosition = getStartPositionForDrawing(parent)
        val finishPosition = getFinishPositionForDrawing(parent)

        for (i in startPosition until finishPosition) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams

            val left = child.right + params.rightMargin
            val top = parent.paddingTop + topPaddingPx
            val right = left + dividerDrawable.intrinsicWidth
            val bottom = parent.height - parent.paddingBottom - bottomPaddingPx

            dividerDrawable.setBounds(left, top, right, bottom)
            dividerDrawable.draw(canvas)
        }
    }

    private fun getStartPositionForDrawing(parent: RecyclerView): Int {
        val currentFirstItemChild = parent.getChildAt(0)
        var startPositionForDrawing = if (headerCount > 0) headerCount - 1 else 0
        if (isFirstItem(currentFirstItemChild, parent) && !drawBeforeFirst) {
            if (startPositionForDrawing == 0) {
                startPositionForDrawing = 1
            } else {
                startPositionForDrawing++
            }
        }
        return startPositionForDrawing
    }

    private fun getFinishPositionForDrawing(parent: RecyclerView): Int {
        val currentLastItemChild = parent.getChildAt(parent.childCount - 1)
        var finishPositionForDrawing = parent.childCount - footerCount
        if (isLastItem(currentLastItemChild, parent)) {
            if (drawAfterLast) {
                if (footerCount > 0) {
                    finishPositionForDrawing++
                }
            } else {
                if (footerCount == 0) {
                    finishPositionForDrawing--
                }
            }
        }
        return finishPositionForDrawing
    }

    private fun isFirstItem(child: View, parent: RecyclerView): Boolean {
        return parent.getChildAdapterPosition(child) == 0
    }

    private fun isLastItem(child: View, parent: RecyclerView): Boolean {
        return parent.getChildAdapterPosition(child) == parent.adapter!!.itemCount - 1
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (orientation == VERTICAL_LIST) {
            outRect.set(0, 0, 0, dividerDrawable.intrinsicHeight)
        } else {
            outRect.set(0, 0, dividerDrawable.intrinsicWidth, 0)
        }
    }
}