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
package ru.surfstudio.android.imageloader.transformations

import android.content.Context
import android.graphics.*
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool

/**
 * Трансформатор для скругления углов изображения. Особенности:
 * * настраиваемый радиус скругления угла в px;
 * * настраиваемый отступ от края в px;
 * * возможность скругления конкретных углов.
 */
class RoundedCornersTransformation(
        private val roundedCornersBundle: RoundedCornersBundle = RoundedCornersBundle()
) : BaseGlideImageTransformation() {

    private val diameter: Int = roundedCornersBundle.radiusPx * 2

    override fun getId() = "ru.surfstudio.android.imageloader.transformations.RoundedCornersTransformation"

    override fun transform(
            context: Context,
            pool: BitmapPool,
            toTransform: Bitmap,
            outWidth: Int,
            outHeight: Int
    ): Bitmap? {
        val width = toTransform.width
        val height = toTransform.height

        var bitmap: Bitmap? = pool.get(width, height, Bitmap.Config.ARGB_8888)
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        }

        val canvas = Canvas(bitmap!!)
        val paint = Paint()
        paint.isAntiAlias = true
        paint.shader = BitmapShader(toTransform, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        drawRoundRect(canvas, paint, width.toFloat(), height.toFloat())
        return bitmap
    }

    override fun hashCode() = getId().hashCode()

    override fun equals(other: Any?) = other is RoundedCornersTransformation

    private fun drawRoundRect(canvas: Canvas, paint: Paint, width: Float, height: Float) {
        val right = width - roundedCornersBundle.marginPx
        val bottom = height - roundedCornersBundle.marginPx

        when (roundedCornersBundle.cornerType) {
            RoundedCornersTransformation.CornerType.ALL ->
                canvas.drawRoundRect(
                        RectF(
                                roundedCornersBundle.marginPx.toFloat(),
                                roundedCornersBundle.marginPx.toFloat(),
                                right,
                                bottom
                        ),
                        roundedCornersBundle.radiusPx.toFloat(),
                        roundedCornersBundle.radiusPx.toFloat(),
                        paint)
            RoundedCornersTransformation.CornerType.TOP_LEFT ->
                drawTopLeftRoundRect(canvas, paint, right, bottom)
            RoundedCornersTransformation.CornerType.TOP_RIGHT ->
                drawTopRightRoundRect(canvas, paint, right, bottom)
            RoundedCornersTransformation.CornerType.BOTTOM_LEFT ->
                drawBottomLeftRoundRect(canvas, paint, right, bottom)
            RoundedCornersTransformation.CornerType.BOTTOM_RIGHT ->
                drawBottomRightRoundRect(canvas, paint, right, bottom)
            RoundedCornersTransformation.CornerType.TOP ->
                drawTopRoundRect(canvas, paint, right, bottom)
            RoundedCornersTransformation.CornerType.BOTTOM ->
                drawBottomRoundRect(canvas, paint, right, bottom)
            RoundedCornersTransformation.CornerType.LEFT ->
                drawLeftRoundRect(canvas, paint, right, bottom)
            RoundedCornersTransformation.CornerType.RIGHT ->
                drawRightRoundRect(canvas, paint, right, bottom)
            RoundedCornersTransformation.CornerType.EXCEPT_TOP_LEFT ->
                drawExceptTopLeftRoundRect(canvas, paint, right, bottom)
            RoundedCornersTransformation.CornerType.EXCEPT_TOP_RIGHT ->
                drawExceptTopRightRoundRect(canvas, paint, right, bottom)
            RoundedCornersTransformation.CornerType.EXCEPT_BOTTOM_LEFT ->
                drawExceptBottomLeftRoundRect(canvas, paint, right, bottom)
            RoundedCornersTransformation.CornerType.EXCEPT_BOTTOM_RIGHT ->
                drawExceptBottomRightRoundRect(canvas, paint, right, bottom)
            RoundedCornersTransformation.CornerType.DIAGONAL_TOP_LEFT ->
                drawDiagonalTopLeftRoundRect(canvas, paint, right, bottom)
            RoundedCornersTransformation.CornerType.DIAGONAL_TOP_RIGHT ->
                drawDiagonalTopRightRoundRect(canvas, paint, right, bottom)
        }
    }

    private fun drawTopLeftRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(
                RectF(
                        roundedCornersBundle.marginPx.toFloat(),
                        roundedCornersBundle.marginPx.toFloat(),
                        (roundedCornersBundle.marginPx + diameter).toFloat(),
                        (roundedCornersBundle.marginPx + diameter).toFloat()
                ),
                roundedCornersBundle.radiusPx.toFloat(),
                roundedCornersBundle.radiusPx.toFloat(),
                paint)
        canvas.drawRect(
                RectF(
                        roundedCornersBundle.marginPx.toFloat(),
                        (roundedCornersBundle.marginPx + roundedCornersBundle.radiusPx).toFloat(),
                        (roundedCornersBundle.marginPx + roundedCornersBundle.radiusPx).toFloat(),
                        bottom
                ),
                paint)
        canvas.drawRect(
                RectF(
                        (roundedCornersBundle.marginPx + roundedCornersBundle.radiusPx).toFloat(),
                        roundedCornersBundle.marginPx.toFloat(),
                        right, bottom
                ),
                paint)
    }

    private fun drawTopRightRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(
                RectF(
                        right - diameter,
                        roundedCornersBundle.marginPx.toFloat(),
                        right,
                        (roundedCornersBundle.marginPx + diameter).toFloat()
                ),
                roundedCornersBundle.radiusPx.toFloat(),
                roundedCornersBundle.radiusPx.toFloat(),
                paint)
        canvas.drawRect(
                RectF(
                        roundedCornersBundle.marginPx.toFloat(),
                        roundedCornersBundle.marginPx.toFloat(),
                        right - roundedCornersBundle.radiusPx,
                        bottom
                ),
                paint)
        canvas.drawRect(
                RectF(
                        right - roundedCornersBundle.radiusPx,
                        (roundedCornersBundle.marginPx + roundedCornersBundle.radiusPx).toFloat(),
                        right,
                        bottom
                ),
                paint)
    }

    private fun drawBottomLeftRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(
                RectF(
                        roundedCornersBundle.marginPx.toFloat(),
                        bottom - diameter,
                        (roundedCornersBundle.marginPx + diameter).toFloat(),
                        bottom
                ),
                roundedCornersBundle.radiusPx.toFloat(),
                roundedCornersBundle.radiusPx.toFloat(),
                paint)
        canvas.drawRect(
                RectF(
                        roundedCornersBundle.marginPx.toFloat(),
                        roundedCornersBundle.marginPx.toFloat(),
                        (roundedCornersBundle.marginPx + diameter).toFloat(),
                        bottom - roundedCornersBundle.radiusPx
                ),
                paint)
        canvas.drawRect(
                RectF(
                        (roundedCornersBundle.marginPx + roundedCornersBundle.radiusPx).toFloat(),
                        roundedCornersBundle.marginPx.toFloat(),
                        right,
                        bottom
                ),
                paint)
    }

    private fun drawBottomRightRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(
                RectF(
                        right - diameter,
                        bottom - diameter,
                        right,
                        bottom
                ),
                roundedCornersBundle.radiusPx.toFloat(),
                roundedCornersBundle.radiusPx.toFloat(),
                paint)
        canvas.drawRect(
                RectF(
                        roundedCornersBundle.marginPx.toFloat(),
                        roundedCornersBundle.marginPx.toFloat(),
                        right - roundedCornersBundle.radiusPx,
                        bottom
                ),
                paint)
        canvas.drawRect(
                RectF(
                        right - roundedCornersBundle.radiusPx,
                        roundedCornersBundle.marginPx.toFloat(),
                        right,
                        bottom - roundedCornersBundle.radiusPx
                ),
                paint)
    }

    private fun drawTopRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(
                RectF(
                        roundedCornersBundle.marginPx.toFloat(),
                        roundedCornersBundle.marginPx.toFloat(),
                        right,
                        (roundedCornersBundle.marginPx + diameter).toFloat()
                ),
                roundedCornersBundle.radiusPx.toFloat(),
                roundedCornersBundle.radiusPx.toFloat(),
                paint)
        canvas.drawRect(
                RectF(
                        roundedCornersBundle.marginPx.toFloat(),
                        (roundedCornersBundle.marginPx + roundedCornersBundle.radiusPx).toFloat(),
                        right,
                        bottom
                ),
                paint)
    }

    private fun drawBottomRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(
                RectF(
                        roundedCornersBundle.marginPx.toFloat(),
                        bottom - diameter,
                        right,
                        bottom
                ),
                roundedCornersBundle.radiusPx.toFloat(),
                roundedCornersBundle.radiusPx.toFloat(),
                paint)
        canvas.drawRect(
                RectF(
                        roundedCornersBundle.marginPx.toFloat(),
                        roundedCornersBundle.marginPx.toFloat(),
                        right,
                        bottom - roundedCornersBundle.radiusPx
                ),
                paint)
    }

    private fun drawLeftRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(
                RectF(
                        roundedCornersBundle.marginPx.toFloat(),
                        roundedCornersBundle.marginPx.toFloat(),
                        (roundedCornersBundle.marginPx + diameter).toFloat(),
                        bottom
                ),
                roundedCornersBundle.radiusPx.toFloat(),
                roundedCornersBundle.radiusPx.toFloat(),
                paint)
        canvas.drawRect(
                RectF(
                        (roundedCornersBundle.marginPx + roundedCornersBundle.radiusPx).toFloat(),
                        roundedCornersBundle.marginPx.toFloat(),
                        right,
                        bottom
                ),
                paint)
    }

    private fun drawRightRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(
                RectF(
                        right - diameter,
                        roundedCornersBundle.marginPx.toFloat(),
                        right,
                        bottom
                ),
                roundedCornersBundle.radiusPx.toFloat(),
                roundedCornersBundle.radiusPx.toFloat(),
                paint)
        canvas.drawRect(
                RectF(
                        roundedCornersBundle.marginPx.toFloat(),
                        roundedCornersBundle.marginPx.toFloat(),
                        right - roundedCornersBundle.radiusPx,
                        bottom
                ),
                paint)
    }

    private fun drawExceptTopLeftRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(
                RectF(
                        roundedCornersBundle.marginPx.toFloat(),
                        bottom - diameter,
                        right, bottom
                ),
                roundedCornersBundle.radiusPx.toFloat(),
                roundedCornersBundle.radiusPx.toFloat(),
                paint)
        canvas.drawRoundRect(
                RectF(
                        right - diameter,
                        roundedCornersBundle.marginPx.toFloat(),
                        right,
                        bottom
                ),
                roundedCornersBundle.radiusPx.toFloat(),
                roundedCornersBundle.radiusPx.toFloat(),
                paint)
        canvas.drawRect(
                RectF(
                        roundedCornersBundle.marginPx.toFloat(),
                        roundedCornersBundle.marginPx.toFloat(),
                        right - roundedCornersBundle.radiusPx,
                        bottom - roundedCornersBundle.radiusPx
                ),
                paint)
    }

    private fun drawExceptTopRightRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(
                RectF(
                        roundedCornersBundle.marginPx.toFloat(),
                        roundedCornersBundle.marginPx.toFloat(),
                        (roundedCornersBundle.marginPx + diameter).toFloat(),
                        bottom
                ),
                roundedCornersBundle.radiusPx.toFloat(),
                roundedCornersBundle.radiusPx.toFloat(),
                paint)
        canvas.drawRoundRect(
                RectF(
                        roundedCornersBundle.marginPx.toFloat(),
                        bottom - diameter,
                        right,
                        bottom
                ),
                roundedCornersBundle.radiusPx.toFloat(),
                roundedCornersBundle.radiusPx.toFloat(),
                paint)
        canvas.drawRect(
                RectF(
                        (roundedCornersBundle.marginPx + roundedCornersBundle.radiusPx).toFloat(),
                        roundedCornersBundle.marginPx.toFloat(),
                        right,
                        bottom - roundedCornersBundle.radiusPx
                ),
                paint)
    }

    private fun drawExceptBottomLeftRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(
                RectF(
                        roundedCornersBundle.marginPx.toFloat(),
                        roundedCornersBundle.marginPx.toFloat(),
                        right,
                        (roundedCornersBundle.marginPx + diameter).toFloat()
                ),
                roundedCornersBundle.radiusPx.toFloat(),
                roundedCornersBundle.radiusPx.toFloat(),
                paint)
        canvas.drawRoundRect(
                RectF(
                        right - diameter,
                        roundedCornersBundle.marginPx.toFloat(),
                        right,
                        bottom
                ),
                roundedCornersBundle.radiusPx.toFloat(),
                roundedCornersBundle.radiusPx.toFloat(),
                paint)
        canvas.drawRect(
                RectF(
                        roundedCornersBundle.marginPx.toFloat(),
                        (roundedCornersBundle.marginPx + roundedCornersBundle.radiusPx).toFloat(),
                        right - roundedCornersBundle.radiusPx,
                        bottom
                ),
                paint)
    }

    private fun drawExceptBottomRightRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(
                RectF(
                        roundedCornersBundle.marginPx.toFloat(),
                        roundedCornersBundle.marginPx.toFloat(),
                        right,
                        (roundedCornersBundle.marginPx + diameter).toFloat()
                ),
                roundedCornersBundle.radiusPx.toFloat(),
                roundedCornersBundle.radiusPx.toFloat(),
                paint)
        canvas.drawRoundRect(
                RectF(
                        roundedCornersBundle.marginPx.toFloat(),
                        roundedCornersBundle.marginPx.toFloat(),
                        (roundedCornersBundle.marginPx + diameter).toFloat(),
                        bottom
                ),
                roundedCornersBundle.radiusPx.toFloat(),
                roundedCornersBundle.radiusPx.toFloat(),
                paint)
        canvas.drawRect(
                RectF(
                        (roundedCornersBundle.marginPx + roundedCornersBundle.radiusPx).toFloat(),
                        (roundedCornersBundle.marginPx + roundedCornersBundle.radiusPx).toFloat(),
                        right,
                        bottom
                ),
                paint)
    }

    private fun drawDiagonalTopLeftRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(
                RectF(
                        roundedCornersBundle.marginPx.toFloat(),
                        roundedCornersBundle.marginPx.toFloat(),
                        (roundedCornersBundle.marginPx + diameter).toFloat(),
                        (roundedCornersBundle.marginPx + diameter).toFloat()
                ),
                roundedCornersBundle.radiusPx.toFloat(),
                roundedCornersBundle.radiusPx.toFloat(),
                paint)
        canvas.drawRoundRect(
                RectF(
                        right - diameter,
                        bottom - diameter,
                        right,
                        bottom
                ),
                roundedCornersBundle.radiusPx.toFloat(),
                roundedCornersBundle.radiusPx.toFloat(),
                paint)
        canvas.drawRect(
                RectF(
                        roundedCornersBundle.marginPx.toFloat(),
                        (roundedCornersBundle.marginPx + roundedCornersBundle.radiusPx).toFloat(),
                        right - roundedCornersBundle.radiusPx, bottom
                ),
                paint)
        canvas.drawRect(
                RectF(
                        (roundedCornersBundle.marginPx + roundedCornersBundle.radiusPx).toFloat(),
                        roundedCornersBundle.marginPx.toFloat(),
                        right,
                        bottom - roundedCornersBundle.radiusPx
                ),
                paint)
    }

    private fun drawDiagonalTopRightRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(
                RectF(
                        right - diameter,
                        roundedCornersBundle.marginPx.toFloat(),
                        right,
                        (roundedCornersBundle.marginPx + diameter).toFloat()
                ),
                roundedCornersBundle.radiusPx.toFloat(),
                roundedCornersBundle.radiusPx.toFloat(),
                paint)
        canvas.drawRoundRect(
                RectF(
                        roundedCornersBundle.marginPx.toFloat(),
                        bottom - diameter,
                        (roundedCornersBundle.marginPx + diameter).toFloat(),
                        bottom
                ),
                roundedCornersBundle.radiusPx.toFloat(),
                roundedCornersBundle.radiusPx.toFloat(),
                paint)
        canvas.drawRect(
                RectF(
                        roundedCornersBundle.marginPx.toFloat(),
                        roundedCornersBundle.marginPx.toFloat(),
                        right - roundedCornersBundle.radiusPx,
                        bottom - roundedCornersBundle.radiusPx
                ),
                paint)
        canvas.drawRect(
                RectF(
                        (roundedCornersBundle.marginPx + roundedCornersBundle.radiusPx).toFloat(),
                        (roundedCornersBundle.marginPx + roundedCornersBundle.radiusPx).toFloat(),
                        right,
                        bottom
                ),
                paint)
    }

    /**
     * Угол для скругления.
     */
    enum class CornerType {
        ALL,
        TOP, TOP_LEFT, TOP_RIGHT,
        BOTTOM, BOTTOM_LEFT, BOTTOM_RIGHT,
        LEFT, RIGHT,
        EXCEPT_TOP_LEFT, EXCEPT_TOP_RIGHT, EXCEPT_BOTTOM_LEFT, EXCEPT_BOTTOM_RIGHT,
        DIAGONAL_TOP_LEFT, DIAGONAL_TOP_RIGHT
    }

    /**
     * Конфигурационные данных для трансформации [RoundedCornersTransformation].
     */
    data class RoundedCornersBundle(val isRoundedCorners: Boolean = false,
                                    val cornerType: CornerType = CornerType.ALL,
                                    val radiusPx: Int = 0,
                                    val marginPx: Int = 0)
}