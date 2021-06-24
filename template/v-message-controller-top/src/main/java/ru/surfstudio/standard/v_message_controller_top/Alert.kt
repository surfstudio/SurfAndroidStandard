package ru.surfstudio.standard.v_message_controller_top

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import androidx.annotation.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.ViewCompat
import androidx.core.view.children
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.utilktx.ktx.ui.view.setTopMargin
import ru.surfstudio.standard.v_message_controller_top.databinding.AlerterAlertViewBinding

typealias OnHideAlertListener = () -> Unit
typealias OnShowAlertListener = () -> Unit

/**
 * Custom View для отображения уведомления
 */
class Alert @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle),
    View.OnClickListener,
    Animation.AnimationListener,
    SwipeDismissTouchListener.DismissCallbacks {

    private val binding = AlerterAlertViewBinding.inflate(LayoutInflater.from(context), this)

    companion object {

        /**
         * Продолжительность отображения уведомления на экране в миллисекундах
         */
        private const val DISPLAY_TIME_IN_SECONDS: Long = 3000

        private const val CLEAN_UP_DELAY_MILLIS = 100
    }

    /**
     * True если следует показывать иконку уведомления, иначе false
     */
    var showIcon: Boolean = true

    /**
     * true если пользователь может смахнуть уведомление
     */
    var isDismissible = false

    private var onShowListener: OnShowAlertListener? = null
    internal var onHideListener: OnHideAlertListener? = null

    internal var enterAnimation: Animation = AnimationUtils.loadAnimation(context, R.anim.alerter_slide_in_from_top)
    internal var exitAnimation: Animation = AnimationUtils.loadAnimation(context, R.anim.alerter_slide_out_to_top)

    internal var duration = DISPLAY_TIME_IN_SECONDS

    private var enableIconPulse = false
    private var enableInfiniteDuration: Boolean = false

    private var runningAnimation: Runnable? = null

    /**
     * Флаг для включения/выключения вибрационного отклика при отображении уведомления
     */
    private var vibrationEnabled = false

    init {
        isHapticFeedbackEnabled = true
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        ViewCompat.setTranslationZ(this, Float.MAX_VALUE)

        binding.llAlertBackground.setOnClickListener(this)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        enterAnimation.setAnimationListener(this)

        // Устанавливает анимацию появления, которая сработает при добавлении View к Window
        animation = enterAnimation
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // Проверка на наличие выреза
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val window = (context as? Activity)?.window
            val displayCutout = window?.decorView?.rootWindowInsets?.displayCutout
            val notchHeight = displayCutout?.safeInsetTop
            if (notchHeight != null) {
                binding.flClickShield.setTopMargin(notchHeight)
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    // Освобождаем ресурсы после того как вью откреплено
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        enterAnimation.setAnimationListener(null)
    }

    /* Override Methods */

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        super.performClick()
        return super.onTouchEvent(event)
    }

    override fun onClick(v: View) {
        if (isDismissible) {
            hide()
        }
    }

    override fun setOnClickListener(listener: OnClickListener?) {
        binding.llAlertBackground.setOnClickListener(listener)
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        children.forEach { it.visibility = visibility }
    }

    /* Interface Method Implementations */

    override fun onAnimationStart(animation: Animation) {
        if (!isInEditMode) {
            visibility = View.VISIBLE

            if (vibrationEnabled) {
                performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
            }

            if (showIcon) {
                binding.ivIcon.visibility = View.VISIBLE
                // Only pulse if we're not showing the progress
                if (enableIconPulse) {
                    binding.ivIcon.startAnimation(AnimationUtils.loadAnimation(context, R.anim.alerter_pulse))
                }
            } else {
                binding.ivIcon.visibility = View.GONE
            }
        }
    }

    override fun onAnimationEnd(animation: Animation) {
        onShowListener?.invoke()

        startHideAnimation()
    }

    private fun startHideAnimation() {
        // Запускает Handler для очистки уведомления
        if (!enableInfiniteDuration) {
            runningAnimation = Runnable { hide() }

            postDelayed(runningAnimation, duration)
        }
    }

    override fun onAnimationRepeat(animation: Animation) {
        // Ignore
    }

    /* Clean Up Methods */

    /**
     * Скрывает уведомление, которое в данный момент отображается
     */
    private fun hide() {
        try {
            exitAnimation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {
                    binding.llAlertBackground.setOnClickListener(null)
                    binding.llAlertBackground.isClickable = false
                }

                override fun onAnimationEnd(animation: Animation) {
                    removeFromParent()
                }

                override fun onAnimationRepeat(animation: Animation) {
                    // Ignore
                }
            })

            startAnimation(exitAnimation)
        } catch (ex: Exception) {
            Logger.e(javaClass.simpleName, Log.getStackTraceString(ex))
        }
    }

    /**
     * Удаляет вью из родительской ViewGroup
     */
    internal fun removeFromParent() {
        clearAnimation()
        visibility = View.GONE

        postDelayed(object : Runnable {
            override fun run() {
                try {
                    if (parent != null) {
                        try {
                            (parent as ViewGroup).removeView(this@Alert)

                            onHideListener?.invoke()
                        } catch (ex: Exception) {
                            Logger.e(javaClass.simpleName, "Cannot remove from parent layout")
                        }
                    }
                } catch (ex: Exception) {
                    Logger.e(javaClass.simpleName, Log.getStackTraceString(ex))
                }
            }
        }, CLEAN_UP_DELAY_MILLIS.toLong())
    }

    /* Setters and Getters */

    /**
     * Устанавливает цвет фона уведомления
     *
     * @param color The qualified colour integer
     */
    fun setAlertBackgroundColor(@ColorInt color: Int) {
        binding.llAlertBackground.setBackgroundColor(color)
    }

    /**
     * Устанавливает фоновый Drawable уведомления
     *
     * @param resource id drawable ресурса
     */
    fun setAlertBackgroundResource(@DrawableRes resource: Int) {
        binding.llAlertBackground.setBackgroundResource(resource)
    }

    /**
     * Устанавливает фоновый Drawable уведомления
     *
     * @param drawable устанавливаемый drawable
     */
    fun setAlertBackgroundDrawable(drawable: Drawable) {
        binding.llAlertBackground.background = drawable
    }

    /**
     * Устанавливает текст уведомления
     *
     * @param textId id строкового ресурса
     */
    fun setText(@StringRes textId: Int) {
        setText(context.getString(textId))
    }

    /**
     * Выключает обработку прикосновений на время пока уведомление отображается
     */
    fun disableOutsideTouch() {
        binding.flClickShield.isClickable = true
    }

    /**
     * Устанавливает typeface текста
     *
     * @param typeface typeface
     */
    fun setTextTypeface(typeface: Typeface) {
        binding.tvText.typeface = typeface
    }

    /**
     * Устанавливает текст уведомления
     *
     * @param text CharSequence текста
     */
    fun setText(text: CharSequence) {
        if (!TextUtils.isEmpty(text)) {
            binding.tvText.visibility = View.VISIBLE
            binding.tvText.text = text
        }
    }

    /**
     * Устанавливает TextAppearance текста уведомления
     *
     * @param textAppearance id ресурса стиля
     */
    @Suppress("DEPRECATION")
    fun setTextAppearance(@StyleRes textAppearance: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.tvText.setTextAppearance(textAppearance)
        } else {
            binding.tvText.setTextAppearance(binding.tvText.context, textAppearance)
        }
    }

    /**
     * Устанавливает иконку уведомления
     *
     * @param iconId id drawable ресурса
     */
    fun setIcon(@DrawableRes iconId: Int) {
        binding.ivIcon.setImageDrawable(AppCompatResources.getDrawable(context, iconId))
    }

    /**
     * Устанавливает цветовой фильтр иконки уведомления
     *
     * @param color Color int
     */
    fun setIconColorFilter(@ColorInt color: Int) {
        binding.ivIcon.setColorFilter(color)
    }

    /**
     * Устанавливает цветовой фильтр иконки уведомления
     *
     * @param colorFilter ColorFilter
     */
    fun setIconColorFilter(colorFilter: ColorFilter) {
        binding.ivIcon.colorFilter = colorFilter
    }

    /**
     * Устанавливает цветовой фильтр иконки уведомления
     *
     * @param color Color int
     * @param mode PorterDuff.Mode
     */
    fun setIconColorFilter(@ColorInt color: Int, mode: PorterDuff.Mode) {
        binding.ivIcon.setColorFilter(color, mode)
    }

    /**
     * Устанавливает иконку уведомления
     *
     * @param bitmap Bitmap
     */
    fun setIcon(bitmap: Bitmap) {
        binding.ivIcon.setImageBitmap(bitmap)
    }

    /**
     * Устанавливает иконку уведомления
     *
     * @param drawable Drawable
     */
    fun setIcon(drawable: Drawable) {
        binding.ivIcon.setImageDrawable(drawable)
    }

    /**
     * Включает возможность скрывать уведомление смахиванием
     */
    fun enableSwipeToDismiss() {
        binding.llAlertBackground.let {
            it.setOnTouchListener(SwipeDismissTouchListener(it, object : SwipeDismissTouchListener.DismissCallbacks {
                override fun canDismiss(): Boolean {
                    return true
                }

                override fun onDismiss(view: View) {
                    removeFromParent()
                }

                override fun onTouch(view: View, touch: Boolean) {
                    // Ignore
                }
            }))
        }
    }

    /**
     * Включает или выключает анимацию пульсации иконки
     *
     * @param shouldPulse True если следует включить анимацию пульсации
     */
    fun pulseIcon(shouldPulse: Boolean) {
        this.enableIconPulse = shouldPulse
    }

    /**
     * Включает или выключает бесконечную продолжительность отображения уведомления
     *
     * @param enableInfiniteDuration True если следует включить бесконечное отображение
     */
    fun setEnableInfiniteDuration(enableInfiniteDuration: Boolean) {
        this.enableInfiniteDuration = enableInfiniteDuration
    }

    /**
     * Устанавливает слушатель события появления уведомления на экране
     *
     * @param listener OnShowAlertListener
     */
    fun setOnShowListener(listener: OnShowAlertListener) {
        this.onShowListener = listener
    }

    /**
     * Включает или выключает вибрационный отклик на появление уведомления
     *
     * @param vibrationEnabled True для включения, False для выключения
     */
    fun setVibrationEnabled(vibrationEnabled: Boolean) {
        this.vibrationEnabled = vibrationEnabled
    }

    /**
     * Добавляет дополнительный отступ на размер тулбара
     */
    fun addAdditionalMargin(marginSize: Int) {
        setTopMargin(marginSize)
    }

    override fun canDismiss(): Boolean {
        return isDismissible
    }

    override fun onDismiss(view: View) {
        binding.flClickShield.removeView(binding.llAlertBackground)
    }

    override fun onTouch(view: View, touch: Boolean) {
        if (touch) {
            removeCallbacks(runningAnimation)
        } else {
            startHideAnimation()
        }
    }
}