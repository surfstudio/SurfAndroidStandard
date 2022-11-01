/*
 * Copyright (c) 2019-present, SurfStudio LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ru.surfstudio.android.custom.view.shadow

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import ru.surfstudio.android.custom.view.R
import ru.surfstudio.android.imageloader.util.BlurUtil
import ru.surfstudio.android.logger.Logger

/**
 * Layout that creates blurred shadow of its children.
 *
 * How it works:
 * 1. Children are drawn on the canvas.
 * 2. This canvases bitmap is blurred by stack blur algorithm.
 * 3. Blurred bitmap is stored in [shadowBitmap] for reuse during next [dispatchDraw] passes.
 *    Every next pass will skip first three steps, if [shadowBitmap] exists.
 * 4. Children are drawn one more time on the top of the blurred bitmap.
 *
 * If the content of the layout has been changed, shadow could be updated with [redrawShadow] method.
 *
 * Some shadow parameters could be modified from .xml (offsets, blur radius, shadow alpha, etc.).
 * For more information see /res/attrs.xml.
 */
open class ShadowLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    companion object {
        const val DEFAULT_IS_ASYNC = false
        const val DEFAULT_RADIUS = 1
        const val DEFAULT_OFFSET = 0
        const val DEFAULT_ALPHA_PERCENT = 50
        const val DEFAULT_SHADOW_CLIP_TO_PADDING = false
        const val DEFAULT_DOWNSCALE_RATE = 4
        const val MIN_BLUR_RADIUS = 1
        const val MAX_BLUR_RADIUS = 25
        const val ALPHA_OPAQUE_INT = 255
    }

    private var shadowBitmap: Bitmap? = null
    private var shadowPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        isDither = true
        isFilterBitmap = true
    }

    private var shadowTopOffset: Int = DEFAULT_OFFSET
    private var shadowRightOffset: Int = DEFAULT_OFFSET
    private var shadowLeftOffset: Int = DEFAULT_OFFSET
    private var shadowBlurRadius: Int = DEFAULT_RADIUS
    private var shadowClippedToPadding: Boolean = DEFAULT_SHADOW_CLIP_TO_PADDING
    private var isAsync: Boolean = DEFAULT_IS_ASYNC

    private var shadowCreationDisposable = Disposables.disposed()

    /**
     * Enable or disable drawing of shadow.
     * Use case: sometime when view is disabled you don't need to draw shadow.
     */
    var shouldDrawShadow: Boolean = true
        set(value) {
            field = value
            redrawShadow()
        }

    /**
     * Shadow alpha in percents.
     */
    var shadowAlphaPercent: Int = DEFAULT_ALPHA_PERCENT
        set(value) {
            field = value
            shadowPaint.alpha = (value / 100f * ALPHA_OPAQUE_INT).toInt()
            invalidate()
        }

    /**
     * Color of the shadow.
     * Use case: sometimes you want draw shadow of the white background view.
     */
    @ColorInt
    var shadowColor: Int? = null
        set(value) {
            field = value
            shadowPaint.colorFilter = shadowColor?.let {
                PorterDuffColorFilter(it, PorterDuff.Mode.SRC_IN)
            }
            invalidate()
        }

    init {
        obtainAttrs(context, attrs)
        initView()
    }

    private fun obtainAttrs(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ShadowLayout)

        isAsync = typedArray.getBoolean(R.styleable.ShadowLayout_isAsync, DEFAULT_IS_ASYNC)
        shadowClippedToPadding = typedArray.getBoolean(R.styleable.ShadowLayout_shadowClippedToPadding, DEFAULT_SHADOW_CLIP_TO_PADDING)
        shadowBlurRadius = typedArray.getDimensionPixelSize(R.styleable.ShadowLayout_shadowRadius, DEFAULT_RADIUS)
        shadowLeftOffset = typedArray.getDimensionPixelOffset(R.styleable.ShadowLayout_shadowLeftOffset, DEFAULT_OFFSET)
        shadowRightOffset = typedArray.getDimensionPixelOffset(R.styleable.ShadowLayout_shadowRightOffset, DEFAULT_OFFSET)
        shadowTopOffset = typedArray.getDimensionPixelOffset(R.styleable.ShadowLayout_shadowTopOffset, DEFAULT_OFFSET)
        shadowAlphaPercent = typedArray.getInteger(R.styleable.ShadowLayout_shadowAlphaPercent, DEFAULT_ALPHA_PERCENT)
        if (typedArray.hasValue(R.styleable.ShadowLayout_shadowColor)) {
            shadowColor = typedArray.getColor(R.styleable.ShadowLayout_shadowColor, Color.TRANSPARENT)
        }
        typedArray.recycle()
    }

    private fun initView() {
        clipToPadding = false
        clipChildren = false
        if (!shadowClippedToPadding) {
            resetPadding()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        clearShadowBitmap()
        if (isAsync) {
            shadowCreationDisposable.dispose()
        }
    }

    override fun dispatchDraw(canvas: Canvas?) {
        if (shouldDrawShadow && shadowBitmap == null && width > 0 && height > 0) {
            val sourceBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val sourceCanvas = Canvas(sourceBitmap)
            background?.draw(sourceCanvas)
            super.dispatchDraw(sourceCanvas)

            when {
                isAsync -> subscribeToShadowCreation(sourceBitmap)
                else -> shadowBitmap = createShadowBitmap(sourceBitmap)
            }
        }

        shadowBitmap?.let {
            canvas?.drawBitmap(
                    it,
                    -shadowBlurRadius.toFloat() + shadowLeftOffset,
                    -shadowBlurRadius.toFloat() + shadowTopOffset,
                    shadowPaint
            )
        }

        super.dispatchDraw(canvas)
    }

    /**
     * Redrawing of the shadow (clearing the shadow bitmap, recreating and redrawing it).
     * Could be useful when the content of the child view changes.
     * For example after rebinding of the RecyclerView's item.
     */
    fun redrawShadow() {
        clearShadowBitmap()
        invalidate()
    }

    private fun subscribeToShadowCreation(sourceBitmap: Bitmap) {
        shadowCreationDisposable.dispose()
        shadowCreationDisposable = Single.fromCallable { createShadowBitmap(sourceBitmap) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { bitmap: Bitmap?, error: Throwable? ->
                    when {
                        error != null -> Logger.e(error)
                        bitmap != null -> {
                            shadowBitmap = bitmap
                            invalidate()
                        }
                    }
                }
    }

    private fun createShadowBitmap(sourceBitmap: Bitmap): Bitmap {
        val radius = safeCreateBlurRadius(DEFAULT_DOWNSCALE_RATE)
        val downscalePadding = (shadowBlurRadius / DEFAULT_DOWNSCALE_RATE)
        val downscaledBitmap = Bitmap.createScaledBitmap(
                sourceBitmap,
                ((sourceBitmap.width - shadowLeftOffset - shadowRightOffset) / DEFAULT_DOWNSCALE_RATE),
                (sourceBitmap.height / DEFAULT_DOWNSCALE_RATE),
                false
        )

        val blurredBitmap = createBlurredBitmap(downscaledBitmap, downscalePadding, radius)

        val shadowWidth = width + shadowBlurRadius * 2
        val widthScale: Float = (shadowWidth - shadowLeftOffset - shadowRightOffset) / shadowWidth.toFloat()

        val shadowHeight = height + shadowBlurRadius * 2
        return Bitmap.createScaledBitmap(
                blurredBitmap,
                (shadowWidth * widthScale).toInt(),
                shadowHeight,
                true
        )
    }

    private fun createBlurredBitmap(bitmap: Bitmap, padding: Int, radius: Int): Bitmap {
        return BlurUtil.renderScriptBlur(context, bitmap.setPadding(padding), radius)
    }

    private fun resetPadding() {
        // Set padding for shadow bitmap
        // int left, int top, int right, int bottom
        setPadding(
                paddingLeft + shadowBlurRadius - shadowLeftOffset,
                paddingTop + shadowBlurRadius - shadowTopOffset,
                paddingRight + shadowBlurRadius - shadowRightOffset,
                paddingBottom + shadowBlurRadius + shadowTopOffset
        )
    }

    @Suppress("SameParameterValue")
    private fun safeCreateBlurRadius(downscaleRate: Int): Int {
        val desiredRadius = shadowBlurRadius / downscaleRate
        return when {
            desiredRadius < MIN_BLUR_RADIUS -> MIN_BLUR_RADIUS
            desiredRadius > MAX_BLUR_RADIUS -> MAX_BLUR_RADIUS
            else -> desiredRadius
        }
    }

    private fun clearShadowBitmap() {
        if (shadowBitmap != null) {
            shadowBitmap?.recycle()
            shadowBitmap = null
        }
    }
}