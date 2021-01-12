package ru.surfstudio.standard.ui.dialog.simple

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.impls.event.hub.ScreenEventHub
import ru.surfstudio.android.core.mvi.impls.ui.dialog.standard.EMPTY_RES
import ru.surfstudio.android.mvp.dialog.simple.CoreSimpleDialogFragment
import ru.surfstudio.android.template.base_feature.R

/**
 * Вью простого диалога с заголовком, сообщением, позитивной и негативной кнопкой
 *
 * Живет в родительском dagger-scope и содержит в себе родительский EventHub
 * для отсылки событий закрытия и нажатия на кнопки.
 *
 * Для добавления этого диалога на экран, необходимо унаследовать родительский компонент от [SimpleEventHubDialogComponent]
 */
class SimpleDialogView<E : Event> : CoreSimpleDialogFragment() {

    override fun getName(): String = "SimpleDialogView"

    private lateinit var hub: ScreenEventHub<E>
    private lateinit var route: SimpleDialogRoute<E>

    private var positiveButtonEvent: E? = null
    private var negativeButtonEvent: E? = null
    private var dismissEvent: E? = null

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // todo fix typo exception (PersistentScopeStorage.java:67)
        hub = getScreenComponent(SimpleEventHubDialogComponent::class.java).screenHub() as ScreenEventHub<E>
        route = SimpleDialogRoute(arguments!!)

        isCancelable = route.isCancelable

        positiveButtonEvent = route.positiveButtonEvent
        negativeButtonEvent = route.negativeButtonEvent
        dismissEvent = route.dismissEvent
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val title = if (route.titleRes != EMPTY_RES) {
            getString(route.titleRes)
        } else {
            route.title
        }
        val message = if (route.messageRes != EMPTY_RES) {
            getString(route.messageRes)
        } else {
            route.message
        }
        val positiveButtonText = if (route.positiveBtnTextRes != EMPTY_RES) {
            route.positiveBtnTextRes
        } else {
            R.string.simple_dialog_ok
        }
        val negativeButtonText = if (route.negativeBtnTextRes != EMPTY_RES) {
            route.negativeBtnTextRes
        } else {
            R.string.simple_dialog_cancel
        }
        return MaterialAlertDialogBuilder(requireContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveButtonText, positiveListener)
                .apply {
                    if (route.isNegativeButtonVisible) {
                        setNegativeButton(negativeButtonText, negativeListener)
                    }
                }
                .create()
                .apply {
                    // красим кнопку только когда диалог будет показан
                    // потому что до этого момента кнопка может быть не создана
                    setOnShowListener {
                        val color = ContextCompat.getColor(requireContext(), route.positiveBtnColorRes)
                        getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(color)
                    }
                }
    }

    private val positiveListener = DialogInterface.OnClickListener { _, _ ->
        positiveButtonEvent?.let { hub.emit(it) }
    }

    private val negativeListener = DialogInterface.OnClickListener { _, _ ->
        negativeButtonEvent?.let { hub.emit(it) }
    }

    override fun onDismiss(dialog: DialogInterface) {
        dismissEvent?.let { hub.emit(it) }
        super.onDismiss(dialog)
    }
}