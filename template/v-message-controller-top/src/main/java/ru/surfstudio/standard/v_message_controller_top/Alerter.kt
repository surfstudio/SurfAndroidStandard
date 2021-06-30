package ru.surfstudio.standard.v_message_controller_top

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.annotation.*
import androidx.core.view.ViewCompat
import java.lang.ref.WeakReference

/**
 * Класс, временно прикрепляющий уведомление к контенту текущей активити поверх остальных вью.
 */
class Alerter private constructor() {

    companion object {

        private var activityWeakReference: WeakReference<Activity>? = null

        /**
         * Создает уведомление и сохраняет ссылку на активити из которой был вызван метод
         *
         * @param activity Activity в которой будет показано уведомление
         * @return This Alerter
         */
        @JvmStatic
        fun create(activity: Activity?): Alerter {
            if (activity == null) {
                throw IllegalArgumentException("Activity cannot be null!")
            }

            val alerter = Alerter()

            // Скрывает текущее уведомление, если есть активное
            clearCurrent(activity)

            alerter.setActivity(activity)
            alerter.alert = Alert(activity)
            return alerter
        }

        /**
         * Очищает отображаемое уведомление если есть активное
         *
         * @param activity Текущая активити
         */
        @JvmStatic
        fun clearCurrent(activity: Activity?) {
            activity ?: return
            activity.runOnUiThread {
                (activity.window.decorView as? ViewGroup)?.let {
                    // Находим все вью уведомлений в родительском макете
                    for (i in 0..it.childCount) {
                        val childView = if (it.getChildAt(i) is Alert) it.getChildAt(i) as Alert else null
                        if (childView != null && childView.windowToken != null) {
                            ViewCompat.animate(childView).alpha(0f).withEndAction(getRemoveViewRunnable(childView))
                        }
                    }
                }
            }
        }

        /**
         * Скрывает отображаемое уведомление, если такое есть
         */
        @JvmStatic
        fun hide() {
            activityWeakReference?.get()?.let {
                clearCurrent(it)
            }
        }

        /**
         * Проверяет отображается ли в данный момент уведомление
         *
         * @return True если уведомление отображается
         */
        @JvmStatic
        val isShowing: Boolean
            get() {
                var isShowing = false

                activityWeakReference?.get()?.let {
                    isShowing = it.findViewById<View>(R.id.llAlertBackground) != null
                }

                return isShowing
            }

        private fun getRemoveViewRunnable(childView: Alert?): Runnable {
            return Runnable {
                childView?.let {
                    (childView.parent as? ViewGroup)?.removeView(childView)
                }
            }
        }
    }

    /**
     * Ссылка на Alert, для поддержки, конфигурирования
     */
    private var alert: Alert? = null

    /**
     * Получение DecorView активити
     *
     * @return Decor View активити, из которой был вызван Alerter
     */
    private val activityDecorView: ViewGroup?
        get() {
            var decorView: ViewGroup? = null

            activityWeakReference?.get()?.let {
                decorView = it.window.decorView as ViewGroup
            }
            return decorView
        }

    /**
     * Показывает Alert после его создания
     *
     * @return Экземпляр класса Alert, который может быть изменен или скрыт
     */
    fun show(): Alert? {
        activityWeakReference?.get()?.let {
            it.runOnUiThread {
                // Добавляем новый Alert к иерархии вью
                activityDecorView?.addView(alert)
            }
        }
        return alert
    }

    /**
     * Добавляет дополнительный отступ у Alert, для отступа от Toolbar
     */
    fun addAdditionalMargin(marginSize: Int): Alerter {
        alert?.addAdditionalMargin(marginSize)
        return this
    }

    /**
     * Устанавливает текст уведомления
     *
     * @param textId id текстового ресурса
     * @return This Alerter
     */
    fun setText(@StringRes textId: Int): Alerter {
        alert?.setText(textId)
        return this
    }

    /**
     * Устанавливает текст уведомления
     *
     * @param text CharSequence текста
     * @return This Alerter
     */
    fun setText(text: CharSequence): Alerter {
        alert?.setText(text)
        return this
    }

    /**
     * Устанавливает Typeface текста уведомления
     *
     * @param typeface Typeface
     * @return This Alerter
     */
    fun setTextTypeface(typeface: Typeface): Alerter {
        alert?.setTextTypeface(typeface)
        return this
    }

    /**
     * Устанавливает TextAppearance текста уведомления
     *
     * @param textAppearance id ресурса стиля
     * @return This Alerter
     */
    fun setTextAppearance(@StyleRes textAppearance: Int): Alerter {
        alert?.setTextAppearance(textAppearance)
        return this
    }

    /**
     * Устанавливает фоновый Drawable
     *
     * @param drawable Drawable
     * @return This Alerter
     */
    fun setBackgroundDrawable(drawable: Drawable): Alerter {
        alert?.setAlertBackgroundDrawable(drawable)
        return this
    }

    /**
     * Устанавливает фоновый Drawable
     *
     * @param drawableResId Id Drawable ресурса
     * @return This Alerter
     */
    fun setBackgroundResource(@DrawableRes drawableResId: Int): Alerter {
        alert?.setAlertBackgroundResource(drawableResId)
        return this
    }

    /**
     * Устанавливает иконку уведомления
     *
     * @param iconId Id ресурса изображения
     * @return This Alerter
     */
    fun setIcon(@DrawableRes iconId: Int): Alerter {
        alert?.setIcon(iconId)
        return this
    }

