/*
  Copyright (c) 2018-present, SurfStudio LLC.

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
package ru.surfstudio.android.recycler.extension.divider

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

/**
 * Класс для создания разделителей у RecyclerView
 *
 * @param context контекст
 * @param orientation ориентация списка
 * @param resId id drawable-ресурса, который будет использоваться для отрисовки разделителей
 * @param lastItemsCountWithoutDividers количество видимых элементов в конце списка, между которыми не будет разделителей
 * @param firstItemsCountWithoutDividers количество видимых элементов в начале списка, между которыми не будет разделителей
 * @param leftPaddingPx padding слева между разделителями и RecyclerView (для вертикальных списков)
 * @param rightPaddingPx padding справа между разделителями и RecyclerView (для вертикальных списков)
 * @param topPaddingPx padding сверху между разделятелями и RecyclerView (для горизонтальных списков)
 * @param bottomPaddingPx padding снизу между разделятелями и RecyclerView (для горизонтальных списков)
 */
@SuppressLint("DuplicateDivider")
class DividerItemDecoration @JvmOverloads constructor(
        context: Context,
        orientation: Int,
        @DrawableRes resId: Int,
        lastItemsCountWithoutDividers: Int = 0,
        firstItemsCountWithoutDividers: Int = 0,
        var leftPaddingPx: Int = 0,
        var rightPaddingPx: Int = 0,
        var topPaddingPx: Int = 0,
        var bottomPaddingPx: Int = 0
) : RecyclerView.ItemDecoration() {

    companion object {
        const val VERTICAL_LIST = LinearLayout.VERTICAL
        const val HORIZONTAL_LIST = LinearLayout.HORIZONTAL
    }

    private lateinit var dividerDrawable: Drawable

    private var orientation: Int = VERTICAL_LIST

    private var firstItemsCountWithoutDividers: Int = 0
    private var lastItemsCountWithoutDividers: Int = 0

    init {
        setDividerDrawable(context, resId)
        setOrientation(orientation)
        setFirstItemsCountWithoutDividers(firstItemsCountWithoutDividers)
        setLastItemsCountWithoutDividers(lastItemsCountWithoutDividers)
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

    fun setFirstItemsCountWithoutDividers(firstItemsCountWithoutDividers: Int) {
        if (firstItemsCountWithoutDividers < 0) {
            throw IllegalArgumentException("firstItemsCountWithoutDividers must be greater than 0")
        }
        this.firstItemsCountWithoutDividers = firstItemsCountWithoutDividers
    }

    fun setLastItemsCountWithoutDividers(lastItemsCountWithoutDividers: Int) {
        if (lastItemsCountWithoutDividers < 0) {
            throw IllegalArgumentException("lastItemsCountWithoutDividers must be greater than 0")
        }
        this.lastItemsCountWithoutDividers = lastItemsCountWithoutDividers
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
        val finishPosition = parent.childCount - lastItemsCountWithoutDividers

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
        val finishPosition = parent.childCount - lastItemsCountWithoutDividers

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
        return if (isFirstItem(currentFirstItemChild, parent)) {
            if (firstItemsCountWithoutDividers > 0) {
                firstItemsCountWithoutDividers
            } else {
                1
            }
        } else {
            if (firstItemsCountWithoutDividers > 0) {
                firstItemsCountWithoutDividers - 1
            } else {
                0
            }
        }
    }

    private fun isFirstItem(child: View, parent: RecyclerView): Boolean {
        return parent.getChildAdapterPosition(child) == 0
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (orientation == VERTICAL_LIST) {
            outRect.set(0, 0, 0, dividerDrawable.intrinsicHeight)
        } else {
            outRect.set(0, 0, dividerDrawable.intrinsicWidth, 0)
        }
    }
}