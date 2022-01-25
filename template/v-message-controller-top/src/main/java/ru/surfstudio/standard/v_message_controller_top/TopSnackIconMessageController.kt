package ru.surfstudio.standard.v_message_controller_top

import android.content.Context
import android.graphics.Color
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import ru.surfstudio.android.core.ui.provider.ActivityProvider

/**
 * Класс позволяющий отображать уведомления вверху текущей активити.
 */
class TopSnackIconMessageController(private val activityProvider: ActivityProvider) : IconMessageController {

    /**
     * Закрывает отображаемое в данный момент уведомление
     */
    override fun closeSnack() {
        Alerter.hide()
    }

    /**
     * Показывает уведомление с заданными параметрами
     *
     * @param stringId - id строкового ресурса текста уведомления
     * @param backgroundColor - id ресурса цвета фона уведомления
     * @param iconResId - id ресурса изображения иконки уведомления
     * @param durationMillis - продолжительность отображения уведомления в миллисекундах
     * @param listener - лямбда, вызываемая по клику на показываемое уведомление
     */
    override fun show(
        @StringRes stringId: Int,
        @ColorRes backgroundColor: Int?,
        @DrawableRes iconResId: Int?,
        durationMillis: Long,
        listener: (view: View) -> Unit
    ) {

        val message = activityProvider.get().resources.getString(stringId)
        show(message, backgroundColor, iconResId, durationMillis, listener)
    }

    /**
     * то же, что @see [show] только принимает не id строкового ресурса, а [String]
     */
    override fun show(
        message: String,
        @ColorRes backgroundColor: Int?,
        @DrawableRes iconResId: Int?,
        durationMillis: Long,
        listener: (view: View) -> Unit
    ) {
        Alerter.hide()

        val activity = activityProvider.get()
        val backgroundColorTintRes = backgroundColor ?: R.color.error_color_snackbar
        val backgroundColorTint = ContextCompat.getColor(activity as Context, backgroundColorTintRes)
        val backgroundRes = R.drawable.bg_snack
        val background = ContextCompat
            .getDrawable(activity, backgroundRes)
            ?.apply { DrawableCompat.setTint(this, backgroundColorTint) }

        Alerter.create(activity).apply {
            setText(message)
            setDuration(durationMillis)
            setOnClickListener { view -> listener(view) }
            background?.let(::setBackgroundDrawable)
            iconResId?.let(::setIcon)
            showIcon(iconResId != null)
            enableSwipeToDismiss()
            show()
        }
    }
}