    /**
     * Устанавливает иконку уведомления
     *
     * @param bitmap Bitmap, используемый как иконка.
     * @return This Alerter
     */
    fun setIcon(bitmap: Bitmap): Alerter {
        alert?.setIcon(bitmap)
        return this
    }

    /**
     * Устанавливает цветовой фильтр иконки уведомления
     *
     * @param color Color int
     * @return This Alerter
     */
    fun setIconColorFilter(@ColorInt color: Int): Alerter {
        alert?.setIconColorFilter(color)
        return this
    }

    /**
     * Устанавливает цветовой фильтр иконки уведомления
     *
     * @param colorFilter ColorFilter
     * @return This Alerter
     */
    fun setIconColorFilter(colorFilter: ColorFilter): Alerter {
        alert?.setIconColorFilter(colorFilter)
        return this
    }

    /**
     * Устанавливает цветовой фильтр иконки уведомления
     *
     * @param color Color int
     * @param mode PorterDuff.Mode
     * @return This Alerter
     */
    fun setIconColorFilter(@ColorInt color: Int, mode: PorterDuff.Mode): Alerter {
        alert?.setIconColorFilter(color, mode)
        return this
    }

    /**
     * Скрывает иконку уведомления
     *
     * @return This Alerter
     */
    fun hideIcon(): Alerter {
        alert?.showIcon = false
        return this
    }

    /**
     * Устанавливает onClickListener для уведомления
     *
     * @param onClickListener The onClickListener for the Alert
     * @return This Alerter
     */
    fun setOnClickListener(onClickListener: View.OnClickListener): Alerter {
        alert?.setOnClickListener(onClickListener)
        return this
    }

    /**
     * Устанавливает продолжительность отображения уведомления
     *
     * @param milliseconds Продолжительность в миллисекундах
     * @return This Alerter
     */
    fun setDuration(milliseconds: Long): Alerter {
        alert?.duration = milliseconds
        return this
    }

    /**
     * Включает или выключает анимацию пульсации иконки
     *
     * @param pulse True если следует включить анимацию пульсации
     * @return This Alerter
     */
    fun enableIconPulse(pulse: Boolean): Alerter {
        alert?.pulseIcon(pulse)
        return this
    }

    /**
     * Устанавливает следует показывать иконку или нет
     *
     * @param showIcon True чтобы показывать, иначе false
     * @return This Alerter
     */
    fun showIcon(showIcon: Boolean): Alerter {
        alert?.showIcon = showIcon
        return this
    }

    /**
     * Включает или выключает бесконечную продолжительность отображения уведомления
     *
     * @param infiniteDuration True если следует включить бесконечное отображение
     * @return This Alerter
     */
    fun enableInfiniteDuration(infiniteDuration: Boolean): Alerter {
        alert?.setEnableInfiniteDuration(infiniteDuration)
        return this
    }

    /**
     * Устанавливает слушатель события появления уведомления на экране
     *
     * @param listener OnShowAlertListener
     * @return This Alerter
     */
    fun setOnShowListener(listener: OnShowAlertListener): Alerter {
        alert?.setOnShowListener(listener)
        return this
    }

    /**
     * Устанавливает слушатель события скрытия уведомления
     *
     * @param listener OnHideAlertListener
     * @return This Alerter
     */
    fun setOnHideListener(listener: OnHideAlertListener): Alerter {
        alert?.onHideListener = listener
        return this
    }

    /**
     * Включает swipe to dismiss
     *
     * @return This Alerter
     */
    fun enableSwipeToDismiss(): Alerter {
        alert?.enableSwipeToDismiss()
        return this
    }

    /**
     * Включает или выключает вибрационный отклик на появление уведомления
     *
     * @param enable True для включения, False для выключения
     * @return This Alerter
     */
    fun enableVibration(enable: Boolean): Alerter {
        alert?.setVibrationEnabled(enable)
        return this
    }

    /**
     * Выключает обработку прикосновений вне области уведомления
     *
     * @return This Alerter
     */
    fun disableOutsideTouch(): Alerter {
        alert?.disableOutsideTouch()
        return this
    }

    /**
     * Устанавливает может ли пользователь смахнуть уведомление
     *
     * @param dismissible true если пользователь может смахнуть уведомление
     * @return This Alerter
     */
    fun setDismissible(dismissible: Boolean): Alerter {
        alert?.isDismissible = dismissible
        return this
    }

    /**
     * Устанавливает анимацию появления уведомления
     *
     * @param animation анимация появления уведомления
     * @return This Alerter
     */
    fun setEnterAnimation(@AnimRes animation: Int): Alerter {
        alert?.enterAnimation = AnimationUtils.loadAnimation(alert?.context, animation)
        return this
    }

    /**
     * Устанавливает анимацию скрытия уведомления
     *
     * @param animation анимация скрытия уведомления
     * @return This Alerter
     */
    fun setExitAnimation(@AnimRes animation: Int): Alerter {
        alert?.exitAnimation = AnimationUtils.loadAnimation(alert?.context, animation)
        return this
    }

    /**
     * Создает WeakReference для ссылки на активити
     *
     * @param activity The calling Activity
     */
    private fun setActivity(activity: Activity) {
        activityWeakReference = WeakReference(activity)
    }
}
