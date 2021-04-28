package ru.surfstudio.android.recycler.decorator.sample.pager.controllers

import android.animation.ArgbEvaluator
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import androidx.annotation.ColorInt
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.surfstudio.android.easyadapter.EasyAdapter
import ru.surfstudio.android.recycler.decorator.Decorator
import ru.surfstudio.android.recycler.decorator.sample.toPx
import kotlin.math.abs

const val MIN_SHOW_INDICATOR_VALUE = 1

/**
 * Декоратор, который добавляет отрисовку индикатора текущего выбранного элемента.
 * Индикатор имеет вид линии, которая изменяет свою длину для активного и неактивного состояний
 * Можно использовать с ViewPager2 и RecyclerView с PagerSnapHelper
 *
 * @property hasInfiniteScroll есть ли у вью бесконечный скролл
 * @property verticalAlign расположение индиактора (снизу/сверху)
 * @property horizontalAlign выравнивание индикатора (слева/справа/по центру)
 * @property widthInactive длина неактивного индикатора
 * @property widthActive длина активного индикатора
 * @property paddingBetween отступ между индикаторами
 * @property indicatorHeight высота индикатора
 * @property paddingHorizontal отступ индикаторов слева и справа
 * @property indicatorRadius радиус индикаторов для отрисовки закругленных углов. Если ноль, будут отрисованы прямоугольные индикаторы
 * @property paddingBottom отступ индикаторов снизу от границ внутри(!) вью. По умолчанию индикатор рисуется поверх элементов ресайклер/вьюпейджер
 * Внимание, если необходимо, чтобы индикатор рисовался не поверх элементов, необходимо использовать офсет декоратор, а данный параметр оставить нулем!
 * @property colorActive цвет активного индикатора
 * @property colorInactive цвет неактивного индикатора
 */
class ScaleLinePageIndicatorDecoration(
        private val hasInfiniteScroll: Boolean = false,
        private val verticalAlign: VerticalAlign = VerticalAlign.BOTTOM,
        private val horizontalAlign: HorizontalAlign = HorizontalAlign.CENTER,
        private val widthInactive: Int = 8.toPx,
        private val widthActive: Int = 32.toPx,
        private val paddingBetween: Int = 16.toPx,
        private val indicatorHeight: Int = 8.toPx,
        private val indicatorRadius: Int = 0.toPx,
        private val paddingTop: Int = 0.toPx,
        private val paddingBottom: Int = 0.toPx,
        private val paddingHorizontal: Int = 32.toPx,
        @ColorInt val colorActive: Int = -0x1,
        @ColorInt val colorInactive: Int = 0x66FFFFFF
) : Decorator.RecyclerViewDecor {

    private val indicatorPaint = Paint().apply { isAntiAlias = true }
    private val colorEvaluator = ArgbEvaluator()
    private val indicatorDiff = widthActive - widthInactive
    private val rect = Rect()

    override fun draw(
            canvas: Canvas,
            recyclerView: RecyclerView,
            state: RecyclerView.State
    ) {
        val adapter = recyclerView.adapter as EasyAdapter
        val itemsCount = if (hasInfiniteScroll) {
            adapter.itemCount / EasyAdapter.INFINITE_SCROLL_LOOPS_COUNT
        } else {
            adapter.itemCount
        }

        if (itemsCount <= MIN_SHOW_INDICATOR_VALUE) {
            return
        }

        val totalLength = widthInactive * (itemsCount - 1) + widthActive
        val paddingBetweenItems = (itemsCount - 1) * paddingBetween
        val indicatorTotalWidth = totalLength + paddingBetweenItems
        val indicatorStartX = when (horizontalAlign) {
            HorizontalAlign.LEFT -> paddingHorizontal.toFloat()
            HorizontalAlign.CENTER -> (recyclerView.width - indicatorTotalWidth) / 2f
            HorizontalAlign.RIGHT -> recyclerView.width - indicatorTotalWidth - paddingHorizontal.toFloat()
        }

        val indicatorPosY = when (verticalAlign) {
            VerticalAlign.BOTTOM -> recyclerView.height - paddingBottom.toFloat()
            VerticalAlign.TOP -> paddingTop.toFloat()
        }

        val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
        val firstVisibleItemPosition = layoutManager?.findFirstVisibleItemPosition()
        val activePosition = if (firstVisibleItemPosition != null) {
            firstVisibleItemPosition % itemsCount
        } else {
            RecyclerView.NO_POSITION
        }
        if (activePosition == RecyclerView.NO_POSITION) {
            return
        }
        recyclerView.getDecoratedBoundsWithMargins(recyclerView[0], rect)
        val progress = abs(rect.left.toFloat() / rect.width())
        drawIndicators(
                canvas,
                indicatorStartX,
                indicatorPosY,
                itemsCount,
                activePosition,
                progress,
                verticalAlign
        )
    }

    private fun drawIndicators(
            canvas: Canvas,
            startX: Float,
            startY: Float,
            itemsCount: Int,
            activePosition: Int,
            progress: Float,
            verticalAlign: VerticalAlign
    ) {
        val diff = indicatorDiff * progress
        var currentStartX = startX
        for (index in 0 until itemsCount) {
            val indicatorWidth = calculateIndicatorWidth(index, activePosition, itemsCount, diff)
            indicatorPaint.color = calculateColor(index, activePosition, itemsCount, progress)
            val bottom = when (verticalAlign) {
                VerticalAlign.BOTTOM -> startY - indicatorHeight
                VerticalAlign.TOP -> startY + indicatorHeight
            }
            val rect = RectF(currentStartX, bottom, currentStartX + indicatorWidth, startY)
            canvas.drawRoundRect(rect, indicatorRadius.toFloat(), indicatorRadius.toFloat(), indicatorPaint)
            currentStartX += (indicatorWidth + paddingBetween)
        }
    }

    private fun calculateIndicatorWidth(index: Int, activePosition: Int, itemsCount: Int, diff: Float): Float {
        return when (index) {
            activePosition % itemsCount -> {
                widthActive - diff
            }
            (activePosition + 1) % itemsCount -> {
                widthInactive + diff
            }
            else -> {
                widthInactive.toFloat()
            }
        }
    }

    @ColorInt
    private fun calculateColor(index: Int, activePosition: Int, itemsCount: Int, progress: Float): Int {
        return when (index) {
            activePosition % itemsCount -> {
                colorEvaluator.evaluate(
                        progress,
                        colorActive,
                        colorInactive
                ) as Int
            }
            (activePosition + 1) % itemsCount -> {
                colorEvaluator.evaluate(
                        progress,
                        colorInactive,
                        colorActive
                ) as Int
            }
            else -> {
                colorInactive
            }
        }
    }
}

/**
 * Выравнивание индикаторов во вью
 */
enum class HorizontalAlign {
    LEFT, CENTER, RIGHT
}

/**
 * Расположение индикаторов в списке
 */
enum class VerticalAlign {
    TOP, BOTTOM
}