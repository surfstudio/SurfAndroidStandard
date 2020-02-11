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
class StandardReactDialogView<E : Event> : CoreSimpleDialogFragment() {


    private lateinit var hub: ScreenEventHub<E>

    private var positiveButtonEvent: E? = null
    private var negativeButtonEvent: E? = null
    private var dismissEvent: E? = null

    private val positiveListener = DialogInterface.OnClickListener { _, _ ->
        tryEmit(positiveButtonEvent)
    }

    private val negativeListener = DialogInterface.OnClickListener { _, _ ->
        tryEmit(negativeButtonEvent)
    }

    override fun getName(): String = "StandardReactDialogView"

    override fun inject() {
        //Do nothing
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hub = getScreenComponent(EventHubDialogComponent::class.java).screenHub() as ScreenEventHub<E>
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        val route = StandardReactDialogRoute<E>(arguments ?: Bundle.EMPTY)

        isCancelable = route.isCancelable

        positiveButtonEvent = route.positiveButtonEvent
        negativeButtonEvent = route.negativeButtonEvent
        dismissEvent = route.dismissEvent

        val title = if (route.titleRes != EMPTY_RES) getString(route.titleRes) else route.title
        val message = if (route.messageRes != EMPTY_RES) getString(route.messageRes) else route.message

        return AlertDialog.Builder(requireContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButtonSafe(route.positiveBtnTextRes, positiveListener)
                .setNegativeButtonSafe(route.negativeBtnTextRes, negativeListener)
                .create()
                .apply {
                    // paint button only after the dialog is shown
                    setOnShowListener {
                        setButtonColorSafe(AlertDialog.BUTTON_POSITIVE, route.positiveBtnColorRes)
                        setButtonColorSafe(AlertDialog.BUTTON_NEGATIVE, route.negativeBtnColorRes)
                    }
                }
    }

    override fun onDismiss(dialog: DialogInterface) {
        tryEmit(dismissEvent)
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

    private fun tryEmit(event: E?) {
        event?.let(hub::emit)
    }
}
