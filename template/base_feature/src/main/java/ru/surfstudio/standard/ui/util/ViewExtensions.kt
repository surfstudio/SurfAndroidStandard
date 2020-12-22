package ru.surfstudio.standard.ui.util

import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.IntegerRes
import androidx.annotation.StringRes
import ru.surfstudio.android.animations.anim.AnimationUtil
import ru.surfstudio.android.animations.anim.fadeIn
import ru.surfstudio.android.animations.anim.fadeOut
import ru.surfstudio.android.utilktx.ktx.ui.view.actionIfChanged

/**
 * Выполнить действие [action], если [data] была изменена с момента последнего вызова этого метода.
 *
 * Если `data == null` или `data == previous_data` -> [action] не будет вызван.
 * */
fun <T : Any, V : View> V.performIfChanged(data: T?, action: V.(T) -> Unit) {
    actionIfChanged(data, { if (data != null) action(data) })
}

/**
 * см. [performIfChanged]
 */
fun <T1 : Any, T2 : Any, V : View> V.performIfChanged(data1: T1?, data2: T2?, action: V.(T1, T2) -> Unit) {
    actionIfChanged(data1, data2, { _, _ -> if (data1 != null && data2 != null) action(data1, data2) })
}

/**
 * см. [performIfChanged]
 */
fun <T1 : Any, T2 : Any, T3 : Any, V : View> V.performIfChanged(data1: T1?, data2: T2?, data3: T3?, action: V.(T1, T2, T3) -> Unit) {
    actionIfChanged(data1, data2, { _, _ -> if (data1 != null && data2 != null && data3 != null) action(data1, data2, data3) })
}

/**
 * Функция-расширение для удобного получения размеров в px внутри [View].
 */
fun View.getDimensionPixelSize(@DimenRes dimenResId: Int) =
        resources.getDimensionPixelSize(dimenResId)

/**
 * Функция-расширение для удобного получения целого числа из ресурсов внутри [View].
 */
fun View.getInteger(@IntegerRes id: Int): Int =
        context.resources.getInteger(id)

/**
 * Функция-расширение для удобного получения [Drawable] из ресурсов внутри [View].
 */
fun View.getDrawable(@DrawableRes drawableRes: Int): Drawable? =
        context.getDrawable(drawableRes)

/**
 * Функция-расширение для удобного получения строки из ресурсов внутри [View].
 */
fun View.getString(@StringRes stringRes: Int, vararg formatArgs: Any): String =
        context.getString(stringRes, *formatArgs)

/**
 * Плавно скрывающая или показывающая View анимация.
 *
 * @param condition условие, в зависимости от которого view будет показана или скрыта
 */
fun View.fadeOutIf(
        condition: Boolean,
        duration: Long = AnimationUtil.ANIM_LEAVING,
        defaultAlpha: Float = 1.0f,
        endAction: (() -> Unit)? = null
) {
    if (condition) {
        if (visibility != View.GONE) {
            fadeOut(defaultAlpha = defaultAlpha, duration = duration, endAction = endAction)
        }
    } else {
        if (visibility != View.VISIBLE) {
            fadeIn(defaultAlpha = defaultAlpha, duration = duration, endAction = endAction)
        }
    }
}