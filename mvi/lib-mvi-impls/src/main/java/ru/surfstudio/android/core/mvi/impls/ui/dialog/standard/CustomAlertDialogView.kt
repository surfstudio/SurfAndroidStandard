package ru.surfstudio.android.core.mvi.impls.ui.dialog.standard

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.impls.event.hub.ScreenEventHub
import ru.surfstudio.android.core.mvi.impls.ui.dialog.EventHubDialogComponent
import ru.surfstudio.android.mvp.dialog.simple.CoreSimpleDialogFragment

/**
 * Simple alert dialog view with title, messages and two buttons.
 *
 * It lives in parent dagger scope and holds reference to parent [ScreenEventHub]
 * to emit events, which should be consumed by parent.
 *
 * To emit events from this dialog, you need to inherit parent component from [EventHubDialogComponent]
 */
class CustomAlertDialogView<E : Event> : CoreSimpleDialogFragment() {

    override fun getName(): String = "CustomAlertDialogView"

    private lateinit var hub: ScreenEventHub<E>
    private lateinit var route: CustomAlertDialogRoute<E>

    private var positiveButtonEvent: E? = null
    private var negativeButtonEvent: E? = null
    private var dismissEvent: E? = null

    private val positiveListener = DialogInterface.OnClickListener { _, _ ->
        positiveButtonEvent?.let { hub.emit(it) }
    }

    private val negativeListener = DialogInterface.OnClickListener { _, _ ->
        negativeButtonEvent?.let { hub.emit(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hub = getScreenComponent(EventHubDialogComponent::class.java).screenHub() as ScreenEventHub<E>
        route = CustomAlertDialogRoute(arguments!!)

        isCancelable = route.isCancelable

        positiveButtonEvent = route.positiveButtonEvent
        negativeButtonEvent = route.negativeButtonEvent
        dismissEvent = route.dismissEvent
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val titleRes = route.titleRes
        val title = if (titleRes != EMPTY_RES) getString(titleRes) else route.title

        val messageRes = route.messageRes
        val message = if (messageRes != EMPTY_RES) getString(messageRes) else route.message

        val positiveText = route.positiveBtnTextRes
        val negativeText = route.negativeBtnTextRes

        val positiveColor = route.positiveBtnColorRes
        val negativeColor = route.negativeBtnColorRes

        return AlertDialog.Builder(requireContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButtonSafe(positiveText, positiveListener)
                .setNegativeButtonSafe(negativeText, negativeListener)
                .create()
                .apply {
                    // красим кнопку только когда диалог будет показан
                    // потому что до этого момента кнопка может быть не создана
                    setOnShowListener {
                        setButtonColorSafe(AlertDialog.BUTTON_NEGATIVE, negativeColor)
                        setButtonColorSafe(AlertDialog.BUTTON_POSITIVE, positiveColor)
                    }
                }
    }

    override fun onDismiss(dialog: DialogInterface) {
        dismissEvent?.let { hub.emit(it) }
        super.onDismiss(dialog)
    }

    private fun AlertDialog.Builder.setPositiveButtonSafe(
            @StringRes textResId: Int,
            onClickListener: DialogInterface.OnClickListener
    ): AlertDialog.Builder =
            if (textResId != EMPTY_RES) setPositiveButton(textResId, onClickListener) else this

    private fun AlertDialog.Builder.setNegativeButtonSafe(
            @StringRes textResId: Int?,
            onClickListener: DialogInterface.OnClickListener
    ): AlertDialog.Builder =
            if (textResId != null) setNegativeButton(textResId, onClickListener) else this

    private fun AlertDialog.setButtonColorSafe(buttonType: Int, @ColorRes colorResId: Int) {
        if (colorResId != EMPTY_RES) {
            val color = ContextCompat.getColor(context, colorResId)
            getButton(buttonType)?.setTextColor(color)
        }
    }
}
