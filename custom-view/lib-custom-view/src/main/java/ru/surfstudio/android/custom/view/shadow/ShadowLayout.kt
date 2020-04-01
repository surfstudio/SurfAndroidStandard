package ru.surfstudio.android.custom.view.shadow

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.FrameLayout
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import ru.surfstudio.android.custom.view.R
import ru.surfstudio.android.imageloader.util.BlurUtil
import ru.surfstudio.android.logger.Logger

/**
 * Layout that creates blurred shadow of its children.
 */
class ShadowLayout @JvmOverloads constructor(
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
    }

    private var shadowBitmap: Bitmap? = null
    private var shadowPaint: Paint? = null

    private var shadowTopOffset: Int = DEFAULT_OFFSET
    private var shadowRightOffset: Int = DEFAULT_OFFSET
    private var shadowLeftOffset: Int = DEFAULT_OFFSET
    private var shadowBlurRadius: Int = DEFAULT_RADIUS
    private var shadowAlphaPercent: Int = DEFAULT_ALPHA_PERCENT
    private var shadowClippedToPadding: Boolean = DEFAULT_SHADOW_CLIP_TO_PADDING
    private var isAsync: Boolean = DEFAULT_IS_ASYNC

    private var disposable = Disposables.disposed()

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
        typedArray.recycle()
    }

    private fun initView() {
        clipToPadding = false
        clipChildren = false
        shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            isDither = true
            isFilterBitmap = true
            alpha = (shadowAlphaPercent / 100f * 255).toInt()
        }
        if (!shadowClippedToPadding) {
            resetPadding()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        clearShadowBitmap()
        disposable.dispose()
    }

    override fun dispatchDraw(canvas: Canvas?) {
        if (shadowBitmap == null) {
            val sourceBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val sourceCanvas = Canvas(sourceBitmap)
            background?.draw(sourceCanvas)
            super.dispatchDraw(sourceCanvas)

            if (isAsync) {
                disposable.dispose()
                disposable = Single.fromCallable { createShadowBitmap(sourceBitmap) }
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
            } else {
                shadowBitmap = createShadowBitmap(sourceBitmap)
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