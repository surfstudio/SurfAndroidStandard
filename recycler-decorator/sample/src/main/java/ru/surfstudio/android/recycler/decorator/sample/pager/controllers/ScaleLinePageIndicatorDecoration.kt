package ru.surfstudio.android.recycler.decorator.sample.pager.controllers

import android.animation.ArgbEvaluator
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import androidx.annotation.ColorInt
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.surfstudio.android.recycler.decorator.Decorator
import ru.surfstudio.android.recycler.decorator.sample.toPx
import kotlin.math.abs

const val MIN_SHOW_INDICATOR_VALUE = 1

/**
 * Декоратор, который добавляет отрисовку индикатора текущего выбранного элемента.
 * Индикатор имеет вид линии, которая изменяет свою длину для активного и неактивного состояний
 * Можно использовать с ViewPager2 и RecyclerView с PagerSnapHelper
 *
 * @property itemsCount число элементов в списке. Для бесконечного списка/карусели должно быть реальным числом элементов
 * @property widthInactive длина неактивного индикатора
 * @property widthActive длина активного индикатора
 * @property paddingBetween отступ между индикаторами
 * @property indicatorHeight высота индикатора
 * @property isRound края индикатора загруленные или нет
 * @property align выравнивание индикатора (слева/справа/по центру)
 * @property paddingBottom отступ индикаторов снизу
 * @property paddingLeftRight отступ индикаторов слева и справа
 * @property colorActive цвет активного индикатора
 * @property colorActive цвет неактивного индикатора
 */
class ScaleLinePageIndicatorDecoration(
        private val itemsCount: Int,
        private val widthInactive: Int = 8.toPx,
        private val widthActive: Int = 32.toPx,
        private val paddingBetween: Int = 16.toPx,
        private val indicatorHeight: Int = 8.toPx,
        private val isRound: Boolean = false,
        private val align: IndicatorAlign = IndicatorAlign.LEFT,
        private val paddingBottom: Int = 32.toPx,
        private val paddingLeftRight: Int = 32.toPx,
        @ColorInt val colorActive: Int = -0x1,
        @ColorInt val colorInactive: Int = 0x66FFFFFF
) : Decorator.RecyclerViewDecor {

    private val indicatorPaint = Paint()
    private val colorEvaluator = ArgbEvaluator()
    private val indicatorDiff = widthActive - widthInactive

    init {
        indicatorPaint.strokeCap = if (isRound) Paint.Cap.ROUND else Paint.Cap.SQUARE
        indicatorPaint.style = Paint.Style.STROKE
        indicatorPaint.strokeWidth = indicatorHeight.toFloat()
        indicatorPaint.isAntiAlias = true
    }

    override fun draw(
            canvas: Canvas,
            recyclerView: RecyclerView,
            state: RecyclerView.State
    ) {
        if (itemsCount <= MIN_SHOW_INDICATOR_VALUE) {
            return
        }

        val totalLength = widthInactive * (itemsCount - 1) + widthActive
        val paddingBetweenItems = (itemsCount - 1) * paddingBetween
        val indicatorTotalWidth = totalLength + paddingBetweenItems
        val indicatorStartX = when (align) {
            IndicatorAlign.LEFT -> paddingLeftRight.toFloat()
            IndicatorAlign.CENTER -> (recyclerView.width - indicatorTotalWidth) / 2f
            IndicatorAlign.RIGHT -> recyclerView.width - indicatorTotalWidth - paddingLeftRight.toFloat()
        }

        val indicatorPosY = recyclerView.height - indicatorHeight / 2f - paddingBottom

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
        val rect = Rect()
        recyclerView.getDecoratedBoundsWithMargins(recyclerView[0], rect)
        val progress = abs(rect.left.toFloat() / rect.width())
        drawIndicators(
                canvas,
                indicatorStartX,
                indicatorPosY,
                itemsCount,
                activePosition,
                progress
        )
    }

    private fun drawIndicators(
            canvas: Canvas,
            indicatorStartX: Float,
            indicatorPosY: Float,
            itemCount: Int,
            activePosition: Int,
            progress: Float
    ) {
        val diff = indicatorDiff * progress
        var start = indicatorStartX
        for (index in 0 until itemCount) {
            val indicatorWidth = calculateIndicatorWidth(index, activePosition, diff)
            indicatorPaint.color = calculateColor(index, activePosition, progress)
            canvas.drawLine(
                    start,
                    indicatorPosY,
                    start + indicatorWidth,
                    indicatorPosY,
                    indicatorPaint
            )
            start += (indicatorWidth + paddingBetween)
        }
    }

    private fun calculateIndicatorWidth(index: Int, activePosition: Int, diff: Float): Float {
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
    private fun calculateColor(index: Int, activePosition: Int, progress: Float): Int {
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
enum class IndicatorAlign {
    LEFT, CENTER, RIGHT
}