package ru.surfstudio.android.recycler.decorator.sample.pager.controllers

import android.graphics.*
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import ru.surfstudio.android.recycler.decorator.Decorator
import ru.surfstudio.android.recycler.decorator.sample.toPx

/**
 * Декоратор добавляющий отрисовку градиента в ресайклер вью
 * Градиент отрисовывается внизу ресайклер вью и может быть использован, например, для подложки индикатора
 *
 * @param height высота градиента
 * @param paddingLeft отступ градиента слева
 * @param paddingRight отступ градиента справа
 * @param colorStart начальный цвет градиента
 * @param colorEnd конечный цвет градиента
 */
class GradientBackgroundDecoration(
        private val height: Int = 56.toPx,
        private val paddingLeft: Int = 16.toPx,
        private val paddingRight: Int = 16.toPx,
        @ColorInt val colorStart: Int = Color.WHITE,
        @ColorInt val colorEnd: Int = Color.BLACK
) : Decorator.RecyclerViewDecor {

    private val gradientPaint = Paint()

    override fun draw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        drawGradientBackground(c, parent)
    }

    private fun drawGradientBackground(canvas: Canvas, parent: RecyclerView) {
        val gradientPosX = parent.width / 2f
        val gradientStartPosY = parent.height - height.toFloat()
        val gradientEndPosY = parent.height.toFloat()
        val gradient = LinearGradient(
                gradientPosX,
                gradientStartPosY,
                gradientPosX,
                gradientEndPosY,
                colorStart,
                colorEnd,
                Shader.TileMode.CLAMP
        )
        gradientPaint.shader = gradient
        gradientPaint.isAntiAlias = true
        canvas.drawRect(
                paddingLeft.toFloat(),
                gradientStartPosY,
                parent.width.toFloat() - paddingRight,
                parent.height.toFloat(),
                gradientPaint
        );
    }
}